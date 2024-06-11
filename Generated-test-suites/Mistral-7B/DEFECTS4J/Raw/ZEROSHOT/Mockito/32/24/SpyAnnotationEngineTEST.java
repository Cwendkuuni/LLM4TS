package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Exposition;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMockExplainer;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine spyAnnotationEngine;

 @Mock
 private AnnotationEngine annotationEngine;

 @Captor
 private ArgumentCaptor<Annotation> annotationCaptor;

 @Captor
 private ArgumentCaptor<Field> fieldCaptor;

 @Spy
 private Object testClass;

 @Before
 public void setUp() {
 initMocks(this);
 spyAnnotationEngine = new SpyAnnotationEngine() {
 @Override
 public AnnotationEngine getAnnotationEngine() {
 return annotationEngine;
 }
 };
 }

 @Test
 public void testCreateMockForReturnsNull() {
 assertNull(spyAnnotationEngine.createMockFor(mock(Annotation.class), mock(Field.class)));
 }

 @Test
 public void testProcessNoSpyAnnotation() {
 spyAnnotationEngine.process(this.getClass(), testClass);
 verifyNoInteractions(annotationEngine);
 }

 @Test
 public void testProcessOneSpyAnnotation() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn("testField");
 when(field.get(testClass)).thenReturn(mock(Object.class));

 spyAnnotationEngine.process(this.getClass(), testClass);

 verify(annotationEngine).createMockFor(eq(Spy.class), eq(field));
 verifyNoMoreInteractions(annotationEngine);

 ArgumentCaptor<Object> spyCaptor = ArgumentCaptor.forClass(Object.class);
 verify(testClass).setField(eq("testField"), eq(spy(capture(spyCaptor))));

 Object spyInstance = spyCaptor.getValue();
 assertSame(spyInstance, field.get(testClass));
 }

 @Test
 public void testProcessOneSpyAnnotationWithAlreadySpyedInstance() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn("testField");
 when(field.get(testClass)).thenReturn(mock(Object.class));
 when(annotationEngine.createMockFor(eq(Spy.class), eq(field))).thenReturn(spy(field.get(testClass)));

 spyAnnotationEngine.process(this.getClass(), testClass);

 verifyNoMoreInteractions(annotationEngine);

 ArgumentCaptor<Object> spyCaptor = ArgumentCaptor.forClass(Object.class);
 verify(testClass).setField(eq("testField"), eq(spyCaptor.capture()));

 Object spyInstance = spyCaptor.getValue();
 assertSame(spyInstance, field.get(testClass));
 }

 @Test(expected = MockitoException.class)
 public void testProcessWhenFieldIsNull() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn("testField");
 when(field.get(testClass)).thenReturn(null);

 spyAnnotationEngine.process(this.getClass(), testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcessWhenInstanceIsAMock() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn("testField");
 when(field.get(testClass)).thenReturn(mock(Object.class));
 whenNew(Object.class).withArguments(Spymock(any()))
 .thenReturn(mock(Object.class));

 spyAnnotationEngine.process(this.getClass(), testClass);
 }

 @Test(expected = MockitoException.class)
 public void testProcessWhenFieldAccessFailed() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn("testField");
 when(field.get(testClass)).thenThrow(new IllegalAccessException());

 spyAnnotationEngine.process(this.getClass(), testClass);
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testProcessWhenAnnotationCombinationIsNotSupported() {
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn("testField");
 when(field.isAnnotationPresent(Mockito.Mock.class)).thenReturn(true);

 spyAnnotationEngine.process(this.getClass(), testClass);
 }
}