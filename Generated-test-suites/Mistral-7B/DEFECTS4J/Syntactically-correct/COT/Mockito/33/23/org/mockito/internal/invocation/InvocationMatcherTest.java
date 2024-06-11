import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();
        List<Matcher> matchers = invocationMatcher.getMatchers();

        Assert.assertEquals(method.getName(), "testMethod");
        Assert.assertEquals(matchers.size(), 0);
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0].valueOf(String.class), new Class[0].valueOf(Integer.class)), arguments);
        List<Matcher> matchers = Arrays.asList(new IsString(), new IsInstanceOf(Integer.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method method = invocationMatcher.getMethod();
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        Assert.assertEquals(method.getName(), "testMethod");
        Assert.assertEquals(actualMatchers.size(), 2);
        Assert.assertTrue(actualMatchers.get(0) instanceof IsString);
        Assert.assertTrue(actualMatchers.get(1) instanceof IsInstanceOf);
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = invocationMatcher.getMethod();

        Assert.assertEquals(method.getName(), "testMethod");
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Invocation actualInvocation = invocationMatcher.getInvocation();

        Assert.assertEquals(invocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]));
        List<Matcher> matchers = Arrays.asList(new IsString(), new IsInstanceOf(Integer.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        Assert.assertEquals(matchers, actualMatchers);
    }

    @Test
    public void testToString() {
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0].valueOf(String.class), new Class[0].valueOf(Integer.class)), arguments);
        List<Matcher> matchers = Arrays.asList(new IsString(), new IsInstanceOf(Integer.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expectedToString = "InvocationMatcher: testMethod(String, Integer)";

        String actualToString = invocationMatcher.toString();

        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = new Invocation(new Object(), new Method("testMethod", new Class[0]));
        Invocation invocation2 = new Invocation(new Object(), new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertTrue(invocationMatcher1.matches(invocation1));
        Assert.assertTrue(invocationMatcher1.matches(invocation2));
        Assert.assertTrue(invocationMatcher2.matches(invocation2));
        Assert.assertFalse(invocationMatcher1.matches(new Invocation(new Object(), new Method("testOtherMethod", new Class[0]))));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] arguments1 = new Object[]{1, "test"};
        Object[] arguments2 = new Object[]{2, "test"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0].valueOf(String.class), new Class[0].valueOf(Integer.class)), arguments1));

        Assert.assertTrue(invocationMatcher.safelyArgumentsMatch(arguments1));
        Assert.assertFalse(invocationMatcher.safelyArgumentsMatch(arguments2));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithException() {
        Object[] arguments = new Object[]{1};
        InvocationMatcher invocationMatcher = new InvocationMatcher(new Invocation(new Object(), new Method("testMethod", new Class[0].valueOf(String.class), new Class[0].valueOf(Integer.class)), arguments));

        invocationMatcher.safelyArgumentsMatch(new Object[0]);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new Invocation(new Object(), new Method("testMethod", new Class[0]));
        Invocation invocation2 = new Invocation(new Object(), new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation(new Object(), new Method("testMethod", new Class[0]));
        Invocation invocation2 = new Invocation(new Object(), new Method("testMethod", new Class[0]));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        Assert.assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0]), new Location("TestClass.java", 10, 15));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location actualLocation = invocationMatcher.getLocation();

        Assert.assertEquals(new Location("TestClass.java", 10, 15), actualLocation);
    }

    @Test
    public void testToStringWithPrintSettings() {
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0].valueOf(String.class), new Class[0].valueOf(Integer.class)), arguments);
        List<Matcher> matchers = Arrays.asList(new IsString(), new IsInstanceOf(Integer.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = new PrintSettings();

        String expectedToString = "InvocationMatcher: testMethod(String, Integer)\n" +
                "\tActual arguments:\n" +
                "\t\t[\"test\", 1]\n" +
                "\tExpected arguments:\n" +
                "\t\t[<String>, <Integer>]\n";

        String actualToString = invocationMatcher.toString(printSettings);

        Assert.assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(new Object(), new Method("testMethod", new Class[0].valueOf(String.class), new Class[0].valueOf(Integer.class)), arguments);
        List<Matcher> matchers = Arrays.asList(new HasArgumentThat(new ArgumentCaptor<>()));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);

        invocationMatcher.captureArgumentsFrom(invocation);

        Object capturedString = stringArgumentCaptor.getValue();
        Object capturedInteger = integerArgumentCaptor.getValue();

        Assert.assertEquals("test", capturedString);
        Assert.assertEquals(1, capturedInteger.intValue());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        Invocation invocation1 = new Invocation(new Object(), new Method("testMethod1", new Class[0].valueOf(String.class), new Class[0].valueOf(Integer.class)), new Object[]{1, "test1"});
        Invocation invocation2 = new Invocation(new Object(), new Method("testMethod2", new Class[0].valueOf(String.class), new Class[0].valueOf(Integer.class)), new Object[]{2, "test2"});
        invocations.add(invocation1);
        invocations.add(invocation2);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        Assert.assertEquals(invocation1.getMethod().getName(), invocationMatcher1.getMethod().getName());
        Assert.assertEquals(invocation2.getMethod().getName(), invocationMatcher2.getMethod().getName());
    }
}