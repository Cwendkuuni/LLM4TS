package org.mockito.internal.test.MockHandlerTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*; // for importing all static methods, like verify, when etc.

public class MockHandlerTest {

 private MockHandler<MockitoTestClass> mockHandler;
 private MockitoTestClass mockitoTestClass;

 @BeforeEach
 public void setup() {
 mockHandler = new MockHandler<>();
 mockitoTestClass = Mockito.mock(MockitoTestClass.class);
 }

 // Testing the constructor
 @Test
 public void testMockHandlerConstructor() {
 assertNotNull(mockHandler);
 }

 // Testing the handle method with no arguments
 @Test
 public void testHandleNoArguments() throws Throwable {
 when(mockitoTestClass.methodWithNoArguments()).thenReturn(42);
 Object result = mockHandler.handle(new MockitoInvocationOnMock(mockitoTestClass, "methodWithNoArguments", null, null));
 assertEquals(42, result);
 }

 // Testing the handle method with arguments
 @Test
 public void testHandleArguments() throws Throwable {
 when(mockitoTestClass.methodWithArguments(1, 2)).thenReturn(3);
 Object[] args = new Object[]{1, 2};
 InvocationOnMock invocation = new MockitoInvocationOnMock(mockitoTestClass, "methodWithArguments", args, null);
 Object result = mockHandler.handle(invocation);
 assertEquals(3, result);
 }

 // Testing the handle method with exception
 @Test
 public void testHandleException() throws Throwable {
 doThrow(new RuntimeException("Test Exception")).when(mockitoTestClass).methodWithException();
 InvocationOnMock invocation = new MockitoInvocationOnMock(mockitoTestClass, "methodWithException", null, null);
 try {
 mockHandler.handle(invocation);
 fail("Expected Exception not thrown.");
 } catch (RuntimeException e) {
 assertEquals("Test Exception", e.getMessage());
 }
 }

 // Testing the voidMethodStubbable method
 @Test
 public void testVoidMethodStubbable() {
 VoidMethodStubbable<MockitoTestClass> stubbable = mockHandler.voidMethodStubbable(mockitoTestClass);
 stubbable.doStub(invocation -> mockitoTestClass.voidMethod());
 verify(mockitoTestClass).voidMethod();
 }

 // Testing the setAnswersForStubbing method
 @Test
 public void testSetAnswersForStubbing() {
 List<Answer> answers = Arrays.asList(invocation -> invocation.getArgumentAt(0, 0), invocation -> 42);
 mockHandler.setAnswersForStubbing(answers);
 Object[] args = new Object[]{new Object(), 42};
 when(mockitoTestClass.methodWithTwoArguments(args)).thenAnswer(i -> i.getArgumentAt(0, 0));
 when(mockitoTestClass.methodWithTwoArguments(args)).thenAnswer(i -> 42);
 InvocationOnMock invocation = new MockitoInvocationOnMock(mockitoTestClass, "methodWithTwoArguments", args, null);
 Object result = mockHandler.handle(invocation);
 assertEquals(args[0], result);
 }

 // Testing the getInvocationContainer method
 @Test
 public void testGetInvocationContainer() {
 InvocationContainer invocationContainer = mockHandler.getInvocationContainer();
 assertNotNull(invocationContainer);
 }
}

class MockitoTestClass {
 void voidMethod() {
 }
 int methodWithNoArguments() {
 return 0;
 }
 int methodWithArguments(int arg1, int arg2) {
 return arg1 + arg2;
 }
 int methodWithException() throws Exception {
 throw new Exception();
 }
}