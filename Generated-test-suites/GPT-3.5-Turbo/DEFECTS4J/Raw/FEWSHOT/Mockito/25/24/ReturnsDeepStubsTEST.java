package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationMock.getMethod()).thenReturn(SomeReturnType.class.getDeclaredMethod("someMethod"));
        GenericMetadataSupport genericMetadataSupportMock = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(genericMetadataSupportMock.rawType()).thenReturn(Object.class);
        MockUtil mockUtilMock = Mockito.mock(MockUtil.class);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock())).thenReturn(Mockito.mock(InternalMockHandler.class));
        CreationSettings mockSettingsMock = Mockito.mock(CreationSettings.class);
        Mockito.when(mockSettingsMock.getTypeToMock()).thenReturn(Object.class);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings().getTypeToMock()).thenReturn(Object.class);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(mockUtilMock.getMockHandler(invocationMock.getMock())).getMockSettings().getTypeToMock();
        Mockito.when(returnTypeGenericMetadataMock.rawType()).thenReturn(Object.class);
        Mockito.when(mockSettingsMock.getTypeToMock()).thenReturn(Object.class);
        Mockito.when(genericMetadataSupportMock.resolveGenericReturnType(invocationMock.getMethod())).thenReturn(returnTypeGenericMetadataMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock())).thenReturn(mockHandlerMock);
        Mockito.when(invocationMock.getMock()).thenReturn(mock);
        Mockito.when(invocationMock.getMethod()).thenReturn(method);
        Mockito.when(returnTypeGenericMetadataMock.rawType()).thenReturn(classMock);
        Mockito.when(new MockCreationValidator().isTypeMockable(rawType)).thenReturn(true);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock())).thenReturn(mockHandlerMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings().getTypeToMock()).thenReturn(Object.class);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock())).thenReturn(mockHandlerMock);
        Mockito.when(invocationMock.getMock()).thenReturn(mock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings().getTypeToMock()).thenReturn(Object.class);
        Mockito.when(mockUtilMock.getMockHandler(invocationMock.getMock()).getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(invocationMock.getMethod().getReturnType()).thenReturn(Object.class);
        Mockito.when(containerMock.getStubbedInvocations()).thenReturn(stubbedInvocationMatchersMock);
        Mockito.when(stubbedInvocationMatcherMock.getInvocation()).thenReturn(invocationMock);
        Mockito.when(containerMock.getInvocationForStubbing()).thenReturn(invocationForStubbingMock);
        Mockito.when(invocationForStubbingMock.matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        Mockito.when(stubbedInvocationMatcherMock.answer(invocationMock)).thenReturn(answerMock);
        Mockito.when(answerMock.answer(invocationMock)).thenReturn(mock);
        Mockito.when(invocationMock.getMethod()).thenReturn(someMethodClass.getDeclaredMethod("someMethod"));

        Object result = returnsDeepStubs.answer(invocationMock);

        assertEquals(mock, result);
    }

    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
        InternalMockHandler<Object> handlerMock = Mockito.mock(InternalMockHandler.class);
        InvocationContainerImpl containerMock = Mockito.mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcherMock = Mockito.mock(StubbedInvocationMatcher.class);
        Answer<Object> answerMock = Mockito.mock(Answer.class);
        Object mock = new Object();

        Mockito.when(handlerMock.getInvocationContainer()).thenReturn(containerMock);
        Mockito.when(containerMock.getStubbedInvocations()).thenReturn(stubbedInvocationMatcherMock);
        Mockito.when(containerMock.getInvocationForStubbing().matches(stubbedInvocationMatcherMock.getInvocation())).thenReturn(true);
        Mockito.when(stubbedInvocationMatcherMock.answer(invocationMock)).thenReturn(answerMock);
        Mockito.when(answerMock.answer(invocationMock)).thenReturn(mock);

        Object result = returnsDeepStubs.getMock(invocationMock);

        assertEquals(mock, result);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
        InvocationContainerImpl containerMock = Mockito.mock(InvocationContainerImpl.class);
        Class<?> clzMock = Mockito.mock(Class.class);
        final Object mock = new Object();

        Mockito.when(invocationMock.getMethod().getReturnType()).thenReturn(clzMock);
        Mockito.when(handlerMock.getInvocationContainer()).thenReturn(containerMock);
        Mockito.doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(containerMock).addAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return mock;
            }
        }, false);

        Object result = returnsDeepStubs.recordDeepStubMock(invocationMock, containerMock);

        assertEquals(mock, result);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = new Object();
        InternalMockHandler<Object> handlerMock = Mockito.mock(InternalMockHandler.class);
        CreationSettings mockSettingsMock = Mockito.mock(CreationSettings.class);
        Mockito.when(mockHandlerMock.getMockSettings()).thenReturn(mockSettingsMock);
        Mockito.when(mockHandlerMock.getMockSettings().getTypeToMock()).thenReturn(Object.class);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        assertEquals(GenericMetadataSupport.class, result.getClass());
    }

}