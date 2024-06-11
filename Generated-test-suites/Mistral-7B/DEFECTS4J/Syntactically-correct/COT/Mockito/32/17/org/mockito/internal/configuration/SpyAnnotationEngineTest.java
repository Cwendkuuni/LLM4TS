import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.JUnit4;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.reflect.FieldUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

@RunWith(JUnit4.class)
public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine annotationEngine;
 @Spy private Object testClass;

 @Before
 public void setup() throws IllegalAccessException {
 MockitoAnnotations.initMocks(this);
 annotationEngine = new SpyAnnotationEngine();
 }

 @Test(expected = MockitoException.class)
 public void testCreateMockForNullAnnotation() {
 annotationEngine.createMockFor(null, null);
 }

 @Test
 public void testCreateMockForNoAnnotationPresent() throws IllegalAccessException {
 Field field = FieldUtils.getField(testClass, "fieldName", true);
 annotationEngine.createMockFor(null, field);
 }

 @Test
 public void testCreateMockForAnnotationPresentButInstanceIsNull() throws IllegalAccessException {
 Field field = FieldUtils.getField(testClass, "fieldName", true);
 annotationEngine.createMockFor(new Spy(), field);
 }

 @Test
 public void testCreateMockForInstanceAlreadyMocked() throws IllegalAccessException {
 Field field = FieldUtils.getField(testClass, "fieldName", true);
 Object instance = new Object();
 field.set(testClass, instance);
 annotationEngine.createMockFor(new Spy(), field);
 }

 @Test(expected = MockitoException.class)
 public void testProcessNullContext() {
 annotationEngine.process(null, testClass);
 }

 @Test
 public void testProcessEmptyContext() {
 annotationEngine.process(Object.class, testClass);
 }

 @Test
 public void testProcessFieldWithAnnotationPresentButInstanceIsNull() throws IllegalAccessException {
 Field field = FieldUtils.getField(testClass, "fieldName", true);
 annotationEngine.process(testClass.getClass(), testClass);
 }

 @Test
 public void testProcessFieldWithAnnotationPresentButInstanceAlreadyMocked() throws IllegalAccessException {
 Field field = FieldUtils.getField(testClass, "fieldName", true);
 Object instance = new Object();
 field.set(testClass, instance);
 annotationEngine.process(testClass.getClass(), testClass);
 }

 @Test
 public void testProcessFieldWithAnnotationPresentButInstanceIsMock() throws IllegalAccessException {
 Field field = FieldUtils.getField(testClass, "fieldName", true);
 Mockito.mock(field.getType());
 annotationEngine.process(testClass.getClass(), testClass);
 }

 @Test(expected = IllegalAccessException.class)
 public void testProcessFieldWithAccessDenied() throws IllegalAccessException {
 Field field = FieldUtils.getField(testClass, "fieldName", false);
 annotationEngine.process(testClass.getClass(), testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcessFieldWithInvalidAnnotation() throws IllegalAccessException {
 Field field = FieldUtils.getField(testClass, "fieldName", true);
 field.set(testClass, new Object());
 annotationEngine.process(testClass.getClass(), testClass);
 field.setAccessible(true);
 field.setAnnotation(new MyCustomAnnotation());
 }

 @Test(expected = MockitoException.class)
 public void testProcessFieldWithMixedAnnotations() throws IllegalAccessException {
 Field field = FieldUtils.getField(testClass, "fieldName", true);
 field.set(testClass, new Object());
 annotationEngine.process(testClass.getClass(), testClass);
 field.setAccessible(true);
 field.setAnnotation(new Spy());
 field.setAnnotation(new MockAnnotation());
 }

 @Test(expected = MockitoException.class)
 public void testProcessFieldWithMixedAnnotationsAndInstanceIsNull() throws IllegalAccessException {
 Field field = FieldUtils.getField(testClass, "fieldName", true);
 annotationEngine.process(testClass.getClass(), testClass);
 field.setAccessible(true);
 field.setAnnotation(new Spy());
 field.setAnnotation(new MockAnnotation());
 }

 @Test(expected = MockitoException.class)
 public void testProcessFieldWithMixedAnnotationsAndInstanceIsAlreadyMocked() throws IllegalAccessException {
 Field field = FieldUtils.getField(testClass, "fieldName", true);
 Object instance = new Object();
 field.set(testClass, instance);
 annotationEngine.process(testClass.getClass(), testClass);
 field.setAccessible(true);
 field.setAnnotation(new Spy());
 field.setAnnotation(new MockAnnotation());
 }

 @Test
 public void testProcessFieldWithMultipleSameAnnotations() throws IllegalAccessException {
 Field field = FieldUtils.getField(testClass, "fieldName", true);
 field.set(testClass, new Object());
 annotationEngine.process(testClass.getClass(), testClass);
 field.setAccessible(true);
 field.setAnnotation(new Spy());
 field.setAnnotation(new Spy());
 }

 @Test
 public void testProcessFieldWithAnnotationPresentButInstanceIsNotAMock() throws IllegalAccessException {
 Field field = FieldUtils.getField(testClass, "fieldName", true);
 Object instance = new Object();
 field.set(testClass, instance);
 annotationEngine.process(testClass.getClass(), testClass);
 assertFalse(Mockito.isMock(instance));
 }

 @Test
 public void testProcessFieldWithAnnotationPresentButInstanceIsASpy() throws IllegalAccessException {
 Field field = FieldUtils.getField(testClass, "fieldName", true);
 Object instance = Mockito.spy(new Object());
 field.set(testClass, instance);
 annotationEngine.process(testClass.getClass(), testClass);
 assertTrue(Mockito.isSpy(instance));
 }

 @Test
 public void testProcessFieldWithUndesiredAnnotations() throws IllegalAccessException {
 Field field = FieldUtils.getField(testClass, "fieldName", true);
 field.set(testClass, new Object());
 annotationEngine.process(testClass.getClass(), testClass);
 assertFalse(field.isAnnotationPresent(Mockito.Captor.class));
 assertFalse(field.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class));
 assertFalse(field.isAnnotationPresent(org.mockito.Mock.class));
 }
}