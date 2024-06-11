package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswerForNonMockableType() {
        Object nonMockableType = new Object() {};
        when(mock(nonMockableType.getClass())).thenReturn(nonMockableType);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(new MockMethod("foo", nonMockableType.getClass(), Collections.emptyList()));

        Object result = returnsDeepStubs.answer(invocation);
        assertEquals(nonMockableType, result);
    }

    @Test
    public void testGetMockForMatchingInvocation() {
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(new MockMethod("foo", Object.class, Collections.emptyList()));

        InvocationContainerImpl container = new InvocationContainerImpl();
        container.addAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return "matching answer";
            }
        }, false);

        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        handler.setInvocationContainer(container);

        when(handler.getInvocationContainer()).thenReturn(container);

        Object result = returnsDeepStubs.getMock(invocation);
        assertEquals("matching answer", result);

        verify(mock).answer(invocation);
    }

    @Test
    public void testGetMockForNonMatchingInvocation() {
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(new MockMethod("foo", Object.class, Collections.emptyList()));

        InvocationContainerImpl container = new InvocationContainerImpl();

        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        handler.setInvocationContainer(container);

        when(handler.getInvocationContainer()).thenReturn(container);

        Object result = returnsDeepStubs.getMock(invocation);
        assertNull(result);
    }

    @Test
    public void testRecordDeepStubMock() {
        Class<?> clz = String[].class;
        Object mock = mock(clz, returnsDeepStubs);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.setMethod(new MockMethod("foo", clz, Collections.emptyList()));

        InvocationContainerImpl container = new InvocationContainerImpl();

        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        handler.setInvocationContainer(container);

        when(handler.getInvocationContainer()).thenReturn(container);

        Object deepStubMock = returnsDeepStubs.recordDeepStubMock(invocation, container);

        assertEquals(mock, deepStubMock);

        InvocationOnMock stubbedInvocation = mock(InvocationOnMock.class);
        stubbedInvocation.setMethod(new MockMethod("answer", ReturnsDeepStubs.class, Arrays.asList(InvocationOnMock.class)));
        StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(stubbedInvocation);

        container.addStubbedInvocation(stubbedInvocationMatcher);

        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("stubbed answer");

        Object answer = returnsDeepStubs.getMock(invocation);

        assertEquals("stubbed answer", answer);
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(List.class);
        CreationSettings mockSettings = new CreationSettings();
        mockSettings.setTypeToMock(List.class);

        GenericMetadataSupport genericMetadataSupport = new GenericMetadataSupport();

        Class<?> returnType = genericMetadataSupport.inferFrom(mockSettings.getTypeToMock());

        assertEquals(List.class, returnType);
    }
}