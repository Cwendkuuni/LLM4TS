import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class MathUtilsTest {
    
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void testHash_double() {
        assertEquals(123456, MathUtils.hash(123.456));
    }

    @Test
    public void testHash_doubleArray() {
        double[] values = {1.0, 2.0, 3.0};
        assertEquals(12345, MathUtils.hash(values));
    }

    @Test
    public void testNormalizeAngle() {
        assertEquals(1.0, MathUtils.normalizeAngle(7.0, 2.0), 0.0001);
    }

    @Test
    public void testReduce() {
        assertEquals(3.0, MathUtils.reduce(8.0, 5.0, 2.0), 0.0001);
    }

    @Test
    public void testCopySign_byte() {
        assertEquals(-5, MathUtils.copySign((byte)5, (byte)-1));
    }

    @Test
    public void testCopySign_short() {
        assertEquals(10, MathUtils.copySign((short)-10, (short)1));
    }

    @Test
    public void testCopySign_int() throws MathArithmeticException {
        exception.expect(MathArithmeticException.class);
        MathUtils.copySign(Integer.MIN_VALUE, -1);
    }

    @Test
    public void testCopySign_long() throws MathArithmeticException {
        assertEquals(15, MathUtils.copySign(15L, -1L));
    }

    @Test
    public void testCheckFinite() throws NotFiniteNumberException {
        MathUtils.checkFinite(100.0);
    }

    @Test
    public void testCheckFinite_array() throws NotFiniteNumberException {
        double[] values = {1.0, 2.0, Double.NaN};
        exception.expect(NotFiniteNumberException.class);
        MathUtils.checkFinite(values);
    }

    @Test
    public void testCheckNotNull_message() throws NullArgumentException {
        exception.expect(NullArgumentException.class);
        MathUtils.checkNotNull(null, LocalizedFormats.ARRAY_ELEMENT, 1);
    }

    @Test
    public void testCheckNotNull_noArgs() throws NullArgumentException {
        MathUtils.checkNotNull(new Object());
    }
}