package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.mock.MockComponentImplementation;
import org.mockito.internal.mock.MockSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockHandler;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_returnsExistingMock() throws Exception {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        Answer<Object> answer = mock(Answer.class);
        doAnswer(answer).when(mock).answer(any(InvocationOnMock.class));

        Object result = ReturnsDeepStubs.getInstance().answer(mock(InvocationOnMock.class));

        verify(answer).answer(any(InvocationOnMock.class));
        assertEquals(mock, result);
    }

    @Test
    public void testAnswer_returnsNewMock() throws Exception {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        Object newMock = mock(Object.class);
        Object result = ReturnsDeepStubs.getInstance().answer(mock(InvocationOnMock.class));

        assertNotSame(mock, result);
        assertSame(newMock, result);

        verify(newMock).answer(any(InvocationOnMock.class));
    }

    @Test
    public void testGetMock_existingStub() throws Exception {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        MockComponentImplementation handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
        StubbedInvocationMatcher stubbedInvocationMatcher = stub(answer(anyString())).when(mock(String.class));

        Object result = ReturnsDeepStubs.getInstance().getMock(mock(InvocationOnMock.class),
                new GenericMetadataSupport(String.class));

        assertEquals(stubbedInvocationMatcher.getMock(), result);
        verify(stubbedInvocationMatcher).answer(any(InvocationOnMock.class));
    }

    @Test
    public void testGetMock_newStub() throws Exception {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        MockSettings mockSettings = new MockSettings();
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
        Object newMock = mock(Object.class, withSettings(mockSettings));

        Object result = ReturnsDeepStubs.getInstance().getMock(mock(InvocationOnMock.class),
                new GenericMetadataSupport(Object.class));

        assertEquals(newMock, result);
        verify(newMock).answer(any(InvocationOnMock.class));
    }

    @Test
    public void testGetMock_multipleStubs() throws Exception {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        MockComponentImplementation handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        StubbedInvocationMatcher stub1 = stub(answer(1)).when(mock(Integer.class));
        StubbedInvocationMatcher stub2 = stub(answer(2)).when(mock(Integer.class));
        container.addAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return stub1.getMock();
            }
        }, false);

        Object result1 = ReturnsDeepStubs.getInstance().getMock(mock(InvocationOnMock.class),
                new GenericMetadataSupport(Integer.class));
        assertEquals(stub1.getMock(), result1);

        container.addAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return stub2.getMock();
            }
        }, false);

        Object result2 = ReturnsDeepStubs.getInstance().getMock(mock(InvocationOnMock.class),
                new GenericMetadataSupport(Integer.class));
        assertEquals(stub2.getMock(), result2);
    }

    @Test
    public void testCreateNewDeepStubMock_simpleClass() throws Exception {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) type.getActualTypeArguments()[0];
        Type returnType = parameterizedType.getActualTypeArguments()[0];
        GenericMetadataSupport metadata = new GenericMetadataSupport(returnType.getTypeName());

        Object newMock = ReturnsDeepStubs.getInstance().createNewDeepStubMock(metadata);
        assertEquals(mockitoCore().mock(returnType), newMock);
    }

    @Test
    public void testCreateNewDeepStubMock_nestedClass() throws Exception {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        Type type = getClass().getGenericSuperclass();
        Type listType = type.getActualTypeArguments()[0];
        Type nestedType = ((ParameterizedType) listType).getActualTypeArguments()[0];
        GenericMetadataSupport metadata = new GenericMetadataSupport(nestedType.getTypeName());

        Object newMock = ReturnsDeepStubs.getInstance().createNewDeepStubMock(metadata);
        assertEquals(mockitoCore().mock(nestedType), newMock);
    }

    private Object mockitoCore() {
        return MockitoCore.getInstance();
    }
}