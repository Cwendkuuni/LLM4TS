package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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
import org.mockito.stubbing.AnswerWithMock;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);

        Object result = new ReturnsDeepStubs().answer(invocation);
        assertNotNull(result);
        verify(mock).answer(invocation);
    }

    @Test
    public void testAnswer_notMockableType() {
        Object mock = mock(Object.class);

        Class<String> stringClass = String.class;
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(stringClass.getDeclaredMethod("valueOf", String.class));
        when(invocation.getMock()).thenReturn(mock);

        Object result = new ReturnsDeepStubs().answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswer_deepStub() {
        Object mock = mock(Object.class);
        InvocationContainerImpl container = spy(new InvocationContainerImpl());

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(new StubbedInvocationMatcher(invocation)));

        Object result = new ReturnsDeepStubs().answer(invocation);
        assertNotNull(result);
        assertEquals(mock.getClass(), result.getClass());
    }

    @Test
    public void testAnswer_deepStub_multipleMatchedInvocations() {
        Object mock = mock(Object.class);
        InvocationContainerImpl container = spy(new InvocationContainerImpl());

        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        when(invocation1.getMock()).thenReturn(mock);
        when(invocation2.getMock()).thenReturn(mock);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(
                new StubbedInvocationMatcher(invocation1),
                new StubbedInvocationMatcher(invocation2)
        ));

        Object result1 = new ReturnsDeepStubs().answer(invocation1);
        Object result2 = new ReturnsDeepStubs().answer(invocation2);
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(mock.getClass(), result1.getClass());
        assertEquals(mock.getClass(), result2.getClass());
    }

    @Test
    public void testGetMock() throws Exception {
        Object mock = mock(Object.class);
        InvocationContainerImpl container = spy(new InvocationContainerImpl());

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);
        when(container.getInvocationForStubbing()).thenReturn(invocation);

        Object result = new ReturnsDeepStubs().getMock(invocation, container);
        assertNotNull(result);
        assertEquals(mock.getClass(), result.getClass());
    }

    @Test
    public void testRecordDeepStubMock() throws Exception {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = spy(new InvocationContainerImpl());

        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(invocation.getMethod()).thenReturn(mock(List.class.getDeclaredMethod("iterator")));

        Object deepStub = new ReturnsDeepStubs().recordDeepStubMock(invocation, container);
        assertNotNull(deepStub);
        assertEquals(List.class, deepStub.getClass());

        InvocationOnMock deepStubInvocation = mock(InvocationOnMock.class);
        when(deepStubInvocation.getMethod()).thenReturn(mock(List.class.getDeclaredMethod("get", int.class)));
        when(deepStubInvocation.getArgument(0)).thenReturn(0);

        when(container.getInvocationForStubbing()).thenReturn(deepStubInvocation);
        when(deepStubInvocation.getMock()).thenReturn(deepStub);

        Object result = new ReturnsDeepStubs().answer(deepStubInvocation);
        assertNotNull(result);
        assertEquals(mock(Integer.class).getClass(), result.getClass());
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        CreationSettings creationSettings = new CreationSettings();

        Type typeToMock = mock(Type.class);
        when(typeToMock.getTypeName()).thenReturn("java.util.List<java.util.List<java.util.List<Object>>>");
        when(mock.getClass()).thenReturn(List.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(creationSettings);
        when(creationSettings.getTypeToMock()).thenReturn(typeToMock);

        GenericMetadataSupport genericMetadataSupport = new ReturnsDeepStubs().actualParameterizedType(mock);
        assertEquals(List.class, genericMetadataSupport.rawType());

        Type genericType = genericMetadataSupport.getGenericType();
        assertEquals(List.class, genericType.getComponentType());
        assertEquals(List.class, ((ParameterizedType) genericType).getActualTypeArguments()[0]);
        assertEquals(List.class, ((ParameterizedType) ((ParameterizedType) ((ParameterizedType) genericType).getActualTypeArguments()[0])).getActualTypeArguments()[0]);
    }
}