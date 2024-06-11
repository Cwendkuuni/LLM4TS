import org.junit.Assert;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.MockitoInvocation;
import org.mockito.matchers.ArgumentMatcher;

import java.lang.reflect.Method;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        // Arrange
        Invocation invocation = new MockitoInvocation(null, null, null, null);
        ArgumentMatcher matcher = new ArgumentMatcher() {
            @Override
            public boolean matches(Object o) {
                return true;
            }
        };

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));

        // Assert
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(Collections.singletonList(matcher), invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        // Arrange
        Invocation invocation = new MockitoInvocation(null, null, null, null);

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Assert
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        // Arrange
        Invocation invocation = new MockitoInvocation(null, null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        String toString = invocationMatcher.toString();

        // Assert
        Assert.assertEquals(invocation.toString(invocationMatcher.getMatchers(), new PrintSettings()), toString);
    }

    @Test
    public void testMatches() {
        // Arrange
        Invocation invocation1 = new MockitoInvocation(null, null, null, new Object[]{"arg1"});
        Invocation invocation2 = new MockitoInvocation(null, null, null, new Object[]{"arg2"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Act
        boolean matches1 = invocationMatcher.matches(invocation1);
        boolean matches2 = invocationMatcher.matches(invocation2);

        // Assert
        Assert.assertTrue(matches1);
        Assert.assertFalse(matches2);
    }

    @Test
    public void testHasSimilarMethod() {
        // Arrange
        Invocation invocation1 = new MockitoInvocation(null, null, null, null);
        Invocation invocation2 = new MockitoInvocation(null, null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Act
        boolean hasSimilarMethod1 = invocationMatcher.hasSimilarMethod(invocation1);
        boolean hasSimilarMethod2 = invocationMatcher.hasSimilarMethod(invocation2);

        // Assert
        Assert.assertTrue(hasSimilarMethod1);
        Assert.assertTrue(hasSimilarMethod2);
    }

    @Test
    public void testHasSameMethod() {
        // Arrange
        Invocation invocation1 = new MockitoInvocation(null, null, null, null);
        Invocation invocation2 = new MockitoInvocation(null, null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Act
        boolean hasSameMethod1 = invocationMatcher.hasSameMethod(invocation1);
        boolean hasSameMethod2 = invocationMatcher.hasSameMethod(invocation2);

        // Assert
        Assert.assertTrue(hasSameMethod1);
        Assert.assertTrue(hasSameMethod2);
    }

    @Test
    public void testGetLocation() {
        // Arrange
        Invocation invocation = new MockitoInvocation(null, null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        Location location = invocationMatcher.getLocation();

        // Assert
        Assert.assertEquals(invocation.getLocation(), location);
    }

    @Test
    public void testToStringWithPrintSettings() {
        // Arrange
        Invocation invocation = new MockitoInvocation(null, null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        String toString = invocationMatcher.toString(new PrintSettings());

        // Assert
        Assert.assertEquals(invocation.toString(invocationMatcher.getMatchers(), new PrintSettings()), toString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Arrange
        Invocation invocation1 = new MockitoInvocation(null, null, null, new Object[]{"arg1"});
        Invocation invocation2 = new MockitoInvocation(null, null, null, new Object[]{"arg2"});
        ArgumentMatcher matcher = new ArgumentMatcher() {
            @Override
            public boolean matches(Object o) {
                return true;
            }
        };
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, Collections.singletonList(matcher));

        // Act
        invocationMatcher.captureArgumentsFrom(invocation2);

        // Assert
        Assert.assertEquals("arg2", invocationMatcher.getMatchers().get(0).toString());
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // Arrange
        Invocation invocation = new MockitoInvocation(null, null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object[] args = new Object[]{"arg1"};

        // Act
        boolean safelyArgumentsMatch = invocationMatcher.safelyArgumentsMatch(args);

        // Assert
        Assert.assertTrue(safelyArgumentsMatch);
    }
}