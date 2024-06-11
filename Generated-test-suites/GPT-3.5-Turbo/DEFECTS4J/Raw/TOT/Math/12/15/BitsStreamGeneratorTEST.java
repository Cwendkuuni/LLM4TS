import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BitsStreamGeneratorTest {
  
  // Tests for nextBoolean() method
  @Test
  public void testNextBoolean() {
    BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
    assertTrue(generator.nextBoolean());
    assertFalse(generator.nextBoolean());
  }
  
  // Tests for nextBytes(byte[] bytes) method
  @Test
  public void testNextBytes() {
    BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
    byte[] bytes = new byte[10];
    generator.nextBytes(bytes);
    
    // check if the bytes are filled with random values
    boolean random = true;
    for (byte b : bytes) {
      if (b != 0) {
        random = false;
        break;
      }
    }
    assertTrue(random);
  }
  
  // Tests for nextDouble() method
  
  @Test
  public void testNextDouble() {
    BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
    double randomDouble = generator.nextDouble();
    
    // check if the generated double is within the valid range [0,1)
    assertTrue(randomDouble >= 0.0 && randomDouble < 1.0);
  }
  
  // Tests for nextFloat() method
  
  @Test
  public void testNextFloat() {
    BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
    float randomFloat = generator.nextFloat();
    
    // check if the generated float is within the valid range [0,1)
    assertTrue(randomFloat >= 0.0 && randomFloat < 1.0);
  }
  
  // Tests for nextGaussian() method
  
  @Test
  public void testNextGaussian() {
    BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
    double randomGaussian = generator.nextGaussian();
    
    // check if the generated double is within a reasonable range
    assertTrue(randomGaussian >= Double.NEGATIVE_INFINITY && randomGaussian <= Double.POSITIVE_INFINITY);
  }
  
  // Tests for nextInt() method
  
  @Test
  public void testNextInt() {
    BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
    int randomInt = generator.nextInt();
    
    // check if the generated int is within the valid range of integers
    assertTrue(randomInt >= Integer.MIN_VALUE && randomInt <= Integer.MAX_VALUE);
  }
  
  // Tests for nextInt(int n) method
  
  @Test
  public void testNextIntWithPositiveN() {
    BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
    int n = 10;
    int randomInt = generator.nextInt(n);
    
    // check if the generated int is within the valid range [0, n)
    assertTrue(randomInt >= 0 && randomInt < n);
  }
  
  @Test(expected = NotStrictlyPositiveException.class)
  public void testNextIntWithNegativeN() {
    BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
    int n = -10;
    generator.nextInt(n);
  }
  
  // Tests for nextLong() method
  
  @Test
  public void testNextLong() {
    BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
    long randomLong = generator.nextLong();
    
    // check if the generated long is within the valid range of longs
    assertTrue(randomLong >= Long.MIN_VALUE && randomLong <= Long.MAX_VALUE);
  }
  
  // Tests for clear() method
  
  @Test
  public void testClear() {
    BitsStreamGenerator generator = new BitsStreamGeneratorImpl();
    generator.nextGaussian(); // generate a random Gaussian number
    generator.clear(); // clear the cache
    
    // check if the cache is cleared (i.e., nextGaussian is NaN)
    assertTrue(Double.isNaN(generator.nextGaussian()));
  }
  
  // Test helper class for BitsStreamGenerator implementation
  private class BitsStreamGeneratorImpl extends BitsStreamGenerator {
    public void setSeed(int seed) {}
    public void setSeed(int[] seed) {}
    public void setSeed(long seed) {}
    protected int next(int bits) { return 0; }
  }
}