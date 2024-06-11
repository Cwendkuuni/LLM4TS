import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setUp() throws Exception {
        // Create a mock Invocation and matchers for testing
        invocation = new Invocation();
        matchers = new ArrayList<>();
    }

    @Test
    public void testConstructorWithMatchers() {
        // Test the constructor with matchers
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        // Test the constructor without matchers
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        // Test the getMethod() method
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocation.getMethod();

        Assert.assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        // Test the toString() method
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        String expected = invocation.toString(matchers, new PrintSettings());
        String actual = invocationMatcher.toString();

        Assert.assertEquals(expected, actual);
    }

    // Add more test cases for other public methods...

}

