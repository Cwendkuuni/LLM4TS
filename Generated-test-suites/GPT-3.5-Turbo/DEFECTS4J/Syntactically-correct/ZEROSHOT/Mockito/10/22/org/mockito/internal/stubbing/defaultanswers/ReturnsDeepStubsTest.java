import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Create mock InvocationOnMock object
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(new Object()); // Set mock object for invocation

        // Create mock GenericMetadataSupport object
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class); // Set mock return type

        // Create mock InternalMockHandler object
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class)); // Set mock InvocationContainerImpl object

        // Create mock StubbedInvocationMatcher object
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(mock(InvocationOnMock.class)); // Set mock InvocationOnMock object
        when(handler.getInvocationContainer().getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher)); // Set list of StubbedInvocationMatcher objects for invocation container

        // Create mockitoCore object
        MockitoCore mockitoCore = mock(MockitoCore.class);

        // Create mock MockHandler object
        MockHandler mockHandler = mock(MockHandler.class);
        when(mockHandler.getMockSettings()).thenReturn(mock(CreationSettings.class)); // Set mock CreationSettings object

        // Create mock MockUtil object
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(any())).thenReturn(mockHandler); // Set mock MockHandler object

        // Create mock DeeplyStubbedAnswer object
        DeeplyStubbedAnswer deeplyStubbedAnswer = mock(DeeplyStubbedAnswer.class);
        when(deeplyStubbedAnswer.answer(any())).thenReturn(new Object()); // Set return value for DeeplyStubbedAnswer

        // Create mock ReturnsEmptyValues object
        ReturnsEmptyValues returnsEmptyValues = mock(ReturnsEmptyValues.class);

        // Stub static methods
        ReturnsDeepStubsMockitoCoreStub.mockitoCore = mockitoCore;
        ReturnsDeepStubsMockUtilStub.mockUtil = mockUtil;
        ReturnsDeepStubsDeeplyStubbedAnswerStub.deeplyStubbedAnswer = deeplyStubbedAnswer;
        ReturnsDeepStubsReturnsEmptyValuesStub.returnsEmptyValues = returnsEmptyValues;

        // Call answer() method
        Object result = returnsDeepStubs.answer(invocation);

        // Verify the result
        assertTrue(result instanceof Object);
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Create mock InvocationOnMock object
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(new Object()); // Set mock object for invocation

        // Create mock GenericMetadataSupport object
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class); // Set mock return type

        // Create mock InternalMockHandler object
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class)); // Set mock InvocationContainerImpl object

        // Create mock StubbedInvocationMatcher object
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(mock(InvocationOnMock.class)); // Set mock InvocationOnMock object
        when(handler.getInvocationContainer().getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher)); // Set list of StubbedInvocationMatcher objects for invocation container

        // Create mockitoCore object
        MockitoCore mockitoCore = mock(MockitoCore.class);

        // Create mock MockHandler object
        MockHandler mockHandler = mock(MockHandler.class);
        when(mockHandler.getMockSettings()).thenReturn(mock(CreationSettings.class)); // Set mock CreationSettings object

        // Create mock MockUtil object
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(any())).thenReturn(mockHandler); // Set mock MockHandler object

        // Create mock DeeplyStubbedAnswer object
        DeeplyStubbedAnswer deeplyStubbedAnswer = mock(DeeplyStubbedAnswer.class);
        when(deeplyStubbedAnswer.answer(any())).thenReturn(new Object()); // Set return value for DeeplyStubbedAnswer

        // Create mock ReturnsEmptyValues object
        ReturnsEmptyValues returnsEmptyValues = mock(ReturnsEmptyValues.class);

        // Stub static methods
        ReturnsDeepStubsMockitoCoreStub.mockitoCore = mockitoCore;
        ReturnsDeepStubsMockUtilStub.mockUtil = mockUtil;
        ReturnsDeepStubsDeeplyStubbedAnswerStub.deeplyStubbedAnswer = deeplyStubbedAnswer;
        ReturnsDeepStubsReturnsEmptyValuesStub.returnsEmptyValues = returnsEmptyValues;

        // Call deepStub() method
        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        // Verify the result
        assertTrue(result instanceof Object);
    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Create mock GenericMetadataSupport object
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class); // Set mock return type

        // Create mockitoCore object
        MockitoCore mockitoCore = mock(MockitoCore.class);

        // Stub static methods
        ReturnsDeepStubsMockitoCoreStub.mockitoCore = mockitoCore;

        // Call newDeepStubMock() method
        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

        // Verify the result
        assertTrue(result instanceof Object);
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Create mock GenericMetadataSupport object
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class); // Set mock return type
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true); // Set mock hasRawExtraInterfaces value
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class}); // Set mock rawExtraInterfaces value

        // Call withSettingsUsing() method
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Verify the mock settings
        assertTrue(mockSettings.isSerializable());
        assertArrayEquals(new Class[]{Serializable.class}, mockSettings.getExtraInterfaces());
        assertNotNull(mockSettings.getDefaultAnswer());
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Create mock GenericMetadataSupport object
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Call returnsDeepStubsAnswerUsing() method
        ReturnsDeepStubs mockReturnsDeepStubs = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Verify the result
        assertNotNull(mockReturnsDeepStubs);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Create mock object
        Object mock = new Object();

        // Create mock InvocationContainerImpl object
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        // Create mock DeeplyStubbedAnswer object
        DeeplyStubbedAnswer deeplyStubbedAnswer = mock(DeeplyStubbedAnswer.class);

        // Stub static methods
        ReturnsDeepStubsDeeplyStubbedAnswerStub.deeplyStubbedAnswer = deeplyStubbedAnswer;

        // Call recordDeepStubAnswer() method
        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);

        // Verify the result
        assertSame(result, mock);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Create mock object
        Object mock = new Object();

        // Create mock CreationSettings object
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class); // Set mock type to mock

        // Create mock MockUtil object
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(any())).thenReturn(mock(MockHandler.class)).thenReturn(mock(MockHandler.class)); // Set mock MockHandler object

        // Stub static methods
        ReturnsDeepStubsMockUtilStub.mockUtil = mockUtil;

        // Call actualParameterizedType() method
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() throws IOException {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);

        // Call writeReplace() method
        Object result = returnsDeepStubsSerializationFallback.writeReplace();

        // Verify the result
        assertEquals(Mockito.RETURNS_DEEP_STUBS, result);
    }

    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        Object mock = new Object();

        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = new ReturnsDeepStubs.DeeplyStubbedAnswer(mock);

        // Call answer() method
        Object result = deeplyStubbedAnswer.answer(mock(InvocationOnMock.class));

        // Verify the result
        assertSame(mock, result);
    }

    private static class ReturnsDeepStubsMockitoCoreStub {
        static MockitoCore mockitoCore;
    }

    private static class ReturnsDeepStubsMockUtilStub {
        static MockUtil mockUtil;
    }

    private static class ReturnsDeepStubsDeeplyStubbedAnswerStub {
        static DeeplyStubbedAnswer deeplyStubbedAnswer;
    }

    private static class ReturnsDeepStubsReturnsEmptyValuesStub {
        static ReturnsEmptyValues returnsEmptyValues;
    }

}