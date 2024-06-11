import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, true, "matcher");
        assertTrue(matcher.matches('\0'));
    }

    @Test
    public void testMatches_zeroNotContained() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, false, "matcher");
        assertFalse(matcher.matches('\0'));
    }

    @Test
    public void testMatches_containsCharacter() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 7L, false, "matcher");
        assertTrue(matcher.matches('a'));
    }

    @Test
    public void testMatches_characterNotContained() {
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 7L, false, "matcher");
        assertFalse(matcher.matches('d'));
    }

    @Test
    public void testSetBits_containsZero() {
        BitSet expectedBits = new BitSet();
        expectedBits.set(0);
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, true, "matcher");
        BitSet actualBits = new BitSet();
        matcher.setBits(actualBits);
        assertEquals(expectedBits, actualBits);
    }

    @Test
    public void testSetBits_doesNotContainZero() {
        BitSet expectedBits = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, false, "matcher");
        BitSet actualBits = new BitSet();
        matcher.setBits(actualBits);
        assertEquals(expectedBits, actualBits);
    }

    @Test
    public void testSetBits_containsCharacters() {
        BitSet expectedBits = new BitSet();
        expectedBits.set('a');
        expectedBits.set('b');
        expectedBits.set('c');
        char[] table = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(table, 7L, false, "matcher");
        BitSet actualBits = new BitSet();
        matcher.setBits(actualBits);
        assertEquals(expectedBits, actualBits);
    }

    @Test
    public void testSmear_negativeHashCode() {
        int hashCode = -12345;
        int expectedSmear = -1456275933;
        assertEquals(expectedSmear, SmallCharMatcher.smear(hashCode));
    }

    @Test
    public void testSmear_zeroHashCode() {
        int hashCode = 0;
        int expectedSmear = -1712282479;
        assertEquals(expectedSmear, SmallCharMatcher.smear(hashCode));
    }

    @Test
    public void testChooseTableSize_singleElement() {
        int setSize = 1;
        int expectedTableSize = 2;
        assertEquals(expectedTableSize, SmallCharMatcher.chooseTableSize(setSize));
    }

    @Test
    public void testChooseTableSize_multipleElements() {
        int setSize = 10;
        int expectedTableSize = 16;
        assertEquals(expectedTableSize, SmallCharMatcher.chooseTableSize(setSize));
    }

    @Test
    public void testCheckFilter_includedCharacter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 1L, true, "matcher");
        assertTrue(matcher.checkFilter(0));
    }

    @Test
    public void testCheckFilter_excludedCharacter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 1L, true, "matcher");
        assertFalse(matcher.checkFilter(1));
    }

    @Test
    public void testFrom_emptyChars() {
        BitSet chars = new BitSet();
        String description = "matcher";
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);
        assertFalse(matcher.matches('a'));
        assertFalse(matcher.matches('b'));
        assertFalse(matcher.matches('c'));
    }

    @Test
    public void testFrom_nonEmptyChars() {
        BitSet chars = new BitSet();
        chars.set('a');
        chars.set('b');
        chars.set('c');
        String description = "matcher";
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, description);
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertTrue(matcher.matches('c'));
        assertFalse(matcher.matches('d'));
    }
}