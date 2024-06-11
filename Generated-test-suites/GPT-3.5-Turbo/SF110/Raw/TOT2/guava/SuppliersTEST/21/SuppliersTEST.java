import static org.junit.Assert.*;
import org.junit.Test;

public class SuppliersTest {
    @Test
    public void testCompose() {
        // Typical use case
        Supplier<Integer> supplier = () -> 5;
        Supplier<String> composedSupplier = Suppliers.compose(Object::toString, supplier);
        assertEquals("5", composedSupplier.get());
        
        // Edge case: function returns null
        composedSupplier = Suppliers.compose(obj -> null, supplier);
        assertNull(composedSupplier.get());
        
        // Error scenario: null function
        try {
            Suppliers.compose(null, supplier);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
        
        // Error scenario: null supplier
        try {
            Suppliers.compose(Object::toString, null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
    
    @Test
    public void testMemoize() {
        // Typical use case
        Supplier<Integer> supplier = () -> 5;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(5, (int)memoizedSupplier.get());
        
        // Edge case: memoize twice
        memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(5, (int)memoizedSupplier.get());
        
        // Error scenario: null supplier
        try {
            Suppliers.memoize(null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        // Typical use case
        Supplier<Integer> supplier = () -> 5;
        Supplier<Integer> expiringMemoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        assertEquals(5, (int)expiringMemoizedSupplier.get());
        
        // Edge case: expiration time less than zero
        try {
            Suppliers.memoizeWithExpiration(supplier, -1, TimeUnit.SECONDS);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testOfInstance() {
        // Typical use case
        String instance = "test";
        Supplier<String> instanceSupplier = Suppliers.ofInstance(instance);
        assertEquals("test", instanceSupplier.get());
        
        // Edge case: instance is null
        Supplier<Object> nullSupplier = Suppliers.ofInstance(null);
        assertNull(nullSupplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Typical use case
        Supplier<Integer> supplier = () -> 5;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertEquals(5, (int)synchronizedSupplier.get());
        
        // Edge case: synchronized get
        synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertEquals(5, (int)synchronizedSupplier.get());
        
        // Error scenario: null supplier
        try {
            Suppliers.synchronizedSupplier(null);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
    
    @Test
    public void testSupplierFunction() {
        // Use case
        Supplier<Integer> supplier = () -> 5;
        Function<Supplier<Integer>, Integer> function = Suppliers.supplierFunction();
        assertEquals(5, (int)function.apply(supplier));
    }
}