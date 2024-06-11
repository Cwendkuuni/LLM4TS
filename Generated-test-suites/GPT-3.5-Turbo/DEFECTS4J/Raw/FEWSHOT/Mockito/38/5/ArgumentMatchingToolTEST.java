
package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

@SuppressWarnings("unchecked")
public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[]{};
        assertArrayEquals(new Integer[0], tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
        
        matchers = new LinkedList<>();
        arguments = new Object[]{1};
        assertArrayEquals(new Integer[]{}, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
        
        matchers = new LinkedList<>();
        arguments = new Object[]{1, 2};
        assertArrayEquals(new Integer[]{}, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
        
        matchers = Arrays.asList(new ContainsExtraTypeInformation());
        arguments = new Object[]{1};
        assertArrayEquals(new Integer[]{0}, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
        
        matchers = Arrays.asList(new ContainsExtraTypeInformation(), new ContainsExtraTypeInformation());
        arguments = new Object[]{1, 2};
        assertArrayEquals(new Integer[]{0, 1}, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
        
        matchers = Arrays.asList(new ContainsExtraTypeInformation(), new ContainsExtraTypeInformation());
        arguments = new Object[]{1, 1};
        assertArrayEquals(new Integer[0], tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
        
        matchers = Arrays.asList(new ContainsExtraTypeInformation(), new ContainsExtraTypeInformation());
        arguments = new Object[]{1, "1"};
        assertArrayEquals(new Integer[]{1}, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments));
    }

    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Matcher matcher = new ContainsExtraTypeInformation();
        
        assertTrue(tool.safelyMatches(matcher, 1));
        assertTrue(tool.safelyMatches(matcher, "1"));
        assertFalse(tool.safelyMatches(matcher, null));
        assertFalse(tool.safelyMatches(null, 1));
    }

    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Matcher matcher = new ContainsExtraTypeInformation();
        
        assertFalse(tool.toStringEquals(matcher, 1));
        assertTrue(tool.toStringEquals(matcher, matcher));
    }
}
