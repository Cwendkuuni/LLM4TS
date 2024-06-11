import com.google.common.base.CharMatcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;

public class SmallCharMatcherTest {

    private SmallCharMatcher smallCharMatcher;

    @Before
    public void setup() {
        BitSet bitSet = new BitSet();
        bitSet.set(0);
        bitSet.set(1);
        bitSet.set(2);
        String description = "Test Description";
        smallCharMatcher = SmallCharMatcher.from(bitSet, description);
    }

    @Test
    public void testMatches_containsZero() {
        boolean result = smallCharMatcher.matches('\0');
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_doesNotContainZero() {
        boolean result = smallCharMatcher.matches('A');
        Assert.assertFalse(result);
    }

    @Test
    public void testMatches_validCharacter() {
        boolean result = smallCharMatcher.matches('B');
        Assert.assertTrue(result);
    }

    @Test
    public void testMatches_invalidCharacter() {
        boolean result = smallCharMatcher.matches('C');
        Assert.assertFalse(result);
    }

    @Test
    public void testSetBits_containsZero() {
        BitSet table = new BitSet();
        smallCharMatcher.setBits(table);
        boolean result = table.get(0);
        Assert.assertTrue(result);
    }

    @Test
    public void testSetBits_containsValidCharacter() {
        BitSet table = new BitSet();
        smallCharMatcher.setBits(table);
        boolean result = table.get(1);
        Assert.assertTrue(result);
    }

    @Test
    public void testSetBits_doesNotContainCharacter() {
        BitSet table = new BitSet();
        smallCharMatcher.setBits(table);
        boolean result = table.get(3);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testChooseTableSize_zeroSetSize() {
        int result = SmallCharMatcher.chooseTableSize(0);
        Assert.assertEquals(2, result);
    }
    
    @Test
    public void testChooseTableSize_setSizeExactlyPowerOfTwo() {
        int result = SmallCharMatcher.chooseTableSize(4);
        Assert.assertEquals(8, result);
    }
    
    @Test
    public void testChooseTableSize_setSizeGreaterThanPowerOfTwo() {
        int result = SmallCharMatcher.chooseTableSize(7);
        Assert.assertEquals(8, result);
    }

    @Test
    public void testSmear_zeroHashCode() {
        int result = SmallCharMatcher.smear(0);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testSmear_nonZeroHashCode() {
        int result = SmallCharMatcher.smear(1);
        Assert.assertEquals(710605016, result);
    }

    @Test
    public void testCheckFilter_onFilterBitSet_true() {
        boolean result = smallCharMatcher.checkFilter(0);
        Assert.assertTrue(result);
    }

    @Test
    public void testCheckFilter_offFilterBitSet_false() {
        boolean result = smallCharMatcher.checkFilter(3);
        Assert.assertFalse(result);
    }
}