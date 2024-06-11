
package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.StringDescription;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        // Test case 1: empty matchers and arguments lists
        List<Matcher> matchers1 = new LinkedList<Matcher>();
        Object[] arguments1 = new Object[0];
        Integer[] expected1 = {};
        Integer[] actual1 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers1, arguments1);
        assertEquals(expected1.length, actual1.length);
        for (int i = 0; i < expected1.length; i++) {
            assertEquals(expected1[i], actual1[i]);
        }

        // Test case 2: non-empty matching matchers and arguments lists
        List<Matcher> matchers2 = new LinkedList<Matcher>();
        matchers2.add(new ContainsExtraTypeInformation());
        matchers2.add(new ContainsExtraTypeInformation());
        Object[] arguments2 = new Object[] { "foo", "foo" };
        Integer[] expected2 = {};
        Integer[] actual2 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers2, arguments2);
        assertEquals(expected2.length, actual2.length);
        for (int i = 0; i < expected2.length; i++) {
            assertEquals(expected2[i], actual2[i]);
        }

        // Test case 3: non-empty non-matching matchers and arguments lists
        List<Matcher> matchers3 = new LinkedList<Matcher>();
        matchers3.add(new ContainsExtraTypeInformation());
        matchers3.add(new ContainsExtraTypeInformation());
        Object[] arguments3 = new Object[] { "foo", "bar" };
        Integer[] expected3 = { 1 };
        Integer[] actual3 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers3, arguments3);
        assertEquals(expected3.length, actual3.length);
        for (int i = 0; i < expected3.length; i++) {
            assertEquals(expected3[i], actual3[i]);
        }
    }
}

