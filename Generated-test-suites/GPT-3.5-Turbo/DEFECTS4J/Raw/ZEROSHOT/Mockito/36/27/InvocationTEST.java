import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationTest {
    private static final Object MOCK = mock(Object.class);
    private static final MockitoMethod METHOD = mock(MockitoMethod.class);
    private static final Object[] ARGS = new Object[]{1, 2, 3};
    private static final int SEQUENCE_NUMBER = 1;
    private static final RealMethod REAL_METHOD = mock(RealMethod.class);
    
    @Test
    public void testGetMock() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertEquals(MOCK, invocation.getMock());
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertEquals(METHOD, invocation.getMethod());
    }
    
    @Test
    public void testGetArguments() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertArrayEquals(ARGS, invocation.getArguments());
    }
    
    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertFalse(invocation.isVerified());
    }
    
    @Test
    public void testGetSequenceNumber() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertEquals(Integer.valueOf(SEQUENCE_NUMBER), invocation.getSequenceNumber());
    }
    
    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertFalse(invocation.isVerifiedInOrder());
    }
    
    @Test
    public void testEquals() {
        Invocation invocation1 = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        Invocation invocation2 = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertTrue(invocation1.equals(invocation2));
    }
    
    @Test
    public void testHashCode() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        // Hash code not implemented, expecting RuntimeException
        try {
            invocation.hashCode();
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
        }
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        String expected = "Mock for java.lang.Object@hashCode()" +
                "." + METHOD.getName() + "(1, 2, 3)";
        assertEquals(expected, invocation.toString());
    }
    
    @Test
    public void testIsValidException() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertFalse(invocation.isValidException(new RuntimeException()));
    }
    
    @Test
    public void testIsValidReturnType() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertFalse(invocation.isValidReturnType(String.class));
    }
    
    @Test
    public void testIsVoid() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertFalse(invocation.isVoid());
    }
    
    @Test
    public void testPrintMethodReturnType() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertEquals(METHOD.getReturnType().getSimpleName(), invocation.printMethodReturnType());
    }
    
    @Test
    public void testGetMethodName() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertEquals(METHOD.getName(), invocation.getMethodName());
    }
    
    @Test
    public void testReturnsPrimitive() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertFalse(invocation.returnsPrimitive());
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertNotNull(invocation.getLocation());
    }
    
    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertEquals(ARGS.length, invocation.getArgumentsCount());
    }
    
    @Test
    public void testGetRawArguments() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertArrayEquals(ARGS, invocation.getRawArguments());
    }
    
    @Test
    public void testCallRealMethod() throws Throwable {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        assertNull(invocation.callRealMethod());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        PrintSettings printSettings = new PrintSettings();
        String expected = "Mock for java.lang.Object@hashCode()" +
                "." + METHOD.getName() + "(1, 2, 3)";
        assertEquals(expected, invocation.toString(printSettings));
    }
    
    @Test
    public void testMarkVerified() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }
    
    @Test
    public void testMarkVerifiedInOrder() {
        Invocation invocation = new Invocation(MOCK, METHOD, ARGS, SEQUENCE_NUMBER, REAL_METHOD);
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }
}