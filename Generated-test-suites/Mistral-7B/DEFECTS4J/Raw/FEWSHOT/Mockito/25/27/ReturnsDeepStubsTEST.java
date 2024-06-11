package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.lang.reflect.Type;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());

        when(mock.method1()).thenReturn("return value");

        InvocationOnMock invocation = new InvocationOnMock(mock, new Invocation("method1", new Object[0], null));
        Object result = new ReturnsDeepStubs().answer(invocation);

        assertNotNull(result);
        assertEquals("return value", result);
    }

    @Test
    public void testDelegate() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());

        when(mock.method1()).thenThrow(new RuntimeException());

        InvocationOnMock invocation = new InvocationOnMock(mock, new Invocation("method1", new Object[0], null));
        Object result = new ReturnsDeepStubs().delegate.returnValueFor(mock.getClass());

        assertNull(result);
    }

    @Test
    public void testIsTypeMockable() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());

        Class<?> clazz = String[].class;
        boolean isTypeMockable = new ReturnsDeepStubs().isTypeMockable(clazz);

        assertTrue(isTypeMockable);

        clazz = Mockito.class;
        isTypeMockable = new ReturnsDeepStubs().isTypeMockable(clazz);

        assertFalse(isTypeMockable);
    }

    @Test
    public void testGetMock() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());

        InternalMockHandler mockHandler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) mockHandler.getInvocationContainer();

        Object result = new ReturnsDeepStubs().getMock(new InvocationOnMock(mock, new Invocation("method1", new Object[0], null)));

        assertNotNull(result);
        assertSame(mock, result);

        verifyZeroInteractions(result);
    }

    @Test
    public void testGetMockWithStubbedInvocation() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        Object stubbedMock = mock(String.class);

        InternalMockHandler mockHandler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) mockHandler.getInvocationContainer();

        when(mock.method1()).thenReturn(stubbedMock);

        InvocationOnMock invocation = new InvocationOnMock(mock, new Invocation("method1", new Object[0], null));
        Object result = new ReturnsDeepStubs().getMock(invocation);

        assertSame(stubbedMock, result);

        verify(stubbedMock).method1();
    }

    @Test
    public void testRecordDeepStubMock() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());

        Class<?> clazz = String[].class;
        Object deepStub = new ReturnsDeepStubs().recordDeepStubMock(new InvocationOnMock(mock, new Invocation("method1", new Object[0], null)), null);

        assertNotNull(deepStub);
        assertSame(mock, deepStub);

        Type type = new MockCreationValidator().getMockType(clazz);
        verify(mock).method1();
    }

    @Test
    public void testRecordDeepStubMockWithContainer() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());

        InternalMockHandler mockHandler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) mockHandler.getInvocationContainer();

        Class<?> clazz = String[].class;
        Object deepStub = new ReturnsDeepStubs().recordDeepStubMock(new InvocationOnMock(mock, new Invocation("method1", new Object[0], null)), container);

        assertNotNull(deepStub);
        assertSame(mock, deepStub);

        Type type = new MockCreationValidator().getMockType(clazz);
        verify(mock).method1();
    }
}