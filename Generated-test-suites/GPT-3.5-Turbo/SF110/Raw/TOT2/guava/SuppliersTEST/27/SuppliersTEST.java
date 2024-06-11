import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.Suppliers;

public class SuppliersTest {
    
    @Test
    public void testCompose() {
        // Typical use case
        Suppliers.compose(Function.identity(), Suppliers.ofInstance("Test"));
        
        // Edge case: function and supplier are null
        assertThrows(NullPointerException.class, () -> Suppliers.compose(null, null));
        
        // Error scenario: function is null
        assertThrows(NullPointerException.class, () -> Suppliers.compose(null, Suppliers.ofInstance("Test")));
        
        // Error scenario: supplier is null
        assertThrows(NullPointerException.class, () -> Suppliers.compose(Function.identity(), null));
    }
    
    @Test
    public void testMemoize() {
        // Typical use case
        Suppliers.memoize(Suppliers.ofInstance("Test"));
        
        // Edge case: memoize the same supplier twice
        Suppliers.memoize(Suppliers.memoize(Suppliers.ofInstance("Test")));
        
        // Error scenario: supplier is null
        assertThrows(NullPointerException.class, () -> Suppliers.memoize(null));
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        // Typical use case
        Suppliers.memoizeWithExpiration(Suppliers.ofInstance("Test"), 1, TimeUnit.SECONDS);
        
        // Edge case: expiration duration is 0
        assertThrows(IllegalArgumentException.class, () -> Suppliers.memoizeWithExpiration(Suppliers.ofInstance("Test"), 0, TimeUnit.SECONDS));
        
        // Error scenario: supplier is null
        assertThrows(NullPointerException.class, () -> Suppliers.memoizeWithExpiration(null, 1, TimeUnit.SECONDS));
    }
    
    // More test cases for other methods
    
}