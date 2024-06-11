import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        Object answer1 = returnsDeepStubs.answer(invocation1);
        assertNull(answer1);

        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        when(invocation2.getMock()).thenReturn(null);
        Object answer2 = returnsDeepStubs.answer(invocation2);
        assertNull(answer2);
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        GenericMetadataSupport genericMetadataSupport1 = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport1.rawType()).thenReturn(Object.class);
        Object answer1 = returnsDeepStubs.deepStub(invocation1, genericMetadataSupport1);
        assertNotNull(answer1);

        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        GenericMetadataSupport genericMetadataSupport2 = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport2.rawType()).thenReturn(String.class);
        Object answer2 = returnsDeepStubs.deepStub(invocation2, genericMetadataSupport2);
        assertNotNull(answer2);
    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object mock1 = returnsDeepStubs.newDeepStubMock(mock(GenericMetadataSupport.class));
        assertNotNull(mock1);

        Object mock2 = returnsDeepStubs.newDeepStubMock(mock(GenericMetadataSupport.class));
        assertNotNull(mock2);
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        GenericMetadataSupport returnTypeGenericMetadata1 = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata1.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata1.rawExtraInterfaces()).thenReturn(new Class<?>[]{Serializable.class});

        MockSettings mockSettings1 = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata1);
        assertNotNull(mockSettings1);

        GenericMetadataSupport returnTypeGenericMetadata2 = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata2.hasRawExtraInterfaces()).thenReturn(false);

        MockSettings mockSettings2 = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata2);
        assertNotNull(mockSettings2);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        ReturnsDeepStubs returnsDeepStubsAnswer = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        assertNotNull(returnsDeepStubsAnswer);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        doNothing().when(container).addAnswer(any(), anyBoolean());

        Object mock = mock(Object.class);

        Object answer = returnsDeepStubs.recordDeepStubAnswer(mock, container);
        assertNotNull(answer);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(any())).thenReturn(mock(InternalMockHandler.class));
        whenNew(MockUtil.class).withNoArguments().thenReturn(mockUtil);

        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);

        InternalMockHandler mockHandler = mock(InternalMockHandler.class);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);

        when(mockUtil.getMockHandler(any())).thenReturn(mockHandler);

        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(GenericMetadataSupport.inferFrom(Object.class)).thenReturn(genericMetadataSupport);

        Object mock = mock(Object.class);
        Object answer = returnsDeepStubs.actualParameterizedType(mock);
        assertNotNull(answer);
    }
}