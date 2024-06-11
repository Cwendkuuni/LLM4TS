import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object result = returnsDeepStubs.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(invocation.getMock()).thenReturn(this);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(invocation);

        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

        assertNotNull(result);
    }

    @Test
    public void testCreateNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);

        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object mock = mock(Object.class);

        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[]{Serializable.class});
        when(mockitoCore.mock(returnTypeGenericMetadata.rawType(), withSettings().extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces()))).thenReturn(mock);
        when(mockitoCore.mock(returnTypeGenericMetadata.rawType(), withSettings())).thenReturn(mock);

        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs expectedAnswer = Mockito.mock(ReturnsDeepStubs.class);

        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[]{Serializable.class});
        when(expectedAnswer.actualParameterizedType(this)).thenReturn(returnTypeGenericMetadata);

        ReturnsDeepStubs actualAnswer = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        assertNotNull(actualAnswer);
        assertEquals(expectedAnswer, actualAnswer);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(null);

        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);

        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);

        when(new MockUtil().getMockHandler(this)).thenReturn(mock(InternalMockHandler.class));
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock())).thenReturn(genericMetadataSupport);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(this);

        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType_Exception() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        CreationSettings mockSettings = mock(CreationSettings.class);

        when(new MockUtil().getMockHandler(this)).thenReturn(mock(InternalMockHandler.class));
        when(mockSettings.getTypeToMock()).thenThrow(new RuntimeException());

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(this);

        assertNull(result);
    }
}