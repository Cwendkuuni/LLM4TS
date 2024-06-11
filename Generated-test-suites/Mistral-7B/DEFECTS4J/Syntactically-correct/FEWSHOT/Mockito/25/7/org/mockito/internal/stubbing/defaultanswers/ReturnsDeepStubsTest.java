package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Type;
import java.util.Arrays;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_MockableType() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = new InvocationOnMock(mock, new Invocation(null, Arrays.asList(null), null));

        when(mock.toString()).thenReturn("mock");

        Object result = new ReturnsDeepStubs().answer(invocation);
        assertNotNull(result);
        verify(mock).toString();
    }

    @Test
    public void testAnswer_NotMockableType() {
        Object mock = mock(String.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = new InvocationOnMock(mock, new Invocation(null, Arrays.asList(String[].class), "toString"));

        Object result = new ReturnsDeepStubs().answer(invocation);
        assertEquals(new ReturnsEmptyValues().returnValueFor(String.class), result);
    }

    @Test
    public void testGetMock_MatchedInvocation() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = new InvocationOnMock(mock, new Invocation(null, Arrays.asList(null), null));

        StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(invocation);
        InvocationContainerImpl container = new InvocationContainerImpl(mock, Arrays.asList(stubbedInvocationMatcher), null);

        when(mock.toString()).thenReturn("mock");

        Object result = new ReturnsDeepStubs().getMock(invocation);
        assertNotNull(result);
        verify(mock).toString();

        assertEquals(mock, result);
    }

    @Test
    public void testGetMock_NotMatchedInvocation() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = new InvocationOnMock(mock, new Invocation(null, Arrays.asList(Integer.class), "toString"));

        InvocationContainerImpl container = new InvocationContainerImpl(mock, Arrays.emptyList(), null);

        Object result = new ReturnsDeepStubs().getMock(invocation);
        assertNull(result);
    }

    @Test
    public void testRecordDeepStubMock() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = new InvocationOnMock(mock, new Invocation(null, Arrays.asList(null), null));

        InvocationContainerImpl container = new InvocationContainerImpl(mock, Arrays.emptyList(), null);

        Object deepStubMock = new ReturnsDeepStubs().recordDeepStubMock(invocation, container);

        assertNotNull(deepStubMock);
        verify(mock).toString();
    }

    @Test
    public void testRecordDeepStubMock_SameInvocation() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        Object deepStubMock1 = new ReturnsDeepStubs().recordDeepStubMock(mock, new InvocationOnMock(mock, new Invocation(null, Arrays.asList(null), null)));
        Object deepStubMock2 = new ReturnsDeepStubs().recordDeepStubMock(mock, new InvocationOnMock(mock, new Invocation(null, Arrays.asList(null), null)));

        assertEquals(deepStubMock1, deepStubMock2);
    }

    @Test
    public void testRecordDeepStubMock_DifferentInvocations() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        Object deepStubMock1 = new ReturnsDeepStubs().recordDeepStubMock(mock, new InvocationOnMock(mock, new Invocation(null, Arrays.asList(Integer.class), "toString")));
        Object deepStubMock2 = new ReturnsDeepStubs().recordDeepStubMock(mock, new InvocationOnMock(mock, new Invocation(null, Arrays.asList(Character.class), "toString")));

        assertNotSame(deepStubMock1, deepStubMock2);
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());

        CreationSettings creationSettings = new CreationSettings();
        creationSettings.setTypeToMock(Object[].class);

        GenericMetadataSupport genericMetadataSupport = new GenericMetadataSupport();
        GenericMetadataSupport actualParameterizedType = new ReturnsDeepStubs().actualParameterizedType(mock);

        assertEquals(genericMetadataSupport.inferFrom(creationSettings.getTypeToMock()), actualParameterizedType.rawType());
    }
}