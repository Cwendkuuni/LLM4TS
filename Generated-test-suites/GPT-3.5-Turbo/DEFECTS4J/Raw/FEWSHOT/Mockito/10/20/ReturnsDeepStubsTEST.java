package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import java.io.IOException;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class ReturnsDeepStubsTest {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Test for non-mockable raw type
        assertEquals("returnValue", returnsDeepStubs.answer(invocation));

        // Test for mockable raw type
        setupMockitoCoreMock();
        Object result = returnsDeepStubs.answer(invocation);
        assertEquals("mock", result);

        // Test for deep stub
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        setupDeepStub(container);
        Object mock = mock(Object.class);
        container.addAnswer(new DeeplyStubbedAnswer(mock), false);
        result = returnsDeepStubs.answer(invocation);
        assertEquals(mock, result);
    }

    @Test
    public void testNewDeepStubMock() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        setupMockitoCoreMock();
        Object mock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
        assertEquals(Object.class, mock.getClass());
    }

    @Test
    public void testWithSettingsUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubsInterface answer = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        assertEquals(ReturnsDeepStubsSerializationFallback.class, answer.getClass());

        returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        returnTypeGenericMetadata.hasRawExtraInterfaces();
        answer = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        assertEquals(ReturnsDeepStubsSerializationFallback.class, answer.getClass());
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubsSerializationFallback answer = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        assertEquals(ReturnsDeepStubsSerializationFallback.class, answer.getClass());
        assertEquals(returnTypeGenericMetadata, answer.returnTypeGenericMetadata);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mock = mock(Object.class);
        returnsDeepStubs.recordDeepStubAnswer(mock, container);
        assertEquals(mock, container.getAnswer().getReturnValue());
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        ReturnsDeepStubsInterface mockHandler = mock(ReturnsDeepStubsInterface.class);
        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);

        setupMockitoCoreMock();
        setupMockUtilMock(mock, mockSettings, mockHandler);
        setupGetTypeToMock(mockSettings);
        setupMockitoCoreActualParameterizedType(mockSettings, genericMetadata);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        assertEquals(genericMetadata, result);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallbackActualParameterizedType() {
        ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = new ReturnsDeepStubsSerializationFallback(mock(GenericMetadataSupport.class));
        Object mock = mock(Object.class);
        Object result = returnsDeepStubsSerializationFallback.actualParameterizedType(mock);
        assertEquals(mock, result);
    }

    private void setupMockitoCoreMock() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = mock(ReturnsDeepStubsSerializationFallback.class);
        LazyHolder.MOCKITO_CORE = mockitoCore;
        LazyHolder.DELEGATE = delegate;
    }

    private void setupDeepStub(InvocationContainerImpl container) throws Throwable {
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        InvocationForStubbing invocationForStubbing = mock(InvocationForStubbing.class);

        setupContainerGetStubbedInvocations(container, stubbedInvocationMatcher);
        setupContainerGetInvocationForStubbing(container, invocationForStubbing);
        setupContainerInvocationForStubbingMatches(invocationForStubbing, true);
        setupStubbedInvocationMatcherAnswer(stubbedInvocationMatcher, mock);
    }

    private void setupContainerGetStubbedInvocations(InvocationContainerImpl container, StubbedInvocationMatcher stubbedInvocationMatcher) {
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
    }

    private void setupContainerGetInvocationForStubbing(InvocationContainerImpl container, InvocationForStubbing invocationForStubbing) {
        when(container.getInvocationForStubbing()).thenReturn(invocationForStubbing);
    }

    private void setupContainerInvocationForStubbingMatches(InvocationForStubbing invocationForStubbing, boolean matches) {
        when(invocationForStubbing.matches(any(Invocation.class))).thenReturn(matches);
    }

    private void setupStubbedInvocationMatcherAnswer(StubbedInvocationMatcher stubbedInvocationMatcher, Object returnValue) throws Throwable {
        when(stubbedInvocationMatcher.answer(any(InvocationOnMock.class))).thenReturn(returnValue);
    }

    private void setupMockUtilMock(Object mock, CreationSettings mockSettings, ReturnsDeepStubsInterface handler) {
        MockUtil util = mock(MockUtil.class);
        when(util.getMockHandler(mock)).thenReturn(handler);
        when(handler.getMockSettings()).thenReturn(mockSettings);
    }

    private void setupGetTypeToMock(CreationSettings mockSettings) {
        TypeToMock typeToMock = mock(TypeToMock.class);
        when(mockSettings.getTypeToMock()).thenReturn(typeToMock);
    }

    private void setupMockitoCoreActualParameterizedType(CreationSettings mockSettings, GenericMetadataSupport genericMetadata) {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        when(LazyHolder.MOCKITO_CORE).thenReturn(mockitoCore);
        when(mockitoCore.actualParameterizedType(mockSettings.getTypeToMock())).thenReturn(genericMetadata);
    }

    private class ReturnsDeepStubsSerializationFallback extends ReturnsDeepStubs implements Serializable {
        private final GenericMetadataSupport returnTypeGenericMetadata;

        public ReturnsDeepStubsSerializationFallback(GenericMetadataSupport returnTypeGenericMetadata) {
            this.returnTypeGenericMetadata = returnTypeGenericMetadata;
        }

        @Override
        protected GenericMetadataSupport actualParameterizedType(Object mock) {
            return returnTypeGenericMetadata;
        }
    }

    private class DeeplyStubbedAnswer implements Answer<Object>, Serializable {
        private final Object mock;

        DeeplyStubbedAnswer(Object mock) {
            this.mock = mock;
        }

        public Object answer(InvocationOnMock invocation) throws Throwable {
            return mock;
        }
    }

    private interface ReturnsDeepStubsInterface extends ReturnsDeepStubs {
    }

    private class InvocationContainerImpl {
        private List<StubbedInvocationMatcher> stubbedInvocations;
        private InvocationForStubbing invocationForStubbing;
        private Answer<?> answer;

        public InvocationContainerImpl(List<StubbedInvocationMatcher> stubbedInvocations, InvocationForStubbing invocationForStubbing) {
            this.stubbedInvocations = stubbedInvocations;
            this.invocationForStubbing = invocationForStubbing;
        }

        public List<StubbedInvocationMatcher> getStubbedInvocations() {
            return stubbedInvocations;
        }

        public InvocationForStubbing getInvocationForStubbing() {
            return invocationForStubbing;
        }

        public Answer<?> getAnswer() {
            return answer;
        }

        public void addAnswer(Answer<?> answer, boolean matches) {
            this.answer = answer;
        }
    }

    private class StubbedInvocationMatcher {
        private Invocation invocation;

        public Invocation getInvocation() {
            return invocation;
        }

        public Object answer(InvocationOnMock invocation) throws Throwable {
            return invoke(invocation);
        }
    }

    private interface Invocation {
        boolean matches(Invocation invocation);
    }

    private class InvocationOnMock {
    }

    private class InvocationForStubbing implements Invocation {
        public boolean matches(Invocation invocation) {
            return true;
        }
    }

    private class MockitoCore {
        public boolean isTypeMockable(Class<?> type) {
            return true;
        }

        public <T> T mock(Class<T> type, MockSettings settings) {
            return null;
        }

        public GenericMetadataSupport actualParameterizedType(TypeToMock typeToMock) {
            return mock(GenericMetadataSupport.class);
        }
    }

    private class TypeToMock {
    }

    private class MockUtil {
        public ReturnsDeepStubsInterface getMockHandler(Object mock) {
            return mock(ReturnsDeepStubsInterface.class);
        }
    }

    private class CreationSettings {
        public TypeToMock getTypeToMock() {
            return mock(TypeToMock.class);
        }
    }

    private class GenericMetadataSupport {
        public Class<?> rawType() {
            return Object.class;
        }

        public boolean hasRawExtraInterfaces() {
            return true;
        }

        public Class<?>[] rawExtraInterfaces() {
            return new Class[]{};
        }

        public static GenericMetadataSupport inferFrom(TypeToMock typeToMock) {
            return mock(GenericMetadataSupport.class);
        }
    }

    private class ReturnsEmptyValues {
        public Object returnValueFor(Class<?> type) {
            return "returnValue";
        }
    }
}