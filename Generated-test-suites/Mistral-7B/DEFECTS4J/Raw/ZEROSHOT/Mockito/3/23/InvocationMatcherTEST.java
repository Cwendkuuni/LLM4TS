import org.junit.Before;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation mockInvocation;
    private Invocation realInvocation;

    @Before
    public void setUp() {
        List<Matcher> matchers = Arrays.asList(is("hello"), is(1));
        mockInvocation = Mockito.mock(InvocationOnMock.class);
        realInvocation = new InvocationOnMockImpl(mock(Object.class), new MethodTest(), new Object[0]);
        matcher = new InvocationMatcher(mockInvocation, matchers);
    }

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        InvocationMatcher matcherWithEmptyMatchers = new InvocationMatcher(mockInvocation);
        assertThat(matcherWithEmptyMatchers, is(notNullValue()));
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        InvocationMatcher matcherWithMatchers = new InvocationMatcher(mockInvocation, matchers());
        assertThat(matcherWithMatchers, is(notNullValue()));
    }

    @Test
    public void testGetMethod() {
        Method method = mockInvocation.getMethod();
        Method returnedMethod = matcher.getMethod();
        assertThat(method, is(returnedMethod));
    }

    @Test
    public void testGetInvocation() {
        Invocation returnedInvocation = matcher.getInvocation();
        assertThat(mockInvocation, is(returnedInvocation));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> returnedMatchers = matcher.getMatchers();
        assertThat(matchers, is(returnedMatchers));
    }

    @Test
    public void testToString() {
        String toString = matcher.toString();
        String expectedToString = new PrintSettings().print(matchers, mockInvocation);
        assertThat(toString, is(expectedToString));
    }

    @Test
    public void testMatches() {
        InvocationMatcher anotherMatcher = new InvocationMatcher(realInvocation, matchers());
        boolean matches = matcher.matches(anotherMatcher);
        boolean expectedMatches = mockInvocation.getMock().equals(realInvocation.getMock())
                && matcher.hasSameMethod(anotherMatcher);
        assertThat(matches, is(expectedMatches));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[]{1, 2};
        boolean result = matcher.safelyArgumentsMatch(actualArgs);
        boolean expectedResult = new ArgumentsComparator().argumentsMatch(matcher, actualArgs);
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation anotherInvocation = new InvocationOnMockImpl(mock(Object.class), new MethodTest(), new Object[0]);
        boolean result = matcher.hasSimilarMethod(new InvocationMatcher(anotherInvocation));
        boolean expectedResult = matcher.invocation.getMock().equals(anotherInvocation.getMock())
                && matcher.hasSameMethod(new InvocationMatcher(anotherInvocation));
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testHasSameMethod() {
        Invocation anotherInvocation = new InvocationOnMockImpl(mock(Object.class), new MethodTest(), new Object[0]);
        boolean result = matcher.hasSameMethod(new InvocationMatcher(anotherInvocation));
        boolean expectedResult = matcher.invocation.getMethod().getName().equals(anotherInvocation.getMethod().getName())
                && Arrays.equals(matcher.invocation.getMethod().getParameterTypes(), anotherInvocation.getMethod().getParameterTypes());
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testGetLocation() {
        Location returnedLocation = matcher.getLocation();
        Location expectedLocation = mockInvocation.getLocation();
        assertThat(returnedLocation, is(expectedLocation));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        List<Matcher> matchersWithCaptor = Arrays.asList(Captures.forClass(int[].class), is("expectedValue"));
        InvocationMatcher matcherWithCaptor = new InvocationMatcher(mockInvocation, matchersWithCaptor);
        Invocation invocationWithArguments = new InvocationOnMockImpl(mock(Object.class), new MethodTest(), new Object[]{new int[2], "expectedValue"});
        matcherWithCaptor.captureArgumentsFrom(invocationWithArguments);
        verify(invocationWithArguments).getArgumentAt(0, int[].class);
        verify(invocationWithArguments).getArgumentAt(1, String.class).equals("expectedValue");
    }

    @Test(expected = Throwable.class)
    public void testCaptureArgumentsFromWithVarArgsInvocation() {
        List<Matcher> matchersWithCaptor = Arrays.asList(Captures.forClass(String[].class), is("expectedValue"));
        InvocationMatcher matcherWithCaptor = new InvocationMatcher(mockInvocation, matchersWithCaptor);
        Invocation invocationWithArguments = new InvocationOnMockImpl(mock(Object.class), new MethodTest(), new Object[]{"expectedValue"});
        matcherWithCaptor.captureArgumentsFrom(invocationWithArguments);
    }

    private List<Matcher> matchers() {
        return Arrays.asList(is("test"), is(1), is(2));
    }

    private class MethodTest implements Serializable, Method {
        @Override
        public String getName() {
            return "testMethod";
        }

        @Override
        public Class<?>[] getParameterTypes() {
            return new Class[]{String.class, int.class};
        }

        @Override
        public int getModifiers() {
            return 0;
        }

        @Override
        public Class<?> getReturnType() {
            return void.class;
        }

        @Override
        public Class<?> getDeclaringClass() {
            return Object.class;
        }
    }
}