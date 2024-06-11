import org.hamcrest.Matcher;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Invocation invocation = new Invocation(mock(Object.class), null, null, 0, null);
        Object result = invocation.getMock();
        assertSame(mock(Object.class), result);
    }
    
    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation(null, mock(MockitoMethod.class), null, 0, null);
        MockitoMethod result = invocation.getMethod();
        assertSame(mock(MockitoMethod.class), result);
    }
    
    @Test
    public void testGetArguments() {
        Invocation invocation = new Invocation(null, null, new Object[]{1, 2, 3}, 0, null);
        Object[] result = invocation.getArguments();
        assertArrayEquals(new Object[]{1, 2, 3}, result);
    }
    
    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        boolean result = invocation.isVerified();
        assertFalse(result);
    }
    
    @Test
    public void testGetSequenceNumber() {
        Invocation invocation = new Invocation(null, null, null, 10, null);
        Integer result = invocation.getSequenceNumber();
        assertEquals(Integer.valueOf(10), result);
    }
    
    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        boolean result = invocation.isVerifiedInOrder();
        assertFalse(result);
    }
    
    @Test
    public void testEquals() {
        Invocation invocation1 = new Invocation(null, null, null, 0, null);
        Invocation invocation2 = new Invocation(null, null, null, 0, null);
        boolean result = invocation1.equals(invocation2);
        assertTrue(result);
    }
    
    @Test
    public void testHashCode() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        assertThrows(RuntimeException.class, () -> {
            invocation.hashCode();
        });
    }
    
    @Test
    public void testOverrideToString() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        String result = invocation.toString();
        assertNotNull(result);
    }
    
    @Test
    public void testIsToString() {
        boolean result = Invocation.isToString(mock(InvocationOnMock.class));
        assertFalse(result);
    }
    
    @Test
    public void testIsValidException() {
        Invocation invocation = new Invocation(null, mock(MockitoMethod.class), null, 0, null);
        boolean result = invocation.isValidException(null);
        assertFalse(result);
    }
    
    @Test
    public void testIsValidReturnType() {
        Invocation invocation = new Invocation(null, mock(MockitoMethod.class), null, 0, null);
        boolean result = invocation.isValidReturnType(Object.class);
        assertFalse(result);
    }
    
    @Test
    public void testIsVoid() {
        Invocation invocation1 = new Invocation(null, null, null, 0, null);
        Invocation invocation2 = new Invocation(null, null, null, 0, null);
        assertFalse(invocation1.isVoid());
        assertFalse(invocation2.isVoid());
    }
    
    @Test
    public void testPrintMethodReturnType() {
        Invocation invocation = new Invocation(null, mock(MockitoMethod.class), null, 0, null);
        String result = invocation.printMethodReturnType();
        assertNotNull(result);
    }
    
    @Test
    public void testGetMethodName() {
        Invocation invocation = new Invocation(null, mock(MockitoMethod.class), null, 0, null);
        String result = invocation.getMethodName();
        assertNotNull(result);
    }
    
    @Test
    public void testReturnsPrimitive() {
        Invocation invocation = new Invocation(null, mock(MockitoMethod.class), null, 0, null);
        boolean result = invocation.returnsPrimitive();
        assertFalse(result);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        Location result = invocation.getLocation();
        assertNotNull(result);
    }
    
    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(null, null, new Object[]{1, 2, 3}, 0, null);
        int result = invocation.getArgumentsCount();
        assertEquals(3, result);
    }
    
    @Test
    public void testGetRawArguments() {
        Invocation invocation = new Invocation(null, null, new Object[]{1, 2, 3}, 0, null);
        Object[] result = invocation.getRawArguments();
        assertArrayEquals(new Object[]{1, 2, 3}, result);
    }
    
    @Test
    public void testCallRealMethod() throws Throwable {
        Invocation invocation = new Invocation(null, null, new Object[]{1, 2, 3}, 0, mock(RealMethod.class));
        Object result = invocation.callRealMethod();
        assertNull(result);
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        String result = invocation.toString(mock(PrintSettings.class));
        assertNotNull(result);
    }
    
    @Test
    public void testMarkVerified() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }
    
    @Test
    public void testMarkVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }
}