import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.invocation.MockHandler;
import org.mockito.internal.invocation.OngoingStubbing;
import org.mockito.internal.invocation.VerificationData;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;
import org.mockito.stubbing.VoidMethodStubbable;
import org.mockito.verification.VerificationMode;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

  @Test
  public void testHandle_whenInvocationContainerHasAnswersForStubbing_shouldSetMethodForStubbing() throws Throwable {
    // Arrange
    MockHandler<Object> mockHandler = new MockHandler<Object>();

    Invocation invocation = mock(Invocation.class);
    when(invocationContainerImpl.hasAnswersForStubbing()).thenReturn(true);
    when(matchersBinder.bindMatchers(any(), any())).thenReturn(new InvocationMatcher());

    // Act
    Object result = mockHandler.handle(invocation);

    // Assert
    verify(matchersBinder).bindMatchers(any(), any());
    verify(invocationContainerImpl).setMethodForStubbing(any());
    assertEquals(result, null);
  }

  @Test
  public void testHandle_whenVerificationModeNotNull_shouldVerifyData() throws Throwable {
    // Arrange
    MockHandler<Object> mockHandler = new MockHandler<Object>();

    Invocation invocation = mock(Invocation.class);
    when(matchersBinder.bindMatchers(any(), any())).thenReturn(new InvocationMatcher());
    when(mockingProgress.pullVerificationMode()).thenReturn(mock(VerificationMode.class));

    // Act
    Object result = mockHandler.handle(invocation);

    // Assert
    verify(matchersBinder).bindMatchers(any(), any());
    verify(mockingProgress).pullVerificationMode();
    verify(mockingProgress).validateState();
    verify(invocationContainerImpl).getInvocations();
    verify(invocationContainerImpl).setInvocationForPotentialStubbing(any());
    verify(data).verify(any());
    assertEquals(result, null);
  }

  @Test
  public void testHandle_whenStubbedInvocationNotNull_shouldCaptureArgumentsAndReturnAnswer() throws Throwable {
    // Arrange
    MockHandler<Object> mockHandler = new MockHandler<Object>();

    Invocation invocation = mock(Invocation.class);
    when(matchersBinder.bindMatchers(any(), any())).thenReturn(new InvocationMatcher());
    when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
    when(invocationContainerImpl.findAnswerFor(any())).thenReturn(mock(StubbedInvocationMatcher.class));

    // Act
    Object result = mockHandler.handle(invocation);

    // Assert
    verify(matchersBinder).bindMatchers(any(), any());
    verify(mockSettings).getDefaultAnswer();
    verify(invocationContainerImpl).findAnswerFor(any());
    verify(stubbedInvocation).captureArgumentsFrom(any());
    verify(stubbedInvocation).answer(any());
    assertEquals(result, null);
  }

  @Test
  public void testHandle_whenStubbedInvocationIsNull_shouldAnswerWithDefaultAnswer() throws Throwable {
    // Arrange
    MockHandler<Object> mockHandler = new MockHandler<Object>();

    Invocation invocation = mock(Invocation.class);
    when(matchersBinder.bindMatchers(any(), any())).thenReturn(new InvocationMatcher());
    when(mockSettings.getDefaultAnswer()).thenReturn(mock(Answer.class));
    when(invocationContainerImpl.findAnswerFor(any())).thenReturn(null);

    // Act
    Object result = mockHandler.handle(invocation);

    // Assert
    verify(matchersBinder).bindMatchers(any(), any());
    verify(mockSettings).getDefaultAnswer();
    verify(invocationContainerImpl).findAnswerFor(any());
    verify(invocationContainerImpl).resetInvocationForPotentialStubbing(any());
    assertEquals(result, null);
  }

  @Test
  public void testVoidMethodStubbable_shouldReturnVoidMethodStubbableObject() {
    // Arrange
    MockHandler<Object> mockHandler = new MockHandler<Object>();
    Object mock = mock(Object.class);

    // Act
    VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);

    // Assert
    assertThat(voidMethodStubbable, notNullValue());
    assertEquals(mock, voidMethodStubbable.getMock());
    assertEquals(invocationContainerImpl, voidMethodStubbable.getInvocationContainer());
  }

  @Test
  public void testSetAnswersForStubbing_shouldSetAnswersForStubbingInInvocationContainer() {
    // Arrange
    MockHandler<Object> mockHandler = new MockHandler<Object>();
    List<Answer> answers = new ArrayList<>();

    // Act
    mockHandler.setAnswersForStubbing(answers);

    // Assert
    verify(invocationContainerImpl).setAnswersForStubbing(answers);
  }
}