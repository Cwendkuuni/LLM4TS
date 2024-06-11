package org.mockito.internal.configuration.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine annotationEngine;
 @Mock private Object testClass;
 @Spy private Object spiedInstance;
 @Captor private ArgumentCaptor<Annotation> annotationCaptor;

 @Before
 public void setUp() {
 annotationEngine = new SpyAnnotationEngine();
 initMocks(this);
 }

 @Test
 public void testCreateMockFor() {
 assertNull(annotationEngine.createMockFor(Spy.class, null));
 }

 @Test(expected = MockitoException.class)
 public void testProcess_MissingInstance() {
 annotationEngine.process(Object.class, testClass);
 }

 @Test
 public void testProcess_NullValue() {
 Object fieldValue = null;
 try {
 Field testField = Mockito.spy(SpyAnnotationEngine.class).getSuperclass().getDeclaredField("testClass");
 testField.setAccessible(true);
 testField.set(annotationEngine, testClass);

 Field spiedField = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("spiedInstance");
 spiedField.setAccessible(true);
 spiedField.set(annotationEngine, fieldValue);

 annotationEngine.process(Object.class, testClass);
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testProcess_MockInstance() {
 Object mockInstance = mock(String.class);
 try {
 Field testField = Mockito.spy(SpyAnnotationEngine.class).getSuperclass().getDeclaredField("testClass");
 testField.setAccessible(true);
 testField.set(annotationEngine, testClass);

 Field spiedField = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("spiedInstance");
 spiedField.setAccessible(true);
 spiedField.set(annotationEngine, mockInstance);

 annotationEngine.process(Object.class, testClass);
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }

 verify(testClass, Mockito.never()).equals(mockInstance);
 verifyNoMoreInteractions(mockInstance);
 }

 @Test
 public void testProcess_FieldIsAccessible() {
 Object fieldValue = new Object();
 try {
 Field testField = Mockito.spy(SpyAnnotationEngine.class).getSuperclass().getDeclaredField("testClass");
 testField.setAccessible(true);
 testField.set(annotationEngine, testClass);

 Field spiedField = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("spiedInstance");
 spiedField.setAccessible(true);
 spiedField.set(annotationEngine, fieldValue);

 annotationEngine.process(Object.class, testClass);
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }

 Object spiedFieldValue = (Object) spiedField.get(annotationEngine);
 verify(testClass).equals(annotationEngine.getTestClass());
 verifyZeroInteractions(spiedFieldValue);
 }

 @Test(expected = MockitoException.class)
 public void testProcess_FieldIsNotAccessible() {
 try {
 Field testField = Mockito.spy(SpyAnnotationEngine.class).getSuperclass().getDeclaredField("testClass");
 testField.setAccessible(false);
 testField.set(annotationEngine, testClass);

 Field spiedField = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("spiedInstance");
 annotationEngine.process(Object.class, testClass);
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }

 verifyZeroInteractions(testClass);
 }

 @Test
 public void testProcess_FieldIsAccessibleButHasAnnotation() {
 Object fieldValue = new Object();
 try {
 Field testField = Mockito.spy(SpyAnnotationEngine.class).getSuperclass().getDeclaredField("testClass");
 testField.setAccessible(true);
 testField.set(annotationEngine, testClass);

 Field spiedField = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("spiedInstance");
 spiedField.setAccessible(true);
 spiedField.set(annotationEngine, fieldValue);

 Field annotationField = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("annotation");
 annotationField.setAccessible(true);
 annotationField.set(annotationEngine, spy(Spy.class));

 annotationEngine.process(Object.class, testClass);
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }

 verify(testClass).equals(annotationEngine.getTestClass());
 verifyZeroInteractions(fieldValue);
 }

 @Test(expected = MockitoException.class)
 public void testProcess_FieldHasBothMockAndSpyAnnotations() {
 Object fieldValue = new Object();
 try {
 Field testField = Mockito.spy(SpyAnnotationEngine.class).getSuperclass().getDeclaredField("testClass");
 testField.setAccessible(true);
 testField.set(annotationEngine, testClass);

 Field spiedField = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("spiedInstance");
 spiedField.setAccessible(true);
 spiedField.set(annotationEngine, fieldValue);

 Field mockAnnotationField = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("annotation");
 mockAnnotationField.setAccessible(true);
 mockAnnotationField.set(annotationEngine, mock(Annotation.class));

 Field spyAnnotationField = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("annotation");
 spyAnnotationField.setAccessible(true);
 spyAnnotationField.set(annotationEngine, spy(Spy.class));

 annotationEngine.process(Object.class, testClass);
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testProcess_FieldHasSameAnnotation() {
 Object fieldValue = new Object();
 try {
 Field testField = Mockito.spy(SpyAnnotationEngine.class).getSuperclass().getDeclaredField("testClass");
 testField.setAccessible(true);
 testField.set(annotationEngine, testClass);

 Field spiedField = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("spiedInstance");
 spiedField.setAccessible(true);
 spiedField.set(annotationEngine, fieldValue);

 Field annotationField = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("annotation");
 annotationField.setAccessible(true);
 annotationField.set(annotationEngine, spy(Spy.class));

 annotationEngine.process(Object.class, testClass);
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }

 Object spiedFieldValue = (Object) spiedField.get(annotationEngine);
 verify(testClass).equals(annotationEngine.getTestClass());
 verifyZeroInteractions(spiedFieldValue);
 }

 @Test
 public void testProcess_FieldHasSameAnnotationTwice() {
 Object fieldValue = new Object();
 try {
 Field testField = Mockito.spy(SpyAnnotationEngine.class).getSuperclass().getDeclaredField("testClass");
 testField.setAccessible(true);
 testField.set(annotationEngine, testClass);

 Field spiedField = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("spiedInstance");
 spiedField.setAccessible(true);
 spiedField.set(annotationEngine, fieldValue);

 Field annotationField = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("annotation");
 annotationField.setAccessible(true);
 annotationField.set(annotationEngine, spy(Spy.class));

 Field otherAnnotationField = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("annotation");
 otherAnnotationField.setAccessible(true);
 otherAnnotationField.set(annotationEngine, spy(Spy.class));

 annotationEngine.process(Object.class, testClass);
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }

 Object spiedFieldValue = (Object) spiedField.get(annotationEngine);
 verify(testClass).equals(annotationEngine.getTestClass());
 verifyZeroInteractions(spiedFieldValue);
 }

 @Test
 public void testProcess_FieldHasDifferentAnnotations() {
 Object fieldValue1 = new Object();
 Object fieldValue2 = new Object();
 try {
 Field testField = Mockito.spy(SpyAnnotationEngine.class).getSuperclass().getDeclaredField("testClass");
 testField.setAccessible(true);
 testField.set(annotationEngine, testClass);

 Field spiedField1 = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("spiedInstance");
 spiedField1.setAccessible(true);
 spiedField1.set(annotationEngine, fieldValue1);

 Field spiedField2 = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("spiedInstance");
 spiedField2.setAccessible(true);
 spiedField2.set(annotationEngine, fieldValue2);

 Field annotationField1 = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("annotation");
 annotationField1.setAccessible(true);
 annotationField1.set(annotationEngine, spy(Spy.class));

 Field annotationField2 = Mockito.spy(SpyAnnotationEngine.class).getDeclaredField("annotation");
 annotationField2.setAccessible(true);
 annotationField2.set(annotationEngine, mock(Annotation.class));

 annotationEngine.process(Object.class, testClass);
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }

 Object spiedFieldValue1 = (Object) spiedField1.get(annotationEngine);
 Object spiedFieldValue2 = (Object) spiedField2.get(annotationEngine);
 verify(testClass).equals(annotationEngine.getTestClass());
 verifyZeroInteractions(spiedFieldValue1);
 verifyZeroInteractions(spiedFieldValue2);
 }
}