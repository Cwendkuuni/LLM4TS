import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsDeepStubs.answer(invocation);
        assertNotNull(result);
    }

    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        assertNotNull(returnsDeepStubs.getMock(invocation, null));
    }

    @Test
    public void testCreateNewDeepStubMock() {
        assertNotNull(returnsDeepStubs.createNewDeepStubMock(null));
    }

    @Test
    public void testWithSettingsUsing() {
        assertNotNull(returnsDeepStubs.withSettingsUsing(null));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        assertNotNull(returnsDeepStubs.returnsDeepStubsAnswerUsing(null));
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        Object mock = new Object();
        assertNotNull(returnsDeepStubs.recordDeepStubMock(mock, null));
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        assertNotNull(returnsDeepStubs.actualParameterizedType(mock));
    }

}