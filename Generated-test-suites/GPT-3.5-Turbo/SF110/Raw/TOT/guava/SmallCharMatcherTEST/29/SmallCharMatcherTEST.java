
import com.google.common.base.CharMatcher;
import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher scm = SmallCharMatcher.from(new BitSet().set(0), "Contains Zero");
        assertTrue(scm.matches('\0'));
    }

    @Test
    public void testMatches_checkFilter_true() {
        SmallCharMatcher scm = SmallCharMatcher.from(new BitSet().set(65), "Check Filter True");
        assertTrue(scm.matches('A'));
    }

    @Test
    public void testMatches_checkFilter_false() {
        SmallCharMatcher scm = SmallCharMatcher.from(new BitSet().set(65), "Check Filter False");
        assertFalse(scm.matches('B'));
    }

    @Test
    public void testMatches_tableEmpty() {
        SmallCharMatcher scm = SmallCharMatcher.from(new BitSet().set(65), "Table Empty");
        assertFalse(scm.matches('A'));
    }

    @Test
    public void testMatches_tableContains() {
        BitSet bitSet = new BitSet();
        bitSet.set(65);
        bitSet.set(66);
        SmallCharMatcher scm = SmallCharMatcher.from(bitSet, "Table Contains");
        assertTrue(scm.matches('A'));
        assertTrue(scm.matches('B'));
    }

    @Test
    public void testSetBits_containsZero() {
        SmallCharMatcher scm = SmallCharMatcher.from(new BitSet().set(0), "Set Bits Contains Zero");
        BitSet bitSet = new BitSet();
        scm.setBits(bitSet);
        assertTrue(bitSet.get(0));
    }

    @Test
    public void testSetBits_tableContains() {
        BitSet bitSet = new BitSet();
        bitSet.set(65);
        bitSet.set(66);
        SmallCharMatcher scm = SmallCharMatcher.from(bitSet, "Set Bits Table Contains");
        BitSet result = new BitSet();
        scm.setBits(result);
        assertTrue(result.get(65));
        assertTrue(result.get(66));
    }
}

