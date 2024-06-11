package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Object mock;

    @Before
    public void setup() {
        mock = Mockito.mock(String.class);
    }

    @Test
    public void testSimpleMethodMatching() {
        Invocation invocation = new Invocation(mock, new MethodCall(String.class, "length", new Object[0]).setTarget(mock), new Object[0]);
        matcher = new InvocationMatcher(invocation);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation);

        assertTrue(matcher.matches(expectedMatcher));
    }

    @Test
    public void testDifferentMethodsDontMatch() {
        Invocation invocation = new Invocation(mock, new MethodCall(String.class, "length", new Object[0]).setTarget(mock), new Object[0]);
        InvocationMatcher expectedMatcher = new InvocationMatcher(new Invocation(mock, new MethodCall(Integer.class, "intValue", new Object[0]).setTarget(mock), new Object[0]));

        assertFalse(matcher.matches(expectedMatcher));
    }

    @Test
    public void testSameMethodButDifferentArgumentsDontMatch() {
        Invocation invocation1 = new Invocation(mock, new MethodCall(String.class, "length", new Object[0]).setTarget(mock), new Object[0]);
        Invocation invocation2 = new Invocation(mock, new MethodCall(String.class, "length", new Object[]{new String("test")}).setTarget(mock));

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void testSameMethodWithSameArgumentsMatch() {
        Invocation invocation = new Invocation(mock, new MethodCall(String.class, "length", new Object[0]).setTarget(mock), new Object[0]);
        Object[] arguments = new Object[]{new Object[]{1, 2, 3}};
        InvocationMatcher matcher1 = new InvocationMatcher(invocation);
        InvocationMatcher matcher2 = new InvocationMatcher(new Invocation(mock, new MethodCall(String.class, "length", new Object[0]).setTarget(mock), arguments));

        assertTrue(matcher1.matches(matcher2));
    }

    @Test
    public void testInvocationMatcherWithCustomMatcher() {
        String expected = "test";

        when(mock.length()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                InvocationMatcher matcher = new InvocationMatcher(invocation);
                assertTrue(matcher.matches(new InvocationMatcher(new Invocation(mock, new MethodCall(String.class, "length", new Object[0]).setTarget(mock), new Object[0]))));
                return 3;
            }
        });

        InvocationMatcher matcher = new InvocationMatcher(new Invocation(mock, new MethodCall(String.class, "length", new Object[0]).setTarget(mock), new Object[0]));
        verify(mock).length();
        assertTrue(matcher.matches(new InvocationMatcher(new Invocation(mock, new MethodCall(String.class, "length", new Object[0]).setTarget(mock), new Object[0]))));
        assertEquals(expected, mock.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarargs() {
        String[] args = {"test1", "test2"};
        when(mock.method(args)).thenReturn("testResult");

        matcher = new InvocationMatcher(new Invocation(mock, new MethodCall(String.class, "method", (Object[]) args.clone()).setTarget(mock), args));
        matcher.captureArgumentsFrom(new Invocation(mock, new MethodCall(String.class, "method", (Object[]) args.clone()).setTarget(mock), args));

        List<Matcher> matchers = matcher.getMatchers();
        Matcher argMatcher = matchers.get(0);
        assert(argMatcher instanceof ArgumentsMatcher);
        ArgumentMatcher argMatcher1 = (ArgumentMatcher) argMatcher;
        assertTrue(argMatcher1.matches(Arrays.asList(args)));
    }
}