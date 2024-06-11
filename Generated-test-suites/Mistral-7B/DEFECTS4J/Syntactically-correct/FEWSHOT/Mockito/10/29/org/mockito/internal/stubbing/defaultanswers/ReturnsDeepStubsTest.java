package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.Mockito;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;

import java.io.Serializable;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerWithReturnType() {
        MockSettings mockSettings = withSettings().serializable();
        Object mock = mock(String.class, mockSettings);
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(String.class);
        ReturnsDeepStubs deepStubsAnswer = new ReturnsDeepStubs(returnTypeGenericMetadata);
        handler.setAnswer(InvocationOnMock.method(String.class, "toString"), deepStubsAnswer);

        assertEquals("deep stub", mock.toString());
        assertEquals(mock, container.getDeepStubbedAnswer("toString"));
    }

    @Test
    public void testAnswerWithGenericReturnType() {
        MockSettings mockSettings = withSettings().serializable();
        Object mock = mock(List.class, mockSettings);
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(ArrayList.class);
        ReturnsDeepStubs deepStubsAnswer = new ReturnsDeepStubs(returnTypeGenericMetadata);
        handler.setAnswer(InvocationOnMock.method(List.class, "size"), deepStubsAnswer);

        List list = (List) mock;
        assertEquals(42, list.size());
        assertEquals(list, container.getDeepStubbedAnswer("size"));
    }

    @Test
    public void testAnswerWithNestedGenerics() {
        MockSettings mockSettings = withSettings().serializable();
        Object mock = mock(Map.class, mockSettings);
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(HashMap.class);
        ReturnsDeepStubs deepStubsAnswer = new ReturnsDeepStubs(returnTypeGenericMetadata);
        handler.setAnswer(InvocationOnMock.method(Map.class, "entrySet").andReturn(mock), deepStubsAnswer);

        Map map = (Map) mock;
        assertEquals(true, map.isEmpty());
        assertEquals(map, container.getDeepStubbedAnswer("entrySet"));

        Set set = map.entrySet().iterator().next().getValue();
        assertEquals(1, set.size());
        assertEquals(set, container.getDeepStubbedAnswer("entrySet").iterator().next().getValue());

        Number number = (Number) set.iterator().next();
        assertEquals(42, number.intValue());
        assertEquals(number, container.getDeepStubbedAnswer("entrySet").iterator().next().getValue().iterator().next());
    }

    @Test
    public void testAnswerWithDeepStubbedAnswer() {
        MockSettings mockSettings = withSettings().serializable();
        Object mock = mock(Map.class, mockSettings);
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(HashMap.class);
        ReturnsDeepStubs deepStubsAnswer = new ReturnsDeepStubs(returnTypeGenericMetadata);
        handler.setAnswer(InvocationOnMock.method(Map.class, "entrySet"), deepStubsAnswer);

        Map map = (Map) mock;
        Set set = map.entrySet().iterator().next().getValue();
        Number number = (Number) set.iterator().next();

        handler.setAnswer(InvocationOnMock.method(Number.class, "intValue"), deepStubsAnswer);

        assertEquals(42, number.intValue());

        Number deepStubNumber = mock(Number.class, withSettings());
        handler.setAnswer(InvocationOnMock.method(Number.class, "intValue"), deepStubNumber);

        handler.setAnswer(InvocationOnMock.method(Map.class, "entrySet").andReturn(mock), deepStubsAnswer);

        assertEquals(42, number.intValue());
        assertEquals(deepStubNumber, container.getDeepStubbedAnswer("entrySet").iterator().next().getValue().iterator().next());
    }

    @Test(expected = AssertionError.class)
    public void testAnswerWithNonMockableType() {
        MockSettings mockSettings = withSettings().serializable();
        Object mock = mock(String[].class, mockSettings);

        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(String[].class);
        ReturnsDeepStubs deepStubsAnswer = new ReturnsDeepStubs(returnTypeGenericMetadata);
        handler.setAnswer(InvocationOnMock.method(Object[].class, "clone"), deepStubsAnswer);

        String[] array = (String[]) mock;
        assertArrayEquals(new String[]{"foo", "bar"}, array);

        try {
            assertEquals(array, container.getDeepStubbedAnswer("clone"));
            fail();
        } catch (AssertionError e) {
            // expected
        }
    }
}