import static org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {
    
    @Test
    public void testSmear() {
        assertEquals(-1155484575, SmallCharMatcher.smear(1));
        assertEquals(-1483252754, SmallCharMatcher.smear(2));
        assertEquals(2067175576, SmallCharMatcher.smear(3));
    }
    
    @Test
    public void testCheckFilter() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x1L, false, "");
        assertTrue(matcher.checkFilter(0));
        assertFalse(matcher.checkFilter(1));
        assertFalse(matcher.checkFilter(2));
    }
    
    @Test
    public void testChooseTableSize() {
        assertEquals(2, SmallCharMatcher.chooseTableSize(1));
        assertEquals(4, SmallCharMatcher.chooseTableSize(2));
        assertEquals(8, SmallCharMatcher.chooseTableSize(4));
    }
    
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(1);
        chars.set(3);
        chars.set(5);
        SmallCharMatcher matcher = SmallCharMatcher.from(chars, "description");
        assertTrue(matcher.matches((char)1));
        assertFalse(matcher.matches((char)2));
        assertTrue(matcher.matches((char)3));
        assertFalse(matcher.matches((char)4));
        assertTrue(matcher.matches((char)5));
    }
    
    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher(null, 0x1L, true, "");
        assertTrue(matcher.matches((char)0));
        assertFalse(matcher.matches((char)1));
        assertFalse(matcher.matches((char)2));
        
        matcher = new SmallCharMatcher(new char[]{(char)1, (char)3, (char)5}, 0x1L, false, "");
        assertFalse(matcher.matches((char)0));
        assertTrue(matcher.matches((char)1));
        assertFalse(matcher.matches((char)2));
        assertFalse(matcher.matches((char)4));
        assertTrue(matcher.matches((char)5));
    }
    
    @Test
    public void testSetBits() {
        SmallCharMatcher matcher = new SmallCharMatcher(new char[]{(char)1, (char)3, (char)5}, 0x1L, true, "");
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get(0));
        assertTrue(table.get(1));
        assertFalse(table.get(2));
        assertTrue(table.get(3));
        assertFalse(table.get(4));
        assertTrue(table.get(5));
    }
}