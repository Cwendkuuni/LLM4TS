
package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;

import org.junit.Test;

public class SmallCharMatcherTest {
    @Test
    public void testFrom() {
        BitSet chars1 = new BitSet();
        chars1.set(1);
        chars1.set(2);
        chars1.set(3);
        CharMatcher matcher1 = SmallCharMatcher.from(chars1, "Matcher1");
        assertTrue(matcher1.matches((char) 1));
        assertTrue(matcher1.matches('2'));
        assertTrue(matcher1.matches('3'));
        assertFalse(matcher1.matches('4'));
        
        BitSet chars2 = new BitSet();
        chars2.set(48);
        chars2.set(49);
        chars2.set(50);
        CharMatcher matcher2 = SmallCharMatcher.from(chars2, "Matcher2");
        assertTrue(matcher2.matches('0'));
        assertTrue(matcher2.matches('1'));
        assertTrue(matcher2.matches('2'));
        assertFalse(matcher2.matches('3'));
    }
    
    @Test
    public void testMatches() {
        BitSet chars1 = new BitSet();
        chars1.set(1);
        chars1.set(2);
        chars1.set(3);
        CharMatcher matcher1 = SmallCharMatcher.from(chars1, "Matcher1");
        assertTrue(matcher1.matches((char) 1));
        assertTrue(matcher1.matches('2'));
        assertTrue(matcher1.matches('3'));
        assertFalse(matcher1.matches('4'));
        
        BitSet chars2 = new BitSet();
        chars2.set(48);
        chars2.set(49);
        chars2.set(50);
        CharMatcher matcher2 = SmallCharMatcher.from(chars2, "Matcher2");
        assertTrue(matcher2.matches('0'));
        assertTrue(matcher2.matches('1'));
        assertTrue(matcher2.matches('2'));
        assertFalse(matcher2.matches('3'));
    }
    
    @Test
    public void testSetBits() {
        BitSet chars1 = new BitSet();
        chars1.set(1);
        chars1.set(2);
        chars1.set(3);
        CharMatcher matcher1 = SmallCharMatcher.from(chars1, "Matcher1");
        BitSet table1 = new BitSet();
        matcher1.setBits(table1);
        assertTrue(table1.get(1));
        assertTrue(table1.get(2));
        assertTrue(table1.get(3));
        assertFalse(table1.get(4));
        
        BitSet chars2 = new BitSet();
        chars2.set(48);
        chars2.set(49);
        chars2.set(50);
        CharMatcher matcher2 = SmallCharMatcher.from(chars2, "Matcher2");
        BitSet table2 = new BitSet();
        matcher2.setBits(table2);
        assertTrue(table2.get(48));
        assertTrue(table2.get(49));
        assertTrue(table2.get(50));
        assertFalse(table2.get(51));
    }
}
