import org.junit.Assert.*;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmear() {
        int hashCode = 123;
        int expectedResult = -1354718687;
        int result = SmallCharMatcher.smear(hashCode);
        assertEquals(expectedResult,result);
    }
    
    @Test
    public void testCheckFilter() {
        int c = 65;
        boolean expectedResult = true;
        boolean result = SmallCharMatcher.checkFilter(c);
        assertEquals(expectedResult,result);
    }
    
    @Test
    public void testChooseTableSize() {
        int setSize = 1024;
        int expectedResult = 2048;
        int result = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(expectedResult,result);
    }
    
    @Test
    public void testFrom() {
        BitSet chars = new BitSet();
        chars.set(65);
        chars.set(66);
        String description = "TestDescription";
        CharMatcher result = SmallCharMatcher.from(chars, description);
        assertNotNull(result);
    }
    
    @Test
    public void testMatches() {
        SmallCharMatcher matcher = new SmallCharMatcher();
        char c = 'A';
        boolean expectedResult = true;
        boolean result = matcher.matches(c);
        assertEquals(expectedResult,result);
    }
    
    @Test
    public void testSetBits() {
        SmallCharMatcher matcher = new SmallCharMatcher();
        BitSet table = new BitSet();
        matcher.setBits(table);
        assertTrue(table.get(0));
    }
}