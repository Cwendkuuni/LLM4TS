import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private MockHandlerInterface<Object> oldMockHandler;

    @Before
    public void setup() {
        mockHandler = new MockHandler<Object>();
    }

    @Test
    public void testHandle_withInvocationContainerImplHasAnswersForStubbing() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.mockSettings = mockSettings;

        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(invocationMatcher);

        Object result = mockHandler.handle(invocation);

        verify(invocationContainerImpl).setMethodForStubbing(invocationMatcher);
        verify(mockingProgress).validateState();
        verifyNoMoreInteractions(invocationContainerImpl, matchersBinder, mockingProgress, mockSettings);
        assertNull(result);
    }

    @Test
    public void testHandle_withVerificationModeIsNotNull() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.mockSettings = mockSettings;

        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(invocationMatcher);
        when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
        when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(invocation.getMock());

        Object result = mockHandler.handle(invocation);

        verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockingProgress).reportOngoingStubbing(Mockito.any());
        verify(invocationContainerImpl).findAnswerFor(invocation);
        if (invocationMatcher != null) {
            verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
        }
        verify(mockSettings).getDefaultAnswer();
        verifyNoMoreInteractions(invocationContainerImpl, matchersBinder, mockingProgress, mockSettings);
        assertNull(result);
    }

    @Test
    public void testHandle_withStubbedInvocationIsNotNull() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.mockSettings = mockSettings;

        when(matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.captureArgumentsFrom(invocation)).thenReturn(any());
        when(stubbedInvocation.answer(invocation)).thenReturn(any());

        Object result = mockHandler.handle(invocation);

        verify(mockSettings).getDefaultAnswer();
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
        verifyNoMoreInteractions(invocationContainerImpl, matchersBinder, mockingProgress, mockSettings);
        assertNotNull(result);
    }

    @Test
    public void testHandle_withStubbedInvocationIsNull() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);

        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.mockSettings = mockSettings;

        when(matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(invocationMatcher);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockSettings.getDefaultAnswer()).thenReturn(any());

        Object result = mockHandler.handle(invocation);

        verify(mockSettings).getDefaultAnswer();
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
        verifyNoMoreInteractions(invocationContainerImpl, matchersBinder, mockingProgress, mockSettings);
        assertNotNull(result);
    }

    @Test
    public void testVoidMethodStubbable() {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        Object mock = mock(Object.class);

        mockHandler.invocationContainerImpl = invocationContainerImpl;

        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);

        assertNotNull(stubbable);
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);

        mockHandler.mockSettings = mockSettings;

        MockSettingsImpl result = mockHandler.getMockSettings();

        assertEquals(mockSettings, result);
    }

    @Test
    public void testSetAnswersForStubbing() {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        List<Answer> answers = new ArrayList<>();

        mockHandler.invocationContainerImpl = invocationContainerImpl;

        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);

        mockHandler.invocationContainerImpl = invocationContainerImpl;

        InvocationContainer result = mockHandler.getInvocationContainer();

        assertEquals(invocationContainerImpl, result);
    }
}