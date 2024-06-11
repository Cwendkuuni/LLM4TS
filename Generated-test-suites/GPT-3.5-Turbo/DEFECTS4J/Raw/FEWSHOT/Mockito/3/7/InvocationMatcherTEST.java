package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {
    private Invocation invocation;
  
    @Before
    public void setUp() {
        invocation = new Invocation() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public Method getMethod() {
                return null;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Location getLocation() {
                return null;
            }

            @Override
            public DescribedInvocation described() {
                return null;
            }
        };
    }

    @Test
    public void testConstructorWithInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        List<Matcher> matchers = Arrays.asList((Matcher) () -> true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(null, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        String expected = "PrintSettings";
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = new Invocation() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public Method getMethod() {
                return null;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Location getLocation() {
                return null;
            }

            @Override
            public DescribedInvocation described() {
                return null;
            }
        };

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = new Invocation() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public Method getMethod() {
                return null;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Location getLocation() {
                return null;
            }

            @Override
            public DescribedInvocation described() {
                return null;
            }
        };

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = new Invocation() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public Method getMethod() {
                return null;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Location getLocation() {
                return null;
            }

            @Override
            public DescribedInvocation described() {
                return null;
            }
        };

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(null, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public Method getMethod() {
                return null;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Location getLocation() {
                return null;
            }

            @Override
            public DescribedInvocation described() {
                return null;
            }
        };

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation = new Invocation() {
            @Override
            public Object getMock() {
                return null;
            }

            @Override
            public Method getMethod() {
                return null;
            }

            @Override
            public Object[] getArguments() {
                return null;
            }

            @Override
            public Location getLocation() {
                return null;
            }

            @Override
            public DescribedInvocation described() {
                return null;
            }
        };

        List<Invocation> invocations = Collections.singletonList(invocation);
        List<InvocationMatcher> expected = Collections.singletonList(new InvocationMatcher(invocation));

        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
        assertEquals(expected, actual);
    }
}