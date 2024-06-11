import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsDeepStubs.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = returnsDeepStubs.getMock(invocation, mock(GenericMetadataSupport.class));
        assertNotNull(mock);
    }

    @Test
    public void testCreateNewDeepStubMock() {
        Object mock = returnsDeepStubs.createNewDeepStubMock(mock(GenericMetadataSupport.class));
        assertNotNull(mock);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);
        Object result = returnsDeepStubs.recordDeepStubMock(mock, mock(InvocationContainerImpl.class));
        assertEquals(mock, result);
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs returnsDeepStubsAnswer = returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadataSupport);
        assertNotNull(returnsDeepStubsAnswer);
    }

}