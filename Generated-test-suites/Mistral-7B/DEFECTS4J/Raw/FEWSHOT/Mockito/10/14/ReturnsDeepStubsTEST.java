package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import static org.mockito.internal.util.MockUtil.getMockHandler;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Class<?> rawType = returnTypeGenericMetadata.rawType();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(rawType, withSettings().defaultAnswer(returnsDeepStubs));

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = returnsDeepStubs.answer(invocation);

        InternalMockHandler<Object> handler = (InternalMockHandler<Object>) getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        assertSame(mock, result);
        assertSame(container, handler.getInvocationContainer());
    }

    @Test
    public void testDeepStub() throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Class<?> rawType = returnTypeGenericMetadata.rawType();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        MockSettings mockSettings = withSettings().defaultAnswer(returnsDeepStubs);
        Object mock = mock(rawType, mockSettings);

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = returnsDeepStubs.answer(invocation);

        InternalMockHandler<Object> handler = (InternalMockHandler<Object>) getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(invocation);
        container.addAnswer(new DeeplyStubbedAnswer(mock), false);

        assertSame(mock, result);
        assertSame(stubbedInvocationMatcher.answer(invocation), result);
    }

    @Test
    public void testNewDeepStubMock() throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Class<?> rawType = returnTypeGenericMetadata.rawType();

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

        assertSame(mockitoCore().mock(rawType), mock);
    }

    @Test
    public void testWithSettingsUsing() throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        MockSettings mockSettings = new ReturnsDeepStubs().withSettingsUsing(returnTypeGenericMetadata);
        Class<?> rawType = returnTypeGenericMetadata.rawType();

        Object mock = mockitoCore().mock(rawType, mockSettings);

        assertSame(mockitoCore().mock(rawType, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS)), mock);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        Object mock = newReturnsDeepStubsAnswer(returnTypeGenericMetadata).newDeepStubMock(returnTypeGenericMetadata);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);

        assertSame(mock, result);
        assertSame(container, getMockHandler(mock).getInvocationContainer());
        assertTrue(container.hasAnswer(new DeeplyStubbedAnswer(mock)));
    }

    private ReturnsDeepStubs newReturnsDeepStubsAnswer(final GenericMetadataSupport returnTypeGenericMetadata) {
        return new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata) {
            @Override
            protected GenericMetadataSupport actualParameterizedType(Object mock) {
                return returnTypeGenericMetadata;
            }
        };
    }
}