import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNotNull(returnsDeepStubs.answer(mock(InvocationOnMock.class)));
    }

    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        assertNotNull(returnsDeepStubs.getMock(invocation, mock(GenericMetadataSupport.class)));
    }

    @Test
    public void testCreateNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNotNull(returnsDeepStubs.createNewDeepStubMock(mock(GenericMetadataSupport.class)));
    }

    @Test
    public void withSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNotNull(returnsDeepStubs.withSettingsUsing(mock(GenericMetadataSupport.class)));
    }

    @Test
    public void returnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNotNull(returnsDeepStubs.returnsDeepStubsAnswerUsing(mock(GenericMetadataSupport.class)));
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNotNull(returnsDeepStubs.recordDeepStubMock(mock(Object.class), mock(InvocationContainerImpl.class)));
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNotNull(returnsDeepStubs.actualParameterizedType(mock(Object.class)));
    }

}