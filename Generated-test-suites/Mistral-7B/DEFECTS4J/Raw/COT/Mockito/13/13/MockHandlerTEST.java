package org.mockito.internal.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWithMockitoMessage;
import org.mockito.stubbing.AnswerWithThrowable;
import org.mockito.stubbing.Stub;
import org.mockito.stubbing.Stubbing;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class MockHandlerTest {

 private MockHandler<MockitoInvocationHandler> mockHandler;
 private MockSettingsImpl mockSettings;
 private InvocationContainer invocationContainer;
 private MockitoInvocationHandler mockitoInvocationHandler;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 invocationContainer = mockHandler.getInvocationContainer();
 mockitoInvocationHandler = mock(MockitoInvocationHandler.class);
 }

 // Test for handle method with no arguments
 @Test
 public void testHandleNoArguments() throws Throwable {
 Invocation invocation = mock(InvocationOnMock.class);
 when(mockHandler.handle(invocation)).thenReturn(42);
 assertEquals(42, mockHandler.handle(invocation));
 }

 // Test for handle method with arguments
 @Test
 public void testHandleWithArguments() throws Throwable {
 Invocation invocation = mock(InvocationOnMock.class);
 when(mockHandler.handle(invocation)).thenAnswer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return invocation.getArguments()[0];
 }
 });
 String argument = "test";
 when(invocation.getArguments()).thenReturn(new Object[]{argument});
 assertEquals(argument, mockHandler.handle(invocation));
 }

 // Test for handle method with verificationMode
 @Test
 public void testHandleWithVerificationMode() throws Throwable {
 Invocation invocation = mock(InvocationOnMock.class);
 VerificationMode verificationMode = mock(VerificationMode.class);
 when(mockHandler.handle(invocation)).thenReturn(null);
 when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);

 // Test verification with correct mock
 verify(mockitoInvocationHandler).methodToBeVerified(argument);

 // Test verification with different mock
 verify(mockitoInvocationHandler2, never()).methodToBeVerified(argument);
 }

 // Test for handle method with answersForStubbing
 @Test
 public void testHandleWithAnswersForStubbing() throws Throwable {
 Invocation invocation = mock(InvocationOnMock.class);
 List<Answer> answers = Arrays.asList(new AnswerWithMockitoMessage(), new AnswerWithThrowable(new Exception()));
 when(mockHandler.handle(invocation)).thenReturn(null);
 when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
 when(invocationContainer.getAnswersForStubbing()).thenReturn(answers);

 Answer answer = answers.get(0);
 assertEquals("Answer with mockito message", mockHandler.handle(invocation));

 answer = answers.get(1);
 try {
 mockHandler.handle(invocation);
 } catch (Exception e) {
 assertEquals("Exception thrown by answer", e.getMessage(), e.getCause().getMessage());
 }
 }

 // Test for handle method with default answer
 @Test
 public void testHandleWithDefaultAnswer() throws Throwable {
 Invocation invocation = mock(InvocationOnMock.class);
 when(mockSettings.getDefaultAnswer()).thenReturn(new AnswerWithMockitoMessage());
 when(mockHandler.handle(invocation)).thenReturn(null);
 assertEquals("Answer with mockito message", mockHandler.handle(invocation));
 }

 // Test for voidMethodStubbable method
 @Test
 public void testVoidMethodStubbable() throws Throwable {
 T mock = mock(MockitoInvocationHandler.class);
 VoidMethodStubbable<T> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
 Stub stub = stub(mockitoInvocationHandler).methodToBeStubbed();
 voidMethodStubbable.stub(stub);
 verify(mockitoInvocationHandler).methodToBeStubbed();
 }
}