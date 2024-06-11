import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.invocation.Invocation;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.ArgumentsProcessor;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.Reflections;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private static final PrintSettings PRINT_SETTINGS = new PrintSettings();

    private List<InvocationMatcher> invocationMatchers;
    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Method method;
    private Object[] arguments;
    private Object mock;

    @Before
    public void setup() {
        mock = mock(Object.class);
        method = Reflections.getMethod(InvocationMatcher.class, "matches", Invocation.class);
        invocation = new InvocationOnMock(mock, new MethodInterceptor() {
            @Override
            public Object intercept(Method method, Object[] args, Object target) {
                InvocationMatcher testInstance = new InvocationMatcher(new Invocation(mock, method, args, new Location(0, 0)));
                invocationMatchers = InvocationMatcher.createFrom(Collections.singletonList(invocation));
                invocationMatcher = testInstance;
                return null;
            }
        });
        arguments = new Object[]{1, 2, 3};
    }

    @Test
    public void testInvocationMatcherCreationWithEmptyListOfMatchers() {
        // given
        List<Matcher> matchers = Collections.emptyList();
        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        // then
        assertThat(invocationMatcher.getMatchers(), is(notNullValue()));
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testInvocationMatcherCreationWithMatchers() {
        // given
        List<Matcher> matchers = Arrays.asList(Mockito.is(1), Mockito.is(2));
        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        // then
        assertThat(invocationMatcher.getMatchers(), is(notNullValue()));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testInvocationMatcherCreationWithEmptyInvocation() {
        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        // then
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testGetMethod() {
        // then
        Method invocationMethod = invocationMatcher.getMethod();
        assertThat(invocationMethod, is(sameInstance(invocation.getMethod())));
    }

    @Test
    public void testGetInvocation() {
        // then
        Invocation invocationFromMatcher = invocationMatcher.getInvocation();
        assertThat(invocationFromMatcher, is(sameInstance(invocation)));
    }

    @Test
    public void testGetMatchers() {
        // then
        List<Matcher> matchersFromMatcher = invocationMatcher.getMatchers();
        assertThat(matchersFromMatcher, is(notNullValue()));
        assertThat(matchersFromMatcher, is(invocationMatchers));
    }

    @Test
    public void testToString() {
        // then
        String expectedString = PRINT_SETTINGS.print(invocationMatchers, invocation);
        String toString = invocationMatcher.toString();
        assertThat(toString, is(expectedString));
    }

    @Test
    public void testMatchesInvocationWithSameParameters() {
        // given
        List<Matcher> matchers = Arrays.asList(Mockito.is(1), Mockito.is(2), Mockito.is(3));
        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        // then
        Invocation actual = new InvocationOnMock(mock, method, arguments, new Location(0, 0));
        boolean matches = invocationMatcher.matches(actual);
        // then
        assertTrue(matches);
    }

    @Test
    public void testMatchesInvocationWithDifferentParameters() {
        // given
        List<Matcher> matchers = Arrays.asList(Mockito.is(1), Mockito.is(3));
        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        // then
        Invocation actual = new InvocationOnMock(mock, method, new Object[]{1, 2}, new Location(0, 0));
        boolean matches = invocationMatcher.matches(actual);
        // then
        assertFalse(matches);
    }

    @Test
    public void testMatchesInvocationWithSameMethodNameButDifferentMethods() {
        // given
        List<Matcher> matchers = Arrays.asList(Mockito.is(1), Mockito.is(2));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, new Method(InvocationMatcher.class, "test", new Class[]{String.class}), new Object[]{"test"}, new Location(0, 0)));
        // when
        boolean matches = invocationMatcher1.matches(invocationMatcher2);
        // then
        assertFalse(matches);
    }

    @Test
    public void testMatchesInvocationWithSameMethodNameAndSameArguments() {
        // given
        List<Matcher> matchers = Arrays.asList(Mockito.is(1), Mockito.is(2), Mockito.is(3));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, method, arguments, new Location(0, 0)));
        // when
        boolean matches = invocationMatcher1.matches(invocationMatcher2);
        // then
        assertTrue(matches);
    }

    @Test
    public void testHasSameMethod() {
        // given
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, method, arguments, new Location(0, 0)));
        // when
        boolean hasSameMethod = invocationMatcher1.hasSameMethod(invocationMatcher2);
        // then
        assertTrue(hasSameMethod);
    }

    @Test
    public void testHasSimilarMethodWithSameMethodNameButDifferentMethods() {
        // given
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, new Method(InvocationMatcher.class, "test", new Class[]{String.class}), new Object[]{"test"}, new Location(0, 0)));
        // when
        boolean hasSimilarMethod = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        // then
        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWithSameMethodNameAndSameArguments() {
        // given
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, method, arguments, new Location(0, 0)));
        // when
        boolean hasSimilarMethod = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        // then
        assertTrue(hasSimilarMethod);
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethods() {
        // given
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new InvocationOnMock(mock, new Method(Object.class, "test", new Class[0]), new Object[0], new Location(0, 0)));
        // when
        boolean hasSimilarMethod = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        // then
        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        // given
        List<Matcher> matchers = Arrays.asList(new ArgumentMatcher("1", 0), new ArgumentMatcher("2", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        // when
        invocationMatcher.captureArgumentsFrom(invocation);
        // then
        ArgumentMatcher argumentMatcher1 = (ArgumentMatcher) invocationMatcher.getMatchers().get(0);
        ArgumentMatcher argumentMatcher2 = (ArgumentMatcher) invocationMatcher.getMatchers().get(1);
        ArgumentMatcher capturedArgumentMatcher1 = argumentMatcher1.getCaptured();
        ArgumentMatcher capturedArgumentMatcher2 = argumentMatcher2.getCaptured();
        // then
        assertNotNull(capturedArgumentMatcher1);
        assertNotNull(capturedArgumentMatcher2);
        assertEquals("1", capturedArgumentMatcher1.getValue());
        assertEquals("2", capturedArgumentMatcher2.getValue());
    }

    @Test
    public void testCreateFromInvocations() {
        // given
        List<Invocation> invocations = Arrays.asList(invocation, new InvocationOnMock(mock, method, arguments, new Location(0, 0)));
        // when
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        // then
        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);
        // then
        assertEquals(invocation, invocationMatcher1.getInvocation());
        assertEquals(invocation, invocationMatcher2.getInvocation());
        assertEquals(invocationMatchers.size(), 2);
    }

    private static class ArgumentMatcher implements Matcher<Object>, CapturesArguments {
        private final int index;
        private Object captured;

        public ArgumentMatcher(String name, int index) {
            this.index = index;
        }

        public boolean matches(Object argument) {
            return true;
        }

        public void captureFrom(Invocation invocation) {
            this.captured = invocation.getArgumentAt(index, Object.class);
        }

        public Object getCaptured() {
            return captured;
        }

        public String toString() {
            return String.format("%s(index=%d)", this.getClass().getSimpleName(), index);
        }

        private static class Value {
            private final String value;

            Value(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }
        }
    }
}