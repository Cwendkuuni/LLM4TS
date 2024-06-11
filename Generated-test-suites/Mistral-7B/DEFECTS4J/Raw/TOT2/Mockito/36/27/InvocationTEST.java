import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.invocation.Invocation;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class InvocationTest {

    @Test
    public void testInvocationConstruction() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));
        assertNotNull(invocation);
    }

    @Test
    public void testInvocationConstructionWithNullArguments() {
        assertThrows(NullPointerException.class, () -> new Invocation(null, null, null, 1, Mockito.mock(RealMethod.class)));
    }

    @Test
    public void testInvocationConstructionWithNullMockitoMethod() {
        Object mock = Mockito.mock(String.class);
        assertThrows(NullPointerException.class, () -> new Invocation(mock, null, new Object[0], 1, Mockito.mock(RealMethod.class)));
    }

    @Test
    public void testInvocationConstructionWithNullRealMethod() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        assertThrows(NullPointerException.class, () -> new Invocation(mock, mockitoMethod, new Object[0], 1, null));
    }

    @Test
    public void testInvocationConstructionWithNonUniqueSequenceNumber() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation1 = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));
        assertThrows(IllegalArgumentException.class, () -> new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class), 1));
    }

    @Test
    public void testInvocationConstructionWithVarArgsAndNullArray() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", Mockito.varargs(String[].class));
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, Mockito.mock(RealMethod.class));
        assertNotNull(invocation);
    }

    @Test
    public void testInvocationConstructionWithVarArgsAndEmptyArray() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length", Mockito.varargs(String[].class));
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));
        assertNotNull(invocation);
    }

    @Test
    public void testInvocationConstructionWithVarArgsAndValidArray() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "format", Mockito.varargs(Object[].class));
        Object[] arguments = new Object[] {"%s", "Hello", "World"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, Mockito.mock(RealMethod.class));
        assertNotNull(invocation);
    }

    @Test
    public void testIsValidExceptionWithValidException() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, Mockito.mock(RealMethod.class));
        Throwable throwable = new Exception("Test Exception");
        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidExceptionWithInvalidException() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, Mockito.mock(RealMethod.class));
        assertFalse(invocation.isValidException(new IOException("Test IOException")));
    }

    @Test
    public void testIsValidReturnTypeWithValidPrimitiveType() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, Mockito.mock(RealMethod.class));
        assertTrue(invocation.isValidReturnType(byte.class));
    }

    @Test
    public void testIsValidReturnTypeWithValidClass() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, Mockito.mock(RealMethod.class));
        assertTrue(invocation.isValidReturnType(String[].class));
    }

    @Test
    public void testIsValidReturnTypeWithInvalidClass() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, Mockito.mock(RealMethod.class));
        assertFalse(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsVoidWithVoidMethod() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "voidMethod");
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, Mockito.mock(RealMethod.class));
        assertTrue(invocation.isVoid());
    }

    @Test
    public void testIsVoidWithNonVoidMethod() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, Mockito.mock(RealMethod.class));
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testToStringWithEmptyArguments() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, Mockito.mock(RealMethod.class));
        assertEquals("Invocation[mock=org.mockito.internal.stubs.MockitoStubImpl@3a0b0a, method=length(), arguments=[]]", invocation.toString());
    }

    @Test
    public void testToStringWithVarArgs() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "format", Mockito.varargs(Object[].class));
        Object[] arguments = new Object[] {"%s", "Hello", "World"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, Mockito.mock(RealMethod.class));
        assertEquals("Invocation[mock=org.mockito.internal.stubs.MockitoStubImpl@3a0b0a, method=format(java.lang.String, java.lang.String, java.lang.String), arguments=[%s, Hello, World]]", invocation.toString());
    }

    @Test
    public void testEqualsWithSameInvocation() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation1 = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));
        Invocation invocation2 = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));
        assertTrue(invocation1.equals(invocation2));
    }

    @Test
    public void testEqualsWithDifferentInvocation() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Invocation invocation1 = new Invocation(mock, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));
        Object mock2 = Mockito.mock(Integer.class);
        Invocation invocation2 = new Invocation(mock2, mockitoMethod, new Object[0], 1, Mockito.mock(RealMethod.class));
        assertFalse(invocation1.equals(invocation2));
    }

}