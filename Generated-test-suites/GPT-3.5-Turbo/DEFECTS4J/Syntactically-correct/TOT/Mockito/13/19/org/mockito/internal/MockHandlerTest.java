import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.stubbing.VoidMethodStubbable;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class MockHandlerTest {

  private MockHandler<String> mockHandler;
  private Invocation invocation;

  @Before
  public void setUp() {
    mockHandler = new MockHandler<>();
    invocation = mock(Invocation.class);
  }

  @Test
  public void handle_invocationContainerHasAnswersForStubbing_returnNull() throws Throwable {
    InvocationContainerImpl invocationContainer = mock(InvocationContainerImpl.class);
    when(invocationContainer.hasAnswersForStubbing()).thenReturn(true);
    mockHandler.invocationContainerImpl = invocationContainer;

    Object result = mockHandler.handle(invocation);

    assertNull(result);
  }

  @Test
  public void handle_verificationModeIsNull_returnNull() throws Throwable {
    mockHandler.mockingProgress = mock(ThreadSafeMockingProgress.class);
    when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(null);

    Object result = mockHandler.handle(invocation);

    assertNull(result);
  }

  @Test
  public void handle_verificationModeIsNotNullAndMockIsSame_handleVerification() throws Throwable {
    mockHandler.mockingProgress = mock(ThreadSafeMockingProgress.class);
    MockitoInvocationHandler mockAwareVerificationMode = mock(MockAwareVerificationMode.class);
    when(mockAwareVerificationMode.getMock()).thenReturn(invocation.getMock());
    when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(mockAwareVerificationMode);

    Object result = mockHandler.handle(invocation);

    assertNull(result);
  }

  @Test
  public void handle_stubbedInvocationNotNull_captureArgumentsAndAnswerInvocation() throws Throwable {
    mockHandler.invocationContainerImpl = mock(InvocationContainerImpl.class);
    OngoingStubbingImpl<String> ongoingStubbing = mock(OngoingStubbingImpl.class);
    when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(mock(StubbedInvocationMatcher.class));
    when(mockHandler.invocationContainerImpl.resetInvocationForPotentialStubbing(any())).thenReturn(null);
    when(ongoingStubbing.answer(invocation)).thenReturn("Stubbed answer");
    when(mockHandler.mockSettings.getDefaultAnswer()).thenReturn(ongoingStubbing);

    Object result = mockHandler.handle(invocation);

    assertNotNull(result);
    assertEquals("Stubbed answer", result);
  }

  @Test
  public void handle_stubbedInvocationNull_returnDefaultAnswer() throws Throwable {
    mockHandler.invocationContainerImpl = mock(InvocationContainerImpl.class);
    when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(null);
    when(mockHandler.mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));

    Object result = mockHandler.handle(invocation);

    assertNotNull(result);
  }

  @Test
  public void voidMethodStubbable_returnVoidMethodStubbable() {
    String mock = mock(String.class);

    VoidMethodStubbable<String> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

    assertNotNull(voidMethodStubbable);
  }

  @Test
  public void getMockSettings_returnMockSettings() {
    assertNotNull(mockHandler.getMockSettings());
  }

  @Test
  public void setAnswersForStubbing_setAnswersForStubbingInInvocationContainer() {
    @SuppressWarnings("unchecked")
    List<Answer> answers = mock(List.class);

    mockHandler.setAnswersForStubbing(answers);

    verify(mockHandler.invocationContainerImpl).setAnswersForStubbing(answers);
  }

  @Test
  public void getInvocationContainer_returnInvocationContainer() {
    assertNotNull(mockHandler.getInvocationContainer());
  }
}