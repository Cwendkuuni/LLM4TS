
import org.junit.Test;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

import static org.junit.Assert.*;

public class MathUtilsTest {

	@Test
	public void testHash_double() {
		double value = 10.0;
		int expectedHash = Double.valueOf(value).hashCode();
		assertEquals(expectedHash, MathUtils.hash(value));
	}

	@Test
	public void testHash_doubleArray() {
		double[] value = {1.0, 2.0, 3.0};
		int expectedHash = Arrays.hashCode(value);
		assertEquals(expectedHash, MathUtils.hash(value));
	}

	@Test
	public void testNormalizeAngle() {
		double a = 4 * MathUtils.TWO_PI;
        double center = MathUtils.TWO_PI;
        double expectedAngle = 4 * MathUtils.TWO_PI - 6.283185307179586 * Math.floor((a + 3.141592653589793 - center) / 6.283185307179586);
        assertEquals(expectedAngle, MathUtils.normalizeAngle(a, center), 0.000001);
	}

	@Test
	public void testReduce() {
		double a = 10.0;
        double period = 3.0;
        double offset = 1.0;
        double expectedReduced = a - period * Math.floor((a - offset) / period) - offset;
        assertEquals(expectedReduced, MathUtils.reduce(a, period, offset), 0.000001);
	}

	@Test
	public void testCopySign_byte() {
		byte magnitude = 10;
        byte sign = -20;
        byte expectedValue = (byte)(-magnitude);
        assertEquals(expectedValue, MathUtils.copySign(magnitude, sign));
	}

	@Test(expected = MathArithmeticException.class)
	public void testCopySign_byte_Exception() {
		byte magnitude = -128;
        byte sign = 1;
        MathUtils.copySign(magnitude, sign);
	}

	@Test
	public void testCopySign_short() {
		short magnitude = 100;
        short sign = -1000;
        short expectedValue = (short)(-magnitude);
        assertEquals(expectedValue, MathUtils.copySign(magnitude, sign));
	}

	@Test(expected = MathArithmeticException.class)
	public void testCopySign_short_Exception() {
		short magnitude = -32768;
        short sign = 1;
        MathUtils.copySign(magnitude, sign);
	}

	@Test
	public void testCopySign_int() {
		int magnitude = 100000;
        int sign = -1000000;
        int expectedValue = -magnitude;
        assertEquals(expectedValue, MathUtils.copySign(magnitude, sign));
	}

	@Test(expected = MathArithmeticException.class)
	public void testCopySign_int_Exception() {
		int magnitude = Integer.MIN_VALUE;
        int sign = 1;
        MathUtils.copySign(magnitude, sign);
	}

	@Test
	public void testCopySign_long() {
		long magnitude = 1000000L;
        long sign = -10000000L;
        long expectedValue = -magnitude;
        assertEquals(expectedValue, MathUtils.copySign(magnitude, sign));
	}

	@Test(expected = MathArithmeticException.class)
	public void testCopySign_long_Exception() {
		long magnitude = Long.MIN_VALUE;
        long sign = 1;
        MathUtils.copySign(magnitude, sign);
	}

	@Test
	public void testCheckFinite_double() {
		double value = 10.0;
        MathUtils.checkFinite(value);
	}

	@Test(expected = NotFiniteNumberException.class)
	public void testCheckFinite_double_Exception() {
		double value = Double.POSITIVE_INFINITY;
        MathUtils.checkFinite(value);
	}

	@Test
	public void testCheckFinite_doubleArray() {
		double[] value = {1.0, 2.0, 3.0};
        MathUtils.checkFinite(value);
	}

	@Test(expected = NotFiniteNumberException.class)
	public void testCheckFinite_doubleArray_Exception() {
		double[] value = {1.0, Double.POSITIVE_INFINITY, 3.0};
        MathUtils.checkFinite(value);
	}

	@Test
	public void testCheckNotNull_Object_Localizable_ObjectArray() {
		Object o = new Object();
        Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {1};
        MathUtils.checkNotNull(o, pattern, args);
	}

	@Test(expected = NullArgumentException.class)
	public void testCheckNotNull_Object_Localizable_ObjectArray_Exception() {
		Object o = null;
        Localizable pattern = LocalizedFormats.ARRAY_ELEMENT;
        Object[] args = {1};
        MathUtils.checkNotNull(o, pattern, args);
	}

	@Test
	public void testCheckNotNull_Object() {
		Object o = new Object();
        MathUtils.checkNotNull(o);
	}

	@Test(expected = NullArgumentException.class)
	public void testCheckNotNull_Object_Exception() {
		Object o = null;
        MathUtils.checkNotNull(o);
	}
}

