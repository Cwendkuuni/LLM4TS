package com.google.common.base;

import static org.junit.Assert.*;

import com.google.common.annotations.GwtIncompatible;
import org.junit.Test;
import java.util.BitSet;

@GwtIncompatible("no precomputation is done in GWT")
public class SmallCharMatcherTest {

    @Test
    public void testSmear_withPositiveInput_returnsSmearResult() {
        int input = 10;
        int expected = 1973052520;

        int result = SmallCharMatcher.smear(input);

        assertEquals(expected, result);
    }

    @Test
    public void testCheckFilter_withMatchingFilter_returnsTrue() {
        char[] table = {'a', 'b', 'c'};
        long filter = 0x1L;
        boolean containsZero = true;
        String description = "Test";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        int c = 1;

        boolean result = matcher.checkFilter(c);

        assertTrue(result);
    }

    @Test
    public void testCheckFilter_withNonMatchingFilter_returnsFalse() {
        char[] table = {'a', 'b', 'c'};
        long filter = 0x0L;
        boolean containsZero = true;
        String description = "Test";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        int c = 1;

        boolean result = matcher.checkFilter(c);

        assertFalse(result);
    }

    @Test
    public void testChooseTableSize_withSetSizeOne_returnsTwo() {
        int setSize = 1;
        int expected = 2;

        int result = SmallCharMatcher.chooseTableSize(setSize);

        assertEquals(expected, result);
    }

    @Test
    public void testChooseTableSize_withSetSizeNotOne_returnsNextPowerOfTwo() {
        int setSize = 5;
        int expected = 8;

        int result = SmallCharMatcher.chooseTableSize(setSize);

        assertEquals(expected, result);
    }

    @Test
    public void testFrom_withValidChars_returnsSmallCharMatcher() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(2);
        String description = "Test";

        CharMatcher matcher = SmallCharMatcher.from(chars, description);

        assertNotNull(matcher);
    }

    @Test
    public void testMatches_withMatchingChar_returnsTrue() {
        char[] table = {'a', 'b', 'c'};
        long filter = 0x1L;
        boolean containsZero = true;
        String description = "Test";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        char c = 'b';

        boolean result = matcher.matches(c);

        assertTrue(result);
    }

    @Test
    public void testMatches_withNonMatchingChar_returnsFalse() {
        char[] table = {'a', 'b', 'c'};
        long filter = 0x1L;
        boolean containsZero = true;
        String description = "Test";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        char c = 'd';

        boolean result = matcher.matches(c);

        assertFalse(result);
    }

    @Test
    public void testSetBits_withContainsZero_setsBitAtZero() {
        char[] table = {'a', 'b', 'c'};
        long filter = 0x1L;
        boolean containsZero = true;
        String description = "Test";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        BitSet expected = new BitSet();
        expected.set(0);

        BitSet result = new BitSet();
        matcher.setBits(result);

        assertEquals(expected, result);
    }

    @Test
    public void testSetBits_withoutContainsZero_setsBitsInTable() {
        char[] table = {'a', 'b', 'c'};
        long filter = 0x1L;
        boolean containsZero = false;
        String description = "Test";
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, description);
        BitSet expected = new BitSet();
        expected.set('a');
        expected.set('b');
        expected.set('c');

        BitSet result = new BitSet();
        matcher.setBits(result);

        assertEquals(expected, result);
    }

}