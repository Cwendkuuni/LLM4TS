package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationOnMock;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.MockSettings;
import org.mockito.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.Invocation;

import java.io.Serializable;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(mock.answer(invocation)).thenReturn(42);

        assertEquals(42, mock.answer(invocation));
    }

    @Test
    public void testDeepStub() throws Exception {
        Object mockA = mock(Object.class, new ReturnsDeepStubs());
        Object mockB = mock(Object.class, new ReturnsDeepStubs());
        Object mockC = mock(Object.class, new ReturnsDeepStubs());

        when(mockA.deepStub(any(InvocationOnMock.class), any(GenericMetadataSupport.class))).thenReturn(mockB);
        when(mockB.deepStub(any(InvocationOnMock.class), any(GenericMetadataSupport.class))).thenReturn(mockC);

        Object invocationMock = mock(InvocationOnMock.class);
        Object invocationMockType = invocationMock.getMock().getClass();
        GenericMetadataSupport invocationMetadata = mock(GenericMetadataSupport.class);
        invocationMetadata.setRawType(invocationMockType);

        Object result = mockA.answer(invocationMock);

        assertEquals(mockC, result);
        verify(mockB).answer(invocationMock);
        verify(mockA).deepStub(invocationMock, invocationMetadata);
    }

    @Test
    public void testNewDeepStubMock() throws Exception {
        Class<?> clazz = Mockito.class;
        Object mock = mock(clazz, new ReturnsDeepStubs());
        GenericMetadataSupport metadata = new GenericMetadataSupport(clazz);

        MockSettings settings = mockitoCore().mock(clazz, withSettings().extraInterfaces(new Class[]{MockSettings.class}));
        MockSettings deepSettings = new ReturnsDeepStubs().withSettingsUsing(metadata);

        assertEquals(deepSettings, settings);
    }

    @Test(expected = AssertionError.class)
    public void testNonMockableType() throws Exception {
        Object mock = mock(Integer.TYPE, new ReturnsDeepStubs());
        fail();
    }

    @Test
    public void testRecordDeepStubAnswer() throws Exception {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = mockContainer(mock);

        Object stubbedMock = mock(Object.class, new Answers.ReturnsDeepStubs());
        StubbedInvocationMatcher stubbedMatcher = new StubbedInvocationMatcher(stubbedMock, any());
        container.addAnswer(stubbedMatcher, true);

        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object result = mock.answer(invocation);
        assertEquals(stubbedMock, result);
        verify(stubbedMock).answer(invocation);
    }

    private Object mockContainer(Object mock) {
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        return (InvocationContainerImpl) handler.getInvocationContainer();
    }

    private MockitoCore mockitoCore() {
        return MockitoCore.getInstance();
    }
}