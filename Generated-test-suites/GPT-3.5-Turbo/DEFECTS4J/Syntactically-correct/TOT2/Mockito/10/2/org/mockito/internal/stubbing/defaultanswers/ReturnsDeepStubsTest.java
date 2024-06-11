import org.junit.Test;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsDeepStubs.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
        Object result = returnsDeepStubs.deepStub(invocation, genericMetadata);
        assertNull(result);
    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
        Object result = returnsDeepStubs.newDeepStubMock(genericMetadata);
        assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(genericMetadata);
        assertNotNull(mockSettings);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);
        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        assertNotNull(result);
    }

}