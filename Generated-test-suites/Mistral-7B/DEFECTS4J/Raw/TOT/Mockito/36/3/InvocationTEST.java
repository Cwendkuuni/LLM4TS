import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class InvocationTest {

    private Invocation mockInvocation;
    private Object mockObject;

    @Before
    public void setup() {
        mockObject = Mockito.mock(String.class);
    }

    @Test
    public void testValidInvocation() {
        MockitoMethod method = Mockito.method(String.class, "length");
        RealMethod realMethod = new RealMethod(method, String.class, new Object[]{}, 0);
        mockInvocation = new Invocation(mockObject, method, new Object[0], 1, realMethod);

        String result = mockObject.length();
        verify(mockObject).length();
        assertEquals(result.length(), mockInvocation.getArguments()[0].toString().length());
    }

    @Test
    public void testInvocationWithArguments() {
        MockitoMethod method = Mockito.method(String.class, "concat", String.class, String.class);
        Object[] arguments = new Object[]{"hello", "world"};
        RealMethod realMethod = new RealMethod(method, String.class, arguments, 1);

        mockInvocation = new Invocation(mockObject, method, arguments, 2, realMethod);
        String result = mockObject.concat("hello", "world");
        verify(mockObject).concat(ArgumentMatchers.eq("hello"), ArgumentMatchers.eq("world"));
        assertEquals(result, "helloworld");
    }

    @Test
    public void testInvocationWithVarArgs() {
        MockitoMethod method = Mockito.method(String.class, "concat", String.class, String[].class);
        Object[] arguments = new Object[]{"hello", new Object[]{"world", "!"}};
        RealMethod realMethod = new RealMethod(method, String.class, arguments, 1);

        mockInvocation = new Invocation(mockObject, method, arguments, 2, realMethod);
        String result = mockObject.concat(Arrays.asList("hello", "world", "!").toArray(new String[0]));
        verify(mockObject).concat(ArgumentMatchers.arrayContaining(Collections.singletonList("world")));
        assertEquals(result, "helloworld!");
    }

    @Test
    public void testVerifiedInvocation() {
        doReturn("mock").when(mockObject).length();
        String result = mockObject.length();
        Invocation invocation = new Invocation(mockObject, Mockito.method(String.class, "length"), new Object[0], 1, null);
        invocation.markVerified();
        verify(mockObject).length();
        assertEquals("mock", result);
    }

    @Test(expected = AssertionError.class)
    public void testNotVerifiedInvocation() {
        String result = mockObject.length();
        Invocation invocation = new Invocation(mockObject, Mockito.method(String.class, "length"), new Object[0], 1, null);
        assertEquals(result.length(), 5); // this assertion will fail because invocation is not verified
        verify(mockObject).length();
    }

    @Test
    public void testExceptionThrown() {
        doThrow(new RuntimeException("Unexpected error")).when(mockObject).length();
        try {
            String result = mockObject.length();
            fail("Exception expected");
        } catch (RuntimeException e) {
            Invocation invocation = new Invocation(mockObject, Mockito.method(String.class, "length"), new Object[0], 1, null);
            assertEquals("Unexpected error", e.getMessage());
            verify(mockObject).length();
        }
    }

    @Test
    public void testReturnType() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return 42;
            }
        }).when(mockObject).length();
        Invocation invocation = new Invocation(mockObject, Mockito.method(String.class, "length"), new Object[0], 1, null);
        assertEquals(42, invocation.callRealMethod());
    }
}