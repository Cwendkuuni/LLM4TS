import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.verification.argumentmatching.ArgumentMatchingTool;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.Description;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyList() {
        List<Matcher> matchers = new ArrayList<>();
        Object[] arguments = new Object[0];
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MatchingArguments() {
        List<Matcher> matchers = new ArrayList<>();
        Matcher<String> stringMatcher = new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String item) {
                return item.equals("test");
            }

            @Override
            public void describeTo(Description description) {
                description.description("a string equal to test");
            }
        };
        matchers.add(stringMatcher);
        Object[] arguments = new Object[]{"test"};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(new Integer[0], result);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchedTypes() {
        List<Matcher> matchers = new ArrayList<>();
        Matcher<Integer> integerMatcher = new TypeSafeMatcher<Integer>() {
            @Override
            protected boolean matchesSafely(Integer item) {
                return item.equals(1);
            }

            @Override
            public void describeTo(Description description) {
                description.description("an integer equal to 1");
            }
        };
        matchers.add(integerMatcher);
        Object[] arguments = new Object[]{"test"};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(new Integer[0], result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SizeMismatch() {
        List<Matcher> matchers = new ArrayList<>();
        Matcher<String> stringMatcher = new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String item) {
                return item.equals("test");
            }

            @Override
            public void describeTo(Description description) {
                description.description("a string equal to test");
            }
        };
        matchers.add(stringMatcher);
        Object[] arguments = new Object[2];
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatch() {
        List<Matcher> matchers = new ArrayList<>();
        Matcher<String> stringMatcher = new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String item) {
                return item.equals("test");
            }

            @Override
            public void describeTo(Description description) {
                description.description("a string equal to test");
            }
        };
        Matcher<Integer> integerMatcher = new TypeSafeMatcher<Integer>() {
            @Override
            protected boolean matchesSafely(Integer item) {
                return item.equals(1);
            }

            @Override
            public void describeTo(Description description) {
                description.description("an integer equal to 1");
            }
        };
        matchers.add(stringMatcher);
        matchers.add(integerMatcher);
        Object[] arguments = new Object[]{"test", 1};
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        Assert.assertArrayEquals(new Integer[]{1}, result);
    }

    @Test
    public void testSafelyMatches_MatcherMatchesObject() {
        Matcher<Integer> integerMatcher = new TypeSafeMatcher<Integer>() {
            @Override
            protected boolean matchesSafely(Integer item) {
                return item.equals(1);
            }

            @Override
            public void describeTo(Description description) {
                description.description("an integer equal to 1");
            }
        };
        Object arg = 1;
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.safelyMatches(integerMatcher, arg);
        assertTrue(result);
    }

    @Test
    public void testSafelyMatches_MatcherDoesNotMatchObject() {
        Matcher<Integer> integerMatcher = new TypeSafeMatcher<Integer>() {
            @Override
            protected boolean matchesSafely(Integer item) {
                return item.equals(2);
            }

            @Override
            public void describeTo(Description description) {
                description.description("an integer equal to 2");
            }
        };
        Object arg = 1;
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.safelyMatches(integerMatcher, arg);
        assertFalse(result);
    }

    @Test
    public void testToStringEquals_MatcherToStringEqualsObjectToString() {
        Matcher<String> stringMatcher = new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String item) {
                return item.equals("test");
            }

            @Override
            public void describeTo(Description description) {
                description.description("a string equal to test");
            }
        };
        Object arg = "test";
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(stringMatcher, arg);
        assertTrue(result);
    }

    @Test
    public void testToStringEquals_MatcherToStringDifferentFromObjectToString() {
        Matcher<String> stringMatcher = new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String item) {
                return item.equals("test");
            }

            @Override
            public void describeTo(Description description) {
                description.description("a string equal to test");
            }
        };
        Object arg = "different";
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        boolean result = tool.toStringEquals(stringMatcher, arg);
        assertFalse(result);
    }
}