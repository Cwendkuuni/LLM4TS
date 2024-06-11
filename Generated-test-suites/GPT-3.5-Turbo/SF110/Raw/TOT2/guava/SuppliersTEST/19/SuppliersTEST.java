import static org.junit.Assert.*;
import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Test a basic scenario
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        Supplier<String> composedSupplier = Suppliers.compose(Object::toString, supplier);
        assertEquals("5", composedSupplier.get());
        
        // Test edge case where supplier is null
        try {
            Suppliers.compose(null, supplier);
            fail("Exception expected");
        } catch (NullPointerException e) {
            // Exception expected
        }
    }
    
    @Test
    public void testMemoize() {
        // Test a basic scenario
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(10, (int) memoizedSupplier.get());
        
        // Test memoization behavior
        assertEquals(10, (int) memoizedSupplier.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        // Test a basic scenario
        Supplier<Integer> supplier = Suppliers.ofInstance(15);
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        assertEquals(15, (int) memoizedSupplier.get());
        
        // Test expiration of cache
        Thread.sleep(2000); // Sleep for 2 seconds
        assertEquals(15, (int) memoizedSupplier.get()); // Should return the same value
    }
    
    @Test
    public void testOfInstance() {
        // Test a basic scenario
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        assertEquals("Hello", supplier.get());
        
        // Test edge case where instance is null
        Supplier<Object> nullSupplier = Suppliers.ofInstance(null);
        assertNull(nullSupplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Test a basic scenario
        Supplier<Integer> supplier = Suppliers.ofInstance(20);
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertEquals(20, (int) synchronizedSupplier.get());
        
        // Test synchronization behavior
        synchronized (supplier) {
            assertEquals(20, (int) synchronizedSupplier.get());
        }
    }
    
    @Test
    public void testSupplierFunction() {
        // Test a basic scenario
        Supplier<String> supplier = Suppliers.ofInstance("World");
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        assertEquals("World", supplierFunction.apply(supplier));
    }
}