import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MockHandlerTest {

    private MockHandler mockHandler;
    
    @Before
    public void setUp() {
        mockHandler = new MockHandler();
    }
    
    @Test
    public void testHandle_withInvocationHasAnswerForStubbing_shouldSetMethodForStubbingAndReturnNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.getInvocationContainer().hasAnswersForStubbing()).thenReturn(true);
        Object result = mockHandler.handle(invocation);
        assertNull(result);
        verify(mockHandler.getInvocationContainer()).setMethodForStubbing(any(InvocationMatcher.class));
    }
    
    @Test
    public void testHandle_withVerificationModeIsNull_shouldNotVerifyDataAndReturnNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.getInvocationContainer().hasAnswersForStubbing()).thenReturn(false);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockHandler.getMatchersBinder().bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        when(mockHandler.getMockingProgress().pullVerificationMode()).thenReturn(null);
        
        Object result = mockHandler.handle(invocation);
        
        assertNull(result);
        verify(mockHandler.getMockingProgress(), never()).validateState();
        verify(mockHandler.getInvocationContainer(), never()).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.getInvocationContainer(), never()).findAnswerFor(invocation);
        verify(invocation, never()).getMock();
    }
    
    @Test
    public void testHandle_withVerificationModeIsValidMockAwareModeAndMockIsDifferent_shouldNotVerifyDataAndReturnNull() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.getInvocationContainer().hasAnswersForStubbing()).thenReturn(false);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockHandler.getMatchersBinder().bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        
        MockAwareVerificationMode verificationMode = mock(MockAwareVerificationMode.class);
        when(verificationMode.getMock()).thenReturn(mock(Object.class));
        
        when(mockHandler.getMockingProgress().pullVerificationMode()).thenReturn(verificationMode);
        
        Object result = mockHandler.handle(invocation);
        
        assertNull(result);
        verify(mockHandler.getMockingProgress(), never()).validateState();
        verify(mockHandler.getInvocationContainer(), never()).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.getInvocationContainer(), never()).findAnswerFor(invocation);
        verify(invocation, never()).getMock();
    }
    
    @Test
    public void testHandle_withMatchedInvocation_shouldSetInvocationForPotentialStubbingAndReturnAnswerResult() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.getInvocationContainer().hasAnswersForStubbing()).thenReturn(false);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockHandler.getMatchersBinder().bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        
        when(mockHandler.getMockingProgress().pullVerificationMode()).thenReturn(null);
        
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(mockHandler.getInvocationContainer().findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        Object answerResult = new Object();
        when(stubbedInvocation.answer(invocation)).thenReturn(answerResult);
        
        Object result = mockHandler.handle(invocation);
        
        assertEquals(answerResult, result);
        verify(mockHandler.getMockingProgress()).validateState();
        verify(mockHandler.getInvocationContainer()).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.getMockSettings(), never()).getDefaultAnswer();
        verify(mockHandler.getInvocationContainer(), never()).resetInvocationForPotentialStubbing(invocationMatcher);
    }
    
    @Test
    public void testHandle_withUnmatchedInvocation_shouldSetInvocationForPotentialStubbingAndReturnDefaultAnswerResult() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        when(mockHandler.getInvocationContainer().hasAnswersForStubbing()).thenReturn(false);
        InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
        when(mockHandler.getMatchersBinder().bindMatchers(any(), eq(invocation))).thenReturn(invocationMatcher);
        
        when(mockHandler.getMockingProgress().pullVerificationMode()).thenReturn(null);
        
        when(mockHandler.getInvocationContainer().findAnswerFor(invocation)).thenReturn(null);
        Object answerResult = new Object();
        when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn(answerResult);
        
        Object result = mockHandler.handle(invocation);
        
        assertEquals(answerResult, result);
        verify(mockHandler.getMockingProgress()).validateState();
        verify(mockHandler.getInvocationContainer()).setInvocationForPotentialStubbing(invocationMatcher);
        verify(mockHandler.getMockSettings()).getDefaultAnswer();
        verify(mockHandler.getInvocationContainer()).resetInvocationForPotentialStubbing(invocationMatcher);
    }
    
    @Test
    public void testVoidMethodStubbable_withMock_shouldReturnInstanceOfVoidMethodStubbableImpl() {
        Object mock = mock(Object.class);
        VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);
        assertNotNull(stubbable);
        assertTrue(stubbable instanceof VoidMethodStubbableImpl);
        assertSame(mock, ((VoidMethodStubbableImpl) stubbable).getMock());
        assertSame(mockHandler.getInvocationContainer(), ((VoidMethodStubbableImpl) stubbable).getInvocationContainer());
    }
    
    @Test
    public void testSetAnswersForStubbing_shouldSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();
        mockHandler.setAnswersForStubbing(answers);
        verify(mockHandler.getInvocationContainer()).setAnswersForStubbing(answers);
    }

}