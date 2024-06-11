import static org.junit.Assert.*;
import org.junit.Test;

public class BitsStreamGeneratorTest {

    @Test
    public void testNextBoolean() {
        // Arrange
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        
        // Act
        boolean result = generator.nextBoolean();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testNextBytes() {
        // Arrange
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        byte[] bytes = new byte[10];
        
        // Act
        generator.nextBytes(bytes);
        
        // Assert
        assertNotNull(bytes);
        assertEquals(10, bytes.length);
    }

    @Test
    public void testNextDouble() {
        // Arrange
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        
        // Act
        double result = generator.nextDouble();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testNextFloat() {
        // Arrange
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        
        // Act
        float result = generator.nextFloat();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testNextGaussian() {
        // Arrange
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        
        // Act
        double result = generator.nextGaussian();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testNextInt() {
        // Arrange
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        
        // Act
        int result = generator.nextInt();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testNextIntWithArgument() {
        // Arrange
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        
        // Act
        int result = generator.nextInt(10);
        
        // Assert
        assertNotNull(result);
        assertTrue(result >= 0 && result <= 10);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNextIntWithNegativeArgument() {
        // Arrange
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        
        // Act
        generator.nextInt(-10);
        
        // Assert
        fail("Expected NotStrictlyPositiveException");
    }

    @Test
    public void testNextLong() {
        // Arrange
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        
        // Act
        long result = generator.nextLong();
        
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testClear() {
        // Arrange
        BitsStreamGenerator generator = new ConcreteBitsStreamGenerator();
        
        // Act
        generator.clear();
        
        // Assert
        assertEquals(Double.NaN, generator.nextGaussian, 0);
    }

}