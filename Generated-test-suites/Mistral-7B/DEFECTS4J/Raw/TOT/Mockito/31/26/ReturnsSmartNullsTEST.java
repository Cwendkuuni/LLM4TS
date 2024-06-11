package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 private Object mock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 returnsSmartNulls = new ReturnsSmartNulls();
 mock = mock(String.class, returnsSmartNulls);
 }

 @Test
 public void testSmartNullForStringMethod() {
 String expected = "expected";
 ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
 mock.foo(stringArgumentCaptor.capture());
 verify(mock).toString(); // should call unstubbed method and return SmartNull
 Assert.assertEquals("SmartNull returned by unstubbed foo() method on mock", stringArgumentCaptor.getValue());
 }

 @Test
 public void testNullReturnValue() {
 mock.foo(null);
 // method foo() should not be called
 verify(mock, Mockito.never()).foo(null);
 }

 @Test
 public void testSmartNullForFinalClass() {
 final Object finalObject = new Object();
 mock.foo(finalObject);
 // method foo() should be called with the final object
 verify(mock).foo(finalObject);
 // but the return value will be null
 Assert.assertNull(mock.foo(finalObject));
 }

 @Test(expected = NullPointerException.class)
 public void testSmartNullForToStringMethod() {
 mock.toString();
 }
}