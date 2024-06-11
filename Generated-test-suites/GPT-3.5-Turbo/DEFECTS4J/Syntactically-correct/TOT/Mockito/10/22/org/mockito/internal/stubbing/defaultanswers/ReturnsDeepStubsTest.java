import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("testMock");
        when(invocation.getMethod()).thenReturn(this.getClass().getDeclaredMethod("testAnswer"));

        Object result = returnsDeepStubs.answer(invocation);

        // Verify that the actualParameterizedType method is called
        verify(returnsDeepStubs, times(1))
                .actualParameterizedType(eq("testMock"));

        // Verify that the delegate method is called
        verify(returnsDeepStubs, times(1))
                .delegate();

        // Verify that the mockitoCore method is called
        verify(returnsDeepStubs, times(1))
                .mockitoCore();

        // Verify that the deepStub method is called
        verify(returnsDeepStubs, times(1))
                .deepStub(eq(invocation), any(GenericMetadataSupport.class));

        // Verify that the recordDeepStubAnswer method is called
        verify(returnsDeepStubs, times(1))
                .recordDeepStubAnswer(any(Object.class), any(InvocationContainerImpl.class));
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = spy(new ReturnsDeepStubs());
        ReturnsDeepStubs.GenericMetadataSupport genericMetadataSupport = mock(ReturnsDeepStubs.GenericMetadataSupport.class);

        when(genericMetadataSupport.resolveGenericReturnType(any())).thenReturn(genericMetadataSupport);
        when(genericMetadataSupport.rawType()).thenReturn(Object.class);

        Object result = returnsDeepStubs.actualParameterizedType(new Object());

        // Verify that the resolveGenericReturnType method is called
        verify(genericMetadataSupport, times(1))
                .resolveGenericReturnType(any());

        // Verify that the rawType method is called
        verify(genericMetadataSupport, times(1))
                .rawType();

        assertEquals(Object.class, result);
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = spy(new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        when(returnsDeepStubs.getMockHandler(invocation.getMock()))
                .thenReturn(mockHandler);

        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(mockHandler.getInvocationContainer()).thenReturn(container);

        StubbedInvocationMatcher stubbedInvocationMatcher1 = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher1));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher1.getInvocation()))
                .thenReturn(true);

        Object result = returnsDeepStubs.deepStub(invocation, mock(ReturnsDeepStubs.GenericMetadataSupport.class));

        // Verify that the getMockHandler method is called
        verify(returnsDeepStubs, times(1))
                .getMockHandler(eq(invocation.getMock()));

        // Verify that the addAnswer method is called
        verify(container, times(1))
                .addAnswer(any(ReturnsDeepStubs.DeeplyStubbedAnswer.class), eq(false));

        assertNotNull(result);
    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = spy(new ReturnsDeepStubs());
        ReturnsDeepStubs.GenericMetadataSupport genericMetadataSupport = mock(ReturnsDeepStubs.GenericMetadataSupport.class);

        Class<?> rawType = Object.class;
        when(genericMetadataSupport.rawType()).thenReturn(rawType);
        when(returnsDeepStubs.mockitoCore().mock(eq(Object.class), any()))
                .thenReturn("mock");

        Object result = returnsDeepStubs.newDeepStubMock(genericMetadataSupport);

        // Verify that the rawType method is called
        verify(genericMetadataSupport, times(1))
                .rawType();

        // Verify that the mockitoCore method is called
        verify(returnsDeepStubs.mockitoCore(), times(1))
                .mock(eq(Object.class), any());

        assertNotNull(result);
        assertEquals("mock", result);
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = spy(new ReturnsDeepStubs());
        ReturnsDeepStubs.GenericMetadataSupport genericMetadataSupport = mock(ReturnsDeepStubs.GenericMetadataSupport.class);

        when(genericMetadataSupport.hasRawExtraInterfaces()).thenReturn(true);
        when(genericMetadataSupport.rawExtraInterfaces())
                .thenReturn(Arrays.asList(Comparable.class, Cloneable.class));

        when(withSettings().extraInterfaces(eq(Arrays.asList(Comparable.class, Cloneable.class))))
                .thenReturn(withSettings());
        when(withSettings().serializable()).thenReturn(withSettings());
        when(withSettings().defaultAnswer(any())).thenReturn(withSettings());

        returnsDeepStubs.withSettingsUsing(genericMetadataSupport);

        // Verify that the hasRawExtraInterfaces method is called
        verify(genericMetadataSupport, times(1))
                .hasRawExtraInterfaces();

        // Verify that the rawExtraInterfaces method is called
        verify(genericMetadataSupport, times(1))
                .rawExtraInterfaces();

        // Verify that the withSettings().extraInterfaces method is called
        verify(withSettings(), times(1))
                .extraInterfaces(any());

        // Verify that the withSettings().serializable method is called
        verify(withSettings(), times(1))
                .serializable();

        // Verify that the withSettings().defaultAnswer method is called
        verify(withSettings(), times(1))
                .defaultAnswer(any());
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = spy(new ReturnsDeepStubs());
        ReturnsDeepStubs.GenericMetadataSupport genericMetadataSupport = mock(ReturnsDeepStubs.GenericMetadataSupport.class);
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback =
                mock(ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback.class);

        when(returnsDeepStubs.returnsDeepStubsAnswerUsing(eq(genericMetadataSupport)))
                .thenReturn(returnsDeepStubsSerializationFallback);

        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback result =
                returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadataSupport);

        // Verify that the returnsDeepStubsAnswerUsing method is called
        verify(returnsDeepStubs, times(1))
                .returnsDeepStubsAnswerUsing(eq(genericMetadataSupport));

        assertNotNull(result);
        assertEquals(returnsDeepStubsSerializationFallback, result);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = spy(new ReturnsDeepStubs());
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(returnsDeepStubs.recordDeepStubAnswer(any(), any())).thenCallRealMethod();
        when(container.addAnswer(any(ReturnsDeepStubs.DeeplyStubbedAnswer.class), eq(false))).thenReturn(container);

        Object result = returnsDeepStubs.recordDeepStubAnswer(new Object(), container);

        // Verify that the addAnswer method is called
        verify(container, times(1))
                .addAnswer(any(ReturnsDeepStubs.DeeplyStubbedAnswer.class), eq(false));

        assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() throws IOException {
        ReturnsDeepStubs returnsDeepStubs = spy(new ReturnsDeepStubs());
        ReturnsDeepStubs.GenericMetadataSupport genericMetadataSupport = mock(ReturnsDeepStubs.GenericMetadataSupport.class);

        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback serializationFallback =
                new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(genericMetadataSupport);

        when(genericMetadataSupport.hasRawExtraInterfaces()).thenReturn(true);

        when(serializationFallback.writeReplace()).thenCallRealMethod();
        when(Mockito.RETURNS_DEEP_STUBS).thenReturn(serializationFallback);

        Object result = serializationFallback.writeReplace();

        // Verify that the hasRawExtraInterfaces method is called
        verify(genericMetadataSupport, times(1))
                .hasRawExtraInterfaces();

        assertNotNull(result);
        assertEquals(Mockito.RETURNS_DEEP_STUBS, result);
    }

    @Test
    public void testDeeplyStubbedAnswer() {
        ReturnsDeepStubs returnsDeepStubs = spy(new ReturnsDeepStubs());
        Object mock = mock(Object.class);

        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = new ReturnsDeepStubs.DeeplyStubbedAnswer(mock);

        Object result = deeplyStubbedAnswer.answer(mock(InvocationOnMock.class));

        assertEquals(mock, result);
    }

    @Test
    public void testLazyHolder() {
        ReturnsDeepStubs.LazyHolder lazyHolder = new ReturnsDeepStubs.LazyHolder();

        MockitoCore mockitoCore = lazyHolder.MOCKITO_CORE;
        ReturnsEmptyValues delegate = lazyHolder.DELEGATE;

        assertNotNull(mockitoCore);
        assertNotNull(delegate);
    }
}