import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatcherTest;
import org.mockito.internal.invocation.InvocationWithMethods;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.matchers.ArgumentsMatcher;
import org.mockito.matchers.NullArgMatcher;
import org.mockito.matchers.NullArgumentMatcher;
import org.mockito.matchers.StringArgThat;
import org.mockito.matchers.StringMatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.isA;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithInvocationAndEmptyMatcherList() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
        assertEquals(invocation.getLocation(), invocationMatcher.getLocation());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertTrue(invocationMatcher.matches(invocation));
    }

    // ... Test other methods and constructors here

    @Test
    public void testMatchesWithDifferentTypes() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mock(Invocation.class), Arrays.asList(isA(Integer.class), isA(String.class)));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mock(Invocation.class), Arrays.asList(isA(String.class), isA(Integer.class)));

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    // ... Test edge cases and exception handling scenarios here

    @Test
    public void testStringRepresentation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(mock(Invocation.class), Arrays.asList(isA(String.class), isA(Integer.class)));

        String expectedStringRepresentation = "InvocationMatcher [invocation=MockitoMockInvocationInterceptor$2 at InvocationMatcher.java:64, matchers=[ArgumentMatchers.instanceOf(String.class), ArgumentMatchers.instanceOf(Integer.class)]]";
        String actualStringRepresentation = invocationMatcher.toString();

        assertEquals(expectedStringRepresentation, actualStringRepresentation);
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullMatcherList() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(mock(Invocation.class), null);
        invocationMatcher.toString();
    }

    // ... Other test cases here
}