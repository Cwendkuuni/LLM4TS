import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

@SuppressWarnings("unchecked")
public class MockHandlerTest {

    private MockHandler mockHandler;
    private MockHandlerInterface mockHandlerInterface;
    private InvocationContainerImpl invocationContainerImpl;
    private MatchersBinder matchersBinder;
    private MockingProgress mockingProgress;
    private MockSettingsImpl mockSettings;

    @Before
    public void setUp() {
        mockSettings = Mockito.mock(MockSettingsImpl.class);
        mockHandler = new MockHandler(mockSettings);
        mockHandlerInterface = Mockito.mock(MockHandlerInterface.class);
        invocationContainerImpl = Mockito.mock(InvocationContainerImpl.class);
        matchersBinder = Mockito.mock(MatchersBinder.class);
        mockingProgress = Mockito.mock(MockingProgress.class);
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
    }

    @Test
    public void testHandle() throws Throwable {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationContainerImpl invocationContainer = Mockito.mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = Mockito.mock(MatchersBinder.class);
        Mockito.when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        Mockito.when(mockHandler.matchersBinder.bindMatchers(mockHandler.mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(Mockito.mock(InvocationMatcher.class));
        Mockito.when(mockHandler.invocationContainerImpl.setMethodForStubbing(Mockito.any(InvocationMatcher.class))).thenReturn(null);
        mockHandler.handle(invocation);
        Mockito.verify(mockHandler.invocationContainerImpl, Mockito.times(1)).hasAnswersForStubbing();
        Mockito.verify(mockHandler.matchersBinder, Mockito.times(1)).bindMatchers(mockHandler.mockingProgress.getArgumentMatcherStorage(), invocation);
        Mockito.verify(mockHandler.invocationContainerImpl, Mockito.times(1)).setMethodForStubbing(Mockito.any(InvocationMatcher.class));
    }

    @Test
    public void testHandleWithVerificationMode() throws Throwable {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationContainerImpl invocationContainer = Mockito.mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = Mockito.mock(MatchersBinder.class);
        VerificationMode verificationMode = Mockito.mock(VerificationMode.class);
        MockAwareVerificationMode mockAwareVerificationMode = new MockAwareVerificationMode(verificationMode);
        mockAwareVerificationMode.setMock(invocation.getMock());
        Mockito.when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        Mockito.when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(mockAwareVerificationMode);
        Mockito.when(mockHandler.matchersBinder.bindMatchers(mockHandler.mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(Mockito.mock(InvocationMatcher.class));
        Mockito.when(mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(Mockito.any(InvocationMatcher.class))).thenReturn(null);
        mockHandler.handle(invocation);
        Mockito.verify(mockHandler.invocationContainerImpl, Mockito.times(1)).hasAnswersForStubbing();
        Mockito.verify(mockHandler.mockingProgress, Mockito.times(1)).pullVerificationMode();
        Mockito.verify(mockHandler.matchersBinder, Mockito.times(1)).bindMatchers(mockHandler.mockingProgress.getArgumentMatcherStorage(), invocation);
        Mockito.verify(mockHandler.invocationContainerImpl, Mockito.times(1)).setInvocationForPotentialStubbing(Mockito.any(InvocationMatcher.class));
        Mockito.verify(mockHandler.invocationContainerImpl, Mockito.never()).findAnswerFor(invocation);
        Mockito.verify(mockHandler.mockSettings, Mockito.never()).getDefaultAnswer();
    }

    @Test
    public void testHandleWithStubbedInvocation() throws Throwable {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationContainerImpl invocationContainer = Mockito.mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = Mockito.mock(MatchersBinder.class);
        Mockito.when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        Mockito.when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(null);
        Mockito.when(mockHandler.matchersBinder.bindMatchers(mockHandler.mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(Mockito.mock(InvocationMatcher.class));
        Mockito.when(mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(Mockito.any(InvocationMatcher.class))).thenReturn(null);
        Mockito.when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(Mockito.mock(StubbedInvocationMatcher.class));
        mockHandler.handle(invocation);
        Mockito.verify(mockHandler.invocationContainerImpl, Mockito.times(1)).hasAnswersForStubbing();
        Mockito.verify(mockHandler.mockingProgress, Mockito.times(1)).pullVerificationMode();
        Mockito.verify(mockHandler.matchersBinder, Mockito.times(1)).bindMatchers(mockHandler.mockingProgress.getArgumentMatcherStorage(), invocation);
        Mockito.verify(mockHandler.invocationContainerImpl, Mockito.times(1)).setInvocationForPotentialStubbing(Mockito.any(InvocationMatcher.class));
        Mockito.verify(mockHandler.invocationContainerImpl, Mockito.times(1)).findAnswerFor(invocation);
        Mockito.verify(mockHandler.mockSettings, Mockito.never()).getDefaultAnswer();
    }

    @Test
    public void testHandleWithNoStubbedInvocation() throws Throwable {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationContainerImpl invocationContainer = Mockito.mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = Mockito.mock(MatchersBinder.class);
        Mockito.when(mockHandler.invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        Mockito.when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(null);
        Mockito.when(mockHandler.matchersBinder.bindMatchers(mockHandler.mockingProgress.getArgumentMatcherStorage(), invocation)).thenReturn(Mockito.mock(InvocationMatcher.class));
        Mockito.when(mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(Mockito.any(InvocationMatcher.class))).thenReturn(null);
        Mockito.when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
        Mockito.when(mockHandler.mockSettings.getDefaultAnswer().answer(invocation)).thenReturn(Mockito.mock(Object.class));
        mockHandler.handle(invocation);
        Mockito.verify(mockHandler.invocationContainerImpl, Mockito.times(1)).hasAnswersForStubbing();
        Mockito.verify(mockHandler.mockingProgress, Mockito.times(1)).pullVerificationMode();
        Mockito.verify(mockHandler.matchersBinder, Mockito.times(1)).bindMatchers(mockHandler.mockingProgress.getArgumentMatcherStorage(), invocation);
        Mockito.verify(mockHandler.invocationContainerImpl, Mockito.times(1)).setInvocationForPotentialStubbing(Mockito.any(InvocationMatcher.class));
        Mockito.verify(mockHandler.invocationContainerImpl, Mockito.times(1)).findAnswerFor(invocation);
        Mockito.verify(mockHandler.mockSettings, Mockito.times(1)).getDefaultAnswer();
    }

    @Test
    public void testVoidMethodStubbable() {
        Mockito.when(mockHandler.invocationContainerImpl.findAnswerFor(Mockito.any(Invocation.class))).thenReturn(Mockito.mock(StubbedInvocationMatcher.class));
        Assert.assertNotNull(mockHandler.voidMethodStubbable(Mockito.mock(Object.class)));
        Mockito.verify(mockHandler.invocationContainerImpl, Mockito.times(1)).findAnswerFor(Mockito.any(Invocation.class));
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = Mockito.mock(List.class);
        mockHandler.setAnswersForStubbing(answers);
        Mockito.verify(mockHandler.invocationContainerImpl, Mockito.times(1)).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        Assert.assertEquals(mockHandler.getInvocationContainer(), invocationContainerImpl);
    }

}