package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertArrayEquals;

import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

public class ArgumentMatchingToolTest {
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new LinkedList<Matcher>();
        matchers.add(new ContainsExtraTypeInformation());
        matchers.add(new ContainsExtraTypeInformation());
        
        Object[] arguments = new Object[] { "foo", "bar" };
        
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        
        Integer[] expected = new Integer[] { 0, 1 };
        Integer[] result = tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments);
        
        assertArrayEquals(expected, result);
    }
}