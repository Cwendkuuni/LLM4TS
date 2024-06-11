package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

    @Test
    public void testGetMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = new InvocationContainerImpl(mockitoCore.getMockHandler(mock));

        when(mock).thenReturn("foo");

        Object result = container.getMock(new Object() {}, new GenericMetadataSupport(Object.class));
        verify(result).thenReturn("foo");
        assertNotNull(result);
    }

    @Test
    public void testGetMockWithMatchingStubbedInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = new InvocationContainerImpl(mockitoCore.getMockHandler(mock));

        Object stubbedMock = mock(String.class, new ReturnsDeepStubs());
        container.addAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return (String) stubbedMock;
            }
        }, false);

        when(mock).thenReturn(stubbedMock);

        Object result = container.getMock(new Object() {}, new GenericMetadataSupport(String.class));
        verify(result).thenReturn(stubbedMock);
        assertNotNull(result);
    }

    @Test
    public void testGetMockWithNoMatchingStubbedInvocation() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = new InvocationContainerImpl(mockitoCore.getMockHandler(mock));

        Object stubbedMock = mock(String.class, new ReturnsDeepStubs());
        container.addAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return "foo";
            }
        }, false);

        when(mock).thenReturn(stubbedMock);

        Object result = container.getMock(new Object() {}, new GenericMetadataSupport(Integer.class));
        assertNull(result);
    }

    @Test
    public void testCreateNewDeepStubMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = (InvocationContainerImpl) mockitoCore.getMockHandler(mock);

        GenericMetadataSupport genericMetadata = new GenericMetadataSupport(List.class);
        Object deepStubMock = createNewDeepStubMock(container, mock, genericMetadata);

        assertNotNull(deepStubMock);
        verify(deepStubMock).thenAnswer(new Answer<List<Object>>() {
            @Override
            public List<Object> answer(InvocationOnMock invocation) throws Throwable {
                return Collections.emptyList();
            }
        });
    }

    @Test
    public void testRecordDeepStubMock() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = (InvocationContainerImpl) mockitoCore.getMockHandler(mock);

        Object deepStubMock = mock(List.class, new ReturnsDeepStubs());
        container.addAnswer(new Answer<List<Object>>() {
            @Override
            public List<Object> answer(InvocationOnMock invocation) throws Throwable {
                return Arrays.asList(new Object() {});
            }
        }, false);

        Object result = recordDeepStubMock(mock, container, deepStubMock);
        verify(deepStubMock).thenAnswer(new Answer<List<Object>>() {
            @Override
            public List<Object> answer(InvocationOnMock invocation) throws Throwable {
                return Arrays.asList(new Object() {});
            }
        });
        assertNotNull(result);
    }

    @Test
    public void testGetMockWithExtraInterfaces() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = new InvocationContainerImpl(mockitoCore.getMockHandler(mock));

        when(mock).thenReturn(new HashMap<Object, Set<Number>>());

        GenericMetadataSupport genericMetadata = new GenericMetadataSupport(Map.class, Set[].class);
        Object deepStubMock = createNewDeepStubMock(container, mock, genericMetadata);

        assertNotNull(deepStubMock);
        verify(deepStubMock).thenAnswer(new Answer<Set<Number>>() {
            @Override
            public Set<Number> answer(InvocationOnMock invocation) throws Throwable {
                return Collections.emptySet();
            }
        });
    }

    @Test
    public void testAnswerWithDeepStub() {
        MockitoCore mockitoCore = new MockitoCore();
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = new InvocationContainerImpl(mockitoCore.getMockHandler(mock));

        Object deepStubMock = mock(String.class, new ReturnsDeepStubs());
        container.addAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                return (String) deepStubMock;
            }
        }, false);

        Object result = container.answer(new Object() {}, ArgumentMatchers.anyString(), new GenericMetadataSupport(String.class));

        assertNotNull(result);
        verify(deepStubMock).thenReturn("result");
        assertEquals("result", result);
    }

    private Object createNewDeepStubMock(InvocationContainerImpl container, Object mock, GenericMetadataSupport genericMetadata) {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs() {
            @Override
            protected GenericMetadataSupport actualParameterizedType(Object mock) {
                return genericMetadata;
            }
        };
        return container.createNewMock(genericMetadata.rawType(), withSettingsUsing(genericMetadata), returnsDeepStubs);
    }

    private Object recordDeepStubMock(Object mock, InvocationContainerImpl container, Object deepStubMock) throws Throwable {
        container.addAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return deepStubMock;
            }
        }, false);
        return deepStubMock;
    }
}