import org.junit.Test;
import static org.mockito.Mockito.*;

public class MockHandlerTest {
    @Test
    public void testHandle() throws Throwable {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandlerInterface<T> oldMockHandler = mock(MockHandlerInterface.class);
        Invocation invocation = mock(Invocation.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        ThreadSafeMockingProgress threadSafeMockingProgress = mock(ThreadSafeMockingProgress.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        ArgumentMatcherStorage argumentMatcherStorage = mock(ArgumentMatcherStorage.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        MockitoInvocationHandler answer = mock(MockitoInvocationHandler.class);
        VerificationDataImpl data = mock(VerificationDataImpl.class);
        OngoingStubbingImpl<T> ongoingStubbing = mock(OngoingStubbingImpl.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        List<Answer> answers = mock(List.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(matchersBinder.bindMatchers(argumentMatcherStorage, invocation)).thenReturn(invocationMatcher);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(argumentMatcherStorage);
        when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
        when(invocationContainerImpl.getInvocations()).thenReturn(invocations);
        when(invocationMatcher.verify(data)).thenReturn(null);
        when(mockSettings.getDefaultAnswer().answer(invocation)).thenReturn(ret);
        
        MockHandler<T> mockHandler = new MockHandler<T>(mockSettings);
        mockHandler.handle(invocation);

    }

    @Test
    public void testVoidMethodStubbable() {
        T mock = mock(T.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        
        VoidMethodStubbable<T> stubbable = mockHandler.voidMethodStubbable(mock);
        
        assertNotNull(stubbable);
    }

    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        MockHandler<T> mockHandler = new MockHandler<T>(mockSettings);
        
        assertEquals(mockSettings, mockHandler.getMockSettings());
    }

    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = mock(List.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MockHandler<T> mockHandler = new MockHandler<T>();
        
        mockHandler.setAnswersForStubbing(answers);
        
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }

    @Test
    public void testGetInvocationContainer() {
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MockHandler<T> mockHandler = new MockHandler<T>();
        
        InvocationContainer container = mockHandler.getInvocationContainer();
        
        assertEquals(invocationContainerImpl, container);
    }
}