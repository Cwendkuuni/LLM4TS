import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        // typical use case
        assertTrue(generator.nextBoolean());
        // edge cases
        assertFalse(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        // exception handling
        try {
            generator.nextBoolean(-1);
            fail("Expected NotStrictlyPositiveException to be thrown.");
        } catch (NotStrictlyPositiveException e) {
            // expected exception
        }
    }
}