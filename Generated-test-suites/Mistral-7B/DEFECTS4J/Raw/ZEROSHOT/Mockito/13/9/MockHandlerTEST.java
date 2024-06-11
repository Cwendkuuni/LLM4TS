import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWithoutSideEffects;
import org.mockito.stubbing.Stub;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockHandlerTestSubject> mockHandler;
 private MockHandler<MockHandlerTestSubject> mockHandlerSpy;
 private MockHandler<MockHandlerTestSubject> oldMockHandler;
 private MockSettingsImpl mockSettings;
 private InvocationOnMock invocation;
 private StubbedInvocationMatcher stubbedInvocation;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 mockHandlerSpy = mock(MockHandler.class, mockSettings);
 oldMockHandler = mockHandler;
 invocation = Mockito.mock(InvocationOnMock.class);
 }

 @Test
 public void testHandleInvocationWithNoStubbingOrVerification() {
 Object returnValue = mockHandler.handle(invocation);
 assertEquals(mockSettings.getDefaultAnswer().answer(invocation), returnValue);
 }

 @Test
 public void testHandleInvocationWithStubbing() {
 Answer<Object> answer = Mockito.answer(new Object());
 mockHandler.setAnswersForStubbing(List.of(answer));
 Object returnValue = mockHandler.handle(invocation);
 assertEquals(answer.answer(invocation), returnValue);
 }

 @Test
 public void testHandleInvocationWithVerification() {
 ArgumentMatcher<Object> argumentMatcher = ArgumentMatchers.eq(1L);
 verify(mockHandlerSpy).handle(Mockito.argThat(argumentMatcher));
 }

 @Test(expected = AssertionError.class)
 public void testHandleInvocationWithVerificationOnWrongMock() {
 ArgumentMatcher<Object> argumentMatcher = ArgumentMatchers.eq(1L);
 verify(mockHandler).handle(Mockito.argThat(argumentMatcher));
 }

 @Test
 public void testHandleInvocationWithStubbingAndVerification() {
 ArgumentMatcher<Object> argumentMatcher = ArgumentMatchers.eq(1L);
 when(mockHandlerSpy.handle(invocation)).thenReturn(1L);
 mockHandler.setAnswersForStubbing(List.of(Answer.callRealMethod()));
 Object returnValue = mockHandler.handle(invocation);
 assertEquals(1L, returnValue);
 verify(mockHandlerSpy).handle(invocation);
 }

 @Test
 public void testHandleInvocationWithStubbingAndVerifyNoMoreInteractions() {
 ArgumentMatcher<Object> argumentMatcher = ArgumentMatchers.eq(1L);
 when(mockHandlerSpy.handle(invocation)).thenReturn(1L);
 mockHandler.setAnswersForStubbing(List.of(Answer.callRealMethod()));
 Object returnValue = mockHandler.handle(invocation);
 assertEquals(1L, returnValue);
 verifyNoMoreInteractions(mockHandlerSpy);
 }

 @Test
 public void testHandleInvocationWithStubbingAndVerifyPartially() {
 ArgumentMatcher<Object> argumentMatcher = ArgumentMatchers.eq(1L);
 when(mockHandlerSpy.handle(invocation)).thenReturn(1L);
 Answer<Void> answer = Mockito.doNothing().answer(Mockito.any());
 mockHandler.setAnswersForStubbing(List.of(answer));
 Object returnValue = mockHandler.handle(invocation);
 assertEquals(1L, returnValue);
 verify(mockHandlerSpy).handle(invocation);
 }

 @Test
 public void testHandleInvocationWithStubbingAndVerifyWithAnswer() {
 ArgumentMatcher<Object> argumentMatcher = ArgumentMatchers.eq(1L);
 when(mockHandlerSpy.handle(invocation)).thenAnswer(new AnswerWithoutSideEffects<InvocationOnMock>() {
 @Override
 public Object answer(InvocationOnMock invocation) {
 return 1L;
 }
 });
 Object returnValue = mockHandler.handle(invocation);
 assertEquals(1L, returnValue);
 verify(mockHandlerSpy).handle(invocation);
 }
}