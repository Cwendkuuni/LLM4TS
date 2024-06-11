package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls smartNulls;
 private Object mock;

 @Before
 public void setUp() {
 smartNulls = new ReturnsSmartNulls();
 mock = mock(Object.class);
 }

 @Test
 public void testAnswerWithDefaultValue() {
 Object answer = smartNulls.answer(mock(new InvocationOnMock(Object.class, new Method(Object.class, "toString"), new Object[0], new Object[0])));
 assertNotNull(answer);
 }

 @Test
 public void testAnswerWithNull() {
 Object answer = smartNulls.answer(mock(new InvocationOnMock(String[].class, new Method(String[].class, "clone"), new Object[0], new Object[0])));
 assertNull(answer);
 }

 @Test
 public void testAnswerWithSmartNullForToStringMethod() {
 InvocationOnMock invocation = mock(new InvocationOnMock(Object.class, new Method(Object.class, "toString"), new Object[0], new Object[0]));
 Object answer = smartNulls.answer(invocation);
 assertNotNull(answer);
 assertEquals("SmartNull returned by unstubbed toString() method on mock", answer.toString());
 }

 @Test
 public void testAnswerWithSmartNullForMethodWithImposterisedReturnType() {
 ClassImposterizer imposterizer = spy(ClassImposterizer.class);
 imposterizer.canImposterise = (type) -> true;
 ReturnsMoreEmptyValues delegate = spy(new ReturnsMoreEmptyValues());
 ReturnsSmartNulls smartNullsSpy = spy(smartNulls);
 smartNullsSpy.delegate = delegate;

 Object mockWithStringReturnType = mock(String.class);
 InvocationOnMock invocation = mock(new InvocationOnMock(Object.class, new Method(Object.class, "getSmartNullString", mockWithStringReturnType), new Object[0], new Object[0]));
 Object answer = smartNullsSpy.answer(invocation);
 assertNotNull(answer);
 verify(imposterizer).imposterise(any(MethodInterceptor.class), isA(String.class));
 verify(delegate).answer(invocation);
 }

 @Test(expected = NullPointerException.class)
 public void testAnswerWithNullForImposterisedReturnType() {
 ClassImposterizer imposterizer = spy(ClassImposterizer.class);
 imposterizer.canImposterise = (type) -> true;
 ReturnsMoreEmptyValues delegate = spy(new ReturnsMoreEmptyValues());
 ReturnsSmartNulls smartNullsSpy = spy(smartNulls);
 smartNullsSpy.delegate = delegate;

 Object mockWithNullReturnType = mock(Object[].class);
 InvocationOnMock invocation = mock(new InvocationOnMock(Object.class, new Method(Object.class, "getNullArray", mockWithNullReturnType), new Object[0], new Object[0]));
 Object answer = smartNullsSpy.answer(invocation);
 assertNull(answer);
 verify(imposterizer).imposterise(any(MethodInterceptor.class), isA(Object[].class));
 verify(delegate).answer(invocation);
 }
}