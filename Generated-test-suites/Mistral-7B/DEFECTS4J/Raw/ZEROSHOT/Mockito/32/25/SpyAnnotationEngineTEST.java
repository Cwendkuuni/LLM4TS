package org.mockito.internal.configuration;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine engine;
 @Spy private Object testObject;
 @Mock private Class testClassMock;

 @Before
 public void setup() {
 engine = new SpyAnnotationEngine();
 }

 @Test(expected = MockitoException.class)
 public void testNullAnnotation() {
 engine.process(testClassMock, testObject);
 }

 @Test
 public void testNoSpyAnnotation() {
 engine.process(testClassMock, testObject);
 assertFalse(testObject.getClass().getDeclaredFields()[0].isAnnotationPresent(Spy.class));
 }

 @Test(expected = MockitoException.class)
 public void testMissingInstance() {
 Field field = Mockito.mock(Field.class);
 field.setAccessible(true);
 try {
 engine.process(testClassMock, testObject);
 } finally {
 field.setAccessible(false);
 }
 field.set(testObject, null);
 engine.process(testClassMock, testObject);
 }

 @Test
 public void testSpyAnnotationPresent() {
 Field field = Mockito.mock(Field.class);
 field.setAccessible(true);
 try {
 Object instance = new Object();
 field.set(testObject, instance);
 engine.process(testClassMock, testObject);
 assertNotNull(field.get(testObject));
 assertTrue(Mockito.isSpy(field.get(testObject)));
 } finally {
 field.setAccessible(false);
 }
 }

 @Test(expected = MockitoException.class)
 public void testMockAnnotationPresent() {
 Field field = Mockito.mock(Field.class);
 field.setAccessible(true);
 try {
 Object instance = new Object();
 field.set(testObject, instance);
 Mockito.mockStatic(SpyAnnotationEngine.class);
 SpyAnnotationEngine.class.getDeclaredMethod("createMockFor")
 .invoke(engine, Mockito.mock(Annotation.class), field);
 } finally {
 field.setAccessible(false);
 }
 }

 @Test(expected = MockitoException.class)
 public void testCaptorAnnotationPresent() {
 Field field = Mockito.mock(Field.class);
 field.setAccessible(true);
 try {
 Object instance = new Object();
 field.set(testObject, instance);
 Mockito.mockStatic(SpyAnnotationEngine.class);
 SpyAnnotationEngine.class.getDeclaredMethod("process")
 .invoke(engine, testClassMock, testObject);
 } finally {
 field.setAccessible(false);
 }
 }

 @Test
 public void testNoUndesiredAnnotation() {
 Field field = Mockito.mock(Field.class);
 field.setAccessible(true);
 try {
 Object instance = new Object();
 field.set(testObject, instance);
 engine.process(testClassMock, testObject);
 } finally {
 field.setAccessible(false);
 }
 assertFalse(field.getAnnotations().length > 0);
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testUndesiredAnnotation() {
 Field field = Mockito.mock(Field.class);
 field.setAccessible(true);
 try {
 Object instance = new Object();
 field.set(testObject, instance);
 Class<Annotation> mockAnnotation = Mockito.mock(Annotation.class);
 field.setAnnotation(mockAnnotation);
 engine.process(testClassMock, testObject);
 } finally {
 field.setAccessible(false);
 }
 }
}