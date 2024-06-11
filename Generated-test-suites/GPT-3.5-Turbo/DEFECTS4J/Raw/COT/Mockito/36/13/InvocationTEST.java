import org.junit.Test;
import java.util.Arrays;

public class InvocationTest {

    @Test
    public void testGetMock() {
        // Test case for the getMock() method
        Invocation invocation = new Invocation(null, null, null, 0, null);
        // Test the return value of the method
        assert(invocation.getMock() == null);
    }

    @Test
    public void testGetMethod() {
        // Test case for the getMethod() method
        Invocation invocation = new Invocation(null, MockitoMethod.create("methodName"), null, 0, null);
        // Test the return value of the method
        assert(invocation.getMethod().getName().equals("methodName"));
    }

    @Test
    public void testGetArguments() {
        // Test case for the getArguments() method
        Invocation invocation = new Invocation(null, null, new Object[]{"arg1", "arg2"}, 0, null);
        // Test the return value of the method
        assert(Arrays.equals(invocation.getArguments(), new Object[]{"arg1", "arg2"}));
    }

    @Test
    public void testIsVerified() {
        // Test case for the isVerified() method
        Invocation invocation = new Invocation(null, null, null, 0, null);
        // Test the default value of the method
        assert(!invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        // Test case for the getSequenceNumber() method
        Invocation invocation = new Invocation(null, null, null, 5, null);
        // Test the return value of the method
        assert(invocation.getSequenceNumber() == 5);
    }

    @Test
    public void testIsVerifiedInOrder() {
        // Test case for the isVerifiedInOrder() method
        Invocation invocation = new Invocation(null, null, null, 0, null);
        // Test the default value of the method
        assert(!invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        // Test case for the equals() method
        Invocation invocation1 = new Invocation(null, null, null, 0, null);
        Invocation invocation2 = new Invocation(null, null, null, 0, null);
        // Test when the invocations are equal
        assert(invocation1.equals(invocation2));
    }

    @Test
    public void testToString() {
        // Test case for the toString() method
        Invocation invocation = new Invocation(null, null, null, 0, null);
        // Test the format of the returned string
        assert(invocation.toString().matches(".*\\.\\(Invocation@\\w+\\)"));
    }

    @Test
    public void testToStringWithMatchers() {
        // Test case for the toString() method with matchers
        Invocation invocation = new Invocation(null, null, new Object[]{Matchers.any(String.class), Matchers.anyInt()}, 0, null);
        // Test the format of the returned string
        assert(invocation.toString().matches(".*\\.\\(Invocation@\\w+\\)"));
    }

    @Test
    public void testIsToString() {
        // Test case for the isToString() method
        Invocation invocation = new Invocation(null, MockitoMethod.create("toString"), null, 0, null);
        // Test when the method is toString()
        assert(invocation.isToString());
    }

    @Test
    public void testIsValidException() {
        // Test case for the isValidException() method
        Invocation invocation = new Invocation(null, MockitoMethod.create("methodName"), null, 0, null);
        // Test when the exception is valid
        assert(invocation.isValidException(new Exception()));
    }

    @Test
    public void testIsValidReturnType() {
        // Test case for the isValidReturnType() method
        Invocation invocation = new Invocation(null, MockitoMethod.create("methodName", String.class), null, 0, null);
        // Test when the return type is valid
        assert(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        // Test case for the isVoid() method
        Invocation invocation = new Invocation(null, MockitoMethod.create("methodName", Void.TYPE), null, 0, null);
        // Test when the return type is void
        assert(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        // Test case for the printMethodReturnType() method
        Invocation invocation = new Invocation(null, MockitoMethod.create("methodName", String.class), null, 0, null);
        // Test the return value of the method
        assert(invocation.printMethodReturnType().equals("String"));
    }

    @Test
    public void testGetMethodName() {
        // Test case for the getMethodName() method
        Invocation invocation = new Invocation(null, MockitoMethod.create("methodName"), null, 0, null);
        // Test the return value of the method
        assert(invocation.getMethodName().equals("methodName"));
    }

    @Test
    public void testReturnsPrimitive() {
        // Test case for the returnsPrimitive() method
        Invocation invocation1 = new Invocation(null, MockitoMethod.create("methodName", int.class), null, 0, null);
        Invocation invocation2 = new Invocation(null, MockitoMethod.create("methodName", Integer.class), null, 0, null);
        // Test when the return type is primitive
        assert(invocation1.returnsPrimitive());
        // Test when the return type is not primitive
        assert(!invocation2.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        // Test case for the getLocation() method
        Invocation invocation = new Invocation(null, null, null, 0, null);
        // Test the return value of the method
        assert(invocation.getLocation() instanceof Location);
    }

    @Test
    public void testGetArgumentsCount() {
        // Test case for the getArgumentsCount() method
        Invocation invocation = new Invocation(null, null, new Object[]{1, 2, 3}, 0, null);
        // Test the return value of the method
        assert(invocation.getArgumentsCount() == 3);
    }

    @Test
    public void testGetRawArguments() {
        // Test case for the getRawArguments() method
        Invocation invocation = new Invocation(null, null, new Object[]{1, 2, 3}, 0, null);
        // Test the return value of the method
        assert(Arrays.equals(invocation.getRawArguments(), new Object[]{1, 2, 3}));
    }

    @Test
    public void testCallRealMethod() {
        // Test case for the callRealMethod() method
        Invocation invocation = new Invocation(null, null, null, 0, new RealMethod() {
            public Object invoke(Object mock, Object[] args) throws Throwable {
                return "RealMethod";
            }
        });
        // Test the return value of the method
        try {
            assert(invocation.callRealMethod().equals("RealMethod"));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}