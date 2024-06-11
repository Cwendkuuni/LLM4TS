
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;

public class MockHandlerTest {

    private MockHandler<Object> mockHandler;
    private InvocationMatcher invocationMatcher;
    private StubbedInvocationMatcher stubbedInvocationMatcher;
    private List<Answer> answers;
    
    @Before
    public void setup() {
        mockHandler = new MockHandler<>();
        invocationMatcher = mock(InvocationMatcher.class);
        stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        answers = Arrays.asList(mock(Answer.class), mock(Answer.class));
    }
    
    @Test
    public void testHandle_whenInvocationsForStubbingExists_thenInvocationForStubbingSet() throws Throwable {
        // Setup
        when(mockHandler.getInvocationContainer().hasAnswersForStubbing()).thenReturn(true);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        
        // Execute
        Object result = mockHandler.handle(mock(Invocation.class));
        
        // Verify
        verify(mockHandler.getInvocationContainer()).setMethodForStubbing(invocationMatcher);
        Assert.assertNull(result);
    }
    
    @Test
    public void testHandle_whenVerificationModeExists_thenVerifyInvocations() throws Throwable {
        // Setup
        mockHandler.mockingProgress.pullVerificationMode();
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        
        // Execute
        Object result = mockHandler.handle(mock(Invocation.class));
        
        // Verify
        verify(mockHandler.mockingProgress).validateState();
        verify(mockHandler.getInvocationContainer()).getInvocations();
        verify(mockHandler.mockingProgress).verify(any(VerificationDataImpl.class));
        Assert.assertNull(result);
    }
    
    @Test
    public void testHandle_whenNoStubbedInvocationExists_thenDefaultAnswerIsCalled() throws Throwable {
        // Setup
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
        when(mockHandler.mockSettings.getDefaultAnswer().answer(invocation)).thenReturn("default answer");
        
        // Execute
        Object result = mockHandler.handle(invocation);
        
        // Verify
        verify(mockHandler.getInvocationContainer()).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.getInvocationContainer()).findAnswerFor(invocation);
        verify(mockHandler.mockSettings.getDefaultAnswer()).answer(invocation);
        verify(mockHandler.getInvocationContainer()).resetInvocationForPotentialStubbing(invocationMatcher);
        Assert.assertEquals("default answer", result);
    }
    
    @Test
    public void testVoidMethodStubbable_whenVoidMethodCalled_thenVoidMethodStubbableIsReturned() {
        // Setup
        Object mock = mock(Object.class);
        
        // Execute
        VoidMethodStubbable result = mockHandler.voidMethodStubbable(mock);
        
        // Verify
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testGetMockSettings() {
        // Execute
        MockSettingsImpl result = mockHandler.getMockSettings();
        
        // Verify
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testSetAnswersForStubbing() {
        // Execute
        mockHandler.setAnswersForStubbing(answers);
        
        // Verify
        verify(mockHandler.getInvocationContainer()).setAnswersForStubbing(answers);
    }
    
    @Test
    public void testGetInvocationContainer() {
        // Execute
        InvocationContainer result = mockHandler.getInvocationContainer();
        
        // Verify
        Assert.assertNotNull(result);
    }
}
