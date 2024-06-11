import static org.junit.Assert.*;
import org.junit.Test;

public class DfpDecTest {

    @Test
    public void testGetDecimalDigits() {
        DfpDec dfp = new DfpDec(null);
        int expected = 4 * dfp.mant.length - 3;
        int actual = dfp.getDecimalDigits();
        assertEquals(expected, actual);
    }

    @Test
    public void testRound() {
        DfpDec dfp = new DfpDec(null);

        // Test when msb is 0
        dfp.mant[dfp.mant.length - 1] = 0;
        int expected = 0;
        int actual = dfp.round(0);
        assertEquals(expected, actual);

        // Test when digits is equal to 4 * mant.length - 3
        dfp.mant[dfp.mant.length - 1] = 1;
        expected = 0; // Super class round method will be called
        actual = dfp.round(0);
        assertEquals(expected, actual);

        // Test when lsbthreshold is 1
        dfp.mant = new int[] {1000, 1000, 2000, 0};
        dfp.exp = 0;
        expected = 0 | dfp.mant[dfp.mant.length - 2];
        actual = dfp.round(0);
        assertEquals(expected, actual);

        // Test when lsbthreshold is not 1
        dfp.mant = new int[] {100, 100, 200, 0};
        dfp.exp = 0;
        expected = 0 | dfp.mant[dfp.mant.length - 2] % (10 / 10);
        actual = dfp.round(0);
        assertEquals(expected, actual);
    }

    @Test
    public void testNextAfter() {
        DfpDec dfp = new DfpDec(null);
        DfpDec x = new DfpDec(null);
        DfpDec result;

        // Test when getField returns different radix digits
        x.setField(new DfpField(10));
        x.setRadixDigits(5);
        result = dfp.nextAfter(x);
        assertTrue(result.isNaN());
        assertEquals(3, result.nans);

        // Test when this is less than x
        x.setField(new DfpField(2));
        x.setRadixDigits(2);
        dfp.setField(x.getField());
        dfp.setRadixDigits(2);
        dfp.mant = new int[] {1};
        x.mant = new int[] {0, 1};

        result = dfp.nextAfter(x);
        assertTrue(result.isGreaterThan(dfp));

        // Test when this is equal to x
        dfp.mant = new int[] {0, 1};
        result = dfp.nextAfter(x);
        assertEquals(dfp, result);

        // Test when dfp is less than 0
        dfp.setNegative();
        result = dfp.nextAfter(x);
        assertFalse(result.isNegative());

        // Test when up is false
        x.mant = new int[] {0, 0};
        result = dfp.nextAfter(x);
        assertTrue(result.isLessThan(dfp));

        // Test when up is true
        dfp.mant = new int[] {1, 0};
        result = dfp.nextAfter(x);
        assertTrue(result.isGreaterThan(dfp));

        // Test when result is 1.0
        dfp.mant = new int[] {1};
        result = dfp.nextAfter(x);
        assertEquals(result, dfp);

        // Test when result is NaN
        dfp.setNaN();
        result = dfp.nextAfter(x);
        assertTrue(result.isNaN());
    }

}