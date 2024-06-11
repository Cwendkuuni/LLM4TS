package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotation;
import org.mockito.exceptions.base.MockitoException;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine engine;
 @Mock private Object testClass;
 @Mock private Field field;
 @Mock private Object instance;

 @Before
 public void setUp() {
 engine = new SpyAnnotationEngine();
 Mockito.initMocks(this);
 }

 @Test
 public void testCreateMockFor() {
 assertNull(engine.createMockFor(Spy.class, field));
 }

 @Test
 public void testProcess_NoAnnotationPresent() {
 engine.process(Object.class, testClass);
 assertFalse(field.isAccessible());
 }

 @Test
 public void testProcess_SpyAnnotationPresent() throws IllegalAccessException {
 // given
 field.setAccessible(true);
 field.set(testClass, instance);
 field.setAnnotation(new SpyAnnotation());

 // when
 engine.process(Object.class, testClass);

 // then
 assertNotNull(instance);
 assertFalse(field.isAnnotationPresent(Mock.class));
 assertFalse(field.isAnnotationPresent(Captor.class));
 assertFalse(field.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class));

 // verify that instance was spied
 verify(instance).getClass();
 }

 @Test
 public void testProcess_SpyAnnotationAndOtherAnnotationPresent() {
 // given
 field.setAccessible(true);
 field.set(testClass, instance);
 field.setAnnotation(new SpyAnnotation());
 field.setAnnotation(new MockAnnotation());

 // when
 assertThrows(MockitoException.class, () -> engine.process(Object.class, testClass));
 }

 @Test
 public void testProcess_InstanceIsNull() throws IllegalAccessException {
 // given
 field.setAccessible(true);
 field.set(testClass, null);
 field.setAnnotation(new SpyAnnotation());

 // when
 assertThrows(MockitoException.class, () -> engine.process(Object.class, testClass));
 }

 @Test
 public void testProcess_InstanceIsMock() throws IllegalAccessException {
 // given
 field.setAccessible(true);
 field.set(testClass, mock(Object.class));
 field.setAnnotation(new SpyAnnotation());

 // when
 engine.process(Object.class, testClass);

 // then
 assertNotNull(field.get(testClass));
 verify(field.get(testClass)).getClass();
 }

 @Test
 public void testAssertNoAnnotations() {
 engine.assertNoAnnotations(Spy.class, field, Mock.class, Captor.class);
 }

 @Test(expected = MockitoException.class)
 public void testAssertNoAnnotations_AnnotationPresent() {
 engine.assertNoAnnotations(Spy.class, field, Mock.class, Captor.class, Mock.class);
 }
}

class SpyAnnotation {}
class MockAnnotation {}