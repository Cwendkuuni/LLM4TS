
package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

@SuppressWarnings("unchecked")
public class ReturnsDeepStubsTest {
    private ReturnsDeepStubs returnsDeepStubs;
    private InvocationOnMock invocation;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
        invocation = mock(InvocationOnMock.class);
    }

    @Test
    public void testAnswerWhenReturnTypeNotMockableReturnsDefault() throws Throwable {
        Object result = returnsDeepStubs.answer(invocation);

        assertEquals("MockType", result);
    }

    @Test
    public void testAnswerWhenReturnTypeMockableInvokesMethod() throws Throwable {
        Object result = returnsDeepStubs.answer(invocation);

        assertEquals("MockType", result);
    }

    @Test
    public void testGetMockWhenInvocationMatchesReturnsStubbedObject() throws Throwable {
        Object result = returnsDeepStubs.getMock(invocation);

        assertEquals("StubbedObject", result);
    }

    @Test
    public void testGetMockWhenInvocationDoesNotMatchReturnsDeepStubMock() throws Throwable {
        Object result = returnsDeepStubs.getMock(invocation);

        assertEquals("DeepStubMock", result);
    }

    @Test
    public void testRecordDeepStubMockAddsAnswerToInvocationContainer() throws Throwable {
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, invocation.getMock());

        assertEquals("AnswerAdded", result);
    }

    @Test
    public void testActualParameterizedTypeReturnsGenericMetadata() {
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(invocation.getMock());

        assertEquals("GenericMetadata", result);
    }
}
