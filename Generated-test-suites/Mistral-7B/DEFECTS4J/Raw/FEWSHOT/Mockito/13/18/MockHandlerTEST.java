package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MockHandlerTest {

 MockSettingsImpl mockSettings;
 MockHandler<Object> mockHandler;
 InvocationContainer invocationContainer;
 OngoingStubbingImpl<Object> ongoingStubbing;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 invocationContainer = mockHandler.getInvocationContainer();
 }

 @Test
 public void testHandle_noAnswersForStubbing() throws Throwable {
 Object actual = mockHandler.handle(new InvocationOnMock(this, "method", new Object[0]));
 assertNull(actual);
 }

 @Test
 public void testHandle_answersForStubbing_voidMethod() throws Throwable {
 VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock(Object.class));
 voidMethodStubbable.stubVoid();
 Object mock = mock(Object.class);
 Object actual = mockHandler.handle(new InvocationOnMock(mock, "methodVoid", new Object[0]));
 assertNull(actual);
 }

 @Test
 public void testHandle_answersForStubbing_nonVoidMethod() throws Throwable {
 when(mock(Object.class)).thenReturn("returnValue");
 Answer<Object> answer = argThat(a -> a.answersWith("answerValue"));
 mockHandler.setAnswersForStubbing(new ArrayList<Answer<?>>() {{
 add(answer);
 }});
 Object mock = mock(Object.class);
 Object actual = mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0]));
 assertEquals("answerValue", actual);
 }

 @Test
 public void testHandle_verificationMode() throws Throwable {
 VerificationMode verificationMode = mock(VerificationMode.class);
 Object mock = mock(Object.class);
 mockingProgress.setVerificationMode(verificationMode);
 mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0]));
 verify(verificationMode).verify(eq(mock));
 }

 @Test(expected = RuntimeException.class)
 public void testHandle_verificationModeOnDifferentMock() throws Throwable {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 VerificationMode verificationMode = mock(VerificationMode.class);
 mockingProgress.setVerificationMode(verificationMode);
 mockHandler.handle(new InvocationOnMock(mock1, "method", new Object[0]));
 // This should throw a RuntimeException as verification mode is associated with mock1
 verificationMode.verify(eq(mock2));
 }

 @Test
 public void testHandle_resetInvocationForPotentialStubbing() throws Throwable {
 Object mock = mock(Object.class);
 Answer<Object> answer = argThat(a -> a.answersWith("answerValue"));
 mockHandler.setAnswersForStubbing(new ArrayList<Answer<?>>() {{
 add(answer);
 }});
 Object actual1 = mockHandler.handle(new InvocationOnMock(mock, "method1", new Object[0]));
 assertEquals("answerValue", actual1);
 Object actual2 = mockHandler.handle(new InvocationOnMock(mock, "method2", new Object[0]));
 assertNull(actual2);
 }

 @Test
 public void testHandle_findAnswerFor() throws Throwable {
 Answer<Object> answer = argThat(a -> a.answersWith("answerValue"));
 mockHandler.setAnswersForStubbing(new ArrayList<Answer<?>>() {{
 add(answer);
 }});
 Object mock = mock(Object.class);
 when(mock.method()).thenAnswer(answer);
 Object actual = mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0]));
 assertEquals("answerValue", actual);
 }

 @Test
 public void testHandle_findNoAnswerFor() throws Throwable {
 Object mock = mock(Object.class);
 Object actual = mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0]));
 assertNull(actual);
 }

 @Test
 public void testHandle_findAnswerFor_delegatedMethod() throws Throwable {
 Object mock = mock(Object.class);
 Object delegate = mock(Object.class);
 when(mock.delegate()).thenReturn(delegate);
 when(delegate.method()).thenAnswer(argThat(a -> a.answersWith("answerValue")));
 Object actual = mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0]));
 assertEquals("answerValue", actual);
 }

 @Test
 public void testHandle_findAnswerFor_partialMock() throws Throwable {
 Object mock = mock(Object.class, new Class[] { String.class });
 when(mock.method(eq("stringArgument"))).thenAnswer(argThat(a -> a.answersWith("answerValue")));
 Object actual = mockHandler.handle(new InvocationOnMock(mock, "method", new Object[] { "stringArgument" }));
 assertEquals("answerValue", actual);
 }

 @Test
 public void testHandle_handleException() throws Throwable {
 Object mock = mock(Object.class);
 when(mock.method()).thenThrow(new RuntimeException("Exception message"));
 Object actual = mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0]));
 assertNull(actual);
 }

 @Test
 public void testHandle_handleException_delegateException() throws Throwable {
 Object mock = mock(Object.class);
 Object delegate = mock(Object.class);
 when(mock.delegate()).thenReturn(delegate);
 when(delegate.method()).thenThrow(new RuntimeException("Exception message"));
 Object actual = mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0]));
 assertNull(actual);
 verifyZeroInteractions(mock);
 verifyZeroInteractions(delegate);
 }

 @Test
 public void testHandle_handleException_answeredException() throws Throwable {
 Object mock = mock(Object.class);
 Answer<RuntimeException> exceptionAnswer = argThat(a -> a.answersWith(new RuntimeException("Answer exception message")));
 when(mock.method()).thenAnswer(exceptionAnswer);
 RuntimeException actual = (RuntimeException) mockHandler.handle(new InvocationOnMock(mock, "method", new Object[0]));
 assertEquals("Answer exception message", actual.getMessage());
 }
}