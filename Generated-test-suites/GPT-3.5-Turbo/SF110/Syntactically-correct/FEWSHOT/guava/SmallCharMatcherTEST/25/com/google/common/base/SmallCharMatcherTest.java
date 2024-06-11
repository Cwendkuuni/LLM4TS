
package com.google.common.base;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;

import org.junit.Test;

public class SmallCharMatcherTest {
    
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(97);
        chars.set(98);
        chars.set(99);
        CharMatcher matcher = SmallCharMatcher.from(chars, "abc");
        
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        assertFalse(matcher.matches('d'));
        assertFalse(matcher.matches('e'));
    }
    
    @Test
    public void testMatches() {
        char[] table = new char[16];
        table[0] = 'a';
        table[1] = 'b';
        table[2] = 'c';
        table[3] = 'd';
        table[4] = 'e';
        SmallCharMatcher matcher = new SmallCharMatcher(table, 5L, true, "abcde");
        
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        assertTrue(matcher.matches('d'));
        assertTrue(matcher.matches('e'));
        assertFalse(matcher.matches('f'));
        assertFalse(matcher.matches('g'));
    }
    
    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[16], 0L, true, "abcde");
        
        matcher.setBits(table);
        
        assertTrue(table.get(0));
        assertFalse(table.get(1));
        assertFalse(table.get(2));
        assertFalse(table.get(3));
        assertFalse(table.get(4));
    }
}
