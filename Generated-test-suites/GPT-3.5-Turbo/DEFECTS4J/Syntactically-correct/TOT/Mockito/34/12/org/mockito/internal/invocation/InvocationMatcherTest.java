import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.TypeSafeMatcher;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithMatchers() {
        Invocation invocation = new Invocation(null, null, null, null);
        TypeSafeMatcher<Invocation> matcher = new TypeSafeMatcher<Invocation>() {
            @Override
            protected boolean matchesSafely(Invocation item) {
                return true;
            }

            @Override
            public void describeTo(Description description) {
            }
        };

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));

        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(Collections.singletonList(matcher), invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcherConstructorWithoutMatchers() {
        Invocation invocation = new Invocation(null, null, null, null);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = Foo.class.getMethod("bar");
        Invocation invocation = new Invocation(null, expectedMethod, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method actualMethod = invocationMatcher.getMethod();

        Assert.assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testMatches_WithEqualInvocation_ReturnsTrue() {
        Invocation invocation = new Invocation(Mockito.mock(Foo.class), null, null, null);
        Invocation actual = new Invocation(Mockito.mock(Foo.class), null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.matches(actual);

        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_WithDifferentMock_ReturnsFalse() {
        Invocation invocation = new Invocation(Mockito.mock(Foo.class), null, null, null);
        Invocation actual = new Invocation(Mockito.mock(Foo.class), null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.matches(actual);

        Assert.assertFalse(result);
    }

    @Test
    public void testMatches_WithDifferentMethod_ReturnsFalse() throws Exception {
        Invocation invocation = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar"), null, null);
        Invocation actual = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("baz"), null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.matches(actual);

        Assert.assertFalse(result);
    }

    @Test
    public void testMatches_WithSameMethodAndSameArguments_ReturnsTrue() throws Exception {
        Invocation invocation = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar", String.class), new Object[]{"baz"}, null);
        Invocation actual = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar", String.class), new Object[]{"baz"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.matches(actual);

        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_WithSameMethodAndDifferentArguments_ReturnsFalse() throws Exception {
        Invocation invocation = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar", String.class), new Object[]{"baz"}, null);
        Invocation actual = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar", String.class), new Object[]{"qux"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.matches(actual);

        Assert.assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMethodName_ReturnsFalse() throws Exception {
        Invocation invocation = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar"), null, null);
        Invocation candidate = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("baz"), null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WithDifferentVerificationStatus_ReturnsFalse() throws Exception {
        Invocation invocation = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar"), null, null);
        Invocation candidate = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar"), null, null);
        candidate.setVerified(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMock_ReturnsFalse() throws Exception {
        Invocation invocation = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar"), null, null);
        Invocation candidate = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar"), null, null);
        candidate.setMock(Mockito.mock(Foo.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WithSameMethodAndDifferentArguments_ReturnsTrue() throws Exception {
        Invocation invocation = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar", String.class), new Object[]{"baz"}, null);
        Invocation candidate = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar", String.class), new Object[]{"qux"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_WithOverloadedAndSameArguments_ReturnsFalse() throws Exception {
        Invocation invocation = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar", String.class), new Object[]{"baz"}, null);
        Invocation candidate = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar", String.class), new Object[]{"baz"}, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        Assert.assertFalse(result);
    }

    @Test
    public void testHasSameMethod_WithSameMethod_ReturnsTrue() throws Exception {
        Invocation invocation = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar"), null, null);
        Invocation candidate = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar"), null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.hasSameMethod(candidate);

        Assert.assertTrue(result);
    }

    @Test
    public void testHasSameMethod_WithDifferentMethod_ReturnsFalse() throws Exception {
        Invocation invocation = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("bar"), null, null);
        Invocation candidate = new Invocation(Mockito.mock(Foo.class), Foo.class.getMethod("baz"), null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.hasSameMethod(candidate);

        Assert.assertFalse(result);
    }

    @Test
    public void testGetLocation() throws Exception {
        Location expectedLocation = new Location(null);
        Invocation invocation = new Invocation(Mockito.mock(Foo.class), null, null, null);
        invocation.setLocation(expectedLocation);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Location actualLocation = invocationMatcher.getLocation();

        Assert.assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testToString_WithDefaultPrintSettings() throws Exception {
        String expectedString = "InvocationMatcher.toString()";
        Invocation invocation = new Invocation(Mockito.mock(Foo.class), null, null, null);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        PrintSettings printSettings = new PrintSettings();

        String actualString = invocationMatcher.toString(printSettings);

        Assert.assertEquals(expectedString, actualString);
    }

    @Test
    public void testCaptureArgumentsFrom() throws Exception {
        Invocation invocation = new Invocation(Mockito.mock(Foo.class), null, new Object[]{"baz"}, null);
        Invocation i = new Invocation(null, null, new Object[]{"qux"}, null);
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(Object.class);
        Mockito.when(i.getArguments()).thenReturn(new Object[]{"qux"});
        Matchers.any(Class.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(i);

        Mockito.verify(captor).captureFrom(Mockito.any());

    }
}

class Foo {
    public void bar() {
    }

    public void bar(String arg) {
    }

    public void baz() {
    }
}