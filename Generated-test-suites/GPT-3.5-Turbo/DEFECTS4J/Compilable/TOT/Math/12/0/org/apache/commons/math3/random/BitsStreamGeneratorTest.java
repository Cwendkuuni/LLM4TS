package org.apache.commons.math3.random;
import static org.junit.Assert.*;
import org.junit.Test;

public class BitsStreamGeneratorTest {

  @Test
  public void testNextBoolean() {
    // Typical use case
    BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
    boolean result = generator.nextBoolean();
    assertTrue(result);

    // Typical use case
    generator = new ConcreteBitsStreamGenerator();
    result = generator.nextBoolean();
    assertFalse(result);
  }

  @Test
  public void testNextBytes() {
    // Typical use case
    BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
    byte[] bytes = new byte[4];
    generator.nextBytes(bytes);
    assertNotEquals(0, bytes[0]);
    assertNotEquals(0, bytes[1]);
    assertNotEquals(0, bytes[2]);
    assertNotEquals(0, bytes[3]);
  }

  @Test
  public void testNextDouble() {
    // Typical use case
    BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
    double result = generator.nextDouble();
    assertTrue(result >= 0.0 && result < 1.0);
  }

  @Test
  public void testNextFloat() {
    // Typical use case
    BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
    float result = generator.nextFloat();
    assertTrue(result >= 0.0 && result < 1.0);
  }

  @Test
  public void testNextGaussian() {
    // Typical use case
    BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
    double result = generator.nextGaussian();
    assertNotNull(result);

    // Typical use case
    generator = new ConcreteBitsStreamGenerator();
    generator.clear();
    result = generator.nextGaussian();
    assertNotNull(result);
  }

  @Test
  public void testNextInt() {
    // Typical use case
    BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
    int result = generator.nextInt();
    assertNotNull(result);

    // Typical use case
    generator = new ConcreteBitsStreamGenerator();
    result = generator.nextInt(10);
    assertTrue(result >= 0 && result < 10);

    // Edge case
    generator = new ConcreteBitsStreamGenerator();
    result = generator.nextInt(-10);
    assertNotNull(result);
  }

  @Test
  public void testNextLong() {
    // Typical use case
    BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
    long result = generator.nextLong();
    assertNotNull(result);
  }

  private class ConcreteBitsStreamGenerator extends BitsStreamGenerator {
    public void setSeed(int seed) {}
    public void setSeed(int[] seed) {}
    public void setSeed(long seed) {}
    protected int next(int bits) { return 0; }
  }
}