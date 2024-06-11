import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Test normal functionality
        assertNotNull(returnsDeepStubs.answer(mock(InvocationOnMock.class)));

        // Test if exception is thrown correctly
        try {
            returnsDeepStubs.answer(null);
            fail("Expected an exception to be thrown");
        } catch (Throwable e) {
            assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testGetMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Test generating mock with appropriate metadata
        assertNotNull(returnsDeepStubs.getMock(mock(InvocationOnMock.class), null));

        // Test with actual metadata
        assertNotNull(returnsDeepStubs.getMock(mock(InvocationOnMock.class), mock(GenericMetadataSupport.class)));
    }

    @Test
    public void testCreateNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Test creating mock with no metadata
        assertNotNull(returnsDeepStubs.createNewDeepStubMock(null));

        // Test creating mock with metadata
        assertNotNull(returnsDeepStubs.createNewDeepStubMock(mock(GenericMetadataSupport.class)));
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Test creating mock settings with no extra interfaces
        assertNotNull(returnsDeepStubs.withSettingsUsing(mock(GenericMetadataSupport.class)));

        // Test creating mock settings with extra interfaces
        assertNotNull(returnsDeepStubs.withSettingsUsing(mock(GenericMetadataSupport.class).rawExtraInterfaces(new Class[]{Serializable.class})));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Test creating ReturnsDeepStubs with metadata
        assertNotNull(returnsDeepStubs.returnsDeepStubsAnswerUsing(mock(GenericMetadataSupport.class)));
    }

    @Test
    public void testRecordDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Test recording deep stub mock
        assertNotNull(returnsDeepStubs.recordDeepStubMock(mock(Object.class), mock(InvocationContainerImpl.class)));
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Test actual parameterized type with mock
        assertNotNull(returnsDeepStubs.actualParameterizedType(mock(Object.class)));
    }
}