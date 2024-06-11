import static org.mockito.Mockito.*;

import org.junit.Test;

public class MockitoCoreTest {

  // Test case for mock method
  @Test
  public void testMock() {
    MockitoCore mockitoCore = new MockitoCore();
    ClassToMock classToMock = mockitoCore.mock(ClassToMock.class, null);

    // Typical use case
    assertNotNull(classToMock);

    // Edge case
    assertNotEquals(classToMock.getClass(), ClassToMock.class);

    // Error scenario
    assertNull(classToMock.getInvalidMethod());
  }

  // Test case for stub() method
  @Test
  public void testStub() {
    MockitoCore mockitoCore = new MockitoCore();
    IOngoingStubbing stubbing = mockitoCore.stub();

    // Test stubbingStarted() method
    assertTrue(mockitoCore.mockingProgress.isStubbingInProgress());

    // Test missingMethodInvocation() method
    assertNotNull(stubbing);

    // Test reset() method
    mockitoCore.reset();

    assertFalse(mockitoCore.mockingProgress.isStubbingInProgress());
  }

  // Test case for stub(T methodCall) method
  @Test
  public void testStubMethodCall() {
    MockitoCore mockitoCore = new MockitoCore();
    DeprecatedOngoingStubbing stubbing = mockitoCore.stub("methodCall");

    // Test stubbingStarted() method
    assertTrue(mockitoCore.mockingProgress.isStubbingInProgress());

    // Test missingMethodInvocation() method
    assertNotNull(stubbing);

    // Test reset() method
    mockitoCore.reset();

    assertFalse(mockitoCore.mockingProgress.isStubbingInProgress());
  }

  // Test case for when() method
  @Test
  public void testWhen() {
    MockitoCore mockitoCore = new MockitoCore();
    OngoingStubbing stubbing = mockitoCore.when("methodCall");

    // Test stubbingStarted() method
    assertTrue(mockitoCore.mockingProgress.isStubbingInProgress());

    // Test missingMethodInvocation() method
    assertNotNull(stubbing);

    // Test reset() method
    mockitoCore.reset();

    assertFalse(mockitoCore.mockingProgress.isStubbingInProgress());
  }

  // Test case for verify(T mock, VerificationMode mode) method
  @Test
  public void testVerify() {
    MockitoCore mockitoCore = new MockitoCore();
    ClassToMock classToMock = mockitoCore.mock(ClassToMock.class, null);

    // Test nullPassedToVerify() method
    mockitoCore.verify(null, VerificationModeFactory.times(1));

    // Test notAMockPassedToVerify() method
    String notAMock = "notAMock";
    mockitoCore.verify(notAMock, VerificationModeFactory.times(1));

    // Test verificationStarted() method
    assertEquals(classToMock, mockitoCore.verify(classToMock, VerificationModeFactory.times(1)));

    // Test reset() method
    mockitoCore.reset(classToMock);

    verify(classToMock, VerificationModeFactory.times(0));
  }

  // Test case for reset(T ... mocks) method
  @Test
  public void testReset() {
    MockitoCore mockitoCore = new MockitoCore();
    ClassToMock classToMock = mockitoCore.mock(ClassToMock.class, null);
    ClassToMock anotherClassToMock = mockitoCore.mock(ClassToMock.class, null);

    // Test validateState() method
    mockitoCore.validateMockitoUsage();
    
    // Test reset() method
    mockitoCore.reset(classToMock, anotherClassToMock);

    verify(classToMock, VerificationModeFactory.times(0));
    verify(anotherClassToMock, VerificationModeFactory.times(0));
  }

  // Test case for verifyNoMoreInteractions(Object... mocks) method
  @Test
  public void testVerifyNoMoreInteractions() {
    MockitoCore mockitoCore = new MockitoCore();
    ClassToMock classToMock = mockitoCore.mock(ClassToMock.class, null);
    ClassToMock anotherClassToMock = mockitoCore.mock(ClassToMock.class, null);

    // Test nullPassedToVerifyNoMoreInteractions() method
    mockitoCore.verifyNoMoreInteractions(null);

    // Test notAMockPassedToVerifyNoMoreInteractions() method
    String notAMock = "notAMock";
    mockitoCore.verifyNoMoreInteractions(notAMock);

    // Test validateState() method
    mockitoCore.validateMockitoUsage();

    // Test getMockHandler() method
    mockitoCore.verifyNoMoreInteractions(classToMock);

    // Test mocksHaveToBePassedToVerifyNoMoreInteractions() method
    mockitoCore.verifyNoMoreInteractions();

    verify(classToMock, VerificationModeFactory.times(0));
    verify(anotherClassToMock, VerificationModeFactory.times(0));
  }

  // Test case for verifyNoMoreInteractionsInOrder(List<Object> mocks, InOrderContext inOrderContext) method
  @Test
  public void testVerifyNoMoreInteractionsInOrder() {
    MockitoCore mockitoCore = new MockitoCore();
    ClassToMock classToMock = mockitoCore.mock(ClassToMock.class, null);
    ClassToMock anotherClassToMock = mockitoCore.mock(ClassToMock.class, null);

    // Test validateState() method
    mockitoCore.validateMockitoUsage();

    // Test find() method
    mockitoCore.verifyNoMoreInteractionsInOrder(Arrays.asList(classToMock, anotherClassToMock), null);

    verify(classToMock, VerificationModeFactory.times(0));
    verify(anotherClassToMock, VerificationModeFactory.times(0));
  }

  // Test case for assertMocksNotEmpty(Object[] mocks) method
  @Test
  public void testAssertMocksNotEmpty() {
    MockitoCore mockitoCore = new MockitoCore();

    // Test mocksHaveToBePassedWhenCreatingInOrder() method
    mockitoCore.assertMocksNotEmpty(null);

    // Test mocksHaveToBePassedWhenCreatingInOrder() method
    Object[] mocks = new Object[0];
    mockitoCore.assertMocksNotEmpty(mocks);

    // Test mockHandler.getInvocationContainer().getInvocations() method
    ClassToMock classToMock = mockitoCore.mock(ClassToMock.class, null);
    mockitoCore.assertMocksNotEmpty(new Object[]{classToMock});

    verify(classToMock, VerificationModeFactory.times(0));
  }

  // Test case for inOrder(Object... mocks) method
  @Test
  public void testInOrder() {
    MockitoCore mockitoCore = new MockitoCore();
    ClassToMock classToMock = mockitoCore.mock(ClassToMock.class, null);
    ClassToMock anotherClassToMock = mockitoCore.mock(ClassToMock.class, null);

    // Test mocksHaveToBePassedWhenCreatingInOrder() method
    mockitoCore.inOrder(null);

    // Test nullPassedWhenCreatingInOrder() method
    mockitoCore.inOrder(classToMock, null, anotherClassToMock);

    // Test notAMockPassedWhenCreatingInOrder() method
    Integer notAMock = 123;
    mockitoCore.inOrder(notAMock);

    verify(classToMock, VerificationModeFactory.times(0));
    verify(anotherClassToMock, VerificationModeFactory.times(0));
  }

  // Test case for doAnswer(Answer answer) method
  @Test
  public void testDoAnswer() {
    MockitoCore mockitoCore = new MockitoCore();

    // Test stubbingStarted() method
    mockitoCore.doAnswer(null);
    
    // Test resetOngoingStubbing() method
    assertFalse(mockitoCore.mockingProgress.isOngoingStubbing());

    verify(classToMock, VerificationModeFactory.times(0));
  }

  // Test case for stubVoid(T mock) method
  @Test
  public void testStubVoid() {
    MockitoCore mockitoCore = new MockitoCore();
    ClassToMock classToMock = mockitoCore.mock(ClassToMock.class, null);

    // Test getMockHandler() method
    mockitoCore.stubVoid(classToMock);

    // Test stubbingStarted() method
    assertTrue(mockitoCore.mockingProgress.isStubbingInProgress());

    verify(classToMock, VerificationModeFactory.times(0));
  }

  // Test case for validateMockitoUsage() method
  @Test
  public void testValidateMockitoUsage() {
    MockitoCore mockitoCore = new MockitoCore();

    // Test validateState() method
    mockitoCore.validateMockitoUsage();

    verify(classToMock, VerificationModeFactory.times(0));
  }

  // Test case for getLastInvocation() method
  @Test
  public void testGetLastInvocation() {
    MockitoCore mockitoCore = new MockitoCore();

    // Test pullOngoingStubbing() method
    Invocation lastInvocation = mockitoCore.getLastInvocation();

    assertNotNull(lastInvocation);
  }

}