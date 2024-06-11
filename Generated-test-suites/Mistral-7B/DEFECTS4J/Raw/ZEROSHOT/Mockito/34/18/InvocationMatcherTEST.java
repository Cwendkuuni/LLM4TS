import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationWithArguments;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.stub.Answer;
import org.mockito.stub.MockitoStub;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Method method;
    private MockitoStub mockitoStub;
    private List<Matcher> matchers;
    private CapturesArguments capturesArguments;
    private ArgumentsComparator argumentsComparator;

    @Before
    public void setUp() {
        method = new Method(TestClass.class.getName(), "testMethod", new Class[0]);
        invocation = new InvocationWithArguments(mockitoStub, method, new Object[0]);
        matchers = Arrays.asList(eq(1), is("test"));
        capturesArguments = spy(new CapturesArguments() {
            @Override
            public void captureFrom(Object argument) {
                // no-op
            }
        });
        argumentsComparator = new ArgumentsComparator();

        invocationMatcher = new InvocationMatcher(invocation, matchers);
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
        PrintSettings printSettings = new PrintSettings();
        String expected = "InvocationMatcher:\n" +
                "    method: testMethod()\n" +
                "    arguments: []\n" +
                "    matchers: [ArgumentMatcher[this is 1], ArgumentMatcher[is test]]\n";
        String actual = invocationMatcher.toString(printSettings);
        assertThat(actual, is(expected));
    }

    @Test
    public void testMatches() {
        InvocationMatcher otherMatcher = spy(InvocationMatcher.class);
        doAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(Invocation invocation) throws Throwable {
                verify(otherMatcher).matches(invocation);
                return true;
            }
        }).when(otherMatcher).matches(any(Invocation.class));

        Invocation otherInvocation = new InvocationWithArguments(mockitoStub, method, new Object[0]);
        InvocationMatcher otherMatcherWithInvocation = new InvocationMatcher(otherInvocation);

        invocationMatcher.matches(invocation);
        verify(otherMatcher).matches(invocation);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] otherArguments = new Object[1];
        otherArguments[0] = "other";

        assertThat(invocationMatcher.safelyArgumentsMatch(new Object[0]), is(true));
        assertThat(invocationMatcher.safelyArgumentsMatch(otherArguments), is(false));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation otherInvocation = new InvocationWithArguments(mockitoStub, method, new Object[0]);
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);

        assertTrue(invocationMatcher.hasSimilarMethod(otherMatcher));
        assertTrue(otherMatcher.hasSimilarMethod(invocationMatcher));

        Invocation differentInvocation = new InvocationWithArguments(mockitoStub, new Method("differentMethod", new Class[0]), new Object[0]);
        InvocationMatcher differentMatcher = new InvocationMatcher(differentInvocation);

        assertFalse(invocationMatcher.hasSimilarMethod(differentMatcher));
        assertFalse(differentMatcher.hasSimilarMethod(invocationMatcher));
    }

    @Test
    public void testHasSameMethod() {
        Invocation otherInvocation = new InvocationWithArguments(mockitoStub, method, new Object[0]);
        InvocationMatcher otherMatcher = new InvocationMatcher(otherInvocation);

        assertTrue(invocationMatcher.hasSameMethod(otherMatcher));
        assertTrue(otherMatcher.hasSameMethod(invocationMatcher));

        Invocation differentInvocation = new InvocationWithArguments(mockitoStub, new Method("differentMethod", new Class[0]), new Object[0]);
        InvocationMatcher differentMatcher = new InvocationMatcher(differentInvocation);

        assertFalse(invocationMatcher.hasSameMethod(differentMatcher));
        assertFalse(differentMatcher.hasSameMethod(invocationMatcher));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Object[] invocationArguments = new Object[]{1, "two"};

        invocationMatcher.captureArgumentsFrom(invocation);

        for (int i = 0; i < invocationArguments.length; i++) {
            Matcher matcher = matchers.get(i);
            if (matcher instanceof CapturesArguments) {
                CapturesArguments capturesArguments = (CapturesArguments) matcher;
                Object capturedArgument = capturesArguments.getCapturedValue();
                assertEquals(invocationArguments[i], capturedArgument);
            }
        }
    }

    private void assertEquals(Object expected, Object actual) {
        if (expected == null) {
            assertThat(actual, is(not(sameInstance(expected))))
                    .describedAs("expected null but was %s", actual);
        } else if (!expected.equals(actual)) {
            assertThat(expected, is(expected))
                    .describedAs("expected <%s> but was <%s>", expected, actual);
        }
    }
}

class TestClass {
    public void testMethod() {
        // no-op
    }
}