import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

  @Test
  public void testAnswer() throws Throwable {
    ReturnsDeepStubs stub = new ReturnsDeepStubs();

    // Create mock InvocationOnMock
    InvocationOnMock invocation = mock(InvocationOnMock.class);

    // Define expected return value
    Object expected = new Object();

    // Set up mockitoCore() method
    MockitoCore mockitoCore = mock(MockitoCore.class);
    when(stub.mockitoCore()).thenReturn(mockitoCore);

    // Set up actualParameterizedType() method
    CreationSettings mockSettings = mock(CreationSettings.class);
    MockUtil mockUtil = mock(MockUtil.class);
    InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
    when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
    when(handler.getMockSettings()).thenReturn(mockSettings);
    when(mockSettings.getTypeToMock()).thenReturn(Object.class);
    when(stub.actualParameterizedType(invocation.getMock())).thenReturn(new GenericMetadataSupport());

    // Set up delegate() method
    ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
    when(stub.delegate()).thenReturn(delegate);
    when(delegate.returnValueFor(Object.class)).thenReturn(expected);

    // Set up deepStub() method
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
    when(handler.getInvocationContainer()).thenReturn(container);
    when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
    when(container.getInvocationForStubbing()).thenReturn(invocation);
    when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
    when(stubbedInvocationMatcher.answer(invocation)).thenReturn(expected);

    // Set up recordDeepStubAnswer() method
    Object mock = new Object();
    when(stub.newDeepStubMock(any())).thenReturn(mock);
    doNothing().when(container).addAnswer(any(DeeplyStubbedAnswer.class), anyBoolean());

    // Test answer() method
    Object result = stub.answer(invocation);

    // Verify mockitoCore() method
    verify(stub).mockitoCore();

    // Verify actualParameterizedType() method
    verify(stub).actualParameterizedType(invocation.getMock());

    // Verify delegate() method
    verify(stub).delegate();

    // Verify deepStub() method
    verify(stub).deepStub(invocation, new GenericMetadataSupport());

    // Verify recordDeepStubAnswer() method
    verify(stub).recordDeepStubAnswer(mock, container);

    // Verify answer() method result
    assertEquals(expected, result);
  }

  @Test
  public void testNewDeepStubMock() {
    ReturnsDeepStubs stub = new ReturnsDeepStubs();

    // Create mock GenericMetadataSupport
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

    // Set up mockitoCore() method
    MockitoCore mockitoCore = mock(MockitoCore.class);
    when(stub.mockitoCore()).thenReturn(mockitoCore);

    // Set up returnTypeGenericMetadata.rawType() method
    Class<?> rawType = Object.class;
    when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);

    // Set up withSettingsUsing() method
    MockSettings mockSettings = mock(MockSettings.class);
    when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
    when(stub.withSettingsUsing(returnTypeGenericMetadata)).thenReturn(mockSettings);

    // Create mock Object
    Object mock = new Object();
    when(mockitoCore.mock(rawType, mockSettings)).thenReturn(mock);

    // Test newDeepStubMock() method
    Object result = stub.newDeepStubMock(returnTypeGenericMetadata);

    // Verify mockitoCore() method
    verify(stub).mockitoCore();

    // Verify returnTypeGenericMetadata.rawType() method
    verify(returnTypeGenericMetadata).rawType();

    // Verify withSettingsUsing() method
    verify(stub).withSettingsUsing(returnTypeGenericMetadata);

    // Verify mockitoCore.mock() method
    verify(mockitoCore).mock(rawType, mockSettings);

    // Verify newDeepStubMock() method result
    assertEquals(mock, result);
  }

  @Test
  public void testWithSettingsUsing() {
    ReturnsDeepStubs stub = new ReturnsDeepStubs();

    // Create mock GenericMetadataSupport
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

    // Set up returnTypeGenericMetadata.hasRawExtraInterfaces() method
    when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);

    // Set up returnTypeGenericMetadata.rawExtraInterfaces() method
    Class<?>[] rawExtraInterfaces = new Class<?>[0];
    when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(rawExtraInterfaces);

    // Set up withSettings() method
    MockSettings mockSettings = mock(MockSettings.class);
    when(stub.withSettings()).thenReturn(mockSettings);

    // Set up serializable() method
    MockSettings mockSettingsSerializable = mock(MockSettings.class);
    when(mockSettings.serializable()).thenReturn(mockSettingsSerializable);

    // Set up returnsDeepStubsAnswerUsing() method
    ReturnsDeepStubs returnsDeepStubsAnswer = mock(ReturnsDeepStubs.class);
    when(stub.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)).thenReturn(returnsDeepStubsAnswer);

    // Create mock Object
    Object mock = new Object();
    when(mockSettingsSerializable.defaultAnswer(returnsDeepStubsAnswer)).thenReturn(mock);

    // Test withSettingsUsing() method
    Object result = stub.withSettingsUsing(returnTypeGenericMetadata);

    // Verify returnTypeGenericMetadata.hasRawExtraInterfaces() method
    verify(returnTypeGenericMetadata).hasRawExtraInterfaces();

    // Verify returnTypeGenericMetadata.rawExtraInterfaces() method
    verify(returnTypeGenericMetadata).rawExtraInterfaces();

    // Verify withSettings() method
    verify(stub).withSettings();

    // Verify serializable() method
    verify(mockSettings).serializable();

    // Verify returnsDeepStubsAnswerUsing() method
    verify(stub).returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

    // Verify mockSettings.serializable().defaultAnswer() method
    verify(mockSettingsSerializable).defaultAnswer(returnsDeepStubsAnswer);

    // Verify withSettingsUsing() method result
    assertEquals(mock, result);
  }

  @Test
  public void testReturnsDeepStubsAnswerUsing() {
    ReturnsDeepStubs stub = new ReturnsDeepStubs();

    // Create mock GenericMetadataSupport
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

    // Set up ReturnsDeepStubsSerializationFallback() constructor
    ReturnsDeepStubsSerializationFallback fallback = mock(ReturnsDeepStubsSerializationFallback.class);
    when(stub.new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata)).thenReturn(fallback);

    // Test returnsDeepStubsAnswerUsing() method
    Object result = stub.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

    // Verify ReturnsDeepStubsSerializationFallback() constructor
    verify(stub).new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);

    // Verify returnsDeepStubsAnswerUsing() method result
    assertEquals(fallback, result);
  }

  @Test
  public void testRecordDeepStubAnswer() throws Throwable {
    ReturnsDeepStubs stub = new ReturnsDeepStubs();

    // Create mock Object
    Object mock = new Object();

    // Create mock InvocationContainerImpl
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);

    // Set up newDeepStubMock() method
    Object deepStubMock = new Object();
    when(stub.newDeepStubMock(any())).thenReturn(deepStubMock);

    // Set up addAnswer() method
    doNothing().when(container).addAnswer(any(DeeplyStubbedAnswer.class), anyBoolean());

    // Test recordDeepStubAnswer() method
    Object result = stub.recordDeepStubAnswer(mock, container);

    // Verify newDeepStubMock() method
    verify(stub).newDeepStubMock(any());

    // Verify addAnswer() method
    verify(container).addAnswer(any(DeeplyStubbedAnswer.class), anyBoolean());

    // Verify recordDeepStubAnswer() method result
    assertEquals(mock, result);
  }

}