import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationTest {

    @Test
    public void testGetMock() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation object using the mock object
        Invocation invocation = new Invocation(mockObject, MockitoMethod.GET_MOCK, null, 1, null);

        // Verify that the getMock() method returns the mock object
        assertEquals(mockObject, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation object using the mock object
        Invocation invocation = new Invocation(mockObject, MockitoMethod.GET_METHOD, null, 1, null);

        // Verify that the getMethod() method returns the correct method
        assertEquals(MockitoMethod.GET_METHOD, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create arguments array
        Object[] args = {1, "test"};

        // Create Invocation object using the mock object and arguments
        Invocation invocation = new Invocation(mockObject, MockitoMethod.GET_ARGUMENTS, args, 1, null);

        // Verify that the getArguments() method returns the correct arguments
        assertArrayEquals(args, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation object using the mock object
        Invocation invocation = new Invocation(mockObject, MockitoMethod.IS_VERIFIED, null, 1, null);

        // Verify that the isVerified() method initially returns false
        assertFalse(invocation.isVerified());

        // Call markVerified() to mark the invocation as verified
        invocation.markVerified();

        // Verify that the isVerified() method now returns true
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation object using the mock object
        Invocation invocation = new Invocation(mockObject, MockitoMethod.GET_SEQUENCE_NUMBER, null, 1, null);

        // Verify that the getSequenceNumber() method returns the correct sequence number
        assertEquals((Integer) 1, invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation object using the mock object
        Invocation invocation = new Invocation(mockObject, MockitoMethod.IS_VERIFIED_IN_ORDER, null, 1, null);

        // Verify that the isVerifiedInOrder() method initially returns false
        assertFalse(invocation.isVerifiedInOrder());

        // Call markVerifiedInOrder() to mark the invocation as verified in order
        invocation.markVerifiedInOrder();

        // Verify that the isVerifiedInOrder() method now returns true
        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation objects using the mock object and same method with same arguments
        Invocation invocation1 = new Invocation(mockObject, MockitoMethod.EQUALS, null, 1, null);
        Invocation invocation2 = new Invocation(mockObject, MockitoMethod.EQUALS, null, 2, null);

        // Verify that the equals() method returns true for objects with same mock, method, and arguments
        assertTrue(invocation1.equals(invocation1));

        // Verify that the equals() method returns false for objects with different sequence numbers
        assertFalse(invocation1.equals(invocation2));
    }

    @Test
    public void testHashCode() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation object using the mock object
        Invocation invocation = new Invocation(mockObject, MockitoMethod.HASH_CODE, null, 1, null);

        // Verify that the hashCode() method throws a RuntimeException
        try {
            invocation.hashCode();
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            // Exception is expected
        }
    }

    @Test
    public void testToString() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation object using the mock object
        Invocation invocation = new Invocation(mockObject, MockitoMethod.TO_STRING, null, 1, null);

        // Verify that the toString() method returns the correct string representation
        assertEquals("Invocation.toString()", invocation.toString());
    }

    @Test
    public void testIsValidException() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation object using the mock object
        Invocation invocation = new Invocation(mockObject, MockitoMethod.IS_VALID_EXCEPTION, null, 1, null);

        // Verify that the isValidException() method returns false for null throwable
        assertFalse(invocation.isValidException(null));

        // Verify that the isValidException() method returns false for exception not assignable from throwable class
        assertFalse(invocation.isValidException(new NullPointerException()));

        // Verify that the isValidException() method returns true for exception assignable from throwable class
        assertTrue(invocation.isValidException(new Exception()));
    }

    @Test
    public void testIsValidReturnType() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation object using the mock object
        Invocation invocation = new Invocation(mockObject, MockitoMethod.IS_VALID_RETURN_TYPE, null, 1, null);

        // Verify that the isValidReturnType() method returns true for return type same as class
        assertTrue(invocation.isValidReturnType(Object.class));

        // Verify that the isValidReturnType() method returns false for return type not assignable from class
        assertFalse(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsVoid() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation objects using the mock object and same method with different return types
        Invocation invocation1 = new Invocation(mockObject, MockitoMethod.IS_VOID, null, 1, null);
        Invocation invocation2 = new Invocation(mockObject, MockitoMethod.GET_METHOD, null, 2, null);

        // Verify that the isVoid() method returns true for method with return type void
        assertTrue(invocation1.isVoid());

        // Verify that isVoid() method returns false for method with return type not void
        assertFalse(invocation2.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation object using the mock object
        Invocation invocation = new Invocation(mockObject, MockitoMethod.PRINT_METHOD_RETURN_TYPE, null, 1, null);

        // Verify that the printMethodReturnType() method returns the correct string representation of the return type
        assertEquals("Object", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation object using the mock object
        Invocation invocation = new Invocation(mockObject, MockitoMethod.GET_METHOD_NAME, null, 1, null);

        // Verify that the getMethodName() method returns the correct method name
        assertEquals("getMethodName", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation objects using the mock object and same method with different return types
        Invocation invocation1 = new Invocation(mockObject, MockitoMethod.RETURNS_PRIMITIVE, null, 1, null);
        Invocation invocation2 = new Invocation(mockObject, MockitoMethod.GET_METHOD, null, 2, null);

        // Verify that the returnsPrimitive() method returns true for method with primitive return type
        assertTrue(invocation1.returnsPrimitive());

        // Verify that returnsPrimitive() method returns false for method with non-primitive return type
        assertFalse(invocation2.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation object using the mock object
        Invocation invocation = new Invocation(mockObject, MockitoMethod.GET_LOCATION, null, 1, null);

        // Verify that the getLocation() method returns a valid Location object
        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create arguments array
        Object[] args = {1, "test"};

        // Create Invocation object using the mock object and arguments
        Invocation invocation = new Invocation(mockObject, MockitoMethod.GET_ARGUMENTS_COUNT, args, 1, null);

        // Verify that the getArgumentsCount() method returns the correct number of arguments
        assertEquals(args.length, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create arguments array
        Object[] args = {1, "test"};

        // Create Invocation object using the mock object and arguments
        Invocation invocation = new Invocation(mockObject, MockitoMethod.GET_RAW_ARGUMENTS, args, 1, null);

        // Verify that the getRawArguments() method returns the correct raw arguments
        assertArrayEquals(args, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation object using the mock object
        Invocation invocation = new Invocation(mockObject, MockitoMethod.CALL_REAL_METHOD, null, 1, null);

        // Verify that the callRealMethod() method throws a RuntimeException
        try {
            invocation.callRealMethod();
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            // Exception is expected
        }
    }

    @Test
    public void testToStringWithPrintSettings() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation object using the mock object
        Invocation invocation = new Invocation(mockObject, MockitoMethod.TO_STRING_WITH_PRINT_SETTINGS, null, 1, null);

        // Verify that the toString(PrintSettings) method returns the correct string representation with default print settings
        assertEquals("Invocation.toString()", invocation.toString(new PrintSettings()));
    }

    @Test
    public void testMarkVerified() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation object using the mock object
        Invocation invocation = new Invocation(mockObject, MockitoMethod.MARK_VERIFIED, null, 1, null);

        // Call markVerified() to mark the invocation as verified
        invocation.markVerified();

        // Verify that the isVerified() method returns true
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        // Create a mock object
        Object mockObject = mock(Object.class);

        // Create Invocation object using the mock object
        Invocation invocation = new Invocation(mockObject, MockitoMethod.MARK_VERIFIED_IN_ORDER, null, 1, null);

        // Call markVerifiedInOrder() to mark the invocation as verified in order
        invocation.markVerifiedInOrder();

        // Verify that the isVerifiedInOrder() method returns true
        assertTrue(invocation.isVerifiedInOrder());
    }

}