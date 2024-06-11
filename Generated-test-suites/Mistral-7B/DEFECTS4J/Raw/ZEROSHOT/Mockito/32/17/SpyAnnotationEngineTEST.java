package org.mockito.internal.configuration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Annotations;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.util.MockUtil;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

 private AnnotationEngine annotationEngine = new SpyAnnotationEngine();

 @Mock
 private Object testClass;

 @Spy
 @Capture
 private Object spiedField;

 @Test
 public void testCreateMockForNullAnnotationAndField() {
     // Test case for createMockFor(null, null)
     annotationEngine.createMockFor(null, null);
     // No action should be taken
     verifyZeroInteractions(annotationEngine);
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockForSpyAnnotationAndNullField() {
     // Test case for @Spy annotation present and field is null
     Field field = null;
     annotationEngine.process(getClass(), testClass);
     // Should throw MockitoException
     annotationEngine.createMockFor(Spy.class, field);
 }

 @Test
 public void testCreateMockForOtherAnnotationPresentAndField() {
     // Test case for other annotation present and field is not @Spy
     Field field = mock(Field.class);
     when(field.isAnnotationPresent(Spy.class)).thenReturn(false);
     when(field.isAnnotationPresent(Mock.class)).thenReturn(true);

     annotationEngine.process(getClass(), testClass);
     annotationEngine.createMockFor(Mock.class, field);

     verify(annotationEngine).createMockFor(Mock.class, any(Field.class));
     verifyNoInteractions(annotationEngine.process);
 }

 @Test
 public void testCreateMockForSpyAnnotationAndNullInstance() {
     // Test case for @Spy annotation present and instance is null
     Field field = mock(Field.class);
     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     when(field.get(testClass)).thenReturn(null);

     annotationEngine.process(getClass(), testClass);
     annotationEngine.createMockFor(Spy.class, field);

     verify(annotationEngine).createMockFor(Spy.class, field);
     verify(annotationEngine).reset(any(Object.class));
     verifyNoInteractions(annotationEngine.process);
 }

 @Test
 public void testCreateMockForSpyAnnotationAndNotMockInstance() {
     // Test case for @Spy annotation present and instance is not a Mockito mock
     Field field = mock(Field.class);
     Object instance = mock(Object.class);
     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     when(field.get(testClass)).thenReturn(instance);
     when(new MockUtil().isMock(instance)).thenReturn(false);

     annotationEngine.process(getClass(), testClass);
     annotationEngine.createMockFor(Spy.class, field);

     verify(annotationEngine).createMockFor(Spy.class, field);
     verify(field).set(testClass, spy(instance));
     verifyNoInteractions(annotationEngine.process);
 }

 @Test
 public void testCreateMockForSpyAnnotationAndMockInstance() {
     // Test case for @Spy annotation present and instance is a Mockito mock
     Field field = mock(Field.class);
     Object instance = mock(Object.class);
     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     when(field.get(testClass)).thenReturn(instance);
     when(new MockUtil().isMock(instance)).thenReturn(true);

     annotationEngine.process(getClass(), testClass);
     annotationEngine.createMockFor(Spy.class, field);

     verify(annotationEngine).createMockFor(Spy.class, field);
     verify(field).set(testClass, instance);
     verifyNoInteractions(annotationEngine.process);
 }

 @Test
 public void testProcessSpyAnnotationWithAccessibleField() {
     // Test case for @Spy annotation present and field is accessible
     Field field = mock(Field.class);
     Object instance = mock(Object.class);
     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     when(field.isAccessible()).thenReturn(true);
     when(field.get(testClass)).thenReturn(instance);

     annotationEngine.process(getClass(), testClass);

     verify(annotationEngine).process(getClass(), testClass);
     verify(annotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, Captor.class);
     verify(field).setAccessible(true);
 }

 @Test(expected = MockitoException.class)
 public void testProcessSpyAnnotationWithInaccessibleField() {
     // Test case for @Spy annotation present and field is inaccessible
     Field field = mock(Field.class);
     Object instance = mock(Object.class);
     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     when(field.isAccessible()).thenReturn(false);
     when(field.get(testClass)).thenReturn(instance);

     annotationEngine.process(getClass(), testClass);

     verify(annotationEngine).process(getClass(), testClass);
     verify(annotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, Captor.class);
     verify(field).setAccessible(true);

     try {
         annotationEngine.process(getClass(), testClass);
     } catch (IllegalAccessException e) {
         // Expected exception
     } finally {
         verify(field).setAccessible(true);
     }
 }

 @Test(expected = MockitoException.class)
 public void testProcessSpyAnnotationWithInstanceNull() {
     // Test case for @Spy annotation present and instance is null
     Field field = mock(Field.class);
     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     when(field.get(testClass)).thenReturn(null);

     annotationEngine.process(getClass(), testClass);

     verify(annotationEngine).process(getClass(), testClass);
     verify(annotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, Captor.class);

     try {
         annotationEngine.process(getClass(), testClass);
     } catch (MockitoException e) {
         // Expected exception
     }
 }

 @Test
 public void testProcessSpyAnnotationWithOtherAnnotationPresent() {
     // Test case for @Spy and other annotations present
     Field field = mock(Field.class);
     Object instance = mock(Object.class);
     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     when(field.isAnnotationPresent(Mock.class)).thenReturn(true);
     when(field.isAnnotationPresent(Captor.class)).thenReturn(true);

     annotationEngine.process(getClass(), testClass);

     verify(annotationEngine).process(getClass(), testClass);
     verify(annotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, Captor.class);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessSpyAnnotationWithUndesiredAnnotationPresent() {
     // Test case for @Spy and undesired annotation present
     Field field = mock(Field.class);
     Object instance = mock(Object.class);
     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     when(field.isAnnotationPresent(Mock.class)).thenReturn(false);
     when(field.isAnnotationPresent(Captor.class)).thenReturn(false);

     when(field.isAnnotationPresent( org.mockito.MockitoAnnotations.Mock.class)).thenReturn(true);

     annotationEngine.process(getClass(), testClass);

     verify(annotationEngine).process(getClass(), testClass);
     verify(annotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, Captor.class);

     try {
         annotationEngine.process(getClass(), testClass);
     } catch (IllegalArgumentException e) {
         // Expected exception
     }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessSpyAnnotationWithDuplicatedAnnotationPresent() {
     // Test case for @Spy and same annotation present
     Field field = mock(Field.class);
     Object instance = mock(Object.class);
     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
     when(field.isAnnotationPresent(Spy.class)).thenReturn(true);

     annotationEngine.process(getClass(), testClass);

     verify(annotationEngine).process(getClass(), testClass);
     verify(annotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, Captor.class);

     try {
         annotationEngine.process(getClass(), testClass);
     } catch (IllegalArgumentException e) {
         // Expected exception
     }
 }
}