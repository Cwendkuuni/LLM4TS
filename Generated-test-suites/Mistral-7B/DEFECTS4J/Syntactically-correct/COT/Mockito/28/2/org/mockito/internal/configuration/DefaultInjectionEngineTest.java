package org.mockito.internal.configuration. DefaultInjectionEngineTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.reflection.FieldAccessException;
import org.mockito.internal.reflection.FieldInitializer;
import org.mockito.internal.reflection.Whitebox;
import org.mockito.mock.MockitoMock;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Set<Object> mocks;
 private Object testClassInstance;
 private Field testField;
 private Object mock;

 @Before
 public void setUp() {
 mocks = new HashSet<>();
 testClassInstance = mock(Object.class);
 injectionEngine = new DefaultInjectionEngine();
 mock = mock(String.class);
 }

 @Test
 public void testInjectMocksOnFields_NoMocks() {
 Set<Field> injectMocksFields = new HashSet<>();
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 // No mocks should be injected as no fields are annotated with @InjectMocks
 verifyZeroInteractions(mocks);
 }

 @Test
 public void testInjectMocksOnFields_SingleMock() throws Exception {
 // Setup test class with a single field annotated with @InjectMocks
 Field testField = Whitebox.getInternalState(testClassInstance, "testField");
 testField.setAccessible(true);
 testField.setAnnotation(MockitoAnnotations.InjectMocks.class);
 injectionEngine.injectMocksOnFields(getInjectMocksFields(testClassInstance), mocks, testClassInstance);

 // Mock candidate should be injected using property setter
 when(testField.getType().getDeclaredField("testProperty")).thenReturn(mock);
 Whitebox.setInternalState(testClassInstance, "testField", testField);
 injectionEngine.injectMocksOnFields(getInjectMocksFields(testClassInstance), mocks, testClassInstance);

 // Verify that the mock was injected
 verify(mock).someMethod();
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFields_NoMockCandidate() throws Exception {
 // Setup test class with a single field annotated with @InjectMocks
 Field testField = Whitebox.getInternalState(testClassInstance, "testField");
 testField.setAccessible(true);
 testField.setAnnotation(MockitoAnnotations.InjectMocks.class);
 injectionEngine.injectMocksOnFields(getInjectMocksFields(testClassInstance), mocks, testClassInstance);

 // No mock candidate found, so an exception should be thrown
 when(testField.getType().getDeclaredFields()).thenReturn(new Field[0]);
 injectionEngine.injectMocksOnFields(getInjectMocksFields(testClassInstance), mocks, testClassInstance);
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFields_MultipleMockCandidates() throws Exception {
 // Setup test class with multiple fields having the same type and annotated with @InjectMocks
 Field testField1 = Whitebox.getInternalState(testClassInstance, "testField1");
 testField1.setAccessible(true);
 testField1.setAnnotation(MockitoAnnotations.InjectMocks.class);
 Field testField2 = Whitebox.getInternalState(testClassInstance, "testField2");
 testField2.setAccessible(true);
 testField2.setAnnotation(MockitoAnnotations.InjectMocks.class);
 injectionEngine.injectMocksOnFields(getInjectMocksFields(testClassInstance), mocks, testClassInstance);

 // Two mock candidates found, so an exception should be thrown
 when(testField1.getType()).thenReturn(mock.getClass());
 when(testField2.getType()).thenReturn(mock.getClass());
 injectionEngine.injectMocksOnFields(getInjectMocksFields(testClassInstance), mocks, testClassInstance);
 }

 @Test
 public void testInjectMocksOnFields_MultipleInheritance() throws Exception {
 // Test class with multiple inheritance
 Class<?> testClass = mock(Class.class);
 when(testClass.getSuperclass()).thenReturn(Object.class);
 when(testClass.getDeclaredFields()).thenReturn(new Field[] { getField("field1", testClass), getField("field2", testClass) });
 injectionEngine.injectMocksOnFields(getInjectMocksFields(testClass), mocks, testClass);

 // Verify that the correct mock is injected for each field, regardless of inheritance
 verify(mocks).contains(getFieldValue(getField("field1", testClass), testClass));
 verify(mocks).contains(getFieldValue(getField("field2", testClass), testClass));
 }

 @Test
 public void testInjectMocksOnFields_ExceptionInInitialization() throws Exception {
 // Test class with a field annotated with @InjectMocks, but initialization throws an exception
 Field testField = Whitebox.getInternalState(testClassInstance, "testField");
 testField.setAccessible(true);
 testField.setAnnotation(MockitoAnnotations.InjectMocks.class);
 injectionEngine.injectMocksOnFields(getInjectMocksFields(testClassInstance), mocks, testClassInstance);

 // An exception should be thrown during initialization
 Whitebox.setInternalState(testClassInstance, "testField", new FieldInitializer(testClassInstance, testField).initialize());
 injectionEngine.injectMocksOnFields(getInjectMocksFields(testClassInstance), mocks, testClassInstance);
 }

 private Set<Field> getInjectMocksFields(Object testInstance) {
 Set<Field> fields = new HashSet<>();
 ReflectUtils.getAllFields(testInstance.getClass(), fields);
 return fields;
 }

 private Object getFieldValue(Field field, Object testInstance) throws FieldAccessException {
 Object value = field.get(testInstance);
 if (Modifier.isStatic(field.getModifiers())) {
 value = Whitebox.getInternalState(testInstance, field.getName());
 }
 return value;
 }

 private Field getField(String name, Class<?> clazz) {
 try {
 return clazz.getDeclaredField(name);
 } catch (NoSuchFieldException e) {
 throw new RuntimeException(e);
 }
 }
}