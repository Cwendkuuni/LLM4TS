import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private Object mock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() {
        mock = mock(Object.class);
        invocation = Mockito.mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(new Method(Object.class.getMethod("equals", Object.class)));
        matcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testConstructorWithEmptyListOfMatchers() {
        // Given

        // When
        InvocationMatcher matcherWithEmptyList = new InvocationMatcher(invocation);

        // Then
        assertThat(matcherWithEmptyList.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testConstructorWithListOfMatchers() {
        // Given
        Matcher matcher1 = ArgumentMatchers.eq("testString");
        List<Matcher> matchers = Arrays.asList(matcher1);

        // When
        InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation, matchers);

        // Then
        assertThat(matcherWithMatchers.getMatchers(), is(matchers));
    }

    @Test
    public void testGetMethod() {
        // Given

        // When
        Method method = matcher.getMethod();

        // Then
        assertThat(method, notNullValue());
        assertThat(method.getName(), is("equals"));
    }

    @Test
    public void testGetInvocation() {
        // Given

        // When
        Invocation invocationToCompare = mock(Invocation.class);

        // Then
        assertThat(matcher.getInvocation(), is(invocation));
        assertThat(matcher.getInvocation(), is(sameInstance(invocationToCompare)));
    }

    @Test
    public void testGetMatchers() {
        // Given
        Matcher matcher1 = ArgumentMatchers.eq("testString");
        List<Matcher> matchers = Arrays.asList(matcher1);

        // When
        InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation, matchers);

        // Then
        List<Matcher> matchersFromMatcher = matcherWithMatchers.getMatchers();

        // Then
        assertThat(matchersFromMatcher, is(matchers));
    }

    @Test
    public void testToString() {
        // Given
        Matcher matcher1 = ArgumentMatchers.eq("testString");
        List<Matcher> matchers = Arrays.asList(matcher1);

        // When
        InvocationMatcher matcherWithMatchers = new InvocationMatcher(invocation, matchers);

        // Then
        String actual = matcherWithMatchers.toString();
        String expected = "Invocation on testClass with method('equals(java.lang.Object)')" +
                " and arguments:\n" +
                "\tArgument #0: equals(java.lang.Object)\n";
        assertThat(actual, is(expected));
    }

    @Test
    public void testMatchesWithSameInvocation() {
        // Given

        // When
        boolean result = matcher.matches(invocation);

        // Then
        assertThat(result, is(true));
    }

    @Test
    public void testMatchesWithDifferentInvocation() {
        // Given
        Invocation differentInvocation = mock(Invocation.class);

        // When
        boolean result = matcher.matches(differentInvocation);

        // Then
        assertThat(result, is(false));
    }

    @Test
    public void testMatchesWithSameMethodButDifferentArguments() {
        // Given
        Invocation differentInvocation = mock(Invocation.class);
        when(differentInvocation.getMethod()).thenReturn(invocation.getMethod());

        // When
        boolean result = matcher.matches(differentInvocation);

        // Then
        assertThat(result, is(false));
    }

    @Test
    public void testMatchesWithSameMethodAndArgumentsButDifferentInvocations() {
        // Given
        Invocation differentInvocation = mock(Invocation.class);
        when(differentInvocation.getMethod()).thenReturn(invocation.getMethod());

        // When
        boolean result = matcher.matches(differentInvocation);

        // Then
        assertThat(result, is(true));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodButDifferentInvocations() {
        // Given
        Invocation differentInvocation = mock(Invocation.class);
        when(differentInvocation.getMethod()).thenReturn(invocation.getMethod());

        // When
        boolean result = matcher.hasSimilarMethod(differentInvocation);

        // Then
        assertThat(result, is(true));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethods() {
        // Given
        Invocation differentInvocation = mock(Invocation.class);
        when(differentInvocation.getMethod()).thenReturn(new Method(Object.class.getMethod("hashCode", Object.class)));

        // When
        boolean result = matcher.hasSimilarMethod(differentInvocation);

        // Then
        assertThat(result, is(false));
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        // Given
        Invocation differentInvocation = mock(Invocation.class);
        when(differentInvocation.getMethod()).thenReturn(new Method(Object.class.getMethod("hashCode", Object.class)));

        // When
        boolean result = matcher.hasSameMethod(differentInvocation);

        // Then
        assertThat(result, is(false));
    }

    @Test
    public void testCaptureArgumentsFromInvocationWithVarArgs() {
        // Given
        Matcher matcher1 = ArgumentMatchers.containsString("test");
        Matcher matcher2 = ArgumentMatchers.containsString("test2");
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        Object[] varargs = new Object[]{"testString1", "testString2", "testString3"};

        // When
        Invocation invocationWithVarargs = mock(Invocation.class);
        when(invocationWithVarargs.getArguments()).thenReturn(varargs);

        matcher.captureArgumentsFrom(invocationWithVarargs);

        // Then
        MatcherDecorator matcher1Decorator = (MatcherDecorator) matchers.get(0);
        Matcher capturedArgumentForMatcher1 = matcher1Decorator.getRealMatcher();
        assertThat(capturedArgumentForMatcher1.getArguments(), is(Arrays.asList("testString1", "testString2")));

        MatcherDecorator matcher2Decorator = (MatcherDecorator) matchers.get(1);
        Matcher capturedArgumentForMatcher2 = matcher2Decorator.getRealMatcher();
        assertThat(capturedArgumentForMatcher2.getArguments(), is(Arrays.asList("testString3")));
    }

    @Test
    public void testCaptureArgumentsFromInvocationWithNoVarArgs() {
        // Given
        Matcher matcher = ArgumentMatchers.eq("testString");
        List<Matcher> matchers = Arrays.asList(matcher);
        Object[] arguments = new Object[]{"testString"};

        // When
        Invocation invocationWithNoVarargs = mock(Invocation.class);
        when(invocationWithNoVarargs.getArguments()).thenReturn(arguments);

        matcher.captureArgumentsFrom(invocationWithNoVarargs);

        // Then
        MatcherDecorator matcherDecorator = (MatcherDecorator) matchers.get(0);
        Matcher capturedArgumentForMatcher = matcherDecorator.getRealMatcher();
        assertThat(capturedArgumentForMatcher.getArguments(), is(arguments));
    }

    @Test
    public void testCreateFromMethodWithEmptyListOfInvocations() {
        // Given
        List<Invocation> invocations = Collections.emptyList();

        // When
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // Then
        assertThat(invocationMatchers, is(Collections.emptyList()));
    }

    @Test
    public void testCreateFromMethodWithListOfInvocations() {
        // Given
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        // When
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // Then
        InvocationMatcher invocationMatcher1 = invocationMatchers.get(0);
        InvocationMatcher invocationMatcher2 = invocationMatchers.get(1);

        // Then
        assertThat(invocationMatcher1.getInvocation(), is(invocation1));
        assertThat(invocationMatcher2.getInvocation(), is(invocation2));
    }
}