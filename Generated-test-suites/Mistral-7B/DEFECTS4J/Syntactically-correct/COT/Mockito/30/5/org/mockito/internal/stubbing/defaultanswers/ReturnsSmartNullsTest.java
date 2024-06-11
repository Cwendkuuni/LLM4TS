package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;

 @Before
 public void setup() {
 returnsSmartNulls = new ReturnsSmartNulls();
 }

 // Testing the constructor
 @Test(expected = Exception.class)
 public void testThrowsExceptionWhenInvokedWithNull() {
 returnsSmartNulls = new ReturnsSmartNulls(null);
 }

 // Basic test case for answer method
 @Test
 public void testAnswerMethod_ReturnsNull_WhenInvokedOnNonMockableType() {
 Object result = returnsSmartNulls.answer(mock(Object.class));
 assertNull(result);
 }

 @Test
 public void testAnswerMethod_ReturnsSmartNull_WhenInvokedOnMockableType() {
 Object mock = mock(String.class);
 String result = (String) returnsSmartNulls.answer(mock(String.class));
 assertNotNull(result);
 assertEquals("SmartNull returned by unstubbed method on mock", result.getClass().getName());
 }

 // Testing ThrowingInterceptor
 @Test
 public void testThrowingInterceptor_ReturnsToString_WhenInvokedOnToStringMethod() {
 String mock = mock(String.class);
 when(mock.toString()).thenAnswer(returnsSmartNulls);
 String result = mock.toString();
 assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
 }

 @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
 public void testThrowingInterceptor_ThrowsSmartNullPointerException_WhenInvokedOnNonToStringMethod() {
 String mock = mock(String.class);
 when(mock.hashCode()).thenAnswer(returnsSmartNulls);
 mock.hashCode();
 }

 // Testing delegate
 @Captor
 ArgumentCaptor<InvocationOnMock> invocationCaptor;

 @Test
 public void testDelegate_AnswersEmptyValuesByDefault() {
 String mock = mock(String.class);
 when(mock.length()).thenAnswer(returnsSmartNulls);
 verifyNoMoreInteractions(mock);
 }

 @Test
 public void testDelegate_AnswersSmartNulls_WhenInvokedOnNonMockableType() {
 Object result = returnsSmartNulls.answer(mock(Object.class));
 assertNull(result);
 }

 @Test
 public void testDelegate_AnswersDefaultReturnValue_WhenInvokedOnMockableType() {
 String mock = mock(String.class);
 when(mock.length()).thenAnswer(returnsSmartNulls);
 verify(mock).length();
 }

}