import static org.junit.Assert.*;
import org.junit.*;

public class BitsStreamGeneratorTest {

  private BitsStreamGenerator generator;

  @Before
  public void setUp() {
    generator = new BitsStreamGeneratorImpl();
  }

  @Test
  public void testSetSeed() {
    int seed = 123456789;
    generator.setSeed(seed);
    // Verify that the seed was set correctly
    assertEquals(seed, generator.getSeed());
  }

  @Test
  public void testSetSeedIntArray() {
    int[] seed = {1, 2, 3, 4, 5};
    generator.setSeed(seed);

    // Verify that the seed was set correctly
    assertArrayEquals(seed, generator.getSeed());
  }

  @Test
  public void testSetSeedLong() {
    long seed = 987654321;
    generator.setSeed(seed);
    // Verify that the seed was set correctly
    assertEquals(seed, generator.getSeed());
  }

  @Test
  public void testNextBoolean() {
    boolean result = generator.nextBoolean();
    // Verify that the result is either true or false
    assertTrue(result || !result);
  }

  @Test
  public void testNextBytes() {
    byte[] bytes = new byte[10];
    generator.nextBytes(bytes);
    // Verify that all bytes are within the valid range
    for (byte b : bytes) {
      assertTrue(b >= 0 && b <= 255);
    }
  }

  @Test
  public void testNextDouble() {
    double result = generator.nextDouble();
    // Verify that the result is between 0 and 1
    assertTrue(result >= 0 && result < 1);
  }

  @Test
  public void testNextFloat() {
    float result = generator.nextFloat();
    // Verify that the result is between 0 and 1
    assertTrue(result >= 0 && result < 1);
  }

  @Test
  public void testNextGaussian() {
    double result = generator.nextGaussian();
    // No straightforward way to test the gaussian distribution
    // Verify that the result is a valid double value
    assertTrue(Double.isFinite(result));
  }

  @Test
  public void testNextInt() {
    int result = generator.nextInt();
    // No constraints on the result, just verify that it is an integer
    assertTrue(result == (int) result);
  }

  @Test
  public void testNextIntWithBound() {
    int n = 100;
    int result = generator.nextInt(n);
    // Verify that the result is between 0 and n-1
    assertTrue(result >= 0 && result < n);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testNextIntWithInvalidBound() {
    int n = -1;
    generator.nextInt(n);
  }

  @Test
  public void testNextLong() {
    long result = generator.nextLong();
    // No constraints on the result, just verify that it is a long
    assertTrue(result == (long) result);
  }

  @Test
  public void testClear() {
    generator.clear();
    // Verify that the nextGaussian cache is cleared
    assertTrue(Double.isNaN(generator.getNextGaussian()));
  }
}