package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerWithDeepStubs() {
        interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {}

        GenericsNest<Object> mock = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
        Map<Object, Set<Number>> deepStubMap = mock.entrySet().iterator().next().getValue();
        assertNotNull(deepStubMap);

        Number number = deepStubMap.entrySet().iterator().next().getValue().iterator().next();
        assertNotNull(number);

        when(mock.entrySet().iterator().next().getValue().iterator().next()).thenReturn(1);
        verify(mock).entrySet().iterator();

        assertEquals(1, number.intValue());
    }

    @Test
    public void testAnswerWithEmptyValues() {
        interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {}

        GenericsNest<Object> mock = mock(GenericsNest.class, new ReturnsEmptyValues());
        Map<Object, Set<Number>> emptyMap = mock.entrySet().iterator().next().getValue();
        assertNotNull(emptyMap);

        Number number = deepStubMap.entrySet().iterator().next().getValue().iterator().next();
        assertNull(number);

        when(mock.entrySet().iterator().next().getValue().iterator().next()).thenReturn(Collections.emptySet().iterator().next());
        verify(mock).entrySet().iterator();
    }

    @Test
    public void testGetMockWithMatchingInvocation() throws Exception {
        interface GenericsInterface {}

        GenericsInterface mockInterface = mock(GenericsInterface.class, Answers.RETURNS_DEEP_STUBS);
        GenericsInterface mock = (GenericsInterface) getMock(mockInterface);

        Method method = GenericsInterface.class.getMethod("testMethod", String.class);
        InvocationOnMock invocation = new InvocationOnMock(mock, method, new Object[]{"test"});

        Object deepStub = getMock(invocation);
        assertNotNull(deepStub);

        verify(mock).testMethod("test");
    }

    @Test
    public void testGetMockWithNoMatchingInvocation() throws Exception {
        interface GenericsInterface {}

        GenericsInterface mockInterface = mock(GenericsInterface.class, Answers.RETURNS_DEEP_STUBS);
        GenericsInterface mock = (GenericsInterface) getMock(mockInterface);

        Method method = GenericsInterface.class.getMethod("testMethod2", String.class);
        InvocationOnMock invocation = new InvocationOnMock(mock, method, new Object[]{"test"});

        Object deepStub = getMock(invocation);
        assertNull(deepStub);

        verify(mock, never()).testMethod2("test");
    }

    private Object getMock(Object mock) throws Exception {
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
        return container.getReturnsDeepStubs().getMock(invocation);
    }
}