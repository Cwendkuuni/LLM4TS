
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class MockHandlerTest {

    @Mock
    private InvocationContainerImpl invocationContainerImpl;

    @Mock
    private MatchersBinder matchersBinder;

    @Mock
    private MockingProgress mockingProgress;

    private MockHandler<MockHandlerInterface> mockHandler;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockHandler = new MockHandler<>(new MockSettingsImpl());
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
    }

    // Test for handle method when the invocationContainerImpl has answers for stubbing
    @Test
    public void testHandle_WhenInvocationContainerImplHasAnswersForStubbing_ReturnsNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
        verify(invocationContainerImpl).setMethodForStubbing(any(InvocationMatcher.class));
    }

    // Test for handle method when the verification mode is not null
    @Test
    public void testHandle_WhenVerificationModeNotNull_VerifyDataAndReturnsNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);

        mockingProgress.pullVerificationMode();
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(ArgumentMatchers.any());

        Object result = mockHandler.handle(invocation);

        assertNull(result);
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
        verify(mockingProgress).validateState();
        verify(invocationContainerImpl).getInvocations();
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }

    // Test for handle method when stubbed invocation is found in the invocation container
    @Test
    public void testHandle_WhenStubbedInvocationFound_ReturnsStubbedResult() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        Object expectedResult = new Object();

        when(mockSettings.getDefaultAnswer().answer(invocation)).thenReturn(expectedResult);
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(stubbedInvocation.answer(invocation)).thenReturn(expectedResult);
        
        Object result = mockHandler.handle(invocation);
        
        assertEquals(expectedResult, result);
        verify(matchersBinder).bindMatchers(any(), eq(invocation));
        verify(mockSettings.getDefaultAnswer()).answer(invocation);
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
    }

    // Test for voidMethodStubbable method
    @Test
    public void testVoidMethodStubbable_ReturnsVoidMethodStubbableImpl() {
        MockHandlerInterface mock = mock(MockHandlerInterface.class);
        VoidMethodStubbableImpl<VoidMethodStubbable> expected = mock(VoidMethodStubbableImpl.class);

        when(expected.voidMethodStubbable(mock, invocationContainerImpl)).thenReturn(expected);

        VoidMethodStubbable<VoidMethodStubbable> result = mockHandler.voidMethodStubbable(mock);

        assertEquals(expected, result);
        verify(expected).voidMethodStubbable(mock, invocationContainerImpl);
    }

    // Test for setAnswersForStubbing method
    @Test
    public void testSetAnswersForStubbing_SetsAnswersForStubbingInInvocationContainer() {
        List<Answer> answers = mock(List.class);

        mockHandler.setAnswersForStubbing(answers);

        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    // Test for getInvocationContainer method
    @Test
    public void testGetInvocationContainer_ReturnsInvocationContainerImpl() {
        InvocationContainerImpl expected = instanceOf(InvocationContainerImpl.class);

        InvocationContainer result = mockHandler.getInvocationContainer();

        assertEquals(expected, result);
    }
}

