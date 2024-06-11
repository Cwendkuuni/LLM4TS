package org.mockito.internal.configuration;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.exceptions.Reporter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine engine;
 private Object testObject;
 private Field field;

 @Before
 public void setup() {
 engine = new SpyAnnotationEngine();
 testObject = new Object();
 field = Mockito.mock(Field.class);
 field.getName() = "testField";
 try {
 field.set(testObject, new Object());
 } catch (IllegalAccessException e) {
 e.printStackTrace();
 }
 }

 @Test
 public void testCreateMockFor() {
 assertNull(engine.createMockFor(Mockito.mock(Annotation.class), field));
 }

 @Test
 public void testProcess_NoSpyAnnotation() {
 engine.process(SpyAnnotationEngine.class, testObject);
 assertFalse(field.isAnnotationPresent(Spy.class));
 }

 @Test
 public void testProcess_InvalidAnnotation() {
 field.annotate(new Object() {}.getClass()).annotate(Spy.class);
 engine.process(SpyAnnotationEngine.class, testObject);
 try {
 engine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class);
 fail("Expected Reporter.unsupportedCombinationOfAnnotations to be thrown.");
 } catch (MockitoException e) {
 // Expected
 }
 }

 @Test
 public void testProcess_MockInstance() {
 Mockito.when(field.get(testObject)).thenReturn(Mockito.mock(Object.class));
 engine.process(SpyAnnotationEngine.class, testObject);
 Mockito.verify(field).set(testObject, Mockito.isA(Spy.class));
 }

 @Test
 public void testProcess_NullInstance() {
 field.setAccessible(true);
 field.set(testObject, null);
 engine.process(SpyAnnotationEngine.class, testObject);
 assertNull(field.get(testObject));
 }

 @Test(expected = IllegalAccessException.class)
 public void testProcess_IllegalAccessException() {
 field.setAccessible(false);
 engine.process(SpyAnnotationEngine.class, testObject);
 }

 @Test
 public void testProcess_NullTestClass() {
 engine.process(SpyAnnotationEngine.class, null);
 }
}