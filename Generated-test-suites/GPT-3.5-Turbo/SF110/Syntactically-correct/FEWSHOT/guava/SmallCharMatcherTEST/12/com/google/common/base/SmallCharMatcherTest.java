
package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testCheckFilter_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0L, true, "");
        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testCheckFilter_notContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0L, false, "");
        assertFalse(matcher.matches('\0'));
    }

    @Test
    public void testCheckFilter_containsCharacter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0x1L, false, "");
        assertTrue(matcher.matches((char) 1));
    }

    @Test
    public void testCheckFilter_notContainsCharacter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0x0L, false, "");
        assertFalse(matcher.matches((char) 1));
    }

    @Test
    public void testChooseTableSize_setSize1() {
        int tableSize = SmallCharMatcher.chooseTableSize(1);
        assertEquals(2, tableSize);
    }

    @Test
    public void testChooseTableSize_setSize16() {
        int tableSize = SmallCharMatcher.chooseTableSize(16);
        assertEquals(32, tableSize);
    }

    @Test
    public void testFrom_singleCharacter() {
        BitSet chars = new BitSet();
        chars.set(1);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertTrue(matcher.matches((char) 1));
    }

    @Test
    public void testFrom_multipleCharacters() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(2);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertTrue(matcher.matches((char) 1));
        assertTrue(matcher.matches((char) 2));
        assertFalse(matcher.matches((char) 3));
    }

    @Test
    public void testFrom_containsZero() {
        BitSet chars = new BitSet();
        chars.set(0);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "");
        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testSmear() {
        int smeared = SmallCharMatcher.smear(1);
        // Smear(1) = 461845907 * Integer.rotateLeft(1 * -862048943, 15)
        //           = 461845907 * Integer.rotateLeft(-1724097889, 15)
        //           = 461845907 * 1982960567
        //           = 107151081
        assertEquals(107151081, smeared);
    }

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0x1L, true, "");
        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testMatches_notContainsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0x1L, false, "");
        assertFalse(matcher.matches('\0'));
    }

    @Test
    public void testMatches_containsCharacter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a'}, 0x1L, false, "");
        assertTrue(matcher.matches('a'));
    }

    @Test
    public void testMatches_notContainsCharacter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{}, 0x1L, false, "");
        assertFalse(matcher.matches('a'));
    }
}
