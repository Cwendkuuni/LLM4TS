import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<TestClass> mockHandler;

    @Before
    public void setup() {
        mockHandler = new MockHandler<>(new MockHandlerInterface<TestClass>() {
            @Override
            public MockSettingsImpl getMockSettings() {
                return null;
            }

            @Override
            public void setAnswersForStubbing(List<Answer> answers) {
            }

            @Override
            public InvocationContainer getInvocationContainer() {
                return null;
            }
        });
    }

    @Test
    public void handle_shouldSetMethodForStubbingAndReturnNull() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);

        Object result = mockHandler.handle(invocation);

        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        verifyZeroInteractions(invocationContainerImpl, invocationMatcher, mockingProgress);
        assertNull(result);
    }

    @Test
    public void handle_shouldVerifyDataWhenVerificationModeIsNotNull() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        OngoingStubbing<T> ongoingStubbing = mock(OngoingStubbing.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        VerificationDataImpl verificationData = mock(VerificationDataImpl.class);
        VerificationMode verificationMode = mock(MockAwareVerificationMode.class);

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(invocation.getMock()).thenReturn(mock(TestClass.class));
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn("Mocked answer");

        Object result = mockHandler.handle(invocation);

        verify(mockingProgress).validateState();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).reportOngoingStubbing(any(VoidMethodStubbable.class));
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verifyZeroInteractions(ongoingStubbing);
        verifyZeroInteractions(verificationData);
        assertEquals("Mocked answer", result);
    }

    @Test
    public void handle_shouldExecuteDefaultAnswerWhenNoStubbedInvocationFound() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        OngoingStubbing<T> ongoingStubbing = mock(OngoingStubbing.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);

        Object result = mockHandler.handle(invocation);

        verify(mockingProgress).validateState();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).reportOngoingStubbing(any(VoidMethodStubbable.class));
        verify(invocationContainerImpl, times(2)).resetInvocationForPotentialStubbing(invocationMatcher);
        verifyZeroInteractions(ongoingStubbing);
        verifyZeroInteractions(stubbedInvocation);
        assertNull(result);
    }

    @Test
    public void voidMethodStubbable_shouldReturnVoidMethodStubbableImpl() {
        TestClass mock = mock(TestClass.class);
        VoidMethodStubbable expected = new VoidMethodStubbableImpl(mock, mockHandler.invocationContainerImpl);

        VoidMethodStubbable actual = mockHandler.voidMethodStubbable(mock);

        assertEquals(expected.getClass(), actual.getClass());
    }

    @Test
    public void getMockSettings_shouldReturnMockSettingsImpl() {
        MockSettingsImpl expected = new MockSettingsImpl();

        MockSettingsImpl actual = mockHandler.getMockSettings();

        assertEquals(expected.getClass(), actual.getClass());
    }

    @Test
    public void setAnswersForStubbing_shouldSetAnswersInInvocationContainerImpl() {
        List<Answer> answers = new ArrayList<>();
        mockHandler.invocationContainerImpl = mock(InvocationContainerImpl.class);

        mockHandler.setAnswersForStubbing(answers);

        verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void getInvocationContainer_shouldReturnInvocationContainerImpl() {
        InvocationContainerImpl expected = new InvocationContainerImpl(mock(MockingProgress.class));

        mockHandler.invocationContainerImpl = expected;

        InvocationContainer actual = mockHandler.getInvocationContainer();

        assertEquals(expected.getClass(), actual.getClass());
    }

    private class TestClass {
    }
}