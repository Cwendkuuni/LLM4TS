package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethodWithEmptyReturnType() {
        Object mock = Mockito.mock(Object.class);
        Answer<Object> returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class));
        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(ReturnsEmptyValues.class, result);
    }

    @Test
    public void testAnswerMethodWithMockableReturnType() throws Exception {
        Object mock = Mockito.mock(Map.class);
        Answer<Object> returnsDeepStubs = new ReturnsDeepStubs();
        Map<String, Set<Number>> map = (Map<String, Set<Number>>) returnsDeepStubs.answer(Mockito.mock(InvocationOnMock.class));
        Assertions.assertNotNull(map);
        Assertions.assertInstanceOf(Map.class, map);
        Assertions.assertEquals(Collections.emptyMap(), map);
    }

    @Test
    public void testGetMockMethodWithNoMatchingStubbedInvocation() throws Throwable {
        Object mock = Mockito.mock(Map.class);
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.getMock(Mockito.mock(InvocationOnMock.class));
        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(Map.class, result);
    }

    @Test
    public void testGetMockMethodWithMatchingStubbedInvocation() throws Throwable {
        Object mapMock = Mockito.mock(Map.class);
        Object numberMock = Mockito.mock(Number.class);
        InternalMockHandler<Object> mapHandler = new MockUtil().getMockHandler(mapMock);
        InternalMockHandler<Object> numberHandler = new MockUtil().getMockHandler(numberMock);
        InvocationContainerImpl mapContainer = (InvocationContainerImpl) mapHandler.getInvocationContainer();
        InvocationContainerImpl numberContainer = (InvocationContainerImpl) numberHandler.getInvocationContainer();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        mapContainer.addAnswer(Mockito.answer(() -> numberMock), false);

        Object result = returnsDeepStubs.getMock(Mockito.mock(InvocationOnMock.class));
        Assertions.assertNotNull(result);
        Assertions.assertInstanceOf(Number.class, result);
    }

    @Test(expected = Throwable.class)
    public void testGetMockMethodWithException() throws Throwable {
        Object mock = Mockito.mock(Map.class);
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        throw new RuntimeException("Exception in getMock method");
    }
}