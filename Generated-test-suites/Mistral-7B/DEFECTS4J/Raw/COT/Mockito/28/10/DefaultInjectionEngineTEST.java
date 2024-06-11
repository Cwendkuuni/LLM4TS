package org.mockito.internal.configuration. DefaultInjectionEngineTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stub.MockSettings;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultInjectionEngineTest {

 private DefaultInjectionEngine injectionEngine;
 private Object testClassInstance;
 private Set<Object> mocks;
 private Set<Field> injectMocksFields;

 @Before
 public void setUp() {
 injectionEngine = new DefaultInjectionEngine();
 testClassInstance = spy(TestClass.class);
 mocks = new HashSet<>();
 mocks.add(mock(MockA.class));
 mocks.add(mock(MockB.class));
 mocks.add(mock(MockC.class));
 injectMocksFields = Arrays.asList(TestClass.class.getDeclaredFields()).stream()
 .filter(field -> field.isAnnotationPresent(InjectMocks.class)).collect(Collectors.toSet());
 }

 @Test
 public void testInjectMocksOnFields() {
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);

 for (Field field : injectMocksFields) {
 Object mock = null;
 try {
 mock = field.get(testClassInstance);
 } catch (IllegalAccessException e) {
 // handle exception
 }
 assertNotNull(mock);
 }
 }

 @Test(expected = MockitoException.class)
 public void testInjectMocksOnFieldsWithException() {
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 // set a mock candidate with a failure in its initialization
 Mockito.doThrow(new RuntimeException("Mock initialization failed")).when(mocks.iterator().next());
 injectionEngine.injectMocksOnFields(injectMocksFields, mocks, testClassInstance);
 }

 @Test
 public void testInjectMockCandidate() {
 Class<?> awaitingInjectionClazz = TestClassAnother.class;
 Set<Object> mocksToBeInjected = new HashSet<>(mocks);
 Object fieldInstance = mock(AwaitingInjectionClass.class);
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksToBeInjected, fieldInstance);

 // check if mock A was injected
 Field mockAPointer = TestClassAnother.class.getDeclaredField("mockA");
 mockAPointer.setAccessible(true);
 Object mockA = mockAPointer.get(testClassInstance);
 assertEquals(mocks.iterator().next(), mockA);
 }

 @Test
 public void testInjectMockCandidateWithMultipleCandidates() {
 Class<?> awaitingInjectionClazz = TestClassAnother.class;
 Set<Object> mocksToBeInjected = new HashSet<>(mocks);
 Object fieldInstance = mock(TestClassAnother.class);
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksToBeInjected, fieldInstance);

 // check if mock B was injected
 Field mockBPointer = TestClassAnother.class.getDeclaredField("mockB");
 mockBPointer.setAccessible(true);
 Object mockB = mockBPointer.get(fieldInstance);
 assertEquals(mocks.iterator().next(), mockB);
 }

 @Test(expected = MockitoException.class)
 public void testInjectMockCandidateWithNoCandidate() {
 Class<?> awaitingInjectionClazz = TestClassAnother.class;
 Set<Object> mocksToBeInjected = new HashSet<>(mocks);
 Object fieldInstance = mock(TestClassAnother.class);
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksToBeInjected, fieldInstance);
 // remove all mocks from the mocks set
 mocksToBeInjected.clear();
 injectionEngine.injectMockCandidate(awaitingInjectionClazz, mocksToBeInjected, fieldInstance);
 }

 @Test
 public void testOrderedInstanceFieldsFrom() {
 Class<?> awaitingInjectionClazz = TestClassAnother.class;
 Field[] fields = injectionEngine.orderedInstanceFieldsFrom(awaitingInjectionClazz);
 assertArrayEquals(new Field[]{awaitingInjectionClazz.getDeclaredField("mockB"), awaitingInjectionClazz.getDeclaredField("mockA")}, fields);
 }
}

class TestClass {
 @InjectMocks
 private MockA mockA;
 @InjectMocks
 private MockB mockB;
}

class TestClassAnother {
 private MockA mockA;
 private MockB mockB;
 @InjectMocks
 private AwaitingInjectionClass awaitingInjectionInstance;

 public MockA getMockA() {
 return mockA;
 }

 public MockB getMockB() {
 return mockB;
 }

 public AwaitingInjectionClass getAwaitingInjectionInstance() {
 return awaitingInjectionInstance;
 }
}

class AwaitingInjectionClass {
 // empty class
}

class MockA {
}

class MockB {
}