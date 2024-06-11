package org.mockito.internal.configuration. DefaultInjectionEngineTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.DefaultInjectionEngine;
import org.mockito.internal.configuration.injection.MockCandidateFilter;
import org.mockito.internal.configuration.injection.NameBasedCandidateFilter;
import org.mockito.internal.configuration.injection.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.reflection.FieldAccessOrder;
import org.mockito.internal.reflection.FieldInitializer;
import org.mockito.internal.util.reflection.FieldUtil;
import org.mockito.internal.util.reflection.Reflections;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.assertThrows;

public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine defaultInjectionEngine;
 private Object testObject;
 private Set<Object> mocks;
 private Set<Field> injectMocksFields;
 private Field testField1;
 private Field testField2;
 private Field testField3;

 @Before
 public void setup() {
 testObject = new TestClass();
 mocks = Reflections.newSet(mock(FirstMock.class), mock(SecondMock.class), mock(ThirdMock.class));
 injectMocksFields = Arrays.asList(testField1, testField2, testField3).stream().map(Field::declaredField).collect(Collectors.toSet());
 defaultInjectionEngine = new DefaultInjectionEngine();
 }

 @Test
 public void testInjectMocksOnFields() {
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject);

 // Test field1 has been injected with FirstMock
 verify(testField1).set(testObject, firstMock);
 // Test field2 has been injected with SecondMock
 verify(testField2).set(testObject, secondMock);
 // Test field3 has been injected with ThirdMock
 verify(testField3).set(testObject, thirdMock);
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFieldsWithDuplicateCandidate() {
 // Set up a duplicate mock candidate for testField1
 Field duplicateField1 = Reflections.getDeclaredField(TestClass2.class, "testField1");
 FieldUtil.setAccessible(duplicateField1);
 TestClass2 testObject2 = new TestClass2();
 Object duplicateMock = mock(FirstMock.class);

 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject);
 defaultInjectionEngine.injectMocksOnFields(injectMocksFields, mocks, testObject2);

 // This test should fail since there's a duplicate mock candidate for testField1
 }

 @Test
 public void testInjectMocksOnFieldsWithNoMockCandidate() {
 // Set up a field without a mock candidate
 Field noMockField = Reflections.getDeclaredField(TestClass3.class, "testField");
 FieldUtil.setAccessible(noMockField);

 defaultInjectionEngine.injectMocksOnFields(Arrays.asList(noMockField).stream().map(Field::declaredField).collect(Collectors.toSet()), mocks, testObject);

 // Verify no interaction with the noMockField since no mock candidate was found
 verifyZeroInteractions(noMockField);
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFieldsWithException() {
 // Set up a field that throws an exception when initialized
 Field exceptionField = Reflections.getDeclaredField(TestClass4.class, "testField");
 FieldUtil.setAccessible(exceptionField);
 TestClass4 testObject4 = new TestClass4();

 try {
 defaultInjectionEngine.injectMocksOnFields(Arrays.asList(exceptionField).stream().map(Field::declaredField).collect(Collectors.toSet()), mocks, testObject4);
 } catch (MockitoException e) {
 // Expected exception
 }

 // Verify that the exception was thrown
 verify(testObject4, times(1)).getCauses();
 }

 @Test
 public void testInjectMockCandidate() {
 Class<?> awaitingInjectionClazz = TestClass5.class;
 Set<Object> mocksCopy = new HashSet<>(mocks);

 // Test that the first mock candidate (TestClass5TestMock) is injected
 defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksCopy, testObject);
 verify(testObject).setTestClass5TestMock(testObject.getTestClass5TestMock());

 // Test that the second mock candidate (TestClass5OtherMock) is not injected since it's not the first one
 Field testClass5OtherMockField = Reflections.getDeclaredField(TestClass5Other.class, "testField");
 defaultInjectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksCopy, testObject);
 verifyZeroInteractions(testClass5OtherMockField);

 // Test that the first mock candidate is removed from the mocks copy to avoid duplicate injections
 verify(mocksCopy, times(1)).remove(testObject.getTestClass5TestMock());
 }

}

class TestClass {
 @InjectMocks
 private FirstMock testField1;
}

class TestClass2 extends TestClass {
 @InjectMocks
 @SuppressWarnings("unused")
 private FirstMock testField1; // Duplicate field
}

class TestClass3 {
 @InjectMocks
 @SuppressWarnings("unused")
 private Object testField; // No mock candidate
}

class TestClass4 {
 @InjectMocks
 @SuppressWarnings("unused")
 private Throwable testField; // Throws exception
}

class TestClass5 {
 @InjectMocks
 private TestClass5TestMock testField;
}

class TestClass5Other {
 @InjectMocks
 private TestClass5OtherMock testField;
}

interface FirstMock {
}

interface SecondMock {
}

interface ThirdMock {
}

class TestClass5TestMock implements FirstMock {
}

class TestClass5OtherMock implements FirstMock {
}