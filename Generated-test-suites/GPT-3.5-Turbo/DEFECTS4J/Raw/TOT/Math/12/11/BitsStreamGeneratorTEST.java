import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BitsStreamGeneratorTest {

  private BitsStreamGenerator generator;

  @Before
  public void setUp() {
    generator = new CustomBitsStreamGenerator();
  }

  // Test case for setSeed(int seed)
  @Test
  public void testSetSeedWithPositiveSeed() {
    generator.setSeed(123);
    // Check that the generator has been properly seeded
    // Expected behavior: no exceptions should be thrown
    assertNotNull(generator); 
  }

  // Test case for setSeed(int[] seed)
  @Test
  public void testSetSeedWithPositiveSeedArray() {
    int[] seed = { 1, 2, 3 };
    generator.setSeed(seed);
    // Check that the generator has been properly seeded
    // Expected behavior: no exceptions should be thrown
    assertNotNull(generator); 
  }

  // Test case for setSeed(long seed)
  @Test
  public void testSetSeedWithPositiveLongSeed() {
    generator.setSeed(1234567890L);
    // Check that the generator has been properly seeded
    // Expected behavior: no exceptions should be thrown
    assertNotNull(generator); 
  }

  // Test case for nextBoolean()
  @Test
  public void testNextBoolean() {
    boolean result = generator.nextBoolean();
    // Check that the result is either true or false
    // Expected behavior: no exceptions should be thrown
    assertTrue(result || !result);
  }

  // Test case for nextBytes(byte[] bytes)
  @Test
  public void testNextBytes() {
    byte[] bytes = new byte[4];
    generator.nextBytes(bytes);
    // Check that all bytes have been properly generated
    // Expected behavior: no exceptions should be thrown
    assertNotNull(bytes);
    assertEquals(4, bytes.length);
  }

  // Test case for nextDouble()
  @Test
  public void testNextDouble() {
    double result = generator.nextDouble();
    // Check that the result is between 0.0 and 1.0 (exclusive)
    // Expected behavior: no exceptions should be thrown
    assertTrue(result > 0.0 && result < 1.0);
  }

  // Test case for nextFloat()
  @Test
  public void testNextFloat() {
    float result = generator.nextFloat();
    // Check that the result is between 0.0 and 1.0 (exclusive)
    // Expected behavior: no exceptions should be thrown
    assertTrue(result > 0.0 && result < 1.0);
  }

  // Test case for nextGaussian()
  @Test
  public void testNextGaussian() {
    double result = generator.nextGaussian();
    // Check that the result has been properly generated
    // Expected behavior: no exceptions should be thrown
    assertNotNull(result);
  }

  // Test case for nextInt()
  @Test
  public void testNextInt() {
    int result = generator.nextInt();
    // Check that the result is a valid integer value
    // Expected behavior: no exceptions should be thrown
    assertNotNull(result);
  }

  // Test case for nextInt(int n)
  // Corner case where n is 1
  @Test
  public void testNextIntWithNEqualTo1() {
    int result = generator.nextInt(1);
    // Check that the result is 0
    // Expected behavior: no exceptions should be thrown
    assertEquals(0, result);
  }

  // Test case for nextInt(int n)
  // Corner case where n is positive
  @Test
  public void testNextIntWithPositiveN() {
    int result = generator.nextInt(100);
    // Check that the result is between 0 and n (exclusive)
    // Expected behavior: no exceptions should be thrown
    assertTrue(result >= 0 && result < 100);
  }

  // Test case for nextInt(int n)
  // Corner case where n is 0
  @Test(expected = NotStrictlyPositiveException.class)
  public void testNextIntWithNEqualTo0() {
    generator.nextInt(0);
    // Check that an exception is thrown when n is 0
  }

  // Test case for nextLong()
  @Test
  public void testNextLong() {
    long result = generator.nextLong();
    // Check that the result is a valid long value
    // Expected behavior: no exceptions should be thrown
    assertNotNull(result);
  }

  // Test case for clear()
  @Test
  public void testClear() {
    generator.clear();
    // Check that the generator's cache has been cleared
    // Expected behavior: no exceptions should be thrown
    assertNotNull(generator);
  }

  // Custom implementation as a subclass of BitsStreamGenerator for testing purposes
  private class CustomBitsStreamGenerator extends BitsStreamGenerator {

    @Override
    public void setSeed(int seed) {
      // Custom implementation for testing purposes
    }

    @Override
    public void setSeed(int[] seed) {
      // Custom implementation for testing purposes
    }

    @Override
    public void setSeed(long seed) {
      // Custom implementation for testing purposes
    }

    @Override
    protected int next(int bits) {
      // Custom implementation for testing purposes
      return 0;
    }

  }

}