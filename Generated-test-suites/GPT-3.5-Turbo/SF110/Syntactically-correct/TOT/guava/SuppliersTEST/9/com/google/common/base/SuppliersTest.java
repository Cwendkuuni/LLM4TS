import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class SuppliersTest {
    
    // Test case for compose method
    @Test
    public void testCompose() {
        // Typical use case: function and supplier are valid
        Supplier<Integer> supplier = Suppliers.compose(num -> num * 2, () -> 5);
        assertEquals(Integer.valueOf(10), supplier.get());
        
        // Edge case: function returns null
        supplier = Suppliers.compose(num -> null, () -> 5);
        assertNull(supplier.get());
        
        // Error scenario: function is null
        try {
            Suppliers.compose(null, () -> 5);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("function must not be null", e.getMessage());
        }
        
        // Error scenario: supplier is null
        try {
            Suppliers.compose(num -> num * 2, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("supplier must not be null", e.getMessage());
        }
    }
    
    // Test case for memoize method
    @Test
    public void testMemoize() {
        // Typical use case: delegate supplier returns valid value
        Supplier<Integer> delegate = () -> 5;
        Supplier<Integer> supplier = Suppliers.memoize(delegate);
        assertEquals(Integer.valueOf(5), supplier.get());
        
        // Edge case: memoizing an already memoizing supplier
        supplier = Suppliers.memoize(supplier);
        assertEquals(Integer.valueOf(5), supplier.get());
        
        // Error scenario: delegate supplier is null
        try {
            Suppliers.memoize(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("delegate must not be null", e.getMessage());
        }
    }
    
    // Test case for memoizeWithExpiration method
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        // Typical use case: delegate supplier returns valid value within time limit
        Supplier<Integer> delegate = () -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 5;
        };
        Supplier<Integer> supplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals(Integer.valueOf(5), supplier.get());
        
        // Edge case: memoizing an already memoizing supplier with longer duration
        supplier = Suppliers.memoizeWithExpiration(supplier, 2, TimeUnit.SECONDS);
        assertEquals(Integer.valueOf(5), supplier.get());
        
        // Error scenario: delegate supplier is null
        try {
            Suppliers.memoizeWithExpiration(null, 1, TimeUnit.SECONDS);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("delegate must not be null", e.getMessage());
        }
        
        // Error scenario: duration is zero
        try {
            Suppliers.memoizeWithExpiration(delegate, 0, TimeUnit.SECONDS);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("duration must be greater than zero", e.getMessage());
        }
    }
    
    // Test case for ofInstance method
    @Test
    public void testOfInstance() {
        // Typical use case: instance is valid
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        assertEquals("Hello", supplier.get());
        
        // Edge case: instance is null
        supplier = Suppliers.ofInstance(null);
        assertNull(supplier.get());
    }
    
    // Test case for synchronizedSupplier method
    @Test
    public void testSynchronizedSupplier() {
        // Typical use case: delegate supplier returns valid value
        Supplier<Integer> delegate = () -> 5;
        Supplier<Integer> supplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(Integer.valueOf(5), supplier.get());
        
        // Edge case: synchronized supplier with synchronized delegate
        supplier = Suppliers.synchronizedSupplier(supplier);
        assertEquals(Integer.valueOf(5), supplier.get());
        
        // Error scenario: delegate supplier is null
        try {
            Suppliers.synchronizedSupplier(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("delegate must not be null", e.getMessage());
        }
    }
    
    // Test case for supplierFunction method
    @Test
    public void testSupplierFunction() {
        // Typical use case
        Supplier<String> supplier = () -> "Hello";
        String result = Suppliers.supplierFunction().apply(supplier);
        assertEquals("Hello", result);
    }
}