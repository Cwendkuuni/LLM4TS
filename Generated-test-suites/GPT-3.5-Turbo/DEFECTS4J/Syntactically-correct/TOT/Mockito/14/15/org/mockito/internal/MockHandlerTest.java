import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.stubbing.Answer;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationDataImpl;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {
    
    private MockHandler<Object> mockHandler;
    
    @Before
    public void setUp() {
        mockHandler = new MockHandler<Object>();
    }
    
    @Test
    public void handle_WhenHasAnswersForStubbing_ReturnsNull() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = new InvocationContainerImpl(null);
        invocationContainerImpl.setAnswersForStubbing(new ArrayList<Answer>());
        mockHandler.setInvocationContainer(invocationContainerImpl);
        
        Invocation invocation = new Invocation(null, null, null, 0);
        
        assertNull(mockHandler.handle(invocation));
    }
    
    @Test
    public void handle_WhenVerificationModeNotNull_VerifyDataAndReturnsNull() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = new InvocationContainerImpl(null);
        MockingProgress mockingProgress = new ThreadSafeMockingProgress();
        invocationContainerImpl.setInvocations(new ArrayList<Invocation>());
        invocationContainerImpl.setStubbedInvocations(new ArrayList<StubbedInvocationMatcher>());
        MatchersBinder matchersBinder = new MatchersBinder();
        mockingProgress.setArgumentMatcherStorage(null);
        mockHandler.setMatchersBinder(matchersBinder);
        mockHandler.setMockingProgress(mockingProgress);
        mockHandler.setInvocationContainer(invocationContainerImpl);
        
        Invocation invocation = new Invocation(null, null, null, 0);
        InvocationMatcher invocationMatcher = new InvocationMatcher(null, null);
        invocationContainerImpl.setMethodForStubbing(invocationMatcher);
        
        VerificationMode verificationMode = new VerificationMode();
        mockingProgress.setVerificationMode(verificationMode);
        
        assertNull(mockHandler.handle(invocation));
    }
    
    @Test
    public void handle_WhenStubbedInvocationNotNull_CaptureArgumentsAndReturnsAnswer() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = new InvocationContainerImpl(null);
        MatchersBinder matchersBinder = new MatchersBinder();
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer defaultAnswer = new Answer();
        mockSettings.setDefaultAnswer(defaultAnswer);
        mockHandler.setInvocationContainer(invocationContainerImpl);
        mockHandler.setMatchersBinder(matchersBinder);
        mockHandler.setMockSettings(mockSettings);
        
        Invocation invocation = new Invocation(null, null, null, 0);
        
        StubbedInvocationMatcher stubbedInvocation = new StubbedInvocationMatcher();
        invocationContainerImpl.setStubbedInvocation(stubbedInvocation);
        
        assertNotNull(mockHandler.handle(invocation));
    }
    
    @Test
    public void handle_WhenStubbedInvocationNull_CallsDefaultAnswerAndResetInvocationForPotentialStubbing() throws Throwable {
        InvocationContainerImpl invocationContainerImpl = new InvocationContainerImpl(null);
        MatchersBinder matchersBinder = new MatchersBinder();
        MockSettingsImpl mockSettings = new MockSettingsImpl();
        Answer defaultAnswer = new Answer();
        mockSettings.setDefaultAnswer(defaultAnswer);
        mockHandler.setInvocationContainer(invocationContainerImpl);
        mockHandler.setMatchersBinder(matchersBinder);
        mockHandler.setMockSettings(mockSettings);
        
        Invocation invocation = new Invocation(null, null, null, 0);
        
        assertNull(mockHandler.handle(invocation));
    }
    
    @Test
    public void voidMethodStubbable_ReturnsVoidMethodStubbableImplObject() {
        Object mock = new Object();
        VoidMethodStubbableImpl voidMethodStubbableImpl = new VoidMethodStubbableImpl(mock, null);
        
        assertEquals(voidMethodStubbableImpl, mockHandler.voidMethodStubbable(mock));
    }
    
    @Test
    public void setAnswersForStubbing_SetsAnswersForStubbingInInvocationContainer() {
        List<Answer> answers = new ArrayList<Answer>();
        invocationContainerImpl.setAnswersForStubbing(answers);
        
        assertEquals(answers, invocationContainerImpl.getAnswersForStubbing());
    }
    
    @Test
    public void getInvocationContainer_ReturnsInvocationContainerImplObject() {
        InvocationContainerImpl invocationContainerImpl = new InvocationContainerImpl(null);
        mockHandler.setInvocationContainer(invocationContainerImpl);
        
        assertEquals(invocationContainerImpl, mockHandler.getInvocationContainer());
    }

}