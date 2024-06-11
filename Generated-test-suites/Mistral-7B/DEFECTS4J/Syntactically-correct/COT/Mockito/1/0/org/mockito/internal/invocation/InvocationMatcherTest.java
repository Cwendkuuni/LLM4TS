import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyMatchers() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        InvocationMatcher expectedInvocationMatcher = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        assertTrue(invocationMatcher.equals(expectedInvocationMatcher));
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndMatchers() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Matcher matcher1 = Mockito.mock(Matcher.class);
        Matcher matcher2 = Mockito.mock(Matcher.class);
        List<Matcher> matchers = new LinkedList<Matcher>() {{
            add(matcher1);
            add(matcher2);
        }};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher expectedInvocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.equals(expectedInvocationMatcher));
    }

    @Test
    public void testGetMethod() {
        Method method = Mockito.mock(Method.class);
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method actualMethod = invocationMatcher.getMethod();

        verify(invocation).getMethod();
        assertTrue(actualMethod.equals(method));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation actualInvocation = invocationMatcher.getInvocation();

        assertTrue(actualInvocation.equals(invocation));
    }

    @Test
    public void testGetMatchers() {
        Matcher matcher1 = Mockito.mock(Matcher.class);
        Matcher matcher2 = Mockito.mock(Matcher.class);
        List<Matcher> matchers = new LinkedList<Matcher>() {{
            add(matcher1);
            add(matcher2);
        }};
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getArguments()).thenReturn(new Object[]{null, null});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        assertTrue(actualMatchers.equals(matchers));
    }

    @Test
    public void testToString() {
        Matcher matcher1 = Mockito.mock(Matcher.class);
        Matcher matcher2 = Mockito.mock(Matcher.class);
        List<Matcher> matchers = new LinkedList<Matcher>() {{
            add(matcher1);
            add(matcher2);
        }};
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{null, null});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expectedToString = "invocation: org.mockito.internal.invocation.InvocationOnMock@";
        String actualToString = invocationMatcher.toString();

        String actualInvocationToString = invocation.toString();
        String expectedMatchersToString = matchers.toString();

        assertTrue(actualToString.startsWith(expectedToString));
        assertTrue(actualToString.contains(actualInvocationToString));
        assertTrue(actualToString.contains(expectedMatchersToString));
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndSameArguments() {
        Matcher matcher = Mockito.mock(Matcher.class);
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[]{null, null});
        when(matcher.matches(any(Invocation.class))).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(Invocation invocation) {
                return true;
            }
        });

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.<Matcher>singletonList(matcher));

        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testMatchesInvocationWithDifferentMethod() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        when(invocation1.getMethod()).thenReturn(Mockito.mock(Method.class, "method1"));
        when(invocation2.getMethod()).thenReturn(Mockito.mock(Method.class, "method2"));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesInvocationWithDifferentMock() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        when(invocation1.getMock()).thenReturn(Mockito.mock(Object.class));
        when(invocation2.getMock()).thenReturn(Mockito.mock(Object.class, "otherMock"));

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testMatchesInvocationWithDifferentArguments() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments1 = new Object[]{null, null};
        Object[] arguments2 = new Object[]{1, 2};
        when(invocation.getArguments()).thenReturn(arguments1);
        when(invocation.getRawArguments()).thenReturn(new Object[]{arguments1, arguments2});

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testSafelyArgumentsMatchWithSameArguments() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{null, null};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.<Matcher>asList(eq(arguments)));

        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithDifferentArguments() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments1 = new Object[]{null, null};
        Object[] arguments2 = new Object[]{1, 2};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.<Matcher>asList(eq(arguments1)));

        invocationMatcher.safelyArgumentsMatch(arguments2);
    }

    @Test
    public void testHasSimilarMethodWithSameMethod() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method = Mockito.mock(Method.class);

        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethods() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Method method1 = Mockito.mock(Method.class, "method1");
        Method method2 = Mockito.mock(Method.class, "method2");

        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Matcher matcher = Mockito.mock(Matcher.class, "matcher");
        Stubber stubber = Mockito.mock(Stubber.class);

        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(new Object[]{Arrays.asList(1, 2).toArray()});
        when(matcher.matches(any(Object.class))).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(Object o) {
                stubber.then(isA(CaptureArgumentStub.class)).answer(eq(o));
                return true;
            }
        });

        CaptureArgumentStub captureArgumentStub = Mockito.mock(CaptureArgumentStub.class, "captureArgumentStub");
        when(stubber.thenReturn(captureArgumentStub)).thenReturn(captureArgumentStub);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.<Matcher>singletonList(matcher));

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(invocation).getArgumentAt(0, eq(Integer.class));
        verify(invocation).getArgumentAt(1, eq(Integer.class));
        verify(captureArgumentStub).captureFrom(eq(1));
        verify(captureArgumentStub).captureFrom(eq(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromWithVarargs() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Matcher matcher = Mockito.mock(Matcher.class);

        when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class, "methodWithVarargs"));
        when(invocation.getRawArguments()).thenReturn(new Object[]{Arrays.asList(1, 2, 3).toArray()});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.<Matcher>singletonList(matcher));

        invocationMatcher.captureArgumentsFrom(invocation);
    }
}

class CaptureArgumentStub {
    private Object argument;

    public void captureFrom(Object argument) {
        this.argument = argument;
    }
}