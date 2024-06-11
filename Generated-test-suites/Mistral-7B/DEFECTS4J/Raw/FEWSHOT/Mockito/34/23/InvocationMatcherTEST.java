package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argument;
import static org.mockito.internal.matchers.ArgumentsMatcher.equalTo;
import static org.mockito.internal.matchers.ArgumentsMatcher.not;
import static org.mockito.internal.matchers.ArgumentsMatcher.same;
import static org.mockito.internal.matchers.ArgumentsMatcher.uniqueInstance;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.InvocationNotVerifiedException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

public class InvocationMatcherTest {

    @Test
    public void testNewInstance() {
        Location location = new Location("TestClass.java", 1, 1);
        Method method = MockitoTestUtils.getMethod(TestClass.class, "someMethod", int.class);
        Object[] arguments = new Object[]{1};

        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(location, method, arguments));
        assertNotNull(invocationMatcher);
    }

    @Test
    public void testNewInstanceWithMatchers() {
        Location location = new Location("TestClass.java", 1, 1);
        Method method = MockitoTestUtils.getMethod(TestClass.class, "someMethod", int.class);
        Object[] arguments = new Object[]{1};
        List<Matcher> matchers = Arrays.asList(equalTo(1), not(equalTo(2)));

        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(location, method, arguments), matchers);
        assertNotNull(invocationMatcher);
    }

    @Test
    public void testNewInstanceWithoutMatchers() {
        Location location = new Location("TestClass.java", 1, 1);
        Method method = MockitoTestUtils.getMethod(TestClass.class, "someMethod", int.class);
        Object[] arguments = new Object[]{1};

        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(location, method, arguments));
        assertNotNull(invocationMatcher);
        assertNull(invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Location location = new Location("TestClass.java", 1, 1);
        Method method = MockitoTestUtils.getMethod(TestClass.class, "someMethod", int.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(location, method, new Object[]{1}));

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        Location location = new Location("TestClass.java", 1, 1);
        Method method = MockitoTestUtils.getMethod(TestClass.class, "someMethod", int.class);
        Object[] arguments = new Object[]{1};
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(location, method, arguments));

        assertEquals(new InvocationOnMock(location, method, arguments), invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Location location = new Location("TestClass.java", 1, 1);
        Method method = MockitoTestUtils.getMethod(TestClass.class, "someMethod", int.class);
        Object[] arguments = new Object[]{1};
        List<Matcher> matchers = Arrays.asList(equalTo(1), not(equalTo(2)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(location, method, arguments), matchers);

        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        Location location = new Location("TestClass.java", 1, 1);
        Method method = MockitoTestUtils.getMethod(TestClass.class, "someMethod", int.class);
        Object[] arguments = new Object[]{1};
        List<Matcher> matchers = Arrays.asList(argument(0), equalTo(1), not(equalTo(2)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(location, method, arguments), matchers);

        String expected = "Invocation on TestClass.someMethod(int) with arguments:\n"
                + "0: <1>\n"
                + "1: <1>\n"
                + "2: <not 2>\n";
        String actual = invocationMatcher.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testMatches() {
        Location location1 = new Location("TestClass.java", 1, 1);
        Location location2 = new Location("TestClass.java", 2, 2);

        Method method = MockitoTestUtils.getMethod(TestClass.class, "someMethod", int.class);
        Object[] arguments1 = new Object[]{1};
        Object[] arguments2 = new Object[]{2};

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMock(location1, method, arguments1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(location2, method, arguments2));

        assertTrue(invocationMatcher1.matches(invocationMatcher1));
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Location location = new Location("TestClass.java", 1, 1);
        Method method = MockitoTestUtils.getMethod(TestClass.class, "someMethod", int[].class);
        Object[] arguments = new Object[]{new int[]{1, 2}};

        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(location, method, arguments));
        assertTrue(invocationMatcher.safelyArgumentsMatch(new int[]{1, 2}));
    }

    @Test(expected = InvocationNotVerifiedException.class)
    public void testSafelyArgumentsMatchThrowsException() {
        Location location = new Location("TestClass.java", 1, 1);
        Method method = MockitoTestUtils.getMethod(TestClass.class, "someMethod", int.class);
        Object[] arguments = new Object[]{1};

        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(location, method, arguments));
        invocationMatcher.safelyArgumentsMatch(new Object[]{2});
    }

    @Test
    public void testHasSimilarMethod() {
        Location location1 = new Location("TestClass.java", 1, 1);
        Location location2 = new Location("TestClass.java", 2, 2);

        Method method1 = MockitoTestUtils.getMethod(TestClass.class, "someMethod1", int.class);
        Method method2 = MockitoTestUtils.getMethod(TestClass.class, "someMethod2", int.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMock(location1, method1, new Object[]{1}));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(location2, method2, new Object[]{1}));

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));

        Method method3 = MockitoTestUtils.getMethod(TestClass.class, "someMethod1", int.class);
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(new InvocationOnMock(location1, method3, new Object[]{1}));
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher3));
    }

    @Test
    public void testHasSameMethod() {
        Location location = new Location("TestClass.java", 1, 1);

        Method method1 = MockitoTestUtils.getMethod(TestClass.class, "someMethod1", int.class);
        Method method2 = MockitoTestUtils.getMethod(TestClass.class, "someMethod1", int.class);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMock(location, method1, new Object[]{1}));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(location, method2, new Object[]{1}));

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testGetLocation() {
        Location location = new Location("TestClass.java", 1, 1);
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(location, MockitoTestUtils.getMethod(TestClass.class, "someMethod", int.class), new Object[]{1}));

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Location location = new Location("TestClass.java", 1, 1);
        Method method = MockitoTestUtils.getMethod(TestClass.class, "someMethod", int.class);
        Object[] arguments = new Object[]{1};
        List<Matcher> matchers = Arrays.asList(argument(0), equalTo(1), not(equalTo(2)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(location, method, arguments), matchers);

        PrintSettings printSettings = new PrintSettings();
        printSettings.setSuppressed(false);

        String expected = "Invocation on TestClass.someMethod(int) with arguments:\n"
                + "0: <1>\n";
        String actual = invocationMatcher.toString(printSettings);
        assertEquals(expected, actual);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Location location = new Location("TestClass.java", 1, 1);
        Method method = MockitoTestUtils.getMethod(TestClass.class, "someMethod", int.class);
        Object[] arguments = new Object[]{1};
        List<Matcher> matchers = Arrays.asList(argument(0), equalTo(1), not(equalTo(2)));

        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(location, method, arguments), matchers);
        int sizeBefore = invocationMatcher.getMatchers().size();

        Stubber stubber = Mockito.stub();
        Invocation invocation = stubber.invoke(uniqueInstance(TestClass.class), 1);

        invocationMatcher.captureArgumentsFrom(invocation);

        int sizeAfter = invocationMatcher.getMatchers().size();
        assertEquals(sizeBefore + 1, sizeAfter);

        Matcher matcher = invocationMatcher.getMatchers().get(sizeBefore);
        assertTrue(matcher instanceof CapturesArguments);
        assertEquals(1, ((CapturesArguments) matcher).getCapturedValue().size());
        assertEquals(invocation.getArguments()[0], ((CapturesArguments) matcher).getCapturedValue().get(0));
    }
}