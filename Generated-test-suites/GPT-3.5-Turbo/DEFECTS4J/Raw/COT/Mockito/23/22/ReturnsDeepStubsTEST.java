
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
  
        // Test case for returnTypeGenericMetadata is not mockable
        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        when(invocation1.getMock()).thenReturn(new Object());
        Object result1 = returnsDeepStubs.answer(invocation1);
        // assert result is the same as delegate's returnValueFor

        // Test case for matching stubbedInvocationMatcher
        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        when(invocation2.getMock()).thenReturn(new Object());
        when(invocation2.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);
        when(mockitoCore.isTypeMockable(Object.class)).thenReturn(true);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(new InvocationContainerImpl());
        when(handler.getInvocationContainer().getStubbedInvocations()).thenReturn(Arrays.asList(new StubbedInvocationMatcher(invocation2), new StubbedInvocationMatcher(invocation2)));
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(invocation2.getMock())).thenReturn(handler);
        when(mockitoCore.mock(Object.class, withSettings().defaultAnswer(returnsDeepStubs))).thenReturn(new Object());
        when(mockitoCore).thenReturn(mockitoCore);
        when(mockitoCore.isTypeMockable(Object.class)).thenReturn(false);
        when(mockitoCore.isTypeMockable(Object.class)).thenReturn(true);
        when(mockitoCore.mock(Object.class, withSettings().defaultAnswer(returnsDeepStubs))).thenReturn(new Object());
        Object result2 = returnsDeepStubs.answer(invocation2);
        // assert result is the same as answer from stubbedInvocationMatcher

        // Test case for recordDeepStubMock
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        when(delegate.returnValueFor(Object.class)).thenReturn(new Object());
        ReturnsDeepStubs returnsDeepStubs3 = mock(ReturnsDeepStubs.class, RETURNS_DEEP_STUBS);
        GenericMetadataSupport returnTypeGenericMetadata3 = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata3.rawType()).thenReturn(Object.class);
        MockitoCore mockitoCore3 = mock(MockitoCore.class);
        InternalMockHandler<Object> handler3 = mock(InternalMockHandler.class);
        InvocationContainerImpl container3 = mock(InvocationContainerImpl.class);
        when(container3.getStubbedInvocations()).thenReturn(Arrays.asList(new StubbedInvocationMatcher(invocation2), new StubbedInvocationMatcher(invocation2)));
        when(handler3.getInvocationContainer()).thenReturn(container3);
        when(mockitoCore3.mock(Object.class, withSettings().defaultAnswer(returnsDeepStubs))).thenReturn(new Object());
        when(mockitoCore3.isTypeMockable(Object.class)).thenReturn(false);
        when(mockitoCore3.isTypeMockable(Object.class)).thenReturn(true);
        when(mockitoCore3.mock(Object.class, withSettings().defaultAnswer(returnsDeepStubs))).thenReturn(new Object());
        when(mockitoCore, atLeastOnce()).thenReturn(mockitoCore3);
        when(handler3.getInvocationContainer()).thenReturn(container3);
        when(mockUtil.getMockHandler(invocation2.getMock())).thenReturn(handler3);
        when(returnTypeGenericMetadata3.rawType()).thenReturn(Object.class);
        Object mock3 = new Object();
        when(container3.addAnswer(eq(returnsDeepStubs3), eq(false))).thenReturn(mock3);
        Object result3 = returnsDeepStubs3.answer(invocation2);
        // assert result is the same as mock3
    }

}
