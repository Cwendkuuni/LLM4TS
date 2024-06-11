import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.reflection.Whitebox;
import org.mockito.mock.MockConstructor;
import org.mockito.stubbing.Answer;
import org.mockito.util.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.internal.util.reflection.FieldInitializationReport.Type.FIELD_INITIALIZATION_ERROR;

public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection propertyAndSetterInjection;
 private Object mockCandidates;
 private Object fieldInstanceNeedingInjection;
 private Class<?> awaitingInjectionClazz;
 private Set<Object> mocks;
 private List<Field> orderedInstanceFields;

 @Before
 public void setUp() {
 propertyAndSetterInjection = new PropertyAndSetterInjection();
 mockCandidates = new HashSet<>();
 fieldInstanceNeedingInjection = mock(Object.class);
 awaitingInjectionClazz = getClass();
 mocks = new HashSet<>();
 mockCandidates.add(mock(Object.class));
 mockCandidates.add(mock(Object.class));
 orderedInstanceFields = orderedInstanceFieldsFrom(awaitingInjectionClazz);
 }

 @Test
 public void testProcessInjection_Success() throws IllegalAccessException {
 // Given
 Field injectMocksField = ReflectionUtils.getField(PropertyAndSetterInjection.class, "injectMocksField");
 Object injectMocksFieldOwner = ReflectionUtils.getOwnerForField(injectMocksField, PropertyAndSetterInjection.class);

 // When
 boolean injectionOccurred = propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

 // Then
 assertTrue(injectionOccurred);
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjection_FieldInitializationError() throws IllegalAccessException {
 // Given
 doThrow(new InvocationTargetException(new MockConstructor(String.class), new Exception())).when(mockCandidates.iterator().next()).newInstance();

 Field injectMocksField = ReflectionUtils.getField(PropertyAndSetterInjection.class, "injectMocksField");
 Object injectMocksFieldOwner = ReflectionUtils.getOwnerForField(injectMocksField, PropertyAndSetterInjection.class);

 // When
 propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

 // Then
 verify(mockCandidates.iterator().next()).newInstance(); // This line will throw an exception
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjection_CannotInitializeForInjectMocksAnnotation() throws IllegalAccessException {
 // Given
 doThrow(new MockitoException("Cannot initialize for injectMocksAnnotation")).when(propertyAndSetterInjection).initializeInjectMocksField(anyField(), anyObject());

 Field injectMocksField = ReflectionUtils.getField(PropertyAndSetterInjection.class, "injectMocksField");
 Object injectMocksFieldOwner = ReflectionUtils.getOwnerForField(injectMocksField, PropertyAndSetterInjection.class);

 // When
 propertyAndSetterInjection.processInjection(injectMocksField, injectMocksFieldOwner, mockCandidates);

 // Then
 verify(propertyAndSetterInjection).new Reporter().cannotInitializeForInjectMocksAnnotation(anyString(), any(MockitoException.class));
 }

 @Test
 public void testInjectMockCandidates_Success() throws IllegalAccessException {
 // Given
 doAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 mockCandidates.remove(invocation.getArgument(0));
 return null;
 }
 }).when(mockCandidates).thenInject();

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, fieldInstanceNeedingInjection);

 // Then
 assertTrue(injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFields_NoMockFound() throws IllegalAccessException {
 // Given
 doReturn(Collections.emptyList()).when(propertyAndSetterInjection).orderedInstanceFieldsFrom(awaitingInjectionClazz);

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidates(awaitingInjectionClazz, mocks, fieldInstanceNeedingInjection);

 // Then
 verifyNoInteractions(mocks);
 }

 @Test
 public void testInjectMockCandidatesOnFields_SingleMockFound() throws IllegalAccessException {
 // Given
 doAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 Object mock = (Object) invocation.getArguments()[0];
 mocks.remove(mock);
 return mock;
 }
 }).when(mockCandidates).thenInject();

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, fieldInstanceNeedingInjection, false, orderedInstanceFields);

 // Then
 assertTrue(injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFields_MultipleMocksFound() throws IllegalAccessException {
 // Given
 doAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 Object mock = (Object) invocation.getArguments()[0];
 mocks.remove(mock);
 return mock;
 }
 }).when(mockCandidates).thenInject();

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, fieldInstanceNeedingInjection, false, orderedInstanceFields);

 // Then
 assertTrue(injectionOccurred);
 }

 @Test
 public void testInjectMockCandidatesOnFields_NoMockFoundWithMultiplePasses() throws IllegalAccessException {
 // Given
 doReturn(Collections.emptyList()).when(propertyAndSetterInjection).orderedInstanceFieldsFrom(awaitingInjectionClazz);

 // When
 boolean injectionOccurred = propertyAndSetterInjection.injectMockCandidatesOnFields(mocks, fieldInstanceNeedingInjection, false, orderedInstanceFields);

 // Then
 verifyNoInteractions(mocks);
 }
}