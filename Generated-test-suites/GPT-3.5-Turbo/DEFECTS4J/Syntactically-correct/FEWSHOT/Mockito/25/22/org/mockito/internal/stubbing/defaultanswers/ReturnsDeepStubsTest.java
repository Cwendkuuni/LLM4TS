package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
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

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsEmptyValues delegateMock = mock(ReturnsEmptyValues.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.delegate = delegateMock;

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("mockObject");
        when(invocation.getMethod()).thenReturn(String.class.getMethod("trim"));

        GenericMetadataSupport returnTypeGenericMetadataMock = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadataMock.rawType()).thenReturn(String.class);

        MockCreationValidator mockCreationValidatorMock = mock(MockCreationValidator.class);
        when(mockCreationValidatorMock.isTypeMockable(any(Class.class))).thenReturn(true);

        MockUtil mockUtilMock = mock(MockUtil.class);
        InternalMockHandler<Object> handlerMock = mock(InternalMockHandler.class);
        InvocationContainerImpl containerMock = mock(InvocationContainerImpl.class);

        when(mockUtilMock.getMockHandler(any())).thenReturn(handlerMock);
        when(handlerMock.getInvocationContainer()).thenReturn(containerMock);

        StubbedInvocationMatcher stubbedInvocationMatcherMock = mock(StubbedInvocationMatcher.class);
        when(containerMock.getInvocationForStubbing()).thenReturn(invocation);
        when(containerMock.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcherMock});
        when(stubbedInvocationMatcherMock.getInvocation()).thenReturn(invocation);

        CreationSettings creationSettingsMock = mock(CreationSettings.class);
        when(mockUtilMock.getMockHandler(any()).getMockSettings()).thenReturn(creationSettingsMock);

        when(creationSettingsMock.getTypeToMock()).thenReturn("mockType");
        when(mockUtilMock.getMockHandler(any()).getMockSettings()).thenReturn(creationSettingsMock);

        when(delegateMock.returnValueFor(any(Class.class))).thenReturn("returnValue");

        Object result = returnsDeepStubs.answer(invocation);

        assertEquals("returnValue", result);

        verify(invocation, times(2)).getMock();
        verify(invocation, times(1)).getMethod();
        verify(delegateMock, times(1)).returnValueFor(any(Class.class));
        verify(returnTypeGenericMetadataMock, times(1)).rawType();
        verify(mockCreationValidatorMock, times(1)).isTypeMockable(any(Class.class));
        verify(mockUtilMock, times(1)).getMockHandler(any());
        verify(handlerMock, times(1)).getInvocationContainer();
        verify(containerMock, times(1)).getInvocationForStubbing();
        verify(containerMock, times(1)).getStubbedInvocations();
        verify(stubbedInvocationMatcherMock, times(1)).getInvocation();
        verify(mockUtilMock, times(2)).getMockHandler(any());
        verify(creationSettingsMock, times(1)).getTypeToMock();
        verify(mockUtilMock, times(2)).getMockHandler(any());
        verifyNoMoreInteractions(delegateMock, returnTypeGenericMetadataMock,
                mockCreationValidatorMock, handlerMock, containerMock, stubbedInvocationMatcherMock,
                creationSettingsMock, mockUtilMock, invocation);
    }

    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMock()).thenReturn("mock");
        Object mockValue = "mockValue";

        MockUtil mockUtilMock = mock(MockUtil.class);
        InternalMockHandler<Object> handlerMock = mock(InternalMockHandler.class);
        InvocationContainerImpl containerMock = mock(InvocationContainerImpl.class);

        when(mockUtilMock.getMockHandler(any())).thenReturn(handlerMock);
        when(handlerMock.getInvocationContainer()).thenReturn(containerMock);

        StubbedInvocationMatcher stubbedInvocationMatcherMock = mock(StubbedInvocationMatcher.class);
        when(containerMock.getInvocationForStubbing()).thenReturn(invocationMock);
        when(containerMock.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcherMock});

        when(invocationMock.matches(any())).thenReturn(true);
        when(stubbedInvocationMatcherMock.answer(any())).thenReturn(mockValue);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.getMock(invocationMock);

        assertEquals(mockValue, result);

        verify(invocationMock, times(3)).getMock();
        verify(mockUtilMock, times(1)).getMockHandler(any());
        verify(handlerMock, times(1)).getInvocationContainer();
        verify(containerMock, times(1)).getInvocationForStubbing();
        verify(containerMock, times(1)).getStubbedInvocations();
        verify(stubbedInvocationMatcherMock, times(1)).getInvocation();
        verify(invocationMock, times(1)).matches(any());
        verify(stubbedInvocationMatcherMock, times(1)).answer(any());
        verifyNoMoreInteractions(mockUtilMock, handlerMock, containerMock,
                stubbedInvocationMatcherMock, invocationMock);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        InvocationContainerImpl containerMock = mock(InvocationContainerImpl.class);

        MockUtil mockUtilMock = mock(MockUtil.class);
        InternalMockHandler<Object> handlerMock = mock(InternalMockHandler.class);
        CreationSettings creationSettingsMock = mock(CreationSettings.class);

        when(mockUtilMock.getMockHandler(any())).thenReturn(handlerMock);
        when(handlerMock.getInvocationContainer()).thenReturn(containerMock);
        when(mockUtilMock.getMockHandler(any()).getMockSettings()).thenReturn(creationSettingsMock);

        when(creationSettingsMock.getTypeToMock()).thenReturn("mockType");
        when(creationSettingsMock.getTypeToMock()).thenReturn("mockType");

        Answer<Object> answerMock = mock(Answer.class);
        when(answerMock.answer(any())).thenReturn("mockObject");

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.delegate = mock(ReturnsEmptyValues.class);
        Object result = returnsDeepStubs.recordDeepStubMock(invocationMock, containerMock);

        assertEquals("mockObject", result);

        verify(invocationMock, times(2)).getMethod();
        verify(mockUtilMock, times(1)).getMockHandler(any());
        verify(handlerMock, times(1)).getInvocationContainer();
        verify(mockUtilMock, times(2)).getMockHandler(any());
        verify(invocationMock, times(1)).getMethod();
        verify(containerMock, times(1)).addAnswer(any(), anyBoolean());
        verify(mockUtilMock, times(2)).getMockHandler(any());
        verify(creationSettingsMock, times(2)).getTypeToMock();
        verifyNoMoreInteractions(invocationMock, containerMock, mockUtilMock,
                handlerMock, creationSettingsMock);
    }
}