import org.junit.Test;
import org.junit.Assert;
import java.util.BitSet;

public class SmallCharMatcherTest {

    @Test
    public void testMatches_containsZero() {
        char c = '\0';
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, true, "");
        boolean result = matcher.matches(c);
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_checkFilterTrue() {
        char c = 'a';
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0x1L, false, "");
        boolean result = matcher.matches(c);
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_checkFilterFalse() {
        char c = 'a';
        SmallCharMatcher matcher = new SmallCharMatcher(new char[0], 0L, false, "");
        boolean result = matcher.matches(c);
        Assert.assertFalse(result);
    }

    @Test
    public void testMatches_matchTrue() {
        char c = 'a';
        char[] table = new char[1];
        table[0] = 'a';
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0L, false, "");
        boolean result = matcher.matches(c);
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_matchFalse() {
        char c = 'b';
        char[] table = new char[1];
        table[0] = 'a';
        SmallCharMatcher matcher = new SmallCharMatcher(table, 0L, false, "");
        boolean result = matcher.matches(c);
        Assert.assertFalse(result);
    }

    @Test
    public void testSetBits_containsZero() {
        BitSet table = new BitSet();
        char[] tableArr = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(tableArr, 0x1L, true, "");
        matcher.setBits(table);

        boolean result1 = table.get(0);
        boolean result2 = table.get('a');
        boolean result3 = table.get('b');
        boolean result4 = table.get('c');

        Assert.assertTrue(result1);
        Assert.assertTrue(result2);
        Assert.assertTrue(result3);
        Assert.assertTrue(result4);
    }

    @Test
    public void testSetBits_matchTrue() {
        BitSet table = new BitSet();
        char[] tableArr = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(tableArr, 0L, false, "");
        matcher.setBits(table);

        boolean result1 = table.get('a');
        boolean result2 = table.get('b');
        boolean result3 = table.get('c');

        Assert.assertTrue(result1);
        Assert.assertTrue(result2);
        Assert.assertTrue(result3);
    }

    @Test
    public void testSetBits_matchFalse() {
        BitSet table = new BitSet();
        char[] tableArr = {'a', 'b', 'c'};
        SmallCharMatcher matcher = new SmallCharMatcher(tableArr, 0L, false, "");
        matcher.setBits(table);

        boolean result1 = table.get('d');
        boolean result2 = table.get('e');
        boolean result3 = table.get('f');

        Assert.assertFalse(result1);
        Assert.assertFalse(result2);
        Assert.assertFalse(result3);
    }
}