
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Mocking and stubbing the required objects
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(invocation.getMock()).thenReturn(genericMetadataSupport);
        when(genericMetadataSupport.rawType()).thenReturn(Object.class);
        when(genericMetadataSupport.resolveGenericReturnType(invocation.getMethod())).thenReturn(genericMetadataSupport);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());
        when(container.getInvocationForStubbing()).thenReturn(invocation);

        // Test for type that is not mockable
        Object result = returnsDeepStubs.answer(invocation);
        assertNull(result);

        // Test for deep stub scenario
        when(genericMetadataSupport.rawType()).thenReturn(String.class);
        Object mock = mock(Object.class);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(new StubbedInvocationMatcher(invocation, null, null)));

        result = returnsDeepStubs.answer(invocation);
        assertEquals(mock, result);

        verify(container, times(2)).getStubbedInvocations();
        verify(container).addAnswer(any(), eq(false));
    }
}
