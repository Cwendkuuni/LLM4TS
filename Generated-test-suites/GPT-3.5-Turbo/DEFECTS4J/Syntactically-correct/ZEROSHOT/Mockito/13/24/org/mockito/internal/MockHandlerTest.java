import org.junit.Test;
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
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;
import static org.mockito.Mockito.*;

public class MockHandlerTest {
  
  @Test
  public void testHandle() throws Throwable {
    MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
    MockHandler mockHandler = new MockHandler(mockSettings);
    InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
    MatchersBinder matchersBinder = mock(MatchersBinder.class);
    MockingProgress mockingProgress = mock(MockingProgress.class);
    
    mockHandler.invocationContainerImpl = invocationContainer;
    mockHandler.matchersBinder = matchersBinder;
    mockHandler.mockingProgress = mockingProgress;
    
    Invocation invocation = mock(Invocation.class);
    InvocationMatcher invocationMatcher = mock(InvocationMatcher.class);
    Answer answer = mock(Answer.class);
    VerificationMode verificationMode = mock(VerificationMode.class);
    StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
    
    when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
    when(matchersBinder.bindMatchers(any(), any())).thenReturn(invocationMatcher);
    when(mockingProgress.getArgumentMatcherStorage()).thenReturn(null);
    when(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
    when(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(invocation.getMock());
    when(invocationContainer.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
    when(stubbedInvocation.answer(invocation)).thenReturn(null);
    
    Object result = mockHandler.handle(invocation);
    
    verify(invocationContainer).hasAnswersForStubbing();
    verify(matchersBinder).bindMatchers(any(), any());
    verify(mockingProgress).getArgumentMatcherStorage();
    verify(verificationMode instanceof MockAwareVerificationMode).thenReturn(true);
    verify(((MockAwareVerificationMode) verificationMode).getMock()).thenReturn(invocation.getMock());
    verify(invocationContainer).findAnswerFor(invocation);
    verify(stubbedInvocation).captureArgumentsFrom(invocation);
    verify(stubbedInvocation).answer(invocation);
  }
  
  @Test
  public void testVoidMethodStubbable() {
    MockHandler mockHandler = new MockHandler();
    T mock = mock(T.class);
    InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
    
    mockHandler.invocationContainerImpl = invocationContainer;
    
    VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);
    
    verify(invocationContainer).resetInvocationForPotentialStubbing(invocationMatcher);
  }
  
  @Test
  public void testGetMockSettings() {
    MockHandler mockHandler = new MockHandler();
    MockSettingsImpl mockSettings = mock(MockSettingsImpl.class);
    
    mockHandler.mockSettings = mockSettings;
    
    assertEquals(mockSettings, mockHandler.getMockSettings());
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testSetAnswersForStubbing() {
    MockHandler mockHandler = new MockHandler();
    List<Answer> answers = mock(List.class);
    InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
    
    mockHandler.invocationContainerImpl = invocationContainer;
    
    mockHandler.setAnswersForStubbing(answers);
    
    verify(invocationContainer).setAnswersForStubbing(answers);
  }
  
  @Test
  public void testGetInvocationContainer() {
    MockHandler mockHandler = new MockHandler();
    InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
    
    mockHandler.invocationContainerImpl = invocationContainer;
    
    assertEquals(invocationContainer, mockHandler.getInvocationContainer());
  }
}