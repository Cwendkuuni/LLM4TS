package org.mockito.internal.configuration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.initFields;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;

import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine spyAnnotationEngine;
 @Mock private Class context;
 @InjectMocks @Spy private SpyAnnotationEngine testedSpyAnnotationEngine;
 @Captor private ArgumentCaptor<Field> fieldCaptor;

 @Before
 public void setUp() {
 initFields(this);
 spyAnnotationEngine = new SpyAnnotationEngine();
 }

 @Test
 public void testCreateMockFor_WithAnnotationAnnotationEngine_ShouldReturnNull() {
 verify(spyAnnotationEngine).createMockFor((Annotation) eq(Annotation.class), any(Field.class)).isNull();
 }

 @Test
 public void testProcess_WithNoSpyAnnotation_ShouldDoNothing() {
 Field[] fields = mock(Class.class).getDeclaredFields();
 verifyNoInteractions(fields);

 spyAnnotationEngine.process(context, mock(Object.class));
 verifyNoInteractions(fields);
 }

 @Test
 public void testProcess_WithSpyAnnotation_ShouldNotHaveOtherAnnotation() {
 Field field = mock(Field.class);
 doReturn(true).when(field).isAnnotationPresent(Spy.class);
 doReturn(false).when(field).isAnnotationPresent(Mock.class);
 doReturn(false).when(field).isAnnotationPresent(Captor.class);
 doReturn(false).when(field).isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class);

 try {
 spyAnnotationEngine.process(context, mock(Object.class));
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }

 verify(field).assertNoAnnotations(Spy.class, eq(Annotation.class), eq(Mock.class), eq(org.mockito.MockitoAnnotations.Mock.class), eq(Captor.class));
 }

 @Test
 public void testProcess_WithSpyAnnotation_ShouldSetAccessible() {
 Field field = mock(Field.class);
 doReturn(true).when(field).isAnnotationPresent(Spy.class);
 doReturn(false).when(field).isAccessible();

 try {
 spyAnnotationEngine.process(context, mock(Object.class));
 } catch (IllegalAccessException e) {
 throw new RuntimeException(e);
 }

 verify(field).setAccessible(true);
 }

 @Test
 public void testProcess_WithSpyAnnotation_ShouldReturnSpyInstance() throws IllegalAccessException {
 Field field = mock(Field.class);
 doReturn(true).when(field).isAnnotationPresent(Spy.class);
 doReturn(mock(Object.class)).when(field).get(mock(Object.class));
 doReturn(mock(Object.class)).when(field).get(any(Object.class));

 Object instance = new Object();
 when(field.getName()).thenReturn("fieldName");

 spyAnnotationEngine.process(context, instance);

 verify(field).set(instance, argThat(isA(Mockito.spy(instance))));
 }

 @Test
 public void testProcess_WithSpyAnnotation_AlreadySpyedInstance_ShouldResetInstance() throws IllegalAccessException {
 Field field = mock(Field.class);
 doReturn(true).when(field).isAnnotationPresent(Spy.class);
 doReturn(mock(Object.class)).when(field).get(mock(Object.class));
 doReturn(mock(Object.class)).when(field).get(any(Object.class));
 doReturn(mock(Object.class)).when(field).get(eq(instance));
 doReturn(mock(Object.class)).when(field).get(any(Object.class));

 Object instance = new Object();

 spyAnnotationEngine.process(context, instance);
 verify(field).set(instance, argThat(isA(Mockito.spy(instance))));

 verifyZeroInteractions(instance);

 spyAnnotationEngine.process(context, instance);
 verify(field).set(instance, argThat(isA(Mockito.spy(instance))));
 }

 @Test
 public void testProcess_WithSpyAnnotation_NullInstance_ShouldThrowException() {
 assertThrows(MockitoException.class, () -> {
 spyAnnotationEngine.process(context, (Object) null);
 });
 }

 @Test
 public void testProcess_WithSpyAnnotation_FieldWithMockInstance_ShouldResetInstance() throws IllegalAccessException {
 Field field = mock(Field.class);
 doReturn(true).when(field).isAnnotationPresent(Spy.class);
 doReturn(mock(Object.class)).when(field).get(mock(Object.class));
 doReturn(mock(Mockito.Mock.class)).when(field).get(any(Object.class));
 doReturn(mock(Object.class)).when(field).get(eq(instance));
 doReturn(mock(Object.class)).when(field).get(any(Object.class));

 Object instance = new Object();
 doReturn("mockInstance").when(field).getName();

 spyAnnotationEngine.process(context, instance);
 verify(field).set(instance, argThat(isA(Mockito.spy(instance))));

 verifyZeroInteractions(instance);

 verify(field).set(instance, isA(Mockito.mock(Object.class)));
 }

 @Test
 public void testAssertNoAnnotations_WithAnnotationPresent_ShouldThrowException() {
 Field field = mock(Field.class);
 doReturn(true).when(field).isAnnotationPresent(Spy.class);
 doReturn(true).when(field).isAnnotationPresent(Mock.class);

 try {
 spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class);
 } catch (Exception e) {
 // Expected
 }
 }
}