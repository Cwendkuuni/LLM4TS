package org.mockito.internal.configuration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockComponentImplementation;
import org.mockito.plugins.AnnotationProcessingInfo;
import org.mockito.plugins.AnnotationProcessingPlugin;
import org.mockito.plugins.MockProcessingEnvironment;

public class SpyAnnotationEngineTest {

 private static final String FIELD_NAME = "testField";
 private static final Class<SpyAnnotationEngine> SPY_ANNOTATION_ENGINE_CLASS = SpyAnnotationEngine.class;
 private static final Class<MockitoException> MOCKITO_EXCEPTION_CLASS = MockitoException.class;

 @SuppressWarnings("unchecked")
 @Test
 public void testCreateMockFor() {
 SpyAnnotationEngine engine = spy(SpyAnnotationEngine.class);
 Annotation annotation = mock(Annotation.class);
 Field field = mock(Field.class);
 Assert.assertNull(engine.createMockFor(annotation, field));
 }

 @Test
 public void testProcess_NoSpyAnnotation_DoNothing() {
 SpyAnnotationEngine engine = spy(SpyAnnotationEngine.class);
 Class<?> context = mock(Class.class);
 Object testClass = mock(Object.class);
 engine.process(context, testClass);
 verifyNoMoreInteractions(engine);
 }

 @Test
 public void testProcess_SpyAnnotationPresent_SpyExistingInstance() {
 SpyAnnotationEngine engine = spy(SpyAnnotationEngine.class);
 Class<?> context = mock(Class.class);
 Object testClass = mock(Object.class);
 Object instance = mock(Object.class);
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn(FIELD_NAME);
 when(field.get(testClass)).thenReturn(instance);

 doNothing().when(engine).assertNoAnnotations(eq(Spy.class), eq(field), eq(Mock.class), eq(Annotation.class), eq(Capture.class));
 doAnswer(invocation -> {
 field.setAccessible(true);
 return null;
 }).when(engine).process(eq(context), eq(testClass));

 MockitoAnnotations.initMocks(testClass);
 engine.process(context, testClass);

 verify(engine).assertNoAnnotations(eq(Spy.class), eq(field), eq(Mock.class), eq(Annotation.class), eq(Capture.class));
 verify(engine).setAccessible(field, true);
 verify(engine).setAccessible(field, false);
 verify(engine).setField(eq(context), eq(testClass), eq(field), any());
 }

 @Test
 public void testProcess_SpyAnnotationPresent_SpyNullInstance() {
 SpyAnnotationEngine engine = spy(SpyAnnotationEngine.class);
 Class<?> context = mock(Class.class);
 Object testClass = mock(Object.class);
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn(FIELD_NAME);
 when(field.get(testClass)).thenReturn(null);

 doThrow(new MockitoException("Cannot create a @Spy for '" + FIELD_NAME + "' field because the *instance* is missing"))
 .when(engine).setField(eq(context), eq(testClass), eq(field), any());

 AnnotationProcessingPlugin plugin = mock(AnnotationProcessingPlugin.class);
 AnnotationProcessingInfo info = mock(AnnotationProcessingInfo.class);
 MockProcessingEnvironment processingEnvironment = mock(MockProcessingEnvironment.class);
 when(plugin.getProcessingEnvironment()).thenReturn(processingEnvironment);
 when(processingEnvironment.getClasses()).thenReturn(new Class[] { context });
 when(processingEnvironment.getSource()).thenReturn(info);

 engine.process(context, testClass);

 verify(engine).assertNoAnnotations(eq(Spy.class), eq(field), eq(Mock.class), eq(Annotation.class), eq(Capture.class));
 verify(engine).setAccessible(field, true);
 verify(engine).setAccessible(field, false);
 verify(engine).setField(eq(context), eq(testClass), eq(field), eq(instance));
 verify(plugin).onProcessingComplete(eq(info), eq(engine));
 verifyZeroInteractions(engine.getClass().getDeclaredConstructor());
 }

 @Test(expected = MockitoException.class)
 public void testProcess_SpyAnnotationPresent_InstanceAlreadyAMock() {
 SpyAnnotationEngine engine = spy(SpyAnnotationEngine.class);
 Class<?> context = mock(Class.class);
 Object testClass = mock(Object.class);
 Object instance = mock(Object.class);
 Field field = mock(Field.class);
 when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
 when(field.getName()).thenReturn(FIELD_NAME);
 when(field.get(testClass)).thenReturn(instance);
 when(instance.getClass()).thenReturn(MockComponentImplementation.class);

 doNothing().when(engine).assertNoAnnotations(eq(Spy.class), eq(field), eq(Mock.class), eq(Annotation.class), eq(Capture.class));
 doAnswer(invocation -> {
 field.setAccessible(true);
 return null;
 }).when(engine).process(eq(context), eq(testClass));

 MockitoAnnotations.initMocks(testClass);
 engine.process(context, testClass);

 verify(engine).assertNoAnnotations(eq(Spy.class), eq(field), eq(Mock.class), eq(Annotation.class), eq(Capture.class));
 verify(engine).setAccessible(field, true);
 verify(engine).setAccessible(field, false);
 verify(engine).setField(eq(context), eq(testClass), eq(field), any());

 engine.reset(instance);
 engine.process(context, testClass);

 verify(engine).assertNoAnnotations(eq(Spy.class), eq(field), eq(Mock.class), eq(Annotation.class), eq(Capture.class));
 verify(engine).setAccessible(field, true);
 verify(engine).setAccessible(field, false);
 verify(engine).setField(eq(context), eq(testClass), eq(field), any());
 verify(instance).equals(instance);
 }

 @Test
 public void testAssertNoAnnotations() {
 SpyAnnotationEngine engine = spy(SpyAnnotationEngine.class);
 Field field = mock(Field.class);
 Class<?>[] undesiredAnnotations = new Class[] { Mock.class, Capture.class };

 doNothing().when(engine).assertNoAnnotations(eq(Spy.class), eq(field), anyArray());
 doAnswer(invocation -> {
 Field field1 = (Field) invocation.getArguments()[1];
 assertFalse("@Spy annotation should not be present", field1.isAnnotationPresent(Spy.class));
 for (Class u : undesiredAnnotations) {
 assertFalse("Unwanted annotation " + u.getSimpleName() + " should not be present", field1.isAnnotationPresent(u));
 }
 }).when(engine).assertNoAnnotations(eq(Spy.class), eq(field), anyArray());

 engine.assertNoAnnotations(SpyAnnotationEngine.class.getField("field1"), anyArray());
 engine.assertNoAnnotations(SpyAnnotationEngine.class.getField("field2"), undesiredAnnotations);
 }
}