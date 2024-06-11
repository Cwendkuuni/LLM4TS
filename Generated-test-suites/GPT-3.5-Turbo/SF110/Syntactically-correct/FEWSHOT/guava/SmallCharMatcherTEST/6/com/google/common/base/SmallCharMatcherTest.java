package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.BitSet;

public class SmallCharMatcherTest {
    
    @Test
    public void testSmear() {
        assertEquals(68161475, SmallCharMatcher.smear(123));
        assertEquals(-1528914, SmallCharMatcher.smear(-789));
        assertEquals(0, SmallCharMatcher.smear(0));
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0, false, "");
        assertFalse(matcher.checkFilter(1));
        assertFalse(matcher.checkFilter(10));
        assertFalse(matcher.checkFilter(-1));
        assertTrue(matcher.checkFilter(0));
        
        matcher = new SmallCharMatcher(null, 10, false, "");
        assertFalse(matcher.checkFilter(1));
        assertFalse(matcher.checkFilter(20));
        assertFalse(matcher.checkFilter(-1));
        assertFalse(matcher.checkFilter(0));
        assertTrue(matcher.checkFilter(3));
        assertTrue(matcher.checkFilter(10));
    }
    
    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
        assertEquals(4, SmallCharMatcher.chooseTableSize(3));
        assertEquals(8, SmallCharMatcher.chooseTableSize(4));
        assertEquals(8, SmallCharMatcher.chooseTableSize(5));
        assertEquals(16, SmallCharMatcher.chooseTableSize(9));
        assertEquals(32, SmallCharMatcher.chooseTableSize(17));
        assertEquals(64, SmallCharMatcher.chooseTableSize(33));
        assertEquals(128, SmallCharMatcher.chooseTableSize(65));
        assertEquals(256, SmallCharMatcher.chooseTableSize(129));
    }
    
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(5);
        chars.set(10);
        
        CharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertTrue(matcher.matches((char)1));
        assertFalse(matcher.matches((char)2));
        assertFalse(matcher.matches((char)3));
        assertFalse(matcher.matches((char)4));
        assertTrue(matcher.matches((char)5));
        assertFalse(matcher.matches((char)6));
        assertFalse(matcher.matches((char)7));
        assertFalse(matcher.matches((char)8));
        assertFalse(matcher.matches((char)9));
        assertTrue(matcher.matches((char)10));
    }
    
    @Test
    public void testMatches() {
        char[] table = {(char)1, (char)5, (char)10};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0, false, "");

        assertTrue(matcher.matches((char)1));
        assertFalse(matcher.matches((char)2));
        assertFalse(matcher.matches((char)3));
        assertFalse(matcher.matches((char)4));
        assertTrue(matcher.matches((char)5));
        assertFalse(matcher.matches((char)6));
        assertFalse(matcher.matches((char)7));
        assertFalse(matcher.matches((char)8));
        assertFalse(matcher.matches((char)9));
        assertTrue(matcher.matches((char)10));
        assertFalse(matcher.matches((char)11));
        assertFalse(matcher.matches((char)12));
    }
    
    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        char[] characters = {(char)1, (char)5, (char)10};
        SmallCharMatcher matcher = new SmallCharMatcher(characters, 0, false, "");
        matcher.setBits(table);
        
        assertFalse(table.get(0));
        assertTrue(table.get(1));
        assertFalse(table.get(2));
        assertFalse(table.get(3));
        assertFalse(table.get(4));
        assertTrue(table.get(5));
        assertFalse(table.get(6));
        assertFalse(table.get(7));
        assertFalse(table.get(8));
        assertFalse(table.get(9));
        assertTrue(table.get(10));
    }
}