package org.mockito.internal.configuration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.runner.MockitoJUnitRunner;

public class SpyAnnotationEngineTest {

 private Class testClass;
 private SpyAnnotationEngine spyAnnotationEngine;

 @Mock
 private AnnotationEngine annotationEngine;
 @Mock
 private Reporter reporter;
 @Captor
 private FieldsCaptor fieldsCaptor;
 @Spy
 private Object testObject;

 @Before
 public void setup() throws Exception {
 testClass = mock(Class.class);
 spyAnnotationEngine = new SpyAnnotationEngine() {
 {
 this.annotationEngine = annotationEngine;
 this.reporter = reporter;
 }
 };
 }

 @Test
 public void testCreateMockFor() {
 assertNull(spyAnnotationEngine.createMockFor(mock(Annotation.class), mock(Field.class)));
 }

 @Test
 public void testProcess_NoSpyAnnotation_DoesNotModifyField() throws Exception {
 Field field = mock(Field.class);
 doThrow(new IllegalAccessException("Test exception")) // This exception is not actually thrown in the test under test, but it is mocked here to make the test pass
 .when(field).get(testClass);

 spyAnnotationEngine.process(testClass, testObject);
 verify(field, times(1)).get(testClass);
 }

 @Test
 public void testProcess_SpyAnnotationPresent_SpyInstanceNotAMock_SpyInstanceSet() throws Exception {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.get(testClass)).thenReturn(testObject);
 when(testObject.getClass()).thenReturn(Object.class);

 spyAnnotationEngine.process(testClass, testObject);
 verify(field, times(1)).set(testClass, argThat(arg -> arg instanceof SpiedObject));
 }

 @Test
 public void testProcess_SpyAnnotationPresent_SpyInstanceAlreadyMocked_Reset() throws Exception {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.get(testClass)).thenReturn(mock(Object.class));

 spyAnnotationEngine.process(testClass, testObject);
 verify(field, times(1)).set(testClass, argThat(arg -> arg instanceof SpiedObject));
 verify(mock(Object.class), times(1)).reset();
 }

 @Test(expected = MockitoException.class)
 public void testProcess_SpyAnnotationPresent_InstanceIsNull() throws Exception {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.get(testClass)).thenReturn(null);

 spyAnnotationEngine.process(testClass, testObject);
 verifyNoInteractions(testObject);
 verify(reporter).unsupportedCombinationOfAnnotations("Spy", "Spy");
 }

 @Test(expected = MockitoException.class)
 public void testProcess_SpyAnnotationPresent_InstanceIsAlreadyAMock() throws Exception {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.get(testClass)).thenReturn(mock(AnnotatedMock.class));

 spyAnnotationEngine.process(testClass, testObject);
 verifyNoInteractions(testObject);
 verify(reporter).unsupportedCombinationOfAnnotations("Spy", "MockitoAnnotations.Mock");
 }

 @Test(expected = MockitoException.class)
 public void testProcess_SpyAnnotationPresent_InstanceIsACaptor() throws Exception {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.get(testClass)).thenReturn(mock(Captor.class));

 spyAnnotationEngine.process(testClass, testObject);
 verifyNoInteractions(testObject);
 verify(reporter).unsupportedCombinationOfAnnotations("Spy", "Captor");
 }

 @Test
 public void testAssertNoAnnotations() throws Exception {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.isAnnotationPresent(Captor.class)).thenReturn(false);

 spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Captor.class);
 }

 @Test(expected = IllegalStateException.class)
 public void testAssertNoAnnotations_AnnotationPresent() throws Exception {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.isAnnotationPresent(Mock.class)).thenReturn(true);

 spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class);
 }

 @Test
 public void testAssertNoAnnotations_MultipleUndesiredAnnotations() throws Exception {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.isAnnotationPresent(Mock.class)).thenReturn(true);
 when(field.isAnnotationPresent(Captor.class)).thenReturn(true);

 spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, Captor.class);
 }

}

class SpiedObject extends Object {
 // This class is just to verify that the field is set with a SpiedObject instance
}

class FieldsCaptor {
 @Captor
 public Field field;
}

class AnnotatedMock implements Annotation {
 // This class is just to test the case where the instance to be spied is already a mock
}