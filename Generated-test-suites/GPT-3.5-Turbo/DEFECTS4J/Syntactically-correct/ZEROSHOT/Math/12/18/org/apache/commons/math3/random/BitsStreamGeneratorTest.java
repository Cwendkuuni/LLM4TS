import org.junit.Test;
import org.junit.Assert;

public class BitsStreamGeneratorTest {

  @Test
  public void testNextBoolean() {
    BitsStreamGenerator generator = new BitsStreamGenerator() {
      @Override
      protected int next(int bits) {
        return 1; // always return true for testing
      }
    };

    boolean result = generator.nextBoolean();
    Assert.assertTrue(result);
  }

  @Test
  public void testNextBytes() {
    BitsStreamGenerator generator = new BitsStreamGenerator() {
      @Override
      protected int next(int bits) {
        return 0; // always return 0 for testing
      }
    };

    byte[] bytes = new byte[4];
    generator.nextBytes(bytes);
    byte[] expected = new byte[]{0, 0, 0, 0};
    Assert.assertArrayEquals(expected, bytes);
  }

  @Test
  public void testNextDouble() {
    BitsStreamGenerator generator = new BitsStreamGenerator() {
      @Override
      protected int next(int bits) {
        return 1; // always return 1 for testing
      }
    };

    double result = generator.nextDouble();
    Assert.assertEquals(2.220446049250313e-16d, result, 0.0);
  }

  @Test
  public void testNextFloat() {
    BitsStreamGenerator generator = new BitsStreamGenerator() {
      @Override
      protected int next(int bits) {
        return 1; // always return 1 for testing
      }
    };

    float result = generator.nextFloat();
    Assert.assertEquals(1.1920929E-7f, result, 0.0f);
  }

  @Test
  public void testNextGaussian() {
    BitsStreamGenerator generator = new BitsStreamGenerator() {
      private boolean first = true;

      @Override
      protected int next(int bits) {
        return 1; // always return 1 for testing
      }

      @Override
      public double nextDouble() {
        if (first) {
          first = false;
          return 0.5; // first call, return 0.5
        } else {
          return 0.6; // second call, return 0.6
        }
      }
    };

    double result1 = generator.nextGaussian();
    double result2 = generator.nextGaussian();
    Assert.assertEquals(-0.6744897501960817d, result1, 0.0);
    Assert.assertEquals(-0.434322847478811d, result2, 0.0);
  }

  @Test
  public void testNextInt() {
    BitsStreamGenerator generator = new BitsStreamGenerator() {
      @Override
      protected int next(int bits) {
        return 123; // always return 123 for testing
      }
    };

    int result = generator.nextInt();
    Assert.assertEquals(123, result);
  }

  @Test
  public void testNextIntPositive() {
    BitsStreamGenerator generator = new BitsStreamGenerator() {
      @Override
      protected int next(int bits) {
        return 123; // always return 123 for testing
      }
    };

    int result = generator.nextInt(5);
    Assert.assertEquals(3, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNextIntZero() {
    BitsStreamGenerator generator = new BitsStreamGenerator() {
      @Override
      protected int next(int bits) {
        return 123; // always return 123 for testing
      }
    };

    generator.nextInt(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNextIntNegative() {
    BitsStreamGenerator generator = new BitsStreamGenerator() {
      @Override
      protected int next(int bits) {
        return 123; // always return 123 for testing
      }
    };

    generator.nextInt(-5);
  }

  @Test
  public void testNextLong() {
    BitsStreamGenerator generator = new BitsStreamGenerator() {
      @Override
      protected int next(int bits) {
        return 123; // always return 123 for testing
      }
    };

    long result = generator.nextLong();
    Assert.assertEquals(5336233431643106419L, result);
  }

  @Test
  public void testClear() {
    BitsStreamGenerator generator = new BitsStreamGenerator() {
      @Override
      protected int next(int bits) {
        return 123; // always return 123 for testing
      }
    };

    generator.clear();
    double result = generator.nextGaussian();
    Assert.assertEquals(-0.6744897501960817d, result, 0.0);
  }
}