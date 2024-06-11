import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class BitsStreamGeneratorTest {
    @Test
    public void testNextBoolean() {
        // Create an instance of BitsStreamGenerator
        BitsStreamGenerator generator = new BitsStreamGenerator();

        // Test basic functionality
        assertTrue(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        assertTrue(!generator.nextBoolean());
        assertTrue(!generator.nextBoolean());

        // Test edge cases and exceptions
        generator.setSeed(1); // Seed should not affect boolean generation
        for (int i = 0; i < 1000; i++) {
            assertTrue(generator.nextBoolean() || generator.nextBoolean());
        }
    }
}