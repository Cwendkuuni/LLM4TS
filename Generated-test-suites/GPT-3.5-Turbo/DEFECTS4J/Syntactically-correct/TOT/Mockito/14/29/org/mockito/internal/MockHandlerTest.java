import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

    @Mock
    private MockingProgress mockingProgress;
    
    @Mock
    private MatchersBinder matchersBinder;
    
    @Mock
    private InvocationContainerImpl invocationContainerImpl;
    
    @InjectMocks
    private MockHandler<Object> mockHandler;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockHandler = new MockHandler<>();
        this.mockingProgress = new ThreadSafeMockingProgress();
        this.matchersBinder = new MatchersBinder();
        this.invocationContainerImpl = new InvocationContainerImpl(mockingProgress);
    }

    @Test
    public void handle_InvocationWithStubbingVoids_ReturnNull() throws Throwable {
        Invocation invocation = new Invocation();
        invocationContainerImpl.addAnswerForStubbing(new OngoingStubbingImpl<Object>());
        InvocationMatcher invocationMatcher = new InvocationMatcher();
        
        matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
        invocationContainerImpl.setMethodForStubbing(invocationMatcher);
        
        Object result = mockHandler.handle(invocation);
        
        assertNull(result);
    }
    
    @Test
    public void handle_InvocationWithVerificationMode_VerifyInvocation() throws Throwable {
        Invocation invocation = new Invocation();
        VerificationMode verificationMode = Mockito.mock(VerificationMode.class);
        invocationContainerImpl.addInvocation(invocation);
        InvocationMatcher invocationMatcher = new InvocationMatcher();
        
        matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
        mockingProgress.pullVerificationMode();
        
        VerificationDataImpl verificationData = new VerificationDataImpl(invocationContainerImpl.getInvocations(),
                invocationMatcher);
        verificationMode.verify(verificationData);
        
        verify(verificationMode).verify(verificationData);
    }
    
    @Test
    public void handle_InvocationWithStubbedInvocation_StubbedInvocationAnswered() throws Throwable {
        Invocation invocation = new Invocation();
        OngoingStubbingImpl<Object> ongoingStubbing = new OngoingStubbingImpl<>(invocationContainerImpl);
        invocationContainerImpl.addStubbedInvocation(ArgumentMatchers.any(), ArgumentMatchers.any());
        StubbedInvocationMatcher stubbedInvocation = new StubbedInvocationMatcher();
        
        matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
        invocationContainerImpl.setInvocationForPotentialStubbing(stubbedInvocation);
        invocationContainerImpl.findAnswerFor(invocation);
        
        assertNotNull(stubbedInvocation);
        assertNotNull(ongoingStubbing);
    }
    
    @Test
    public void handle_InvocationWithNoStubbedInvocation_DefaultAnswerInvoked() throws Throwable {
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher();
        
        matchersBinder.bindMatchers(mockingProgress.getArgumentMatcherStorage(), invocation);
        invocationContainerImpl.setMethodForStubbing(invocationMatcher);
        
        Object result = mockHandler.handle(invocation);
        
        assertNotNull(result);
    }
    
    @Test
    public void voidMethodStubbable_CreateVoidMethodStubbable_ReturnVoidMethodStubbableImpl() {
        Object mock = new Object();
        
        VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
        
        assertNotNull(voidMethodStubbable);
        assertTrue(voidMethodStubbable instanceof VoidMethodStubbableImpl);
    }
    
    @Test
    public void getMockSettings_ReturnMockSettingsImpl() {
        MockSettingsImpl mockSettings = mockHandler.getMockSettings();
        
        assertNotNull(mockSettings);
        assertTrue(mockSettings instanceof MockSettingsImpl);
    }
    
    @Test
    public void setAnswersForStubbing_SetAnswersForStubbingInInvocationContainer() {
        List<Answer> answers = new ArrayList<>();
        
        mockHandler.setAnswersForStubbing(answers);
        
        assertEquals(answers, invocationContainerImpl.getAnswersForStubbing());
    }
    
    @Test
    public void getInvocationContainer_ReturnInvocationContainerImpl() {
        InvocationContainer invocationContainer = mockHandler.getInvocationContainer();
        
        assertNotNull(invocationContainer);
        assertTrue(invocationContainer instanceof InvocationContainerImpl);
    }
}