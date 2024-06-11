import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object result = returnsDeepStubs.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        when(returnTypeGenericMetadata.rawType()).thenReturn(null);
        when(mockitoCore.isTypeMockable(null)).thenReturn(false);
        when(delegate.returnValueFor(null)).thenReturn(null);

        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

        assertNull(result);
    }

    @Test
    public void testCreateNewDeepStubMock() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        when(returnTypeGenericMetadata.rawType()).thenReturn(null);

        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        ReturnsDeepStubs innerReturnsDeepStubs = mock(ReturnsDeepStubs.class);

        Object result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = mock(Answer.class);
        when(container.addAnswer(answer, false)).thenReturn(null);

        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);

        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);

        Object result = returnsDeepStubs.actualParameterizedType(mock);

        assertNotNull(result);
    }
}