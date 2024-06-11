import org.junit.Test;
import static org.junit.Assert.*;

public class PartialTest {
    @Test
    public void testSize() {
        Partial partial = new Partial();
        int expectedSize = 0;
        int actualSize = partial.size();
        assertEquals(expectedSize, actualSize);
    }
}