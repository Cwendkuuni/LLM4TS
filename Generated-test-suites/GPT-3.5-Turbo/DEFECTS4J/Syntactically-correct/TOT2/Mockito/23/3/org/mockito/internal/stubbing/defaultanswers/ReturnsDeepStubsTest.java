import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNotNull(returnsDeepStubs.answer(mock(InvocationOnMock.class)));
    }

    @Test
    public void testGetMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNotNull(returnsDeepStubs.getMock(mock(InvocationOnMock.class), new GenericMetadataSupport()));
    }

    @Test
    public void testCreateNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNotNull(returnsDeepStubs.createNewDeepStubMock(new GenericMetadataSupport()));
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNotNull(returnsDeepStubs.withSettingsUsing(new GenericMetadataSupport()));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNotNull(returnsDeepStubs.returnsDeepStubsAnswerUsing(new GenericMetadataSupport()));
    }

    @Test
    public void testRecordDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNotNull(returnsDeepStubs.recordDeepStubMock(mock(Object.class), new InvocationContainerImpl()));
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNotNull(returnsDeepStubs.actualParameterizedType(mock(Object.class)));
    }
}