import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {
    
    private MockHandler<String> mockHandler;
    
    @Before
    public void setup() {
        MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
        mockHandler = new MockHandler<>(mockSettings);
    }
    
    @Test
    public void testHandle() throws Throwable {
        Invocation invocation = mock(Invocation.class);
        ArgumentMatchers argumentMatchers = mock(ArgumentMatchers.class);
        Answer defaultAnswer = mock(Answer.class);
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        
        when(invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
        when(mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
        
        Object result = mockHandler.handle(invocation);
        
        verify(invocationContainerImpl).findAnswerFor(invocation);
        verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
        verify(defaultAnswer).answer(invocation);
        
        assertEquals(stubbedInvocation.answer(invocation), result);
    }
    
    @Test
    public void testVoidMethodStubbable() {
        String mock = "mock";
        VoidMethodStubbableImpl<String> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
        
        assertNotNull(voidMethodStubbable);
        assertEquals(mock, voidMethodStubbable.getMock());
        assertEquals(invocationContainerImpl, voidMethodStubbable.getInvocationContainer());
    }
    
    @Test
    public void testGetMockSettings() {
        MockSettingsImpl mockSettings = mockHandler.getMockSettings();
        
        assertEquals(mockSettings, mockSettings);
    }
    
    @Test
    public void testSetAnswersForStubbing() {
        List<Answer> answers = new ArrayList<>();
        answers.add(mock(Answer.class));
        answers.add(mock(Answer.class));
        
        mockHandler.setAnswersForStubbing(answers);
        
        verify(invocationContainerImpl).setAnswersForStubbing(answers);
    }
    
    @Test
    public void testGetInvocationContainer() {
        InvocationContainer invocationContainer = mockHandler.getInvocationContainer();
        
        assertEquals(invocationContainerImpl, invocationContainer);
    }
    
}