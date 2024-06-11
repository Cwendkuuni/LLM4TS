import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Test typical use case
        Supplier<Integer> supplier = Suppliers.compose((Integer i) -> i * 2, () -> 3);
        assertEquals(Integer.valueOf(6), supplier.get());

        // Test edge case where input is null
        Supplier<String> stringSupplier = Suppliers.compose((String s) -> s.toUpperCase(), () -> null);
        assertNull(stringSupplier.get());
        
        // Test exception handling with null function
        try {
            Suppliers.compose(null, () -> "test").get();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
        
        // Add more test cases as needed
    }

    @Test
    public void testMemoize() {
        // Test typical use case
        Supplier<String> supplier = Suppliers.memoize(() -> "memoized");
        assertEquals("memoized", supplier.get());
        
        // Test edge case where result is null
        Supplier<Integer> intSupplier = Suppliers.memoize(() -> null);
        assertNull(intSupplier.get());
        
        // Add more test cases as needed
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Test typical use case
        Supplier<Integer> supplier = Suppliers.memoizeWithExpiration(() -> 5, 1, TimeUnit.SECONDS);
        assertEquals(Integer.valueOf(5), supplier.get());
        
        // Test edge case where duration is zero
        Supplier<String> stringSupplier = Suppliers.memoizeWithExpiration(() -> "test", 0, TimeUnit.SECONDS);
        assertEquals("test", stringSupplier.get());
        
        // Add more test cases as needed
    }

    @Test
    public void testOfInstance() {
        // Test typical use case
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        assertEquals(Integer.valueOf(10), supplier.get());
        
        // Test edge case where instance is null
        Supplier<String> stringSupplier = Suppliers.ofInstance(null);
        assertNull(stringSupplier.get());
        
        // Add more test cases as needed
    }

    @Test
    public void testSynchronizedSupplier() {
        // Test typical use case
        Supplier<Integer> supplier = Suppliers.synchronizedSupplier(() -> 7);
        assertEquals(Integer.valueOf(7), supplier.get());
        
        // Test edge case where delegate supplier is null
        Supplier<String> stringSupplier = Suppliers.synchronizedSupplier(null);
        assertNull(stringSupplier.get());
        
        // Add more test cases as needed
    }

    @Test
    public void testSupplierFunction() {
        // Test typical use case
        Supplier<String> stringSupplier = Suppliers.ofInstance("test");
        Function<Supplier<String>, String> function = Suppliers.supplierFunction();
        assertEquals("test", function.apply(stringSupplier));
        
        // Add more test cases as needed
    }
}