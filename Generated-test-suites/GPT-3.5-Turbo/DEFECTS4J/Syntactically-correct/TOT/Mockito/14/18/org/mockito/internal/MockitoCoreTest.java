import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {

  private MockitoCore mockitoCore;

  @Before
  public void setUp() {
    mockitoCore = new MockitoCore();
  }

  @Test
  public void testMock() {
    // Typical use case
    MyClass mock = mockitoCore.mock(MyClass.class, new MockSettingsImpl());

    // Edge case
    assertNull("Mock should not be null", mock);

    // Error scenario
    verify(mockitoCore.getReporter()).nullPassedToVerify();
  }

  @Test
  public void testStub() {
    // Typical use case
    IOngoingStubbing stubbing = mockitoCore.stub();

    // Edge case
    assertNull("Stubbing should be null", stubbing);

    // Error scenario
    verify(mockitoCore.getReporter()).missingMethodInvocation();
  }

  @Test
  public void testStubWithMethodCall() {
    // Typical use case
    DeprecatedOngoingStubbing stubbing = mockitoCore.stub(new MyClass());

    // Edge case
    assertNull("Stubbing should be null", stubbing);

    // Error scenario
    verify(mockitoCore.getReporter()).missingMethodInvocation();
  }

  @Test
  public void testWhen() {
    // Typical use case
    OngoingStubbing stubbing = mockitoCore.when(new MyClass());

    // Edge case
    assertNull("Stubbing should be null", stubbing);

    // Error scenario
    verify(mockitoCore.getReporter()).missingMethodInvocation();
  }

  @Test
  public void testVerify() {
    // Typical use case
    MyOtherClass mock = mock(MyOtherClass.class);
    MyOtherClass verifiedMock = mockitoCore.verify(mock, times(1));

    // Edge case
    assertNotNull("Verified mock should not be null", verifiedMock);

    // Error scenario
    verify(mockitoCore.getReporter()).nullPassedToVerify();
    verify(mockitoCore.getReporter()).notAMockPassedToVerify();
  }

  @Test
  public void testReset() {
    // Typical use case
    MyClass mock1 = mock(MyClass.class);
    MyClass mock2 = mock(MyClass.class);
    mockitoCore.reset(mock1, mock2);

    // Error scenario
    verify(mockitoCore.getMockUtil(), never()).resetMock(isA(MyClass.class));
  }

  @Test
  public void testVerifyNoMoreInteractions() {
    // Typical use case
    MyOtherClass mock = mock(MyOtherClass.class);
    mockitoCore.verifyNoMoreInteractions(mock);

    // Error scenario
    verify(mockitoCore.getReporter()).nullPassedToVerifyNoMoreInteractions();
    verify(mockitoCore.getReporter()).notAMockPassedToVerifyNoMoreInteractions();
  }

  @Test
  public void testVerifyNoMoreInteractionsInOrder() {
    // Typical use case
    MyOtherClass mock1 = mock(MyOtherClass.class);
    MyOtherClass mock2 = mock(MyOtherClass.class);
    mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(mock1, mock2), new InOrderContext());

    // Error scenario
    verify(mockitoCore.getReporter()).mocksHaveToBePassedToVerifyNoMoreInteractions();
  }

  @Test
  public void testInOrder() {
    // Typical use case
    MyOtherClass mock1 = mock(MyOtherClass.class);
    MyOtherClass mock2 = mock(MyOtherClass.class);
    InOrder inOrder = mockitoCore.inOrder(mock1, mock2);

    // Edge case
    assertNotNull("InOrder instance should not be null", inOrder);

    // Error scenario
    verify(mockitoCore.getReporter()).mocksHaveToBePassedWhenCreatingInOrder();
    verify(mockitoCore.getReporter()).nullPassedWhenCreatingInOrder();
    verify(mockitoCore.getReporter()).notAMockPassedWhenCreatingInOrder();
  }

  @Test
  public void testDoAnswer() {
    // Typical use case
    Answer answer = mock(Answer.class);
    Stubber stubber = mockitoCore.doAnswer(answer);

    // Edge case
    assertNotNull("Stubber instance should not be null", stubber);

    // Error scenario
    verify(mockitoCore.getMockingProgress()).resetOngoingStubbing();
  }

  @Test
  public void testStubVoid() {
    // Typical use case
    MyClass mock = mock(MyClass.class);
    VoidMethodStubbable<MyClass> stubbable = mockitoCore.stubVoid(mock);

    // Edge case
    assertNotNull("VoidMethodStubbable instance should not be null", stubbable);

    // Error scenario
    verify(mockitoCore.getMockUtil(), never()).getMockHandler(isA(MyClass.class));
  }

  @Test
  public void testValidateMockitoUsage() {
    // Typical use case
    mockitoCore.validateMockitoUsage();

    // Error scenario
    verify(mockitoCore.getMockingProgress()).validateState();
  }

  @Test
  public void testGetLastInvocation() {
    // Typical use case
    Invocation lastInvocation = mockitoCore.getLastInvocation();

    // Error scenario
    verify(mockitoCore.getMockingProgress()).pullOngoingStubbing();
  }
}