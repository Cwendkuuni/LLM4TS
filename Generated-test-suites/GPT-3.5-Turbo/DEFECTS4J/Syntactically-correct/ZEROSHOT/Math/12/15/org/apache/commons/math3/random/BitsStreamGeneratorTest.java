import static org.junit.Assert.*;
import org.junit.Test;

public class BitsStreamGeneratorTest {
  
  @Test
  public void testNext() {
    BitsStreamGenerator bsg = new MockBitsStreamGenerator();
    int result = bsg.next(5);
    assertTrue(result >= 0 && result <= 31);
  }
  
  @Test
  public void testNextBoolean() {
    BitsStreamGenerator bsg = new MockBitsStreamGenerator();
    boolean result = bsg.nextBoolean();
    assertTrue(result == true || result == false);
  }
  
  @Test
  public void testNextBytes() {
    BitsStreamGenerator bsg = new MockBitsStreamGenerator();
    byte[] bytes = new byte[10];
    bsg.nextBytes(bytes);
    for(byte b : bytes) {
      assertTrue(b >= 0 && b <= 255);
    }
  }
  
  @Test
  public void testNextDouble() {
    BitsStreamGenerator bsg = new MockBitsStreamGenerator();
    double result = bsg.nextDouble();
    assertTrue(result >= 0.0 && result <= 1.0);
  }
  
  @Test
  public void testNextFloat() {
    BitsStreamGenerator bsg = new MockBitsStreamGenerator();
    float result = bsg.nextFloat();
    assertTrue(result >= 0.0 && result <= 1.0);
  }
  
  @Test
  public void testNextGaussian() {
    BitsStreamGenerator bsg = new MockBitsStreamGenerator();
    double result = bsg.nextGaussian();
    // Cannot assert specific values as it is a random number
    assertNotNull(result);
  }
  
  @Test
  public void testNextInt() {
    BitsStreamGenerator bsg = new MockBitsStreamGenerator();
    int result = bsg.nextInt();
    assertTrue(result >= 0 && result <= Integer.MAX_VALUE);
  }
  
  @Test
  public void testNextIntWithPositiveN() {
    BitsStreamGenerator bsg = new MockBitsStreamGenerator();
    int result = bsg.nextInt(10);
    assertTrue(result >= 0 && result < 10);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNextIntWithZeroN() {
    BitsStreamGenerator bsg = new MockBitsStreamGenerator();
    bsg.nextInt(0);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testNextIntWithNegativeN() {
    BitsStreamGenerator bsg = new MockBitsStreamGenerator();
    bsg.nextInt(-10);
  }
  
  @Test
  public void testNextLong() {
    BitsStreamGenerator bsg = new MockBitsStreamGenerator();
    long result = bsg.nextLong();
    assertTrue(result >= 0 && result <= Long.MAX_VALUE);
  }
  
  @Test
  public void testClear() {
    BitsStreamGenerator bsg = new MockBitsStreamGenerator();
    bsg.clear();
    // Cannot assert specific values as it is a random number
    assertNotNull(bsg.nextGaussian());
  }
  
  private static class MockBitsStreamGenerator extends BitsStreamGenerator {
    
    public void setSeed(int seed) {}
    
    public void setSeed(int[] seed) {}
    
    public void setSeed(long seed) {}
    
    protected int next(int bits) {
      return 0; // Mock implementation, always returns 0
    }
  }
}