package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.exceptions.Reporter;
import org.mockito.Mockito;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.util.MockUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

 private AnnotationEngine annotationEngine;
 @Mock private Reporter reporter;
 @Spy private Object testClass;
 @Captor private ArgumentCaptor<Annotation> annotationCaptor;
 @Captor private ArgumentCaptor<Field> fieldCaptor;

 @Before
 public void setUp() {
 annotationEngine = new SpyAnnotationEngine();
 }

 @Test
 public void testCreateMockFor() {
 assertNull(annotationEngine.createMockFor(Mockito.mock(Annotation.class), null));
 }

 @Test(expected = MockitoException.class)
 public void testProcessNullContext() {
 annotationEngine.process(null, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcessNullTestClass() {
 annotationEngine.process(Mockito.mock(Class.class), null);
 }

 @Test
 public void testProcessWithoutSpyAnnotation() {
 Field field = Mockito.mock(Field.class);
 annotationEngine.process(Mockito.mock(Class.class), testClass);
 verifyZeroInteractions(testClass, reporter);
 }

 @Test
 public void testProcessWithSpyAnnotationAndNullInstance() {
 Field field = Mockito.mock(Field.class);
 field.when(field).isAnnotationPresent(Spy.class).thenReturn(true);
 annotationEngine.process(Mockito.mock(Class.class), testClass);
 verifyZeroInteractions(testClass, reporter);
 }

 @Test
 public void testProcessWithSpyAnnotationAndInstanceAlreadySpyed() {
 Field field = Mockito.mock(Field.class);
 Object instance = mock(Object.class);
 field.when(field).getName().thenReturn("testField");
 field.when(field).isAnnotationPresent(Spy.class).thenReturn(true);
 field.when(field).get(testClass).thenReturn(instance);
 field.when(field).isAccessible().thenReturn(true);
 annotationEngine.process(Mockito.mock(Class.class), testClass);
 Mockito.reset(instance);
 verify(instance).getClass();
 }

 @Test
 public void testProcessWithSpyAnnotationAndInstanceNotMock() {
 Field field = Mockito.mock(Field.class);
 Object instance = new Object();
 field.when(field).getName().thenReturn("testField");
 field.when(field).isAnnotationPresent(Spy.class).thenReturn(true);
 field.when(field).get(testClass).thenReturn(instance);
 field.when(field).isAccessible().thenReturn(true);
 annotationEngine.process(Mockito.mock(Class.class), testClass);
 field.when(field).get(testClass).thenReturn(instance);
 Mockito.verify(instance).getClass();
 Mockito.verify(instance).getClass(); // verify that spy was applied to the instance
 }

 @Test
 public void testProcessWithSpyAnnotationAndInstanceAlreadySpyedByAnotherClass() {
 Object instance = mock(Object.class);
 Mockito.spy(instance);
 Field field = Mockito.mock(Field.class);
 field.when(field).getName().thenReturn("testField");
 field.when(field).isAnnotationPresent(Spy.class).thenReturn(true);
 field.when(field).get(testClass).thenReturn(instance);
 field.when(field).isAccessible().thenReturn(true);
 annotationEngine.process(Mockito.mock(Class.class), testClass);
 verifyNoMoreInteractions(instance, reporter);
 }

 @Test(expected = MockitoException.class)
 public void testProcessWithSpyAnnotationAndInstanceMissing() {
 Field field = Mockito.mock(Field.class);
 field.when(field).getName().thenReturn("testField");
 field.when(field).isAnnotationPresent(Spy.class).thenReturn(true);
 field.when(field).get(testClass).thenReturn(null);
 field.when(field).isAccessible().thenReturn(true);
 annotationEngine.process(Mockito.mock(Class.class), testClass);
 }

 @Test
 public void testAssertNoAnnotations() {
 Field field = Mockito.mock(Field.class);
 annotationEngine.assertNoAnnotations(null, field, Mockito.mock(Annotation.class));
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testAssertNoAnnotationsWithNullAnnotation() {
 annotationEngine.assertNoAnnotations(null, fieldCaptor.capture().getValue(), Mockito.mock(Annotation.class));
 verify(reporter).unsupportedCombinationOfAnnotations("Spy", null);
 assertNull(fieldCaptor.getValue());
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testAssertNoAnnotationsWithCaptorAnnotation() {
 annotationEngine.assertNoAnnotations(Spy.class, fieldCaptor.capture().getValue(), Mockito.mock(Captor.class));
 verify(reporter).unsupportedCombinationOfAnnotations("Spy", "Captor");
 assertNull(fieldCaptor.getValue());
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testAssertNoAnnotationsWithMockAnnotation() {
 annotationEngine.assertNoAnnotations(Spy.class, fieldCaptor.capture().getValue(), Mockito.mock(Mock.class));
 verify(reporter).unsupportedCombinationOfAnnotations("Spy", "Mock");
 assertNull(fieldCaptor.getValue());
 }

 @Test(expected = java.lang.IllegalArgumentException.class)
 public void testAssertNoAnnotationsWithMockitoAnnotationsMockAnnotation() {
 annotationEngine.assertNoAnnotations(Spy.class, fieldCaptor.capture().getValue(), MockitoAnnotations.Mock.class);
 verify(reporter).unsupportedCombinationOfAnnotations("Spy", "org.mockito.MockitoAnnotations.Mock");
 assertNull(fieldCaptor.getValue());
 }
}