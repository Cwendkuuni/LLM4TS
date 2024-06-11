import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.injection.PropertyAndSetterInjection;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.util.collections.ListUtil;
import org.mockito.internal.util.reflection.FieldInitializationReport;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.SuperTypesLastSorter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertyAndSetterInjectionTest {

 private PropertyAndSetterInjection injectionStrategy;
 private Object mockObject;
 private Object targetObject;
 private Field targetField;
 private List<Object> mockCandidates;

 @Before
 public void setup() {
 mockObject = mock(Object.class);
 targetObject = new TestTarget();
 targetField = mock(Field.class);
 injectionStrategy = new PropertyAndSetterInjection();
 mockCandidates = Arrays.asList(new MockitoTestClass(), new TestSubClass(), mockObject);
 }

 @Test
 public void testProcessInjectionWithSetters() {
 // given
 Field injectMocksField = mock(Field.class);
 doReturn(targetField).when(targetObject).getClass().getField("injectMocksField");
 doReturn(targetObject).when(targetField).get(any());
 doReturn(mockCandidates).when(targetField).getAnnotations(Field.class);
 doReturn(true).when(injectionStrategy).processInjection(injectMocksField, targetObject, mockCandidates);

 // when
 Object injectedMock = injectionStrategy.processInjection(injectMocksField, targetObject, mockCandidates).getMock();

 // then
 assertEquals(MockitoTestClass.class, injectedMock.getClass());
 verify(targetField).set(targetObject, injectedMock);
 }

 @Test
 public void testProcessInjectionWithFields() {
 // given
 Field injectMocksField = mock(Field.class);
 doReturn(targetField).when(targetObject).getClass().getField("injectMocksField");
 doThrow(new NoSuchFieldException("Not a field")).when(targetField).get(any());
 doReturn(mockCandidates).when(targetField).getAnnotations(Field.class);
 doReturn(true).when(injectionStrategy).processInjection(injectMocksField, targetObject, mockCandidates);

 // when
 Object injectedMock = injectionStrategy.processInjection(injectMocksField, targetObject, mockCandidates).getMock();

 // then
 assertNull(injectedMock);
 }

 @Test
 public void testProcessInjectionWithNoCandidates() {
 // given
 Field injectMocksField = mock(Field.class);
 doReturn(targetField).when(targetObject).getClass().getField("injectMocksField");
 doReturn(Arrays.asList().toArray(new Object[0]))
 .when(targetField).getAnnotations(Field.class);
 doReturn(true).when(injectionStrategy).processInjection(injectMocksField, targetObject, mockCandidates);

 // when
 Object injectedMock = injectionStrategy.processInjection(injectMocksField, targetObject, mockCandidates).getMock();

 // then
 assertNull(injectedMock);
 }

 @Test(expected = IllegalAccessException.class)
 public void testProcessInjectionWithPrivateField() {
 // given
 Field injectMocksField = mock(Field.class);
 doReturn(targetField).when(targetObject).getClass().getDeclaredField("injectMocksField");
 doReturn(targetObject).when(targetField).get(any());
 doReturn(mockCandidates).when(targetField).getAnnotations(Field.class);
 doReturn(true).when(injectionStrategy).processInjection(injectMocksField, targetObject, mockCandidates);

 // when
 injectionStrategy.processInjection(injectMocksField, targetObject, mockCandidates);
 // then (exception thrown)
 }

 @Test(expected = MockitoException.class)
 public void testProcessInjectionWithFinalField() {
 // given
 Field injectMocksField = mock(Field.class);
 doReturn(targetField).when(targetObject).getClass().getDeclaredField("finalField");
 doReturn(targetObject).when(targetField).get(any());
 doReturn(mockCandidates).when(targetField).getAnnotations(Field.class);
 doReturn(true).when(injectionStrategy).processInjection(injectMocksField, targetObject, mockCandidates);

 // when, then (exception thrown)
 }

 @Test(expected = InvocationTargetException.class)
 public void testProcessInjectionWithInvalidConstructor() {
 // given
 Field injectMocksField = mock(Field.class);
 doReturn(targetField).when(targetObject).getClass().getDeclaredField("invalidConstructorField");
 doReturn(targetObject).when(targetField).get(any());
 doReturn(mockCandidates).when(targetField).getAnnotations(Field.class);
 doReturn(new FieldInitializationReport(new InvalidConstructorException())).when(new FieldInitializer(targetObject, injectMocksField)).initialize();
 doReturn(true).when(injectionStrategy).processInjection(injectMocksField, targetObject, mockCandidates);

 // when, then (exception thrown)
 }

 @Test
 public void testProcessInjectionWithSameType() {
 // given
 Field injectMocksField = mock(Field.class);
 doReturn(targetField).when(targetObject).getClass().getField("sameTypeField");
 doReturn(targetObject).when(targetField).get(any());
 doReturn(Arrays.asList(targetObject).toArray(new Object[0])).when(targetField).getAnnotations(Field.class);
 doReturn(true).when(injectionStrategy).processInjection(injectMocksField, targetObject, mockCandidates);

 // when
 Object injectedMock = injectionStrategy.processInjection(injectMocksField, targetObject, mockCandidates).getMock();

 // then
 assertEquals(targetObject, injectedMock);
 verify(targetField, never()).set(targetObject, injectedMock);
 }

 @Test
 public void testProcessInjectionWithDifferentTypes() {
 // given
 Field injectMocksField = mock(Field.class);
 doReturn(targetField).when(targetObject).getClass().getField("differentTypeField");
 doReturn(targetObject).when(targetField).get(any());
 doReturn(Arrays.asList(new TestDifferentClass(), mockObject).toArray(new Object[0]))
 .when(targetField).getAnnotations(Field.class);
 doReturn(new TestDifferentClass()).when(mockObject).getClass();
 doReturn(true).when(injectionStrategy).processInjection(injectMocksField, targetObject, mockCandidates);

 // when
 Object injectedMock = injectionStrategy.processInjection(injectMocksField, targetObject, mockCandidates).getMock();

 // then
 assertNotSame(targetObject, injectedMock);
 }

 private static class TestTarget {
 @InjectMocks
 private Field injectMocksField;
 private Field finalField;
 private Field sameTypeField;
 private Field differentTypeField;
 private Field invalidConstructorField;
 }

 private static class TestDifferentClass {
 }

 private static class InvalidConstructorException extends Exception {
 }
}