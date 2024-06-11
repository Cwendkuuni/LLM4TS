
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_returnsPreviouslyCreatedMock() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = answer.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testGetMock_returnsMock() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = answer.getMock(invocation);

        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubMock_createsMock() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = answer.recordDeepStubMock(invocation, mock(InvocationContainerImpl.class));

        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType_returnsGenericMetadataSupport() {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();

        Object mock = mock(Object.class);

        GenericMetadataSupport result = answer.actualParameterizedType(mock);

        assertNotNull(result);
    }
}

