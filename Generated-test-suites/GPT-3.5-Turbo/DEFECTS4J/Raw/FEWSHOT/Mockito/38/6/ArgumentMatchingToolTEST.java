
package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.LinkedList;
import java.util.List;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import org.mockito.internal.matchers.ContainsExtraTypeInformation;
import org.junit.Test;

public class ArgumentMatchingToolTest {

    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new ContainsExtraTypeInformation("abc"));
        matchers.add(new ContainsExtraTypeInformation(123));
        matchers.add(new ContainsExtraTypeInformation(true));
        
        Object[] arguments1 = {"def", 123, false};
        assertEquals(0, ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments1).length);
        
        Object[] arguments2 = {"def", "123", true};
        assertEquals(1, ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments2).length);
        
        Object[] arguments3 = {"abc", 123, true};
        assertEquals(1, ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments3).length);
        
        Object[] arguments4 = {"abc", 123, false};
        assertEquals(2, ArgumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers, arguments4).length);
    }
}
