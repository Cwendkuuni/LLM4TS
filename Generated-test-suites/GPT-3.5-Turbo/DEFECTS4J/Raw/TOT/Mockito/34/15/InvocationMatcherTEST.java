import org.junit.Test;
import org.junit.Assert;
import org.hamcrest.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        // Arrange
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new IsEqual(1));

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Assert
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        // Arrange
        Invocation invocation = new Invocation();

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Assert
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        // Arrange
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        Method method = invocationMatcher.getMethod();

        // Assert
        Assert.assertEquals(invocation.getMethod(), method);
    }

    @Test
    public void testToString() {
        // Arrange
        Invocation invocation = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        String toString = invocationMatcher.toString();

        // Assert
        Assert.assertEquals(invocation.toString(matchers, new PrintSettings()), toString);
    }

    @Test
    public void testMatches() {
        // Arrange
        Invocation invocation = new Invocation();
        Object[] args = new Object[] {1, 2, 3};
        Invocation actual = new Invocation(invocation.getMock(), invocation.getMethod(), args);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Act
        boolean matches = invocationMatcher.matches(actual);

        // Assert
        Assert.assertTrue(matches);
    }

    @Test
    public void testMatchesWithDifferentMock() {
        // Arrange
        Invocation invocation = new Invocation();
        Object[] args = new Object[] {1, 2, 3};
        Invocation actual = new Invocation(new Mock(), invocation.getMethod(), args);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Act
        boolean matches = invocationMatcher.matches(actual);

        // Assert
        Assert.assertFalse(matches);
    }

    @Test
    public void testHasSimilarMethod() {
        // Arrange
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Act
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        Assert.assertTrue(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        // Arrange
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        candidate.setMethodName("different");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Act
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        Assert.assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWithVerifiedCandidate() {
        // Arrange
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        candidate.setVerified(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Act
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        Assert.assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        // Arrange
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        candidate.setMock(new Mock());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Act
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        Assert.assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWithOverloadedButSameArgs() {
        // Arrange
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        Object[] args = new Object[] {1, 2, 3};
        candidate.setArguments(args);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Act
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        Assert.assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSameMethod() {
        // Arrange
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Act
        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        // Assert
        Assert.assertTrue(hasSameMethod);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Arrange
        Invocation invocation = new Invocation();
        Invocation i = new Invocation();
        Object[] args = new Object[] {1, 2, 3};
        i.setArguments(args);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Act
        invocationMatcher.captureArgumentsFrom(i);

        // Assert
        Assert.assertEquals(1, ((CapturesArguments) matchers.get(0)).capturedValue());
        Assert.assertEquals(2, ((CapturesArguments) matchers.get(1)).capturedValue());
        Assert.assertEquals(3, ((CapturesArguments) matchers.get(2)).capturedValue());
    }

}