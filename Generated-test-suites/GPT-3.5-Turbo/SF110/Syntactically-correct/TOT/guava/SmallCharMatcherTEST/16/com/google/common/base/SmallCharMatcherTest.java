package com.google.common.base;

import org.junit.Test;
import org.junit.Assert;
import java.util.BitSet;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        // Typical use case
        Assert.assertEquals(1234, SmallCharMatcher.smear(1234));
        // Edge case
        Assert.assertEquals(0, SmallCharMatcher.smear(0));
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[1], 0, true, "Description");

        // Typical use case
        Assert.assertTrue(matcher.checkFilter(0));
        // Error scenario
        Assert.assertFalse(matcher.checkFilter(1));
    }

    @Test
    public void testChooseTableSize() {
        // Typical use case
        Assert.assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        // Edge case
        Assert.assertEquals(8, SmallCharMatcher.chooseTableSize(5));
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(2);
        String description = "Description";
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);

        // Typical use case
        Assert.assertEquals(2, matcher.table.length);
        Assert.assertEquals(0b110, matcher.filter);
        Assert.assertFalse(matcher.containsZero);
        Assert.assertEquals(description, matcher.getDescription());
    }

    @Test
    public void testMatches() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(2);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "Description");

        // Typical use case
        Assert.assertTrue(matcher.matches((char)1));
        // Edge case
        Assert.assertFalse(matcher.matches((char)3));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{(char)1, '\0'}, 0b10, false, "Description");

        // Typical use case
        matcher.setBits(table);
        Assert.assertTrue(table.get(1));
        // Edge case
        Assert.assertFalse(table.get(0));
    }

}