import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private InvocationMatcher invocationMatcher2;
    private InvocationMatcher invocationMatcher3;
    private Object mock;
    private Method method;
    private List<Matcher> matchers;
    private ArgumentsComparator argumentsComparator;

    @Before
    public void setUp() {
        mock = Mockito.mock(String.class);
        method = Mockito.method(String.class, "toString");
        matchers = new ArrayList<>();
        matchers.add(ArgumentMatchers.eq("test"));

        invocationMatcher = new InvocationMatcher(new InvocationOnMock(mock, method, null, new Location(1, 1), new Object[0]), matchers);
        invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, method, null, new Location(1, 2), new Object[0]), matchers);
        invocationMatcher3 = new InvocationMatcher(new InvocationOnMock(Mockito.mock(Object.class), method, null, new Location(1, 3), new Object[]{"test"}), matchers);
        argumentsComparator = new ArgumentsComparator();
    }

    @Test
    public void testMatches() {
        assertTrue(invocationMatcher.matches(invocationMatcher));
        assertTrue(invocationMatcher.matches(invocationMatcher2));
        assertFalse(invocationMatcher.matches(invocationMatcher3));
    }

    @Test
    public void testHasSameMethod() {
        assertTrue(invocationMatcher.hasSameMethod(invocationMatcher));
        assertTrue(invocationMatcher.hasSameMethod(invocationMatcher2));
        assertFalse(invocationMatcher.hasSameMethod(invocationMatcher3));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[]{"test1"};
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
        assertFalse(invocationMatcher.safelyArgumentsMatch(new Object[]{null}));
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationOnMock candidate = new InvocationOnMock(mock, method, null, new Location(1, 4), new Object[0]);
        InvocationOnMock candidate2 = new InvocationOnMock(mock, method, null, new Location(1, 5), new Object[]{"test2"});

        assertTrue(invocationMatcher.hasSimilarMethod(invocationMatcher));
        assertTrue(invocationMatcher.hasSimilarMethod(invocationMatcher2));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate2));
    }

    @Test
    public void testToString() {
        String expected = String.format("new InvocationMatcher(%s, %s)", method.toGenericString(), matchers.toString());
        assertTrue(invocationMatcher.toString().contains(expected));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object argument = "test";
        invocationMatcher.captureArgumentsFrom(new InvocationOnMock(mock, method, null, new Location(1, 6), new Object[]{argument}));
        assertTrue(matchers.get(0).matches(argument));
    }

    @Test
    public void testCreateFrom() {
        List<InvocationOnMock> invocations = Arrays.asList(new InvocationOnMock(mock, method, null, new Location(1, 7), new Object[]{"test1"}),
                new InvocationOnMock(mock, method, null, new Location(1, 8), new Object[]{"test2"}));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher invocationMatcher4 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher5 = invocationMatchers.get(1);

        assertTrue(invocationMatcher4.matches(invocationMatcher4));
        assertTrue(invocationMatcher5.matches(invocationMatcher5));
        assertFalse(invocationMatcher4.matches(invocationMatcher5));

        assertTrue(invocationMatcher4.hasSameMethod(invocationMatcher4));
        assertTrue(invocationMatcher5.hasSameMethod(invocationMatcher5));
        assertFalse(invocationMatcher4.hasSameMethod(invocationMatcher5));

        assertFalse(invocationMatcher4.safelyArgumentsMatch(invocationMatcher5.getArguments()));
        assertFalse(invocationMatcher5.safelyArgumentsMatch(invocationMatcher4.getArguments()));

        InvocationMatcher invocationMatcher6 = new InvocationMatcher(new InvocationOnMock(mock, method, null, new Location(1, 9), new Object[]{"test3"}), matchers);
        assertFalse(invocationMatcher4.matches(invocationMatcher6));
    }
}