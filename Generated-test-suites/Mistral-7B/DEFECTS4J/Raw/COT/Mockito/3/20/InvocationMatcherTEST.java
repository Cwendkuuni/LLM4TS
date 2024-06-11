import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Location;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.invocation.DescribedInvocation;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcher_EmptyMatcherList() {
        Invocation invocation = mockInvocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());
        Assert.assertFalse(matcher.matches(mockInvocation()));
        Assert.assertEquals(invocation.getMethod().toGenericString(), matcher.getMethod().toGenericString());
        Assert.assertFalse(matcher.hasSameMethod(mockInvocation()));
    }

    @Test
    public void testInvocationMatcher_SingleMatcherList() {
        Invocation invocation = mockInvocation();
        Matcher matcher = mock(Matcher.class);
        List<Matcher> matchers = Arrays.asList(matcher);
        InvocationMatcher matcherUnderTest = new InvocationMatcher(invocation, matchers);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        matcher.matches(captor.capture());
        Assert.assertEquals(invocation.getMethod().toGenericString(), matcherUnderTest.getMethod().toGenericString());
        Assert.assertTrue(matcherUnderTest.matches(mockInvocationWithArgument(String.class, "test")));
        Assert.assertTrue(matcherUnderTest.matches(mockInvocationWithArgument(String.class, "test2")));
        Assert.assertFalse(matcherUnderTest.matches(mockInvocationWithArgument(Integer.class, 1)));
        verify(matcher).matches(ArgumentsMatcher.containsString("test"));
        verifyNoMoreInteractions(matcher);
    }

    @Test
    public void testInvocationMatcher_MultipleMatchersList() {
        Invocation invocation = mockInvocation();
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);
        InvocationMatcher matcherUnderTest = new InvocationMatcher(invocation, matchers);

        ArgumentCaptor<String> captor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Integer> captor2 = ArgumentCaptor.forClass(Integer.class);
        matcher1.matches(captor1.capture());
        matcher2.matches(captor2.capture());
        Assert.assertEquals(invocation.getMethod().toGenericString(), matcherUnderTest.getMethod().toGenericString());
        Assert.assertTrue(matcherUnderTest.matches(mockInvocationWithArguments(String.class, "test1", Integer.class, 1)));
        Assert.assertTrue(matcherUnderTest.matches(mockInvocationWithArguments(String.class, "test2", Integer.class, 2)));
        Assert.assertFalse(matcherUnderTest.matches(mockInvocationWithArguments(String.class, "test1", Integer.class, 2)));
        Assert.assertFalse(matcherUnderTest.matches(mockInvocationWithArguments(String.class, "test2", Integer.class, 1)));
        verify(matcher1).matches(ArgumentsMatcher.containsString("test1"));
        verify(matcher2).matches(ArgumentsMatcher.containsInt(1));
        verifyNoMoreInteractions(matcher1);
        verifyNoMoreInteractions(matcher2);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mockInvocation();
        Invocation invocation2 = mockInvocation();
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Assert.assertFalse(matcher1.hasSameMethod(matcher2));
        Assert.assertFalse(matcher1.hasSameMethod(mockInvocationMatcher()));
        InvocationMatcher identicalMatcher = new InvocationMatcher(invocation1);
        Assert.assertTrue(matcher1.hasSameMethod(identicalMatcher));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = mockInvocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Matcher matcherWithCaptor = new CapturesArguments() {
            private Object capturedValue;

            @Override
            public void captureFrom(Object argument) {
                this.capturedValue = argument;
            }

            @SuppressWarnings("unchecked")
            @Override
            public boolean matches(Object argument) {
                return capturedValue.equals(argument);
            }
        };
        matcher.captureArgumentsFrom(invocation);
        matcher.captureArgumentsFrom(invocation);
        Assert.assertEquals(Arrays.asList(invocation.getArguments()), matcher.getMatchers());
        Assert.assertEquals(matcherWithCaptor, matcher.getMatchers().get(0));
        Assert.assertSame(invocation.getArgumentAt(0, Object.class), matcherWithCaptor.getCapturedValue());
        Assert.assertSame(invocation.getArgumentAt(1, Object.class), matcherWithCaptor.getCapturedValue());
    }

    private Invocation mockInvocation() {
        Class<?> clazz = Object.class;
        try {
            Method method = clazz.getMethod("method", String.class, Integer.class);
            return new DescribedInvocation(new Location("TestClass.class", 1, 2), method, new Object[]{null, "test", 1});
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private Invocation mockInvocationWithArgument(Class<?> clazz, Object argument) {
        return mockInvocationWithArguments(clazz, argument, null);
    }

    private Invocation mockInvocationWithArguments(Class<?>... parameterTypes) {
        return mockInvocationWithArguments(null, null, parameterTypes);
    }

    private Invocation mockInvocationWithArguments(Class<?> clazz, Object argument, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getMethod("method", parameterTypes);
            return new DescribedInvocation(new Location("TestClass.class", 1, 2), method, new Object[]{argument});
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private InvocationMatcher mockInvocationMatcher() {
        Invocation invocation = mockInvocation();
        return new InvocationMatcher(invocation);
    }
}