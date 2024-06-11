import org.junit.Before;
import org.junit.Test;

import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;

    @Before
    public void setUp() {
        mockHandler = new MockHandler<>(new MockSettingsImpl());
    }

    @Test
    public void handle_shouldCallSetMethodForStubbingWhenInvocationContainerHasAnswersForStubbing() throws Throwable {
        InvocationContainerImpl invocationContainerImplMock = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinderMock = mock(MatchersBinder.class);
        MockingProgress mockingProgressMock = mock(MockingProgress.class);
        Invocation invocationMock = mock(Invocation.class);

        when(invocationContainerImplMock.hasAnswersForStubbing()).thenReturn(true);
        when(invocationContainerImplMock.getMethodForStubbing(any(InvocationMatcher.class))).thenReturn(null);

        mockHandler.invocationContainerImpl = invocationContainerImplMock;
        mockHandler.matchersBinder = matchersBinderMock;
        mockHandler.mockingProgress = mockingProgressMock;

        mockHandler.handle(invocationMock);

        verify(mockingProgressMock).getArgumentMatcherStorage();
        verify(matchersBinderMock).bindMatchers(any(), eq(invocationMock));
        verify(invocationContainerImplMock).setMethodForStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void handle_shouldCallPullVerificationModeWhenInvocationContainerDoesNotHaveAnswersForStubbingAndVerificationModeIsNotNull() throws Throwable {
        InvocationContainerImpl invocationContainerImplMock = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinderMock = mock(MatchersBinder.class);
        MockingProgress mockingProgressMock = mock(MockingProgress.class);
        Invocation invocationMock = mock(Invocation.class);
        VerificationMode verificationModeMock = mock(VerificationMode.class);

        when(invocationContainerImplMock.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgressMock.pullVerificationMode()).thenReturn(verificationModeMock);

        mockHandler.invocationContainerImpl = invocationContainerImplMock;
        mockHandler.matchersBinder = matchersBinderMock;
        mockHandler.mockingProgress = mockingProgressMock;

        mockHandler.handle(invocationMock);

        verify(mockingProgressMock).pullVerificationMode();
        verify(mockingProgressMock).getArgumentMatcherStorage();
        verify(matchersBinderMock).bindMatchers(any(), eq(invocationMock));
    }

    @Test
    public void handle_shouldCallSetInvocationForPotentialStubbingAndReportOngoingStubbingWhenInvocationContainerDoesNotHaveAnswersForStubbingAndVerificationModeIsNull() throws Throwable {
        InvocationContainerImpl invocationContainerImplMock = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinderMock = mock(MatchersBinder.class);
        MockingProgress mockingProgressMock = mock(MockingProgress.class);
        Invocation invocationMock = mock(Invocation.class);
        Answer defaultAnswerMock = mock(Answer.class);

        when(invocationContainerImplMock.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgressMock.pullVerificationMode()).thenReturn(null);
        when(mockingProgressMock.getDefaultAnswer()).thenReturn(defaultAnswerMock);

        mockHandler.invocationContainerImpl = invocationContainerImplMock;
        mockHandler.matchersBinder = matchersBinderMock;
        mockHandler.mockingProgress = mockingProgressMock;

        mockHandler.handle(invocationMock);

        verify(mockingProgressMock).pullVerificationMode();
        verify(mockingProgressMock).getArgumentMatcherStorage();
        verify(invocationContainerImplMock).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockingProgressMock).reportOngoingStubbing(any(OngoingStubbingImpl.class));
    }

    @Test
    public void handle_shouldCallFindAnswerForAndReturnStubbedInvocationAnswerWhenStubbedInvocationIsNotNull() throws Throwable {
        InvocationContainerImpl invocationContainerImplMock = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinderMock = mock(MatchersBinder.class);
        MockingProgress mockingProgressMock = mock(MockingProgress.class);
        Invocation invocationMock = mock(Invocation.class);
        Answer defaultAnswerMock = mock(Answer.class);
        StubbedInvocationMatcher stubbedInvocationMock = mock(StubbedInvocationMatcher.class);

        when(invocationContainerImplMock.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgressMock.pullVerificationMode()).thenReturn(null);
        when(mockingProgressMock.getDefaultAnswer()).thenReturn(defaultAnswerMock);
        when(invocationContainerImplMock.findAnswerFor(any(Invocation.class))).thenReturn(stubbedInvocationMock);

        mockHandler.invocationContainerImpl = invocationContainerImplMock;
        mockHandler.matchersBinder = matchersBinderMock;
        mockHandler.mockingProgress = mockingProgressMock;

        mockHandler.handle(invocationMock);

        verify(mockingProgressMock).pullVerificationMode();
        verify(mockingProgressMock).getArgumentMatcherStorage();
        verify(invocationContainerImplMock).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockingProgressMock).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verify(stubbedInvocationMock).captureArgumentsFrom(any(Invocation.class));
        verify(stubbedInvocationMock).answer(any(Invocation.class));
    }

    @Test
    public void handle_shouldCallAnswerFromMockSettingsWhenStubbedInvocationIsNull() throws Throwable {
        InvocationContainerImpl invocationContainerImplMock = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinderMock = mock(MatchersBinder.class);
        MockingProgress mockingProgressMock = mock(MockingProgress.class);
        Invocation invocationMock = mock(Invocation.class);
        Answer defaultAnswerMock = mock(Answer.class);

        when(invocationContainerImplMock.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgressMock.pullVerificationMode()).thenReturn(null);
        when(mockingProgressMock.getDefaultAnswer()).thenReturn(defaultAnswerMock);
        when(invocationContainerImplMock.findAnswerFor(any(Invocation.class))).thenReturn(null);

        mockHandler.invocationContainerImpl = invocationContainerImplMock;
        mockHandler.matchersBinder = matchersBinderMock;
        mockHandler.mockingProgress = mockingProgressMock;

        mockHandler.handle(invocationMock);

        verify(mockingProgressMock).pullVerificationMode();
        verify(mockingProgressMock).getArgumentMatcherStorage();
        verify(invocationContainerImplMock).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockingProgressMock).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verify(defaultAnswerMock).answer(any(Invocation.class));
    }

    @Test
    public void voidMethodStubbable_shouldReturnVoidMethodStubbableImplInstance() {
        Object mock = new Object();

        VoidMethodStubbable voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        assertInstanceOf(VoidMethodStubbableImpl.class, voidMethodStubbable);
    }

    @Test
    public void getMockSettings_shouldReturnMockSettings() {
        MockSettingsImpl mockSettings = mockHandler.getMockSettings();

        assertSame(mockSettings, mockHandler.mockSettings);
    }

    @Test
    public void setAnswersForStubbing_shouldCallSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);

        mockHandler.setAnswersForStubbing(answers);

        verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void getInvocationContainer_shouldReturnInvocationContainer() {
        InvocationContainer invocationContainer = mockHandler.getInvocationContainer();

        assertSame(invocationContainer, mockHandler.invocationContainerImpl);
    }
}