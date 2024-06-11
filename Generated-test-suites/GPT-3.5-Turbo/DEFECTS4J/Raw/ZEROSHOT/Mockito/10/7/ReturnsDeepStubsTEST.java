import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation1.getMock()).thenReturn(Object.class);
        Object result1 = returnsDeepStubs.answer(invocation1);
        assertNotNull(result1);
        assertEquals(ReturnsDeepStubs.DeeplyStubbedAnswer.class, result1.getClass());

        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation2.getMock()).thenReturn(ReturnsDeepStubs.class);
        Object result2 = returnsDeepStubs.answer(invocation2);
        assertNotNull(result2);
        assertEquals(ReturnsDeepStubs.Delegate.class, result2.getClass());
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation1.getMock()).thenReturn(Object.class);
        Class returnType1 = returnsDeepStubs.deepStub(invocation1, null).getClass();
        assertNotNull(returnType1);
        assertEquals(Object.class, returnType1);

        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation2.getMock()).thenReturn(ReturnsDeepStubs.class);
        Class returnType2 = returnsDeepStubs.deepStub(invocation2, null).getClass();
        assertNotNull(returnType2);
        assertEquals(Object.class, returnType2);
    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation1.getMock()).thenReturn(Object.class);
        Object result1 = returnsDeepStubs.newDeepStubMock(null);
        assertNotNull(result1);
        assertEquals(Object.class, result1.getClass());

        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation2.getMock()).thenReturn(ReturnsDeepStubs.class);
        Object result2 = returnsDeepStubs.newDeepStubMock(null);
        assertNotNull(result2);
        assertEquals(ReturnsDeepStubs.class, result2.getClass());
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation1.getMock()).thenReturn(Object.class);
        Object result1 = returnsDeepStubs.withSettingsUsing(null);
        assertNotNull(result1);
        assertEquals(Mockito.class, result1.getClass());

        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation2.getMock()).thenReturn(ReturnsDeepStubs.class);
        Object result2 = returnsDeepStubs.withSettingsUsing(null);
        assertNotNull(result2);
        assertEquals(Mockito.class, result2.getClass());
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation1.getMock()).thenReturn(Object.class);
        ReturnsDeepStubs deepStubs1 = returnsDeepStubs.returnsDeepStubsAnswerUsing(null);
        assertNotNull(deepStubs1);
        assertEquals(ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback.class, deepStubs1.getClass());

        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation2.getMock()).thenReturn(ReturnsDeepStubs.class);
        ReturnsDeepStubs deepStubs2 = returnsDeepStubs.returnsDeepStubsAnswerUsing(null);
        assertNotNull(deepStubs2);
        assertEquals(ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback.class, deepStubs2.getClass());
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation1.getMock()).thenReturn(Object.class);
        Object mock1 = Mockito.mock(Object.class);
        Object result1 = returnsDeepStubs.recordDeepStubAnswer(mock1, null);
        assertNotNull(result1);
        assertEquals(mock1, result1);

        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation2.getMock()).thenReturn(ReturnsDeepStubs.class);
        Object mock2 = Mockito.mock(ReturnsDeepStubs.class);
        Object result2 = returnsDeepStubs.recordDeepStubAnswer(mock2, null);
        assertNotNull(result2);
        assertEquals(mock2, result2);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        ReturnsDeepStubs.GenericMetadataSupport metadataSupport = Mockito.mock(ReturnsDeepStubs.GenericMetadataSupport.class);
        ReturnsEmptyValues mockSettings = Mockito.mock(ReturnsEmptyValues.class);
        Mockito.when(mockSettings.getTypeToMock()).thenReturn(metadataSupport);

        Object mock = Mockito.mock(Object.class);
        Mockito.when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        assertNotNull(result);
        assertEquals(metadataSupport, result);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() throws IOException {
        ReturnsDeepStubs.GenericMetadataSupport genericMetadataSupport = Mockito.mock(ReturnsDeepStubs.GenericMetadataSupport.class);
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback fallback = new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(genericMetadataSupport);

        Object result = fallback.writeReplace();
        assertNotNull(result);
    }

    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        Object mock = Mockito.mock(Object.class);
        ReturnsDeepStubs.DeeplyStubbedAnswer answer = new ReturnsDeepStubs.DeeplyStubbedAnswer(mock);

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = answer.answer(invocation);
        assertNotNull(result);
        assertEquals(mock, result);
    }

    @Test
    public void testMockitoCore() {
        MockitoCore core = ReturnsDeepStubs.mockitoCore();
        assertNotNull(core);
        assertEquals(MockitoCore.class, core.getClass());
    }

    @Test
    public void testDelegate() {
        ReturnsEmptyValues delegate = ReturnsDeepStubs.delegate();
        assertNotNull(delegate);
        assertEquals(ReturnsEmptyValues.class, delegate.getClass());
    }
}