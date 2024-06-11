import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

    private MockHandler handler;
    private MockHandlerInterface mockHandlerInterface;
    private MockSettingsImpl mockSettings;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;

    @Before
    public void setUp() {
        mockSettings = mock(MockSettingsImpl.class);
        invocationContainerImpl = mock(InvocationContainerImpl.class);
        matchersBinder = mock(MatchersBinder.class);
        mockingProgress = mock(MockingProgress.class);
        mockHandlerInterface = mock(MockHandlerInterface.class);
        handler = new MockHandler(mockSettings);
    }

    @Test
    public void testHandle() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));

        Object result = handler.handle(invocation);

        assertNull(result);
        verify(invocationContainerImpl).setMethodForStubbing(any());
    }

    @Test
    public void testHandle_withVerificationMode() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(mock(InvocationMatcher.class));

        Object result = handler.handle(invocation);

        assertNull(result);
        verify(mockingProgress).validateState();
        verify(verificationMode).verify(any());
    }

    @Test
    public void testHandle_withStubbedInvocation() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(stubbedInvocation);
        when(stubbedInvocation.captureArgumentsFrom(any())).thenReturn(null);
        when(stubbedInvocation.answer(any())).thenReturn(null);

        Object result = handler.handle(invocation);

        assertNull(result);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any());
        verify(stubbedInvocation).captureArgumentsFrom(any());
        verify(stubbedInvocation).answer(any());
    }

    @Test
    public void testHandle_withNoStubbedInvocation() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        Object ret = new Object();
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(mockSettings.getDefaultAnswer().answer(any())).thenReturn(ret);
        when(invocationContainerImpl.findAnswerFor(any())).thenReturn(null);

        Object result = handler.handle(invocation);

        assertEquals(ret, result);
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any());
        verify(mockSettings).getDefaultAnswer();
        verify(mockSettings.getDefaultAnswer(), times(2)).answer(any());
    }

    @Test
    public void testVoidMethodStubbable() {
        Object mock = new Object();
        VoidMethodStubbableImpl stubbable = mock(VoidMethodStubbableImpl.class);
        when(stubbable.voidMethodStubbable(any())).thenReturn(null);

        VoidMethodStubbable result = handler.voidMethodStubbable(mock);

        assertNotNull(result);
        verify(stubbable).voidMethodStubbable(any());
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl result = handler.getMockSettings();

        assertEquals(mockSettings, result);
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();
        handler.setAnswersForStubbing(answers);

        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainerImpl result = handler.getInvocationContainer();

        assertEquals(invocationContainerImpl, result);
    }
}