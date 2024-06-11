import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.stubbing.Address;
import org.mockito.internal.stubbing.Invocation;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.verification.MockAwareVerificationMode;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

  private MockHandler<Address> mockHandler;
  private MockSettingsImpl mockSettings;
  private Invocation invocation;

  @Before
  public void setup() {
    mockSettings = mock(MockSettingsImpl.class);
    mockHandler = new MockHandler<>(mockSettings);
    invocation = mock(Invocation.class);
  }

  @Test
  public void testHandle_WithInvocationsAndVerificationMode() throws Throwable {
    // Arrange
    mockHandler.getInvocationContainer().setAnswersForStubbing(List.of(mock(Answer.class)));
    when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn(new Address());

    // Act
    Object result = mockHandler.handle(invocation);

    // Assert
    verify(mockHandler.getInvocationContainer()).setInvocationForPotentialStubbing(invocation);
    verify(mockHandler.getMockSettings().getDefaultAnswer()).answer(invocation);
    assertNotNull(result);
  }

  @Test
  public void testHandle_WithInvocationsAndNullVerificationMode() throws Throwable {
    // Arrange
    mockHandler.getInvocationContainer().setAnswersForStubbing(List.of(mock(Answer.class)));
    when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn(null);

    // Act
    Object result = mockHandler.handle(invocation);

    // Assert
    verify(mockHandler.getInvocationContainer()).setInvocationForPotentialStubbing(invocation);
    verify(mockHandler.getMockSettings().getDefaultAnswer()).answer(invocation);
    assertNull(result);
  }

  @Test
  public void testHandle_WithoutInvocationsAndVerificationMode() throws Throwable {
    // Arrange
    when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn(new Address());

    // Act
    Object result = mockHandler.handle(invocation);

    // Assert
    verify(mockHandler.getMockSettings().getDefaultAnswer()).answer(invocation);
    verifyNoMoreInteractions(mockHandler.getInvocationContainer());
    assertNotNull(result);
  }

  @Test
  public void testHandle_WithoutInvocationsAndNullVerificationMode() throws Throwable {
    // Arrange
    when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn(null);

    // Act
    Object result = mockHandler.handle(invocation);

    // Assert
    verify(mockHandler.getMockSettings().getDefaultAnswer()).answer(invocation);
    verifyNoMoreInteractions(mockHandler.getInvocationContainer());
    assertNull(result);
  }

  @Test
  public void testHandle_WithAnswersForStubbing() throws Throwable {
    // Arrange
    List<Answer> answers = List.of(mock(Answer.class));
    mockHandler.setAnswersForStubbing(answers);
    StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
    when(mockHandler.getInvocationContainer().findAnswerFor(invocation)).thenReturn(stubbedInvocation);
    when(stubbedInvocation.answer(invocation)).thenReturn(new Address());

    // Act
    Object result = mockHandler.handle(invocation);

    // Assert
    verify(mockHandler.getInvocationContainer()).findAnswerFor(invocation);
    verify(stubbedInvocation).captureArgumentsFrom(invocation);
    verify(stubbedInvocation).answer(invocation);
    assertNotNull(result);
  }

  @Test
  public void testHandle_WithoutAnswersForStubbing() throws Throwable {
    // Arrange
    List<Answer> answers = List.of();
    mockHandler.setAnswersForStubbing(answers);
    when(mockHandler.getMockSettings().getDefaultAnswer().answer(invocation)).thenReturn(new Address());

    // Act
    Object result = mockHandler.handle(invocation);

    // Assert
    verify(mockHandler.getInvocationContainer()).findAnswerFor(invocation);
    verify(mockHandler.getMockSettings().getDefaultAnswer()).answer(invocation);
    assertNotNull(result);
  }

  @Test
  public void testVoidMethodStubbable() {
    // Arrange
    Address mockAddress = mock(Address.class);
    OngoingStubbingImpl<Address> ongoingStubbing = mock(OngoingStubbingImpl.class);
    when(mockHandler.voidMethodStubbable(mockAddress)).thenReturn(ongoingStubbing);

    // Act
    VoidMethodStubbable<Address> result = mockHandler.voidMethodStubbable(mockAddress);

    // Assert
    verify(mockHandler).voidMethodStubbable(mockAddress);
    assertEquals(ongoingStubbing, result);
  }

  @Test
  public void testGetMockSettings() {
    // Act
    MockSettingsImpl result = mockHandler.getMockSettings();

    // Assert
    verify(mockSettings, times(2)).getDefaultAnswer();
    assertEquals(mockSettings, result);
  }

  @Test
  public void testSetAnswersForStubbing() {
    // Arrange
    List<Answer> answers = List.of(mock(Answer.class));

    // Act
    mockHandler.setAnswersForStubbing(answers);

    // Assert
    verify(mockHandler.getInvocationContainer()).setAnswersForStubbing(answers);
  }

  @Test
  public void testGetInvocationContainer() {
    // Act
    InvocationContainer result = mockHandler.getInvocationContainer();

    // Assert
    verify(mockHandler).getInvocationContainer();
    assertEquals(mockHandler.invocationContainerImpl, result);
  }

  @Test
  public void testMockHandlerConstructor() {
    // Act
    MockHandler<Address> result = new MockHandler<Address>();

    // Assert
    assertNotNull(result);
  }

  @Test
  public void testMockHandlerConstructor_WithParameters() {
    // Act
    MockHandler<Address> result = new MockHandler<>(mockSettings);

    // Assert
    verify(mockSettings, times(2)).getDefaultAnswer();
    assertNotNull(result);
  }

  @Test
  public void testMockHandlerConstructor_WithOldMockHandler() {
    // Arrange
    MockHandlerInterface<Address> oldMockHandler = mock(MockHandlerInterface.class);

    // Act
    MockHandler<Address> result = new MockHandler<>(oldMockHandler);

    // Assert
    verify(oldMockHandler).getMockSettings();
    assertNotNull(result);
  }
}