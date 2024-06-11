import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Location;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.MockitoMethod;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private MockitoMethod method;
    private Object[] arguments;
    private Object[] varArgs;

    @Before
    public void setUp() {
        method = MockitoMethod.createRealMethod(String.class, "method", String.class, int.class);
        arguments = new Object[]{1, "test"};
        varArgs = new Object[]{"test1", "test2"};
        invocation = createInvocation("mock", method, arguments);
        matcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testMatchInvocationWithEmptyMatcherList() {
        InvocationMatcher emptyMatcher = new InvocationMatcher(invocation);
        assertThat(emptyMatcher, is(matcher));
    }

    @Test
    public void testMatchInvocationWithMatcherList() {
        MatcherMatcher matcher1 = argThat(is("test"));
        MatcherMatcher matcher2 = argThat(is(1));
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);

        InvocationMatcher customMatcher = new InvocationMatcher(invocation, matchers);
        assertThat(customMatcher, is(matcher));
        assertThat(customMatcher.getMatchers().size(), is(2));
        assertThat(customMatcher.getMatchers().get(0), is(matcher1));
        assertThat(customMatcher.getMatchers().get(1), is(matcher2));
    }

    @Test
    public void testMatchesSameInvocation() {
        InvocationMatcher matcher2 = new InvocationMatcher(invocation);
        assertThat(matcher.matches(matcher2), is(true));
    }

    @Test
    public void testMatchesDifferentInvocationWithSameMethod() {
        InvocationMatcher differentMatcher = new InvocationMatcher(createInvocation("mock2", method, arguments));
        assertThat(matcher.matches(differentMatcher), is(true));
    }

    @Test
    public void testMatchesDifferentInvocationWithDifferentMethod() {
        InvocationMatcher differentMatcher = new InvocationMatcher(createInvocation("mock2", new MockitoMethod("otherMethod", int.class), arguments));
        assertThat(matcher.matches(differentMatcher), is(false));
    }

    @Test
    public void testMatchesDifferentInvocationWithDifferentMock() {
        InvocationMatcher differentMatcher = new InvocationMatcher(createInvocation("mock2", method, arguments));
        assertThat(matcher.matches(differentMatcher), is(false));
    }

    @Test
    public void testMatchesDifferentInvocationWithVarArgs() {
        InvocationMatcher differentMatcher = new InvocationMatcher(createInvocation("mock2", method, varArgs));
        assertThat(matcher.matches(differentMatcher), is(false));
    }

    @Test
    public void testToString() {
        String expected = "Invocation [method = method(java.lang.String, java.lang.String, int), mock = [mock], arguments = [1, test]]";
        assertThat(matcher.toString(), is(expected));
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher differentMatcher = new InvocationMatcher(createInvocation("mock2", method, arguments));
        assertThat(matcher.hasSameMethod(differentMatcher), is(true));
    }

    @Test
    public void testHasSimilarMethod() {
        InvocationMatcher differentMatcher = new InvocationMatcher(createInvocation("mock2", new MockitoMethod("method", int.class), arguments));
        assertThat(matcher.hasSimilarMethod(differentMatcher), is(false));

        InvocationMatcher differentMatcherVarArgs = new InvocationMatcher(createInvocation("mock2", method, varArgs));
        assertThat(matcher.hasSimilarMethod(differentMatcherVarArgs), is(false));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        CapturesArgumentsMatcher argThatStringCapture = argThat(new CaptureStringArgumentMatcher());
        List<Matcher> matchers = Arrays.asList(argThatStringCapture);
        InvocationMatcher customMatcher = new InvocationMatcher(invocation, matchers);
        Answer<String> answer = mock(Answer.class);
        OngoingStubbing<String> stringStubbing = when(answer.answer(eq(1), eq("test"))).thenReturn("stubbed");
        Object originalArg1 = invocation.getArgumentAt(0, Object.class);
        Object originalArg2 = invocation.getArgumentAt(1, String.class);

        customMatcher.captureArgumentsFrom(invocation);
        verify(answer).answer(eq(originalArg1), eq(originalArg2));
    }

    @Test
    public void testCreateFromInvocations() {
        List<Invocation> invocations = Arrays.asList(invocation, createInvocation("mock2", method, arguments));
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher firstMatcher = matchers.get(0);
        InvocationMatcher secondMatcher = matchers.get(1);

        assertThat(firstMatcher, is(matcher));
        assertThat(secondMatcher, is(new InvocationMatcher(createInvocation("mock2", method, arguments))));
    }

    private Invocation createInvocation(String mockName, Method method, Object[] arguments) {
        return new Invocation(spy(mock(Object.class)), method, arguments, null, new Location(1, 1));
    }

    private static Matcher<String> argThat(CaptureStringArgumentMatcher captureStringArgumentMatcher) {
        return new MatcherDecorator<String>(captureStringArgumentMatcher) {
            @Override
            public String describeTo(org.hamcrest.Description description) {
                description.appendText("capture argument");
                super.describeTo(description);
            }
        };
    }

    private static class CaptureStringArgumentMatcher implements Matcher<String> {
        private String capturedValue;

        @Override
        public boolean matches(String argument) {
            this.capturedValue = argument;
            return true;
        }

        public String getCapturedValue() {
            return capturedValue;
        }
    }
}