import org.junit.Assert;
import org.junit.Test;

public class BitsStreamGeneratorTest {

  private BitsStreamGenerator generator;

  @Test
  public void testSetSeedInt() {
    generator = new MockBitsStreamGenerator();
    generator.setSeed(12345);
    Assert.assertEquals(12345, ((MockBitsStreamGenerator) generator).getSeed());
  }

  @Test
  public void testSetSeedIntArray() {
    generator = new MockBitsStreamGenerator();
    generator.setSeed(new int[] { 1, 2, 3 });
    Assert.assertArrayEquals(new int[] { 1, 2, 3 }, ((MockBitsStreamGenerator) generator).getSeed());
  }

  @Test
  public void testSetSeedLong() {
    generator = new MockBitsStreamGenerator();
    generator.setSeed(1234567890L);
    Assert.assertEquals(1234567890L, ((MockBitsStreamGenerator) generator).getSeed());
  }

  @Test
  public void testNextBoolean() {
    generator = new MockBitsStreamGenerator();
    Assert.assertTrue(generator.nextBoolean());
    Assert.assertFalse(generator.nextBoolean());
  }

  @Test
  public void testNextBytes() {
    generator = new MockBitsStreamGenerator();
    byte[] bytes1 = new byte[10];
    byte[] bytes2 = new byte[20];
    generator.nextBytes(bytes1);
    generator.nextBytes(bytes2);
    Assert.assertNotEquals(bytes1, bytes2);
  }

  @Test
  public void testNextDouble() {
    generator = new MockBitsStreamGenerator();
    double value = generator.nextDouble();
    Assert.assertTrue(value >= 0.0 && value < 1.0);
  }

  @Test
  public void testNextFloat() {
    generator = new MockBitsStreamGenerator();
    float value = generator.nextFloat();
    Assert.assertTrue(value >= 0.0 && value < 1.0);
  }

  @Test
  public void testNextGaussian() {
    generator = new MockBitsStreamGenerator();
    double value = generator.nextGaussian();
    Assert.assertTrue(value >= Double.NEGATIVE_INFINITY && value <= Double.POSITIVE_INFINITY);
  }

  @Test
  public void testNextInt() {
    generator = new MockBitsStreamGenerator();
    int value = generator.nextInt();
    Assert.assertEquals(32, Integer.bitCount(value));
  }

  @Test
  public void testNextIntWithBound() {
    generator = new MockBitsStreamGenerator();
    int value = generator.nextInt(100);
    Assert.assertTrue(value >= 0 && value < 100);
  }

  @Test(expected = NotStrictlyPositiveException.class)
  public void testNextIntWithBoundZero() {
    generator = new MockBitsStreamGenerator();
    generator.nextInt(0);
  }

  @Test
  public void testNextLong() {
    generator = new MockBitsStreamGenerator();
    long value = generator.nextLong();
    Assert.assertEquals(64, Long.bitCount(value));
  }

  private static class MockBitsStreamGenerator extends BitsStreamGenerator {

    private int seed;
    private int[] intSeed;

    @Override
    public void setSeed(int seed) {
      this.seed = seed;
    }

    @Override
    public void setSeed(int[] seed) {
      this.intSeed = seed;
    }

    @Override
    public void setSeed(long seed) {
      this.seed = (int) seed;
    }

    @Override
    protected int next(int bits) {
      return bits;
    }

    public int getSeed() {
      return seed;
    }

    public int[] getSeedArray() {
      return intSeed;
    }
  }

}