package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

public class ReturnsDeepStubsTest {
  @Test
  public void testAnswer() throws Throwable {
    ReturnsEmptyValues delegate = new ReturnsEmptyValues();

    InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
    GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    when(invocationOnMock.getMock()).thenReturn(null);
    when(returnTypeGenericMetadata.rawType()).thenReturn(null);
    when(returnTypeGenericMetadata.resolveGenericReturnType(null)).thenReturn(null);
    when(invocationOnMock.getMethod()).thenReturn(null);

    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    Object result = returnsDeepStubs.answer(invocationOnMock);
    assertNull(result);
  }

  @Test
  public void testGetMock() throws Throwable {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

    InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
    InternalMockHandler<Object> internalMockHandler = mock(InternalMockHandler.class);
    MockUtil mockUtil = mock(MockUtil.class);
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    when(invocationOnMock.getMock()).thenReturn(null);
    when(mockUtil.getMockHandler(null)).thenReturn(internalMockHandler);
    when(internalMockHandler.getInvocationContainer()).thenReturn(container);

    Object result = returnsDeepStubs.getMock(invocationOnMock);
    assertNull(result);
  }

  @Test
  public void testRecordDeepStubMock() throws Throwable {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

    InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    when(invocationOnMock.getMethod()).thenReturn(null);
    when(invocationOnMock.getMethod().getReturnType()).thenReturn(null);
    when(container.addAnswer(null, false)).thenReturn(null);

    Object result = returnsDeepStubs.recordDeepStubMock(invocationOnMock, container);
    assertNull(result);
  }

  @Test
  public void testActualParameterizedType() throws Throwable {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

    Object mock = null;
    CreationSettings mockSettings = mock(CreationSettings.class);
    MockHandler mockHandler = mock(MockHandler.class);
    when(new MockUtil().getMockHandler(null)).thenReturn(mockHandler);
    when(mockHandler.getMockSettings()).thenReturn(mockSettings);
    when(mockSettings.getTypeToMock()).thenReturn(null);

    GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
    assertNull(result);
  }
}