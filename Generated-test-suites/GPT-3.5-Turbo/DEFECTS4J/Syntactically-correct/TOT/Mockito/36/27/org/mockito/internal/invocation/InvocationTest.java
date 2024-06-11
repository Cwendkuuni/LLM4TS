import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationTest {

    private Object mock;
    private MockitoMethod method;
    private Object[] args;
    private RealMethod realMethod;
    private Invocation invocation;

    @Before
    public void setUp() {
        mock = mock(Object.class);
        method = mock(MockitoMethod.class);
        args = new Object[]{1, 2, 3};
        realMethod = mock(RealMethod.class);
        invocation = new Invocation(mock, method, args, 1, realMethod);
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
        assertArrayEquals(args, invocation.getArguments());
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
        Invocation other = new Invocation(mock, method, args, 1, realMethod);
        assertTrue(invocation.equals(other));
    }

    @Test
    public void testHashCode() {
        assertThrows(RuntimeException.class, () -> invocation.hashCode());
    }

    @Test
    public void testToString() {
        String expected = mockitoMock(mock) + ".method(1, 2, 3)";
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testArgumentsToMatchers_PrimitiveType() {
        Object[] args = new Object[]{1, true, "test"};
        invocation = new Invocation(mock, method, args, 1, realMethod);

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals(1));
        matchers.add(new Equals(true));
        matchers.add(new Equals("test"));

        assertEquals(matchers, invocation.argumentsToMatchers());
    }

    @Test
    public void testArgumentsToMatchers_ObjectType() {
        Object[] args = new Object[]{new Object(), new Object()};
        invocation = new Invocation(mock, method, args, 1, realMethod);

        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals(args[0]));
        matchers.add(new Equals(args[1]));

        assertEquals(matchers, invocation.argumentsToMatchers());
    }

    @Test
    public void testIsToString() {
        MockitoMethod toStringMethod = mock(MockitoMethod.class);
        when(toStringMethod.getName()).thenReturn("toString");
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(toStringMethod);

        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException_Valid() {
        MockitoMethod methodWithException = mock(MockitoMethod.class);
        when(methodWithException.getExceptionTypes()).thenReturn(new Class[]{IllegalArgumentException.class});
        Throwable throwable = new IllegalArgumentException();

        invocation = new Invocation(mock, methodWithException, args, 1, realMethod);
        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidException_Invalid() {
        MockitoMethod methodWithException = mock(MockitoMethod.class);
        when(methodWithException.getExceptionTypes()).thenReturn(new Class[]{IllegalArgumentException.class, NullPointerException.class});
        Throwable throwable = new IllegalStateException();

        invocation = new Invocation(mock, methodWithException, args, 1, realMethod);
        assertFalse(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType_PrimitiveType() {
        MockitoMethod methodWithPrimitiveReturnType = mock(MockitoMethod.class);
        when(methodWithPrimitiveReturnType.getReturnType()).thenReturn(int.class);

        invocation = new Invocation(mock, methodWithPrimitiveReturnType, args, 1, realMethod);
        assertTrue(invocation.isValidReturnType(int.class));
    }
    
    @Test
    public void testIsValidReturnType_ObjectType() {
        MockitoMethod methodWithObjectType = mock(MockitoMethod.class);
        when(methodWithObjectType.getReturnType()).thenReturn(Object.class);

        invocation = new Invocation(mock, methodWithObjectType, args, 1, realMethod);
        assertTrue(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid_True() {
        MockitoMethod methodWithVoid = mock(MockitoMethod.class);
        when(methodWithVoid.getReturnType()).thenReturn(void.class);

        invocation = new Invocation(mock, methodWithVoid, args, 1, realMethod);
        assertTrue(invocation.isVoid());
    }
    
    @Test
    public void testIsVoid_False() {
        MockitoMethod methodWithNonVoid = mock(MockitoMethod.class);
        when(methodWithNonVoid.getReturnType()).thenReturn(Object.class);

        invocation = new Invocation(mock, methodWithNonVoid, args, 1, realMethod);
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod methodWithReturnType = mock(MockitoMethod.class);
        when(methodWithReturnType.getReturnType()).thenReturn(String.class);

        invocation = new Invocation(mock, methodWithReturnType, args, 1, realMethod);
        assertEquals("String", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod methodWithName = mock(MockitoMethod.class);
        when(methodWithName.getName()).thenReturn("method");

        invocation = new Invocation(mock, methodWithName, args, 1, realMethod);
        assertEquals("method", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive_True() {
        MockitoMethod methodWithPrimitiveReturnType = mock(MockitoMethod.class);
        when(methodWithPrimitiveReturnType.getReturnType()).thenReturn(int.class);

        invocation = new Invocation(mock, methodWithPrimitiveReturnType, args, 1, realMethod);
        assertTrue(invocation.returnsPrimitive());
    }
    
    @Test
    public void testReturnsPrimitive_False() {
        MockitoMethod methodWithObjectReturnType = mock(MockitoMethod.class);
        when(methodWithObjectReturnType.getReturnType()).thenReturn(Object.class);

        invocation = new Invocation(mock, methodWithObjectReturnType, args, 1, realMethod);
        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        assertEquals(args.length, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        assertArrayEquals(args, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object expectedResult = new Object();
        when(realMethod.invoke(mock, args)).thenReturn(expectedResult);

        Object result = invocation.callRealMethod();
        assertEquals(expectedResult, result);
    }

    @Test
    public void testToString_PrintSettings() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals(1));
        matchers.add(new Equals(2));
        matchers.add(new Equals(3));

        PrintSettings printSettings = mock(PrintSettings.class);
        when(printSettings.isMultiline()).thenReturn(true);

        String expected = mockitoMock(mock) + ".method(\n" +
                "    1,\n" +
                "    2,\n" +
                "    3\n" +
                ")";
        assertEquals(expected, invocation.toString(printSettings));
    }

    @Test
    public void testMarkVerified() {
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
        assertTrue(invocation.isVerified());
    }

    private String mockitoMock(Object mock) {
        return new MockUtil().getMockName(mock) + ".";
    }

}