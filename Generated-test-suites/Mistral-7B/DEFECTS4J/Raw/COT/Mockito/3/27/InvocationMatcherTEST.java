import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockImpl;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class InvocationMatcherTest {

    private List<InvocationOnMock> invocations;

    @Before
    public void setup() {
        invocations = Arrays.asList(new InvocationOnMockImpl(new Object(), new Method(null, "testMethod", new Class[0])),
                new InvocationOnMockImpl(new Object(), new Method(null, "testMethodWithArgs", new Class[]{String.class, int.class})));
    }

    @Test
    public void testConstructorWithMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Collections.singletonList(ArgumentMatchers.any()));

        InvocationMatcher expectedInvocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any()));

        assertThat(invocationMatcher, is(expectedInvocationMatcher));
    }

    @Test
    public void testConstructorWithNoMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])));

        InvocationMatcher expectedInvocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method(null, "testMethod", new Class[0])));

        assertThat(invocationMatcher, is(expectedInvocationMatcher));
    }

    @Test
    public void testGetMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])));
        Method method = invocationMatcher.getMethod();

        InvocationMatcher expectedInvocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method(null, "testMethod", new Class[0])));
        Method expectedMethod = new Method(null, "testMethod", new Class[0]);

        assertThat(invocationMatcher, is(expectedInvocationMatcher));
        assertThat(method, is(expectedMethod));
    }

    @Test
    public void testGetInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])));
        Invocation invocation = invocationMatcher.getInvocation();

        InvocationMatcher expectedInvocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method(null, "testMethod", new Class[0])));
        Invocation expectedInvocation = new InvocationOnMockImpl(new Object(), new Method(null, "testMethod", new Class[0]));

        assertThat(invocationMatcher, is(expectedInvocationMatcher));
        assertThat(invocation, is(expectedInvocation));
    }

    @Test
    public void testGetMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));

        InvocationMatcher expectedInvocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));

        List<Matcher> matchers = invocationMatcher.getMatchers();
        List<Matcher> expectedMatchers = Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString());

        assertThat(invocationMatcher, is(expectedInvocationMatcher));
        assertThat(matchers, is(expectedMatchers));
    }

    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));

        String expectedToString = "InvocationMatcher{invocation=MockitoMockInvocation[testMethod()], matchers=[ArgThat can be any value, ArgThat can be any String]}";
        String toString = invocationMatcher.toString();

        assertThat(toString, is(expectedToString));
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndArguments() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));
        InvocationOnMockInvocation invocation = new InvocationOnMockImpl(new Object(), new Method(null, "testMethod", new Class[0]));

        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesInvocationWithSameMethodButDifferentArguments() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));
        InvocationOnMockInvocation invocation = new InvocationOnMockImpl(new Object(), new Method(null, "testMethod", new Class[0]), new Object[]{"test"});

        assertFalse(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesInvocationWithDifferentMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));
        InvocationOnMockInvocation invocation = new InvocationOnMockImpl(new Object(), new Method(null, "testMethodWithArgs", new Class[]{String.class, int.class}), new Object[]{"test", 1});

        assertFalse(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesInvocationWithUnverified() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));
        InvocationOnMockInvocation invocation = Mockito.mock(InvocationOnMock.class);

        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesInvocationWithSameMock() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));

        assertTrue(invocationMatcher1.matches(invocationMatcher2.getInvocation()));
        assertTrue(invocationMatcher2.matches(invocationMatcher1.getInvocation()));
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndDifferentMocks() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Collections.singletonList(ArgumentMatchers.any()));

        assertFalse(invocationMatcher1.matches(invocationMatcher2.getInvocation()));
    }

    @Test
    public void testMatchesInvocationWithOverloadedMethodsButSameArguments() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));
        InvocationOnMockInvocation invocation = new InvocationOnMockImpl(new Object(), new Method(null, "testMethodWithArgs", new Class[]{String.class, int.class}), new Object[]{"test", 1});

        assertFalse(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesInvocationWithOverloadedMethodsButDifferentArguments() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));
        InvocationOnMockInvocation invocation1 = new InvocationOnMockImpl(new Object(), new Method(null, "testMethodWithArgs", new Class[]{String.class, int.class}), new Object[]{"test1", 1});
        InvocationOnMockInvocation invocation2 = new InvocationOnMockImpl(new Object(), new Method(null, "testMethodWithArgs", new Class[]{String.class, int.class}), new Object[]{"test2", 2});

        assertFalse(invocationMatcher.matches(invocation1));
        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));

        Object[] testArguments = new Object[0];
        Object[] argumentsToMatch = new Object[0];

        assertTrue(invocationMatcher.safelyArgumentsMatch(testArguments));
        assertTrue(invocationMatcher.safelyArgumentsMatch(argumentsToMatch));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithThrowable() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));

        Object[] testArguments = new Object[]{new Exception()};

        invocationMatcher.safelyArgumentsMatch(testArguments);
    }

    @Test
    public void testHasSimilarMethodWithSameMethod() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));

        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2.getInvocation()));
        assertTrue(invocationMatcher2.hasSimilarMethod(invocationMatcher1.getInvocation()));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethods() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod1", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod2", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2.getInvocation()));
        assertFalse(invocationMatcher2.hasSimilarMethod(invocationMatcher1.getInvocation()));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])),
                Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.anyString()));

        List<Matcher> matchers = invocationMatcher.getMatchers();
        Matcher firstMatcher = matchers.get(0);
        Matcher secondMatcher = matchers.get(1);

        Object[] arguments = new Object[]{"test"};

        invocationMatcher.captureArgumentsFrom(new InvocationOnMock(new Object(), new Method(null, "testMethod", new Class[0])), arguments);

        assertEquals(1, firstMatcher.getNumberOfUses());
        assertEquals("test", firstMatcher.getValue());

        assertEquals(1, secondMatcher.getNumberOfUses());
        assertNull(secondMatcher.getValue());
    }

    @Test
    public void testCreateFrom() {
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher expectedInvocationMatcher1 = new InvocationMatcher(invocations.get(0));
        InvocationMatcher expectedInvocationMatcher2 = new InvocationMatcher(invocations.get(1));

        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        assertThat(invocationMatcher1, is(expectedInvocationMatcher1));
        assertThat(invocationMatcher2, is(expectedInvocationMatcher2));
    }
}