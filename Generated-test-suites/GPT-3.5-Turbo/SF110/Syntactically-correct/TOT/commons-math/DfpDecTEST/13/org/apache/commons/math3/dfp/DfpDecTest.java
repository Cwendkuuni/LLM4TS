import org.apache.commons.math3.dfp.Dfp;
import org.apache.commons.math3.dfp.DfpField;
import org.junit.Test;
import static org.junit.Assert.*;

public class DfpDecTest {

    private final DfpField field = new DfpField(16);
    private final DfpDec testInstance = new DfpDec(field);
    
    @Test
    public void testGetDecimalDigits() {
        // Typical use case
        int expected = 13;
        int actual = testInstance.getDecimalDigits();
        assertEquals(expected, actual);

        // Edge case (minimum value)
        field.setRadixDigits(1);
        expected = 1;
        actual = testInstance.getDecimalDigits();
        assertEquals(expected, actual);

        // Edge case (maximum value)
        field.setRadixDigits(32768);
        expected = 131069;
        actual = testInstance.getDecimalDigits();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testRound() {
        // Typical use case
        int in = 5;
        int expected = 0;
        int actual = testInstance.round(in);
        assertEquals(expected, actual);

        // Edge case (msb = 0)
        testInstance.mant[testInstance.mant.length - 1] = 0;
        expected = 0;
        actual = testInstance.round(in);
        assertEquals(expected, actual);

        // Edge case (lsbthreshold <= 1 and digits = 4 * mant.length - 3)
        testInstance.mant[testInstance.mant.length - 1] = 1;
        expected = 0;
        actual = testInstance.round(in);
        assertEquals(expected, actual);

        // Edge case (exp < -32767)
        testInstance.exp = -32768;
        expected = 8;
        actual = testInstance.round(in);
        assertEquals(expected, actual);

        // Edge case (exp > 32768)
        testInstance.exp = 32769;
        expected = 4;
        actual = testInstance.round(in);
        assertEquals(expected, actual);

        // Edge case (n != 0 or discarded != 0)
        testInstance.exp = 0;
        testInstance.mant[0] = 1;
        expected = 16;
        actual = testInstance.round(in);
        assertEquals(expected, actual);
    }

    @Test
    public void testNextAfter() {
        // Typical use case (up = false)
        Dfp x = testInstance.newInstance(2);
        Dfp expected = testInstance.newInstance(1);
        Dfp actual = testInstance.nextAfter(x);
        assertEquals(expected, actual);

        // Typical use case (up = true)
        x = testInstance.newInstance(1);
        expected = testInstance.newInstance(2);
        actual = testInstance.nextAfter(x);
        assertEquals(expected, actual);

        // Edge case (x = this)
        x = testInstance.newInstance(1);
        expected = testInstance.newInstance(1);
        actual = testInstance.nextAfter(x);
        assertEquals(expected, actual);

        // Edge case (this < 0)
        testInstance.sign = -1;
        x = testInstance.newInstance(2);
        expected = testInstance.newInstance(3);
        actual = testInstance.nextAfter(x);
        assertEquals(expected, actual);

        // Edge case (result2.classify() == 1 and this.classify() != 1)
        testInstance.sign = 1;
        testInstance.nans = 1;
        x = testInstance.newInstance(2);
        expected = testInstance.newInstance(2);
        Dfp actual = testInstance.nextAfter(x);
        assertEquals(expected, actual);

        // Edge case (result2.equals(0) and !this.equals(0))
        testInstance.nans = 0;
        expected = testInstance.newInstance(2);
        actual = testInstance.nextAfter(x);
        assertEquals(expected, actual);
    }
}