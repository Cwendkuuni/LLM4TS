import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;
import java.util.List;

public class MockHandlerTest {
  
  private MockHandler<Object> mockHandler;
  private InvocationContainerImpl invocationContainerImpl;
  private MatchersBinder matchersBinder;
  private MockingProgress mockingProgress;
  private MockHandler<Object> oldMockHandler;
  
  @Before
  public void setup() {
    mockHandler = new MockHandler<>();
    invocationContainerImpl = Mockito.mock(InvocationContainerImpl.class);
    matchersBinder = Mockito.mock(MatchersBinder.class);
    mockingProgress = Mockito.mock(ThreadSafeMockingProgress.class);
    oldMockHandler = Mockito.mock(MockHandler.class);
    mockHandler.invocationContainerImpl = invocationContainerImpl;
    mockHandler.matchersBinder = matchersBinder;
    mockHandler.mockingProgress = mockingProgress;
  }
  
  @Test
  public void testHandle() throws Throwable {
    Invocation invocation = Mockito.mock(Invocation.class);
    InvocationMatcher invocationMatcher = Mockito.mock(InvocationMatcher.class);
    VerificationMode verificationMode = Mockito.mock(VerificationMode.class);
    Answer<Object> defaultAnswer = Mockito.mock(Answer.class);
    OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
    mockHandler.mockSettings.setDefaultAnswer(defaultAnswer);
    Mockito.when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
    Mockito.when(matchersBinder.bindMatchers(Mockito.any(), Mockito.any())).thenReturn(invocationMatcher);
    Mockito.when(mockingProgress.pullVerificationMode()).thenReturn(verificationMode);
    Mockito.when(invocation.getMock()).thenReturn(new Object());
    Mockito.when(mockingProgress.getArgumentMatcherStorage()).thenReturn(Mockito.any());
    Mockito.when(invocationContainerImpl.findAnswerFor(Mockito.any())).thenReturn(null);
    Mockito.when(mockHandler.voidMethodStubbable(Mockito.any())).thenReturn(ongoingStubbing);
    
    mockHandler.handle(invocation);
    
    Mockito.verify(invocationContainerImpl).hasAnswersForStubbing();
    Mockito.verify(matchersBinder).bindMatchers(Mockito.any(), Mockito.any());
    Mockito.verify(mockingProgress).pullVerificationMode();
    Mockito.verify(invocation.getMock()).thenReturn(new Object());
    Mockito.verify(mockingProgress).getArgumentMatcherStorage();
    Mockito.verify(invocationContainerImpl).setInvocationForPotentialStubbing(invocationMatcher);
    Mockito.verify(mockingProgress).reportOngoingStubbing(ongoingStubbing);
    Mockito.verify(invocationContainerImpl).findAnswerFor(invocation);
    Mockito.verify(mockingProgress).validateState();
    Mockito.verify(mockHandler.voidMethodStubbable(Mockito.any())).thenReturn(ongoingStubbing);
    Mockito.verify(invocationContainerImpl).resetInvocationForPotentialStubbing(invocationMatcher);
  }
  
  @Test
  public void testVoidMethodStubbable() {
    Object mock = Mockito.mock(Object.class);
    
    mockHandler.voidMethodStubbable(mock);
    
    Mockito.verify(mockHandler).voidMethodStubbable(mock);
  }
  
  @Test
  public void testSetAnswersForStubbing() {
    List<Answer> answers = Mockito.mock(List.class);
    
    mockHandler.setAnswersForStubbing(answers);
    
    Mockito.verify(invocationContainerImpl).setAnswersForStubbing(answers);
  }
  
  @Test
  public void testGetInvocationContainer() {
    InvocationContainerImpl container = mockHandler.getInvocationContainer();
    
    assertSame(invocationContainerImpl, container);
  }
}