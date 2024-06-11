import org.apache.commons.math3.random.BitsStreamGenerator;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

 private BitsStreamGenerator generator;

 @Before
 public void setUp() {
 generator = new BitsStreamGenerator();
 }

 // Test for constructor
 @Test
 public void testConstructor() {
 // Check that object is initialized
 assertNotNull(generator);
 }

 // Test for setSeed(int) method
 @Test
 public void testSetSeedInt() {
 // Set seed with an integer value
 generator.setSeed(12345);
 // Check that seed has been set
 assertEquals(12345, generator.nextInt());
 }

 // Test for setSeed(int[]) method
 @Test
 public void testSetSeedIntArray() {
 int[] seed = {1, 2, 3, 4, 5};
 generator.setSeed(seed);
 // Check that seed has been set
 for (int i = 0; i < seed.length; i++) {
 assertEquals(seed[i], generator.nextInt());
 }
 }

 // Test for setSeed(long) method
 @Test
 public void testSetSeedLong() {
 long seed = 1234567890L;
 generator.setSeed(seed);
 // Check that seed has been set
 assertEquals(seed, generator.nextLong());
 }

 // Test for nextBoolean() method
 @Test
 public void testNextBoolean() {
 // Test that method returns true for some inputs
 assertTrue(generator.nextBoolean());
 // Test that method returns false for some inputs
 assertFalse(generator.nextBoolean());
 }

 // Test for nextBytes(byte[]) method
 @Test
 public void testNextBytes() {
 byte[] bytes = new byte[10];
 generator.nextBytes(bytes);
 // Check that each byte is between 0 and 255
 for (byte b : bytes) {
 assertTrue(Precision.isClose(b, (byte) 0, 1));
 assertTrue(Precision.isClose(b, (byte) 255, 1));
 }
 }

 // Test for nextDouble() method
 @Test
 public void testNextDouble() {
 // Test that method returns a number between 0 and 1
 double number = generator.nextDouble();
 assertTrue(number >= 0);
 assertTrue(number <= 1);
 // Test that method returns a different number for each call
 double previous = generator.nextDouble();
 double current = generator.nextDouble();
 assertNotSame(previous, current);
 }

 // Test for nextFloat() method
 @Test
 public void testNextFloat() {
 // Test that method returns a number between -1 and 1
 double number = generator.nextFloat();
 assertTrue(Precision.isClose(number, 0, 1));
 assertTrue(Precision.isClose(number, -1, 2));
 // Test that method returns a different number for each call
 double previous = generator.nextFloat();
 double current = generator.nextFloat();
 assertNotSame(previous, current);
 }

 // Test for nextGaussian() method
 @Test
 public void testNextGaussian() {
 // Test that method returns a number that follows a normal distribution
 double mean = 0;
 double stdDev = 1;
 int numberOfTests = 10000;
 double sum = 0;
 for (int i = 0; i < numberOfTests; i++) {
 double number = generator.nextGaussian();
 sum += number * number;
 }
 double average = sum / numberOfTests;
 assertTrue(Precision.isClose(average, mean, stdDev));
 }

 // Test for nextInt() method
 @Test
 public void testNextInt() {
 // Test that method returns a number between 0 and Integer.MAX_VALUE
 int number = generator.nextInt();
 assertTrue(number >= 0);
 assertTrue(number <= Integer.MAX_VALUE);
 }

 // Test for nextInt(int) method
 @Test
 public void testNextIntInt() {
 // Test that method returns a number between 0 and n (inclusive)
 int n = 10;
 int number = generator.nextInt(n);
 assertTrue(number >= 0);
 assertTrue(number <= n);
 }

 // Test for nextInt(int) method with negative n
 @Test(expected = IllegalArgumentException.class)
 public void testNextIntIntNegativeN() {
 generator.nextInt(-5);
 }

 // Test for nextLong() method
 @Test
 public void testNextLong() {
 // Test that method returns a number between -Long.MAX_VALUE and Long.MAX_VALUE (inclusive)
 long number = generator.nextLong();
 assertTrue(Precision.isClose(number, -Long.MAX_VALUE, Long.MAX_VALUE));
 }

 // Test for clear() method
 @Test
 public void testClear() {
 // Test that method clears the cache
 generator.nextGaussian();
 generator.clear();
 assertFalse(Double.isNaN(generator.nextGaussian()));
 }

}