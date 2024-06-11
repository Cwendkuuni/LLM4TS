import static org.junit.Assert.*;
import org.junit.Test;

public class PredicatesTest {
    @Test
    public void testAlwaysTrue() {
        Predicate<Object> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply(null));
        assertTrue(predicate.apply(""));
        // Add more test cases here
    }
}