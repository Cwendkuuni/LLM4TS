package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Answer;
import org.mockito.InvocationOnMock;
import org.mockito.MockitoCore;
import org.mockito.MockSettings;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.GenericMetadataSupport;
import org.mockito.internal.util.reflection.GenericMetadataSupport.InferredType;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.AnswerWith;

import java.lang.reflect.GenericDeclaration;
import java.util.Arrays;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() {
        MockitoCore mockitoCore = MockitoCore.getInstance();
        Class<?> rawType = String[].class;
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(InferredType.from(String[].class));
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Test with a mockable type
        Object mock1 = mock(rawType, withSettings().defaultAnswer(returnsDeepStubs));
        assertEquals(mock1, returnsDeepStubs.answer((InvocationOnMock) mock(Invocation.class).withCallRealMethod()));

        // Test with a non-mockable type
        rawType = IOException.class;
        Object mock2 = mock(rawType, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
        assertNotNull(mock2);
        assertEquals(mockitoCore().isTypeMockable(rawType), false);

        // Test deep stubbing
        Object container = new InvocationContainerImpl(null, null, null);
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        container.addAnswer(stubbedInvocationMatcher, false);
        ReturnsDeepStubs deepStubsAnswer = returnsDeepStubs.newDeepStubsAnswerUsing(returnTypeGenericMetadata);
        MockSettings mockSettings = withSettings().defaultAnswer(deepStubsAnswer);
        Object mock3 = mock(String[].class, mockSettings);
        Invocation invocation = mock(Invocation.class);
        stubbedInvocationMatcher.answer(invocationMock);
        assertEquals(mock3, returnsDeepStubs.answer(invocationMock));
        assertEquals(mock3, returnsDeepStubs.answer(invocation));

        // Test deep stubbing with nested types
        rawType = MyNestedClass[].class;
        returnTypeGenericMetadata = new GenericMetadataSupport(InferredType.from(MyNestedClass[].class));
        MyNestedClass nestedMock = mock(MyNestedClass.class, withSettings().defaultAnswer(Answers.RETURNS_DEEP_STUBS));
        MyNestedClass nestedDeepStub = mock(MyNestedClass.class, withSettings().defaultAnswer(deepStubsAnswer));
        Object deepMock = mock(MyClass.class, withSettings()
                .serializable()
                .defaultAnswer(new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata)));
        Object deepStubContainer = new InvocationContainerImpl(null, null, null);
        deepStubContainer.addAnswer(new DeeplyStubbedAnswer(nestedDeepStub), false);
        Object deepStub = deepStubsAnswer.deepStub(invocationMock, returnTypeGenericMetadata);
        assertEquals(nestedDeepStub, deepStub);
        assertEquals(nestedMock, deepStubContainer.getMock());
        assertEquals(deepStubContainer, deepMock.getMockHandler().getInvocationContainer());
    }

    @Test
    public void testWriteReplace() {
        MockitoCore mockitoCore = MockitoCore.getInstance();
        Class<?> rawType = String[].class;
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(InferredType.from(String[].class));
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(rawType, withSettings().defaultAnswer(returnsDeepStubs));
        assertEquals(mockitoCore.RETURNS_DEEP_STUBS, returnsDeepStubs.recordDeepStubAnswer(mock, null).writeReplace());
    }

    static class MyClass {
        MyNestedClass nested;
    }

    static class MyNestedClass {
    }

    static class DeeplyStubbedAnswer implements Answer<Object> {
        private final Object mock;

        DeeplyStubbedAnswer(Object mock) {
            this.mock = mock;
        }

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            return mock;
        }
    }
}