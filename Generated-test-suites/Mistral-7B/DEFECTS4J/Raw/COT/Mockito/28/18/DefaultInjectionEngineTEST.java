package org.mockito.internal.configuration.DefaultInjectionEngine;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.OngoingStubbing;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine defaultInjectionEngine;
 private Set<Object> mocks;
 private Object testClassInstance;
 private Field fieldAnnotatedWithInjectMocks;
 private Field fieldInSuperClass;
 private Field fieldInAnotherClass;
 private Object mockFieldAnnotatedWithInjectMocks;
 private Object mockFieldInSuperClass;
 private Object mockFieldInAnotherClass;

 @Before
 public void setUp() {
 mocks = new HashSet<>();
 mockFieldAnnotatedWithInjectMocks = mock(ClassWithInjectMocks.class);
 mockFieldInSuperClass = mock(ClassWithSuperClass.class);
 mockFieldInAnotherClass = mock(ClassWithAnotherClass.class);
 testClassInstance = new TestClass();
 initMocks(testClassInstance);
 defaultInjectionEngine = new DefaultInjectionEngine();
 fieldAnnotatedWithInjectMocks = ReflectionUtils.getFieldAnnotatedWithInjectMocks(testClassInstance, "fieldAnnotatedWithInjectMocks");
 fieldInSuperClass = ReflectionUtils.getFieldAnnotatedWithInjectMocks(testClassInstance.getClass().getSuperclass(), "fieldInSuperClass");
 }

 @Test
 public void testInjectMocksOnFields() {
 // Basic test case: injectMocksOnFields method called with correct arguments
 defaultInjectionEngine.injectMocksOnFields(Arrays.asList(fieldAnnotatedWithInjectMocks), mocks, testClassInstance);

 // Test for edge cases and exception handling

 // Test when the field annotated with @InjectMocks is null
 defaultInjectionEngine.injectMocksOnFields(null, mocks, testClassInstance);
 // Test when the mocks set is null
 defaultInjectionEngine.injectMocksOnFields(Arrays.asList(fieldAnnotatedWithInjectMocks), null, testClassInstance);
 // Test when the testClassInstance is null
 defaultInjectionEngine.injectMocksOnFields(Arrays.asList(fieldAnnotatedWithInjectMocks), mocks, null);

 // Test when there is no field annotated with @InjectMocks
 TestClassWithoutInjectMocks testClassWithoutInjectMocks = new TestClassWithoutInjectMocks();
 defaultInjectionEngine.injectMocksOnFields(Arrays.asList(), mocks, testClassWithoutInjectMocks);
 verifyZeroInteractions(mocks);

 // Test when there is only one field annotated with @InjectMocks, and its candidate is found by type
 defaultInjectionEngine.injectMocksOnFields(Arrays.asList(fieldAnnotatedWithInjectMocks), mocks, testClassInstance);
 verify(mockFieldAnnotatedWithInjectMocks).someMethod();

 // Test when there is only one field annotated with @InjectMocks, but its candidate is not found by type
 TestClassWithNoMatchingType testClassWithNoMatchingType = new TestClassWithNoMatchingType();
 defaultInjectionEngine.injectMocksOnFields(Arrays.asList(ReflectionUtils.getFieldAnnotatedWithInjectMocks(testClassWithNoMatchingType.getClass(), "fieldAnnotatedWithInjectMocks")), mocks, testClassWithNoMatchingType);
 verifyZeroInteractions(mocks);

 // Test when there is only one field annotated with @InjectMocks, and its candidate is found by name
 TestClassWithNameMatchingType testClassWithNameMatchingType = new TestClassWithNameMatchingType();
 defaultInjectionEngine.injectMocksOnFields(Arrays.asList(fieldAnnotatedWithInjectMocks), mocks, testClassWithNameMatchingType);
 verify(mockFieldAnnotatedWithInjectMocks).someMethod();
 verify(mockFieldInAnotherClass).anotherMethod();

 // Test when there are multiple fields annotated with @InjectMocks, and their candidates are found by type
 TestClassWithMultipleFields testClassWithMultipleFields = new TestClassWithMultipleFields();
 defaultInjectionEngine.injectMocksOnFields(Arrays.asList(fieldAnnotatedWithInjectMocks, fieldInSuperClass), mocks, testClassWithMultipleFields);
 verify(mockFieldAnnotatedWithInjectMocks).someMethod();
 verify(mockFieldInSuperClass).someSuperClassMethod();
 verifyNoMoreInteractions(mocks);

 // Test when there are multiple fields annotated with @InjectMocks, and their candidates are not found by type, but are found by name
 TestClassWithMultipleFieldsAndNameMatching testClassWithMultipleFieldsAndNameMatching = new TestClassWithMultipleFieldsAndNameMatching();
 defaultInjectionEngine.injectMocksOnFields(Arrays.asList(fieldAnnotatedWithInjectMocks, fieldInSuperClass), mocks, testClassWithMultipleFieldsAndNameMatching);
 verify(mockFieldAnnotatedWithInjectMocks).someMethod();
 verify(mockFieldInAnotherClass).anotherMethod();
 verify(mockFieldInSuperClass).someSuperClassMethod();

 // Test when there are multiple fields annotated with @InjectMocks, and their candidates are not found by type or name
 TestClassWithMultipleFieldsAndNoMatching testClassWithMultipleFieldsAndNoMatching = new TestClassWithMultipleFieldsAndNoMatching();
 defaultInjectionEngine.injectMocksOnFields(Arrays.asList(fieldAnnotatedWithInjectMocks, fieldInSuperClass), mocks, testClassWithMultipleFieldsAndNoMatching);
 verifyZeroInteractions(mocks);

 // Test when there is a MockitoException during initialization of a field annotated with @InjectMocks
 TestClassWithInitException testClassWithInitException = new TestClassWithInitException();
 try {
 defaultInjectionEngine.injectMocksOnFields(Arrays.asList(ReflectionUtils.getFieldAnnotatedWithInjectMocks(testClassWithInitException.getClass(), "fieldAnnotatedWithInjectMocks")), mocks, testClassWithInitException);
 } catch (MockitoException e) {
 new Reporter().cannotInitializeForInjectMocksAnnotation(ReflectionUtils.getFieldAnnotatedWithInjectMocks(testClassWithInitException.getClass(), "fieldAnnotatedWithInjectMocks").getName(), e);
 }

 // Test when a field annotated with @InjectMocks is already injected
 TestClassWithInjectedField testClassWithInjectedField = new TestClassWithInjectedField();
 defaultInjectionEngine.injectMocksOnFields(Arrays.asList(fieldAnnotatedWithInjectMocks, fieldInAnotherClass), mocks, testClassWithInjectedField);
 verify(mockFieldAnnotatedWithInjectMocks).someMethod();
 verify(mockFieldInAnotherClass).anotherMethod();
 verify(mockFieldAnnotatedWithInjectMocks).someMethodCall();
 }

 private static class TestClass {
 private Field fieldAnnotatedWithInjectMocks;
 }

 private static class TestClassWithoutInjectMocks {
 // empty class
 }

 private static class TestClassWithSuperClass {
 private Field fieldInSuperClass;
 }

 private static class TestClassWithAnotherClass {
 private Field fieldInAnotherClass;
 }

 private static class TestClassWithNoMatchingType {
 private String fieldAnnotatedWithInjectMocks;
 }

 private static class TestClassWithNameMatchingType {
 private String fieldAnnotatedWithInjectMocks;
 private String anotherField;
 }

 private static class TestClassWithMultipleFields {
 private String field1;
 private String field2;
 }

 private static class TestClassWithMultipleFieldsAndNameMatching {
 private String field1;
 private SomeOtherClass field2;
 }

 private static class TestClassWithMultipleFieldsAndNoMatching {
 private String field1;
 private SomeOtherClass field2;
 }

 private static class TestClassWithInitException {
 private SomeExceptionField fieldAnnotatedWithInjectMocks;
 }

 private static class SomeOtherClass {
 private void someSuperClassMethod() {
 // empty method
 }

 private void anotherMethod() {
 // empty method
 }

 public void someMethod() {
 throw new RuntimeException("This is an exception");
 }

 public void someMethodCall() {
 new Answer<Void>() {
 @Override
 public Void answer(InvocationOnMock invocation) throws Throwable {
 ((TestClassWithInitException) invocation.getMock()).fieldAnnotatedWithInjectMocks.someMethod();
 return null;
 }
 }.answer(mockFieldAnnotatedWithInjectMocks.someMethod);
 }
 }

 private static class Reporter {
 public void cannotInitializeForInjectMocksAnnotation(String fieldName, MockitoException e) {
 // empty method
 }
 }

 private static class ReflectionUtils {
 public static Field getFieldAnnotatedWithInjectMocks(Object clazz, String fieldName) {
 Field field = null;
 for (Field fieldDeclaration : clazz.getClass().getDeclaredFields()) {
 if (fieldDeclaration.getName().equals(fieldName) && fieldDeclaration.isAnnotationPresent(InjectMocks.class)) {
 field = fieldDeclaration;
 break;
 }
 }
 return field;
 }
 }
}