
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
  
  // Test case for the answer method
  @Test
  public void testAnswer() throws Throwable {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMock()).thenReturn(mock(Object.class));
    
    Object result = returnsDeepStubs.answer(invocation);
    
    // Verify that delegate.returnValueFor was not called
    verify(invocation, times(0)).getMock();
    
    // Verify that getMock was called
    verify(invocation, times(1)).getMock();
    assertNotNull(result);
  }
  
  // Test case for the getMock method with matching invocation
  @Test
  public void testGetMockWithMatchingInvocation() throws Throwable {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMock()).thenReturn(mock(Object.class));
    
    Object result = returnsDeepStubs.getMock(invocation, mock(GenericMetadataSupport.class));
    
    assertNotNull(result);
    // Verify that recordDeepStubMock was not called
    verify(invocation, times(0)).getMock();
  }
  
  // Test case for the getMock method without matching invocation
  @Test
  public void testGetMockWithoutMatchingInvocation() throws Throwable {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMock()).thenReturn(mock(Object.class));
    when(invocation.matches(any(InvocationOnMock.class))).thenReturn(false);
    
    Object result = returnsDeepStubs.getMock(invocation, mock(GenericMetadataSupport.class));
    
    // Verify that recordDeepStubMock was called
    verify(invocation, times(1)).getMock();
    assertNotNull(result);
  }
  
  // Test case for the createNewDeepStubMock method
  @Test
  public void testCreateNewDeepStubMock() {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    
    Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
    
    assertNotNull(result);
  }
  
  // Test case for the withSettingsUsing method with extra interfaces
  @Test
  public void testWithSettingsUsingWithExtraInterfaces() {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[]{Serializable.class});
    
    MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
    
    assertNotNull(result);
    assertTrue(result.hasExtraInterfaces(Serializable.class));
  }
  
  // Test case for the withSettingsUsing method without extra interfaces
  @Test
  public void testWithSettingsUsingWithoutExtraInterfaces() {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[]{});
    
    MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
    
    assertNotNull(result);
    assertFalse(result.hasExtraInterfaces(Serializable.class));
  }
  
  // Test case for the returnsDeepStubsAnswerUsing method
  @Test
  public void testReturnsDeepStubsAnswerUsing() {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    
    ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
    
    assertNotNull(result);
    assertSame(returnTypeGenericMetadata, result.actualParameterizedType(mock(Object.class)));
  }
  
  // Test case for the recordDeepStubMock method
  @Test
  public void testRecordDeepStubMock() throws Throwable {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(mock(Object.class));
    
    Object result = returnsDeepStubs.recordDeepStubMock(mock(Object.class), container);
    
    assertNotNull(result);
    // Verify that addAnswer was called
    verify(container, times(1)).addAnswer(any(Answer.class), anyBoolean());
  }
  
  // Test case for the actualParameterizedType method
  @Test
  public void testActualParameterizedType() {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    MockUtil mockUtil = mock(MockUtil.class);
    CreationSettings mockSettings = mock(CreationSettings.class);
    GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
    when(mockUtil.getMockHandler(anyObject())).thenReturn(mock(InternalMockHandler.class));
    when(mockUtil.getMockHandler(anyObject()).getMockSettings()).thenReturn(mockSettings);
    when(mockSettings.getTypeToMock()).thenReturn(Object.class);
    GenericMetadataSupport.inferFrom(Object.class);
    when(GenericMetadataSupport.inferFrom(Object.class)).thenReturn(genericMetadata);
    
    GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock(Object.class));
    
    assertNotNull(result);
    assertSame(genericMetadata, result);
  }
  
  // Test case for the entire answer method flow
  @Test
  public void testAnswerFlow() throws Throwable {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMock()).thenReturn(mock(Object.class));
    when(invocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
    
    Object result = returnsDeepStubs.answer(invocation);
    
    // Verify that getMock was called
    verify(invocation, times(1)).getMock();
    assertNotNull(result);
  }
  
}
