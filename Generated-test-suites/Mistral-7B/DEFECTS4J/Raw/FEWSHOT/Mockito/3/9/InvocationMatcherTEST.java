package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherCreation() {
        Invocation invocationMock = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(CoreMatchers.is("foo"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock, matchers);
        InvocationMatcher invocationMatcherWithEmptyList = new InvocationMatcher(invocationMock);

        assertEquals(invocationMock, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
        assertEquals(matchers, invocationMatcherWithEmptyList.getMatchers());
        assertNull(invocationMatcherWithEmptyList.getMatchers().get(0));
    }

    @Test
    public void testToString() {
        Invocation invocationMock = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(CoreMatchers.argThat(new TypeSafeMatcher<String>() {
            @Override
            public boolean matchesSafely(String arg1) {
                return arg1.equals("foo");
            }

            @Override
            public String description() {
                return "matches string foo";
            }
        }), CoreMatchers.is("bar"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock, matchers);

        String result = invocationMatcher.toString();
        String expected = "Invocation on mock [mock] with method [public void mockito.internal.SomeClass.someMethod(java.lang.String, org.mockito.internal.matchers.TypeSafeMatcher)]:\n" +
                "arguments are: <{0}=matches string foo, <{1}=org.hamcrest.CoreMatchers.is(bar)>>";
        String actual = new PrintSettings().print(invocationMatcher.getMatchers(), invocationMatcher.getInvocation());
        assertTrue(result.equals(actual));
    }

    @Test
    public void testMatchesInvocation() {
        Invocation invocationMock = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock, Arrays.asList(CoreMatchers.is("foo")));
        Invocation actualInvocation = mock(Invocation.class);

        when(invocationMock.getMock()).thenReturn(invocationMock.getInvocation().getMock());
        when(invocationMock.getMethod()).thenReturn(mockMethod());
        when(actualInvocation.getMock()).thenReturn(invocationMock.getInvocation().getMock());
        when(actualInvocation.getMethod()).thenReturn(mockMethod());

        assertTrue(invocationMatcher.matches(invocationMock));
        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocationMock = mock(Invocation.class);
        List<Object> actualArgs = Arrays.asList("foo", 1, new Object[]{ "bar" });
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock, Arrays.asList(CoreMatchers.is("foo")));

        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs.toArray()));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocationMock1 = mock(Invocation.class);
        Invocation invocationMock2 = mock(Invocation.class);
        Method methodMock = mockMethod();

        when(invocationMock1.getMethod()).thenReturn(methodMock);
        when(invocationMock2.getMethod()).thenReturn(methodMock);
        when(invocationMock1.getMock()).thenReturn(invocationMock1.getInvocation().getMock());
        when(invocationMock2.getMock()).thenReturn(invocationMock1.getInvocation().getMock());

        Method method = mock(Method.class);
        when(method.getName()).thenReturn("someMethod");
        when(methodMock.getName()).thenReturn(method);

        assertTrue(new InvocationMatcher(invocationMock1).hasSimilarMethod(invocationMock1));
        assertTrue(new InvocationMatcher(invocationMock1).hasSimilarMethod(invocationMock2));
        assertFalse(new InvocationMatcher(invocationMock1).hasSimilarMethod(mock(Invocation.class)));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocationMock = mock(Invocation.class);
        Object[] args = new Object[]{ "foo", 1, new Object[]{ "bar" } };
        List<Matcher> matchers = Arrays.asList(
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object arg1) {
                        return arg1.equals("foo");
                    }

                    @Override
                    public String description() {
                        return "matches string foo";
                    }
                },
                new TypeSafeMatcher<Object[]>() {
                    @Override
                    public boolean matchesSafely(Object[] arg1) {
                        return Arrays.equals(arg1, new Object[]{"bar"});
                    }

                    @Override
                    public String description() {
                        return "matches array of strings [bar]";
                    }
                }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock, matchers);

        when(invocationMock.getMethod()).thenReturn(mockMethod());
        when(invocationMock.getArguments()).thenReturn(args);

        invocationMatcher.captureArgumentsFrom(invocationMock);

        MatcherAssert.assertThat(invocationMatcher.getMatchers().get(0), CoreMatchers.is("foo"));
        MatcherAssert.assertThat(invocationMatcher.getMatchers().get(1),
                new TypeSafeMatcher<Object[]>() {
                    @Override
                    public boolean matchesSafely(Object[] arg1) {
                        return Arrays.equals(arg1, new Object[]{"bar"});
                    }

                    @Override
                    public String description() {
                        return "matches array of strings [bar]";
                    }
                }
        );
    }

    @Test
    public void testCreateFromInvocations() {
        List<Invocation> invocations = Arrays.asList(mock(Invocation.class), mock(Invocation.class), mock(Invocation.class));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            assertTrue(invocationMatcher instanceof InvocationMatcher);
        }
    }

    @Test
    public void testHasSameMethodWithVarargs() {
        Invocation invocationMock1 = mock(Invocation.class);
        Invocation invocationMock2 = mock(Invocation.class);
        Method methodMock = mockMethod();

        when(invocationMock1.getMethod()).thenReturn(methodMock);
        when(invocationMock2.getMethod()).thenReturn(methodMock);
        when(invocationMock1.getMock()).thenReturn(invocationMock1.getInvocation().getMock());
        when(invocationMock2.getMock()).thenReturn(invocationMock1.getInvocation().getMock());

        Method method = mock(Method.class);
        when(method.getName()).thenReturn("someMethod");
        when(methodMock.getName()).thenReturn(method);

        when(methodMock.isVarArgs()).thenReturn(true);

        Object[] args = new Object[]{"foo", 1, new Object[]{"bar"}};

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocationMock1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocationMock2);

        invocationMatcher1.captureArgumentsFrom(invocationMock1);
        invocationMatcher2.captureArgumentsFrom(invocationMock2);

        when(invocationMock1.getArguments()).thenReturn(args);
        when(invocationMock2.getArguments()).thenReturn(args);

        assertTrue(invocationMatcher1.matches(invocationMock1));
        assertTrue(invocationMatcher2.matches(invocationMock2));
    }

    @Test
    public void testHasDifferentMethodWithVarargs() {
        Invocation invocationMock1 = mock(Invocation.class);
        Invocation invocationMock2 = mock(Invocation.class);
        Method methodMock1 = mockMethod();
        Method methodMock2 = mockMethod();

        when(invocationMock1.getMethod()).thenReturn(methodMock1);
        when(invocationMock2.getMethod()).thenReturn(methodMock2);
        when(invocationMock1.getMock()).thenReturn(invocationMock1.getInvocation().getMock());
        when(invocationMock2.getMock()).thenReturn(invocationMock1.getInvocation().getMock());

        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(method1.getName()).thenReturn("someMethod1");
        when(method2.getName()).thenReturn("someMethod2");
        when(methodMock1.getName()).thenReturn(method1);
        when(methodMock2.getName()).thenReturn(method2);

        when(methodMock1.isVarArgs()).thenReturn(true);
        when(methodMock2.isVarArgs()).thenReturn(true);

        Object[] args1 = new Object[]{"foo", 1, new Object[]{"bar"}};
        Object[] args2 = new Object[]{"foo", "qux", new Object[]{ "bar", "quux" }};

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocationMock1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocationMock2);

        invocationMatcher1.captureArgumentsFrom(invocationMock1);
        invocationMatcher2.captureArgumentsFrom(invocationMock2);

        when(invocationMock1.getArguments()).thenReturn(args1);
        when(invocationMock2.getArguments()).thenReturn(args2);

        assertFalse(invocationMatcher1.matches(invocationMock2));
    }

    @Test
    public void testHasSameMethodWithNoVarargs() {
        Invocation invocationMock1 = mock(Invocation.class);
        Invocation invocationMock2 = mock(Invocation.class);
        Method methodMock1 = mockMethod();
        Method methodMock2 = mockMethod();

        when(invocationMock1.getMethod()).thenReturn(methodMock1);
        when(invocationMock2.getMethod()).thenReturn(methodMock2);
        when(invocationMock1.getMock()).thenReturn(invocationMock1.getInvocation().getMock());
        when(invocationMock2.getMock()).thenReturn(invocationMock1.getInvocation().getMock());

        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(method1.getName()).thenReturn("someMethod1");
        when(method2.getName()).thenReturn("someMethod2");
        when(methodMock1.getName()).thenReturn(method1);
        when(methodMock2.getName()).thenReturn(method2);

        Object[] args = new Object[]{"foo", 1, new Object[]{"bar"}};

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocationMock1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocationMock2);

        invocationMatcher1.captureArgumentsFrom(invocationMock1);
        invocationMatcher2.captureArgumentsFrom(invocationMock2);

        when(invocationMock1.getArguments()).thenReturn(args);
        when(invocationMock2.getArguments()).thenReturn(args);

        assertTrue(invocationMatcher1.matches(invocationMock1));
        assertTrue(invocationMatcher1.matches(invocationMock2));
    }

    @Test
    public void testCaptureArgumentsFromInvocationWithVarargs() {
        Invocation invocationMock = mock(Invocation.class);
        Object[] args = new Object[]{"foo", 1, new Object[]{"bar", "quux"}};
        List<Matcher> matchers = Arrays.asList(
                new TypeSafeMatcher<Object>() {
                    @Override
                    public boolean matchesSafely(Object arg1) {
                        return arg1.equals("foo");
                    }

                    @Override
                    public String description() {
                        return "matches string foo";
                    }
                },
                new TypeSafeMatcher<Object[]>() {
                    @Override
                    public boolean matchesSafely(Object[] arg1) {
                        return Arrays.equals(arg1, new Object[]{"bar"});
                    }

                    @Override
                    public String description() {
                        return "matches array of strings [bar]";
                    }
                }
        );
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocationMock, matchers);

        when(invocationMock.getMethod()).thenReturn(mockMethod());
        when(invocationMock.getArguments()).thenReturn(args);

        invocationMatcher.captureArgumentsFrom(invocationMock);

        MatcherAssert.assertThat(invocationMatcher.getMatchers().get(0), CoreMatchers.is("foo"));
        MatcherAssert.assertThat(invocationMatcher.getMatchers().get(1),
                new TypeSafeMatcher<Object[]>() {
                    @Override
                    public boolean matchesSafely(Object[] arg1) {
                        return Arrays.equals(arg1, new Object[]{"bar"});
                    }

                    @Override
                    public String description() {
                        return "matches array of strings [bar]";
                    }
                }
        );
    }

    private Method mockMethod() {
        try {
            Class clazz = Class.forName("org.mockito.internal.matchers.MockMethod");
            Constructor constructor = clazz.getConstructor(String.class, int.class);
            return (Method) constructor.newInstance("someMethod", 1);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}