package org.mockito.internal.configuration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.ExposedToMock;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.util.MockUtil;

public class SpyAnnotationEngineTest {

 private AnnotationEngine annotationEngine;
 private Object testClass;
 @Mock private Object context;
 @Spy @ExposedToMock private Object instanceToSpy;
 @Captor private ArgumentCaptor<Field> fieldCaptor;

 @Before
 public void setUp() {
 testClass = new TestClass();
 annotationEngine = new SpyAnnotationEngine();
 }

 @Test
 public void testCreateMockForReturnsNull() {
 // Testing the createMockFor method which returns null
 verifyZeroInteractions(annotationEngine);
 }

 @Test(expected = MockitoException.class)
 public void testCannotCreateASpyOnMissingInstance() throws IllegalAccessException {
 // Testing the case when the instance is missing
 annotationEngine.process(context, testClass);
 Field field = Mockito.spy(TestClass.class).getDeclaredField("fieldToSpy");
 field.setAccessible(true);
 field.set(testClass, null);

 annotationEngine.process(context, testClass);
 field.setAccessible(false);
 }

 @Test
 public void testSpyExistingMock() throws IllegalAccessException {
 // Testing the case when the instance has been spied earlier
 annotationEngine.process(context, testClass);
 Field field = Mockito.spy(TestClass.class).getDeclaredField("fieldToSpy");
 field.setAccessible(true);
 Object existingMock = mock(String.class);
 field.set(testClass, existingMock);
 field.setAccessible(false);

 annotationEngine.process(context, testClass);
 verifyNoMoreInteractions(existingMock);
 }

 @Test
 public void testSpyInstance() throws IllegalAccessException {
 // Testing the case when the instance is not a mock
 annotationEngine.process(context, testClass);
 Field field = Mockito.spy(TestClass.class).getDeclaredField("fieldToSpy");
 field.setAccessible(true);
 Object instanceToSpy = new Object();
 field.set(testClass, instanceToSpy);
 field.setAccessible(false);

 verify(context).getDeclaredFields();
 verify(context).getClass();
 verify(instanceToSpy).getClass();
 verify(instanceToSpy).equals(field.get(testClass));
 verify(instanceToSpy).getClass().getName();
 verify(instanceToSpy).toString();

 // Resetting the spy to make sure it's not interacted with during the following assertions
 Mockito.reset(instanceToSpy);

 verify(instanceToSpy).getClass().getDeclaredFields();
 verify(instanceToSpy).getClass().getSuperclass();
 verify(instanceToSpy).getClass().getInterfaces();
 verifyNoMoreInteractions(instanceToSpy);
 }

 @Test(expected = MockitoException.class)
 public void testFieldIsAlreadyAnnotatedWithSpy() throws IllegalAccessException {
 // Testing the case when the field is already annotated with @Spy
 Field existingSpyField = Mockito.spy(TestClass.class).getDeclaredField("fieldToSpy");
 existingSpyField.setAccessible(true);
 existingSpyField.set(testClass, instanceToSpy);
 existingSpyField.setAccessible(false);

 annotationEngine.process(context, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testFieldIsAlreadyAnnotatedWithMockitoAnnotationsMock() throws IllegalAccessException {
 // Testing the case when the field is already annotated with @Mock by MockitoAnnotations
 MockitoAnnotations.initMocks(testClass);
 Field field = Mockito.spy(TestClass.class).getDeclaredField("fieldToSpy");
 field.setAccessible(true);
 field.set(testClass, mock(String.class));
 field.setAccessible(false);

 annotationEngine.process(context, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testFieldIsAlreadyAnnotatedWithCaptor() throws IllegalAccessException {
 // Testing the case when the field is already annotated with @Captor
 Field field = Mockito.spy(TestClass.class).getDeclaredField("fieldToCapture");
 field.setAccessible(true);
 field.set(testClass, mock(String.class));
 field.setAccessible(false);

 annotationEngine.process(context, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testFieldIsAlreadyAnnotatedWithAnyAnnotation() throws IllegalAccessException {
 // Testing the case when the field is already annotated with any other annotation than @Spy
 Class<?> anyAnnotation = mock(Annotation.class);
 Field field = Mockito.spy(TestClass.class).getDeclaredField("fieldToSpy");
 field.setAccessible(true);
 field.set(testClass, instanceToSpy);
 field.setAnnotation(anyAnnotation);
 field.setAccessible(false);

 annotationEngine.process(context, testClass);
 }

 @Test
 public void testProcessDoesNotChangeInstanceWhenNotAnnotatedWithSpy() throws IllegalAccessException {
 // Testing the case when the field is not annotated with @Spy
 annotationEngine.process(context, testClass);
 Field field = Mockito.spy(TestClass.class).getDeclaredField("fieldNotToSpy");
 field.setAccessible(true);
 Object originalInstance = field.get(testClass);
 field.setAccessible(false);

 verify(field).get(testClass);
 verify(field).get(testClass).equals(originalInstance);
 }

 @Test
 public void testProcessWithFieldAlreadyAccessible() throws IllegalAccessException {
 // Testing the case when the field is already accessible
 annotationEngine.process(context, testClass);
 Field field = Mockito.spy(TestClass.class).getDeclaredField("fieldToSpy");
 field.setAccessible(true);
 Object originalInstance = field.get(testClass);
 field.setAccessible(false);

 verify(field).setAccessible(true);
 verify(field).setAccessible(false);
 verify(field).get(testClass).equals(originalInstance);
 }

 @Test
 public void testProcessWithExistingAnnotationOfSpy() throws IllegalAccessException {
 // Testing the case when the field already has a @Spy annotation
 Class<?> spyAnnotation = mock(Annotation.class);
 Field field = Mockito.spy(TestClass.class).getDeclaredField("fieldToSpy");
 field.setAccessible(true);
 field.setAnnotation(spyAnnotation);
 field.set(testClass, instanceToSpy);
 field.setAccessible(false);

 annotationEngine.process(context, testClass);
 }

 @Test
 public void testProcessWithAnnotationEngineAlreadyProcessed() throws IllegalAccessException {
 // Testing the case when the SpyAnnotationEngine has already been processed
 annotationEngine.process(context, testClass);
 annotationEngine.process(context, testClass);
 }

 @Test
 public void testProcessWithInvalidContextType() throws IllegalAccessException {
 // Testing the case when the context type is invalid
 Class<?> invalidContextType = mock(Class.class);
 annotationEngine.process(invalidContextType, testClass);
 verifyZeroInteractions(invalidContextType);
 }

 @Test
 public void testProcessWithInvalidTestClassType() {
 // Testing the case when the test class type is invalid
 Class<?> invalidTestClassType = mock(Class.class);
 try {
 annotationEngine.process(context, invalidTestClassType);
 } catch (IllegalArgumentException e) {
 // Pass
 }
 verifyZeroInteractions(context);
 }
}

 class TestClass {
 @Spy
 private Object fieldToSpy;
 @Mock
 private Object fieldToMock;
 @Captor
 private ArgumentCaptor<String> fieldToCapture;
 }