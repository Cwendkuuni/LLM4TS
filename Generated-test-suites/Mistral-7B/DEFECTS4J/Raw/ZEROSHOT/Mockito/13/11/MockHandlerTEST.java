import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.AnswerWithMockito;
import org.mockito.stubbing.AnswerWrap;
import org.mockito.stubbing.Stub;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockitoInvocationHandler> mockHandler;
 private MockSettingsImpl mockSettings;
 private MockingProgress mockingProgress;
 private InvocationContainer invocationContainer;

 @Before
 public void setUp() {
 mockSettings = Mockito.spy(new MockSettingsImpl());
 mockingProgress = Mockito.spy(new ThreadSafeMockingProgress());
 invocationContainer = Mockito.spy(new InvocationContainerImpl(mockingProgress));
 mockHandler = new MockHandler<>(mockSettings);
 }

 @Test
 public void testHandle_NoAnswers_InvocationMatchesVerificationMode() throws Throwable {
 MockitoInvocationHandler mock = mock(MockitoInvocationHandler.class);
 Invocation invocation = Mockito.mock(Invocation.class);

 mockingProgress.pushVerificationMode(Mockito.verify(mock));

 Object result = mockHandler.handle(invocation);

 verify(mock).handle(ArgumentMatchers.eq(invocation));
 verify(mockingProgress).popVerificationMode();

 assertNull(result);
 }

 @Test
 public void testHandle_NoAnswers_InvocationMatchesStubbing() throws Throwable {
 MockitoInvocationHandler mock = mock(MockitoInvocationHandler.class);
 Invocation invocation = Mockito.mock(Invocation.class);
 InvocationMatcher invocationMatcher = ArgumentMatchers.argThat((InvocationOnMock arg) -> arg.getTarget() == mock);

 when(mockHandler.invocationContainerImpl.getInvocationForPotentialStubbing()).thenReturn(invocationMatcher);
 when(mockHandler.matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(invocationMatcher);

 mockingProgress.pushVerificationMode(null);

 Object result = mockHandler.handle(invocation);

 verify(mockHandler.invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
 verify(mockHandler.matchersBinder).bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
 verify(mockHandler.invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);

 assertNull(result);
 }

 @Test
 public void testHandle_AnswerPresent_AnswerInvoked() throws Throwable {
 MockitoInvocationHandler mock = mock(MockitoInvocationHandler.class);
 Invocation invocation = Mockito.mock(Invocation.class);
 Answer answer = AnswerWithMockito.returnValue("Answer");
 Stub stub = Mockito.stub(mock.getClass(), "method", answer);
 when(mockHandler.invocationContainerImpl.getAnswersForStubbing()).thenReturn(Arrays.asList(answer));
 when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stub);

 Object result = mockHandler.handle(invocation);

 verify(answer).answer(invocation);

 assertEquals("Answer", result);
 }

 @Test
 public void testHandle_DefaultAnswer_DefaultAnswerInvoked() throws Throwable {
 MockitoInvocationHandler mock = mock(MockitoInvocationHandler.class);
 Invocation invocation = Mockito.mock(Invocation.class);
 Answer answer = Mockito.mock(Answer.class);
 when(mockSettings.getDefaultAnswer()).thenReturn(answer);

 Object result = mockHandler.handle(invocation);

 verify(answer).answer(invocation);

 assertNull(result);
 }

 @Test(expected = AssertionError.class)
 public void testHandle_VerificationModeMismatch() throws Throwable {
 MockitoInvocationHandler mock1 = mock(MockitoInvocationHandler.class);
 MockitoInvocationHandler mock2 = mock(MockitoInvocationHandler.class);
 Invocation invocation = Mockito.mock(Invocation.class);

 mockingProgress.pushVerificationMode(Mockito.verify(mock1));
 mockingProgress.pushVerificationMode(Mockito.verify(mock2));

 Object result = mockHandler.handle(invocation);

 verify(mock1).handle(ArgumentMatchers.eq(invocation));
 verify(mock2).handle(ArgumentMatchers.eq(invocation));
 verify(mockingProgress).popVerificationMode();
 verify(mockingProgress).popVerificationMode();

 fail("Expected AssertionError but got null");
 }
}