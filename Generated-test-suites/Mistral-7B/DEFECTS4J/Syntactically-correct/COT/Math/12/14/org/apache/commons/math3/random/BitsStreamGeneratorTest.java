import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

 private BitsStreamGenerator generator = new BitsStreamGenerator() {
 @Override
 protected int next(int bits) {
 throw new UnsupportedOperationException();
 }
 };

 @Test(expected = IllegalArgumentException.class)
 public void testSetSeedWithNegativeInt() {
 generator.setSeed(-1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSeedWithNegativeLong() {
 generator.setSeed(-1L);
 }

 @Test
 public void testNextBoolean() {
 final boolean expectedTrue = true;
 final boolean expectedFalse = false;
 for (int i = 0; i < 10; i++) {
 final boolean result = generator.nextBoolean();
 if (result == expectedTrue) {
 expectedFalse = !result;
 }
 assertTrue(result == expectedTrue || result == expectedFalse);
 }
 }

 @Test
 public void testNextBytes() {
 final byte[] expected = new byte[]{1, 2, 3, 4, 5};
 final byte[] result = new byte[5];
 generator.nextBytes(result);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testNextDouble() {
 final double expectedMin = 0.0;
 final double expectedMax = 1.0;
 final double tolerance = 1e-10;
 for (int i = 0; i < 10; i++) {
 final double result = generator.nextDouble();
 assertTrue(Precision.isClose(expectedMin, result, tolerance)
         || Precision.isClose(expectedMax, result, tolerance));
 }
 }

 @Test
 public void testNextGaussian() {
 final double expectedMin = -3.0;
 final double expectedMax = 3.0;
 final double tolerance = 0.1;
 for (int i = 0; i < 10; i++) {
 final double result = generator.nextGaussian();
 assertTrue(Precision.isClose(expectedMin, result, tolerance)
         || Precision.isClose(expectedMax, result, tolerance));
 }
 }

 @Test
 public void testNextInt() {
 final int expectedMin = Integer.MIN_VALUE;
 final int expectedMax = Integer.MAX_VALUE;
 for (int i = 0; i < 10; i++) {
 final int result = generator.nextInt();
 assertTrue(result >= expectedMin && result <= expectedMax);
 }
 }

 @Test
 public void testNextIntWithPowerOfTwo() {
 final int expected = 16;
 final int result = generator.nextInt(expected);
 assertTrue((result & (expected - 1)) == result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextIntWithNegativeInt() {
 generator.nextInt(-1);
 }

 @Test
 public void testClear() {
 generator.nextGaussian();
 generator.clear();
 assertFalse(Double.isNaN(generator.nextGaussian()));
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNextIntWithZero() {
 generator.nextInt(0);
 }

 @Test
 public void testNextIntWithLargeNumber() {
 final int expected = Integer.MAX_VALUE / 2 + 1;
 generator.nextInt(expected);
 }

}