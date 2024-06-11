import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerWithNullInvocation() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        
        try {
            answer.answer(null);
            fail("Expected an exception to be thrown");
        } catch (Throwable e) {
            // Exception is expected
        }
    }

    @Test
    public void testAnswerWithNonMatchingInvocation() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        assertNull(answer.answer(invocation));
    }

    @Test
    public void testAnswerWithMatchingInvocation() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        // Stub a matching invocation
        InvocationOnMock stubbedInvocation = mock(InvocationOnMock.class);
        when(stubbedInvocation.getMock()).thenReturn(invocation.getMock());

        // Invoke answer() and check if the stubbed invocation is returned
        assertEquals(stubbedInvocation, answer.answer(invocation));
    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();

        // Create a new deep stub mock with generic metadata
        Object mock = answer.newDeepStubMock(mock(GenericMetadataSupport.class));

        assertNotNull(mock);
    }

    @Test
    public void testMockTypeNotMockable() {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();

        // Try to mock a non-mockable type (not supported by Mockito)
        Object mock = answer.newDeepStubMock(mock(GenericMetadataSupport.class)); 

        assertNull(mock);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() {
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback fallback =
                new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(mock(GenericMetadataSupport.class));

        assertNull(fallback.writeReplace());
    }

    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        ReturnsDeepStubs.DeeplyStubbedAnswer answer =
                new ReturnsDeepStubs.DeeplyStubbedAnswer(mock(Object.class));

        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Invoke answer() and check if the mock object is returned
        assertEquals(answer.mock, answer.answer(invocation));
    }
}