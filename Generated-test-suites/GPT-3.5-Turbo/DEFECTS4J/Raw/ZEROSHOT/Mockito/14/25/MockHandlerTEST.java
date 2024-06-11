import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.verification.VerificationData;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    @Test
    public void testHandle() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<MockSettingsImpl>(mockSettings);
        Invocation invocation = mock(Invocation.class);

        // stub hasAnswersForStubbing
        when(invocation.hasAnswersForStubbing()).thenReturn(true);

        // stub getArgumentMatcherStorage() and bindMatchers
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        when(mockHandler.matchersBinder).thenReturn(matchersBinder);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));

        // set mockSettings getDefaultAnswer()
        Answer<Object> defaultAnswer = mock(Answer.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);

        // call handle method
        mockHandler.handle(invocation);

        // verify
        verify(matchersBinder, times(1)).bindMatchers(any(), any());
        verify(mockSettings, times(1)).getDefaultAnswer();
        verify(invocation, times(1)).hasAnswersForStubbing();
    }

    @Test
    public void testHandleWithVerificationMode() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<MockSettingsImpl>(mockSettings);
        Invocation invocation = mock(Invocation.class);
        VerificationData verificationData = mock(VerificationData.class);
        VerificationMode verificationMode = mock(VerificationMode.class);

        // stub hasAnswersForStubbing
        when(invocation.hasAnswersForStubbing()).thenReturn(false);

        // stub pullVerificationMode
        MockingProgress mockingProgress = mock(MockingProgress.class);
        when(mockHandler.mockingProgress).thenReturn(mockingProgress);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);

        // mock getArgumentMatcherStorage() and bindMatchers
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        when(mockHandler.matchersBinder).thenReturn(matchersBinder);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));

        // set mockSettings getDefaultAnswer()
        Answer<Object> defaultAnswer = mock(Answer.class);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);

        // call handle method
        mockHandler.handle(invocation);

        // verify
        verify(matchersBinder, times(1)).bindMatchers(any(), any());
        verify(mockingProgress, times(1)).pullVerificationMode();
        verify(mockSettings, times(1)).getDefaultAnswer();
        verify(invocation, times(1)).hasAnswersForStubbing();
    }

    @Test
    public void testSetAnswersForStubbing() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<MockSettingsImpl>(mockSettings);
        Answer<Object> answer = mock(Answer.class);

        // call setAnswersForStubbing method
        mockHandler.setAnswersForStubbing(singletonList(answer));

        // verify
        assertEquals(singletonList(answer), mockHandler.invocationContainerImpl.getAnswersForStubbing());
    }

    @Test
    public void testVoidMethodStubbable() {
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<MockSettingsImpl>();
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        
        // stub voidMethodStubbable
        when(mockSettings.getInvocationContainer()).thenReturn(invocationContainerImpl);

        // call voidMethodStubbable method
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mockSettings);

        // verify
        assertNotNull(stubbable);
        assertEquals(mockSettings, stubbable.getMock());
        assertEquals(invocationContainerImpl, stubbable.getInvocationContainer());
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<MockSettingsImpl>(mockSettings);

        // call getMockSettings method
        MockSettingsImpl result = mockHandler.getMockSettings();

        // verify
        assertEquals(mockSettings, result);
    }

    @Test
    public void testGetInvocationContainer() {
        MockHandler<MockSettingsImpl> mockHandler = new MockHandler<MockSettingsImpl>();
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        
        // set invocationContainerImpl
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        // call getInvocationContainer method
        InvocationContainer result = mockHandler.getInvocationContainer();

        // verify
        assertEquals(invocationContainerImpl, result);
    }
}