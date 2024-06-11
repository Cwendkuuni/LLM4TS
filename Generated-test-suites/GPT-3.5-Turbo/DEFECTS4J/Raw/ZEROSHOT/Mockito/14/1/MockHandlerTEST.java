import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    
    @Before
    public void setUp() {
        mockHandler = new MockHandler<>();
    }
    
    @Test
    public void testHandle_withAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        
        Answer<Object> answer = mock(Answer.class);
        when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(answer);
        
        Object result = mockHandler.handle(invocation);
        
        verify(answer).answer(invocation);
        
        assertNull(result);
    }
    
    @Test
    public void testHandle_withoutAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        
        when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(null);
        
        Object result = mockHandler.handle(invocation);
        
        verify(mockHandler.getInvocationContainer()).resetInvocationForPotentialStubbing(any());
        
        assertNull(result);
    }
    
    @Test
    public void testHandle_stubbedInvocation() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
        
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        
        Object result = mockHandler.handle(invocation);
        
        verify(stubbedInvocation).captureArgumentsFrom(invocation);
        verify(stubbedInvocation).answer(invocation);
        
        assertNull(result);
    }
    
    @Test
    public void testHandle_verificationMode() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        
        VerificationMode verificationMode = mock(VerificationMode.class);
        when(mockHandler.getMockingProgress().pullVerificationMode()).thenReturn(verificationMode);
        
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
        
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        MatchersBinder matchersBinder = mock(MatchersBinder.class);
        when(mockHandler.getMatchersBinder()).thenReturn(matchersBinder);
        when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        
        List<Invocation> invocations = Arrays.asList(invocation);
        when(invocationContainer.getInvocations()).thenReturn(invocations);
        
        VerificationDataImpl data = mock(VerificationDataImpl.class);
        whenNew(VerificationDataImpl.class).withArguments(eq(invocations), eq(invocationMatcher)).thenReturn(data);
        
        Object result = mockHandler.handle(invocation);
        
        verify(verificationMode).verify(data);
        
        assertNull(result);
    }
    
    @Test
    public void testVoidMethodStubbable() {
        Object mock = new Object();
        
        VoidMethodStubbableImpl<Object> voidMethodStubbable = mock(VoidMethodStubbableImpl.class);
        whenNew(VoidMethodStubbableImpl.class).withArguments(eq(mock), any()).thenReturn(voidMethodStubbable);
        
        VoidMethodStubbable<Object> result = mockHandler.voidMethodStubbable(mock);
        
        assertEquals(voidMethodStubbable, result);
    }
    
    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        mockHandler = new MockHandler<>(mockSettings);
        
        MockSettingsImpl result = mockHandler.getMockSettings();
        
        assertEquals(mockSettings, result);
    }
    
    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = Arrays.asList(mock(Answer.class));
        
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
        
        mockHandler.setAnswersForStubbing(answers);
        
        verify(invocationContainer).setAnswersForStubbing(answers);
    }
    
    @Test
    public void testGetInvocationContainer() {
        InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
        
        InvocationContainer result = mockHandler.getInvocationContainer();
        
        assertEquals(invocationContainer, result);
    }
}