import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_returnsMockForNonMockableReturnType() {
        // given
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();

        // when
        Something result = deepStubs.answer(mock(InvocationOnMock.class));

        // then
        assertNotNull(result);
    }

    @Test
    public void testAnswer_returnsDeepStubForMockableReturnType() {
        // given
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(MockitoCore.class));

        // when
        Object result = deepStubs.answer(invocation);

        // then
        assertNotNull(result);
    }

    @Test
    public void testDeepStub_invokesAnswerIfInvocationMatches() {
        // given
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(mock(Invocation.class));
        when(container.getInvocationForStubbing()).thenReturn(mock(InvocationMatcher.class));
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(invocation.getMock()).thenReturn(mock(MockitoCore.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        ReflectionUtil reflectionUtil = mock(ReflectionUtil.class);
        when(reflectionUtil.isTypeMockable(any(Class.class))).thenReturn(true);
        FieldUtil fieldUtil = mock(FieldUtil.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(Class.class);
        when(genericMetadataSupport.hasRawExtraInterfaces()).thenReturn(true);
        when(genericMetadataSupport.rawExtraInterfaces()).thenReturn(new Class[] { Serializable.class });
        when(fieldUtil.getField(any(Class.class), any(String.class))).thenReturn(mock(Field.class));
        when(fieldUtil.readField(any(Field.class), any(Object.class))).thenReturn(genericMetadataSupport);
        when(mockUtil.getFieldUtil()).thenReturn(fieldUtil);
        when(mockUtil.getReflectionUtil()).thenReturn(reflectionUtil);
        when(mockUtil.getGenericMetadataSupport()).thenReturn(genericMetadataSupport);
        whenNew(MockUtil.class).withNoArguments().thenReturn(mockUtil);

        // when
        Object result = deepStubs.answer(invocation);

        // then
        assertNotNull(result);
    }

    @Test
    public void testDeepStub_recordsDeepStubAnswerIfInvocationDoesNotMatch() {
        // given
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(mock(Invocation.class));
        when(container.getInvocationForStubbing()).thenReturn(mock(InvocationMatcher.class));
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(invocation.getMock()).thenReturn(mock(MockitoCore.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        ReflectionUtil reflectionUtil = mock(ReflectionUtil.class);
        when(reflectionUtil.isTypeMockable(any(Class.class))).thenReturn(true);
        FieldUtil fieldUtil = mock(FieldUtil.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(Class.class);
        when(genericMetadataSupport.hasRawExtraInterfaces()).thenReturn(true);
        when(genericMetadataSupport.rawExtraInterfaces()).thenReturn(new Class[] { Serializable.class });
        when(fieldUtil.getField(any(Class.class), any(String.class))).thenReturn(mock(Field.class));
        when(fieldUtil.readField(any(Field.class), any(Object.class))).thenReturn(genericMetadataSupport);
        when(mockUtil.getFieldUtil()).thenReturn(fieldUtil);
        when(mockUtil.getReflectionUtil()).thenReturn(reflectionUtil);
        when(mockUtil.getGenericMetadataSupport()).thenReturn(genericMetadataSupport);
        whenNew(MockUtil.class).withNoArguments().thenReturn(mockUtil);
        DeeplyStubbedAnswer deeplyStubbedAnswer = mock(DeeplyStubbedAnswer.class);
        whenNew(DeeplyStubbedAnswer.class).withArguments(any(Object.class)).thenReturn(deeplyStubbedAnswer);

        // when
        Object result = deepStubs.answer(invocation);

        // then
        assertNotNull(result);
        verify(container).addAnswer(eq(deeplyStubbedAnswer), eq(false));
    }

    @Test
    public void testNewDeepStubMock_createsMockUsingGenericsMetadata() {
        // given
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        AnnotationDefaultValue annotationDefaultValue = mock(AnnotationDefaultValue.class);
        when(annotationDefaultValue.getElementType()).thenReturn(RETURN_TYPE);
        FieldDetail fieldDetail = mock(FieldDetail.class);
        when(fieldDetail.getAnnotationsWithDefaults()).thenReturn(Collections.singletonList(annotationDefaultValue));
        whenNew(FieldDetail.class).withArguments(any(ResolvedType.class), any(Field.class)).thenReturn(fieldDetail);
        TypeResolver typeResolver = mock(TypeResolver.class);
        when(typeResolver.resolve(any(Type.class))).thenReturn(mock(ResolvedType.class));
        whenNew(TypeResolver.class).withNoArguments().thenReturn(typeResolver);

        // when
        Object result = deepStubs.newDeepStubMock(mock(GenericMetadataSupport.class));

        // then
        assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing_returnsMockSettingsWithExtraInterfaces() {
        // given
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.hasRawExtraInterfaces()).thenReturn(true);
        when(genericMetadataSupport.rawExtraInterfaces()).thenReturn(new Class[] { Serializable.class });

        // when
        MockSettings result = deepStubs.withSettingsUsing(genericMetadataSupport);

        // then
        assertNotNull(result);
        assertArrayEquals(new Class[] { Serializable.class }, result.getExtraInterfaces());
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing_createsNewReturnsDeepStubsSerializationFallbackInstance() {
        // given
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);

        // when
        ReturnsDeepStubs result = deepStubs.returnsDeepStubsAnswerUsing(genericMetadataSupport);

        // then
        assertNotNull(result);
        assertTrue(result instanceof ReturnsDeepStubsSerializationFallback);
        assertEquals(genericMetadataSupport, ((ReturnsDeepStubsSerializationFallback) result).returnTypeGenericMetadata);
    }

    @Test
    public void testRecordDeepStubAnswer_addsDeepStubAnswerToInvocationContainer() {
        // given
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mock = mock(Object.class);
        DeeplyStubbedAnswer deeplyStubbedAnswer = mock(DeeplyStubbedAnswer.class);

        // when
        Object result = deepStubs.recordDeepStubAnswer(mock, container);

        // then
        assertNotNull(result);
        verify(container).addAnswer(eq(deeplyStubbedAnswer), eq(false));
    }

    @Test
    public void testActualParameterizedType_returnsInferredGenericMetadataFromMockHandler() {
        // given
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        MockHandler mockHandler = mock(MockHandler.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        whenNew(MockUtil.class).withNoArguments().thenReturn(mock(MockUtil.class));
        when(mockHandler.getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class));
        when(mockSettings.getTypeToMock()).thenReturn(mock(Type.class));
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        whenNew(GenericMetadataSupport.class).withNoArguments().thenReturn(genericMetadataSupport);

        // when
        GenericMetadataSupport result = deepStubs.actualParameterizedType(mock(MockitoCore.class));

        // then
        assertEquals(genericMetadataSupport, result);
    }

    @Test
    public void testWriteReplace_returnsMockitoReturnsDeepStubs() throws IOException {
        // given
        ReturnsDeepStubsSerializationFallback fallback = new ReturnsDeepStubsSerializationFallback(mock(GenericMetadataSupport.class));

        // when
        Object result = fallback.writeReplace();

        // then
        assertEquals(Mockito.RETURNS_DEEP_STUBS, result);
    }

    @Test
    public void testDeeplyStubbedAnswer_returnsMock() {
        // given
        Object mock = mock(Object.class);
        DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mock);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // when
        Object result = deeplyStubbedAnswer.answer(invocation);

        // then
        assertNotNull(result);
        assertEquals(mock, result);
    }

    private static class Something {}

}