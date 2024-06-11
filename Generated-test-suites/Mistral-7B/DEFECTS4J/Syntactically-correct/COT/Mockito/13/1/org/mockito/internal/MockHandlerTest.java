package org.mockito.internal.test; // Assume this package is used for test classes

import static org.mockito.ArgumentMatchers.*; // Assuming you have ArgumentMatchers imported for easier matching
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.stubbing.AnswerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.verification.VerificationDataImpl;

@RunWith(MockitoJUnitRunner.class)
public class MockHandlerTest {

 private MockHandler<MockitoTestObject> mockHandler;
 @Mock
 private MockSettingsImpl mockSettings;
 @Captor
 private ArgumentCaptor<InvocationOnMock> invocationCaptor;

 @Before
 public void setUp() {
 mockHandler = new MockHandler<>(mockSettings);
 }

 @Test
 public void testHandle_NoStubbingOrVerification_ReturnsDefaultAnswer() throws Throwable {
 // Given
 Object defaultAnswer = new Object();
 when(mockSettings.getDefaultAnswer()).thenReturn(new AnswerImpl(defaultAnswer));

 // When
 Object result = mockHandler.handle(Mockito.mock(Invocation.class));

 // Then
 assertEquals(defaultAnswer, result);
 }

 @Test
 public void testHandle_StubbingWithAnswer_ReturnsAnswer() throws Throwable {
 // Given
 final Object answer = new Object();
 Answer mockAnswer = Mockito.mock(Answer.class);
 doAnswer(mockAnswer).when(mockSettings).getDefaultAnswer();

 // When
 doAnswer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArguments()[0];
 }
 }).when(mockHandler).handle(Mockito.argThat(argThat((Invocation) -> argThat(invocation, isA(Invocation.class))
 .and(argThat(invocation, argumentEquals(new Object[] { "testArg" })))))
 );

 // When
 Object result = mockHandler.handle(Mockito.mock(Invocation.class, withArguments("testArg")));

 // Then
 assertEquals("testArg", result);
 }

 @Test
 public void testHandle_StubbingWithVoidAnswer_ReturnsNull() throws Throwable {
 // Given
 doAnswer(Mockito.doNothing()).when(mockHandler).handle(any(Invocation.class));

 // When
 Object result = mockHandler.handle(Mockito.mock(Invocation.class));

 // Then
 assertNull(result);
 }

 @Test
 public void testHandle_VerificationModeIsNotNull_VerifiesCorrectMock() throws Throwable {
 // Given
 Object mock = Mockito.mock(Object.class);
 Invocation invocation = Mockito.mock(Invocation.class, withArguments(mock));
 VerificationMode verificationMode = Mockito.verify(mock);

 // When
 mockHandler.handle(invocation);

 // Then
 verify(mock).toString();
 }

 @Test
 public void testHandle_VerificationModeIsNull_DoesNotVerify() throws Throwable {
 // Given
 Object mock = Mockito.mock(Object.class);
 Object arg = new Object();
 Invocation invocation = Mockito.mock(Invocation.class, withArguments(arg));

 // When
 mockHandler.handle(invocation);

 // Then
 verifyZeroInteractions(mock);
 }

 @Test
 public void testHandle_MultipleInvocations_VerifiesCorrectly() throws Throwable {
 // Given
 Object mock = Mockito.mock(Object.class);
 Object arg1 = new Object();
 Object arg2 = new Object();
 Invocation invocation1 = Mockito.mock(Invocation.class, withArguments(arg1));
 Invocation invocation2 = Mockito.mock(Invocation.class, withArguments(arg2));
 doAnswer(invocation -> invocation.getArguments()[0]).when(mockHandler).handle(invocation1);
 doAnswer(invocation -> invocation.getArguments()[0]).when(mockHandler).handle(invocation2);

 // When
 mockHandler.handle(invocation1);
 mockHandler.handle(invocation2);

 // Then
 verify(mock).toString();
 verify(mock).toString();
 }

 @Test(expected = AssertionError.class)
 public void testHandle_VerifyingWrongMock_ThrowsAssertionError() throws Throwable {
 // Given
 Object wrongMock = new Object();
 VerificationMode verificationMode = Mockito.verify(wrongMock);

 // When
 mockHandler.handle(Mockito.mock(Invocation.class));

 // Then
 // AssertionError should be thrown by Mockito as verification was started on a different mock
 }

 @Test
 public void testHandle_OngoingStubbing_SetsCorrectly() throws Throwable {
 // Given
 final Object answer = new Object();
 Object mock = Mockito.mock(Object.class);
 Object arg = new Object();
 Invocation invocation = Mockito.mock(Invocation.class, withArguments(arg));
 OngoingStubbingImpl<Object> ongoingStubbing = Mockito.stub(mock, "method").toReturn(answer);
 when(mockSettings.getDefaultAnswer()).thenReturn(new AnswerImpl(answer));

 // When
 mockHandler.handle(invocation);

 // Then
 assertEquals(ongoingStubbing, invocationContainerImpl.getOngoingStubbing());
 }

 @Test
 public void testHandle_ResetInvocationForPotentialStubbing_ResetsCorrectly() throws Throwable {
 // Given
 final Object answer = new Object();
 Object mock = Mockito.mock(Object.class);
 Object arg = new Object();
 Invocation invocation1 = Mockito.mock(Invocation.class, withArguments(arg));
 Invocation invocation2 = Mockito.mock(Invocation.class, withArguments(arg));
 doAnswer(invocation1::getArgumentAt).when(mockHandler).handle(invocation1);
 doAnswer(answer).when(mockHandler).handle(invocation2);

 // When
 mockHandler.handle(invocation1);
 mockHandler.handle(invocation2);
 mockHandler.handle(invocation1); // reset invocation

 // Then
 assertEquals(answer, mockHandler.handle(invocation2));
 }

 @Test
 public void testHandle_ExceptionInMethod_ThrowsException() throws Throwable {
 // Given
 final RuntimeException exception = new RuntimeException("Test Exception");
 Object mock = Mockito.mock(Object.class);
 Object arg = new Object();
 Invocation invocation = Mockito.mock(Invocation.class, withArguments(arg));
 doThrow(exception).when(mockSettings).getDefaultAnswer();

 // When
 doThrow(new RuntimeException("Different Exception")).when(mockHandler).handle(invocation);
 mockHandler.handle(invocation);

 // Then
 throw exception; // Exception should be rethrown
 }

 @Test
 public void testHandle_ExceptionInMethod_CatchesAndReturnsDefaultAnswer() throws Throwable {
 // Given
 final RuntimeException exception = new RuntimeException("Test Exception");
 Object mock = Mockito.mock(Object.class);
 Object arg = new Object();
 Invocation invocation = Mockito.mock(Invocation.class, withArguments(arg));
 doThrow(exception).when(mockSettings).getDefaultAnswer();
 doAnswer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "Default Answer";
 }
 }).when(mockHandler).handle(invocation);

 // When
 Object result = mockHandler.handle(invocation);

 // Then
 assertEquals("Default Answer", result);
 }

 @Test
 public void testHandle_ExceptionInStubbing_CatchesAndReturnsDefaultAnswer() throws Throwable {
 // Given
 final RuntimeException exception = new RuntimeException("Test Exception");
 Object mock = Mockito.mock(Object.class);
 Object arg = new Object();
 Invocation invocation = Mockito.mock(Invocation.class, withArguments(arg));
 doThrow(exception).when(mockHandler).handle(invocation);
 doAnswer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "Default Answer";
 }
 }).when(mockHandler).handle(any(Invocation.class));

 // When
 Object result = mockHandler.handle(invocation);

 // Then
 assertEquals("Default Answer", result);
 }

 @Test
 public void testHandle_VerifyNoMoreInteractions_ThrowsAssertionError() throws Throwable {
 // Given
 Object mock = Mockito.mock(Object.class);
 Object arg = new Object();
 Invocation invocation = Mockito.mock(Invocation.class, withArguments(arg));
 doAnswer(invocation -> invocation.getArguments()[0]).when(mockHandler).handle(invocation);
 verifyNoMoreInteractions(mock);

 // When
 mockHandler.handle(invocation);

 // Then
 // AssertionError should be thrown by Mockito
 }
}