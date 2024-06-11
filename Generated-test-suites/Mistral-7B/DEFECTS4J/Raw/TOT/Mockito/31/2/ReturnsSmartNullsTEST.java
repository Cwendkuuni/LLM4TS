package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private MyClass myClass;

 @Before
 public void setUp() {
 myClass = mock(MyClass.class, returnsSmartNulls);
 returnsSmartNulls = new ReturnsSmartNulls();
 }

 @Test
 public void testStubbedMethod() {
 when(myClass.stubbedMethod()).thenReturn("expected value");
 String result = myClass.stubbedMethod();
 assertEquals("expected value", result);
 }

 @Test(expected = NullPointerException.class)
 public void testUnstubbedMethodReturnsNull() {
 String result = myClass.unstubbedMethod();
 fail("Expected NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testUnstubbedMethodThrowsException() throws Exception {
 when(myClass.unstubbedMethod()).thenThrow(new Exception());
 myClass.unstubbedMethod();
 }

 @Test(expected = NullPointerException.class)
 public void testUnstubbedMethodThrowsNPE_through_toString() throws Exception {
 String result = (String) myClass.unstubbedMethod();
 fail("Expected NullPointerException");
 }

 @Test
 public void testUnstubbedMethodReturnsSmartNull() {
 String result = myClass.unstubbedMethod();
 verify(returnsSmartNulls).intercept(ArgumentMatchers.any(InvocationOnMock.class));
 assertEquals("SmartNull returned by unstubbed method on mock", result.toString());
 }

 @Test
 public void testDelegateReturnsMoreEmptyValues() {
 Answer<String> delegate = mock(Answer.class);
 ReturnsSmartNulls returnsSmartNullsWithDelegate = new ReturnsSmartNulls() {{
 this.delegate = delegate;
 }};
 when(delegate.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenReturn("delegate return value");
 String result = myClass.unstubbedMethod();
 assertEquals("delegate return value", result);
 }

 @Test(expected = IllegalStateException.class)
 public void testCannotImposteriseFinalClass() {
 when(myClass.unstubbedFinalMethod()).thenReturn(new Object());
 verify(myClass, ArgumentMatchers.never()).unproxy();
 }
}

class MyClass {
 private int value;

 public String unstubbedMethod() {
 return null;
 }

 public String stubbedMethod() {
 return "value";
 }

 public final String unstubbedFinalMethod() {
 return "final value";
 }
}