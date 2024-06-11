import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {

    private Invocation invocation;
    private Object mock;
    private MockitoMethod method;
    private Object[] args;
    private int sequenceNumber;
    private RealMethod realMethod;

    @Before
    public void setup() {
        mock = Mockito.mock(Object.class);
        method = Mockito.mock(MockitoMethod.class);
        args = new Object[]{1, "test", null};
        sequenceNumber = 1;
        realMethod = Mockito.mock(RealMethod.class);
        invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        Object actual = invocation.getMock();
        Assert.assertEquals(mock, actual);
    }

    @Test
    public void testGetMethod() {
        MockitoMethod actual = invocation.getMethod();
        Assert.assertEquals(method, actual);
    }

    @Test
    public void testGetArguments() {
        Object[] actual = invocation.getArguments();
        Assert.assertArrayEquals(args, actual);
    }

    @Test
    public void testIsVerified() {
        boolean actual = invocation.isVerified();
        Assert.assertFalse(actual);
    }

    @Test
    public void testGetSequenceNumber() {
        Integer actual = invocation.getSequenceNumber();
        Assert.assertEquals(sequenceNumber, actual.intValue());
    }

    @Test
    public void testIsVerifiedInOrder() {
        boolean actual = invocation.isVerifiedInOrder();
        Assert.assertFalse(actual);
    }

    @Test
    public void testEquals() {
        Invocation other = new Invocation(mock, method, args, sequenceNumber, realMethod);
        boolean actual = invocation.equals(other);
        Assert.assertTrue(actual);
    }

    @Test
    public void testHashCode() {
        try {
            invocation.hashCode();
            Assert.fail("hashCode() should throw RuntimeException");
        } catch (RuntimeException e) {
            Assert.assertNotNull(e);
        }
    }

    @Test
    public void testToString() {
        String expected = "MockName.method(1, \"test\", null)";
        String actual = invocation.toString();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithMatchers() {
        MatchersPrinter matchersPrinter = Mockito.mock(MatchersPrinter.class);
        Mockito.when(matchersPrinter.getArgumentsLine(Mockito.anyList(), Mockito.any()))
                .thenReturn(" (1, \"test\", null)");
        Mockito.when(matchersPrinter.getArgumentsBlock(Mockito.anyList(), Mockito.any()))
                .thenReturn(" (1,\n \"test\",\n null)");
        invocation = Mockito.spy(invocation);

        String expected = "MockName.method(1, \"test\", null)";
        String actual = invocation.toString();
        Assert.assertEquals(expected, actual);

        String expectedBlock = "MockName.method(1,\n \"test\",\n null)";
        String actualBlock = invocation.toString(matchersPrinter, Mockito.mock(PrintSettings.class));
        Assert.assertEquals(expectedBlock, actualBlock);
    }

    @Test
    public void testIsToString() {
        MockitoMethod toStringMethod = Mockito.mock(MockitoMethod.class);
        Mockito.when(toStringMethod.getName()).thenReturn("toString");

        InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationMock.getMethod()).thenReturn(toStringMethod);

        boolean actual = invocation.isToString(invocationMock);
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new RuntimeException();

        MockitoMethod exceptionMethod = Mockito.mock(MockitoMethod.class);
        Mockito.when(exceptionMethod.getExceptionTypes()).thenReturn(new Class[]{Exception.class});

        invocation = new Invocation(mock, exceptionMethod, args, sequenceNumber, realMethod);

        boolean actual = invocation.isValidException(throwable);
        Assert.assertFalse(actual);
    }

    @Test
    public void testIsValidReturnTypeWithPrimitiveType() {
        MockitoMethod returnTypeMethod = Mockito.mock(MockitoMethod.class);
        Mockito.when(returnTypeMethod.getReturnType()).thenReturn(int.class);

        invocation = new Invocation(mock, returnTypeMethod, args, sequenceNumber, realMethod);

        boolean actual = invocation.isValidReturnType(Integer.class);
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsValidReturnTypeWithNonPrimitiveType() {
        MockitoMethod returnTypeMethod = Mockito.mock(MockitoMethod.class);
        Mockito.when(returnTypeMethod.getReturnType()).thenReturn(String.class);

        invocation = new Invocation(mock, returnTypeMethod, args, sequenceNumber, realMethod);

        boolean actual = invocation.isValidReturnType(Object.class);
        Assert.assertTrue(actual);
    }

    @Test
    public void testIsVoid() {
        MockitoMethod voidMethod = Mockito.mock(MockitoMethod.class);
        Mockito.when(voidMethod.getReturnType()).thenReturn(void.class);

        invocation = new Invocation(mock, voidMethod, args, sequenceNumber, realMethod);

        boolean actual = invocation.isVoid();
        Assert.assertTrue(actual);
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod returnTypeMethod = Mockito.mock(MockitoMethod.class);
        Mockito.when(returnTypeMethod.getReturnType()).thenReturn(String.class);

        invocation = new Invocation(mock, returnTypeMethod, args, sequenceNumber, realMethod);

        String expected = "String";
        String actual = invocation.printMethodReturnType();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod methodNameMethod = Mockito.mock(MockitoMethod.class);
        Mockito.when(methodNameMethod.getName()).thenReturn("methodName");

        invocation = new Invocation(mock, methodNameMethod, args, sequenceNumber, realMethod);

        String expected = "methodName";
        String actual = invocation.getMethodName();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReturnsPrimitive() {
        MockitoMethod primitiveMethod = Mockito.mock(MockitoMethod.class);
        Mockito.when(primitiveMethod.getReturnType()).thenReturn(int.class);

        invocation = new Invocation(mock, primitiveMethod, args, sequenceNumber, realMethod);

        boolean actual = invocation.returnsPrimitive();
        Assert.assertTrue(actual);
    }

    @Test
    public void testGetLocation() {
        Location location = invocation.getLocation();
        Assert.assertNotNull(location);
    }

    @Test
    public void testGetArgumentsCount() {
        int expected = args.length;
        int actual = invocation.getArgumentsCount();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetRawArguments() {
        Object[] actual = invocation.getRawArguments();
        Assert.assertArrayEquals(args, actual);
    }

    @Test
    public void testCallRealMethod() {
        try {
            invocation.callRealMethod();
            Assert.fail("callRealMethod() should throw Throwable");
        } catch (Throwable e) {
            Assert.assertNotNull(e);
        }
    }

    @Test
    public void testToStringWithPrintSettings() {
        MockitoMethod printSettingsMethod = Mockito.mock(MockitoMethod.class);
        Mockito.when(printSettingsMethod.getName()).thenReturn("printMethod");

        PrintSettings printSettings = Mockito.mock(PrintSettings.class);
        Mockito.when(printSettings.isMultiline()).thenReturn(false);

        invocation = new Invocation(mock, printSettingsMethod, args, sequenceNumber, realMethod);

        String expected = "MockName.printMethod(1, \"test\", null)";
        String actual = invocation.toString(printSettings);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testMarkVerified() {
        Assert.assertFalse(invocation.isVerified());
        invocation.markVerified();
        Assert.assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Assert.assertFalse(invocation.isVerified());
        Assert.assertFalse(invocation.isVerifiedInOrder());
        invocation.markVerifiedInOrder();
        Assert.assertTrue(invocation.isVerified());
        Assert.assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testExpandVarArgsWithoutVarArgs() {
        boolean isVarArgs = false;
        Object[] args = new Object[]{1, 2, 3};

        Object[] actual = Invocation.expandVarArgs(isVarArgs, args);
        Assert.assertArrayEquals(args, actual);
    }

    @Test
    public void testExpandVarArgsWithVarArgs() {
        boolean isVarArgs = true;
        Object[] args = new Object[]{1, new Object[]{2, 3}};

        Object[] actual = Invocation.expandVarArgs(isVarArgs, args);
        Object[] expected = new Object[]{1, 2, 3};
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testExpandVarArgsWithNullVarArgs() {
        boolean isVarArgs = true;
        Object[] args = new Object[]{1, null};

        Object[] actual = Invocation.expandVarArgs(isVarArgs, args);
        Object[] expected = new Object[]{1, null};
        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testExpandVarArgsWithDeliberateNullVarArgsArray() {
        boolean isVarArgs = true;
        Object[] args = new Object[]{1, new Object[]{null}};

        Object[] actual = Invocation.expandVarArgs(isVarArgs, args);
        Object[] expected = new Object[]{1, new Object[]{null}};
        Assert.assertArrayEquals(expected, actual);
    }

}