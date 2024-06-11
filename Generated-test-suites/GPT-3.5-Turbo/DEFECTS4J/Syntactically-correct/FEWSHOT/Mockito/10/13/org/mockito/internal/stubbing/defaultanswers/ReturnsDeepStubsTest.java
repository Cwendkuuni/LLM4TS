package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

  @Test
  public void testAnswer() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    returnsDeepStubs.answer(invocation);

    // Add assertions here
  }

  @Test
  public void testDeepStub() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

    // Add assertions here
  }

  @Test
  public void testNewDeepStubMock() {
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

    // Add assertions here
  }

  @Test
  public void testWithSettingsUsing() {
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

    // Add assertions here
  }

  @Test
  public void testReturnsDeepStubsAnswerUsing() {
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

    // Add assertions here
  }

  @Test
  public void testRecordDeepStubAnswer() throws Throwable {
    Object mock = mock(Object.class);
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    returnsDeepStubs.recordDeepStubAnswer(mock, container);

    // Add assertions here
  }

  @Test
  public void testActualParameterizedType() {
    Object mock = mock(Object.class);
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    returnsDeepStubs.actualParameterizedType(mock);

    // Add assertions here
  }

  @Test
  public void testReturnsDeepStubsSerializationFallback() throws Throwable {
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(
        returnTypeGenericMetadata);
    returnsDeepStubsSerializationFallback.writeReplace();

    // Add assertions here
  }

  @Test
  public void testDeeplyStubbedAnswer() throws Throwable {
    Object mock = mock(Object.class);
    ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = new ReturnsDeepStubs.DeeplyStubbedAnswer(
        mock);
    deeplyStubbedAnswer.answer(mock(InvocationOnMock.class));

    // Add assertions here
  }

}