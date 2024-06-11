import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

 private BitsStreamGenerator generator = new BitsStreamGenerator() {
 @Override
 protected int next(int bits) {
 throw new UnsupportedOperationException("Method not implemented.");
 }
 };

 @Test(expected = UnsupportedOperationException.class)
 public void testAbstractMethods() {
 generator.setSeed(1);
 generator.setSeed(new int[]{1, 2, 3});
 generator.setSeed(1L);
 generator.next(5);
 generator.nextBoolean();
 generator.nextBytes(new byte[10]);
 generator.nextDouble();
 generator.nextFloat();
 generator.nextGaussian();
 generator.nextInt();
 generator.nextInt(10);
 generator.nextLong();
 generator.clear();
 }

 @Test
 public void testNextBoolean() {
 boolean expected = true;
 boolean actual = generator.nextBoolean();
 assertTrue("Expected: " + expected + ", but got: " + actual, expected == actual);
 }

 @Test
 public void testNextBytes() {
 byte[] expected = {1, 2, 3, 4};
 byte[] actual = new byte[4];
 generator.nextBytes(actual);
 assertArrayEquals("Expected:", expected, actual);
 }

 @Test
 public void testNextDouble() {
 double expected = 0.123456789;
 double actual = generator.nextDouble();
 assertEquals("Expected: " + expected + ", but got: " + actual, expected, actual, 0.000001);
 }

 @Test
 public void testNextFloat() {
 float expected = 0.123456f;
 float actual = generator.nextFloat();
 assertEquals("Expected: " + expected + ", but got: " + actual, expected, actual, 0.0001f);
 }

 @Test
 public void testNextGaussian() {
 double expectedMean = 0.5;
 double expectedStdDev = 1.0;
 double expected = 0.5 + 1.0 * FastMath.sqrt(Math.log(2) / 2.0);
 double actual = generator.nextGaussian();
 assertEquals("Expected mean: " + expectedMean + ", actual mean: " + (expectedMean + generator.getNextGaussianMeanOffset()), expectedMean, (expectedMean + actual) / 2.0, 0.01);
 assertEquals("Expected std dev: " + expectedStdDev + ", actual std dev: " + Math.sqrt(Math.abs(expectedMean - generator.nextGaussian()) / 2.0), expectedStdDev, Math.sqrt(Math.abs(expectedMean - actual) / 2.0), 0.01);
 }

 @Test
 public void testNextInt() {
 int expected = 123;
 int actual = generator.nextInt();
 assertEquals("Expected: " + expected + ", but got: " + actual, expected, actual);
 }

 @Test
 public void testNextIntWithNegativeSize() {
 try {
 generator.nextInt(-1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testNextIntWithZeroSize() {
 int expected = 0;
 int actual = generator.nextInt(0);
 assertEquals("Expected: " + expected + ", but got: " + actual, expected, actual);
 }

 @Test
 public void testNextIntWithSizeGreaterThanIntMax() {
 try {
 generator.nextInt(Integer.MAX_VALUE + 1);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testNextIntWithPowerOfTwoSize() {
 int expected = 1 << 16;
 int actual = generator.nextInt(1 << 16);
 assertEquals("Expected: " + expected + ", but got: " + actual, expected, actual);
 }

 @Test
 public void testNextIntWithNonPowerOfTwoSize() {
 int expected = 17;
 int actual = 0;
 int bits;
 do {
 bits = generator.next(31);
 actual = bits % 17;
 } while (bits - actual + 16 < 0);
 assertEquals("Expected: " + expected + ", but got: " + actual, expected, actual);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testNextIntWithNegativeSeed() {
 generator.setSeed(-1);
 }

 @Test
 public void testNextLong() {
 long expected = 1234567890123456L;
 long actual = generator.nextLong();
 assertEquals("Expected: " + expected + ", but got: " + actual, expected, actual);
 }

 @Test
 public void testClear() {
 double expectedMeanBefore = generator.getNextGaussianMeanOffset();
 generator.clear();
 double expectedMeanAfter = generator.getNextGaussianMeanOffset();
 assertNotSame("Expected different mean offsets before and after clear", expectedMeanBefore, expectedMeanAfter);
 }

}