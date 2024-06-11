import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
  
  @Mock
  private ReturnsDeepStubs returnsDeepStubs;

  @Test
  public void testAnswer() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    assertNotNull(returnsDeepStubs.answer(invocation));
  }

  @Test
  public void testDeepStub() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
    assertNotNull(returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata));
  }

  @Test
  public void testNewDeepStubMock() {
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
    assertNotNull(returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata));
  }

  @Test
  public void testWithSettingsUsing() {
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
    assertNotNull(returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata));
  }

  @Test
  public void testReturnsDeepStubsAnswerUsing() {
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    assertNotNull(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
  }

  @Test
  public void testRecordDeepStubAnswer() throws Throwable {
    Object mock = mock(Object.class);
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    assertNotNull(returnsDeepStubs.recordDeepStubAnswer(mock, container));
  }

  @Test
  public void testActualParameterizedType() {
    Object mock = mock(Object.class);
    CreationSettings mockSettings = mock(CreationSettings.class);
    when(mockHandler.getMockSettings()).thenReturn(mockSettings);
    assertNotNull(returnsDeepStubs.actualParameterizedType(mock));
  }

  @Test
  public void testReturnsDeepStubsSerializationFallback() {
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    assertNotNull(new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata));
  }

  @Test
  public void testDeeplyStubbedAnswer() {
    Object mock = mock(Object.class);
    assertNotNull(new ReturnsDeepStubs.DeeplyStubbedAnswer(mock));
  }

  @Test
  public void testMockitoCore() {
    assertNotNull(ReturnsDeepStubs.mockitoCore());
  }

  @Test
  public void testDelegate() {
    assertNotNull(ReturnsDeepStubs.delegate());
  }
}