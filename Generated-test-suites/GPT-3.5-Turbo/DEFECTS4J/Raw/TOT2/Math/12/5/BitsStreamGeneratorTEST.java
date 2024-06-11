import static org.junit.Assert.*;
import org.junit.Test;

public class BitsStreamGeneratorTest {
  
  @Test
  public void testNextBoolean() {
      // Test when next(1) returns 0
      // Expected value should be false
      assertFalse(generator.nextBoolean());
      
      // Test when next(1) returns 1
      // Expected value should be true
      assertTrue(generator.nextBoolean());
  }
  
  @Test
  public void testNextBytes() {
      byte[] bytes = new byte[4];
      
      // Test with bytes array of size 4
      generator.nextBytes(bytes);
      
      // Check if bytes are not all zeros
      assertTrue(bytes[0] != 0 || bytes[1] != 0 || bytes[2] != 0 || bytes[3] != 0);
  }
  
  @Test
  public void testNextDouble() {
      double value = generator.nextDouble();
      
      // Check if value is between 0 and 1
      assertTrue(value >= 0.0 && value < 1.0);
  }
  
  @Test
  public void testNextFloat() {
      float value = generator.nextFloat();
      
      // Check if value is between 0 and 1
      assertTrue(value >= 0.0 && value < 1.0);
  }
  
  @Test
  public void testNextGaussian() {
      double value = generator.nextGaussian();
      
      // Check if value is a valid double
      assertTrue(!Double.isNaN(value));
  }
  
  @Test
  public void testNextInt() {
      int value = generator.nextInt();
      
      // Check if value is an integer
      assertTrue(value % 1 == 0);
  }
  
  @Test
  public void testNextIntWithArgument() {
      int value = generator.nextInt(10);
      
      // Check if value is between 0 and 9
      assertTrue(value >= 0 && value < 10);
      
      // Test for negative argument
      try {
          generator.nextInt(-5);
          fail("Expected NotStrictlyPositiveException");
      } catch (NotStrictlyPositiveException e) {
          // Exception expected
      }
  }
  
  @Test
  public void testNextLong() {
      long value = generator.nextLong();
      
      // Check if value is a long integer
      assertTrue(value % 1 == 0);
  }
  
}