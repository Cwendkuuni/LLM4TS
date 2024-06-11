import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.util.MockitoMethod;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.exceptions.VerifyException;

public class InvocationTest {

    @Test
    public void testConstructorWithMethodAndArguments() {
        MockitoMethod mockitoMethod = new MockitoMethod(Invocation.class, "testConstructorWithMethodAndArguments", new Class[]{}, new Class[]{}, null);
        Object mock = MockitoTestUtils.createMock(MockitoMethod.class);
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(mockitoMethod, new Object[0]));

        assertEquals(mockitoMethod, invocation.getMethod());
        assertEquals(mock, invocation.getMock());
        assertArrayEquals(arguments, invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testConstructorWithVarArgs() {
        MockitoMethod mockitoMethod = new MockitoMethod(Invocation.class, "testConstructorWithVarArgs", new Class[]{Object[].class}, new Class[]{}, null);
        Object mock = MockitoTestUtils.createMock(MockitoMethod.class);
        Object[] arguments = new Object[]{new Object[]{1, "test1"}, new Object[]{2, "test2"}};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(mockitoMethod, new Object[0]));

        assertEquals(mockitoMethod, invocation.getMethod());
        assertEquals(mock, invocation.getMock());
        assertArrayEquals(arguments, invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
    }

    @Test(expected = VerifyException.class)
    public void testIsVerifiedReturnsFalseByDefault() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testMarkVerified() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test(expected = VerifyException.class)
    public void testIsVerifiedInOrderThrowsExceptionWhenNotVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        invocation.markVerified();
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        Location location = invocation.getLocation();
        assertNotNull(location);
    }

    @Test
    public void testIsToString() {
        MockitoMethod mockitoMethod = new MockitoMethod(Invocation.class, "toString", new Class[0], new Class[]{String.class}, null);
        InvocationOnMock invocationOnMock = new InvocationOnMock(mockitoMethod, null);
        assertTrue(Invocation.isToString(invocationOnMock));
    }

    @Test
    public void testIsValidExceptionThrowsExceptionWhenExceptionTypeDoesNotMatch() {
        Invocation invocation = new Invocation(null, new MockitoMethod(Object.class, "test", new Class[0], new Class[]{RuntimeException.class}, null), null);
        assertFalse(invocation.isValidException(new IllegalArgumentException()));
    }

    @Test
    public void testIsValidExceptionReturnsTrueWhenExceptionTypeMatches() {
        Invocation invocation = new Invocation(null, new MockitoMethod(Object.class, "test", new Class[0], new Class[]{RuntimeException.class}, null), null);
        assertTrue(invocation.isValidException(new RuntimeException()));
    }

    @Test
    public void testIsValidReturnTypeReturnsTrueWhenReturnTypesMatch() {
        Invocation invocation = new Invocation(null, new MockitoMethod(Object.class, "test", new Class[]{Integer.class}, new Class[]{Integer.class}, null), null);
        assertTrue(invocation.isValidReturnType(int.class));
    }

    @Test
    public void testIsValidReturnTypeReturnsFalseWhenReturnTypesDoNotMatch() {
        Invocation invocation = new Invocation(null, new MockitoMethod(Object.class, "test", new Class[]{Integer.class}, new Class[]{String.class}, null), null);
        assertFalse(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoidReturnsTrueWhenMethodReturnsVoid() {
        Invocation invocation = new Invocation(null, new MockitoMethod(void.class, "test", new Class[0], null, null), null);
        assertTrue(invocation.isVoid());
    }

    @Test
    public void testIsVoidReturnsFalseWhenMethodDoesNotReturnVoid() {
        Invocation invocation = new Invocation(null, new MockitoMethod(Integer.class, "test", new Class[0], null, null), null);
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Invocation invocation = new Invocation(null, new MockitoMethod(Integer.class, "test", new Class[0], null, null), null);
        assertEquals("Integer", invocation.printMethodReturnType());
    }

    @Test
    public void testToString() {
        MockitoMethod mockitoMethod = new MockitoMethod(Invocation.class, "toString", new Class[]{PrintSettings.class}, new Class[]{String.class}, null);
        Invocation invocation = new Invocation(null, mockitoMethod, new Object[]{new PrintSettings()}, 1, new RealMethod(mockitoMethod, new Object[0]));
        String printSettingsString = "PrintSettings{isMultiline=false, printStacktrace=true, printErrorMessages=true}";
        String expected = "[Invocation] test(" + printSettingsString + ")";
        assertEquals(expected, invocation.toString(new PrintSettings()));
    }

    @Test
    public void testToStringWithMatchers() {
        MockitoMethod mockitoMethod = new MockitoMethod(Invocation.class, "toString", new Class[]{List.class, PrintSettings.class}, new Class[]{String.class}, null);
        Invocation invocation = new Invocation(null, mockitoMethod, new Object[]{new ArrayList<>(), new PrintSettings()}, 1, new RealMethod(mockitoMethod, new Object[0]));
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals(1));
        matchers.add(new Equals("test"));
        String expected = "[Invocation] test(1=<1>, test=<test>)";
        assertEquals(expected, invocation.toString(matchers, new PrintSettings()));
    }

    @Test
    public void testToStringWithMultilinePrintSettings() {
        MockitoMethod mockitoMethod = new MockitoMethod(Invocation.class, "toString", new Class[]{List.class, PrintSettings.class}, new Class[]{String.class}, null);
        Invocation invocation = new Invocation(null, mockitoMethod, new Object[]{new ArrayList<>(), new PrintSettings()}, 1, new RealMethod(mockitoMethod, new Object[0]));
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals(1));
        matchers.add(new Equals("test"));
        String expected = "[Invocation] test(\n1=<1>\n, test=<test>\n)";
        assertEquals(expected, invocation.toString(matchers, new PrintSettings(true)));
    }

    @Test
    public void testToStringWithLongMethodName() {
        MockitoMethod mockitoMethod = new MockitoMethod(Invocation.class, "testLongMethodName", new Class[0], new Class[]{String.class}, null);
        Invocation invocation = new Invocation(null, mockitoMethod, new Object[]{null}, 1, new RealMethod(mockitoMethod, new Object[0]));
        String expected = "[Invocation] testLongMethodName()";
        assertEquals(expected, invocation.toString(new PrintSettings()));
    }

    @Test
    public void testToStringWithLongMethodNameAndMatchers() {
        MockitoMethod mockitoMethod = new MockitoMethod(Invocation.class, "testLongMethodName", new Class[0], new Class[]{String.class}, null);
        Invocation invocation = new Invocation(null, mockitoMethod, new Object[]{null}, 1, new RealMethod(mockitoMethod, new Object[0]));
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals(null));
        String expected = "[Invocation] testLongMethodName()\narguments=\nnull=" + matchers.get(0).toString() + "\n";
        assertEquals(expected, invocation.toString(matchers, new PrintSettings()));
    }

    @Test
    public void testToStringWithArrays() {
        MockitoMethod mockitoMethod = new MockitoMethod(Invocation.class, "toString", new Class[]{List.class, PrintSettings.class}, new Class[]{String.class}, null);
        Object[] arguments = new Object[]{new Object[]{1, 2}, 3, new Object[]{4, 5}};
        Invocation invocation = new Invocation(null, mockitoMethod, new Object[]{new ArrayList<>(), new PrintSettings()}, 1, new RealMethod(mockitoMethod, new Object[0]));
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new ArrayEquals(arguments));
        String expected = "[Invocation] test([1, 2], 3, [4, 5])";
        assertEquals(expected, invocation.toString(matchers, new PrintSettings()));
    }

    @Test
    public void testToStringWithArraysAndPrintSettings() {
        MockitoMethod mockitoMethod = new MockitoMethod(Invocation.class, "toString", new Class[]{List.class, PrintSettings.class}, new Class[]{String.class}, null);
        Object[] arguments = new Object[]{new Object[]{1, 2}, 3, new Object[]{4, 5}};
        Invocation invocation = new Invocation(null, mockitoMethod, new Object[]{new ArrayList<>(), new PrintSettings()}, 1, new RealMethod(mockitoMethod, new Object[0]));
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new ArrayEquals(arguments));
        String expected = "[Invocation] test(\n[1, 2],\n3,\n[4, 5]\n)";
        assertEquals(expected, invocation.toString(matchers, new PrintSettings(true)));
    }
}