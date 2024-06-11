package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.*;

import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

import org.junit.Assert;
import org.junit.Test;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsEmptyValues delegate = new ReturnsEmptyValues();
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);

        when(delegate.returnValueFor(Object.class)).thenReturn("");

        Object result = returnsDeepStubs.answer(invocation);
        Assert.assertEquals("", result);
    }

    @Test
    public void testGetMock() throws Throwable {
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);

        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(mock);

        Object result = returnsDeepStubs.getMock(invocation);
        Assert.assertEquals(mock, result);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mock = mock(Object.class);

        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(mock(Object.class, returnsDeepStubs)).thenReturn(mock);

        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);
        Assert.assertEquals(mock, result);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);

        when(mockUtil.getMockHandler(mockSettings.getTypeToMock())).thenReturn(handler);
        when(handler.getMockSettings()).thenReturn(mockSettings);
        when(genericMetadataSupport.inferFrom(mockSettings.getTypeToMock())).thenReturn(genericMetadataSupport);

        Object result = returnsDeepStubs.actualParameterizedType(mockSettings.getTypeToMock());
        Assert.assertEquals(genericMetadataSupport, result);
    }
}