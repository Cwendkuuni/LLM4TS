import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class InvocationMatcherTest {

    private Stubber stubber;
    private InvocationMatcher matcher;

    @Rule
    public TestRule mockitoRules = MockitoJUnit.rule();

    @Before
    public void setUp() {
        stubber = mockitoRules.stubber();
        matcher = new InvocationMatcher(Mockito.mock(Invocation.class), Collections.emptyList());
    }

    @org.junit.Test
    public void testNewInstanceWithMatchers() {
        // given
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("foo"), ArgumentMatchers.eq(1));

        // when
        InvocationMatcher matcher = new InvocationMatcher(Mockito.mock(Invocation.class), matchers);

        // then
        assertThat(matcher.getMatchers(), is(matchers));
    }

    @org.junit.Test
    public void testNewInstanceWithoutMatchers() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);

        // when
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // then
        assertThat(matcher.getInvocation(), is(invocation));
        assertThat(matcher.getMatchers(), is(Collections.emptyList()));
    }

    @org.junit.Test
    public void testGetMethod() {
        // given
        Method method = Mockito.mock(Method.class);
        Invocation invocation = Mockito.mock(Invocation.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return method;
            }
        });
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // when
        Method result = matcher.getMethod();

        // then
        assertThat(result, is(method));
    }

    @org.junit.Test
    public void testMatchesWithSameInvocation() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);

        // when
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        // then
        assertThat(matcher.matches(invocation), is(true));
    }

    @org.junit.Test
    public void testMatchesWithDifferentInvocationButSameMethod() {
        // given
        Method method = Mockito.mock(Method.class);
        Invocation invocation1 = Mockito.mock(Invocation.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return method;
            }
        });
        Invocation invocation2 = Mockito.mock(Invocation.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return method;
            }
        });
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        // when
        boolean result = matcher1.matches(invocation2);

        // then
        assertThat(result, is(true));
    }

    @org.junit.Test
    public void testMatchesWithDifferentInvocationAndDifferentMethod() {
        // given
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Invocation invocation1 = Mockito.mock(Invocation.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return method1;
            }
        });
        Invocation invocation2 = Mockito.mock(Invocation.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return method2;
            }
        });
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        // when
        boolean result = matcher1.matches(invocation2);

        // then
        assertThat(result, is(false));
    }

    @org.junit.Test
    public void testHasSameMethodWithSameMethods() {
        // given
        Method method1 = Mockito.mock(Method.class);
        Method method2 = method1;
        Invocation invocation = Mockito.mock(Invocation.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return method1;
            }
        });
        InvocationMatcher matcher1 = new InvocationMatcher(invocation);

        // when
        boolean result = matcher1.hasSameMethod(matcher1);

        // then
        assertThat(result, is(true));
    }

    @org.junit.Test
    public void testHasSameMethodWithDifferentMethods() {
        // given
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        Invocation invocation = Mockito.mock(Invocation.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return method1;
            }
        });
        InvocationMatcher matcher1 = new InvocationMatcher(invocation);
        InvocationMatcher matcher2 = new InvocationMatcher(Mockito.mock(Invocation.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return method2;
            }
        }));

        // when
        boolean result = matcher1.hasSameMethod(matcher2);

        // then
        assertThat(result, is(false));
    }

    @org.junit.Test
    public void testToString() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("foo"), ArgumentMatchers.eq(1));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        // when
        String result = matcher.toString();

        // then
        String expected = String.format("InvocationMatcher: %s%n%s", matcher.getInvocation(), matcher.getMatchers());
        assertThat(result, is(expected));
    }

    @org.junit.Test
    public void testCaptureArgumentsFromInvocation() {
        // given
        Invocation invocation = Mockito.mock(Invocation.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return "result";
            }
        });
        Object expectedArgument = "expectedArgument";

        // when
        matcher.captureArgumentsFrom(invocation);

        // then
        Matcher captureMatcher = matcher.getMatchers().get(0);
        if (captureMatcher instanceof CapturesArguments) {
            Object capturedArgument = ((CapturesArguments) captureMatcher).getCapturedValue();
            assertThat(capturedArgument, is(expectedArgument));
        }
    }

    @org.junit.Test
    public void testCreateFromInvocations() {
        // given
        List<Invocation> invocations = Arrays.asList(Mockito.mock(Invocation.class), Mockito.mock(Invocation.class));

        // when
        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        // then
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), is(invocations.size()));
    }
}