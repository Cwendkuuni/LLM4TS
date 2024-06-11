import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsDeepStubs.answer(invocation);
        assertNotNull(answer);
    }

    @Test
    public void testDeepStub() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsDeepStubs.deepStub(invocation, null);
        assertNull(answer);
    }

    @Test
    public void testNewDeepStubMock() {
        Object result = returnsDeepStubs.newDeepStubMock(null);
        assertNull(result);
    }

    @Test
    public void testWithSettingsUsing() {
        Object result = returnsDeepStubs.withSettingsUsing(null);
        assertNull(result);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(null);
        assertNull(result);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);
        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        ReturnsDeepStubs.GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        assertNotNull(result);
    }
}