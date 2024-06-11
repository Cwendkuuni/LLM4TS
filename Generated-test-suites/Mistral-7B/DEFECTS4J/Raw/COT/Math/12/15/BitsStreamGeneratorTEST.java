import org.apache.commons.math3.RandomUtils; // for testing purpose
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BitsStreamGeneratorTest {

 private BitsStreamGenerator generator;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 generator = new MersenneTwisterFastRandomGenerator(); // Use a concrete implementation
 }

 @Test
 public void testSetSeed_int() {
 generator.setSeed(12345);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testSetSeed_negativeInt() {
 generator.setSeed(-12345);
 }

 @Test
 public void testSetSeed_intArray() {
 int[] seed = {1, 2, 3, 4, 5};
 generator.setSeed(seed);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSetSeed_emptyIntArray() {
 generator.setSeed(new int[0]);
 }

 @Test
 public void testSetSeed_long() {
 generator.setSeed(0x1234567890L);
 }

 @Test
 public void testNextBoolean() {
 assertEquals(true, generator.nextBoolean());
 assertEquals(false, generator.nextBoolean());
 }

 @Test
 public void testNextBytes_byteArray() {
 byte[] bytes = new byte[5];
 generator.nextBytes(bytes);
 }

 @Test
 public void testNextBytes_byteArray_lengthLessThanFour() {
 byte[] bytes = new byte[2];
 generator.nextBytes(bytes);
 }

 @Test
 public void testNextDouble() {
 // Test the range of Double.MIN_VALUE to Double.MAX_VALUE
 double minValue = Double.MIN_VALUE;
 double maxValue = Double.MAX_VALUE;
 for (int i = 0; i < 10; i++) {
 double value = generator.nextDouble();
 assertEquals(minValue <= value && value <= maxValue, true);
 }
 }

 @Test
 public void testNextFloat() {
 // Test the range of Float.MIN_VALUE to Float.MAX_VALUE
 float minValue = Float.MIN_VALUE;
 float maxValue = Float.MAX_VALUE;
 for (int i = 0; i < 10; i++) {
 float value = generator.nextFloat();
 assertEquals(minValue <= value && value <= maxValue, true);
 }
 }

 @Test
 public void testNextGaussian() {
 // Test the range of generated gaussian numbers
 double mean = 0.0;
 double stdDev = 1.0;
 double minValue = mean - 3 * stdDev;
 double maxValue = mean + 3 * stdDev;
 for (int i = 0; i < 10; i++) {
 double value = generator.nextGaussian();
 assertEquals(minValue <= value && value <= maxValue, true);
 }
 }

 @Test
 public void testNextInt() {
 // Test the range of Int.MIN_VALUE to Int.MAX_VALUE for a power of 2
 int maxValue = 1 << 31;
 assertEquals(maxValue, generator.nextInt(maxValue));
 // Test the range of Int.MIN_VALUE to Int.MAX_VALUE for a non-power of 2
 int n = 17;
 int expected = 123456;
 generator.nextInt(n); // Warm up the generator
 int actual = generator.nextInt(n);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNextInt_negativeInt() {
 generator.nextInt(-12345);
 }

 @Test
 public void testNextLong() {
 // Test the range of Long.MIN_VALUE to Long.MAX_VALUE
 long minValue = Long.MIN_VALUE;
 long maxValue = Long.MAX_VALUE;
 for (int i = 0; i < 10; i++) {
 long value = generator.nextLong();
 assertEquals(minValue <= value && value <= maxValue, true);
 }
 }

 @Test
 public void testClear() {
 double gaussian1 = generator.nextGaussian();
 double gaussian2 = generator.nextGaussian();
 generator.clear();
 double gaussian3 = generator.nextGaussian();
 assertFalse(Double.isNaN(gaussian1) || Double.isNaN(gaussian2));
 assertFalse(Double.isNaN(gaussian3));
 assertFalse(gaussian1 == gaussian2 || gaussian1 == gaussian3);
 }

}