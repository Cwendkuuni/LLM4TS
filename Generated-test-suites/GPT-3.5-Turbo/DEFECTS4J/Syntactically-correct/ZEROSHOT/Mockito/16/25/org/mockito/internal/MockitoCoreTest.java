import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.misusing.NotAMockException;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.progress.IOngoingStubbing;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubberImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.verification.api.VerificationMode;
import org.mockito.stubbing.*;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {
  
  @Mock
  private ClassToMock classToMock;
  
  private MockSettings mockSettings;
  
  private MockitoCore mockitoCore;
  
  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    mockSettings = Mockito.mock(MockSettings.class);
    mockitoCore = new MockitoCore();
  }
  
  @Test
  public void testMock_withClassToMockAndMockSettings_shouldReturnMockObject() {
    // Arrange
    
    // Act
    ClassToMock result = mockitoCore.mock(classToMock.getClass(), mockSettings);
    
    // Assert
    assertNotNull(result);
    assertTrue(result instanceof ClassToMock);
  }
  
  @Test
  public void testStub_shouldReturnOngoingStubbingImpl() {
    // Arrange
    mockitoCore.when(classToMock.methodCall()).thenReturn(null);
    
    // Act
    IOngoingStubbing result = mockitoCore.stub();
    
    // Assert
    assertNotNull(result);
    assertTrue(result instanceof OngoingStubbingImpl);
  }
  
  @Test
  public void testStub_withMethodCall_shouldReturnDeprecatedOngoingStubbing() {
    // Arrange
    mockitoCore.when(classToMock.methodCall()).thenReturn(null);
    
    // Act
    DeprecatedOngoingStubbing result = mockitoCore.stub(classToMock.methodCall());
    
    // Assert
    assertNotNull(result);
    assertTrue(result instanceof DeprecatedOngoingStubbing);
  }
  
  @Test
  public void testWhen_shouldReturnOngoingStubbingImpl() {
    // Arrange
    mockitoCore.when(classToMock.methodCall()).thenReturn(null);
    
    // Act
    OngoingStubbing result = mockitoCore.when(classToMock.methodCall());
    
    // Assert
    assertNotNull(result);
    assertTrue(result instanceof OngoingStubbingImpl);
  }
  
  @Test
  public void testVerify_withMockAndVerificationMode_shouldReturnSameMock() {
    // Arrange
    ClassToMock mock = Mockito.mock(ClassToMock.class);
    VerificationMode mode = Mockito.mock(VerificationMode.class);
    
    // Act
    ClassToMock result = mockitoCore.verify(mock, mode);
    
    // Assert
    assertEquals(mock, result);
  }
  
  @Test
  public void testReset_withMocks_shouldResetMocks() {
    // Arrange
    ClassToMock mock1 = Mockito.mock(ClassToMock.class);
    ClassToMock mock2 = Mockito.mock(ClassToMock.class);
    
    // Act
    mockitoCore.reset(mock1, mock2);
    
    // Assert
    verify(mock1).resetMock();
    verify(mock2).resetMock();
  }
  
  @Test
  public void testVerifyNoMoreInteractions_withMocks_shouldCallVerifyNoMoreInteractions() {
    // Arrange
    ClassToMock mock1 = Mockito.mock(ClassToMock.class);
    ClassToMock mock2 = Mockito.mock(ClassToMock.class);
    Object[] mocks = {mock1, mock2};
    
    // Act
    mockitoCore.verifyNoMoreInteractions(mocks);
    
    // Assert
    verify(mock1).verifyNoMoreInteractions();
    verify(mock2).verifyNoMoreInteractions();
  }
  
  @Test
  public void testInOrder_withMocks_shouldReturnInOrderImpl() {
    // Arrange
    ClassToMock mock1 = Mockito.mock(ClassToMock.class);
    ClassToMock mock2 = Mockito.mock(ClassToMock.class);
    Object[] mocks = {mock1, mock2};
    
    // Act
    InOrder result = mockitoCore.inOrder(mocks);
    
    // Assert
    assertNotNull(result);
    assertTrue(result instanceof InOrderImpl);
  }
  
  @Test
  public void testDoAnswer_shouldReturnStubberImpl() {
    // Arrange
    Answer answer = Mockito.mock(Answer.class);
    
    // Act
    Stubber result = mockitoCore.doAnswer(answer);
    
    // Assert
    assertNotNull(result);
    assertTrue(result instanceof StubberImpl);
  }
  
  @Test
  public void testStubVoid_shouldReturnVoidMethodStubbable() {
    // Arrange
    ClassToMock mock = Mockito.mock(ClassToMock.class);
    
    // Act
    VoidMethodStubbable result = mockitoCore.stubVoid(mock);
    
    // Assert
    assertNotNull(result);
    assertTrue(result instanceof VoidMethodStubbable);
  }
  
  @Test
  public void testValidateMockitoUsage_shouldNotThrowException() {
    // Arrange
    
    // Act
    mockitoCore.validateMockitoUsage();
    
    // Assert
    // No exception thrown
  }
  
  @Test
  public void testGetLastInvocation_shouldReturnLastInvocation() {
    // Arrange
    OngoingStubbingImpl ongoingStubbing = Mockito.mock(OngoingStubbingImpl.class);
    List<Invocation> invocations = Arrays.asList(Mockito.mock(Invocation.class));
    when(ongoingStubbing.getRegisteredInvocations()).thenReturn(invocations);
    ThreadSafeMockingProgress mockingProgress = new ThreadSafeMockingProgress();
    mockingProgress.setOngoingStubbing(ongoingStubbing);
    MockitoCore mockitoCore = new MockitoCore();
    mockitoCore.setMockingProgress(mockingProgress);
    
    // Act
    Invocation result = mockitoCore.getLastInvocation();
    
    // Assert
    assertEquals(invocations.get(invocations.size()-1), result);
  }
}