package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private MyClass myMock;

 @Before
 public void setup() {
 returnsSmartNulls = new ReturnsSmartNulls();
 myMock = mock(MyClass.class, returnsSmartNulls);
 }

 @Test
 public void testReturnsSmartNullForUnstubbedMethod() {
 MyOtherClass otherMock = mock(MyOtherClass.class);
 when(myMock.someMethod(ArgumentMatchers.any(MyOtherClass.class), ArgumentMatchers.anyInt()))
 .thenReturn(otherMock);

 assertEquals("SmartNull returned by unstubbed someMethod method on mock", myMock.someMethod(otherMock, 1));
 }

 @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
 public void testThrowsSmartNullPointerExceptionWhenUnstubbedMethodReturnsNull() {
 when(myMock.anotherMethod()).thenReturn(null);
 myMock.anotherMethod();
 }

 @Test
 public void testSmartNullIsReturnedForFinalClass() {
 final String FINAL_STRING = "Final String";
 String result = myMock.getFinalString();
 assertEquals(null, result);
 }

 @Test
 public void testSmartNullIsReturnedForNonMockableType() {
 int result = myMock.anInteger();
 assertEquals(0, result);
 }

 @Test
 public void testSmartNullIsReturnedForToStringMethod() {
 assertEquals("SmartNull returned by unstubbed toString method on mock", myMock.toString());
 }
}

class MyClass {
 public MyOtherClass someMethod(MyOtherClass other, int i) {
 return other;
 }

 public String anotherMethod() {
 return null;
 }

 public final String getFinalString() {
 return "Final String";
 }

 public int anInteger() {
 return 0;
 }
}

class MyOtherClass {
 // empty class
}