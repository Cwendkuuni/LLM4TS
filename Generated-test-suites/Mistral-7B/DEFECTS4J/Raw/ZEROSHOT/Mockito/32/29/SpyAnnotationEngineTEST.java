package org.mockito.internal.configuration;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

 private AnnotationEngine annotationEngine = new SpyAnnotationEngine();

 @Mock
 private Annotation mockAnnotation;
 @Spy
 private Object spiedObject;

 @Test
 public void testCreateMockFor() {
 assertNull(annotationEngine.createMockFor(mockAnnotation, null));
 }

 @Test
 public void testProcess_NoSpyAnnotation() {
 Class<?> context = getClass();
 Object testClass = this;
 Field[] fields = context.getDeclaredFields();
 annotationEngine.process(context, testClass);
 assertFalse(testClass.getClass().getDeclaredField("spiedField").isAccessible());
 }

 @Test
 public void testProcess_SpyAnnotationPresent() {
 Class<?> context = getClass();
 Object testClass = this;
 Field spiedField = testClass.getClass().getDeclaredField("spiedField");
 spiedField.setAccessible(true);
 spiedField.set(testClass, new Object());

 annotationEngine.process(context, testClass);
 assertTrue(testClass.getClass().getDeclaredField("spiedField").isAccessible());
 assertNotNull(spiedField.get(testClass));
 }

 @Test
 public void testProcess_SpyAnnotationPresent_InstanceIsMock() {
 Class<?> context = getClass();
 Object testClass = this;
 Field spiedField = testClass.getClass().getDeclaredField("spiedField");
 spiedField.setAccessible(true);
 spiedField.set(testClass, Mockito.mock(Object.class));

 annotationEngine.process(context, testClass);
 assertTrue(testClass.getClass().getDeclaredField("spiedField").isAccessible());
 assertNotNull(spiedField.get(testClass));
 }

 @Test(expected = MockitoException.class)
 public void testProcess_SpyAnnotationPresent_InstanceIsNull() {
 Class<?> context = getClass();
 Object testClass = this;
 Field spiedField = testClass.getClass().getDeclaredField("spiedField");
 spiedField.setAccessible(true);

 annotationEngine.process(context, testClass);
 spiedField.set(testClass, null);
 annotationEngine.process(context, testClass);
 }

 @Test
 public void testProcess_SpyAnnotationPresent_MultipleInstances() {
 Class<?> context = getClass();
 Object testClass = this;
 Field spiedField1 = testClass.getClass().getDeclaredField("spiedField1");
 spiedField1.setAccessible(true);
 spiedField1.set(testClass, new Object());

 Field spiedField2 = testClass.getClass().getDeclaredField("spiedField2");
 spiedField2.setAccessible(true);
 spiedField2.set(testClass, new Object());

 annotationEngine.process(context, testClass);
 assertTrue(testClass.getClass().getDeclaredField("spiedField1").isAccessible());
 assertTrue(testClass.getClass().getDeclaredField("spiedField2").isAccessible());
 assertNotNull(spiedField1.get(testClass));
 assertNotNull(spiedField2.get(testClass));
 }

 @Test
 public void testAssertNoAnnotations() {
 Class<?> context = getClass();
 Object testClass = this;
 Field spiedField = testClass.getClass().getDeclaredField("spiedField");

 annotationEngine.assertNoAnnotations(Spy.class, spiedField, Mock.class, Captor.class);
 assertFalse(spiedField.isAnnotationPresent(Mock.class));
 assertFalse(spiedField.isAnnotationPresent(Captor.class));
 }

 @Test(expected = org.mockito.exceptions.base.MockitoException.class)
 public void testAssertNoAnnotations_SpyAnnotationPresentMockAnnotationPresent() {
 Class<?> context = getClass();
 Object testClass = this;
 Field spiedField = testClass.getClass().getDeclaredField("spiedField");
 spiedField.setAccessible(true);
 spiedField.set(testClass, Mockito.mock(Object.class));

 try {
 annotationEngine.assertNoAnnotations(Spy.class, spiedField, Mock.class);
 fail();
 } catch (AssertionError e) {
 // expected
 }
 }
}

class SpyAnnotationEngineTest {
 private static final String FIELD_NAME = "spiedField";

 // Class under test
 private AnnotationEngine annotationEngine = new SpyAnnotationEngine();

 // Mocks
 @Mock
 private Annotation mockAnnotation;
 @Spy
 private Object spiedObject;

 @Before
 public void setUp() {
 // Set up mocks
 MockitoAnnotations.initMocks(this);
 }

 // Test methods

 @Test
 public void testCreateMockFor() {
 assertNull(annotationEngine.createMockFor(mockAnnotation, null));
 }

 @Test
 public void testProcess_NoSpyAnnotation() {
 Field[] fields = getClass().getDeclaredFields();
 annotationEngine.process(getClass(), this);
 assertFalse(getClass().getDeclaredField(FIELD_NAME).isAccessible());
 }

 @Test
 public void testProcess_SpyAnnotationPresent() {
 Field spiedField = getClass().getDeclaredField(FIELD_NAME);
 spiedField.setAccessible(true);
 spiedField.set(this, new Object());

 annotationEngine.process(getClass(), this);
 assertTrue(getClass().getDeclaredField(FIELD_NAME).isAccessible());
 assertNotNull(spiedField.get(this));
 }

 @Test
 public void testProcess_SpyAnnotationPresent_InstanceIsMock() {
 Field spiedField = getClass().getDeclaredField(FIELD_NAME);
 spiedField.setAccessible(true);
 spiedField.set(this, Mockito.mock(Object.class));

 annotationEngine.process(getClass(), this);
 assertTrue(getClass().getDeclaredField(FIELD_NAME).isAccessible());
 assertNotNull(spiedField.get(this));
 }

 @Test(expected = MockitoException.class)
 public void testProcess_SpyAnnotationPresent_InstanceIsNull() {
 Field spiedField = getClass().getDeclaredField(FIELD_NAME);
 spiedField.setAccessible(true);

 annotationEngine.process(getClass(), this);
 spiedField.set(this, null);
 annotationEngine.process(getClass(), this);
 }

 @Test
 public void testProcess_SpyAnnotationPresent_MultipleInstances() {
 Field spiedField1 = getClass().getDeclaredField("spiedField1");
 spiedField1.setAccessible(true);
 spiedField1.set(this, new Object());

 Field spiedField2 = getClass().getDeclaredField("spiedField2");
 spiedField2.setAccessible(true);
 spiedField2.set(this, new Object());

 annotationEngine.process(getClass(), this);
 assertTrue(getClass().getDeclaredField("spiedField1").isAccessible());
 assertTrue(getClass().getDeclaredField("spiedField2").isAccessible());
 assertNotNull(spiedField1.get(this));
 assertNotNull(spiedField2.get(this));
 }

 @Test
 public void testAssertNoAnnotations() {
 Field spiedField = getClass().getDeclaredField(FIELD_NAME);

 annotationEngine.assertNoAnnotations(Spy.class, spiedField, Mock.class, Captor.class);
 assertFalse(spiedField.isAnnotationPresent(Mock.class));
 assertFalse(spiedField.isAnnotationPresent(Captor.class));
 }

 @Test(expected = MockitoException.class)
 public void testAssertNoAnnotations_SpyAnnotationPresentMockAnnotationPresent() {
 Field spiedField = getClass().getDeclaredField(FIELD_NAME);
 spiedField.setAccessible(true);
 spiedField.set(this, Mockito.mock(Object.class));

 try {
 annotationEngine.assertNoAnnotations(Spy.class, spiedField, Mock.class);
 fail();
 } catch (AssertionError e) {
 // expected
 }
 }
}