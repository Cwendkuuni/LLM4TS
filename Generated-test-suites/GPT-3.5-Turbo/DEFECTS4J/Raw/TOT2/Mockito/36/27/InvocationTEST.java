import static org.junit.Assert.*;
import org.junit.Test;

public class InvocationTest {

    @Test
    public void testConstructor() {
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", null, null);
        Object[] args = {"arg1", "arg2"};
        RealMethod realMethod = new RealMethod();

        Invocation invocation = new Invocation(new Object(), mockitoMethod, args, 1, realMethod);

        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(args, invocation.getArguments());
        assertEquals(false, invocation.isVerified());
        assertEquals(1, (int) invocation.getSequenceNumber());
        assertEquals(false, invocation.isVerifiedInOrder());
        assertEquals(realMethod, invocation.realMethod);
    }

    @Test
    public void testEquals() {
        MockitoMethod mockitoMethod1 = new MockitoMethod("testMethod1", null, null);
        Object[] args1 = {"arg1", "arg2"};
        RealMethod realMethod1 = new RealMethod();

        Invocation invocation1 = new Invocation(new Object(), mockitoMethod1, args1, 1, realMethod1);

        MockitoMethod mockitoMethod2 = new MockitoMethod("testMethod2", null, null);
        Object[] args2 = {"arg1", "arg2"};
        RealMethod realMethod2 = new RealMethod();

        Invocation invocation2 = new Invocation(new Object(), mockitoMethod2, args2, 1, realMethod2);

        assertFalse(invocation1.equals(invocation2));
    }

    @Test
    public void testToString() {
        MockitoMethod mockitoMethod = new MockitoMethod("testMethod", null, null);
        Object[] args = {"arg1", "arg2"};
        RealMethod realMethod = new RealMethod();

        Invocation invocation = new Invocation(new Object(), mockitoMethod, args, 1, realMethod);

        String expected = "mockitoMethod.testMethod(arg1, arg2)";
        assertEquals(expected, invocation.toString());
    }

    // Additional test cases for other methods in the Invocation class can be added here

}