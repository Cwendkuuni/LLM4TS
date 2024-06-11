import com.google.common.base.CharMatcher;
import org.junit.Test;
import java.util.BitSet;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatchesContainsZero() {
        SmallCharMatcher charMatcher = (SmallCharMatcher) SmallCharMatcher.from(new BitSet(), "Empty Matcher");
        charMatcher.setBits(new BitSet());
        assertTrue(charMatcher.matches('\0'));
    }
    
    @Test
    public void testMatchesNotContainsZero() {
        SmallCharMatcher charMatcher = (SmallCharMatcher) SmallCharMatcher.from(new BitSet(), "Empty Matcher");
        charMatcher.setBits(new BitSet());
        assertFalse(charMatcher.matches('a'));
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(97);
        chars.set(98);
        chars.set(99);
        SmallCharMatcher charMatcher = (SmallCharMatcher) SmallCharMatcher.from(chars, "Char Matcher");
        charMatcher.setBits(new BitSet());
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[] {'a', 'b', 'c'}, 0L, false, "Char Matcher");
        assertTrue(charMatcher.checkFilter('a'));
        assertTrue(charMatcher.checkFilter('b'));
        assertTrue(charMatcher.checkFilter('c'));
        assertFalse(charMatcher.checkFilter('d'));
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
    }

    @Test
    public void testSmear() {
        assertEquals(1017228227, SmallCharMatcher.smear(65));
        assertEquals(-1946227189, SmallCharMatcher.smear(66));
        assertEquals(584007219, SmallCharMatcher.smear(67));
    }
}