import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        int result = SmallCharMatcher.smear(5);
        assertEquals(-527225319, result);
    }

    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 7, true, "test");
        assertTrue(matcher.checkFilter('a'));
        assertFalse(matcher.checkFilter('d'));
    }

    @Test
    public void testChooseTableSize() {
        int result = SmallCharMatcher.chooseTableSize(5);
        assertEquals(8, result);
    }

    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(0);
        chars.set(1);
        CharMatcher matcher = SmallCharMatcher.from(chars, "test");
        assertTrue(matcher.matches('a'));
        assertTrue(matcher.matches('b'));
        assertFalse(matcher.matches('c'));
    }

    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 7, true, "test");
        assertTrue(matcher.matches('a'));
        assertFalse(matcher.matches('d'));
    }

    @Test
    public void testSetBits() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{'a', 'b', 'c'}, 7, true, "test");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get('a'));
        assertTrue(table.get('b'));
        assertFalse(table.get('d'));
    }

}