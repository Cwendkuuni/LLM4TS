import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationTest {
    
    // Method: getMock()
    @Test
    public void testGetMock() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        Object result = invocation.getMock();
        assertNotNull(result);
    }
    
    // Method: getMethod()
    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        MockitoMethod result = invocation.getMethod();
        assertNotNull(result);
    }
    
    // Method: getArguments()
    @Test
    public void testGetArguments() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        Object[] result = invocation.getArguments();
        assertNotNull(result);
    }
    
    // Method: isVerified()
    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        boolean result = invocation.isVerified();
        assertFalse(result);
    }
    
    // Method: getSequenceNumber()
    @Test
    public void testGetSequenceNumber() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        Integer result = invocation.getSequenceNumber();
        assertNotNull(result);
    }
    
    // Method: isVerifiedInOrder()
    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        boolean result = invocation.isVerifiedInOrder();
        assertFalse(result);
    }
    
    // Method: equals(Object o)
    @Test
    public void testEquals() {
        Invocation invocation1 = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        Invocation invocation2 = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        boolean result = invocation1.equals(invocation2);
        assertTrue(result);
    }
    
    // Method: hashCode()
    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        invocation.hashCode();
    }
    
    // Method: toString()
    @Test
    public void testToString() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        String result = invocation.toString();
        assertNotNull(result);
    }
    
    // Method: toString(List<Matcher> matchers, PrintSettings printSettings)
    @Test
    public void testToStringWithMatchers() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        String result = invocation.toString(mock(List.class), mock(PrintSettings.class));
        assertNotNull(result);
    }
    
    // Method: qualifiedMethodName()
    @Test
    public void testQualifiedMethodName() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        String result = invocation.qualifiedMethodName();
        assertNotNull(result);
    }
    
    // Method: argumentsToMatchers()
    @Test
    public void testArgumentsToMatchers() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        List<Matcher> result = invocation.argumentsToMatchers();
        assertNotNull(result);
    }
    
    // Method: isToString(InvocationOnMock invocation)
    @Test
    public void testIsToString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        boolean result = Invocation.isToString(invocation);
        assertFalse(result);
    }
    
    // Method: isValidException(Throwable throwable)
    @Test
    public void testIsValidException() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        Throwable throwable = mock(Throwable.class);
        boolean result = invocation.isValidException(throwable);
        assertFalse(result);
    }
    
    // Method: isValidReturnType(Class clazz)
    @Test
    public void testIsValidReturnType() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        Class clazz = Object.class;
        boolean result = invocation.isValidReturnType(clazz);
        assertTrue(result);
    }
    
    // Method: isVoid()
    @Test
    public void testIsVoid() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        boolean result = invocation.isVoid();
        assertFalse(result);
    }
    
    // Method: printMethodReturnType()
    @Test
    public void testPrintMethodReturnType() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        String result = invocation.printMethodReturnType();
        assertNotNull(result);
    }
    
    // Method: getMethodName()
    @Test
    public void testGetMethodName() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        String result = invocation.getMethodName();
        assertNotNull(result);
    }
    
    // Method: returnsPrimitive()
    @Test
    public void testReturnsPrimitive() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        boolean result = invocation.returnsPrimitive();
        assertFalse(result);
    }
    
    // Method: getLocation()
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        Location result = invocation.getLocation();
        assertNotNull(result);
    }
    
    // Method: getArgumentsCount()
    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        int result = invocation.getArgumentsCount();
        assertEquals(0, result);
    }
    
    // Method: getRawArguments()
    @Test
    public void testGetRawArguments() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        Object[] result = invocation.getRawArguments();
        assertNotNull(result);
    }
    
    // Method: callRealMethod()
    @Test(expected = Throwable.class)
    public void testCallRealMethod() throws Throwable {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        invocation.callRealMethod();
    }
    
    // Method: toString(PrintSettings printSettings)
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        String result = invocation.toString(mock(PrintSettings.class));
        assertNotNull(result);
    }
    
    // Method: markVerified()
    @Test
    public void testMarkVerified() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }
    
    // Method: markVerifiedInOrder()
    @Test
    public void testMarkVerifiedInOrder() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 0, mock(RealMethod.class));
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }
}