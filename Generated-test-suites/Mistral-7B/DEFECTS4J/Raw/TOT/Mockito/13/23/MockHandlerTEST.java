package org.mockito.internal.test.mockhandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockitoTestClass> mockHandler;
 private MockitoTestClass mockitoTestClass;

 @Before
 public void setup() {
 mockitoTestClass = mock(MockitoTestClass.class);
 mockHandler = new MockHandler<>(mockitoTestClass);
 }

 @Test
 public void testHandleVoidStubbing() {
 // Given
 when(mockitoTestClass.voidMethod(anyInt())).thenReturn(42);

 // When
 int result = mockHandler.handle(new InvocationOnMock(mockitoTestClass, "voidMethod", new Object[]{1}, new Object[0]));

 // Then
 verify(mockitoTestClass).voidMethod(1); // verify the invocation
 verify(mockitoTestClass, never()).someOtherMethod(); // verify no other invocation

 // Then result should be 42
 assertEquals(42, result);
 }

 @Test
 public void testHandleAnswerStubbing() {
 // Given
 when(mockitoTestClass.someMethod(anyInt())).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArgument(0) * 2;
 }
 });

 // When
 int result = mockHandler.handle(new InvocationOnMock(mockitoTestClass, "someMethod", new Object[]{3}, new Object[0]));

 // Then
 verify(mockitoTestClass).someMethod(3); // verify the invocation

 // Then result should be 6
 assertEquals(6, result);
 }

 @Test(expected = RuntimeException.class)
 public void testHandleVerifyFailedOnWrongMock() {
 // Given
 MockHandler<SomeOtherMockClass> otherMockHandler = new MockHandler<>(mock(SomeOtherMockClass.class));

 // When and Then
 try {
 verify(mockitoTestClass).someMethod(1);
 mockHandler.handle(new InvocationOnMock(mockitoTestClass, "someMethod", new Object[]{1}, new Object[0]));
 } catch (Throwable e) {
 // swallow the exception from handle() method
 }

 // Then verify should fail on wrong mock
 verify(mockitoTestClass, never()).someMethod(1);
 verify(otherMockHandler).someMethod(1); // verify this should fail
 }

 @Test
 public void testHandleResetInvocationForPotentialStubbing() {
 // Given
 when(mockitoTestClass.someMethod(anyInt())).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 throw new RuntimeException("Test exception");
 }
 });

 // When
 when(mockitoTestClass.someOtherMethod(anyInt())).thenReturn(42);
 int result1 = mockHandler.handle(new InvocationOnMock(mockitoTestClass, "someMethod", new Object[]{1}, new Object[0]));
 int result2 = mockHandler.handle(new InvocationOnMock(mockitoTestClass, "someOtherMethod", new Object[]{1}, new Object[0]));

 // Then
 verify(mockitoTestClass, never()).someMethod(1); // should not be called due to exception
 verify(mockitoTestClass).someOtherMethod(1); // called normally

 // Then result should be 42
 assertEquals(42, result2);

 // When resetting invocation for potential stubbing
 mockHandler.handle(new InvocationOnMock(mockitoTestClass, "someMethod", new Object[]{1}, new Object[0]));
 int result3 = mockHandler.handle(new InvocationOnMock(mockitoTestClass, "someOtherMethod", new Object[]{1}, new Object[0]));

 // Then result should be 42 as well
 assertEquals(42, result3);
 }

 @Test
 public void testHandleAnswerWithArgumentsCapture() {
 // Given
 List<Integer> list = new ArrayList<>();
 when(mockitoTestClass.someMethod(anyInt())).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 list.add((Integer) invocation.getArgument(0));
 return null;
 }
 });

 // When
 mockHandler.handle(new InvocationOnMock(mockitoTestClass, "someMethod", new Object[]{1}, new Object[0]));
 mockHandler.handle(new InvocationOnMock(mockitoTestClass, "someMethod", new Object[]{2}, new Object[0]));

 // Then
 assertEquals(1, list.size());
 assertEquals(1, list.get(0));
 assertEquals(2, list.get(1));
 }
}

class MockitoTestClass {
 void voidMethod() {}
 int someMethod(int i) { return i; }
 int someOtherMethod(int i) { return i * 2; }
}

class SomeOtherMockClass {
 // empty class
}