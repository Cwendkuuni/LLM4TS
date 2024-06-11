package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.DefaultInjectionEngine.MockCandidate;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.powermock.reflect.Whitebox2;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Set<Object> mocks;
 private Object testInstance;

 @Before
 public void setUp() {
 mocks = new HashSet<>();
 testInstance = new Object() {};
 injectionEngine = new DefaultInjectionEngine();
 }

 @Test
 public void testInjectMocksOnFields_NoMocksToInject() throws Exception {
 Set<Field> injectMocksFields = Arrays.asList(testInstance.getClass().getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toSet());
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testInstance);
 verifyZeroInteractions(mocks);
 }

 @Test
 public void testInjectMocksOnFields_OneMockToInject() throws Exception {
 Set<Field> injectMocksFields = Arrays.asList(testInstance.getClass().getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toSet());

 mockCandidateForTest(Mockito.class, mocks);
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testInstance);

 MockCandidate mockCandidate = mocks.iterator().next();
 Field injectMocksField = Whitebox.getField(testInstance, mockCandidate.getFieldName());
 Whitebox.setInternalValue(injectMocksField, mockCandidate.getMock());

 verify(mockCandidate.getMock()).someMethod();
 }

 @Test
 public void testInjectMocksOnFields_MultipleMocksToInject() throws Exception {
 Set<Field> injectMocksFields = Arrays.asList(testInstance.getClass().getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toSet());

 mockCandidateForTest(Mockito.class, mocks);
 mockCandidateForTest(AnotherMock.class, mocks);
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testInstance);

 MockCandidate mockCandidate1 = mocks.iterator().next();
 Whitebox.setInternalValue(testInstance.getClass().getDeclaredField("field1"), mockCandidate1.getMock());

 MockCandidate mockCandidate2 = mocks.iterator().next();
 Whitebox.setInternalValue(testInstance.getClass().getDeclaredField("field2"), mockCandidate2.getMock());

 verify(mockCandidate1.getMock()).someMethod();
 verify(mockCandidate2.getMock()).anotherMethod();
 verifyNoMoreInteractions(mocks);
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFields_NoMockCandidate() throws Exception {
 Set<Field> injectMocksFields = Arrays.asList(testInstance.getClass().getDeclaredFields())
 .stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class))
 .collect(Collectors.toSet());

 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testInstance);
 new Reporter().cannotInitializeForInjectMocksAnnotation("fieldName", new MockitoException());
 }

 @Test
 public void testInjectMocksOnFields_CircularDependency() throws Exception {
 Class<?> testClass = testInstance.getClass();
 Class<?> dependencyClass = testClass.getDeclaredFields()[0].getType();
 Field dependencyField = testClass.getDeclaredFields()[0];
 Field testField = testClass.getDeclaredFields()[1];

 initMocks(testInstance);
 mockCandidateForTest(dependencyClass, mocks);

 Whitebox.setInternalValue(testField, mock(dependencyClass));
 try {
 injectionEngine.injectMocksOnFields(Arrays.asList(testField, dependencyField).stream()
 .filter(Field::isAnnotationPresent)
 .collect(Collectors.toSet()), mocks, testInstance);
 } catch (MockitoException e) {
 // expected
 }

 verifyZeroInteractions(mocks);
 }

 private void mockCandidateForTest(Class<?> mockType, Set<Object> mocks) {
 Object mock = mock(mockType);
 mocks.add(mock);
 }

}