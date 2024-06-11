import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private InvocationOnMock invocationOnMock;

    @Before
    public void initialize() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.any(), ArgumentMatchers.isA(String.class));
        Method method = Mockito.method(String.class, "length");
        invocationOnMock = Mockito.mock(Object.class);
        invocationMatcher = new InvocationMatcher(new Invocation(method, invocationOnMock, Arrays.asList(new Object[]{"test"}), new Location(1, 1)), matchers);
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = invocationMatcher.getMethod();
        assertThat(expectedMethod, is(notNullValue()));
        assertThat(expectedMethod.getName(), is("length"));
    }

    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = invocationMatcher.getInvocation();
        assertThat(expectedInvocation, is(sameInstance(invocationOnMock)));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = invocationMatcher.getMatchers();
        assertThat(expectedMatchers, is(notNullValue()));
        assertThat(expectedMatchers.size(), is(2));
    }

    @Test
    public void testToString() {
        String expectedString = invocationMatcher.toString();
        String printSettingsString = "Printing arguments as 'args[i]'";
        PrintSettings printSettings = new PrintSettings(printSettingsString);
        String actualString = invocationMatcher.toString(printSettings);

        assertThat(expectedString, is(notNullValue()));
        assertThat(expectedString, is(actualString));
    }

    @Test
    public void testMatches() {
        InvocationMatcher anotherMatcher = new InvocationMatcher(new InvocationMatcher(invocationMatcher.getInvocation(), new ArrayList<>()));
        InvocationOnMock anotherInvocationMock = Mockito.mock(Object.class);
        Invocation anotherInvocation = new Invocation(invocationMatcher.getMethod(), anotherInvocationMock, Arrays.asList(new Object[]{"test2"}), new Location(1, 1));

        boolean matches = invocationMatcher.matches(new InvocationMatcher(anotherInvocation));
        boolean expectedMatches = false;

        assertThat(matches, is(expectedMatches));

        matches = invocationMatcher.matches(new InvocationMatcher(anotherInvocation, matchers));
        expectedMatches = false;

        assertThat(matches, is(expectedMatches));

        matches = invocationMatcher.matches(new InvocationMatcher(invocationMatcher));
        expectedMatches = true;

        assertThat(matches, is(expectedMatches));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        InvocationMatcher anotherMatcher = new InvocationMatcher(new InvocationMatcher(invocationMatcher.getInvocation(), new ArrayList<>()));
        Object[] actualArgs = new Object[]{"test2"};

        boolean matches = invocationMatcher.safelyArgumentsMatch(actualArgs);
        boolean expectedMatches = false;

        assertThat(matches, is(expectedMatches));

        matches = invocationMatcher.safelyArgumentsMatch(new Object[]{"test"});
        expectedMatches = true;

        assertThat(matches, is(expectedMatches));
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationMatcher anotherMatcher = new InvocationMatcher(new InvocationMatcher(invocationMatcher.getInvocation(), new ArrayList<>()));
        InvocationOnMock anotherInvocationMock2 = Mockito.mock(Object.class);
        Invocation anotherInvocation = new Invocation(invocationMatcher.getMethod(), anotherInvocationMock, Arrays.asList(new Object[]{"test2"}), new Location(1, 1));

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(new InvocationMatcher(anotherInvocation));
        boolean expectedHasSimilarMethod = false;

        assertThat(hasSimilarMethod, is(expectedHasSimilarMethod));

        hasSimilarMethod = invocationMatcher.hasSimilarMethod(new InvocationMatcher(new Invocation(invocationMatcher.getMethod(), anotherInvocationMock2, new Object[]{"test2"}, new Location(1, 1))));
        expectedHasSimilarMethod = false;

        assertThat(hasSimilarMethod, is(expectedHasSimilarMethod));

        hasSimilarMethod = invocationMatcher.hasSimilarMethod(new InvocationMatcher(new Invocation(invocationMatcher.getMethod(), invocationOnMock, new Object[]{"test"}, new Location(1, 1))));
        expectedHasSimilarMethod = true;

        assertThat(hasSimilarMethod, is(expectedHasSimilarMethod));
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher anotherMatcher = new InvocationMatcher(new InvocationMatcher(invocationMatcher.getInvocation(), new ArrayList<>()));
        Invocation anotherInvocation = new Invocation(invocationMatcher.getMethod(), invocationMatcher.getInvocation().getMock(), new Object[]{"test2"}, new Location(1, 1));

        boolean hasSameMethod = invocationMatcher.hasSameMethod(new InvocationMatcher(anotherInvocation));
        boolean expectedHasSameMethod = true;

        assertThat(hasSameMethod, is(expectedHasSameMethod));

        hasSameMethod = invocationMatcher.hasSameMethod(new InvocationMatcher(new Invocation(invocationMatcher.getMethod(), invocationMatcher.getInvocation().getMock(), new Object[]{"test"}, new Location(1, 1))));
        expectedHasSameMethod = true;

        assertThat(hasSameMethod, is(expectedHasSameMethod));

        hasSameMethod = invocationMatcher.hasSameMethod(new InvocationMatcher(new Invocation(new Method("anotherMethod", String.class), invocationMatcher.getInvocation().getMock(), new Object[]{"test"}, new Location(1, 1))));
        expectedHasSameMethod = false;

        assertThat(hasSameMethod, is(expectedHasSameMethod));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        List<Object> captureArguments = new ArrayList<>();
        invocationMatcher.captureArgumentsFrom(invocationMatcher);

        for (Matcher matcher : invocationMatcher.getMatchers()) {
            if (matcher instanceof ArgumentsMatcher) {
                ((ArgumentsMatcher) matcher).captureFrom(new Object[]{"test"});
                captureArguments.addAll(((ArgumentsMatcher) matcher).getCapturedArguments());
            }
        }

        Object capturedArgument = captureArguments.get(0);
        Object expectedArgument = "test";

        assertThat(capturedArgument, is(expectedArgument));
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(new Invocation(invocationMatcher.getMethod(), invocationMatcher.getInvocation().getMock(), Arrays.asList(new Object[]{"test1"}), new Location(1, 1)),
                new Invocation(invocationMatcher.getMethod(), invocationMatcher.getInvocation().getMock(), Arrays.asList(new Object[]{"test2"}), new Location(1, 1)));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        for (InvocationMatcher invocationMatcher2 : invocationMatchers) {
            InvocationMatcher expectedInvocationMatcher = new InvocationMatcher(invocationMatcher.getInvocation());
            assertThat(invocationMatcher2, is(sameInstance(expectedInvocationMatcher)));
        }
    }
}