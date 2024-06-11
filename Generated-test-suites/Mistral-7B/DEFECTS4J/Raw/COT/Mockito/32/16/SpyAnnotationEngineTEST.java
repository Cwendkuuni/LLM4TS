package org.mockito.internal.configuration;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine annotationEngine;
 private Object testClass;
 private Object instanceToBeSpyed;
 private Field fieldToBeSpyed;

 @Before
 public void setUp() {
 annotationEngine = new SpyAnnotationEngine();
 testClass = Mockito.spy(new Object() {});
 instanceToBeSpyed = new Object();
 fieldToBeSpyed = Mockito.spyField(TestClass.class, "fieldToBeSpyed");
 fieldToBeSpyed.set(testClass, instanceToBeSpyed);
 }

 @Test
 public void testCreateMockFor() {
 assertNull(annotationEngine.createMockFor(Mockito.mock(Annotation.class), Mockito.spy(Field.class)));
 }

 @Test
 public void testProcess_FieldWithoutSpyAnnotation_DoesNothing() {
 annotationEngine.process(TestClass.class, testClass);
 Mockito.verifyZeroInteractions(testClass);
 }

 @Test
 public void testProcess_FieldWithSpyAnnotation_ProperlySpyInstance() {
 annotationEngine.process(TestClass.class, testClass);
 Mockito.verify(testClass, Mockito.times(1)).equals(ArgumentMatchers.any());
 }

 @Test(expected = MockitoException.class)
 public void testProcess_FieldWithNullInstance() {
 annotationEngine.process(TestClass.class, testClass);
 fieldToBeSpyed.set(testClass, null);
 annotationEngine.process(TestClass.class, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcess_FieldWithMockAnnotation_ThrowsException() {
 fieldToBeSpyed.setAnnotation(Mockito.mock(Mock.class));
 annotationEngine.process(TestClass.class, testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcess_FieldWithCaptorAnnotation_ThrowsException() {
 fieldToBeSpyed.setAnnotation(Mockito.mock(Captor.class));
 annotationEngine.process(TestClass.class, testClass);
 }

 @Test(expected = IllegalAccessException.class)
 public void testProcess_IllegalAccessException_ThrowsException() {
 Mockito.doThrow(new IllegalAccessException("Test exception")).when(fieldToBeSpyed).setAccessible(true);
 annotationEngine.process(TestClass.class, testClass);
 }

 @Test
 public void testProcess_FieldWithSameInstance_ResetsMock() {
 Object instanceToBeSpyedAgain = new Object();
 fieldToBeSpyed.set(testClass, instanceToBeSpyedAgain);
 annotationEngine.process(TestClass.class, testClass);
 Mockito.verify(instanceToBeSpyedAgain, Mockito.times(1)).equals(ArgumentMatchers.any());
 Mockito.verify(instanceToBeSpyed, Mockito.times(1)).reset();
 }

 @Test
 public void testAssertNoAnnotations_AnnotationPresent_ThrowsException() {
 fieldToBeSpyed.setAnnotation(Mockito.mock(Spy.class));
 fieldToBeSpyed.setAnnotation(Mockito.mock(MockitoAnnotations.Mock.class));
 try {
 annotationEngine.assertNoAnnotations(Spy.class, fieldToBeSpyed, Mockito.Mock.class, Mockito.class, Captor.class);
 fail();
 } catch (AssertionError e) {
 // expected
 }
 }

 @Test
 public void testAssertNoAnnotations_NoAnnotationPresent_DoesNothing() {
 annotationEngine.assertNoAnnotations(Spy.class, fieldToBeSpyed, Mockito.Mock.class, Mockito.class, Captor.class);
 }

 @Test
 public void testAssertNoAnnotations_UndesiredAnnotationNotPresent_DoesNothing() {
 fieldToBeSpyed.setAnnotation(Mockito.mock(OtherAnnotation.class));
 annotationEngine.assertNoAnnotations(Spy.class, fieldToBeSpyed, Mockito.Mock.class, Mockito.class, Captor.class);
 }

}

class TestClass {
 @Spy
 private Field fieldToBeSpyed;
}