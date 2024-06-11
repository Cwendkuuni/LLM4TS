package org.mockitotests.ReturnsSmartNulls;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito. Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;
 @Mock private Object mockObject;

 @Before
 public void setUp() {
 returnsSmartNulls = new ReturnsSmartNulls();
 }

 @Test
 public void testAnswerNullObject() {
 given(mockObject.toString()).willReturn(null);
 Object result = returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));
 Assert.assertEquals("SmartNull returned by unstubbed toString method on mock", result);
 }

 @Test
 public void testAnswerEmptyArray() {
 Object[] result = returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class)).getArguments();
 Assert.assertArrayEquals(new Object[0], result);
 }

 @Test
 public void testAnswerDefaultReturnValue() {
 given(mockObject.toString()).willReturn("Hello World");
 Object result = returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));
 Assert.assertEquals("Hello World", result);
 }

 @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
 public void testAnswerNullReturnType() {
 given(mockObject.toString()).willAnswer(Answers.delegate(returnsSmartNulls));
 Object result = mockObject.toString();
 }

 @Test
 public void testAnswerClassNotMockable() {
 given(mockObject.getClass()).willReturn(String[].class);
 Object result = returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));
 Assert.assertNull(result);
 }

 @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
 public void testAnswerNullReturnTypeWithToString() {
 given(mockObject.toString()).willReturn(null);
 Object result = returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));
 }

 @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
 public void testAnswerNullReturnTypeWithMethodInterceptor() throws Exception {
 given(mockObject.toString()).willAnswer(Mockito.answer(new ThrowingInterceptor(Mockito.mock(InvocationOnMock.class))));
 Object result = mockObject.toString();
 }

 @Test
 public void testAnswerNullReturnTypeWithToStringAndMethodInterceptor() throws Exception {
 given(mockObject.toString()).willAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return null;
 }
 });
 given(mockObject.getClass()).willReturn(String[].class);
 Object result = returnsSmartNulls.answer(Mockito.mock(InvocationOnMock.class));
 Assert.assertNull(result);
 }
}