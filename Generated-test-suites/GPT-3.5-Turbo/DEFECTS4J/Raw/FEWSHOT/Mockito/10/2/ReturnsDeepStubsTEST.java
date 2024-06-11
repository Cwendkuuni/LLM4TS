import org.mockito.internal.stubbing.defaultanswers.*;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.Serializable;

import static org.mockito.Mockito.withSettings;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubsAnswer = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        // Test mockitoCore() called
        MockitoCore mockitoCore = Mockito.mock(MockitoCore.class);
        Mockito.when(LazyHolder.MOCKITO_CORE).thenReturn(mockitoCore);

        // Test actualParameterizedType() called
        CreationSettings mockSettings = Mockito.mock(CreationSettings.class);
        Mockito.when(Mockito.mockHandler(invocation.getMock())).thenReturn(Mockito.mock(InternalMockHandler.class));
        Mockito.when(Mockito.mockHandler(invocation.getMock()).getMockSettings()).thenReturn(mockSettings);
        Mockito.when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        Mockito.when(GenericMetadataSupport.inferFrom(Object.class)).thenReturn(returnTypeGenericMetadata);

        // Test deepStub() called
        InternalMockHandler<Object> mockHandler = Mockito.mock(InternalMockHandler.class);
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        Mockito.when(Mockito.mockHandler(invocation.getMock())).thenReturn(mockHandler);
        Mockito.when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(mockHandler);
        Mockito.when(mockHandler.getInvocationContainer()).thenReturn(Mockito.mock(InvocationContainerImpl.class));

        // Test recordDeepStubAnswer() called
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        Mockito.when(mockHandler.getInvocationContainer()).thenReturn(container);
        Mockito.when(container.getStubbedInvocations()).thenReturn(new ArrayList<>());
        ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = Mockito.mock(ReturnsDeepStubsSerializationFallback.class);
        Mockito.when(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)).thenReturn(returnsDeepStubsSerializationFallback);
        Mockito.when(newDeepStubMock(returnTypeGenericMetadata)).thenReturn(returnsDeepStubsSerializationFallback);

        // Test answer() method
        Object result = returnsDeepStubsAnswer.answer(invocation);
        
        // Assertions
        assertEquals(returnsDeepStubsSerializationFallback, result);
        // Verify mockitoCore() called
        Mockito.verify(LazyHolder.MOCKITO_CORE, Mockito.times(1)).isTypeMockable(Object.class);
        // Verify actualParameterizedType() called
        Mockito.verify(GenericMetadataSupport, Mockito.times(1)).inferFrom(Object.class);
        // Verify deepStub() called
        Mockito.verify(mockUtil, Mockito.times(1)).getMockHandler(invocation.getMock());
        Mockito.verify(mockHandler, Mockito.times(1)).getInvocationContainer();
        Mockito.verify(container, Mockito.times(1)).addAnswer(Mockito.any(DeeplyStubbedAnswer.class), Mockito.eq(false));
        // Verify recordDeepStubAnswer() called
        Mockito.verify(returnsDeepStubsSerializationFallback, Mockito.times(1)).returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        Mockito.verify(returnsDeepStubsSerializationFallback, Mockito.times(1)).newDeepStubMock(returnTypeGenericMetadata);
    }
}