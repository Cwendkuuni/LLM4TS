import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.internal.MockHandler;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    @Test
    public void testHandle() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);

        MockHandler<Object> mockHandler = new MockHandler<Object>(mockSettings);
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        stub(invocationContainerImpl.hasAnswersForStubbing()).toReturn(true);

        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        stub(matchersBinder.bindMatchers(any(), any())).toReturn(invocationMatcher);

        mockHandler.handle(invocation);

        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
    }

    @Test
    public void testHandle_verificationModeIsNotNull() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        VerificationMode verificationMode = mock(VerificationMode.class);

        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
        stub(mockAwareVerificationMode.getMock()).toReturn(invocation.getMock());

        MockHandler<Object> mockHandler = new MockHandler<Object>(mockSettings);
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        mockHandler.handle(invocation);

        verify(mockingProgress).pullVerificationMode();
        verify(matchersBinder).bindMatchers(any(), any());
        verify(mockingProgress).validateState();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any());
        verifyZeroInteractions(invocationContainerImpl);
    }

    @Test
    public void testHandle_verificationModeIsNotNull_differentMock() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        VerificationMode verificationMode = mock(VerificationMode.class);

        MockAwareVerificationMode mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
        stub(mockAwareVerificationMode.getMock()).toReturn(null);

        MockHandler<Object> mockHandler = new MockHandler<Object>(mockSettings);
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        mockHandler.handle(invocation);

        verify(mockingProgress).pullVerificationMode();
        verify(matchersBinder).bindMatchers(any(), any());
        verify(mockingProgress).validateState();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any());
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(mockSettings).getDefaultAnswer();
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any());
    }

    @Test
    public void testHandle_stubbedInvocationIsNull() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);

        MockHandler<Object> mockHandler = new MockHandler<Object>(mockSettings);
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        stub(invocationContainerImpl.findAnswerFor(invocation)).toReturn(null);

        mockHandler.handle(invocation);

        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any());
        verify(mockSettings).getDefaultAnswer();
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any());
    }

    @Test
    public void testVoidMethodStubbable() {
        MockHandler<Object> mockHandler = new MockHandler<Object>();
        Object mock = new Object();
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        VoidMethodStubbableImpl<Object> voidMethodStubbableImpl = mock(VoidMethodStubbableImpl.class);

        mockHandler.invocationContainerImpl = invocationContainerImpl;

        stubNew(VoidMethodStubbableImpl.class).withArguments(mock, invocationContainerImpl).toReturn(voidMethodStubbableImpl);

        VoidMethodStubbable<Object> result = mockHandler.voidMethodStubbable(mock);

        verifyNew(VoidMethodStubbableImpl.class).withArguments(mock, invocationContainerImpl);
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        MockHandler<Object> mockHandler = new MockHandler<Object>(mockSettings);

        assertEquals(mockSettings, mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing() {
        MockHandler<Object> mockHandler = new MockHandler<Object>();
        List<Answer> answers = mock(List.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);

        mockHandler.invocationContainerImpl = invocationContainerImpl;

        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        MockHandler<Object> mockHandler = new MockHandler<Object>();
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);

        mockHandler.invocationContainerImpl = invocationContainerImpl;

        InvocationContainer result = mockHandler.getInvocationContainer();

        assertEquals(invocationContainerImpl, result);
    }

}