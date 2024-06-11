package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.withSettings;
import static org.mockito.internal.stubbing.Answer.returnsDeepStubs;

import org.junit.Test;
import org.mockito.Answer;
import org.mockito.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;

import java.io.Serializable;

public class ReturnsDeepStubsTest {

    @Test
    public void testDeepStubs() throws Exception {
        MockitoCore mockitoCore = MockitoCore.getInstance();

        interface MyInterface<T> {
            T getValue();
        }

        MyInterface<String> mockInterface = mock(MyInterface.class, new ReturnsDeepStubs());
        MyInterface<Integer> mockAnotherInterface = mock(MyInterface.class, withSettings().serializable().defaultAnswer(returnsDeepStubs()));

        assertSame(mockInterface, mockInterface.getValue().getValue());
        assertSame(mockAnotherInterface, mockAnotherInterface.getValue().getValue());

        CreationSettings creationSettings = (CreationSettings) mockitoCore.getMockHandler(mockInterface).getMockSettings();
        GenericMetadataSupport returnTypeGenericMetadata = GenericMetadataSupport.inferFrom(creationSettings.getTypeToMock());
        assertSame(returnTypeGenericMetadata.rawType(), mockInterface.getClass().getInterfaces()[0]);
        assertSame(mockInterface, deepStub(mockInterface, returnTypeGenericMetadata));

        InvocationContainerImpl container = (InvocationContainerImpl) mockitoCore.getMockHandler(mockInterface).getInvocationContainer();
        StubbedInvocationMatcher stubbedInvocationMatcher = container.getStubbedInvocations()[0];
        assertSame(mockInterface, stubbedInvocationMatcher.answer(new InvocationOnMock(mockInterface, new Object[0])));
    }

    private Object deepStub(Object object, GenericMetadataSupport returnTypeGenericMetadata) throws Exception {
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(object);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        for (StubbedInvocationMatcher stubbedInvocationMatcher : container.getStubbedInvocations()) {
            if (container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())) {
                return stubbedInvocationMatcher.answer(new InvocationOnMock(object, new Object[0]));
            }
        }

        Object deepStubMock = newDeepStubMock(returnTypeGenericMetadata);
        container.addAnswer(new DeeplyStubbedAnswer(deepStubMock), false);
        return deepStubMock;
    }

    private Object newDeepStubMock(GenericMetadataSupport returnTypeGenericMetadata) {
        return mockitoCore().mock(
                returnTypeGenericMetadata.rawType(),
                withSettingsUsing(returnTypeGenericMetadata)
        );
    }

    private MockSettings withSettingsUsing(GenericMetadataSupport returnTypeGenericMetadata) {
        MockSettings mockSettings = returnTypeGenericMetadata.hasRawExtraInterfaces() ?
                withSettings().extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces()) : withSettings();

        return mockSettings.serializable()
                .defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    private ReturnsDeepStubs returnsDeepStubsAnswerUsing(final GenericMetadataSupport returnTypeGenericMetadata) {
        return new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
    }
}