
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.internal.matchers.CapturesArguments;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation();
        Method expectedMethod = invocation.getMethod();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method actualMethod = invocationMatcher.getMethod();
        assertThat(actualMethod).isEqualTo(expectedMethod);
    }
    
    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(expectedInvocation, matchers);
        Invocation actualInvocation = invocationMatcher.getInvocation();
        assertThat(actualInvocation).isEqualTo(expectedInvocation);
    }
    
    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation();
        List<Matcher> expectedMatchers = invocation.argumentsToMatchers();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertThat(actualMatchers).isEqualTo(expectedMatchers);
    }
    
    @Test
    public void testToString() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expectedToString = invocation.toString(matchers, new PrintSettings());
        String actualToString = invocationMatcher.toString();
        assertThat(actualToString).isEqualTo(expectedToString);
    }
    
    @Test
    public void testMatches() {
        Invocation invocation = new Invocation();
        Invocation actualInvocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean expectedMatches = invocation.getMock().equals(actualInvocation.getMock())
                && invocationMatcher.hasSameMethod(actualInvocation)
                && new ArgumentsComparator().argumentsMatch(invocationMatcher, actualInvocation);
        boolean actualMatches = invocationMatcher.matches(actualInvocation);
        assertThat(actualMatches).isEqualTo(expectedMatches);
    }
    
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean expectedSimilarMethod = invocation.getMethod().getName().equals(candidate.getMethod().getName())
                && !candidate.isVerified()
                && invocation.getMock() == candidate.getMock()
                && invocationMatcher.hasSameMethod(candidate)
                && !invocationMatcher.safelyArgumentsMatch(candidate.getArguments());
        boolean actualSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);
        assertThat(actualSimilarMethod).isEqualTo(expectedSimilarMethod);
    }
    
    @Test
    public void testHasSameMethod() {
        Invocation invocation = new Invocation();
        Invocation candidate = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        boolean expectedSameMethod = invocation.getMethod().equals(candidate.getMethod());
        boolean actualSameMethod = invocationMatcher.hasSameMethod(candidate);
        assertThat(actualSameMethod).isEqualTo(expectedSameMethod);
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation();
        Location expectedLocation = invocation.getLocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Location actualLocation = invocationMatcher.getLocation();
        assertThat(actualLocation).isEqualTo(expectedLocation);
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation();
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = new PrintSettings();
        String expectedToString = invocation.toString(matchers, printSettings);
        String actualToString = invocationMatcher.toString(printSettings);
        assertThat(actualToString).isEqualTo(expectedToString);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation();
        Invocation i = new Invocation();
        i.setArguments(1, 2, 3);
        List<Matcher> matchers = Collections.singletonList(new CapturesArguments());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(i);
        assertThat(matchers.get(0)).isEqualTo(i.getArguments()[0]);
    }
}

