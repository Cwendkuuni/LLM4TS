package com.google.common.base.test;

import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.*;
import java.util.*;

public class SmallCharMatcherTest {
    
    @Test
    public void testMatches() {
        CharMatcher matcher = createMatcher();
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('\0'));
        assertFalse(matcher.matches('c'));
    }
    
    @Test
    public void testSetBits() {
        CharMatcher matcher = createMatcher();
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertTrue(table.get('\0'));
        assertFalse(table.get('c'));
    }
    
    private CharMatcher createMatcher() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        chars.set('\0');
        return SmallCharMatcher.from(chars, "abcMatcher");
    }
}