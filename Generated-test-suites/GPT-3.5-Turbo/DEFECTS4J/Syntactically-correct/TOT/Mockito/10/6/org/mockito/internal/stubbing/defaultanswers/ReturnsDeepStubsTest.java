import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

  @Test
  public void testAnswer() throws Throwable {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    
    // Test answer method with mockitoCore.isTypeMockable() returning true
    InvocationOnMock invocation1 = mock(InvocationOnMock.class);
    when(invocation1.getMethod()).thenReturn(ReturnsDeepStubs.class.getDeclaredMethod("answer", InvocationOnMock.class));
    GenericMetadataSupport returnTypeGenericMetadata1 = mock(GenericMetadataSupport.class);
    when(returnTypeGenericMetadata1.rawType()).thenReturn(Object.class);
    MockitoCore mockitoCore1 = mock(MockitoCore.class);
    when(mockitoCore1.isTypeMockable(Object.class)).thenReturn(true);
    ReturnsEmptyValues delegate1 = mock(ReturnsEmptyValues.class);
    when(delegate1.returnValueFor(Object.class)).thenReturn(null);
    
    when(returnsDeepStubs.actualParameterizedType(invocation1.getMock())).thenReturn(returnTypeGenericMetadata1);
    doReturn(mockitoCore1).when(returnsDeepStubs).mockitoCore();
    doReturn(delegate1).when(returnsDeepStubs).delegate();
    
    returnsDeepStubs.answer(invocation1);
    
    verify(invocation1, times(1)).getMock();
    verify(invocation1, times(1)).getMethod();
    verify(returnTypeGenericMetadata1, times(1)).rawType();
    verify(mockitoCore1, times(1)).isTypeMockable(Object.class);
    verify(delegate1, times(1)).returnValueFor(Object.class);
    
    // Test answer method with mockitoCore.isTypeMockable() returning false
    InvocationOnMock invocation2 = mock(InvocationOnMock.class);
    when(invocation2.getMethod()).thenReturn(ReturnsDeepStubs.class.getDeclaredMethod("answer", InvocationOnMock.class));
    GenericMetadataSupport returnTypeGenericMetadata2 = mock(GenericMetadataSupport.class);
    when(returnTypeGenericMetadata2.rawType()).thenReturn(Object.class);
    MockitoCore mockitoCore2 = mock(MockitoCore.class);
    when(mockitoCore2.isTypeMockable(Object.class)).thenReturn(false);
    ReturnsEmptyValues delegate2 = mock(ReturnsEmptyValues.class);
    when(delegate2.returnValueFor(Object.class)).thenReturn(null);
    
    when(returnsDeepStubs.actualParameterizedType(invocation2.getMock())).thenReturn(returnTypeGenericMetadata2);
    doReturn(mockitoCore2).when(returnsDeepStubs).mockitoCore();
    doReturn(delegate2).when(returnsDeepStubs).delegate();
    
    returnsDeepStubs.answer(invocation2);
    
    verify(invocation2, times(1)).getMock();
    verify(invocation2, times(1)).getMethod();
    verify(returnTypeGenericMetadata2, times(1)).rawType();
    verify(mockitoCore2, times(1)).isTypeMockable(Object.class);
    verify(delegate2, times(1)).returnValueFor(Object.class);
    
    // Test deepStub method with matching stubbed invocation matcher
    InvocationOnMock invocation3 = mock(InvocationOnMock.class);
    InvocationContainerImpl container3 = mock(InvocationContainerImpl.class);
    when(invocation3.getMock()).thenReturn(container3);
    StubbedInvocationMatcher stubbedInvocationMatcher3 = mock(StubbedInvocationMatcher.class);
    when(stubbedInvocationMatcher3.getInvocation()).thenReturn(invocation3);
    when(container3.getInvocationForStubbing()).thenReturn(invocation3);
    when(container3.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher3));
    
    when(returnsDeepStubs.actualParameterizedType(invocation3.getMock())).thenReturn(returnTypeGenericMetadata1);
    doReturn(mockitoCore1).when(returnsDeepStubs).mockitoCore();
    doReturn(delegate1).when(returnsDeepStubs).delegate();
    
    returnsDeepStubs.answer(invocation3);
    
    verify(invocation3, times(1)).getMock();
    verify(container3, times(1)).getInvocationForStubbing();
    verify(container3, times(1)).getStubbedInvocations();
    verify(stubbedInvocationMatcher3, times(1)).getInvocation();
    
    // Test deepStub method without matching stubbed invocation matcher
    InvocationOnMock invocation4 = mock(InvocationOnMock.class);
    InvocationContainerImpl container4 = mock(InvocationContainerImpl.class);
    when(invocation4.getMock()).thenReturn(container4);
    when(container4.getInvocationForStubbing()).thenReturn(invocation4);
    when(container4.getStubbedInvocations()).thenReturn(new ArrayList<>());
    ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = mock(ReturnsDeepStubsSerializationFallback.class);
    
    when(returnsDeepStubs.actualParameterizedType(invocation4.getMock())).thenReturn(returnTypeGenericMetadata1);
    doReturn(mockitoCore1).when(returnsDeepStubs).mockitoCore();
    doReturn(delegate1).when(returnsDeepStubs).delegate();
    doReturn(returnsDeepStubsSerializationFallback).when(returnsDeepStubs).returnsDeepStubsAnswerUsing(returnTypeGenericMetadata1);
    doReturn(newDeepStubMock()).when(returnsDeepStubs).newDeepStubMock(returnTypeGenericMetadata1);
    doNothing().when(container4).addAnswer(any(DeeplyStubbedAnswer.class), anyBoolean());
    
    returnsDeepStubs.answer(invocation4);
    
    verify(invocation4, times(1)).getMock();
    verify(container4, times(1)).getInvocationForStubbing();
    verify(container4, times(1)).getStubbedInvocations();
    verify(stubbedInvocationMatcher3, times(1)).getInvocation();
    verify(returnsDeepStubs, times(1)).returnsDeepStubsAnswerUsing(returnTypeGenericMetadata1);
    verify(returnsDeepStubs, times(1)).newDeepStubMock(returnTypeGenericMetadata1);
    verify(returnsDeepStubs, times(1)).recordDeepStubAnswer(any(Object.class), any(InvocationContainerImpl.class));
  }
  
  @SuppressWarnings("unchecked")
  private Object newDeepStubMock() {
    return mock(Object.class);
  }
  
}