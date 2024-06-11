import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockSettings;

public class MockitoCoreTest {

  private MockitoCore mockitoCore;
  private Reporter reporter;
  private MockUtil mockUtil;
  private MockingProgress mockingProgress;
  
  @Before
  public void setUp() {
    mockitoCore = new MockitoCore();
    reporter = mock(Reporter.class);
    mockUtil = mock(MockUtil.class);
    mockingProgress = mock(MockingProgress.class);
  }

  @Test
  public void testMock_withValidParameters() {
    Class<?> classToMock = Object.class;
    MockSettings mockSettings = mock(MockSettings.class);

    when(mockUtil.createMock(classToMock, mockitoCore.mockUtil.class.cast(mockSettings))).thenReturn(new Object());

    assertNotNull(mockitoCore.mock(classToMock, mockSettings));

    verify(mockUtil).createMock(classToMock, mockitoCore.mockUtil.class.cast(mockSettings));
    verify(mockingProgress).mockingStarted(any(), any(Class.class), any(MockSettings.class));
  }

  @Test
  public void testStub_withOngoingStubbingNotNull() {
    IOngoingStubbing ioStubbing = mock(IOngoingStubbing.class);
    when(mockingProgress.pullOngoingStubbing()).thenReturn(ioStubbing);

    assertNotNull(mockitoCore.stub());

    verify(mockingProgress).pullOngoingStubbing();
  }

  @Test
  public void testStub_withOngoingStubbingNull() {
    IOngoingStubbing ioStubbing = null;
    when(mockingProgress.pullOngoingStubbing()).thenReturn(ioStubbing);

    assertNull(mockitoCore.stub());

    verify(mockingProgress).pullOngoingStubbing();
    verify(mockingProgress).reset();
    verify(reporter).missingMethodInvocation();
  }

  @Test
  public void testStub_withMethodCall() {
    mockingProgress.stubbingStarted();

    assertNull(mockitoCore.stub(new Object()));

    verify(mockingProgress).stubbingStarted();
    verify(mockingProgress).pullOngoingStubbing();
  }

  @Test
  public void testWhen_withMethodCall() {
    mockingProgress.stubbingStarted();

    assertNull(mockitoCore.when(new Object()));

    verify(mockingProgress).stubbingStarted();
    verify(mockingProgress).pullOngoingStubbing();
  }

  @Test
  public void testVerify_withValidParameters() {
    Object mock = mock(Object.class);
    VerificationMode mode = mock(VerificationMode.class);

    assertNotNull(mockitoCore.verify(mock, mode));

    verify(reporter, never()).nullPassedToVerify();
    verify(reporter, never()).notAMockPassedToVerify();
    verify(mockingProgress).verificationStarted(mode);
  }

  @Test
  public void testVerify_withNullMock() {
    Object mock = null;
    VerificationMode mode = mock(VerificationMode.class);

    assertNotNull(mockitoCore.verify(mock, mode));

    verify(reporter).nullPassedToVerify();
    verify(reporter, never()).notAMockPassedToVerify();
    verify(mockingProgress).verificationStarted(mode);
  }

  @Test
  public void testVerify_withNotAMock() {
    Object mock = new Object();
    VerificationMode mode = mock(VerificationMode.class);

    assertNotNull(mockitoCore.verify(mock, mode));

    verify(reporter, never()).nullPassedToVerify();
    verify(reporter).notAMockPassedToVerify();
    verify(mockingProgress).verificationStarted(mode);
  }

  @Test
  public void testReset_withValidMocks() {
    Object[] mocks = {mock(Object.class), mock(Object.class), mock(Object.class)};

    mockitoCore.reset(mocks);

    verify(mockingProgress).validateState();
    verify(mockingProgress).reset();
    verify(mockingProgress).resetOngoingStubbing();

    for (Object mock : mocks) {
      verify(mockitoCore.mockUtil).resetMock(mock);
    }
  }

  @Test
  public void testReset_withNullMocks() {
    Object[] mocks = null;

    mockitoCore.reset(mocks);

    verify(mockingProgress).validateState();
    verify(mockingProgress).reset();
    verify(mockingProgress).resetOngoingStubbing();

    verify(mockitoCore.mockUtil, never()).resetMock(any());
  }

  @Test
  public void testVerifyNoMoreInteractions_withValidMocks() {
    Object[] mocks = {mock(Object.class), mock(Object.class), mock(Object.class)};

    mockitoCore.verifyNoMoreInteractions(mocks);

    verify(mockingProgress).validateState();

    for (Object mock : mocks) {
      verify(mockitoCore.mockUtil).getMockHandler(mock);
    }
  }

  @Test
  public void testVerifyNoMoreInteractions_withNullMocks() {
    Object[] mocks = null;

    mockitoCore.verifyNoMoreInteractions(mocks);

    verify(mockingProgress).validateState();

    verify(mockitoCore.mockUtil, never()).getMockHandler(any());
  }

  @Test
  public void testVerifyNoMoreInteractionsInOrder_withValidMocks() {
    List<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class), mock(Object.class));
    InOrderContext inOrderContext = mock(InOrderContext.class);
    AllInvocationsFinder finder = mock(AllInvocationsFinder.class);
    VerificationDataInOrderImpl data = mock(VerificationDataInOrderImpl.class);

    when(mockitoCore.finder.find(mocks)).thenReturn(finder);
    when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(data);
    when(mockitoCore.VerificationModeFactory.noMoreInteractions().verifyInOrder(data)).thenReturn(data);

    mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

    verify(mockingProgress).validateState();
    verify(mockitoCore).finder.find(mocks);
    verify(mockitoCore.mockingProgress).pullOngoingStubbing();
    verify(mockitoCore.VerificationModeFactory.noMoreInteractions()).verifyInOrder(data);
  }

  @Test
  public void testVerifyNoMoreInteractionsInOrder_withEmptyMocks() {
    List<Object> mocks = Arrays.asList();
    InOrderContext inOrderContext = mock(InOrderContext.class);

    mockitoCore.verifyNoMoreInteractionsInOrder(mocks, inOrderContext);

    verify(mockingProgress).validateState();

    verify(mockitoCore, never()).finder.find(any());
    verify(mockitoCore.mockingProgress, never()).pullOngoingStubbing();
    verify(mockitoCore.VerificationModeFactory.noMoreInteractions(), never()).verifyInOrder(any());
  }

  @Test
  public void testInOrder_withValidMocks() {
    Object[] mocks = {mock(Object.class), mock(Object.class), mock(Object.class)};
    InOrderImpl inOrder = mock(InOrderImpl.class);

    when(mockitoCore.mockUtil.isMock(mocks[0])).thenReturn(true);
    when(mockitoCore.mockUtil.isMock(mocks[1])).thenReturn(true);
    when(mockitoCore.mockUtil.isMock(mocks[2])).thenReturn(true);
    when(mockitoCore.reporter.mocksHaveToBePassedWhenCreatingInOrder()).thenReturn(factoryMock);

    assertNotNull(mockitoCore.inOrder(mocks));

    verify(reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();

    for (Object mock : mocks) {
      if (mock != null) {
        verify(mockitoCore.mockUtil).isMock(mock);
      }
    }
  }

  @Test
  public void testInOrder_withNullMocks() {
    Object[] mocks = null;

    mockitoCore.inOrder(mocks);

    verify(mockitoCore.reporter).mocksHaveToBePassedWhenCreatingInOrder();

    verify(mockitoCore.mockUtil, never()).isMock(any());
  }

  @Test
  public void testInOrder_withInvalidMocks() {
    Object mock1 = mock(Object.class);
    Object mock2 = new Object();
    Object mock3 = mock(Object.class);
    Object[] mocks = {mock1, mock2, mock3};

    when(mockitoCore.mockUtil.isMock(mock1)).thenReturn(true);
    when(mockitoCore.mockUtil.isMock(mock2)).thenReturn(false);
    when(mockitoCore.mockUtil.isMock(mock3)).thenReturn(true);
    when(mockitoCore.reporter.notAMockPassedWhenCreatingInOrder()).thenReturn(factoryMock);

    mockitoCore.inOrder(mocks);

    verify(mockitoCore.reporter, never()).mocksHaveToBePassedWhenCreatingInOrder();
    verify(mockitoCore.reporter).notAMockPassedWhenCreatingInOrder();

    verify(mockitoCore.mockUtil).isMock(mock1);
    verify(mockitoCore.mockUtil).isMock(mock2);
    verify(mockitoCore.mockUtil).isMock(mock3);
  }

  @Test
  public void testDoAnswer_withAnswer() {
    Answer answer = mock(Answer.class);

    mockitoCore.doAnswer(answer);

    verify(mockitoCore.mockingProgress).stubbingStarted();
    verify(mockitoCore.mockingProgress).resetOngoingStubbing();
    verify(mockitoCore.StubberImpl().doAnswer(answer));
  }

  @Test
  public void testStubVoid_withValidMock() {
    Object mock = mock(Object.class);
    MockHandlerInterface handler = mock(MockHandlerInterface.class);

    when(mockitoCore.mockUtil.getMockHandler(mock)).thenReturn(handler);
    when(mockitoCore.mockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);

    assertNotNull(mockitoCore.stubVoid(mock));

    verify(mockitoCore.mockingProgress).stubbingStarted();
    verify(mockitoCore.mockingProgress).pullOngoingStubbing();
    verify(mockitoCore.mockUtil).getMockHandler(mock);
    verify(mockitoCore.voidMethodStubbable(mock));
  }

  @Test
  public void testStubVoid_withNullMock() {
    Object mock = null;

    mockitoCore.stubVoid(mock);

    verify(mockitoCore.mockingProgress).stubbingStarted();
    verify(mockitoCore.mockingProgress).pullOngoingStubbing();

    verify(mockitoCore.mockUtil, never()).getMockHandler(any());
    verify(mockitoCore.voidMethodStubbable(any()), never());
  }

  @Test
  public void testValidateMockitoUsage() {
    mockitoCore.validateMockitoUsage();

    verify(mockingProgress).validateState();
  }

  @Test
  public void testGetLastInvocation() {
    OngoingStubbingImpl ongoingStubbing = mock(OngoingStubbingImpl.class);
    List<Invocation> allInvocations = mock(List.class);
    Invocation invocation = mock(Invocation.class);

    when(mockitoCore.MockingProgress.pullOngoingStubbing()).thenReturn(ongoingStubbing);
    when(ongoingStubbing.getRegisteredInvocations()).thenReturn(allInvocations);
    when(allInvocations.get(anyInt())).thenReturn(invocation);

    assertNotNull(mockitoCore.getLastInvocation());

    verify(mockitoCore.MockingProgress).pullOngoingStubbing();
    verify(ongoingStubbing).getRegisteredInvocations();
    verify(allInvocations).get(eq(allInvocations.size()-1));
  }
}