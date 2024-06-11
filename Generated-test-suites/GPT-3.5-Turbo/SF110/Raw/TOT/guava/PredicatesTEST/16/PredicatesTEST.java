import org.junit.Test;
import static org.junit.Assert.*;

public class PredicatesTest {

    @Test
    public void testAlwaysTrue() {
        assertTrue(Predicates.alwaysTrue().apply(null));
    }
    
    @Test
    public void testAlwaysFalse() {
        assertFalse(Predicates.alwaysFalse().apply(null));
    }
    
    @Test
    public void testIsNull() {
        assertTrue(Predicates.isNull().apply(null));
        assertFalse(Predicates.isNull().apply("test"));
    }
    
    @Test
    public void testNotNull() {
        assertTrue(Predicates.notNull().apply("test"));
        assertFalse(Predicates.notNull().apply(null));
    }
    
    @Test
    public void testNot() {
        Predicate<String> predicate = new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.contains("test");
            }
        };
        
        Predicate<String> notPredicate = Predicates.not(predicate);
        assertFalse(notPredicate.apply("this is a test"));
        assertTrue(notPredicate.apply("this is not a test"));
    }
    
    // Add more test cases for other methods

}