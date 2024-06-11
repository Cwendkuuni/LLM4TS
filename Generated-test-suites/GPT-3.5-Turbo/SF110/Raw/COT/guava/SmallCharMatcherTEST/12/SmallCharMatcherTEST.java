import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");
        assertFalse(matcher.matches('a'));
        assertFalse(matcher.matches('\0'));
    }

    @Test
    public void testSmear() {
        int hashCode = 12345;
        int smeared = SmallCharMatcher.smear(hashCode);
        // assert smeared value here
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, false, "Test");
        assertFalse(matcher.checkFilter(0));
    }

    @Test
    public void testChooseTableSize() {
        int setSize = 10;
        int tableSize = SmallCharMatcher.chooseTableSize(setSize);
        // assert tableSize value here
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "Test");
        // assert matcher object here
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, false, "Test");
        matcher.setBits(table);
        // assert table value here
    }
}