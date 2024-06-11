import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        try {
            deepStubs.answer(invocation);
        } catch (Throwable e) {
            fail("Exception should not be thrown for a valid invocation");
        }
    }

    @Test
    public void testGetMock() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        try {
            Object mock = deepStubs.getMock(invocation, null);
            assertNotNull(mock);
        } catch (Throwable e) {
            fail("Exception should not be thrown while getting mock");
        }
    }

    @Test
    public void testCreateNewDeepStubMock() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport metadata = mock(GenericMetadataSupport.class);
        
        Object mock = deepStubs.createNewDeepStubMock(metadata);
        
        assertNotNull(mock);
        assertTrue(mock instanceof ReturnsDeepStubs);
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport metadata = mock(GenericMetadataSupport.class);
        
        MockSettings settings = deepStubs.withSettingsUsing(metadata);
        assertNotNull(settings);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport metadata = mock(GenericMetadataSupport.class);
        
        ReturnsDeepStubs stubs = deepStubs.returnsDeepStubsAnswerUsing(metadata);
        assertNotNull(stubs);
        
    }

    @Test
    public void testRecordDeepStubMock() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        try {
            Object result = deepStubs.recordDeepStubMock(mock, container);
            assertEquals(mock, result);
        } catch (Throwable e) {
            fail("Exception should not be thrown while recording mock");
        }
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);

        GenericMetadataSupport metadata = deepStubs.actualParameterizedType(mock);
        
        assertNotNull(metadata);
    }
}