package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CallsRealMethodsTest {

 // Define a test class with methods under test
 class TestClass {
 private CallsRealMethods callsRealMethods;

 public void testMethod1(CallsRealMethods callsRealMethods) {
 this.callsRealMethods = callsRealMethods;
 callsRealMethods.someMethod();
 }

 public void testMethod2() {
 someMethod();
 }

 private void someMethod() {
 System.out.println("Real method called.");
 }
 }

 @Test
 public void testCallsRealMethod() throws Exception {
 // Create mock for CallsRealMethods
 CallsRealMethods callsRealMethodsMock = Mockito.mock(CallsRealMethods.class);

 // Create instance of TestClass with mocked CallsRealMethods
 TestClass testClass = new TestClass();
 testClass.testMethod1(callsRealMethodsMock);

 // Verify that real method was called
 verify(callsRealMethodsMock).answer(Mockito.any(InvocationOnMock.class));
 }

 @Test(expected = Throwable.class)
 public void testExceptionThrownByRealMethod() throws Exception {
 // Create mock for CallsRealMethods
 CallsRealMethods callsRealMethodsMock = Mockito.mock(CallsRealMethods.class);

 // Stub exception thrown by real method
 doThrow(new RuntimeException("Test exception")).when(callsRealMethodsMock).answer(Mockito.any(InvocationOnMock.class));

 // Create instance of TestClass with mocked CallsRealMethods
 TestClass testClass = new TestClass();
 testClass.testMethod1(callsRealMethodsMock);
 }

 @Test
 public void testCallsRealMethodInSecondaryMethod() throws Exception {
 // Create mock for CallsRealMethods
 CallsRealMethods callsRealMethodsMock = Mockito.mock(CallsRealMethods.class);
 TestClass testClass = new TestClass();
 testClass.callsRealMethods = callsRealMethodsMock;

 // Verify that real method is called in testMethod2
 verify(callsRealMethodsMock).answer(Mockito.atLeastOnce(Mockito.eq(Mockito.any(InvocationOnMock.class))));

 // Call testMethod2
 testClass.testMethod2();
 }
}