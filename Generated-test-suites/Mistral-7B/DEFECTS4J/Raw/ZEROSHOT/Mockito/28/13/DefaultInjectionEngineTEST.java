package org.mockito.internal.configuration.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.reflection.FieldUtil;
import org.mockito.internal.util.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.Whitebox;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.MockitoStub;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.mockito.internal.configuration.DefaultInjectionEngine.injectMocksOnFields;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Set<Object> mocks;
 private Object testClassInstance;
 private Field testField;
 private Object testFieldInstance;
 private Class testFieldType;

 @Before
 public void setUp() {
 injectionEngine = new DefaultInjectionEngine();
 mocks = new HashSet<>();
 testClassInstance = createTestClassInstance();
 testField = createTestField();
 testFieldInstance = createTestFieldInstance();
 testFieldType = testFieldInstance.getClass();
 }

 @Test
 public void testInjectMocksOnFields() {
 // given
 Set<Field> injectMocksFields = Arrays.asList(testField).stream().collect(Collectors.toSet());
 mocks.add(mock(testFieldType));

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 // then
 verify(testFieldInstance).equals(mocks.iterator().next());
 verifyNoMoreInteractions(testFieldInstance);

 // verify that all mocks were injected and no duplicates were kept
 for (Object mock : mocks) {
 assertNotSame(testFieldInstance, mock);
 }
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFieldsWithMultipleMocks() {
 // given
 Set<Field> injectMocksFields = Arrays.asList(testField, testField).stream().collect(Collectors.toSet());
 mocks.add(mock(testFieldType));
 mocks.add(mock(testFieldType));

 // when
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 // then - exception is thrown
 }

 @Test
 public void testInjectMockCandidateByType() {
 // given
 Class awaitingInjectionClazz = testFieldType.getSuperclass();
 Set<Object> mocksCopy = new HashSet<>(mocks);

 // when
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testFieldInstance);

 // then
 Object injectedMock = mocks.iterator().next();
 assertSame(injectedMock, mocksCopy.iterator().next());
 verify(injectedMock).equals(testFieldInstance);
 }

 @Test
 public void testInjectMockCandidateByName() {
 // given
 Class awaitingInjectionClazz = testFieldType.getSuperclass();
 Set<Object> mocksCopy = new HashSet<>(mocks);

 // when
 Field fieldToInject = createAnotherTestField();
 mocksCopy.add(mock(fieldToInject.getType()));
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testFieldInstance);

 // then
 Object injectedMock = mocks.iterator().next();
 assertNotSame(injectedMock, mocksCopy.iterator().next());
 verify(injectedMock).equals(testFieldInstance);
 }

 @Test
 public void testInjectMockCandidateByTypeAndName() {
 // given
 Class awaitingInjectionClazz = testFieldType.getSuperclass();
 Set<Object> mocksCopy = new HashSet<>(mocks);

 // when - two candidates with the same type and name
 Field fieldToInject1 = createAnotherTestField();
 Field fieldToInject2 = createAnotherTestField();
 mocksCopy.add(mock(fieldToInject1.getType()));
 mocksCopy.add(mock(fieldToInject2.getType()));
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testFieldInstance);

 // then
 Object injectedMock = mocks.iterator().next();
 assertSame(injectedMock, mocksCopy.iterator().next());
 verify(injectedMock).equals(testFieldInstance);
 }

 @Test
 public void testInjectMockCandidateByFieldSetter() {
 // given
 Class awaitingInjectionClazz = testFieldType.getSuperclass();
 Set<Object> mocksCopy = new HashSet<>(mocks);

 // when - mock candidate has a setter method
 Field fieldToInject = createTestFieldWithSetter();
 mocksCopy.add(spy(testFieldType));
 Whitebox.setInternalState(mocksCopy.iterator().next(), "mockedValue", testFieldInstance);
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);

 // then
 Object injectedMock = mocks.iterator().next();
 assertSame(injectedMock, mocksCopy.iterator().next());
 verify(injectedMock).equals(testFieldInstance);
 }

 @Test
 public void testInjectMockCandidateByFieldInjection() {
 // given
 Class awaitingInjectionClazz = testFieldType.getSuperclass();
 Set<Object> mocksCopy = new HashSet<>(mocks);

 // when - mock candidate has no setter method
 Field fieldToInject = createTestFieldWithoutSetter();
 mocksCopy.add(mock(fieldToInject.getType()));
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);

 // then
 Object injectedMock = mocks.iterator().next();
 assertSame(injectedMock, mocksCopy.iterator().next());
 verify(injectedMock).equals(testFieldInstance);
 }

 @Test
 public void testInjectMockCandidateByFieldInjectionWithMultipleCandidates() {
 // given
 Class awaitingInjectionClazz = testFieldType.getSuperclass();
 Set<Object> mocksCopy = new HashSet<>(mocks);

 // when - multiple mock candidates with no setters
 Field fieldToInject1 = createAnotherTestFieldWithoutSetter();
 Field fieldToInject2 = createAnotherTestFieldWithoutSetter();
 mocksCopy.add(mock(fieldToInject1.getType()));
 mocksCopy.add(mock(fieldToInject2.getType()));
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocks, testClassInstance);

 // then - exception is not thrown
 }

 private Object createTestClassInstance() {
 return mock(TestClass.class);
 }

 private Field createTestField() {
 return FieldUtil.getDeclaredField(TestClass.class, "testField");
 }

 private Object createTestFieldInstance() {
 return mock(TestClass_TestField.class);
 }

 private Field createTestFieldWithSetter() {
 return FieldUtil.getDeclaredField(TestClass.class, "testFieldWithSetter");
 }

 private Field createTestFieldWithoutSetter() {
 return FieldUtil.getDeclaredField(TestClass.class, "testFieldWithoutSetter");
 }

 private Field createAnotherTestField() {
 return FieldUtil.getDeclaredField(TestClassAnotherField.class, "testField");
 }

 private Field createAnotherTestFieldWithSetter() {
 return FieldUtil.getDeclaredField(TestClassAnotherField.class, "testFieldWithSetter");
 }

 private static class TestClass {
 @InjectMocks
 private TestClass_TestField testField;
 private TestClass_TestField testFieldWithSetter;
 }

 private static class TestClassAnotherField {
 @InjectMocks
 private TestClass_TestField testField;
 private TestClass_TestFieldAnother testFieldAnother;
 }

 private static class TestClass_TestField {
 }

 private static class TestClass_TestFieldAnother {
 }
}