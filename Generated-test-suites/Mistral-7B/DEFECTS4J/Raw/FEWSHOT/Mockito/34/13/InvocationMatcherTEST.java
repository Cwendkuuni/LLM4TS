package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argThat;
import static org.mockito.internal.matchers.ArgumentsMatcher.contains;
import static org.mockito.internal.matchers.ArgumentsMatcher.isA;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.same;
import static org.mockito.internal.matchers.ArgumentsMatcher.value;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.ReflectionUtils;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.mock.MockitoMock;
import org.mockito.stubbing.Answer;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    @MockitoMock(answers = Answer.RETURNS_DEEP_STUBS)
    private static Object mock;

    @Test
    public void testInvocationMatcher() {
        Class<?> clazz = InvocationMatcher.class;
        Method method = ReflectionUtils.getPublicMethod(clazz, "InvocationMatcher", Invocation.class, List.class);
        InvocationMatcher im = new InvocationMatcher(null, Collections.emptyList());
        assertFalse(im.matches(null));
        assertFalse(im.matches(new Invocation(null, null, null)));
        assertFalse(im.matches(new Invocation(mock, method, new Object[0])));
        assertTrue(im.matches(new Invocation(mock, method, new Object[0])));

        List<Matcher> matchers = Collections.singletonList(value(mock));
        InvocationMatcher im1 = new InvocationMatcher(new Invocation(mock, method, new Object[0]), matchers);
        assertTrue(im1.matches(new Invocation(mock, method, new Object[0])));

        Matcher m = argThat(isA(String.class));
        InvocationMatcher im2 = new InvocationMatcher(new Invocation(mock, method, new Object[0]), Collections.singletonList(m));
        assertFalse(im2.matches(new Invocation(mock, method, new Object[0])));
        Object[] args = new Object[]{"foo"};
        assertTrue(im2.matches(new Invocation(mock, method, args)));
    }

    @Test
    public void testInvocationMatcher_emptyMatchers() {
        Class<?> clazz = InvocationMatcher.class;
        Method method = ReflectionUtils.getPublicMethod(clazz, "InvocationMatcher", Invocation.class);
        InvocationMatcher im1 = new InvocationMatcher(new Invocation(mock, method, new Object[0]));
        InvocationMatcher im2 = new InvocationMatcher(new Invocation(mock, method, new Object[0]), Collections.emptyList());
        assertTrue(im1.matches(im2));
        assertTrue(im2.matches(im1));
    }

    @Test
    public void testInvocationMatcher_nullInvocation() {
        Class<?> clazz = InvocationMatcher.class;
        Method method = ReflectionUtils.getPublicMethod(clazz, "InvocationMatcher", List.class);
        InvocationMatcher im = new InvocationMatcher(null, Collections.emptyList());
        assertNull(im.getInvocation());
    }

    @Test
    public void testInvocationMatcher_nullMatchers() {
        Class<?> clazz = InvocationMatcher.class;
        Method method = ReflectionUtils.getPublicMethod(clazz, "InvocationMatcher", Invocation.class);
        InvocationMatcher im = new InvocationMatcher(new Invocation(mock, method, new Object[0]));
        assertNull(im.getMatchers());
    }

    @Test
    public void testInvocationMatcher_hasSameMethod() {
        Class<?> clazz = InvocationMatcher.class;
        Method method1 = ReflectionUtils.getPublicMethod(clazz, "hasSameMethod", Invocation.class);
        Method method2 = ReflectionUtils.getPublicMethod(clazz, "hasSameMethod", Invocation.class);
        InvocationMatcher im1 = new InvocationMatcher(new Invocation(mock, method1, new Object[0]));
        InvocationMatcher im2 = new InvocationMatcher(new Invocation(mock, method2, new Object[0]));
        assertTrue(im1.hasSameMethod(im2));
        assertTrue(im2.hasSameMethod(im1));
    }

    @Test
    public void testInvocationMatcher_hasSimilarMethod() {
        Class<?> clazz = InvocationMatcher.class;
        Method method1 = ReflectionUtils.getPublicMethod(clazz, "hasSimilarMethod", Invocation.class);
        Method method2 = ReflectionUtils.getPublicMethod(clazz, "hasSimilarMethod", Invocation.class);
        Method method3 = ReflectionUtils.getPublicMethod(clazz, "getMethod", Invocation.class);
        InvocationMatcher im1 = new InvocationMatcher(new Invocation(mock, method1, new Object[0]));
        InvocationMatcher im2 = new InvocationMatcher(new Invocation(mock, method2, new Object[0]));
        InvocationMatcher im3 = new InvocationMatcher(new Invocation(mock, method3, new Object[0]));
        assertTrue(im1.hasSimilarMethod(im3));
        assertFalse(im1.hasSimilarMethod(im2));
        assertFalse(im2.hasSimilarMethod(im1));
        assertFalse(im2.hasSimilarMethod(im3));
        assertFalse(im3.hasSimilarMethod(im1));
        assertFalse(im3.hasSimilarMethod(im2));

        InvocationMatcher im4 = new InvocationMatcher(new Invocation(mock, method1, new Object[0]), Collections.singletonList(value("foo")));
        InvocationMatcher im5 = new InvocationMatcher(new Invocation(mock, method2, new Object[0]), Collections.singletonList(value("foo")));
        assertFalse(im4.hasSimilarMethod(im5));
        assertFalse(im5.hasSimilarMethod(im4));

        InvocationMatcher im6 = new InvocationMatcher(new Invocation(mock, method1, new Object[0]), Collections.singletonList(value(1)));
        InvocationMatcher im7 = new InvocationMatcher(new Invocation(mock, method2, new Object[0]), Collections.singletonList(value(2)));
        assertFalse(im6.hasSimilarMethod(im7));
        assertFalse(im7.hasSimilarMethod(im6));
    }

    @Test
    public void testInvocationMatcher_matches() {
        Class<?> clazz = InvocationMatcher.class;
        Method method = ReflectionUtils.getPublicMethod(clazz, "matches", Invocation.class);
        InvocationMatcher im1 = new InvocationMatcher(new Invocation(mock, method, new Object[0]));
        InvocationMatcher im2 = new InvocationMatcher(new Invocation(mock, method, new Object[0]));
        assertTrue(im1.matches(im2));
        assertTrue(im2.matches(im1));

        Object[] args1 = new Object[]{1, 2, 3};
        Object[] args2 = new Object[]{1, 2, 3};
        InvocationMatcher im3 = new InvocationMatcher(new Invocation(mock, method, args1));
        InvocationMatcher im4 = new InvocationMatcher(new Invocation(mock, method, args2));
        assertTrue(im3.matches(im4));
        assertTrue(im4.matches(im3));

        Object[] args3 = new Object[]{1, 2, 3};
        Object[] args4 = new Object[]{1, 3, 3};
        InvocationMatcher im5 = new InvocationMatcher(new Invocation(mock, method, args3));
        InvocationMatcher im6 = new InvocationMatcher(new Invocation(mock, method, args4));
        assertFalse(im5.matches(im6));
        assertFalse(im6.matches(im5));

        Object[] args5 = new Object[]{1, 2, null};
        Object[] args6 = new Object[]{1, null, 3};
        InvocationMatcher im7 = new InvocationMatcher(new Invocation(mock, method, args5));
        InvocationMatcher im8 = new InvocationMatcher(new Invocation(mock, method, args6));
        assertFalse(im7.matches(im8));
        assertFalse(im8.matches(im7));

        Object[] args7 = new Object[]{1, 2, 3};
        Object[] args8 = new Object[]{2, 2, 3};
        InvocationMatcher im9 = new InvocationMatcher(new Invocation(mock, method, args7), Collections.singletonList(same(1)));
        InvocationMatcher im10 = new InvocationMatcher(new Invocation(mock, method, args8), Collections.singletonList(same(1)));
        assertTrue(im9.matches(im10));
        assertTrue(im10.matches(im9));

        Object[] args9 = new Object[]{1, 2, 3};
        Object[] args10 = new Object[]{1, 2, 4};
        InvocationMatcher im11 = new InvocationMatcher(new Invocation(mock, method, args9), Collections.singletonList(contains(3)));
        InvocationMatcher im12 = new InvocationMatcher(new Invocation(mock, method, args10), Collections.singletonList(contains(3)));
        assertTrue(im11.matches(im12));
        assertTrue(im12.matches(im11));

        Object[] args11 = new Object[]{1, 2, null};
        Object[] args12 = new Object[]{1, null, null};
        InvocationMatcher im13 = new InvocationMatcher(new Invocation(mock, method, args11), Collections.singletonList(value(null)));
        InvocationMatcher im14 = new InvocationMatcher(new Invocation(mock, method, args12), Collections.singletonList(value(null)));
        assertTrue(im13.matches(im14));
        assertTrue(im14.matches(im13));

        Object[] args13 = new Object[]{1, 2, 3};
        Object[] args14 = new Object[]{1, 2, null};
        InvocationMatcher im15 = new InvocationMatcher(new Invocation(mock, method, args13), Collections.singletonList(value(3)));
        InvocationMatcher im16 = new InvocationMatcher(new Invocation(mock, method, args14), Collections.singletonList(isNull()));
        assertFalse(im15.matches(im16));
        assertFalse(im16.matches(im15));
    }

    @Test
    public void testToString() {
        Class<?> clazz = InvocationMatcher.class;
        Method toStringMethod = ReflectionUtils.getPublicMethod(clazz, "toString", PrintSettings.class);
        InvocationMatcher im = new InvocationMatcher(new Invocation(mock, getMethod(), new Object[0]));
        assertTrue(im.toString(new PrintSettings(true)).startsWith("InvocationMatcher:"));
        assertTrue(im.toString(new PrintSettings(false)).startsWith("InvocationMatcher"));
    }
}