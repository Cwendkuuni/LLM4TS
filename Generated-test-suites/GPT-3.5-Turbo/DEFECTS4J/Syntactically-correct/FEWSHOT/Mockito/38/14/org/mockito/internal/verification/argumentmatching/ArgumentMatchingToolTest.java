package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher<String>> matchers = new LinkedList<>();
        matchers.add(null);
        matchers.add(null);

        String[] arguments = new String[2];
        arguments[0] = "arg1";
        arguments[1] = "arg2";

        Integer[] expected = new Integer[]{};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithMatchersAndArguments() {
        List<Matcher<Integer>> matchers = new LinkedList<>();
        matchers.add(null);
        matchers.add(null);

        Integer[] arguments = new Integer[2];
        arguments[0] = 1;
        arguments[1] = 2;

        Integer[] expected = new Integer[]{};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithMismatchedTypes() {
        List<Matcher<String>> matchers = new LinkedList<>();
        matchers.add(null);
        matchers.add(null);

        Integer[] arguments = new Integer[2];
        arguments[0] = 1;
        arguments[1] = 2;

        Integer[] expected = new Integer[]{0, 1};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithExtraTypeInformationAndMismatchedTypes() {
        List<Matcher<CustomType>> matchers = new LinkedList<>();
        matchers.add(null);
        matchers.add(null);

        CustomType[] arguments = new CustomType[2];
        arguments[0] = new CustomType("arg1");
        arguments[1] = new CustomType("arg2");

        Integer[] expected = new Integer[]{};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexesWithExtraTypeInformationAndMismatchedTypes2() {
        List<Matcher<CustomType>> matchers = new LinkedList<>();
        matchers.add(null);
        matchers.add(null);

        CustomType[] arguments = new CustomType[2];
        arguments[0] = new CustomType("arg1");
        arguments[1] = new CustomType("arg2");

        Integer[] expected = new Integer[]{0, 1};

        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Integer[] actual = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);

        assertArrayEquals(expected, actual);
    }

    private class CustomType {
        private String value;

        public CustomType(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            CustomType customType = (CustomType) obj;
            return value.equals(customType.value);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(new Object[]{value});
        }
    }
}