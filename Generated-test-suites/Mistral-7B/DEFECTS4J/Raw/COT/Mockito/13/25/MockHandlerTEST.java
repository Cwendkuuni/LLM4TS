import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.mock;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MockHandlerTest {

 private MockHandler<MockitoTestClass> mockHandler;
 @Mock
 private MockitoTestClass mockitoTestClass;
 @Mock
 private Invocation invocation;
 @Mock
 private VerificationMode verificationMode;
 @Mock
 private Answer answer;
 @Mock
 private List<Answer> answers;
 @Mock
 private MatchersBinder matchersBinder;
 @Mock
 private InvocationContainer invocationContainer;
 @Mock
 private ThreadSafeMockingProgress mockingProgress;
 @Mock
 private MockSettingsImpl mockSettings;

 @Before
 public void setup() {
 mockHandler = new MockHandler<>(mockSettings);
 Mockito.initMocks(this);
 }

 @Test
 public void testHandle_noAnswersOrVerification_returnsDefaultAnswer() throws Throwable {
 given(mockSettings.getDefaultAnswer()).willReturn(answer);
 when(invocationContainer.findAnswerFor(invocation)).thenReturn(null);
 Object result = mockHandler.handle(invocation);
 then(answer).wasCalled();
 assertEquals(answer.answer(invocation), result);
 }

 @Test
 public void testHandle_answerFound_returnsAnswer() throws Throwable {
 given(invocationContainer.findAnswerFor(invocation)).willReturn(answer);
 when(answer.answer(invocation)).thenReturn("answer");
 Object result = mockHandler.handle(invocation);
 then(answer).wasCalled();
 assertEquals("answer", result);
 }

 @Test
 public void testHandle_stubbingVoidMethods_returnsNull() throws Throwable {
 VoidMethodStubbable<MockitoTestClass> voidMethodStubbable = mockHandler.voidMethodStubbable(mockitoTestClass);
 voidMethodStubbable.stubVoid();
 when(invocation.getMethod()).thenReturn(voidMethodStubbable.getMethod());
 Object result = mockHandler.handle(invocation);
 assertNull(result);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_verificationModeNotFound_throwsException() throws Throwable {
 given(mockingProgress.pullVerificationMode()).willReturn(verificationMode);
 doThrow(new IllegalStateException("VerificationMode not found")).when(verificationMode).verify(any());
 when(invocation.getMock()).thenReturn(mockitoTestClass);
 mockHandler.handle(invocation);
 }

 @Test
 public void testHandle_verificationModeMatched_verifiesMethodCall() throws Throwable {
 given(mockingProgress.pullVerificationMode()).willReturn(verificationMode);
 given(verificationMode.getMock()).willReturn(mockitoTestClass);
 given(verificationMode.getMock()).willReturn(same(mockitoTestClass));
 given(invocation.getMethod()).willReturn(same(mockMethod()));
 doNothing().when(mockitoTestClass).methodToBeVerified(eq(anyInt()), isA(Integer[].class));
 mockHandler.handle(invocation);
 then(mockitoTestClass).methodToBeVerified(anyInt(), isA(Integer[].class));
 }

 @Test(expected = AssertionError.class)
 public void testHandle_verificationModeUnequalMocks_throwsException() throws Throwable {
 given(mockingProgress.pullVerificationMode()).willReturn(verificationMode);
 given(verificationMode.getMock()).willReturn(mockitoTestClass1);
 given(invocation.getMock()).willReturn(mockitoTestClass2);
 doThrow(new AssertionError("Different mocks found")).when(verificationMode).verify(any());
 mockHandler.handle(invocation);
 }

 @Test
 public void testHandle_multipleAnswers_returnsFirstAnswer() throws Throwable {
 given(invocationContainer.getAnswersForStubbing()).willReturn(answers);
 given(answers.get(0)).willReturn(answer1);
 given(answers.get(1)).willReturn(answer2);
 when(answer1.answer(invocation)).thenReturn("answer1");
 when(answer2.answer(invocation)).thenReturn("answer2");
 Object result = mockHandler.handle(invocation);
 then(answer1).wasCalled();
 assertEquals("answer1", result);
 }

 @Test
 public void testHandle_invocationReset() throws Throwable {
 given(invocationContainer.getAnswersForStubbing()).willReturn(answers);
 given(answers.get(0)).willReturn(answer1);
 when(answer1.answer(invocation)).thenReturn("answer1");
 when(invocation.getMethod()).thenReturn(same(mockMethod()));
 mockHandler.handle(invocation);
 mockHandler.handle(invocation);
 then(answer1).wasCalledTwice();
 }

 private Method mockMethod() {
 return mock(Method.class);
}
}