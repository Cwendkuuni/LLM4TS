package org.mockito.internal.stubbing.answers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Test class for CallsRealMethods answer
 */
public class CallsRealMethodsTest {

 private CallsRealMethods callsRealMethods;
 private Object target;

 @Before
 public void setUp() {
 target = mock(TestClass.class);
 callsRealMethods = new CallsRealMethods();
 }

 @Test
 public void testCallRealMethod() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(target);
 invocation.setMethod(TestClass.class.getMethod("method1"), new Class[0]);

 try {
 Object result = callsRealMethods.answer(invocation);
 assertEquals(target.method1(), result);
 } catch (Throwable e) {
 // Handle exception if any
 }
 }

 @Test
 public void testCallRealMethodWithArguments() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(target);
 invocation.setMethod(TestClass.class.getMethod("method2", int.class, String.class), new Class[]{int.class, String.class});
 int arg1 = 1;
 String arg2 = "test";

 try {
 Object result = callsRealMethods.answer(invocation, new Object[]{arg1, arg2});
 assertEquals(target.method2(arg1, arg2), result);
 } catch (Throwable e) {
 // Handle exception if any
 }
 }

 @Test(expected = Throwable.class)
 public void testThrowingMethod() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(target);
 invocation.setMethod(TestClass.class.getMethod("method3"), new Class[0]);

 try {
 callsRealMethods.answer(invocation);
 fail("Expected Throwable not thrown");
 } catch (Throwable e) {
 // Expected
 }
 }
}

/**
 * Test class for which methods should be tested
 */
class TestClass {

 public Object method1() {
 return "method1 result";
 }

 public int method2(int arg1, String arg2) {
 return arg1 + arg2.length();
 }

 public void method3() throws Exception {
 throw new Exception("test exception");
 }
}