import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        // Test basic functionality
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        // Test edge cases
        for (int i = 0; i < 1000; i++) {
            if (generator.nextBoolean()) {
                break;
            }
        }
        // Test exception handling
        try {
            generator.nextBoolean((short) 0);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }
    }
}