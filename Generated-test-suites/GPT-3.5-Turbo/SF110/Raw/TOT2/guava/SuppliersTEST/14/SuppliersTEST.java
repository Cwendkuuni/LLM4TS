import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Typical use case
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        Supplier<Integer> composed = Suppliers.compose(String::length, supplier);
        assertEquals(5, (int) composed.get());
        
        // Edge case
        supplier = Suppliers.ofInstance(null);
        composed = Suppliers.compose(String::length, supplier);
        assertNull(composed.get());
        
        // Exception handling
        try {
            Suppliers.compose(null, supplier);
            fail("Expected NullPointerException");
        } catch (NullPointerException ignored) {}
    }

    @Test
    public void testMemoize() {
        // Typical use case
        Supplier<Integer> delegate = Suppliers.ofInstance(42);
        Supplier<Integer> memoized = Suppliers.memoize(delegate);
        assertEquals(42, (int) memoized.get());
        
        // Edge case
        delegate = Suppliers.ofInstance(null);
        memoized = Suppliers.memoize(delegate);
        assertNull(memoized.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Typical use case
        Supplier<Integer> delegate = Suppliers.ofInstance(42);
        Supplier<Integer> memoized = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals(42, (int) memoized.get());
        
        // Edge case
        delegate = Suppliers.ofInstance(null);
        memoized = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertNull(memoized.get());
    }

    @Test
    public void testOfInstance() {
        // Typical use case
        Supplier<String> instanceSupplier = Suppliers.ofInstance("World");
        assertEquals("World", instanceSupplier.get());
        
        // Edge case
        instanceSupplier = Suppliers.ofInstance(null);
        assertNull(instanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        // Typical use case
        Supplier<String> delegate = Suppliers.ofInstance("Sync");
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals("Sync", synchronizedSupplier.get());
        
        // Edge case
        delegate = Suppliers.ofInstance(null);
        synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertNull(synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        // Typical use case
        Supplier<Integer> supplier = Suppliers.ofInstance(42);
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(42, (int) supplierFunction.apply(supplier));
        
        // Edge case
        supplier = Suppliers.ofInstance(null);
        assertNull(supplierFunction.apply(supplier));
    }
}