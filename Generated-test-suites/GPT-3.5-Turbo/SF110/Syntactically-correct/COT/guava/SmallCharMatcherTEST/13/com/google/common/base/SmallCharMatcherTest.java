
import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    // Test matches() method for a single character
    @Test
    public void testMatches() {
        SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "Test");
        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
    }

    // Test setBits() method
    @Test
    public void testSetBits() {
        SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "Test");
        BitSet table = new BitSet();
        charMatcher.setBits(table);
        assertTrue(table.get(0));
    }

    // Test chooseTableSize() method with setSize = 1
    @Test
    public void testChooseTableSizeOne() {
        int setSize = 1;
        int expectedTableSize = 2;
        int actualTableSize = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(expectedTableSize, actualTableSize);
    }

    // Test chooseTableSize() method with setSize > 1
    @Test
    public void testChooseTableSize() {
        int setSize = 5;
        int expectedTableSize = 8;
        int actualTableSize = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(expectedTableSize, actualTableSize);
    }

    // Test from() method with BitSet containing characters
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(97);
        String description = "Test";
        SmallCharMatcher charMatcher = SmallCharMatcher.from(chars, description);
        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
    }

    // Test the private method smear()
    @Test
    public void testSmear() {
        int hashCode = 123;
        int expectedSmear = -1055573635;
        int actualSmear = SmallCharMatcher.smear(hashCode);
        assertEquals(expectedSmear, actualSmear);
    }

    // Test the private method checkFilter()
    @Test
    public void testCheckFilter() {
        int c = 97;
        SmallCharMatcher charMatcher = SmallCharMatcher.from(new BitSet(), "Test");
        assertTrue(charMatcher.checkFilter(c));
    }

}
