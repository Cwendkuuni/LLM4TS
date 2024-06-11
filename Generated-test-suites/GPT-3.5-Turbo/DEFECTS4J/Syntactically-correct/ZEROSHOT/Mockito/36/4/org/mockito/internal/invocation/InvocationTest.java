import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.*;
import org.mockito.invocation.InvocationOnMock;
import java.util.*;

public class InvocationTest {
    
    private Invocation invocation;
    private Object mock;
    private MockitoMethod method;
    private Object[] args;
    private int sequenceNumber;
    private RealMethod realMethod;

    @Before
    public void setup() {
        mock = new Object();
        method = new MockitoMethod("methodName", null);
        args = new Object[]{};
        sequenceNumber = 1;
        realMethod = new RealMethod();
        invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        assertEquals(mock, invocation.getMock());
    }
    
    @Test
    public void testGetMethod() {
        assertEquals(method, invocation.getMethod());
    }
    
    @Test
    public void testGetArguments() {
        assertEquals(args, invocation.getArguments());
    }
    
    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }
    
    @Test
    public void testGetSequenceNumber() {
        assertEquals(Integer.valueOf(sequenceNumber), invocation.getSequenceNumber());
    }
    
    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }
    
    @Test
    public void testEquals() {
        Invocation other = new Invocation(mock, method, args, sequenceNumber, realMethod);
        assertTrue(invocation.equals(other));
        
        other = new Invocation(mock, method, new Object[]{}, sequenceNumber, realMethod);
        assertFalse(invocation.equals(other));
        
        other = new Invocation(mock, new MockitoMethod("otherMethodName", null), args, sequenceNumber, realMethod);
        assertFalse(invocation.equals(other));
        
        other = new Invocation(new Object(), method, args, sequenceNumber, realMethod);
        assertFalse(invocation.equals(other));
    }
    
    @Test
    public void testHashCode() {
        Throwable exception = assertThrows(RuntimeException.class, () -> {
            invocation.hashCode();
        });
    }
    
    @Test
    public void testToString() {
        String expected = "";
        expected += "Mock name: " + mock.toString() + "." + method.getName() + "\n";
        
        MatchersPrinter matchersPrinter = new MatchersPrinter();
        String argumentsLine = matchersPrinter.getArgumentsLine(invocation.argumentsToMatchers(), new PrintSettings());
        
        expected += "Invocation arguments:\n" + argumentsLine;
        
        assertEquals(expected, invocation.toString());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        String expected = "";
        expected += "Mock name: " + mock.toString() + "." + method.getName() + "\n";
        
        MatchersPrinter matchersPrinter = new MatchersPrinter();
        String argumentsLine = matchersPrinter.getArgumentsLine(invocation.argumentsToMatchers(), new PrintSettings());
        
        expected += "Invocation arguments:\n" + argumentsLine;
        
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);
        
        assertEquals(expected, invocation.toString(printSettings));
    }
    
    @Test
    public void testIsToString() {
        InvocationOnMock invocationOnMock = new Invocation(mock, method, args);
        assertFalse(Invocation.isToString(invocationOnMock));
        
        MockitoMethod toStringMethod = new MockitoMethod("toString", null);
        invocationOnMock = new Invocation(mock, toStringMethod, args);
        assertTrue(Invocation.isToString(invocationOnMock));
    }
    
    @Test
    public void testIsValidException() {
        Throwable throwable = new Exception();
        MockitoMethod methodWithException = new MockitoMethod("methodName", new Class[]{Exception.class});
        
        invocation = new Invocation(mock, methodWithException, args, sequenceNumber, realMethod);
        
        assertTrue(invocation.isValidException(throwable));
        
        throwable = new RuntimeException();
        
        assertFalse(invocation.isValidException(throwable));
    }
    
    @Test
    public void testIsValidReturnType() {
        Class<?> returnType = Object.class;
        
        assertTrue(invocation.isValidReturnType(returnType));
        
        returnType = Integer.TYPE;
        method = new MockitoMethod("methodName", new Class[]{Integer.TYPE});
        assertEquals(Integer.TYPE, method.getReturnType());
        
        assertTrue(invocation.isValidReturnType(returnType));
        
        returnType = Integer.class;
        
        assertFalse(invocation.isValidReturnType(returnType));
    }
    
    @Test
    public void testIsVoid() {
        assertFalse(invocation.isVoid());
        
        method = new MockitoMethod("methodName", new Class[]{Void.TYPE});
        invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        assertTrue(invocation.isVoid());
    }
    
    @Test
    public void testPrintMethodReturnType() {
        String returnType = invocation.printMethodReturnType();
        
        assertEquals(method.getReturnType().getSimpleName(), returnType);
    }
    
    @Test
    public void testGetMethodName() {
        assertEquals(method.getName(), invocation.getMethodName());
    }
    
    @Test
    public void testReturnsPrimitive() {
        assertFalse(invocation.returnsPrimitive());
        
        method = new MockitoMethod("methodName", new Class[]{Integer.TYPE});
        invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertTrue(invocation.returnsPrimitive());
    }
    
    @Test
    public void testGetLocation() {
        Location location = invocation.getLocation();
        assertNotNull(location);
    }
    
    @Test
    public void testGetArgumentsCount() {
        int count = invocation.getArgumentsCount();
        assertEquals(args.length, count);
    }
    
    @Test
    public void testGetRawArguments() {
        Object[] rawArguments = invocation.getRawArguments();
        assertEquals(args, rawArguments);
    }
    
    @Test
    public void testCallRealMethod() throws Throwable {
        Object realMethodResult = invocation.callRealMethod();
        assertNull(realMethodResult);
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        String expected = "";
        expected += "Mock name: " + mock.toString() + "." + method.getName() + "\n";
        
        MatchersPrinter matchersPrinter = new MatchersPrinter();
        String argumentsLine = matchersPrinter.getArgumentsLine(invocation.argumentsToMatchers(), new PrintSettings());
        
        expected += "Invocation arguments:\n" + argumentsLine;
        
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);
        
        assertEquals(expected, invocation.toString(printSettings));
    }
    
    @Test
    public void testMarkVerified() {
        assertFalse(invocation.isVerified());
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }
    
    @Test
    public void testMarkVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
        assertFalse(invocation.isVerified());
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
        assertTrue(invocation.isVerified());
    }
}