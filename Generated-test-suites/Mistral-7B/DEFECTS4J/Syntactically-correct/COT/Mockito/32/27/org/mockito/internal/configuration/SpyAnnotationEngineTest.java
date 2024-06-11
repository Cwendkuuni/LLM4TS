package org.mockito.internal.configuration.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;

import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine engine;
 private Object testObject;

 @Before
 public void setUp() {
 engine = new SpyAnnotationEngine();
 testObject = new Object();
 }

 // Test for createMockFor method
 @Test(expected = NullPointerException.class)
 public void testCreateMockForNullAnnotation() {
 engine.createMockFor(null, null);
 }

 // Test for createMockFor method with null field
 @Test(expected = NullPointerException.class)
 public void testCreateMockForNullField() {
 engine.createMockFor(mock(Annotation.class), null);
 }

 // Test for process method with empty testClass
 @Test
 public void testProcessEmptyTestClass() {
 engine.process(Object.class, testObject);
 }

 // Test for process method with no Spy annotation present
 @Test
 public void testProcessNoSpyAnnotation() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(false);
 engine.process(Object.class, testObject);
 verifyZeroInteractions(engine);
 }

 // Test for process method with valid Spy annotation and existing instance
 @Test
 public void testProcessValidSpyAnnotationWithInstance() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn("testField");
 when(field.get(testObject)).thenReturn(new Object());

 engine.process(Object.class, testObject);
 verify(engine).assertNoAnnotations(eq(Spy.class), eq(field), isA(Mock.class), isA(org.mockito.MockitoAnnotations.Mock.class), isA(Captor.class));
 verify(engine).setAccessible(eq(field), isTrue());
 verify(engine).set(eq(testObject), eq(Mockito.spy(capture(instance -> instance.get(testObject)))));
 }

 // Test for process method with valid Spy annotation and null instance
 @Test(expected = MockitoException.class)
 public void testProcessValidSpyAnnotationWithNullInstance() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn("testField");
 when(field.get(testObject)).thenReturn(null);

 engine.process(Object.class, testObject);
 verifyZeroInteractions(engine);
 }

 // Test for process method with valid Spy annotation and existing mock instance
 @Test
 public void testProcessValidSpyAnnotationWithMockInstance() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn("testField");
 when(field.get(testObject)).thenReturn(mock(Object.class));

 engine.process(Object.class, testObject);
 verify(engine).assertNoAnnotations(eq(Spy.class), eq(field), isA(Mock.class), isA(org.mockito.MockitoAnnotations.Mock.class), isA(Captor.class));
 verify(engine).setAccessible(eq(field), isTrue());
 verify(engine).set(eq(testObject), eq(Mockito.spy(argThat(i -> i != null))));
 }

 // Test for process method with illegal access exception
 @Test(expected = MockitoException.class)
 public void testProcessWithIllegalAccessException() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn("testField");
 doThrow(new IllegalAccessException("Test exception")).when(field).get(testObject);

 engine.process(Object.class, testObject);
 }

 // Test for process method with multiple undesired annotations
 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testProcessWithMultipleUndesiredAnnotations() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn("testField");
 when(field.isAnnotationPresent(Mock.class)).thenReturn(true);
 when(field.isAnnotationPresent(Captor.class)).thenReturn(true);

 engine.process(Object.class, testObject);
 }
}