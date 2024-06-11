import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_typicalCase() throws Throwable {
        // Typical case where the mock is able to return a valid answer
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        Object answer = returnsDeepStubs.answer(invocation);
        assertNotEquals(null, answer);
    }

    @Test
    public void testAnswer_nonMockableType() throws Throwable {
        // Case where the raw type is not mockable, delegate is expected to handle the answer
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("getClass"));
        Object answer = returnsDeepStubs.answer(invocation);
        assertNotNull(answer);
        assertEquals(Object.class, answer);
    }

    @Test
    public void testAnswer_nullRawType() throws Throwable {
        // Case where the raw type is null, delegate is expected to handle the answer
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(null);
        Object answer = returnsDeepStubs.answer(invocation);
        assertNotNull(answer);
        assertEquals(Object.class, answer);
    }

    @Test
    public void testDeepStub_matchingInvocation() throws Throwable {
        // Case where the deep stub mock is able to return a previously stubbed value
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        Object mock = mock(Object.class);
        when(mock.toString()).thenReturn("mocked toString");
        Object answer = returnsDeepStubs.answer(invocation);
        assertNotNull(answer);
        assertEquals("mocked toString", answer);
    }

    @Test
    public void testDeepStub_noMatchingInvocation() throws Throwable {
        // Case where there is no previously stubbed value for the invocation, a new deep stub mock is expected to be created
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        Object mock = mock(Object.class);
        when(mock.toString()).thenReturn("mocked toString");
        Object answer = returnsDeepStubs.answer(invocation);
        assertNotNull(answer);
        assertNotEquals("mocked toString", answer);
        verify(mockitoCore().getMockHandler(mock).getInvocationContainer()).addAnswer(any(), eq(false));
        verify(mockitoCore()).mock(any(), any());
    }
}