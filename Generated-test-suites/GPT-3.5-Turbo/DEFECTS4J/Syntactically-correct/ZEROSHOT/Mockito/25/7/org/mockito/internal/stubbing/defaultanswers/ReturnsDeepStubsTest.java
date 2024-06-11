package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsDeepStubs.answer(invocation);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsDeepStubs.getMock(invocation);
        Assert.assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubMock() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);
        Assert.assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        Assert.assertNotNull(result);
    }

}