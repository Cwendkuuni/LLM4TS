package org.mockito.internal.verification.argumentmatching;

import static org.junit.Assert.*;
import org.junit.Test;

public class ArgumentMatchingToolTest {
    @Test
    public void testGetSuspiciouslyNotMatchingArgsIndexes() {
        ArgumentMatchingTool argumentMatchingTool = new ArgumentMatchingTool();

        // Test when matchers size is not equal to arguments length
        List<Matcher> matchers1 = new ArrayList<>();
        matchers1.add(new ContainsExtraTypeInformation());
        matchers1.add(new ContainsExtraTypeInformation());
        Object[] arguments1 = new Object[2];
        arguments1[0] = "test";
        arguments1[1] = 123;
        Integer[] expected1 = new Integer[0];
        Integer[] result1 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers1, arguments1);
        assertArrayEquals(expected1, result1);
        
        // Test when arguments match with matchers
        List<Matcher> matchers2 = new ArrayList<>();
        matchers2.add(new ContainsExtraTypeInformation());
        matchers2.add(new ContainsExtraTypeInformation());
        Object[] arguments2 = new Object[2];
        arguments2[0] = new ContainsExtraTypeInformation();
        arguments2[1] = new ContainsExtraTypeInformation();
        Integer[] expected2 = new Integer[0];
        Integer[] result2 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers2, arguments2);
        assertArrayEquals(expected2, result2);
        
        // Test when arguments don't match with matchers based on toString()
        List<Matcher> matchers3 = new ArrayList<>();
        matchers3.add(new ContainsExtraTypeInformation());
        matchers3.add(new ContainsExtraTypeInformation());
        Object[] arguments3 = new Object[2];
        arguments3[0] = "test";
        arguments3[1] = 123;
        Integer[] expected3 = {0, 1};
        Integer[] result3 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers3, arguments3);
        assertArrayEquals(expected3, result3);
        
        // Test when arguments don't match with matchers based on type
        List<Matcher> matchers4 = new ArrayList<>();
        matchers4.add(new ContainsExtraTypeInformation());
        matchers4.add(new ContainsExtraTypeInformation());
        Object[] arguments4 = new Object[2];
        arguments4[0] = new ContainsExtraTypeInformation();
        arguments4[1] = "test";
        Integer[] expected4 = {1};
        Integer[] result4 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers4, arguments4);
        assertArrayEquals(expected4, result4);
        
        // Test when matchers contain ContainsExtraTypeInformation and arguments match based on toString()
        List<Matcher> matchers5 = new ArrayList<>();
        matchers5.add(new ContainsExtraTypeInformation());
        matchers5.add(new ContainsExtraTypeInformation());
        Object[] arguments5 = new Object[2];
        arguments5[0] = new ContainsExtraTypeInformation();
        arguments5[1] = "test";
        Integer[] expected5 = {0};
        Integer[] result5 = argumentMatchingTool.getSuspiciouslyNotMatchingArgsIndexes(matchers5, arguments5);
        assertArrayEquals(expected5, result5);
    }
}