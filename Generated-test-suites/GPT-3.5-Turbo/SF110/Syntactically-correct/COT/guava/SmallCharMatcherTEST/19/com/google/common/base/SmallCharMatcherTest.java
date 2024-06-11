
import com.google.common.annotations.GwtIncompatible;
import org.junit.Test;

import java.util.BitSet;

import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        assertEquals(-1706314737, SmallCharMatcher.smear(0));
        assertEquals(-333401289, SmallCharMatcher.smear(1));
        assertEquals(-596171041, SmallCharMatcher.smear(2));
        // Additional test cases for edge cases can be added here
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[0], 0, false, "");
        assertFalse(smallCharMatcher.checkFilter(0));
        assertFalse(smallCharMatcher.checkFilter(1));
        // Additional test cases for edge cases can be added here
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(2, SmallCharMatcher.chooseTableSize(2));
        assertEquals(4, SmallCharMatcher.chooseTableSize(3));
        // Additional test cases for edge cases can be added here
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "");
        assertTrue(charMatcher.matches((char)0));
        assertTrue(charMatcher.matches((char)1));
        assertFalse(charMatcher.matches((char)2));
        // Additional test cases for edge cases can be added here
    }

    @Test
    public void testMatches() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "");

        assertTrue(charMatcher.matches((char)0));
        assertTrue(charMatcher.matches((char)1));
        assertFalse(charMatcher.matches((char)2));
        // Additional test cases for edge cases can be added here
    }

    @Test
    public void testSetBits() {
        SmallCharMatcher smallCharMatcher = new SmallCharMatcher(new char[]{(char)0, (char)1}, 1, true, "");
        BitSet table = new BitSet();
        smallCharMatcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get(1));
        assertFalse(table.get(2));
        // Additional test cases for edge cases can be added here
    }

}
