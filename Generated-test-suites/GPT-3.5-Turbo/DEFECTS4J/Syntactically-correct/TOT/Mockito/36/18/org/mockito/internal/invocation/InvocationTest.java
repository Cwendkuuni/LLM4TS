import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.util.ArrayEquals.*;

import org.hamcrest.Matcher;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Test;
import org.mockito.exceptions.verification.junit.ArgumentsAreDifferent;
import org.mockito.exceptions.verification.junit.AssertionErrorWithCause;
import org.mockito.exceptions.verification.junit.CannotVerifyStubOnlyMock;
import org.mockito.exceptions.verification.junit.DifferentArgumentsFound;
import org.mockito.exceptions.verification.junit.WantedButNotInvoked;
import org.mockito.internal.exceptions.Reporter;
import org.mockito.internal.invocation.Invocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.*;
import org.mockito.internal.debugging.Location;

import java.util.*;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(args, invocation.getArguments());
        assertFalse(invocation.isVerified());
        assertEquals(Integer.valueOf(1), invocation.getSequenceNumber());
        assertFalse(invocation.isVerifiedInOrder());
        assertEquals(realMethod, invocation.realMethod);
    }

    @Test
    public void testEquals() {
        Object mock1 = mock(Object.class);
        MockitoMethod mockitoMethod1 = new MockitoMethod();
        Object[] args1 = {1, 2, 3};
        Invocation invocation1 = new Invocation(mock1, mockitoMethod1, args1, 1, new RealMethod());

        Object mock2 = mock(Object.class);
        MockitoMethod mockitoMethod2 = new MockitoMethod();
        Object[] args2 = {1, 2, 3};
        Invocation invocation2 = new Invocation(mock2, mockitoMethod2, args2, 1, new RealMethod());

        assertFalse(invocation1.equals(invocation2));

        Object mock3 = mock(Object.class);
        MockitoMethod mockitoMethod3 = new MockitoMethod();
        Object[] args3 = {1, 2, 3};
        Invocation invocation3 = new Invocation(mock3, mockitoMethod3, args3, 1, new RealMethod());

        assertTrue(invocation1.equals(invocation3));
    }

    @Test
    public void testHashCode() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = {1, 2, 3};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod());

        assertThrows(RuntimeException.class, invocation::hashCode);
    }

    @Test
    public void testToString() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = {1, 2, 3};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod());

        assertEquals("Object.getName()", invocation.toString());
    }

    @Test
    public void testArgumentsToMatchers() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = {1, new int[]{2, 3}, 4};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod());

        List<Matcher> matchers = invocation.argumentsToMatchers();

        assertEquals(3, matchers.size());
        assertTrue(matchers.get(0) instanceof Equals);
        assertTrue(matchers.get(1) instanceof ArrayEquals);
        assertTrue(matchers.get(2) instanceof Equals);
    }

    @Test
    public void testIsToString() {
        Object mock1 = mock(Object.class);
        MockitoMethod mockitoMethod1 = new MockitoMethod("toString", null);
        Invocation invocation1 = new Invocation(mock1, mockitoMethod1, null, 1, new RealMethod());

        assertTrue(invocation1.isToString());

        Object mock2 = mock(Object.class);
        MockitoMethod mockitoMethod2 = new MockitoMethod("notToString", null);
        Invocation invocation2 = new Invocation(mock2, mockitoMethod2, null, 1, new RealMethod());

        assertFalse(invocation2.isToString());
    }

    @Test
    public void testIsValidException() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = {1, 2, 3};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod());

        Throwable throwable = new Throwable();

        assertFalse(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = {1, 2, 3};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod());

        assertFalse(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = {1, 2, 3};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod());

        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = {1, 2, 3};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod());

        assertEquals("void", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod("getName", null);
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod());

        assertEquals("getName", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = {1, 2, 3};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod());

        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = {1, 2, 3};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod());

        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = {1, 2, 3};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod());

        assertEquals(3, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = {1, 2, 3};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod());

        assertArrayEquals(args, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable{
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = {1, 2, 3};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod());

        assertNull(invocation.callRealMethod());
    }

    @Test
    public void testMarkVerified() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = {1, 2, 3};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod());

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = {1, 2, 3};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, new RealMethod());

        invocation.markVerifiedInOrder();

        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }

}