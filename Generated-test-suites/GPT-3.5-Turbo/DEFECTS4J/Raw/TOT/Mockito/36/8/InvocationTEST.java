
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationTest {

    private Invocation invocation;
    private Object mock;

    @Before
    public void setup() {
        mock = mock(Object.class);
        MockitoMethod method = new MockitoMethod(Object.class.getDeclaredMethods()[0]);
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;

        invocation = new Invocation(mock, method, args, sequenceNumber, null);
    }
    
    @Test
    public void testGetMock() {
        assertEquals(mock, invocation.getMock());
    }
    
    @Test
    public void testGetMethod() {
        assertEquals(new MockitoMethod(Object.class.getDeclaredMethods()[0]), invocation.getMethod());
    }
    
    @Test
    public void testGetArguments() {
        Object[] expectedArgs = {1, 2, 3};
        assertArrayEquals(expectedArgs, invocation.getArguments());
    }
    
    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
    }
    
    @Test
    public void testGetSequenceNumber() {
        assertEquals(Integer.valueOf(1), invocation.getSequenceNumber());
    }
    
    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
    }
    
    @Test
    public void testEquals() {
        Invocation otherInvocation = new Invocation(mock, new MockitoMethod(Object.class.getDeclaredMethods()[0]), 
                                                   new Object[]{1, 2, 3}, 1, null);
        assertTrue(invocation.equals(otherInvocation));
    }
    
    @Test
    public void testNotEqualsDifferentMock() {
        Object differentMock = mock(Object.class);
        Invocation otherInvocation = new Invocation(differentMock, new MockitoMethod(Object.class.getDeclaredMethods()[0]), 
                                                   new Object[]{1, 2, 3}, 1, null);
        assertFalse(invocation.equals(otherInvocation));
    }
    
    @Test
    public void testNotEqualsDifferentMethod() {
        Invocation otherInvocation = new Invocation(mock, new MockitoMethod(Object.class.getDeclaredMethods()[1]), 
                                                   new Object[]{1, 2, 3}, 1, null);
        assertFalse(invocation.equals(otherInvocation));
    }
    
    @Test
    public void testNotEqualsDifferentArguments() {
        Invocation otherInvocation = new Invocation(mock, new MockitoMethod(Object.class.getDeclaredMethods()[0]), 
                                                   new Object[]{1, 2, 4}, 1, null);
        assertFalse(invocation.equals(otherInvocation));
    }
    
    @Test
    public void testHashCode() {
        assertThrows(RuntimeException.class, () -> invocation.hashCode());
    }
    
    @Test
    public void testToString() {
        String expectedToString = "ObjectMockitoMethod.methodName([Ljava.lang.Object;)";
        assertEquals(expectedToString, invocation.toString());
    }
    
    @Test
    public void testToStringWithMatchers() {
        Matcher matcher = mock(Matcher.class);
        when(matcher.toString()).thenReturn("matcherString");
        List<Matcher> matchers = Arrays.asList(matcher);
        invocation = spy(invocation);
        doReturn(matchers).when(invocation).argumentsToMatchers();
        
        String expectedToString = "ObjectMockitoMethod.methodName(matcherString)";
        assertEquals(expectedToString, invocation.toString());
    }
    
    @Test
    public void testIsToString() {
        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
        MockitoMethod toStringMethod = new MockitoMethod(Object.class.getDeclaredMethods()[1]);
        when(invocationOnMock.getMethod()).thenReturn(toStringMethod);
        
        assertTrue(Invocation.isToString(invocationOnMock));
    }
    
    @Test
    public void testIsNotToString() {
        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
        MockitoMethod nonToStringMethod = new MockitoMethod(Object.class.getDeclaredMethods()[0]);
        when(invocationOnMock.getMethod()).thenReturn(nonToStringMethod);
        
        assertFalse(Invocation.isToString(invocationOnMock));
    }
    
    @Test
    public void testIsValidExceptionValid() {
        Throwable throwable = new IllegalArgumentException();
        MockitoMethod method = new MockitoMethod(Object.class.getDeclaredMethods()[0]);
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);
        
        assertTrue(invocation.isValidException(throwable));
    }
    
    @Test
    public void testIsValidExceptionInvalid() {
        Throwable throwable = new NullPointerException();
        MockitoMethod method = new MockitoMethod(Object.class.getDeclaredMethods()[0]);
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);
        
        assertFalse(invocation.isValidException(throwable));
    }
    
    @Test
    public void testIsValidReturnTypeValid() {
        Class<?> returnType = Object.class;
        
        assertTrue(invocation.isValidReturnType(returnType));
    }
    
    @Test
    public void testIsValidReturnTypeInvalid() {
        Class<?> returnType = String.class;
        
        assertFalse(invocation.isValidReturnType(returnType));
    }
    
    @Test
    public void testIsVoidWithVoidReturnType() throws NoSuchMethodException {
        MockitoMethod method = new MockitoMethod(Object.class.getDeclaredMethod("toString"));
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);
        
        assertTrue(invocation.isVoid());
    }
    
    @Test
    public void testIsVoidWithNonVoidReturnType() throws NoSuchMethodException {
        MockitoMethod method = new MockitoMethod(Object.class.getDeclaredMethod("hashCode"));
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);
        
        assertFalse(invocation.isVoid());
    }
    
    @Test
    public void testPrintMethodReturnType() throws NoSuchMethodException {
        MockitoMethod method = new MockitoMethod(Object.class.getDeclaredMethod("toString"));
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);
        
        assertEquals("String", invocation.printMethodReturnType());
    }
    
    @Test
    public void testGetMethodName() {
        assertEquals("methodName", invocation.getMethodName());
    }
    
    @Test
    public void testReturnsPrimitiveWithPrimitiveReturnType() throws NoSuchMethodException {
        MockitoMethod method = new MockitoMethod(Object.class.getDeclaredMethod("hashCode"));
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);
        
        assertTrue(invocation.returnsPrimitive());
    }
    
    @Test
    public void testReturnsPrimitiveWithNonPrimitiveReturnType() throws NoSuchMethodException {
        MockitoMethod method = new MockitoMethod(Object.class.getDeclaredMethod("toString"));
        Invocation invocation = new Invocation(mock, method, new Object[0], 1, null);
        
        assertFalse(invocation.returnsPrimitive());
    }
    
    @Test
    public void testGetLocation() {
        assertNotNull(invocation.getLocation());
    }
    
    @Test
    public void testGetArgumentsCount() {
        assertEquals(3, invocation.getArgumentsCount());
    }
    
    @Test
    public void testGetRawArguments() {
        Object[] expectedRawArguments = {1, 2, 3};
        assertArrayEquals(expectedRawArguments, invocation.getRawArguments());
    }
    
    @Test
    public void testCallRealMethod() throws Throwable {
        RealMethod realMethod = mock(RealMethod.class);
        when(realMethod.invoke(any(), any())).thenReturn("result");
        
        Invocation invocation = new Invocation(mock, null, new Object[0], 1, realMethod);
        
        assertEquals("result", invocation.callRealMethod());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);
        MockitoMethod method = new MockitoMethod(Object.class.getDeclaredMethods()[0]);
        Invocation invocation = new Invocation(mock, method, new Object[]{1, 2, 3}, 1, null);
        
        String expectedToString = "ObjectMockitoMethod.methodName(matchers)";
        assertEquals(expectedToString, invocation.toString(printSettings));
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
        
        invocation.markVerifiedInOrder();
        
        assertTrue(invocation.isVerifiedInOrder());
        assertTrue(invocation.isVerified());
    }
}
