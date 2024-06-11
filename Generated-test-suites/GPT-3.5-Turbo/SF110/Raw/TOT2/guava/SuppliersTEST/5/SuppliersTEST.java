import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Test typical scenario
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        Supplier<String> composedSupplier = Suppliers.compose(Object::toString, supplier);
        assertEquals("5", composedSupplier.get());
        
        // Test edge case where input is null
        Supplier<Integer> nullSupplier = Suppliers.ofInstance(null);
        Supplier<String> composedNullSupplier = Suppliers.compose(Object::toString, nullSupplier);
        assertNull(composedNullSupplier.get());
        
        // Test exception handling
        try {
            Suppliers.compose(null, supplier);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMemoize() {
        // Test typical scenario
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(Integer.valueOf(5), memoizedSupplier.get());
        
        // Test edge case where input is null
        Supplier<Integer> nullSupplier = Suppliers.ofInstance(null);
        Supplier<Integer> memoizedNullSupplier = Suppliers.memoize(nullSupplier);
        assertNull(memoizedNullSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Test typical scenario
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        Supplier<Integer> expiringSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        assertEquals(Integer.valueOf(5), expiringSupplier.get());
        
        // Test edge case where expiration is 0
        Supplier<Integer> zeroSupplier = Suppliers.ofInstance(5);
        Supplier<Integer> expiringZeroSupplier = Suppliers.memoizeWithExpiration(zeroSupplier, 0, TimeUnit.SECONDS);
        assertEquals(Integer.valueOf(5), expiringZeroSupplier.get());
        
        // Test exception handling
        try {
            Suppliers.memoizeWithExpiration(supplier, -1, TimeUnit.SECONDS);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Additional test cases for other methods should be added here

}