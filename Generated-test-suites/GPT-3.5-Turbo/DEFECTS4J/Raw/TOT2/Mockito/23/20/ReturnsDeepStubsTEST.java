import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object result = returnsDeepStubs.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object result = returnsDeepStubs.getMock(invocation, null);

        assertNull(result);
    }

    @Test
    public void testCreateNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object result = returnsDeepStubs.createNewDeepStubMock(null);

        assertNull(result);
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object result = returnsDeepStubs.withSettingsUsing(null);

        assertNull(result);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object result = returnsDeepStubs.returnsDeepStubsAnswerUsing(null);

        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object result = returnsDeepStubs.recordDeepStubMock(new Object(), null);

        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);

        Object result = returnsDeepStubs.actualParameterizedType(mock);

        assertNull(result);
    }
}