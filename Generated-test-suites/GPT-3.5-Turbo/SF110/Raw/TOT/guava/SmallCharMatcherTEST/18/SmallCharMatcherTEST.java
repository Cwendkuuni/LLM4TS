
package com.google.common.base;

import org.junit.*;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        int hashCode = 12345;
        int expected = -761005232;
        
        int result = SmallCharMatcher.smear(hashCode);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x1L, true, null);
        int c = 5;
        
        boolean result = matcher.checkFilter(c);
        
        assertTrue(result);
    }
    
    @Test
    public void testChooseTableSize() {
        int setSize = 10;
        int expected = 16;
        
        int result = SmallCharMatcher.chooseTableSize(setSize);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        String description = "Test Matcher";
        
        CharMatcher matcher = SmallCharMatcher.from(chars, description);
        
        assertNotNull(matcher);
        assertEquals(description, matcher.toString());
        assertTrue(matcher.matches((char)1));
        assertFalse(matcher.matches((char)2));
    }
    
    @Test
    public void testMatches_withZeroChar() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x1L, true, null);
        char c = '\0';
        
        boolean result = matcher.matches(c);
        
        assertTrue(result);
    }
    
    @Test
    public void testMatches_withNonZeroChar_matching() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0x1L, true, null);
        char c = 'a';
        
        boolean result = matcher.matches(c);
        
        assertTrue(result);
    }
    
    @Test
    public void testMatches_withNonZeroChar_notMatching() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0x1L, true, null);
        char c = 'c';
        
        boolean result = matcher.matches(c);
        
        assertFalse(result);
    }
    
    @Test
    public void testMatches_withNonZeroChar_loopedIndex_reachedStartingIndex() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0x1L, true, null);
        char c = 'a';
        
        boolean result = matcher.matches(c);
        
        assertTrue(result);
    }
    
    @Test
    public void testSetBits_withContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0x1L, true, null);
        BitSet table = new BitSet();
        
        matcher.setBits(table);
        
        assertTrue(table.get(0));
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
    }
    
    @Test
    public void testSetBits_withoutContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b'}, 0x1L, false, null);
        BitSet table = new BitSet();
        
        matcher.setBits(table);
        
        assertFalse(table.get(0));
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
    }
}

