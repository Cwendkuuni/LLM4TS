package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine annotationEngine;
 @Mock private Object testClass;
 @Spy private Object spyInstance;

 @Before
 public void setUp() {
 annotationEngine = new SpyAnnotationEngine();
 }

 @Test
 public void testCreateMockFor_AnnotationNotPresent() {
 assertNull(annotationEngine.createMockFor(null, null));
 }

 @Test(expected = MockitoException.class)
 public void testProcess_NullContext() {
 annotationEngine.process(null, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcess_NullTestClass() {
 annotationEngine.process(Object.class, null);
 }

 @Test
 public void testProcess_NoSpyAnnotation() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(false);
 annotationEngine.process(Object.class, testClass);
 verify(field, never()).set(testClass, argThat(isInstanceOf(Object.class)));
 }

 @Test
 public void testProcess_SpyAnnotationPresent() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn("testField");
 when(field.get(testClass)).thenReturn(spyInstance);

 annotationEngine.process(Object.class, testClass);
 verify(field, times(1)).set(testClass, argThat(isInstanceOf(Object.class)));
 verify(spyInstance, times(1)).getClass();
 }

 @Test(expected = MockitoException.class)
 public void testProcess_InstanceIsNull() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn("testField");
 when(field.get(testClass)).thenReturn(null);

 annotationEngine.process(Object.class, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcess_InstanceAlreadySpyed() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn("testField");
 when(field.get(testClass)).thenReturn(spyInstance);
 when(MockUtil.isMock(spyInstance)).thenReturn(true);

 annotationEngine.process(Object.class, testClass);
 }

 @Test
 public void testAssertNoAnnotations() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.isAnnotationPresent(Capture.class)).thenReturn(false);
 when(field.isAnnotationPresent(MockitoMock.class)).thenReturn(false);
 when(field.isAnnotationPresent(Captor.class)).thenReturn(false);

 annotationEngine.assertNoAnnotations(Spy.class, field, MockitoMock.class, Captor.class);
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotations_SpyAndMockAnnotationPresent() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.isAnnotationPresent(MockitoMock.class)).thenReturn(true);

 annotationEngine.assertNoAnnotations(Spy.class, field, MockitoMock.class);
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotations_SpyAndCaptorAnnotationPresent() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.isAnnotationPresent(Captor.class)).thenReturn(true);

 annotationEngine.assertNoAnnotations(Spy.class, field, Captor.class);
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotations_SpyAndMockitoAnnotationsMockAnnotationPresent() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class)).thenReturn(true);

 annotationEngine.assertNoAnnotations(Spy.class, field, org.mockito.MockitoAnnotations.Mock.class);
 }

 @Test
 public void testAssertNoAnnotations_NoUndesiredAnnotationsPresent() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);

 annotationEngine.assertNoAnnotations(Spy.class, field);
 }

}