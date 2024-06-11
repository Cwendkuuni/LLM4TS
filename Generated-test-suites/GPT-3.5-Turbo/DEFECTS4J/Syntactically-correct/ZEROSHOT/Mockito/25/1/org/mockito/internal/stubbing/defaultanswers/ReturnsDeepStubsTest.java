import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

  private ReturnsDeepStubs returnsDeepStubs;
  private InvocationOnMock invocation;

  @Before
  public void setUp() {
    returnsDeepStubs = new ReturnsDeepStubs();
    invocation = mock(InvocationOnMock.class);
  }

  @Test
  public void testAnswer() throws Throwable {
    // Test case 1: when returnTypeGenericMetadata is mockable
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);
    when(returnTypeGenericMetadata.resolveGenericReturnType(invocation.getMethod())).thenReturn(returnTypeGenericMetadata);
    when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
    returnsDeepStubs.answer(invocation);
    
    // Test case 2: when returnTypeGenericMetadata is not mockable
    when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
    returnsDeepStubs.answer(invocation);

    // Test case 3: when returnTypeGenericMetadata matches stubbedInvocationMatcher
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    when(invocation.getMock()).thenReturn(container);
    when(container.getInvocationForStubbing()).thenReturn(invocation);
    StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
    when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));
    when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
    when(stubbedInvocationMatcher.answer(invocation)).thenReturn("stubbedAnswer");
    returnsDeepStubs.answer(invocation);

    // Test case 4: when returnTypeGenericMetadata does not match stubbedInvocationMatcher
    when(container.getInvocationForStubbing()).thenReturn(mock(InvocationOnMock.class));
    returnsDeepStubs.answer(invocation);

    // Test case 5: when creating deep stub mock
    when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
    InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
    when(invocation.getMock()).thenReturn(handler);
    when(handler.getInvocationContainer()).thenReturn(container);
    when(mock(Object.class, returnsDeepStubs)).thenReturn("deepStubMock");
    when(container.addAnswer(any(), anyBoolean())).thenReturn(true);
    returnsDeepStubs.answer(invocation);
  }

  @Test
  public void testGetMock() throws Throwable {
    // Test case 1: when stubbedInvocationMatcher matches invocation
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    when(invocation.getMock()).thenReturn(container);
    when(container.getInvocationForStubbing()).thenReturn(invocation);
    StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
    when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));
    when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
    when(stubbedInvocationMatcher.answer(invocation)).thenReturn("stubbedAnswer");
    returnsDeepStubs.getMock(invocation);

    // Test case 2: when stubbedInvocationMatcher does not match invocation
    when(container.getInvocationForStubbing()).thenReturn(mock(InvocationOnMock.class));
    returnsDeepStubs.getMock(invocation);
  }

  @Test
  public void testRecordDeepStubMock() throws Throwable {
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
    when(invocation.getMock()).thenReturn(container);
    when(mock(Object.class, returnsDeepStubs)).thenReturn("deepStubMock");
    when(container.addAnswer(any(), anyBoolean())).thenReturn(true);
    returnsDeepStubs.recordDeepStubMock(invocation, container);
  }

  @Test
  public void testActualParameterizedType() {
    Object mock = mock(Object.class);
    CreationSettings mockSettings = mock(CreationSettings.class);
    when(mockSettings.getTypeToMock()).thenReturn(mock.getClass());
    InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
    when(handler.getMockSettings()).thenReturn(mockSettings);
    when(mock(handler).getMockHandler(mock)).thenReturn(handler);
    returnsDeepStubs.actualParameterizedType(mock);
  }
}