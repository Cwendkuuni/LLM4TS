import static org.mockito.Mockito.*;
import org.junit.Before;
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
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

import java.util.List;

public class MockHandlerTest {
  
  private MockHandler<Object> mockHandler;
  
  @Before
  public void setup() {
    MockSettingsImpl mockSettings = new MockSettingsImpl();
    mockHandler = new MockHandler<>(mockSettings);
  }
  
  @Test
  public void testHandle_withValidInvocation_callsValidateState() throws Throwable {
    MockingProgress mockingProgress = mock(MockingProgress.class);
    Invocation invocation = mock(Invocation.class);
    
    when(mockHandler.mockingProgress).thenReturn(mockingProgress);
    when(mockingProgress.pullVerificationMode()).thenReturn(null);
    
    mockHandler.handle(invocation);
    
    verify(mockingProgress).validateState();
  }
  
  @Test
  public void testHandle_withStubbedInvocation_returnsStubbedAnswer() throws Throwable {
    Invocation invocation = mock(Invocation.class);
    OngoingStubbingImpl<Object> ongoingStubbing = mock(OngoingStubbingImpl.class);
    StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
    Answer<Object> defaultAnswer = mock(Answer.class);
    
    when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);
    when(stubbedInvocation.answer(invocation)).thenReturn("stubbedAnswer");
    when(mockHandler.mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
    
    Object result = mockHandler.handle(invocation);
    
    assertEquals("stubbedAnswer", result);
    verify(stubbedInvocation).captureArgumentsFrom(invocation);
  }
  
  @Test
  public void testHandle_withoutStubbedInvocation_returnsDefaultAnswer() throws Throwable {
    Invocation invocation = mock(Invocation.class);
    Answer<Object> defaultAnswer = mock(Answer.class);
    
    when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
    when(mockHandler.mockSettings.getDefaultAnswer()).thenReturn(defaultAnswer);
    
    Object result = mockHandler.handle(invocation);
    
    assertNull(result);
  }
  
  @Test
  public void testVoidMethodStubbable_returnsVoidMethodStubbableImpl() {
    Object mock = new Object();
    VoidMethodStubbable<T> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
    
    assertNotNull(voidMethodStubbable);
    assertTrue(voidMethodStubbable instanceof VoidMethodStubbableImpl);
  }
  
  @Test
  public void testSetAnswersForStubbing_setsAnswersForStubbingInInvocationContainer() {
    List<Answer> answers = mock(List.class);
    
    mockHandler.setAnswersForStubbing(answers);
    
    verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
  }
  
  @Test
  public void testGetInvocationContainer_returnsInvocationContainerImpl() {
    InvocationContainer invocationContainer = mockHandler.getInvocationContainer();
    
    assertNotNull(invocationContainer);
    assertTrue(invocationContainer instanceof InvocationContainerImpl);
  }
}