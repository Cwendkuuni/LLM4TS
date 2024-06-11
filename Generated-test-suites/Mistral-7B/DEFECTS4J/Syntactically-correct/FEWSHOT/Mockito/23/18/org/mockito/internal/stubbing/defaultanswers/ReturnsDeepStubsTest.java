package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.mockito.MockSettings;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerWith;

public class ReturnsDeepStubsTest {

    @Test
    public void testDeepStubsReturnSameInstanceForSameInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        InternalMockHandler<Object> mockHandler = mockitoCore.mock(Object.class);
        InvocationContainerImpl container = (InvocationContainerImpl) mockHandler.getInvocationContainer();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock1 = returnsDeepStubs.getMock(mock(Object.class), new GenericMetadataSupport(Object.class));
        Object mock2 = returnsDeepStubs.getMock(mock(Object.class), new GenericMetadataSupport(Object.class));

        assertEquals(mock1, mock2);
    }

    @Test
    public void testDeepStubsReturnDifferentInstancesForDifferentInvocations() {
        MockitoCore mockitoCore = new MockitoCore();
        InternalMockHandler<Object> mockHandler1 = mockitoCore.mock(Object.class);
        Object mock1 = mockHandler1.getMock();
        InternalMockHandler<Object> mockHandler2 = mockitoCore.mock(Object.class);
        Object mock2 = mockHandler2.getMock();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock3 = returnsDeepStubs.getMock(mock(Object.class), new GenericMetadataSupport(Object.class));

        assertNotSame(mock1, mock3);
        assertNotSame(mock2, mock3);
    }

    @Test
    public void testDeepStubsReturnMockForMatchingInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        InternalMockHandler<Object> mockHandler = mockitoCore.mock(Object.class);
        InvocationContainerImpl container = (InvocationContainerImpl) mockHandler.getInvocationContainer();

        Object mock = mockitoCore.mock(Map.class, ReturnsDeepStubs.RETURNS_DEEP_STUBS);

        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.answer(any(InvocationOnMock.class))).thenReturn(mock);
        container.addStubbedInvocation(stubbedInvocationMatcher);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object returnedMock = returnsDeepStubs.getMock(mock(Map.class), new GenericMetadataSupport(Map.class));

        assertSame(mock, returnedMock);
    }

    @Test
    public void testDeepStubsReturnDelegateForNonMockableType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.answer(mock(String.class, ReturnsEmptyValues.RETURNS_EMPTY));

        assertEquals(ReturnsEmptyValues.INSTANCE, result);
    }

    @Test
    public void testDeepStubsCreateNewMockForNull() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNull(returnsDeepStubs.getMock(null, new GenericMetadataSupport(Object.class)));
    }

    @Test
    public void testDeepStubsCreateNewMockForUnsupportedType() {
        MockitoCore mockitoCore = new MockitoCore();
        InternalMockHandler<Object> mockHandler = mockitoCore.mock(Long.class);
        Object unsupportedTypeMock = mockHandler.getMock();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertSame(unsupportedTypeMock, returnsDeepStubs.getMock(mock(Long.class), new GenericMetadataSupport(Long.class)));
    }

    @Test
    public void testDeepStubsCreateNewMockForArray() {
        MockitoCore mockitoCore = new MockitoCore();
        InternalMockHandler<Object> mockHandler = mockitoCore.mock(Object[].class);
        Object arrayMock = mockHandler.getMock();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object[] array = new Object[5];
        when(mockHandler.getMock()).thenReturn(arrayMock);
        when(arrayMock.getClass()).thenReturn(array.getClass());

        Object result = returnsDeepStubs.getMock(mock(Object[].class), new GenericMetadataSupport(Object[].class));

        assertSame(arrayMock, result);
    }

    @Test
    public void testDeepStubsCreateNewMockForCollection() {
        MockitoCore mockitoCore = new MockitoCore();
        InternalMockHandler<Object> mockHandler = mockitoCore.mock(Collection.class);
        Object collectionMock = mockHandler.getMock();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Map<String, Object> map = new HashMap<>();
        when(mockHandler.getMock()).thenReturn(collectionMock);
        when(collectionMock.getClass()).thenReturn(map.class);

        Object result = returnsDeepStubs.getMock(mock(Collection.class), new GenericMetadataSupport(Collection.class));

        assertSame(map, result);
    }

    @Test
    public void testDeepStubsCreateNewMockForMap() {
        MockitoCore mockitoCore = new MockitoCore();
        InternalMockHandler<Object> mockHandler = mockitoCore.mock(Map.class);
        Object mapMock = mockHandler.getMock();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Map<String, Object> map = new HashMap<>();
        when(mockHandler.getMock()).thenReturn(mapMock);
        when(mapMock.getClass()).thenReturn(map.class);

        Object result = returnsDeepStubs.getMock(mock(Map.class), new GenericMetadataSupport(Map.class));

        assertSame(map, result);
    }

    @Test
    public void testDeepStubsCreateNewMockForGenericType() {
        MockitoCore mockitoCore = new MockitoCore();
        InternalMockHandler<Object> mockHandler = mockitoCore.mock(Map.class);
        Object mapMock = mockHandler.getMock();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
        Object result = returnsDeepStubs.getMock(mock(Map.class), new GenericMetadataSupport(mapType));

        assertSame(map, result);
    }

    @Test
    public void testDeepStubsCreateNewMockForNestedGenericType() {
        MockitoCore mockitoCore = new MockitoCore();
        InternalMockHandler<Object> mockHandler = mockitoCore.mock(Map.class);
        Object mapMock = mockHandler.getMock();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Type mapType = new TypeToken<Map<String, Map<String, Object>>>() {}.getType();
        Object result = returnsDeepStubs.getMock(mock(Map.class), new GenericMetadataSupport(mapType));

        assertSame(new HashMap<>(), result);
    }

    @Test
    public void testDeepStubsCreateNewMockForComplexGenericType() {
        MockitoCore mockitoCore = new MockitoCore();
        InternalMockHandler<Object> mockHandler = mockitoCore.mock(Map.class);
        Object mapMock = mockHandler.getMock();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Type mapType = new TypeToken<Map<String, Map<String, Map<String, Object>>>>() {}.getType();
        Object result = returnsDeepStubs.getMock(mock(Map.class), new GenericMetadataSupport(mapType));

        assertSame(new HashMap<>(), result);
    }

    @Test
    public void testDeepStubsCreateNewMockForNestedList() {
        MockitoCore mockitoCore = new MockitoCore();
        InternalMockHandler<Object> mockHandler = mockitoCore.mock(List.class);
        Object listMock = mockHandler.getMock();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
        Object result = returnsDeepStubs.getMock(mock(List.class), new GenericMetadataSupport(listType));

        assertSame(Collections.singletonList(new HashMap<>()), result);
    }

    @Test
    public void testDeepStubsCreateNewMockForNestedCollection() {
        MockitoCore mockitoCore = new MockitoCore();
        InternalMockHandler<Object> mockHandler = mockitoCore.mock(Collection.class);
        Object collectionMock = mockHandler.getMock();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Type collectionType = new TypeToken<Collection<Map<String, Object>>>() {}.getType();
        Object result = returnsDeepStubs.getMock(mock(Collection.class), new GenericMetadataSupport(collectionType));

        assertSame(Arrays.asList(new HashMap<>()), result);
    }
}