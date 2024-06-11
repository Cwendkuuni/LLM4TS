package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.internal.matchers.ContainsExtraTypeInformation.*;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        // Initialize matchers and arguments
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments;

        // Test case 1: Empty matchers and empty arguments
        arguments = new Object[0];
        Integer[] expected1 = {};
        Integer[] result1 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected1, result1);

        // Test case 2: Single matcher and single argument that match
        matchers.add(equalTo(1));
        arguments = new Object[]{1};
        Integer[] expected2 = {};
        Integer[] result2 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected2, result2);

        // Test case 3: Single matcher and single argument that do not match
        matchers.clear();
        arguments = new Object[]{2};
        Integer[] expected3 = {0};
        Integer[] result3 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected3, result3);

        // Test case 4: Multiple matchers and multiple arguments where some match and some don't
        matchers.add(equalTo(1));
        matchers.add(equalTo("foo"));
        matchers.add(equalTo(true));
        arguments = new Object[]{1, "bar", true};
        Integer[] expected4 = {1};
        Integer[] result4 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected4, result4);

        // Test case 5: Multiple matchers and multiple arguments where all match
        arguments = new Object[]{1, "foo", true};
        Integer[] expected5 = {};
        Integer[] result5 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected5, result5);

        // Test case 6: Multiple matchers and multiple arguments where none match
        arguments = new Object[]{2, "bar", false};
        Integer[] expected6 = {0, 1, 2};
        Integer[] result6 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected6, result6);

        // Test case 7: Matchers and arguments of different lengths
        matchers.add(equalTo(1));
        matchers.add(equalTo("foo"));
        arguments = new Object[]{1};
        Integer[] expected7 = {};
        Integer[] result7 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected7, result7);

        // Test case 8: Matchers and arguments of different lengths
        arguments = new Object[]{1, "foo"};
        Integer[] expected8 = {1};
        Integer[] result8 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        assertArrayEquals(expected8, result8);
    }
}