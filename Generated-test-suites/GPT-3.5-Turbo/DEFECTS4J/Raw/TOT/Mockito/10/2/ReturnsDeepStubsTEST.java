import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;

import java.util.*;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_WithNonMockableReturnType_ShouldReturnValueFromDelegate() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(new Object());
        
        Object result = returnsDeepStubs.answer(invocation);

        // Assertion
        assertSame(returnsDeepStubs.delegate().returnValueFor(Object.class), result);
    }

    @Test
    public void testAnswer_WithMockableReturnTypeAndInvocationMatchesExistingStubbedInvocations_ShouldReturnValueFromStubbedInvocation() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Collections.emptyList().getClass().getMethod("iterator"));
        when(invocation.getMock()).thenReturn(mock(List.class));
        
        List<Integer> list = new ArrayList<>();
        list.add(1);
        when(invocation.getMock().entrySet()).thenReturn(list);

        Object result = returnsDeepStubs.answer(invocation);

        // Assertion
        assertSame(1, result);
    }

    @Test
    public void testAnswer_WithMockableReturnTypeAndInvocationDoesNotMatchExistingStubbedInvocations_ShouldCreateNewDeepStubMockAndRecordAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Collections.emptyList().getClass().getMethod("iterator"));
        when(invocation.getMock()).thenReturn(mock(List.class));
        
        List<Integer> list = new ArrayList<>();
        when(invocation.getMock().entrySet()).thenReturn(list);

        Object result = returnsDeepStubs.answer(invocation);

        // Assertion
        assertSame(list, result);
        verify(invocation.getMock(), times(1)).entrySet();
        verify(invocation.getMock(), never()).matches(any(InvocationOnMock.class));
    }
}