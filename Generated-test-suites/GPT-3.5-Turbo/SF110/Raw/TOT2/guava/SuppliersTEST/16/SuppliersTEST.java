import static org.junit.Assert.*;
import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Test typical scenario
        Function<Integer, String> function = (Integer num) -> String.valueOf(num);
        Supplier<Integer> supplier = () -> 5;
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals("5", composedSupplier.get());
        
        // Test edge case with null function
        try {
            Suppliers.compose(null, supplier);
            fail("Exception not thrown for null function");
        } catch (NullPointerException e) {
            // Expected exception
        }
        
        // Test edge case with null supplier
        try {
            Suppliers.compose(function, null);
            fail("Exception not thrown for null supplier");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testMemoize() {
        // Test typical scenario
        Supplier<Integer> delegate = () -> 10;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals(10, (int) memoizedSupplier.get());
        
        // Test edge case with MemoizingSupplier instance
        Supplier<Integer> memoizingSupplier = new Suppliers.MemoizingSupplier<>(delegate);
        assertSame(memoizingSupplier, Suppliers.memoize(memoizingSupplier));
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Test typical scenario
        Supplier<Integer> delegate = () -> 20;
        Supplier<Integer> expiringMemoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals(20, (int) expiringMemoizedSupplier.get());
        
        // Test edge case with zero duration
        try {
            Suppliers.memoizeWithExpiration(delegate, 0, TimeUnit.SECONDS);
            fail("Exception not thrown for zero duration");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testOfInstance() {
        // Test typical scenario
        String instance = "test";
        Supplier<String> instanceSupplier = Suppliers.ofInstance(instance);
        assertSame(instance, instanceSupplier.get());
        
        // Test edge case with null instance
        Supplier<String> nullInstanceSupplier = Suppliers.ofInstance(null);
        assertNull(nullInstanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        // Test typical scenario
        Supplier<Integer> delegate = () -> 15;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(15, (int) synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        // Test typical scenario
        Supplier<String> supplier = () -> "test";
        String value = Suppliers.supplierFunction().apply(supplier);
        assertEquals("test", value);
        
        // Test edge case with null supplier
        try {
            Suppliers.supplierFunction().apply(null);
            fail("Exception not thrown for null supplier");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
    
    // Additional test cases can be added for equals(), hashCode(), and toString() methods of inner classes if needed

}