import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class MockHandlerTest {
    
    private MockHandlerInterface<Object> mockHandler;
    private MockSettingsImpl mockSettings;
    
    @Before
    public void setup() {
        mockSettings = mock(MockSettingsImpl.class);
        mockHandler = new MockHandler<>(mockSettings);
    }
    
    @Test
    public void testHandle_InvocationsExist_StubbingVoid() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
        when(mockingProgress.getArgumentMatcherStorage()).thenReturn(mock(ArgumentMatcherStorage.class));
        when(matchersBinder.bindMatchers(any(ArgumentMatcherStorage.class), any(Invocation.class))).thenReturn(mock(InvocationMatcher.class));
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        
        Object result = mockHandler.handle(invocation);
        
        assertNull(result);
        verify(invocationContainerImpl).setMethodForStubbing(any(InvocationMatcher.class));
    }
    
    @Test
    public void testHandle_InvocationsExist_VerificationModeNotNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(mock(VerificationMode.class));
        when(matchersBinder.bindMatchers(any(ArgumentMatcherStorage.class), any(Invocation.class))).thenReturn(mock(InvocationMatcher.class));
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        
        Object result = mockHandler.handle(invocation);
        
        assertNull(result);
        verify(mockingProgress).pullVerificationMode();
        verify(mockingProgress).validateState();
    }
    
    @Test
    public void testHandle_InvocationsExist_NoVerificationMode() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(ArgumentMatcherStorage.class), any(Invocation.class))).thenReturn(mock(InvocationMatcher.class));
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        
        Object result = mockHandler.handle(invocation);
        
        assertNull(result);
        verify(invocationContainerImpl).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(mockingProgress).reportOngoingStubbing(any(OngoingStubbingImpl.class));
        verify(invocationContainerImpl).findAnswerFor(any(Invocation.class));
    }
    
    @Test
    public void testHandle_InvocationIsStubbed() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        MockingProgress mockingProgress = mock(MockingProgress.class);
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        
        when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(false);
        when(mockingProgress.pullVerificationMode()).thenReturn(null);
        when(matchersBinder.bindMatchers(any(ArgumentMatcherStorage.class), any(Invocation.class))).thenReturn(mock(InvocationMatcher.class));
        when(invocationContainerImpl.findAnswerFor(any(Invocation.class))).thenReturn(mock(StubbedInvocationMatcher.class));
        when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        mockHandler.matchersBinder = matchersBinder;
        mockHandler.mockingProgress = mockingProgress;
        mockHandler.mockSettings = mockSettings;
        
        Object result = mockHandler.handle(invocation);
        
        assertNull(result);
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }
    
    @Test
    public void testVoidMethodStubbable() {
        Object mock = new Object();
        InvocationContainerImpl invocationContainerImpl = mock(InvocationContainerImpl.class);
        
        mockHandler.invocationContainerImpl = invocationContainerImpl;
        
        VoidMethodStubbable<Void> stubbable = mockHandler.voidMethodStubbable(mock);
        
        assertNotNull(stubbable);
    }
    
    @Test
    public void testGetMockSettings() {
        MockSettingsImpl settings = mockHandler.getMockSettings();
        
        assertNotNull(settings);
        assertSame(mockSettings, settings);
    }
    
    @Test
    public void testSetAnswersForStubbing() {
        mockHandler.setAnswersForStubbing(mock(List.class));
        
        verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(any(List.class));
    }
    
    @Test
    public void testGetInvocationContainer() {
        InvocationContainerImpl container = mockHandler.getInvocationContainer();
        
        assertNotNull(container);
        assertSame(mockHandler.invocationContainerImpl, container);
    }
}