import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWith;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

 private MockHandler<MockitoTestSubject> mockHandler;
 private MockitoTestSubject mockitoTestSubject;
 private StubbedInvocationMatcher stubbedInvocationMatcher;

 @Before
 public void setup() {
 mockHandler = new MockHandler<>();
 mockitoTestSubject = new MockitoTestSubject();
 stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
 }

 @Test
 public void testHandle_NoAnswersOrVerification_ReturnsDefaultAnswer() throws Throwable {
 Invocation invocation = mock(InvocationOnMock.class);
 when(stubbedInvocationMatcher.matches(any())).thenReturn(stubbedInvocationMatcher);

 mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(stubbedInvocationMatcher);

 Object result = mockHandler.handle(invocation);

 verifyNoInteractions(stubbedInvocationMatcher);

 assertEquals(mockSettings.getDefaultAnswer().answer(invocation), result);
 }

 @Test
 public void testHandle_AnswerProvidedViaStubbing_ReturnsAnswer() throws Throwable {
 Invocation invocation = mock(InvocationOnMock.class);
 when(stubbedInvocationMatcher.matches(any())).thenReturn(stubbedInvocationMatcher);
 when(mockHandler.invocationContainerImpl.getAnswersForStubbing()).thenReturn(Arrays.asList(answer("hello")));
 when(stubbedInvocationMatcher.answer(invocation)).thenReturn("world");

 Object result = mockHandler.handle(invocation);

 verify(stubbedInvocationMatcher).matches(invocation);
 verifyNoMoreInteractions(stubbedInvocationMatcher);

 assertEquals("world", result);
 }

 @Test
 public void testHandle_VerificationModeProvided_VerifiesInvocation() throws Throwable {
 Invocation invocation = mock(InvocationOnMock.class);
 when(stubbedInvocationMatcher.matches(any())).thenReturn(stubbedInvocationMatcher);
 when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(verify(eq(any())));

 mockHandler.handle(invocation);

 verify(stubbedInvocationMatcher).matches(invocation);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_VerificationModeProvided_ThrowsExceptionIfNotSameMock() throws Throwable {
 Invocation invocation = mock(InvocationOnMock.class);
 Invocation otherInvocation = mock(InvocationOnMock.class);
 when(stubbedInvocationMatcher.matches(any())).thenReturn(stubbedInvocationMatcher);
 when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(verify(eq(any())));

 mockHandler.handle(invocation);
 mockHandler.handle(otherInvocation);
 }

 @Test
 public void testVoidMethodStubbable_StubsVoidMethodCall() throws Throwable {
 VoidMethodStubbable<MockitoTestSubject> voidMethodStubbable = mockHandler.voidMethodStubbable(mockitoTestSubject);
 voidMethodStubbable.stubVoid();

 Invocation invocation = mock(InvocationOnMock.class);
 mockHandler.handle(invocation);

 verify(mockitoTestSubject).voidMethod();
 verifyNoMoreInteractions(mockitoTestSubject);
 }

 @Test
 public void testVoidMethodStubbable_ThrowsIllegalStateExceptionIfAlreadyStubbed() throws Throwable {
 VoidMethodStubbable<MockitoTestSubject> voidMethodStubbable = mockHandler.voidMethodStubbable(mockitoTestSubject);
 voidMethodStubbable.stubVoid();

 IllegalStateException exception = assertThrows(IllegalStateException.class, () -> voidMethodStubbable.stubVoid());
 assertEquals("You can stub the void method 'voidMethod()' of 'MockitoTestSubject' only once.", exception.getMessage());
 }

 @Test
 public void testSetAnswersForStubbing_SetsAnswers() throws Throwable {
 List<Answer> answers = Arrays.asList(answer("answer1"), answer("answer2"), answer("answer3"));
 mockHandler.setAnswersForStubbing(answers);

 Invocation invocation = mock(InvocationOnMock.class);
 when(stubbedInvocationMatcher.matches(any())).thenReturn(stubbedInvocationMatcher);

 Object result = mockHandler.handle(invocation);

 List<Answer> answersFromContainer = mockHandler.invocationContainerImpl.getAnswersForStubbing();

 assertEquals(answers, answersFromContainer);
 }

 @Test
 public void testGetInvocationContainer_ReturnsInvocationContainer() throws Throwable {
 Invocation invocation = mock(InvocationOnMock.class);
 when(stubbedInvocationMatcher.matches(any())).thenReturn(stubbedInvocationMatcher);

 mockHandler.handle(invocation);

 InvocationContainer invocationContainer = mockHandler.getInvocationContainer();

 assertEquals(mockHandler.invocationContainerImpl, invocationContainer);
 }

 private Answer answer(String answerString) {
 return new AnswerWithMockito() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return answerString;
 }
 };
}
}