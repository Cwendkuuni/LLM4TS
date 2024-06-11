import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockitoMock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private MockitoMock mock;

    @Before
    public void setup() {
        mock = mockitoMock(String.class);
    }

    @Test
    public void testMatcherWithNoMatchers() {
        Invocation invocation = new Invocation(mock.mock, getClass().getMethod("testMethod", String.class), new Object[]{"test"});
        matcher = new InvocationMatcher(invocation);

        InvocationMatcher expectedMatch = new InvocationMatcher(invocation);
        InvocationMatcher actualMatch = matcher;

        List<Matcher> expectedMatchers = Collections.emptyList();
        List<Matcher> actualMatchers = matcher.getMatchers();

        assertThat(matcher.matches(expectedMatch), is(true));
        assertThat(matcher.getMatchers(), is(expectedMatchers));
    }

    @Test
    public void testMatcherWithMatchers() {
        ArgumentCaptor<String> stringArgumentCaptor = argumentCaptor(String.class);

        Invocation invocation = new Invocation(mock.mock, getClass().getMethod("testMethod", String.class), new Object[]{"test"});
        List<Matcher> matchers = Arrays.asList(argThat(isA(String.class)), stringArgumentCaptor.capture());
        matcher = new InvocationMatcher(invocation, matchers);

        InvocationMatcher expectedMatch = new InvocationMatcher(invocation, matchers);
        InvocationMatcher actualMatch = matcher;

        List<Matcher> expectedMatchers = matchers;
        List<Matcher> actualMatchers = matcher.getMatchers();

        verify(mock).testMethod("test");
        String capturedValue = stringArgumentCaptor.getValue();

        assertThat(matcher.matches(expectedMatch), is(true));
        assertThat(matcher.getMatchers(), is(expectedMatchers));
        assertThat(capturedValue, is("test"));
    }

    @Test
    public void testDifferentMocks() {
        Invocation invocation1 = new Invocation(mock1.mock, getClass().getMethod("testMethod", String.class), new Object[]{"test"});
        Invocation invocation2 = new Invocation(mock2.mock, getClass().getMethod("testMethod", String.class), new Object[]{"test"});
        matcher = new InvocationMatcher(invocation1);

        InvocationMatcher expectedMatch = new InvocationMatcher(invocation2);
        InvocationMatcher actualMatch = matcher;

        assertFalse(matcher.matches(expectedMatch));
    }

    @Test
    public void testDifferentMethods() {
        Invocation invocation1 = new Invocation(mock.mock, getClass().getMethod("testMethod1", String.class), new Object[]{"test"});
        Invocation invocation2 = new Invocation(mock.mock, getClass().getMethod("testMethod2", String.class), new Object[]{"test"});
        matcher = new InvocationMatcher(invocation1);

        InvocationMatcher expectedMatch = new InvocationMatcher(invocation2);
        InvocationMatcher actualMatch = matcher;

        assertFalse(matcher.matches(expectedMatch));
    }

    @Test
    public void testDifferentArguments() {
        Invocation invocation = new Invocation(mock.mock, getClass().getMethod("testMethod", String.class), new Object[]{"test1"});
        matcher = new InvocationMatcher(invocation);

        InvocationMatcher expectedMatch = new InvocationMatcher(new Invocation(mock.mock, getClass().getMethod("testMethod", String.class), new Object[]{"test2"}));
        InvocationMatcher actualMatch = matcher;

        assertFalse(matcher.matches(expectedMatch));
    }

    @Test
    public void testSameInvocation() {
        Invocation invocation = new Invocation(mock.mock, getClass().getMethod("testMethod", String.class), new Object[]{"test"});
        matcher = new InvocationMatcher(invocation);

        InvocationMatcher expectedMatch = new InvocationMatcher(invocation);
        InvocationMatcher actualMatch = matcher;

        assertTrue(matcher.matches(expectedMatch));
    }

    @Test
    public void testDifferentLocations() {
        Invocation invocation1 = new Invocation(mock.mock, getClass().getMethod("testMethod", String.class), new Object[]{"test"}, new Location("TestClass.java", 10));
        Invocation invocation2 = new Invocation(mock.mock, getClass().getMethod("testMethod", String.class), new Object[]{"test"}, new Location("AnotherTestClass.java", 20));
        matcher = new InvocationMatcher(invocation1);

        InvocationMatcher expectedMatch = new InvocationMatcher(invocation2);
        InvocationMatcher actualMatch = matcher;

        assertFalse(matcher.matches(expectedMatch));
    }
}