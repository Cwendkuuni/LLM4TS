import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Typical use case
        Function<String, Integer> function = Integer::parseInt;
        Supplier<String> supplier = () -> "123";
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals(Integer.valueOf(123), composedSupplier.get());
        
        // Edge case: empty string
        Supplier<String> emptySupplier = () -> "";
        Supplier<Integer> composedEmptySupplier = Suppliers.compose(function, emptySupplier);
        assertThrows(NumberFormatException.class, () -> composedEmptySupplier.get());
        
        // Exception handling: null values
        assertThrows(NullPointerException.class, () -> Suppliers.compose(null, supplier));
        assertThrows(NullPointerException.class, () -> Suppliers.compose(function, null));
    }

    @Test
    public void testMemoize() {
        // Typical use case
        Supplier<Integer> delegate = () -> 123;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals(Integer.valueOf(123), memoizedSupplier.get());
        
        // Edge case: repeated calls
        assertEquals(Integer.valueOf(123), memoizedSupplier.get());
        
        // Exception handling: null value
        assertThrows(NullPointerException.class, () -> Suppliers.memoize(null));
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Typical use case
        Supplier<Integer> delegate = () -> 123;
        Supplier<Integer> expiringMemoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals(Integer.valueOf(123), expiringMemoizedSupplier.get());
        
        // Edge case: expiration
        TimeUnit.SECONDS.sleep(1);
        assertNotEquals(Integer.valueOf(123), expiringMemoizedSupplier.get());
        
        // Exception handling: null value
        assertThrows(NullPointerException.class, () -> Suppliers.memoizeWithExpiration(null, 1, TimeUnit.SECONDS));
        
        // Exception handling: duration less than 0
        assertThrows(IllegalArgumentException.class, () -> Suppliers.memoizeWithExpiration(delegate, -1, TimeUnit.SECONDS));
    }

    @Test
    public void testOfInstance() {
        // Typical use case
        String instance = "Test";
        Supplier<String> instanceSupplier = Suppliers.ofInstance(instance);
        assertEquals("Test", instanceSupplier.get());
        
        // Edge case: null instance
        Supplier<String> nullInstanceSupplier = Suppliers.ofInstance(null);
        assertNull(nullInstanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        // Typical use case
        Supplier<Integer> delegate = () -> 123;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(Integer.valueOf(123), synchronizedSupplier.get());
        
        // Exception handling: null value
        assertThrows(NullPointerException.class, () -> Suppliers.synchronizedSupplier(null));
    }

    @Test
    public void testSupplierFunction() {
        // Typical use case
        SupplierFunction<Integer> supplierFunction = Suppliers.supplierFunction();
        Supplier<Integer> supplier = () -> 123;
        assertEquals(Integer.valueOf(123), supplierFunction.apply(supplier));
    }
}