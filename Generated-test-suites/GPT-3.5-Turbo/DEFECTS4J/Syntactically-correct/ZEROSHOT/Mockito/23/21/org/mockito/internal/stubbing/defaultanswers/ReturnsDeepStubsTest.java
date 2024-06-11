import static org.mockito.Mockito.*;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

  @Test
  public void testAnswer() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    Object result = returnsDeepStubs.answer(invocation);
    assertNotNull(result);
    assertEquals(result.getClass(), Object.class);
  }

  @Test
  public void testGetMock() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

    Answers answers = mock(Answers.class);
    when(invocation.getMock()).thenReturn(answers);

    InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
    when(MockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);

    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    when(handler.getInvocationContainer()).thenReturn(container);

    // StubbedInvocationMatcher setup
    StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
    when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));
    when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
    when(stubbedInvocationMatcher.answer(invocation)).thenReturn("mock answer");

    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

    assertNotNull(result);
    assertEquals(result, "mock answer");
  }

  @Test
  public void testCreateNewDeepStubMock() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

    MockitoCore mockitoCore = mock(MockitoCore.class);
    ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
    whenNew(MockitoCore.class).withNoArguments().thenReturn(mockitoCore);
    whenNew(ReturnsEmptyValues.class).withNoArguments().thenReturn(delegate);

    when(mockitoCore.mock(
      returnTypeGenericMetadata.rawType(),
      withSettingsUsing(returnTypeGenericMetadata)
    )).thenReturn("new mock");

    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

    assertNotNull(result);
    assertEquals(result, "new mock");
  }

  @Test
  public void testWithSettingsUsing() throws Throwable {
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    MockSettings mockSettings = mock(MockSettings.class);
    when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[]{Serializable.class});
    whenNew(MockSettings.class).withNoArguments().thenReturn(mockSettings);

    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

    assertNotNull(result);
    assertEquals(result, mockSettings);
    verify(mockSettings).extraInterfaces(new Class<?>[]{Serializable.class});
    verify(mockSettings).defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
  }

  @Test
  public void testReturnsDeepStubsAnswerUsing() throws Throwable {
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    ReturnsDeepStubs originalReturnsDeepStubs = mock(ReturnsDeepStubs.class);
    whenNew(ReturnsDeepStubs.class).withNoArguments().thenReturn(originalReturnsDeepStubs);

    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

    assertNotNull(result);
    assertEquals(result, originalReturnsDeepStubs);
    verify(originalReturnsDeepStubs).actualParameterizedType(returnTypeGenericMetadata);
  }

  @Test
  public void testRecordDeepStubMock() throws Throwable {
    Object mock = mock(Object.class);
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    Answer<Object> answer = mock(Answer.class);
    when(container.addAnswer(answer, false)).thenReturn(true);

    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    Object result = returnsDeepStubs.recordDeepStubMock(mock, container);

    assertNotNull(result);
    assertEquals(result, mock);
    verify(container).addAnswer(answer, false);
  }

  @Test
  public void testActualParameterizedType() throws Throwable {
    Object mock = mock(Object.class);
    CreationSettings mockSettings = mock(CreationSettings.class);
    MockUtil mockUtil = mock(MockUtil.class);
    whenNew(MockUtil.class).withNoArguments().thenReturn(mockUtil);
    when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
    when(mockHandler.getMockSettings()).thenReturn(mockSettings);
    when(mockSettings.getTypeToMock()).thenReturn("type");

    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

    assertNotNull(result);
    assertEquals(result, GenericMetadataSupport.inferFrom("type"));
  }

}