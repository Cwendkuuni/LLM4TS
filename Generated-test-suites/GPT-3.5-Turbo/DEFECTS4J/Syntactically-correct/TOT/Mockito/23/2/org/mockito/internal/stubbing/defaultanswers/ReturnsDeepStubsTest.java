import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {
  
  private ReturnsDeepStubs returnsDeepStubs;
  
  @Before
  public void setUp() {
    returnsDeepStubs = new ReturnsDeepStubs();
  }
  
  @Test
  public void testAnswer_DeprecatedMethod() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(ReturnsDeepStubs.class.getMethod("answer", InvocationOnMock.class));
    assertNull(returnsDeepStubs.answer(invocation));
  }
  
  @Test
  public void testAnswer_TypeNotMockable() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(ReturnsDeepStubs.class.getMethod("answer", InvocationOnMock.class));
    when(invocation.getMock()).thenReturn("NotMockableType");
    assertNull(returnsDeepStubs.answer(invocation));
  }
  
  @Test
  public void testAnswer_WithMock() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(ReturnsDeepStubs.class.getMethod("answer", InvocationOnMock.class));
    when(invocation.getMock()).thenReturn(mock(Object.class));
    assertNull(returnsDeepStubs.answer(invocation));
  }
  
  @Test
  public void testGetMock_MatchesInvocation() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(ReturnsDeepStubs.class.getMethod("getMock", InvocationOnMock.class, GenericMetadataSupport.class));
    
    InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
    MockUtil mockUtil = mock(MockUtil.class);
    when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(mockHandler);
    whenNew(MockUtil.class).withNoArguments().thenReturn(mockUtil);
    
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    when(mockHandler.getInvocationContainer()).thenReturn(container);
    
    when(container.getInvocationForStubbing().matches(any())).thenReturn(true);
    
    assertNotNull(returnsDeepStubs.getMock(invocation, mock(GenericMetadataSupport.class)));}
  
  @Test
  public void testGetMock_NoMatchedInvocation() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(ReturnsDeepStubs.class.getMethod("getMock", InvocationOnMock.class, GenericMetadataSupport.class));
    
    InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
    MockUtil mockUtil = mock(MockUtil.class);
    when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(mockHandler);
    whenNew(MockUtil.class).withNoArguments().thenReturn(mockUtil);
    
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    when(mockHandler.getInvocationContainer()).thenReturn(container);
    
    when(container.getInvocationForStubbing().matches(any())).thenReturn(false);
    
    assertNotNull(returnsDeepStubs.getMock(invocation, mock(GenericMetadataSupport.class)));
  }
  
  @Test
  public void testCreateNewDeepStubMock_WithGenericMetadata() throws Throwable {
    GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
    when(genericMetadata.rawType()).thenReturn(Object.class);
    when(genericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[] {});
    
    MockitoCore mockitoCore = mock(MockitoCore.class);
    whenNew(MockitoCore.class).withNoArguments().thenReturn(mockitoCore);
    
    Object mock = mock(Object.class);
    when(mockitoCore.mock(Object.class, withSettings().defaultAnswer(any()))).thenReturn(mock);
    
    assertNotNull(returnsDeepStubs.createNewDeepStubMock(genericMetadata));
  }
  
  @Test
  public void testWithSettingsUsing_WithExtraInterfaces() throws Throwable {
    GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
    when(genericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[] {Serializable.class});
    
    MockSettings mockSettings = mock(MockSettings.class);
    whenNew(MockSettings.class).withNoArguments().thenReturn(mockSettings);
    
    Object answer = returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadata);
    when(mockSettings.extraInterfaces(new Class<?>[] {Serializable.class})).thenReturn(mockSettings);
    when(mockSettings.defaultAnswer(answer)).thenReturn(mockSettings);
    
    assertNotNull(returnsDeepStubs.withSettingsUsing(genericMetadata));
  }
  
  @Test
  public void testWithSettingsUsing_NoExtraInterfaces() throws Throwable {
    GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
    when(genericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[] {});
    
    MockSettings mockSettings = mock(MockSettings.class);
    whenNew(MockSettings.class).withNoArguments().thenReturn(mockSettings);
    
    Object answer = returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadata);
    when(mockSettings.defaultAnswer(answer)).thenReturn(mockSettings);
    
    assertNotNull(returnsDeepStubs.withSettingsUsing(genericMetadata));
  }
  
  @Test
  public void testRecordDeepStubMock() throws Throwable {
    Object mock = mock(Object.class);
    
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
    MockUtil mockUtil = mock(MockUtil.class);
    when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
    whenNew(MockUtil.class).withNoArguments().thenReturn(mockUtil);
    when(mockHandler.getInvocationContainer()).thenReturn(container);
    
    assertNotNull(returnsDeepStubs.recordDeepStubMock(mock, container));
  }
  
  @Test
  public void testActualParameterizedType() {
    CreationSettings creationSettings = mock(CreationSettings.class);
    MockHandler mockHandler = mock(MockHandler.class);
    MockUtil mockUtil = mock(MockUtil.class);
    when(mockUtil.getMockHandler(any())).thenReturn(mockHandler);
    when(mockHandler.getMockSettings()).thenReturn(creationSettings);
    
    assertNotNull(returnsDeepStubs.actualParameterizedType(mock(Object.class)));
  }
}