package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InvocationOnMock;
import org.mockito.MockSettings;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMockImpl;
import org.mockito.internal.invocation.StubbedInvocationMatcher;
import org.mockito.internal.mock.MockHandler;
import org.mockito.internal.mock.MockFactory;
import org.mockito.internal.mock.MockUtil;
import org.mockito.internal.reflection.GenericMetadataSupport;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.StubbedInvocationMatcherImpl;
import org.mockito.internal.util.MockitoCore;
import org.mockito.internal.util.reflection.AnnotatedType;
import org.mockito.internal.util.reflection.GenericType;
import org.mockito.invocation.Invocation;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        Invocation invocation = mock(Invocation.class);
        when(mock.entrySet().iterator().next()).thenReturn(mock(Number.class));
        Object result = ((ReturnsDeepStubs) ((Answer<?>) mock.answer(invocation)).getAnswer()).answer(new InvocationOnMockImpl(mock, invocation));
        assertEquals(mock(Number.class), result);
    }

    @Test
    public void testGetMock() throws Exception {
        Class<?> clazz = Map.class;
        MockFactory mockFactory = mockitoCore().mockFactory();
        MockHandler mockHandler = mockitoCore().createMockHandler(clazz);
        mockHandler.setInvocationContainer(new InvocationContainerImpl(mockFactory, mockHandler, new StubbedInvocationMatcherImpl(null)));

        Object mock = getMockReturnsDeepStub(mockFactory, mockHandler, clazz);
        Invocation invocation = mock(Invocation.class);
        when(mock.entrySet().iterator().next()).thenReturn(mock(Number.class));
        Object result = ((ReturnsDeepStubs) ((Answer<?>) mock.answer(invocation)).getAnswer()).answer(new InvocationOnMockImpl(mock, invocation));
        assertEquals(mock, result);

        verify(mockHandler.getInvocationContainer()).recordStubbedInvocation(any(), any());
    }

    @Test
    public void testAnswer_PrimitiveType() throws Exception {
        Object mock = mock(int[].class, new ReturnsDeepStubs());
        Invocation invocation = mock(Invocation.class);
        when(mock).thenReturn(new int[]{1, 2, 3});
        Object result = ((ReturnsDeepStubs) ((Answer<?>) mock.answer(invocation)).getAnswer()).answer(new InvocationOnMockImpl(mock, invocation));
        assertArrayEquals(new int[]{1, 2, 3}, (int[]) result);
    }

    @Test
    public void testAnswer_ArrayType() throws Exception {
        Object mock = mock(String[].class, new ReturnsDeepStubs());
        Invocation invocation = mock(Invocation.class);
        when(mock).thenReturn(new String[]{"foo", "bar"});
        Object result = ((ReturnsDeepStubs) ((Answer<?>) mock.answer(invocation)).getAnswer()).answer(new InvocationOnMockImpl(mock, invocation));
        assertArrayEquals(new String[]{"foo", "bar"}, (String[]) result);
    }

    @Test
    public void testAnswer_ClassType() throws Exception {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        Invocation invocation = mock(Invocation.class);
        when(mock.entrySet().iterator().next()).thenReturn(mock(Number.class));
        Object result = ((ReturnsDeepStubs) ((Answer<?>) mock.answer(invocation)).getAnswer()).answer(new InvocationOnMockImpl(mock, invocation));
        assertEquals(mock(Number.class), result);
    }

    @Test
    public void testAnswer_MapType() throws Exception {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        Invocation invocation = mock(Invocation.class);
        when(mock.entrySet().iterator().next()).thenReturn(mock(Map.Entry.class));
        Object result = ((ReturnsDeepStubs) ((Answer<?>) mock.answer(invocation)).getAnswer()).answer(new InvocationOnMockImpl(mock, invocation));
        assertEquals(mock(Map.Entry.class), result);
    }

    @Test
    public void testAnswer_SetType() throws Exception {
        Object mock = mock(Set.class, new ReturnsDeepStubs());
        Invocation invocation = mock(Invocation.class);
        when(mock).thenReturn(Collections.singleton(mock(Number.class)));
        Object result = ((ReturnsDeepStubs) ((Answer<?>) mock.answer(invocation)).getAnswer()).answer(new InvocationOnMockImpl(mock, invocation));
        assertEquals(Collections.singleton(mock(Number.class)), result);
    }

    @Test
    public void testAnswer_ListType() throws Exception {
        Object mock = mock(List.class, new ReturnsDeepStubs());
        Invocation invocation = mock(Invocation.class);
        when(mock).thenReturn(Arrays.asList(mock(Number.class), mock(Number.class)));
        Object result = ((ReturnsDeepStubs) ((Answer<?>) mock.answer(invocation)).getAnswer()).answer(new InvocationOnMockImpl(mock, invocation));
        assertEquals(Arrays.asList(mock(Number.class), mock(Number.class)), result);
    }

    @Test
    public void testAnswer_GenericType() throws Exception {
        Object mock = mock(GenericsNest.class, new ReturnsDeepStubs());
        Invocation invocation = mock(Invocation.class);
        when(mock.entrySet().iterator().next()).thenReturn(mock(Map.Entry.class));
        Object result = ((ReturnsDeepStubs) ((Answer<?>) mock.answer(invocation)).getAnswer()).answer(new InvocationOnMockImpl(mock, invocation));
        assertEquals(mock(Map.Entry.class), result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAnswer_UnsupportedType() throws Exception {
        Object mock = mock(String.class, new ReturnsDeepStubs());
        Invocation invocation = mock(Invocation.class);
        when(mock).thenReturn("foo");
        ((Answer<?>) mock.answer(invocation)).answer(new InvocationOnMockImpl(mock, invocation));
    }

    @Test
    public void testCreateNewDeepStubMock() throws Exception {
        Object mock = mockitoCore().mock(Map.class, withSettings().defaultAnswer(new ReturnsDeepStubs()));
        assertTrue(mock instanceof Map);
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Map.class);
        assertEquals(returnTypeGenericMetadata.toString(), ((ReturnsDeepStubs) ((Answer<?>) mock.answer(mock(Invocation.class))).getAnswer()).actualParameterizedType(mock).toString());
    }

    @Test
    public void testRecordDeepStubMock() throws Exception {
        Object mock = mockitoCore().mock(Map.class);
        InvocationContainer invocationContainer = new InvocationContainerImpl(mockitoCore().mockFactory(), mockitoCore().createMockHandler(mock), new StubbedInvocationMatcherImpl(null));
        Object result = ((ReturnsDeepStubs) new ReturnsDeepStubs().recordDeepStubMock(mock, invocationContainer)).answer(mockitoCore().mock(Invocation.class));
        assertEquals(mock, result);
        verify(invocationContainer).addAnswer(anyAnswer(), anyBoolean());
    }

    @Test
    public void testAnswer_NullInvocation() throws Exception {
        Object mock = mockitoCore().mock(Map.class, new ReturnsDeepStubs());
        assertNull(((ReturnsDeepStubs) ((Answer<?>) mock.answer(null)).getAnswer()).answer(mockitoCore().mock(Invocation.class)));
    }

    @Test
    public void testAnswer_EmptyStubbedInvocationsContainer() throws Exception {
        Object mock = mockitoCore().mock(Map.class, new ReturnsDeepStubs());
        InvocationContainer invocationContainer = new InvocationContainerImpl(mockitoCore().mockFactory(), mockitoCore().createMockHandler(mock), new StubbedInvocationMatcherImpl(null));
        assertNull(((ReturnsDeepStubs) ((Answer<?>) mock.answer(mockitoCore().mock(Invocation.class))).getAnswer()).answer(invocationContainer));
    }

    @Test
    public void testAnswer_MultipleMatchedStubbedInvocations() throws Exception {
        Object mock = mockitoCore().mock(Map.class);
        InvocationContainer invocationContainer = new InvocationContainerImpl(mockitoCore().mockFactory(), mockitoCore().createMockHandler(mock), new StubbedInvocationMatcherImpl(null));
        Object result1 = mockitoCore().mock(Integer.class);
        Object result2 = mockitoCore().mock(Integer.class);
        invocationContainer.addAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return result1;
            }
        }, false);
        invocationContainer.addAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return result2;
            }
        }, false);

        Object result = ((ReturnsDeepStubs) ((Answer<?>) mock.answer(mockitoCore().mock(Invocation.class))).getAnswer()).answer(new InvocationOnMockImpl(mock, mockitoCore().mock(Invocation.class)));

        assertTrue(result1 == result || result2 == result);
    }

    private Object getMockReturnsDeepStub(MockFactory mockFactory, MockHandler mockHandler, Class<?> clazz) throws Exception {
        mockHandler.setInvocationContainer(new InvocationContainerImpl(mockFactory, mockHandler, new StubbedInvocationMatcherImpl(null)));
        return new ReturnsDeepStubs().getMock(new InvocationOnMockImpl(mockFactory.createMock(clazz), mockitoCore().createMockHandler(clazz)), new GenericMetadataSupport(clazz));
    }

    private MockitoCore mockitoCore() {
        return new MockitoCore();
    }
}