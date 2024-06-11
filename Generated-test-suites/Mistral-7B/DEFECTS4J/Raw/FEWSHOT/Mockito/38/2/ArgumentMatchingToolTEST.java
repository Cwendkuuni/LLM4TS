package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.typeMatches;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

public class ArgumentMatchingToolTest {

 private static Matcher<Integer> integerMatcher = new TypeSafeMatcher<Integer>() {
        @Override
        public boolean matchesSafely(Integer item) {
            return item != null && item.equals(1);
        }

        @Override
        public void describeTo(StringDescription description) {
            description.appendText("an integer [1]");
        }
    };

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_EmptyList() {
        List<Matcher<Object>> matchers = Arrays.asList(integerMatcher);
        Object[] arguments = new Object[0];

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[0], indexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_MismatchingSize() {
        List<Matcher<Object>> matchers = Arrays.asList(integerMatcher);
        Object[] arguments = new Object[]{1, 2};

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[0], indexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_TypeMismatch() {
        List<Matcher<Object>> matchers = Arrays.asList(integerMatcher);
        Object[] arguments = new Object[]{1, "one"};

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[]{1}, indexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_ToStringMatch() {
        List<Matcher<Object>> matchers = Arrays.asList(new TypeSafeMatcher<String>() {
            @Override
            public boolean matchesSafely(String item) {
                return item != null && item.equals("hello");
            }

            @Override
            public void describeTo(StringDescription description) {
                description.appendText("a string [\"hello\"]");
            }
        });
        Object[] arguments = new Object[]{"hello", "world"};

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[0], indexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatch() {
        List<Matcher<Object>> matchers = Arrays.asList(new TypeSafeMatcher<Integer>() {
            @Override
            public boolean matchesSafely(Integer item) {
                return item != null && item.equals(1);
            }

            @Override
            public void describeTo(StringDescription description) {
                description.appendText("an integer [1]");
            }
        }, new TypeSafeMatcher<Long>() {
            @Override
            public boolean matchesSafely(Long item) {
                return item != null && item.equals(1L);
            }

            @Override
            public void describeTo(StringDescription description) {
                description.appendText("a long [1]");
            }
        });
        Object[] arguments = new Object[]{1};

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[0], indexes);

        matchers = Arrays.asList(integerMatcher, new TypeSafeMatcher<Long>() {
            @Override
            public boolean matchesSafely(Long item) {
                return item != null && item.equals(1L);
            }

            @Override
            public void describeTo(StringDescription description) {
                description.appendText("a long [1]");
            }
        });
        arguments = new Object[]{1L};

        indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[0], indexes);

        matchers = Arrays.asList(integerMatcher, new TypeSafeMatcher<Long>() {
            @Override
            public boolean matchesSafely(Long item) {
                return item != null && item.equals(2L);
            }

            @Override
            public void describeTo(StringDescription description) {
                description.appendText("a long [2]");
            }
        });
        arguments = new Object[]{1};

        indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[]{0}, indexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_SuspiciousMatchWithExtraTypeInformation() {
        List<Matcher<Object>> matchers = Arrays.asList(new ContainsExtraTypeInformation(integerMatcher),
                new ContainsExtraTypeInformation(new TypeSafeMatcher<Long>() {
                    @Override
                    public boolean matchesSafely(Long item) {
                        return item != null && item.equals(1L);
                    }

                    @Override
                    public void describeTo(StringDescription description) {
                        description.appendText("a long [1]");
                    }

                    @Override
                    public String toString() {
                        return "a long with extra type information [1]";
                    }
                }));
        Object[] arguments = new Object[]{1};

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[]{0}, indexes);

        matchers = Arrays.asList(new ContainsExtraTypeInformation(integerMatcher),
                new ContainsExtraTypeInformation(new TypeSafeMatcher<Long>() {
                    @Override
                    public boolean matchesSafely(Long item) {
                        return item != null && item.equals(2L);
                    }

                    @Override
                    public void describeTo(StringDescription description) {
                        description.appendText("a long [2]");
                    }

                    @Override
                    public String toString() {
                        return "a long with extra type information [2]";
                    }
                }));
        arguments = new Object[]{1};

        indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[0], indexes);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes_NullArgument() {
        List<Matcher<Object>> matchers = Arrays.asList(integerMatcher);
        Object[] arguments = new Object[]{null};

        Integer[] indexes = ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(new Integer[0], indexes);
    }
}