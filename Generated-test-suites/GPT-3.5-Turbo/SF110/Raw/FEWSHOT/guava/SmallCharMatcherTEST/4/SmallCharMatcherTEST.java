package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;

import org.junit.Test;

public class SmallCharMatcherTest {
    
    @Test
    public void testSmear() {
        assertEquals(-861986794, SmallCharMatcher.smear(123456));
        assertEquals(1009118258, SmallCharMatcher.smear(987654));
        assertEquals(-1804516546, SmallCharMatcher.smear(-654321));
        assertEquals(-2010419802, SmallCharMatcher.smear(-987654));
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[1], 1L, true, "smallCharMatcher");
        assertTrue(smallCharMatcher.checkFilter(0));
        assertFalse(smallCharMatcher.checkFilter(1));
        smallCharMatcher = new SmallCharMatcher(new char[1], 0L, false, "smallCharMatcher");
        assertFalse(smallCharMatcher.checkFilter(0));
        assertFalse(smallCharMatcher.checkFilter(1));
    }
    
    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
        assertEquals(8, SmallCharMatcher.chooseTableSize(5));
        assertEquals(16, SmallCharMatcher.chooseTableSize(12));
        assertEquals(1024, SmallCharMatcher.chooseTableSize(510));
    }
    
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(3);
        String description = "smallCharMatcher";
        CharMatcher charMatcher = SmallCharMatcher.from(chars, description);
        assertTrue(charMatcher.matches((char)1));
        assertFalse(charMatcher.matches((char)2));
        assertTrue(charMatcher.matches((char)3));
        assertEquals(description, charMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        char[] table = new char[1024];
        table[0] = 'a';
        table[1] = 'b';
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(table, 1L, true, "smallCharMatcher");
        assertTrue(smallCharMatcher.matches('a'));
        assertFalse(smallCharMatcher.matches('b'));
        assertFalse(smallCharMatcher.matches('c'));
    }
    
    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        char[] chars = {'a', 'b', 'c'};
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(chars, 7L, true, "smallCharMatcher");
        smallCharMatcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get(1));
        assertTrue(table.get(2));
        assertFalse(table.get(3));
    }
}