import org.apache.commons.cli.Util;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Mock
    Invocation invocation;

    @Test
    public void testConstructor() {
        assertNotNull(new InvocationMatcher(invocation, Collections.emptyList()));
    }

    @Test
    public void testConstructorWithMatchers() {
        List<Matcher> matchers = Arrays.asList(Matchers.anyString(), Matchers.anyInt());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Method method = Mockito.mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = Mockito.mock(PrintSettings.class);
        Mockito.when(printSettings.print(Mockito.anyList(), Mockito.any(Invocation.class))).thenReturn("Mockito");

        InvocationMatcher invocationMatcher = Mockito.spy(new InvocationMatcher(invocation));
        Mockito.when(invocationMatcher.getMatchers()).thenReturn(Collections.emptyList());
        Mockito.whenNew(PrintSettings.class).withNoArguments().thenReturn(printSettings);

        assertEquals("Mockito", invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn("Mockito");
        Mockito.when(actualInvocation.getMock()).thenReturn("Mockito");
        Mockito.when(invocation.getMethod()).thenReturn(Util.stripLeadingAndTrailingQuotes("testMethod"));
        Mockito.when(actualInvocation.getMethod()).thenReturn(Util.stripLeadingAndTrailingQuotes("testMethod"));
        Mockito.when(new ArgumentsComparator().argumentsMatch(Mockito.any(), Mockito.any())).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidateInvocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(Util.stripLeadingAndTrailingQuotes("testMethod"));
        Mockito.when(candidateInvocation.getMethod()).thenReturn(Util.stripLeadingAndTrailingQuotes("testMethod"));
        Mockito.when(candidateInvocation.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn("Mockito");
        Mockito.when(candidateInvocation.getMock()).thenReturn("Mockito");
        Mockito.when(new ArgumentsComparator().argumentsMatch(Mockito.any(), Mockito.any())).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidateInvocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(Util.stripLeadingAndTrailingQuotes("testMethod"));
        Mockito.when(candidateInvocation.getMethod()).thenReturn(Util.stripLeadingAndTrailingQuotes("testMethod"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertTrue(invocationMatcher.hasSameMethod(candidateInvocation));
    }

    @Test
    public void testGetLocation() {
        Location location = Mockito.mock(Location.class);
        Mockito.when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        invocationMatcher.captureArgumentsFrom(invocation);

        //TODO: Add assertions
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(Mockito.mock(Invocation.class), Mockito.mock(Invocation.class));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocations.size(), invocationMatchers.size());
    }
}