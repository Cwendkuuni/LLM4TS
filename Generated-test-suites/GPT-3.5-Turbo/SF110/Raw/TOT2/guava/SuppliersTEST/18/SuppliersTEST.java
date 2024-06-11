import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.TimeUnit;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Typical use case
        Supplier<String> supplier = Suppliers.ofInstance("input");
        Supplier<Integer> composedSupplier = Suppliers.compose(String::length, supplier);
        assertEquals(Integer.valueOf(5), composedSupplier.get());

        // Edge case: Null input
        try {
            Suppliers.compose(null, supplier);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMemoize() {
        // Typical use case
        Supplier<String> supplier = Suppliers.ofInstance("input");
        Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals("input", memoizedSupplier.get());
        assertEquals("input", memoizedSupplier.get()); // Should return cached value

        // Edge case: Null input
        try {
            Suppliers.memoize(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Typical use case
        Supplier<String> supplier = Suppliers.ofInstance("input");
        Supplier<String> expiringMemoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        assertEquals("input", expiringMemoizedSupplier.get());
        Thread.sleep(2000); // Wait for expiration
        assertNotEquals("input", expiringMemoizedSupplier.get()); // Should not return cached value

        // Edge case: Negative duration
        try {
            Suppliers.memoizeWithExpiration(supplier, -1, TimeUnit.SECONDS);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOfInstance() {
        // Typical use case
        Integer instance = 123;
        Supplier<Integer> instanceSupplier = Suppliers.ofInstance(instance);
        assertEquals(Integer.valueOf(123), instanceSupplier.get());

        // Edge case: Null input
        Supplier<Object> nullInstanceSupplier = Suppliers.ofInstance(null);
        assertNull(nullInstanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        // Typical use case
        Supplier<Integer> supplier = Suppliers.ofInstance(123);
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertEquals(Integer.valueOf(123), synchronizedSupplier.get());

        // Edge case: Null input
        try {
            Suppliers.synchronizedSupplier(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testSupplierFunction() {
        // Typical use case
        Supplier<String> supplier = Suppliers.ofInstance("input");
        String output = Suppliers.supplierFunction().apply(supplier);
        assertEquals("input", output);
    }

}