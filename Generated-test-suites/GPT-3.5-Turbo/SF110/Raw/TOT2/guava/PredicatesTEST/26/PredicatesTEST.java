import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        Predicate<String> predicate = Predicates.alwaysTrue();
        assertTrue(predicate.apply("test"));
    }

    @Test
    public void testAlwaysFalse() {
        Predicate<Integer> predicate = Predicates.alwaysFalse();
        assertFalse(predicate.apply(5));
    }

    @Test
    public void testIsNull() {
        Predicate<Object> predicate = Predicates.isNull();
        assertTrue(predicate.apply(null));
    }

    @Test
    public void testNotNull() {
        Predicate<String> predicate = Predicates.notNull();
        assertTrue(predicate.apply("not null"));
    }

    // Additional test cases for other methods can be added here...

}