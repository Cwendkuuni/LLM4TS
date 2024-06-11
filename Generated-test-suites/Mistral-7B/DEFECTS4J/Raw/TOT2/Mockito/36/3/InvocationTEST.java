import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.mock.MockitoMock;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class InvocationTest {

    @Test
    public void testInvocationWithValidArguments() {
        Object mock = new MockitoMock(SomeClass.class);
        MockitoMethod mockitoMethod = Mockito.method(SomeClass.class, "someMethod", new Class[]{Integer.class, String.class});
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{1, "test"}, 1, mockitoMethod.getRealMethod());

        when(mock.someMethod(1, "test")).thenReturn("result");

        String result = (String) invocation.callRealMethod();

        verify(mock).someMethod(1, "test");
        verify(mock).someMethod(eq(1), eq("test"));
        verifyNoMoreInteractions(mock);

        assertEquals("result", result);
    }

    @Test
    public void testInvocationWithNullMock() {
        MockitoMethod mockitoMethod = Mockito.method(SomeClass.class, "someMethod", new Class[]{Integer.class, String.class});
        Invocation invocation = new Invocation(null, mockitoMethod, new Object[]{1, "test"}, 1, mockitoMethod.getRealMethod());

        try {
            invocation.callRealMethod();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    // Add more test cases for other edge cases and exceptions
}