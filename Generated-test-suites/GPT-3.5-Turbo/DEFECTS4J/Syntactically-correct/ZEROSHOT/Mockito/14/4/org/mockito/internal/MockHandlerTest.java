import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler mockHandler;
    private MockHandlerInterface mockHandlerInterface;
    private Invocation invocation;
    private VerificationMode verificationMode;

    @Before
    public void setup() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        mockHandler = new MockHandler(mockSettings);
        mockHandlerInterface = mock(MockHandlerInterface.class);
        invocation = mock(Invocation.class);
        verificationMode = mock(VerificationMode.class);
    }

    @Test
    public void testHandleInvocationWithStubbing() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mock(ArgumentMatcherStorage.class));
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        
        mockHandler.handle(invocation);

        verify(invocationContainerImpl).setMethodForStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testHandleInvocationWithVerification() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mock(ArgumentMatcherStorage.class));
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        
        mockHandler.handle(invocation);

        verify(invocationContainerImpl).getInvocations();
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandleInvocationWithStubbingAndInvocationFound() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mock(ArgumentMatcherStorage.class));
        when(mockingProgress.reportOngoingStubbing(any())).thenReturn(null);
        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(mock(StubbedInvocationMatcher.class));
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.mockSettings = mockSettings;
        
        mockHandler.handle(invocation);

        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(invocationContainerImpl).findAnswerFor(invocation);
    }
    
    @Test
    public void testHandleInvocationWithStubbingAndInvocationNotFound() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        ThreadSafeMockingProgress mockingProgress = mock(ThreadSafeMockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        Answer defaultAnswer = mock(Answer.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mock(ArgumentMatcherStorage.class));
        when(mockingProgress.reportOngoingStubbing(any())).thenReturn(null);
        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.mockSettings = mockSettings;
        
        mockHandler.handle(invocation);

        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockSettings).getDefaultAnswer();
    }
    
    @Test
    public void testVoidMethodStubbable() {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        
        mockHandler.voidMethodStubbable(mockHandlerInterface);
        
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }
    
    @Test
    public void testGetMockSettings() {
        MockSettingsImpl expected = mock(MockSettingsImpl.class);
        
        mockHandler.mockSettings = expected;
        
        MockSettingsImpl actual = mockHandler.getMockSettings();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);
        InvocationContainer invocationContainer = mock(InvocationContainer.class);
        
        mockHandler.invocationContainerImpl = invocationContainer;
        
        mockHandler.setAnswersForStubbing(answers);
        
        verify(invocationContainer).setAnswersForStubbing(any(List.class));
    }
    
    @Test
    public void testGetInvocationContainer() {
        InvocationContainerImpl expected = mock(InvocationContainerImpl.class);
        
        mockHandler.invocationContainerImpl = expected;
        
        InvocationContainer actual = mockHandler.getInvocationContainer();
        
        assertEquals(expected, actual);
    }

}