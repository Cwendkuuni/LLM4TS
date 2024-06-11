import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class MockHandlerTest {

    private MockHandler mockHandler;

    @Before
    public void setUp() {
        mockHandler = new MockHandler();
    }
    
    // Test handle() method when there are answers for stubbing
    
    @Test
    public void testHandle_WithAnswerForStubbing_ShouldReturnNull() throws Throwable {
        List<Answer> answers = Arrays.asList(mock(Answer.class), mock(Answer.class));
        mockHandler.setAnswersForStubbing(answers);
        
        Invocation invocation = mock(Invocation.class);
        
        assertNull(mockHandler.handle(invocation));
    }
    
    // Test handle() method when there is a verification mode
    
    @Test
    public void testHandle_WithVerificationModeAndCorrectMock_ShouldVerify() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        
        MockAwareVerificationMode mockAwareVerificationMode = new MockAwareVerificationMode(verificationMode, invocation.getMock());
        when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn(null);
        
        mockHandler.handle(invocation);
        mockHandler.handle(invocation); // invoking handle() twice for testing purposes
        
        verify(verificationMode).verify(any(VerificationDataImpl.class));
    }
    
    @Test
    public void testHandle_WithVerificationModeAndDifferentMock_ShouldNotVerify() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        Invocation otherInvocation = mock(Invocation.class);
        VerificationMode verificationMode = mock(VerificationMode.class);
        
        MockAwareVerificationMode mockAwareVerificationMode = new MockAwareVerificationMode(verificationMode, otherInvocation.getMock());
        when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(mock(Answer.class));
        when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn(null);
        
        mockHandler.handle(invocation);
        mockHandler.handle(invocation); // invoking handle() twice for testing purposes
        
        verify(verificationMode, never()).verify(any(VerificationDataImpl.class));
    }
    
    // Test handle() method when there is no answer found
    
    @Test
    public void testHandle_WithoutStubbedInvocation_ShouldUseDefaultAnswer() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        
        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "default";
            }
        };
        when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(defaultAnswer);
        
        assertEquals("default", mockHandler.handle(invocation));
    }
    
    @Test
    public void testHandle_WithoutStubbedInvocation_ShouldResetInvocationForPotentialStubbing() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        
        Answer<Object> defaultAnswer = new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "default";
            }
        };
        when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(defaultAnswer);
        
        mockHandler.handle(invocation);
        
        verify(mockHandler.getInvocationContainer()).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
    }
    
    // Test voidMethodStubbable() method
    
    @Test
    public void testVoidMethodStubbable_ShouldReturnVoidMethodStubbableInstance() {
        List<Invocation> invocations = new ArrayList<>();
        when(mockHandler.getInvocationContainer().getInvocations()).thenReturn(invocations);
        
        VoidMethodStubbable<Void> stubbable = mockHandler.voidMethodStubbable(Void.TYPE);
        
        assertNotNull(stubbable);
        assertEquals(invocations, stubbable.getInvocations());
    }
    
    // Test setAnswersForStubbing() method
    
    @Test
    public void testSetAnswersForStubbing_ShouldSetAnswersForStubbing() {
        List<Answer> answers = Arrays.asList(mock(Answer.class), mock(Answer.class));
        
        mockHandler.setAnswersForStubbing(answers);
        
        assertEquals(answers, mockHandler.getInvocationContainer().getAnswersForStubbing());
    }
    
    // Test getInvocationContainer() method
    
    @Test
    public void testGetInvocationContainer_ShouldReturnInvocationContainerInstance() {
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        mockHandler.invocationContainerImpl = container;
        
        assertEquals(container, mockHandler.getInvocationContainer());
    }
}