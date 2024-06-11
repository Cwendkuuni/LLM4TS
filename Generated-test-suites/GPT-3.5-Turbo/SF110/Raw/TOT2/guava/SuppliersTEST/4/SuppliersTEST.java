import static org.junit.Assert.*;
import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Typical use case
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        Supplier<String> composedSupplier = Suppliers.compose(Object::toString, supplier);
        assertEquals("5", composedSupplier.get());
        
        // Edge case
        Supplier<String> nullSupplier = Suppliers.ofInstance(null);
        Supplier<Integer> nullComposedSupplier = Suppliers.compose(Object::hashCode, nullSupplier);
        assertNull(nullComposedSupplier.get());
        
        // Exception handling
        try {
            Suppliers.compose(null, supplier);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMemoize() {
        // Typical use case
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(5, (int) memoizedSupplier.get());
        
        // Edge case
        Supplier<Integer> nullSupplier = Suppliers.ofInstance(null);
        Supplier<Integer> memoizedNullSupplier = Suppliers.memoize(nullSupplier);
        assertNull(memoizedNullSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Typical use case
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        Supplier<Integer> memoizedExpiringSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        assertEquals(5, (int) memoizedExpiringSupplier.get());
        
        // Edge case
        Supplier<Integer> nullSupplier = Suppliers.ofInstance(null);
        Supplier<Integer> expiringNullSupplier = Suppliers.memoizeWithExpiration(nullSupplier, 1, TimeUnit.SECONDS);
        assertNull(expiringNullSupplier.get());
        
        // Exception handling
        try {
            Suppliers.memoizeWithExpiration(supplier, -1, TimeUnit.SECONDS);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOfInstance() {
        // Typical use case
        Supplier<Integer> instanceSupplier = Suppliers.ofInstance(5);
        assertEquals(5, (int) instanceSupplier.get());
        
        // Edge case
        Supplier<Integer> nullSupplier = Suppliers.ofInstance(null);
        assertNull(nullSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        // Typical use case
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertEquals(5, (int) synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        // Typical use case
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        Function<Supplier<Integer>, Integer> function = Suppliers.supplierFunction();
        assertEquals(5, (int) function.apply(supplier));
    }
}