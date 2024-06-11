import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        assertEquals("expectedMethodName", method.getName());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationAndMatchersList() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(createMatcherOne(), createMatcherTwo());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        assertEquals("expectedMethodName", method.getName());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertEquals(invocation, actualInvocation);
    }

    @Test
    public void testGetMatchers_emptyList() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertEquals(Collections.emptyList(), actualMatchers);
    }

    @Test
    public void testGetMatchers_nonEmptyList() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(createMatcherOne(), createMatcherTwo());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertEquals(matchers, actualMatchers);
    }

    @Test
    public void testMatches_sameInvocation() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, Arrays.asList(createMatcherOne()));

        assertTrue(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testMatches_differentInvocations() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocationWithDifferentMethod();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, Arrays.asList(createMatcherOne()));

        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFrom_varargMethod() {
        Invocation invocation = createMockInvocationWithVarargMethod();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCreateFrom_emptyList() {
        List<Invocation> invocations = Collections.emptyList();
        List<InvocationMatcher> actualInvocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(Collections.emptyList(), actualInvocationMatchers);
    }

    @Test
    public void testCreateFrom_nonEmptyList() {
        List<Invocation> invocations = Arrays.asList(createMockInvocation(), createMockInvocation());
        List<InvocationMatcher> actualInvocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(2, actualInvocationMatchers.size());
    }

    private Invocation createMockInvocation() {
        // Create a mock Invocation object
        // ...
        return mock(Invocation.class);
    }

    private Invocation createMockInvocationWithDifferentMethod() {
        // Create a mock Invocation object with a different method
        // ...
        return mock(Invocation.class);
    }

    private Invocation createMockInvocationWithVarargMethod() {
        // Create a mock Invocation object with a vararg method
        // ...
        return mock(Invocation.class);
    }

    private Matcher createMatcherOne() {
        // Create a mock Matcher object
        // ...
        return mock(Matcher.class);
    }

    private Matcher createMatcherTwo() {
        // Create a mock Matcher object
        // ...
        return mock(Matcher.class);
    }

    private String expectedMethodName = "expectedMethodName";
}