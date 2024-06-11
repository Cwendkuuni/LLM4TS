import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {
  
  private MockHandler mockHandler;
  
  @Before
  public void setUp() {
    mockHandler = new MockHandler();
  }
  
  @Test
  public void testHandle() throws Throwable {
    Invocation invocation = mock(Invocation.class);
    mockHandler.handle(invocation);
    verify(invocationContainerImpl, times(1)).hasAnswersForStubbing();
    verify(mockingProgress, times(1)).getArgumentMatcherStorage();
    verify(invocationContainerImpl, times(1)).setMethodForStubbing(any(InvocationMatcher.class));
    verify(mockingProgress, times(1)).pullVerificationMode();
    verify(mockingProgress, times(1)).getArgumentMatcherStorage();
    verify(invocationContainerImpl, times(1)).setInvocationForPotentialStubbing(any(InvocationMatcher.class));
    verify(invocationContainerImpl, times(1)).findAnswerFor(invocation);
    verify(mockSettings, times(1)).getDefaultAnswer();
    verify(invocationContainerImpl, times(1)).resetInvocationForPotentialStubbing(any(InvocationMatcher.class));
  }
  
  @Test
  public void testVoidMethodStubbable() {
    Object mock = mock(Object.class);
    VoidMethodStubbable stubbable = mockHandler.voidMethodStubbable(mock);
    assertNotNull(stubbable);
    assertTrue(stubbable instanceof VoidMethodStubbableImpl);
  }
  
  @Test
  public void testGetMockSettings() {
    MockSettingsImpl settings = mockHandler.getMockSettings();
    assertNotNull(settings);
    assertSame(settings, mockSettings);
  }
  
  @Test
  public void testSetAnswersForStubbing() {
    List<Answer> answers = mock(List.class);
    mockHandler.setAnswersForStubbing(answers);
    verify(invocationContainerImpl, times(1)).setAnswersForStubbing(answers);
  }
  
  @Test
  public void testGetInvocationContainer() {
    InvocationContainer container = mockHandler.getInvocationContainer();
    assertNotNull(container);
    assertSame(container, invocationContainerImpl);
  }
}