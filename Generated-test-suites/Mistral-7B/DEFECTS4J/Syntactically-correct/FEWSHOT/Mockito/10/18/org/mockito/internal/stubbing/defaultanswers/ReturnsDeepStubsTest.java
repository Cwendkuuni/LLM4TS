package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Answer;

import java.util.List;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_ReturnsSameInstanceForSameInvocation() {
        GenericMetadataSupport returnTypeGenericMetadata = GenericMetadataSupport.forClass(List.class);
        Object mock = mock(List.class, new ReturnsDeepStubs(), withSettings().serializable());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setTarget(mock);
        invocation.setMethod(List.class.getMethod("size"));

        Object answer1 = new ReturnsDeepStubs().answer(invocation);
        Object answer2 = new ReturnsDeepStubs().answer(invocation);

        assertSame(answer1, answer2);
    }

    @Test
    public void testDeepStub_ReturnsRecordedStubForSameInvocation() {
        Object mock = mock(Object.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setTarget(mock);
        invocation.setMethod(Object.class.getMethod("toString"));
        container.addStubbedInvocation(Mockito.any(StubbedInvocationMatcher.class));

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object answer = returnsDeepStubs.deepStub(invocation, GenericMetadataSupport.forClass(Object.class));

        assertSame(mock, answer);
    }

    @Test
    public void testNewDeepStubMock_ReturnsNewMockWithContextualGenerics() {
        GenericMetadataSupport returnTypeGenericMetadata = GenericMetadataSupport.forClass(List.class);
        Object mock = spy(mock(List.class));
        MockSettings mockSettings = withSettingsUsing(returnTypeGenericMetadata);
        Object newMock = mockitoCore().mock(List.class, mockSettings);

        assertSame(mock, newMock);
        assertSame(returnTypeGenericMetadata, newMock.getClass().getGenericInterfaces()[0]);
    }

    @Test(expected = IllegalStateException.class)
    public void testAnswer_ThrowsExceptionForNonMockableType() {
        GenericMetadataSupport returnTypeGenericMetadata = GenericMetadataSupport.forClass(IOException.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(IOException.class.getMethod("close"));

        returnsDeepStubs.answer(invocation);
    }

    private MockitoCore mockitoCore() {
        return Mockito.mock(MockitoCore.class);
    }

    private CreationSettings withSettingsUsing(GenericMetadataSupport returnTypeGenericMetadata) {
        return withSettings().serializable().defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    private ReturnsDeepStubs returnsDeepStubsAnswerUsing(final GenericMetadataSupport returnTypeGenericMetadata) {
        return new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
    }
}