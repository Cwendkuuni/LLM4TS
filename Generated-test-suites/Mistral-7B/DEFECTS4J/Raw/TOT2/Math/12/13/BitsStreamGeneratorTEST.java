import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {
    @Test
    public void testNextBoolean() {
        BitsStreamGenerator generator = new BitsStreamGenerator();
        assertTrue(generator.nextBoolean());
        assertFalse(generator.nextBoolean());
        assertTrue(generator.nextBoolean());
        // ... add more test cases as needed
    }
}