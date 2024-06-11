import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockSettings;
import org.mockito.Mockito;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MockitoCoreTest {
  
  private MockitoCore mockitoCore;
  
  @Before
  public void setup() {
    mockitoCore = new MockitoCore();
  }
  
  // Test mock() method with typical use case and edge cases
  @Test
  public void testMock() {
    // Typical use case - creating a mock
    List<String> mockedList = mockitoCore.mock(List.class);
    assertNotNull(mockedList);
    
    // Edge cases - null classToMock
    try {
      mockitoCore.mock(null);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Pass
    }
    
    // null mockSettings
    try {
      mockitoCore.mock(List.class, null);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Pass
    }
    
    // mockSettings with null invokes real methods
    MockSettings mockSettings = new MockSettingsImpl().invocations(mockitoCore.mockSettings().invocations(null));
    List<String> mockedList2 = mockitoCore.mock(List.class, mockSettings);
    assertNotNull(mockedList2);
  }
  
  // Test stub() method with typical use case and error scenario
  @Test
  public void testStub() {
    // Typical use case - pullOngoingStubbing not null
    IOngoingStubbing ongoingStubbing = mockitoCore.stub();
    assertNull(ongoingStubbing); // ongoingStubbing is null because there wasn't any stubbing
    
    // Error scenario - missingMethodInvocation
    Reporter reporterMock = mock(Reporter.class);
    MockitoCore mockitoCoreWithMockReporter = new MockitoCore(reporterMock);
    
    mockitoCoreWithMockReporter.stub();
    
    verify(reporterMock).missingMethodInvocation();
  }
  
  // Test when() method with typical use case and error scenario
  @Test
  public void testWhen() {
    // Typical use case - stubbingStarted called
    MockingProgress mockingProgressMock = mock(MockingProgress.class);
    MockitoCore mockitoCoreWithMockProgress = new MockitoCore(mockingProgressMock);
    
    IOngoingStubbing ongoingStubbing = mockitoCoreWithMockProgress.when(anyString());
    
    verify(mockingProgressMock).stubbingStarted();
    
    // Error scenario - missingMethodInvocation
    Reporter reporterMock = mock(Reporter.class);
    MockitoCore mockitoCoreWithMockReporter = new MockitoCore(reporterMock);
    
    mockitoCoreWithMockReporter.when(anyString());
    
    verify(reporterMock).missingMethodInvocation();
  }
  
  // Test verify() method with typical use case and error scenarios
  @Test
  public void testVerify() {
    // Typical use case - verificationStarted called
    MockingProgress mockingProgressMock = mock(MockingProgress.class);
    MockitoCore mockitoCoreWithMockProgress = new MockitoCore(mockingProgressMock);
    
    List<String> mockedList = mock(List.class);
    mockitoCoreWithMockProgress.verify(mockedList, atLeast(2));
    
    verify(mockingProgressMock).verificationStarted(atLeast(2));
    
    // Error scenarios
    Reporter reporterMock = mock(Reporter.class);
    MockitoCore mockitoCoreWithMockReporter = new MockitoCore(reporterMock);
    
    // null mock
    mockitoCoreWithMockReporter.verify(null, atLeast(1));
    verify(reporterMock).nullPassedToVerify();
    
    // not a mock
    String notAMock = "not a mock";
    mockitoCoreWithMockReporter.verify(notAMock, atLeast(1));
    verify(reporterMock).notAMockPassedToVerify();
  }
  
  // Test reset() method with typical use case
  @Test
  public void testReset() {
    // Typical use case - resetMock called for each mock
    MockUtil mockUtilMock = mock(MockUtil.class);
    MockitoCore mockitoCoreWithMockUtil = new MockitoCore(mockUtilMock);
    
    List<String> mockedList1 = mock(List.class);
    List<Integer> mockedList2 = mock(List.class);
    
    mockitoCoreWithMockUtil.reset(mockedList1, mockedList2);
    
    verify(mockedUtilMock).resetMock(mockedList1);
    verify(mockedUtilMock).resetMock(mockedList2);
  }
  
  // Test verifyNoMoreInteractions() method with typical use case and error scenarios
  @Test
  public void testVerifyNoMoreInteractions() {
    // Typical use case - calling verifyNoMoreInteractions on each mock
    MockUtil mockUtilMock = mock(MockUtil.class);
    List<String> mockedList1 = mock(List.class);
    List<Integer> mockedList2 = mock(List.class);
    
    MockitoCore mockitoCoreWithMockUtil = new MockitoCore(mockUtilMock);
    
    mockitoCoreWithMockUtil.verifyNoMoreInteractions(mockedList1, mockedList2);
    
    verify(mockUtilMock).getMockHandler(mockedList1);
    verify(mockUtilMock).getMockHandler(mockedList2);
    
    // Error scenarios
    Reporter reporterMock = mock(Reporter.class);
    MockitoCore mockitoCoreWithMockReporter = new MockitoCore(reporterMock);
    
    // null mock passed
    mockitoCoreWithMockReporter.verifyNoMoreInteractions((List)null);
    verify(reporterMock).nullPassedToVerifyNoMoreInteractions();
    
    // not a mock passed
    String notAMock = "not a mock";
    mockitoCoreWithMockReporter.verifyNoMoreInteractions(notAMock);
    verify(reporterMock).notAMockPassedToVerifyNoMoreInteractions();
  }
  
  // Test inOrder() method with typical use case and error scenarios
  @Test
  public void testInOrder() {
    // Typical use case - calling verifyNoMoreInteractions on each mock
    MockUtil mockUtilMock = mock(MockUtil.class);
    List<String> mockedList1 = mock(List.class);
    List<Integer> mockedList2 = mock(List.class);
    
    MockitoCore mockitoCoreWithMockUtil = new MockitoCore(mockUtilMock);
    
    InOrder inOrder = mockitoCoreWithMockUtil.inOrder(mockedList1, mockedList2);
    assertNotNull(inOrder);
    
    verify(mockUtilMock).isMock(mockedList1);
    verify(mockUtilMock).isMock(mockedList2);
    
    // Error scenarios
    Reporter reporterMock = mock(Reporter.class);
    MockitoCore mockitoCoreWithMockReporter = new MockitoCore(reporterMock);
    
    // null mock passed
    mockitoCoreWithMockReporter.inOrder((List)null);
    verify(reporterMock).mocksHaveToBePassedWhenCreatingInOrder();
    
    // empty mocks array passed
    mockitoCoreWithMockReporter.inOrder();
    verify(reporterMock).mocksHaveToBePassedWhenCreatingInOrder();
    
    // null mock passed
    mockitoCoreWithMockReporter.inOrder(mockedList1, null, mockedList2);
    verify(reporterMock).nullPassedWhenCreatingInOrder();
    
    // not a mock passed
    String notAMock = "not a mock";
    mockitoCoreWithMockReporter.inOrder(mockedList1, notAMock, mockedList2);
    verify(reporterMock).notAMockPassedWhenCreatingInOrder();
  }
  
  // Test doAnswer() method with typical use case
  @Test
  public void testDoAnswer() {
    // Typical use case - stubbingStarted and resetOngoingStubbing called
    MockingProgress mockingProgressMock = mock(MockingProgress.class);
    MockitoCore mockitoCoreWithMockProgress = new MockitoCore(mockingProgressMock);
    
    Answer answer = mock(Answer.class);
    
    Stubber stubber = mockitoCoreWithMockProgress.doAnswer(answer);
    
    verify(mockingProgressMock).stubbingStarted();
    verify(mockingProgressMock).resetOngoingStubbing();
    
    assertNotNull(stubber);
  }
  
  // Test stubVoid() method with typical use case
  @Test
  public void testStubVoid() {
    // Typical use case - stubbingStarted called
    MockUtil mockUtilMock = mock(MockUtil.class);
    MockitoCore mockitoCoreWithMockUtil = new MockitoCore(mockUtilMock);
    
    MockHandlerInterface mockHandlerMock = mock(MockHandlerInterface.class);
    when(mockUtilMock.getMockHandler(any())).thenReturn(mockHandlerMock);
    
    List<String> mockedList = mock(List.class);
    
    VoidMethodStubbable stubbable = mockitoCoreWithMockUtil.stubVoid(mockedList);
    
    verify(mockingProgressMock).stubbingStarted();
    
    assertNotNull(stubbable);
  }
  
  // Test validateMockitoUsage() method
  @Test
  public void testValidateMockitoUsage() {
    // Typical use case - validateState called
    MockingProgress mockingProgressMock = mock(MockingProgress.class);
    MockitoCore mockitoCoreWithMockProgress = new MockitoCore(mockingProgressMock);
    
    mockitoCoreWithMockProgress.validateMockitoUsage();
    
    verify(mockingProgressMock).validateState();
  }
  
  // Test getLastInvocation() method
  @Test
  public void testGetLastInvocation() {
    // Typical use case - getLastInvocation called
    MockingProgress mockingProgressMock = mock(MockingProgress.class);
    OngoingStubbingImpl ongoingStubbingMock = mock(OngoingStubbingImpl.class);
    List<Invocation> registeredInvocationsMock = mock(List.class);
    
    when(mockingProgressMock.pullOngoingStubbing()).thenReturn(ongoingStubbingMock);
    when(ongoingStubbingMock.getRegisteredInvocations()).thenReturn(registeredInvocationsMock);
    when(registeredInvocationsMock.get(anyInt())).thenReturn(mock(Invocation.class));
    
    MockitoCore mockitoCoreWithMockProgress = new MockitoCore(mockingProgressMock);
    
    Invocation lastInvocation = mockitoCoreWithMockProgress.getLastInvocation();
    
    assertNotNull(lastInvocation);
    verify(ongoingStubbingMock).getRegisteredInvocations();
  }
  
}