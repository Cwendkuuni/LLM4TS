import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = mock(MockSettingsImpl.class);
        mockHandler = new MockHandler<>(mockSettings);
    }

    @Test
    public void testHandle_withInvocationContainerImplHavingAnswersForStubbing_returnsNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        ArgumentMatcherStorage argumentMatcherStorage = mock(ArgumentMatcherStorage.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(argumentMatcherStorage, invocation)).thenReturn(mock(InvocationMatcher.class));
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(argumentMatcherStorage);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        assertNull(mockHandler.handle(invocation));
    }

    @Test
    public void testHandle_withVerificationModeNotNull_verifiesInvocation() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        ArgumentMatcherStorage argumentMatcherStorage = mock(ArgumentMatcherStorage.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(argumentMatcherStorage, invocation)).thenReturn(mock(InvocationMatcher.class));

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        assertNull(mockHandler.handle(invocation));

        // verify that verificationMode.verify(data) is called
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    @Test
    public void testHandle_withStubbedInvocationNotNull_returnsAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        ArgumentMatcherStorage argumentMatcherStorage = mock(ArgumentMatcherStorage.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        DefaultAnswer defaultAnswer = mock(DefaultAnswer.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(argumentMatcherStorage, invocation)).thenReturn(mock(InvocationMatcher.class));
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn("answer");
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.answer(invocation)).thenReturn(null);

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        assertEquals("answer", mockHandler.handle(invocation));

        // verify that invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher) is called
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testHandle_withStubbedInvocationNull_returnsDefaultAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        ArgumentMatcherStorage argumentMatcherStorage = mock(ArgumentMatcherStorage.class);
        DefaultAnswer defaultAnswer = mock(DefaultAnswer.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(argumentMatcherStorage, invocation)).thenReturn(mock(InvocationMatcher.class));
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        when(defaultAnswer.answer(invocation)).thenReturn("defaultAnswer");

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;

        assertEquals("defaultAnswer", mockHandler.handle(invocation));

        // verify that invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher) is called
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }

    @Test
    public void testVoidMethodStubbable_returnsVoidMethodStubbableImpl() {
        Object mock = new Object();
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;

        VoidMethodStubbableImpl stubbable = mockHandler.voidMethodStubbable(mock);

        assertNotNull(stubbable);
        assertEquals(mock, stubbable.getMock());
        assertEquals(invocationContainerImpl, stubbable.getInvocationContainer());
    }

    @Test
    public void testGetMockSettings_returnsMockSettings() {
        assertEquals(mockSettings, mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing_setsAnswersForStubbing() {
        List<Answer> answers = mock(List.class);

        mockHandler.setAnswersForStubbing(answers);

        verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer_returnsInvocationContainerImpl() {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);

        mockHandler.invocationContainerImpl = invocationContainerImpl;

        assertEquals(invocationContainerImpl, mockHandler.getInvocationContainer());
    }
}