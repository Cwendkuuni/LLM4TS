import static junit.framework.Assert.*;

import com.google.common.base.*;
import org.junit.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");
        
        boolean result = matcher.matches('\0');
        
        assertTrue(result);
    }

    @Test
    public void testMatches_checkFilter() {
        SmallCharMatcher matcher = SmallCharMatcher.from(new BitSet(), "Test");
        
        boolean result = matcher.matches('A');
        
        assertFalse(result);
    }

    @Test
    public void testMatches_positive() {
        char[] table = {'A', 'B', 'C'};
        long filter = 7L;
        boolean containsZero = false;
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, "Test");

        boolean result = matcher.matches('B');
        
        assertTrue(result);
    }

    @Test
    public void testMatches_negative() {
        char[] table = {'A', '\0', 'C'};
        long filter = 4L;
        boolean containsZero = true;
        SmallCharMatcher matcher = new SmallCharMatcher(table, filter, containsZero, "Test");

        boolean result = matcher.matches('B');
        
        assertFalse(result);
    }

    @Test
    public void testSetBits_containsZero() {
        BitSet table = new BitSet();
        SmallCharMatcher matcher = SmallCharMatcher.from(table, "Test");

        matcher.setBits(table);
        
        assertTrue(table.get(0));
    }

    @Test
    public void testSetBits() {
        BitSet table = new BitSet();
        char[] tableData = {'A', 'B', 'C'};
        long filter = 7L;
        boolean containsZero = false;
        SmallCharMatcher matcher = new SmallCharMatcher(tableData, filter, containsZero, "Test");

        matcher.setBits(table);
        
        assertTrue(table.get('A'));
        assertTrue(table.get('B'));
        assertTrue(table.get('C'));
    }
}