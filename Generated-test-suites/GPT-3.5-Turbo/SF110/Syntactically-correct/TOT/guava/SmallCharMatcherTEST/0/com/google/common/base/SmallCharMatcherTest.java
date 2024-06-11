import org.junit.*;
import static org.junit.Assert.*;
import com.google.common.base.*;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        assertEquals(-862048943 * Integer.rotateLeft(173 * -862048943, 15), SmallCharMatcher.smear(173));
        assertEquals(-862048943 * Integer.rotateLeft(0 * -862048943, 15), SmallCharMatcher.smear(0));
        assertEquals(-862048943 * Integer.rotateLeft(99 * -862048943, 15), SmallCharMatcher.smear(99));
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{1L}), "matcher");
        assertTrue(matcher.checkFilter(0));
        assertTrue(matcher.checkFilter(1));
        assertFalse(matcher.checkFilter(2));
    }

    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
        assertEquals(16, SmallCharMatcher.chooseTableSize(7));
        assertEquals(32, SmallCharMatcher.chooseTableSize(8));
        assertEquals(32, SmallCharMatcher.chooseTableSize(9));
        assertEquals(1024, SmallCharMatcher.chooseTableSize(1023));
        assertEquals(2048, SmallCharMatcher.chooseTableSize(1024));
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(3);
        chars.set(5);
        chars.set(7);
        chars.set(9);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "matcher");
        assertTrue(matcher.matches((char)1));
        assertFalse(matcher.matches((char)2));
        assertTrue(matcher.matches((char)3));
        assertFalse(matcher.matches((char)4));
        assertTrue(matcher.matches((char)5));
        assertFalse(matcher.matches((char)6));
        assertTrue(matcher.matches((char)7));
        assertFalse(matcher.matches((char)8));
        assertTrue(matcher.matches((char)9));
        assertFalse(matcher.matches((char)10));
    }

    @Test
    public void testMatches() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(2);
        chars.set(4);
        chars.set(6);
        chars.set(8);
        chars.set(10);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "matcher");
        assertTrue(matcher.matches((char)0));
        assertFalse(matcher.matches((char)1));
        assertTrue(matcher.matches((char)2));
        assertFalse(matcher.matches((char)3));
        assertTrue(matcher.matches((char)4));
        assertFalse(matcher.matches((char)5));
        assertTrue(matcher.matches((char)6));
        assertFalse(matcher.matches((char)7));
        assertTrue(matcher.matches((char)8));
        assertFalse(matcher.matches((char)9));
        assertTrue(matcher.matches((char)10));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = SmallCharMatcher.from(BitSet.valueOf(new long[]{1L,2L}), "matcher");
        matcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get(1));
        assertFalse(table.get(2));
        assertFalse(table.get(3));
    }
}