import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[]{String.class, Integer.class}), "mockito", new Object[]{new String("test"), 42});
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Invocation expectedInvocation = invocation;
        Invocation actualInvocation = matcher.getInvocation();
        Assert.assertEquals(expectedInvocation, actualInvocation);
    }

    @Test
    public void testMatchesWithDifferentArguments() {
        InvocationMatcher matcher1 = new InvocationMatcher(new Invocation(new Method("testMethod", new Class[]{String.class, Integer.class}), "mockito1", new Object[]{"test1", 43}), Collections.emptyList());
        InvocationMatcher matcher2 = new InvocationMatcher(new Invocation(new Method("testMethod", new Class[]{String.class, Double.class}), "mockito2", new Object[]{"test2", 4.5}), Collections.emptyList());
        Assert.assertFalse(matcher1.matches(matcher2));
    }

    @Test(expected = NullPointerException.class)
    public void testToStringWithNullPrintSettings() {
        InvocationMatcher matcher = new InvocationMatcher(new Invocation(new Method("testMethod", new Class[]{String.class, Integer.class}), "mockito", new Object[]{"test", 42}), Collections.emptyList());
        matcher.toString(null);
    }

    @Test
    public void testToString() {
        InvocationMatcher matcher = new InvocationMatcher(new Invocation(new Method("testMethod", new Class[]{String.class, Integer.class}), "mockito", new Object[]{"test", 42}), Collections.emptyList());
        PrintSettings printSettings = new PrintSettings();
        String expectedString = "InvocationMatcher: mockito.testMethod(String test, Integer 42)";
        String actualString = matcher.toString(printSettings);
        Assert.assertEquals(expectedString, actualString);
    }
}