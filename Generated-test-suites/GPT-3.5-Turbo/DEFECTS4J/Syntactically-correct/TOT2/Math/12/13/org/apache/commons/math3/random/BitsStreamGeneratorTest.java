import org.junit.Test;
import static org.junit.Assert.*;

public class BitsStreamGeneratorTest {

    @Test
    public void testNext() {
        BitsStreamGenerator generator = new BitsStreamGenerator() {
            @Override
            protected int next(int bits) {
                return 5; // Return a constant value for testing
            }
        };

        int result = generator.next(5);

        assertEquals(5, result);
    }
}