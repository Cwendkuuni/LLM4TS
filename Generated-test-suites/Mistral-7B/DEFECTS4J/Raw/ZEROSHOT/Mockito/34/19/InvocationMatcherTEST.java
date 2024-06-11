import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.reporting.PrintSettings;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Method method;
    private List<Matcher> matchers;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Before
    public void setUp() throws Exception {
        method = Mockito.class.getDeclaredMethod("methodWithTwoArgs", String.class, int.class);
        matchers = Collections.singletonList(is("test argument"));
        invocation = Mockito.mock(Invocation.class);
        invocation.init(mockitoRule.getMock(), method, new Object[]{new String("test argument 1"), 42}, new Location(1, 1, "TestClass.java"), new PrintSettings());
    }

    @Test
    public void testNewInvocationMatcherWithMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher expectedInvocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher, is(sameInstance(expectedInvocationMatcher)));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testNewInvocationMatcherWithoutMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        List<Matcher> expectedMatchers = invocation.argumentsToMatchers();

        assertThat(invocationMatcher, is(notNullValue()));
        assertThat(invocationMatcher.getMatchers(), is(sameInstance(expectedMatchers)));
    }

    @Test
    public void testGetMethod() {
        assertThat(invocationMatcher.getMethod(), is(method));
    }

    @Test
    public void testGetInvocation() {
        assertThat(invocationMatcher.getInvocation(), is(invocation));
    }

    @Test
    public void testGetMatchers() {
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testToString() {
        String expectedString = invocation.toString(matchers, new PrintSettings());
        String actualString = invocationMatcher.toString();

        assertThat(actualString, is(expectedString));
    }

    @Test
    public void testMatches() {
        Invocation candidate = Mockito.mock(Invocation.class);
        candidate.init(mockitoRule.getMock(), method, new Object[]{new String("test argument"), 42}, new Location(1, 1, "TestClass.java"), new PrintSettings());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher.matches(candidate), is(true));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[]{new String("test argument 1"), 43};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean result = invocationMatcher.safelyArgumentsMatch(actualArgs);

        assertThat(result, is(false));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = Mockito.mock(Invocation.class);
        candidate.init(mockitoRule.getMock(), method, new Object[]{new String("test argument"), 42}, new Location(1, 1, "TestClass.java"), new PrintSettings());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation candidateWithDifferentMethod = Mockito.mock(Invocation.class);
        candidateWithDifferentMethod.init(mockitoRule.getMock(), mockitoRule.mock(Method.class), new Object[]{new String("test argument"), 42}, new Location(1, 1, "TestClass.java"), new PrintSettings());

        Invocation candidateWithSameMethodButUnverified = Mockito.mock(Invocation.class);
        candidateWithSameMethodButUnverified.init(mockitoRule.getMock(), method, new Object[]{new String("test argument"), 42}, new Location(1, 1, "TestClass.java"), new PrintSettings());

        Invocation candidateWithSameMethodButDifferentMock = Mockito.mock(Invocation.class);
        candidateWithSameMethodButDifferentMock.init(mockitoRule.getMock(Object.class), method, new Object[]{new String("test argument"), 42}, new Location(1, 1, "TestClass.java"), new PrintSettings());

        Invocation candidateWithSameMethodButDifferentArguments = Mockito.mock(Invocation.class);
        candidateWithSameMethodButDifferentArguments.init(mockitoRule.getMock(), method, new Object[]{new String("test argument 1"), 43}, new Location(1, 1, "TestClass.java"), new PrintSettings());

        Invocation candidateWithOverloadedButSameArguments = Mockito.mock(Invocation.class);
        candidateWithOverloadedButSameArguments.init(mockitoRule.getMock(), mockitoRule.mock(Method.class), new Object[]{new String("test argument"), 42}, new Location(1, 1, "TestClass.java"), new PrintSettings());

        Invocation candidateWithOverloadedButDifferentArguments = Mockito.mock(Invocation.class);
        candidateWithOverloadedButDifferentArguments.init(mockitoRule.getMock(), mockitoRule.mock(Method.class), new Object[]{new String("test argument 1"), 43}, new Location(1, 1, "TestClass.java"), new PrintSettings());

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertThat(invocationMatcher.hasSimilarMethod(candidate), is(true));
        assertThat(invocationMatcher.hasSimilarMethod(candidateWithDifferentMethod), is(false));
        assertThat(invocationMatcher.hasSimilarMethod(candidateWithSameMethodButUnverified), is(true));
        assertThat(invocationMatcher.hasSimilarMethod(candidateWithSameMethodButDifferentMock), is(false));
        assertThat(invocationMatcher.hasSimilarMethod(candidateWithSameMethodButDifferentArguments), is(false));
        assertThat(invocationMatcher.hasSimilarMethod(candidateWithOverloadedButSameArguments), is(true));
        assertThat(invocationMatcher.hasSimilarMethod(candidateWithOverloadedButDifferentArguments), is(false));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(invocation).getArguments()[0].equals("test argument 1");
        verify(invocation).getArguments()[1].equals(42);
        verify(invocation).getArguments()[0].equals(stringArgumentCaptor.getValue());
    }
}