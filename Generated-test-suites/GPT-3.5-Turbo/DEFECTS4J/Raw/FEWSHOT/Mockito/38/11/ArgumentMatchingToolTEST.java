package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.*;
import org.junit.Test;
import org.hamcrest.Matcher;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;

import java.util.LinkedList;
import java.util.List;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        List<Matcher> matchers = new LinkedList<>();
        Object[] arguments = new Object[5];

        // Test when matchers size is not equal to arguments length
        assertEquals(0, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments).length);

        // Test when matchers and arguments have length of 1
        matchers.add(new ContainsExtraTypeInformation());
        arguments[0] = new Object();
        assertEquals(0, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments).length);

        // Test when matchers and arguments have length of 2
        matchers.add(new ContainsExtraTypeInformation());
        arguments[1] = new Object();
        assertEquals(0, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments).length);

        // Test when matchers and arguments have length of 3
        matchers.add(new ContainsExtraTypeInformation());
        arguments[2] = new Object();
        assertEquals(0, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments).length);

        // Test when matchers and arguments have length of 4
        matchers.add(new ContainsExtraTypeInformation());
        arguments[3] = new Object();
        assertEquals(0, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments).length);

        // Test when matchers and arguments have length of 5
        matchers.add(new ContainsExtraTypeInformation());
        arguments[4] = new Object();
        assertEquals(0, tool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments).length);
    }

    @Test
    public void testSafelyMatches() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Matcher matcher = new ContainsExtraTypeInformation();
        Object argument = new Object();

        assertTrue(tool.safelyMatches(matcher, argument));
    }

    @Test
    public void testToStringEquals() {
        ArgumentMatchingTool tool = new ArgumentMatchingTool();
        Matcher matcher = new ContainsExtraTypeInformation();
        Object argument = new Object();

        assertFalse(tool.toStringEquals(matcher, argument));
    }
}