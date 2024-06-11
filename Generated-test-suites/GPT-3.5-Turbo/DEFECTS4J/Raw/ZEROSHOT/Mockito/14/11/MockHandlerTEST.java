import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.mockito.invocation.Invocation;
import org.mockito.internal.MockHandler;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationData;

import org.junit.Before;
import org.junit.Test;

public class MockHandlerTest {

    private MockHandler<String> mockHandler;
    private MockingProgress mockingProgress;
    private InvocationContainerImpl invocationContainerImpl;

    @Before
    public void setup() {
        mockHandler = new MockHandler<String>();
        mockingProgress = mock(MockingProgress.class);
        invocationContainerImpl = mock(InvocationContainerImpl.class);
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.invocationContainerImpl = invocationContainerImpl;
    }

    @Test
    public void testHandle_StubbingVoids() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArgumentMatcherStorage());
        when(mockingProgress.getArgumentMatcherStorage().retrieve()).thenReturn(new ArrayList<ArgumentMatcher>());
        when(invocationContainerImpl.setMethodForStubbing(invocationMatcher)).thenReturn(null);

        mockHandler.handle(invocation);

        verify(invocationContainerImpl, times(1)).hasAnswersForStubbing();
        verify(mockingProgress, times(1)).getArgumentMatcherStorage();
        verify(invocationContainerImpl, times(1)).setMethodForStubbing(invocationMatcher);
        verify(invocationContainerImpl, never()).getInvocations();
        verify(mockingProgress, never()).pullVerificationMode();
        verifyNoMoreInteractions(invocationContainerImpl, mockingProgress);
    }

    @Test
    public void testHandle_VerificationModeNotNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        VerificationData verificationData = mock(VerificationData.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArgumentMatcherStorage());
        when(mockingProgress.getArgumentMatcherStorage().retrieve()).thenReturn(new ArrayList<ArgumentMatcher>());
        when(matchersBinder.bindMatchers(any(ArgumentMatcherStorage.class), eq(invocation))).thenReturn(invocationMatcher);
        when(mockingProgress.validateState()).thenThrow(new Throwable());
        when(invocationContainerImpl.getInvocations()).thenReturn(new ArrayList<Invocation>());
        doNothing().when(verificationMode).verify(verificationData);

        mockHandler.handle(invocation);

        verify(invocationContainerImpl, times(1)).hasAnswersForStubbing();
        verify(mockingProgress, times(1)).pullVerificationMode();
        verify(mockingProgress, times(1)).getArgumentMatcherStorage();
        verify(mockingProgress, times(1)).validateState();
        verify(matchersBinder, times(1)).bindMatchers(any(ArgumentMatcherStorage.class), eq(invocation));
        verify(invocationContainerImpl, times(1)).getInvocations();
        verify(verificationMode, times(1)).verify(verificationData);
        verifyNoMoreInteractions(invocationContainerImpl, mockingProgress, verificationMode);
    }

    @Test
    public void testHandle_StubbedInvocationNotNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArgumentMatcherStorage());
        when(mockingProgress.getArgumentMatcherStorage().retrieve()).thenReturn(new ArrayList<ArgumentMatcher>());
        when(matchersBinder.bindMatchers(any(ArgumentMatcherStorage.class), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainerImpl.getInvocations()).thenReturn(new ArrayList<Invocation>());
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.captureArgumentsFrom(invocation)).thenReturn(null);
        when(stubbedInvocation.answer(invocation)).thenReturn(null);
        when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(null);
        when(invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher)).thenReturn(null);

        mockHandler.handle(invocation);

        verify(invocationContainerImpl, times(1)).hasAnswersForStubbing();
        verify(mockingProgress, times(1)).pullVerificationMode();
        verify(mockingProgress, times(1)).getArgumentMatcherStorage();
        verify(mockingProgress, never()).validateState();
        verify(matchersBinder, times(1)).bindMatchers(any(ArgumentMatcherStorage.class), eq(invocation));
        verify(invocationContainerImpl, times(1)).getInvocations();
        verify(invocationContainerImpl, times(1)).findAnswerFor(invocation);
        verify(stubbedInvocation, times(1)).captureArgumentsFrom(invocation);
        verify(stubbedInvocation, times(1)).answer(invocation);
        verify(mockHandler.getMockSettings(), times(1)).getDefaultAnswer();
        verify(invocationContainerImpl, times(1)).resetInvocationForPotentialStubbing(invocationMatcher);
        verifyNoMoreInteractions(invocationContainerImpl, mockingProgress, invocationMatcher, stubbedInvocation);
    }

    @Test
    public void testHandle_StubbedInvocationNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(new ArgumentMatcherStorage());
        when(mockingProgress.getArgumentMatcherStorage().retrieve()).thenReturn(new ArrayList<ArgumentMatcher>());
        when(matchersBinder.bindMatchers(any(ArgumentMatcherStorage.class), eq(invocation))).thenReturn(invocationMatcher);
        when(invocationContainerImpl.getInvocations()).thenReturn(new ArrayList<Invocation>());
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(null);
        when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn(null);
        when(invocationContainerImpl.resetInvocationForPotentialStubbing(invocationMatcher)).thenReturn(null);

        mockHandler.handle(invocation);

        verify(invocationContainerImpl, times(1)).hasAnswersForStubbing();
        verify(mockingProgress, times(1)).pullVerificationMode();
        verify(mockingProgress, times(1)).getArgumentMatcherStorage();
        verify(mockingProgress, never()).validateState();
        verify(matchersBinder, times(1)).bindMatchers(any(ArgumentMatcherStorage.class), eq(invocation));
        verify(invocationContainerImpl, times(1)).getInvocations();
        verify(invocationContainerImpl, times(1)).findAnswerFor(invocation);
        verify(mockHandler.getMockSettings(), times(2)).getDefaultAnswer();
        verify(invocationContainerImpl, times(1)).resetInvocationForPotentialStubbing(invocationMatcher);
        verifyNoMoreInteractions(invocationContainerImpl, mockingProgress, invocationMatcher);
    }

    @Test
    public void testVoidMethodStubbable() {
        String mock = "mock";
        VoidMethodStubbable<String> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

        assertNotNull(voidMethodStubbable);
        assertEquals(mock, voidMethodStubbable.getMock());
        assertEquals(invocationContainerImpl, voidMethodStubbable.getInvocationContainer());
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<Answer>();
        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainerImpl, times(1)).setAnswersForStubbing(answers);
        verifyNoMoreInteractions(invocationContainerImpl);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainerImpl container = mockHandler.getInvocationContainer();

        assertNotNull(container);
        assertEquals(invocationContainerImpl, container);
    }
}