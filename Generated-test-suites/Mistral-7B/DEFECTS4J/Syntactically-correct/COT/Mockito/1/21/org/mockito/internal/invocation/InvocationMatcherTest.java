import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.mock.MockitoMock;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Mock
    private Object mock;

    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        Invocation invocation = mock(InvocationOnMock.class);
        given(invocation.getMock()).willReturn(mock);
        given(invocation.getMethod()).willReturn(new Method(Object.class.getDeclaredMethod("toString")));
        given(invocation.getArguments()).willReturn(new Object[]{1, "two"});

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.anyInt(), ArgumentMatchers.containsString("two"));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithMatchers() {
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testConstructorWithoutMatchers() {
        invocationMatcher = new InvocationMatcher(invocation);
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = new Method(Object.class.getDeclaredMethod("toString"));
        Method actualMethod = invocationMatcher.getMethod();

        assertThat(actualMethod, is(equalTo(expectedMethod)));
    }

    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = invocation;
        Invocation actualInvocation = invocationMatcher.getInvocation();

        assertThat(actualInvocation, is(equalTo(expectedInvocation)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCapturingArgumentsFromVarargs() {
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testHasSimilarMethod_sameMethod() {
        Invocation candidate = mock(InvocationOnMock.class);
        given(candidate.getMethod()).willReturn(new Method(Object.class.getDeclaredMethod("toString")));

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertThat(result, is(true));
    }

    @Test
    public void testHasSimilarMethod_differentMethod() {
        Invocation candidate = mock(InvocationOnMock.class);
        given(candidate.getMethod()).willReturn(new Method(Object.class.getDeclaredMethod("equals")));

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertThat(result, is(false));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHasSimilarMethod_unverifiedInvocation() {
        Invocation candidate = mock(InvocationOnMock.class);
        given(candidate.isVerified()).willReturn(false);

        invocationMatcher.hasSimilarMethod(candidate);
    }

    @Test
    public void testHasSimilarMethod_differentMock() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Invocation candidate = mock(InvocationOnMock.class);
        given(candidate.getMock()).willReturn(mock2);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertThat(result, is(false));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(InvocationOnMock.class);
        given(candidate.getMethod()).willReturn(new Method(Object.class.getDeclaredMethod("toString")));

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertThat(result, is(true));
    }

    @Test
    public void testHasSameMethod_differentMethods() {
        Invocation candidate = mock(InvocationOnMock.class);
        given(candidate.getMethod()).willReturn(new Method(Object.class.getDeclaredMethod("equals")));

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertThat(result, is(false));
    }

    @Test
    public void testToString() {
        String expected = "InvocationMatcher{invocation=Invocation{method=Method:toString()}, matchers=[]}";
        String actual = invocationMatcher.toString();

        assertThat(actual, is(equalTo(expected)));
    }
}