import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockitoMethod;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.util.ArrayEquals;
import org.mockito.internal.util.Equals;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.RealMethod;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationTest {

    private Invocation invocation;

    @Before
    public void setup() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();

        invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        assertEquals(new Object(), invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        assertEquals(new MockitoMethod(), invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        assertArrayEquals(new Object[]{}, invocation.getArguments());
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
        Invocation other = new Invocation(new Object(), new MockitoMethod(), new Object[]{}, 1, new RealMethod());

        assertTrue(invocation.equals(other));
    }

    @Test
    public void testEqualArguments() {
        assertTrue(invocation.equalArguments(new Object[]{}));
        assertFalse(invocation.equalArguments(new Object[]{new Object()}));
    }

    @Test
    public void testToString() {
        String expected = new MockUtil().getMockName(new Object()) + "." + new MockitoMethod().getName();
        String actual = invocation.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsValidException() {
        assertFalse(invocation.isValidException(new Throwable()));
    }

    @Test
    public void testIsValidReturnType() {
        assertFalse(invocation.isValidReturnType(Object.class));
    }

    @Test
    public void testIsVoid() {
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        assertEquals("", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        assertEquals("", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        assertEquals(0, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        assertArrayEquals(new Object[]{}, invocation.getRawArguments());
    }

    @Test(expected = Throwable.class)
    public void testCallRealMethod() throws Throwable {
        invocation.callRealMethod();
    }

    @Test
    public void testToStringWithPrintSettings() {
        String expected = new MockUtil().getMockName(new Object()) + "." + new MockitoMethod().getName();
        PrintSettings printSettings = new PrintSettings();
        String actual = invocation.toString(printSettings);

        assertEquals(expected, actual);
    }

    @Test
    public void testMarkVerified() {
        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        invocation.markVerifiedInOrder();

        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }
}