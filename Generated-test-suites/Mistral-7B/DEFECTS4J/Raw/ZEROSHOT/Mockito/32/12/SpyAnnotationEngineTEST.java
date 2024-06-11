package org.mockito.internal.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.exceptions.base.MockitoException;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

 private SpyAnnotationEngine engine;
 private Object testInstance;

 @Mock
 private Object mockInstance;

 @Spy
 private Object spiedInstance;

 @Before
 public void setup() {
 engine = new SpyAnnotationEngine();
 testInstance = new Object() {};
 }

 @Test
 public void testCreateMockFor() {
 Annotation annotation = Mockito.mock(Annotation.class);
 Field field = new Field(Annotation.class, "name", null);

 engine.createMockFor(annotation, field);

 assertThat(engine.createMockFor(annotation, field), isNull());
 }

 @Test(expected = MockitoException.class)
 public void testSpyNullInstance() {
 Field field = new Field(Spy.class, "fieldName", null);
 engine.process(SpyAnnotationEngine.class, testInstance);
 field.set(testInstance, null);
 engine.process(SpyAnnotationEngine.class, testInstance);
 }

 @Test(expected = MockitoException.class)
 public void testSpyAlreadyMockedInstance() {
 Field field = new Field(Spy.class, "fieldName", mockInstance);
 engine.process(SpyAnnotationEngine.class, testInstance);
 engine.process(SpyAnnotationEngine.class, testInstance);
 }

 @Test
 public void testSpyInstance() {
 Field field = new Field(Spy.class, "fieldName", testInstance);
 engine.process(SpyAnnotationEngine.class, testInstance);

 assertThat(field.get(testInstance), is(spiedInstance));
 verifyNoMoreInteractions(mockInstance);
 }

 @Test
 public void testSpyInstanceWithCaptor() {
 ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
 Field field = new Field(Spy.class, "fieldName", new Object() {});
 engine.process(SpyAnnotationEngine.class, testInstance);
 engine.process(SpyAnnotationEngine.class, testInstance);

 Mockito.doReturn(spiedInstance).when(mockInstance).getReturnValue();
 Mockito.verify(mockInstance).getReturnValue();
 Mockito.verifyZeroInteractions(testInstance);
 Mockito.verify(testInstance).set(captor.capture(field), spiedInstance);

 Object captured = captor.getValue();
 assertThat(captured, is(field));
 }

 @Test(expected = MockitoException.class)
 public void testSpyAccessibleFieldException() {
 Field field = new Field(Spy.class, "fieldName", testInstance);
 engine.process(SpyAnnotationEngine.class, testInstance);
 Mockito.doThrow(new IllegalAccessException("Exception")).when(field).setAccessible(true);
 engine.process(SpyAnnotationEngine.class, testInstance);
 }
}