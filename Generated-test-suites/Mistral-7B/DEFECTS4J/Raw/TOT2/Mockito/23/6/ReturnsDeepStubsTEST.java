import org.junit.Test;
import org.mockito.Answer;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoJUnit.verifyZeroInteractions;
import static org.mockito.MockitoJUnit.verifyNoMoreInteractions;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.withSettings;
import static org.mockito.Mockito.withSettingsUsing;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(mock.answer(invocation)).thenReturn("testString");

        Object result = mock.answer(invocation);

        verify(mock).answer(invocation);
        verify(mock).answer(eq(invocation));
        verify(mock).answer(same(invocation));
        verifyZeroInteractions(invocation);
        verifyNoMoreInteractions(invocation);

        assertEquals("testString", result);
    }

    @Test
    public void testNullArguments() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Test with null invocation
        when(mock.answer(null)).thenThrow(new NullPointerException("Null invocation"));

        assertThrows(NullPointerException.class, () -> mock.answer(invocation));

        // Test with null return type generic metadata
        GenericMetadataSupport returnTypeGenericMetadata = null;
        when(mock.answer(invocation)).thenThrow(new NullPointerException("Null return type generic metadata"));

        assertThrows(NullPointerException.class, () -> mock.answer(invocation));
    }

    @Test
    public void testNonMatchingTypes() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(mock.answer(invocation)).thenThrow(new ClassCastException("Cannot cast to expected type"));

        assertThrows(ClassCastException.class, () -> mock.answer(invocation));
    }

    @Test
    public void testMockCreationFailure() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(mock.getMock(invocation, null)).thenThrow(new IllegalStateException("Mock creation failed"));

        assertThrows(IllegalStateException.class, () -> mock.answer(invocation));
    }

    @Test
    public void testExceptionHandling() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(mock.answer(invocation)).thenThrow(new IOException("Test exception"));

        assertThrows(IOException.class, () -> mock.answer(invocation));
    }
}

//