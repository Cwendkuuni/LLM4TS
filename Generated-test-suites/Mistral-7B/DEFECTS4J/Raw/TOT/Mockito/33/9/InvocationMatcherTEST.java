import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.MockitoStubs;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Object mock;

    @Before
    public void setup() {
        mock = MockitoStubs.mock(String.class);
        invocation = new Invocation(new Method(String.class, "testMethod", new Class[]{String.class, Integer.TYPE}), mock, new Object[]{new String("test"), 1});
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testInvocationMatcher_emptyMatcherList() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcher_withMatcherList() {
        List<Matcher> matchers = Arrays.asList(argThat(isA(String.class)), argThat(isA(Integer.class)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcher_matches_sameInvocation() {
        assertEquals(true, invocationMatcher.matches(invocationMatcher));
    }

    @Test
    public void testInvocationMatcher_doesNotMatch_differentInvocation() {
        Invocation differentInvocation = new Invocation(new Method(String.class, "testMethod", new Class[]{String.class}), mock, new Object[]{"test"});
        assertEquals(false, invocationMatcher.matches(differentInvocation));
    }

    @Test
    public void testInvocationMatcher_doesNotMatch_differentMethod() {
        Invocation differentInvocation = new Invocation(new Method(Integer.TYPE, "testMethod", new Class[0]), mock, new Object[]{null});
        assertEquals(false, invocationMatcher.matches(differentInvocation));
    }

    @Test
    public void testInvocationMatcher_matches_sameMethod_sameArguments() {
        Invocation sameInvocation = new Invocation(invocation.getMethod(), mock, new Object[]{"test", 1});
        assertEquals(true, invocationMatcher.matches(sameInvocation));
    }

    @Test(expected = AssertionError.class)
    public void testInvocationMatcher_doesNotMatch_differentArguments() {
        Invocation differentInvocation = new Invocation(invocation.getMethod(), mock, new Object[]{"test", 2});
        invocationMatcher.matches(differentInvocation);
    }

    @Test
    public void testHasSimilarMethod_sameInvocation() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation);
        assertEquals(true, invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod_differentInvocation_sameMethod() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new Invocation(invocation.getMethod(), mock, new Object[0]));
        assertEquals(true, invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod_differentInvocation_differentMethod() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new Invocation(new Method(String.class, "testOtherMethod", new Class[0]), mock, new Object[0]));
        assertEquals(false, invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethod_sameMethod_differentArguments() {
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(new Invocation(invocation.getMethod(), mock, new Object[]{"test2", 1}));
        assertEquals(false, invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod_sameInvocation() {
        assertEquals(true, invocationMatcher.hasSameMethod(invocationMatcher));
    }

    @Test
    public void testHasSameMethod_sameInvocation_differentInvocation() {
        Invocation differentInvocation = new Invocation(invocation.getMethod(), mock, new Object[]{new String("test2")});
        assertEquals(false, invocationMatcher.hasSameMethod(new InvocationMatcher(differentInvocation)));
    }

    @Test
    public void testHasSameMethod_differentInvocation_sameMethod() {
        Invocation differentInvocation = new Invocation(invocation.getMethod(), mock, new Object[0]);
        assertEquals(true, invocationMatcher.hasSameMethod(new InvocationMatcher(new Invocation(differentInvocation.getMethod(), mock, new Object[0]))));
    }

    @Test
    public void testHasSameMethod_differentInvocation_differentMethod() {
        Invocation differentInvocation = new Invocation(new Method(String.class, "testOtherMethod", new Class[0]), mock, new Object[0]);
        assertEquals(false, invocationMatcher.hasSameMethod(new InvocationMatcher(new Invocation(differentInvocation.getMethod(), mock, new Object[0]))));
    }

    @Test
    public void testToString() {
        String expected = String.format("InvocationMatcher{invocation=%s, matchers=%s}", invocation.toString(), Arrays.toString(matchers.toArray()));
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        ArgumentMatcher argumentMatcher = argThat(isA(String.class));
        answer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                invocationMatcher.captureArgumentsFrom(invocation);
                return null;
            }
        }).when(mock).matches(argumentMatcher);

        String test = "test";
        when(mock.toString()).thenReturn(test);

        invocationMatcher.captureArgumentsFrom(mock.matches(argumentMatcher));

        ArgumentCaptor<String> stringArgumentCaptor = argumentCaptor(String.class);
        verify(mock).toString(eq(stringArgumentCaptor.capture()));

        assertEquals(test, stringArgumentCaptor.getValue());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(invocation, new Invocation(new Method(String.class, "testOtherMethod", new Class[0]), mock, new Object[]{null}), new Invocation(invocation.getMethod(), mock, new Object[]{new String("test3")}));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(3, invocationMatchers.size());

        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            assertEquals(invocationMatcher.getMethod(), invocation.getMethod());
        }
    }
}