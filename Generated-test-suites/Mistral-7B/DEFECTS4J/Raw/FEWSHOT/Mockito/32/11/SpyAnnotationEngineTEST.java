package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class SpyAnnotationEngineTest {

 @Rule
 public MockitoRule mockitoRule = MockitoJUnit.rule();

 @Mock
 private AnnotationEngine annotationEngine;

 @Mock
 private Field testField;

 @Mock
 private Object testInstance;

 @Spy
 private SpyAnnotationEngine systemUnderTest = new SpyAnnotationEngine();

 @Captor
 private FieldsCaptor fieldsCaptor;

 @Before
 public void setUp() {
 initMocks(this);
 testField.setAccessible(true);
 }

 @Test
 public void testCreateMockFor() {
 assertNull(systemUnderTest.createMockFor(Spy.class, testField));
 }

 @Test
 public void testProcess_NullContext() {
 systemUnderTest.process(null, testInstance);
 verifyZeroInteractions(annotationEngine);
 }

 @Test
 public void testProcess_NullTestClass() {
 systemUnderTest.process(mock(Class.class), null);
 verifyZeroInteractions(annotationEngine);
 }

 @Test
 public void testProcess_NoSpyAnnotation() {
 testField.set(testInstance, "not a spy");
 systemUnderTest.process(mock(Class.class), testInstance);
 verifyZeroInteractions(annotationEngine);
 }

 @Test
 public void testProcess_FieldIsAlreadySpyed() {
 testField.set(testInstance, mock(String.class));
 systemUnderTest.process(mock(Class.class), testInstance);
 verifyNoMoreInteractions(annotationEngine);
 }

 @Test
 public void testProcess_FieldIsAMock() {
 testField.set(testInstance, mock(String.class));
 systemUnderTest.process(mock(Class.class), testInstance);
 Mockito.verify(annotationEngine).createMockFor(Mock.class, testField);
 }

 @Test
 public void testProcess_FieldIsSpyed() {
 testField.set(testInstance, testInstance);
 systemUnderTest.process(mock(Class.class), testInstance);
 assertSame(testInstance, testField.get(testInstance));
 Mockito.verify(annotationEngine).spy(testInstance);
 }

 @Test
 public void testProcess_FieldIsAccessible() {
 testField.setAccessible(false);
 testField.set(testInstance, testInstance);
 systemUnderTest.process(mock(Class.class), testInstance);
 testField.setAccessible(true);
 assertSame(testInstance, testField.get(testInstance));
 Mockito.verify(annotationEngine).spy(testInstance);
 }

 @Test(expected = MockitoException.class)
 public void testProcess_InstanceMissing() {
 testField.set(testInstance, null);
 systemUnderTest.process(mock(Class.class), testInstance);
 }

 @Test
 public void testProcess_FieldIsMockAndInstanceIsMock() {
 testField.set(testInstance, mock(String.class));
 testField.set(testInstance, mock(String.class));
 systemUnderTest.process(mock(Class.class), testInstance);
 Mockito.verifyNoInteractions(annotationEngine);
 }

 @Test
 public void testProcess_FieldIsMockAndInstanceIsSpy() {
 testField.set(testInstance, mock(String.class));
 testField.set(testInstance, spy(mock(String.class)));
 systemUnderTest.process(mock(Class.class), testInstance);
 Mockito.verifyNoInteractions(annotationEngine);
 }

 @Test
 public void testProcess_FieldIsSpyAndInstanceIsMock() {
 testField.set(testInstance, mock(String.class));
 testField.set(testInstance, spy(testInstance));
 systemUnderTest.process(mock(Class.class), testInstance);
 Mockito.verifyNoInteractions(annotationEngine);
 }

 @Test
 public void testProcess_FieldIsSpyAndInstanceIsSpy() {
 testField.set(testInstance, spy(testInstance));
 testField.set(testInstance, spy(testInstance));
 systemUnderTest.process(mock(Class.class), testInstance);
 Mockito.verifyNoInteractions(annotationEngine);
 }

 @Test
 public void testAssertNoAnnotations() {
 Field testField2 = new Field() {};
 testField2.setAccessible(true);
 testField2.set(testInstance, new Object() {});
 systemUnderTest.assertNoAnnotations(Spy.class, testField2, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 verifyZeroInteractions(new Reporter());
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotations_SpyAnnotationPresent() {
 Field testField3 = new Field() {};
 testField3.setAccessible(true);
 testField3.set(testInstance, new Spy());
 systemUnderTest.assertNoAnnotations(Spy.class, testField3, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotations_MockAnnotationPresent() {
 Field testField4 = new Field() {};
 testField4.setAccessible(true);
 testField4.set(testInstance, new Mock());
 systemUnderTest.assertNoAnnotations(Spy.class, testField4, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotations_CaptorAnnotationPresent() {
 Field testField5 = new Field() {};
 testField5.setAccessible(true);
 testField5.set(testInstance, new Captor());
 systemUnderTest.assertNoAnnotations(Spy.class, testField5, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotations_DuplicateAnnotationPresent() {
 Field testField6 = new Field() {};
 testField6.setAccessible(true);
 testField6.set(testInstance, new Spy());
 testField6.set(testInstance, new Mock());
 systemUnderTest.assertNoAnnotations(Spy.class, testField6, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
 }

}