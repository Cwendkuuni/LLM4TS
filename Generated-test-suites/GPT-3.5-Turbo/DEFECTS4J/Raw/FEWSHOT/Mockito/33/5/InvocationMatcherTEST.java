
package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.core.AllOf;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    private Invocation inv;
    private InvocationMatcher invMatcher;

    @Before
    public void setup() throws NoSuchMethodException {

        inv = new Invocation() {

            @Override
            public Object getMock() {
                return "";
            }

            @Override
            public MockCreationSettings getInvocationSettings() {
                return null;
            }

            @Override
            public Location getLocation() {
                return null;
            }

            @Override
            public Method getMethod() {
                return null;
            }

            @Override
            public Object[] getArguments() {
                return new Object[0];
            }

            @Override
            public Class<?> getMockType() {
                return null;
            }

            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }

            @Override
            public boolean isVerified() {
                return false;
            }

            @Override
            public boolean isIgnoredForVerification() {
                return false;
            }

            @Override
            public boolean equals(Object o) {
                return false;
            }

            @Override
            public int hashCode() {
                return 0;
            }
        };

        invMatcher = new InvocationMatcher(inv);
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = null;
        Method actualMethod = invMatcher.getMethod();

        assertEquals(expectedMethod, actualMethod);
    }

    @Test
    public void testGetInvocation() {
        Invocation expectedInvocation = inv;
        Invocation actualInvocation = invMatcher.getInvocation();

        assertEquals(expectedInvocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = new ArrayList<Matcher>();
        List<Matcher> actualMatchers = invMatcher.getMatchers();

        assertEquals(expectedMatchers, actualMatchers);
    }

    @Test
    public void testToString() {
        String expectedString = "";
        String actualString = invMatcher.toString();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = inv;
        assertTrue(invMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = inv;
        assertTrue(invMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = inv;
        assertTrue(invMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = null;
        Location actualLocation = invMatcher.getLocation();

        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void testToStringWithParameters() {
        PrintSettings printSettings = new PrintSettings();
        String expectedString = "";
        String actualString = invMatcher.toString(printSettings);

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = inv;
        invMatcher.captureArgumentsFrom(i);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<Invocation>();
        List<InvocationMatcher> expectedInvocationMatchers = new LinkedList<InvocationMatcher>();
        List<InvocationMatcher> actualInvocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(expectedInvocationMatchers, actualInvocationMatchers);
    }
}
