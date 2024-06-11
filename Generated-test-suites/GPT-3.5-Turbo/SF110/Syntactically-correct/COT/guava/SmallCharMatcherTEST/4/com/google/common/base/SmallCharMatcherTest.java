
package com.google.common.base;

import org.junit.Assert;
import org.junit.Test;

import java.util.BitSet;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero_returnsTrueIfCharIsZero() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "description");
        boolean result = matcher.matches('\0');
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_checkFilter_returnsFalseIfCharNotInFilter() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "description");
        boolean result = matcher.matches('a');
        Assert.assertFalse(result);
    }

    @Test
    public void testMatches_tableContainsChar_returnsTrueIfCharIsInTable() {
        char[] table = {'a', 'b', 'c'};
        long filter = 0b1111;
        boolean containsZero = true;
        String description = "description";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        boolean result = matcher.matches('b');
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_tableDoesNotContainChar_returnsFalseIfCharIsNotInTable() {
        char[] table = {'a', 'b', 'c'};
        long filter = 0b1111;
        boolean containsZero = true;
        String description = "description";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        boolean result = matcher.matches('d');
        Assert.assertFalse(result);
    }

    @Test
    public void testSetBits_containsZero_setsZeroBitInTable() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "description");
        BitSet table = new BitSet();
        matcher.setBits(table);
        Assert.assertTrue(table.get(0));
    }

    @Test
    public void testSetBits_tableContainsChars_setsBitsInTable() {
        char[] table = {'a', 'b', 'c'};
        long filter = 0b1111;
        boolean containsZero = true;
        String description = "description";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        BitSet result = new BitSet();
        matcher.setBits(result);
        Assert.assertTrue(result.get('a'));
        Assert.assertTrue(result.get('b'));
        Assert.assertTrue(result.get('c'));
    }

    @Test
    public void testChooseTableSize_setSizeEqualsOne_returnsTwo() {
        int setSize = 1;
        int result = SmallCharMatcher.chooseTableSize(setSize);
        Assert.assertEquals(2, result);
    }

    @Test
    public void testChooseTableSize_setSizeGreaterThanOne_returnsTableSize() {
        int setSize = 10;
        int result = SmallCharMatcher.chooseTableSize(setSize);
        Assert.assertEquals(16, result);
    }

    @Test
    public void testSmear_returnsSmearValue() {
        int hashCode = 123456789;
        int result = SmallCharMatcher.smear(hashCode);
        Assert.assertEquals(-280809993, result);
    }

    @Test
    public void testCheckFilter_returnsTrueIfCharIsInFilter() {
        char[] table = {'a', 'b', 'c'};
        long filter = 0b1111;
        boolean containsZero = true;
        String description = "description";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        boolean result = matcher.checkFilter('a');
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckFilter_returnsFalseIfCharIsNotInFilter() {
        char[] table = {'a', 'b', 'c'};
        long filter = 0b1111;
        boolean containsZero = true;
        String description = "description";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        boolean result = matcher.checkFilter('d');
        Assert.assertFalse(result);
    }

    // additional test cases for potential edge cases and exception handling scenarios
    // ...

}
