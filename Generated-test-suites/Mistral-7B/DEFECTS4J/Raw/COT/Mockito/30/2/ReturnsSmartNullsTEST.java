package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answer;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.answers.ReturnsSmartNulls;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class ReturnsSmartNullsTest {

 @Mock
 private Object mockObject;

 @InjectMocks
 private ReturnsSmartNulls returnsSmartNulls;

 @Before
 public void setup() {
 Mockito.doAnswer(returnsSmartNulls).when(mockObject).someMethod();
 }

 @Test
 public void testAnswerNullableMethod() {
 Object result = returnsSmartNulls.answer(Mockito.mock(new Object() {}).invocation(new Object[]{}));
 assertEquals("SmartNull returned by unstubbed method on mock", result);
 }

 @Test
 public void testAnswerMockableType() {
 ArgumentCaptor<Answer<Object>> answerCaptor = ArgumentCaptor.forClass(Answer.class);
 Mockito.verify(mockObject).someMethod(answerCaptor.capture());
 Answer<Object> capturedAnswer = answerCaptor.getValue();
 Object result = capturedAnswer.answer(Mockito.mock(new Object() {}).invocation(new Object[]{}));
 assertEquals(ReturnsSmartNulls.class, capturedAnswer.getClass());
 assertNull(result);
 }

 @Test(expected = NullPointerException.class)
 public void testAnswerNonMockableType() {
 Object result = returnsSmartNulls.answer(Mockito.mock(String.class).invocation(new Object[]{}));
 assertFalse(result instanceof String); // Since we expect NPE, this check is just to make sure we're not getting a String instead.
 }

 @Test(expected = org.mockito.exceptions.base.SmartNullPointerException.class)
 public void testAnswerToStringMethod() {
 Object result = returnsSmartNulls.answer(Mockito.mock(String.class).invocation(new Object[]{}));
 assertEquals("SmartNull returned by unstubbed toString() method on mock", result.toString());
 }
}