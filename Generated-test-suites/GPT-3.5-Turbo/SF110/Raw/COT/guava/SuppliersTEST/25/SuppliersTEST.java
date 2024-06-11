
import static org.junit.Assert.*;
import org.junit.Test;

public class SuppliersTest {

    // Test compose() method
    @Test
    public void testCompose() {
        // Test with valid function and supplier
        Function<String, Integer> function1 = String::length;
        Supplier<String> supplier1 = () -> "example";
        Supplier<Integer> composedSupplier1 = Suppliers.compose(function1, supplier1);
        assertEquals((Integer) 7, composedSupplier1.get());

        // Test with null function
        Supplier<String> supplier2 = () -> "test";
        Supplier<Integer> composedSupplier2 = Suppliers.compose(null, supplier2);
        assertNull(composedSupplier2);
    }
    
    // Test memoize() method
    @Test
    public void testMemoize() {
        // Test with non-null delegate
        Supplier<String> delegate1 = () -> "example";
        Supplier<String> memoizedSupplier1 = Suppliers.memoize(delegate1);
        assertEquals("example", memoizedSupplier1.get());

        // Test with null delegate
        Supplier<String> delegate2 = null;
        Supplier<String> memoizedSupplier2 = Suppliers.memoize(delegate2);
        assertNull(memoizedSupplier2);
    }
    
    // Test memoizeWithExpiration() method
    @Test
    public void testMemoizeWithExpiration() {
        // Test with valid delegate, duration and unit
        Supplier<String> delegate1 = () -> "example";
        long duration1 = 100;
        TimeUnit unit1 = TimeUnit.MILLISECONDS;
        Supplier<String> memoizedSupplier1 = Suppliers.memoizeWithExpiration(delegate1, duration1, unit1);
        assertEquals("example", memoizedSupplier1.get());

        // Test with invalid duration
        Supplier<String> delegate2 = () -> "test";
        long duration2 = -1;
        TimeUnit unit2 = TimeUnit.MILLISECONDS;
        Supplier<String> memoizedSupplier2 = Suppliers.memoizeWithExpiration(delegate2, duration2, unit2);
        assertNull(memoizedSupplier2);
    }

    // Test ofInstance() method
    @Test
    public void testOfInstance() {
        // Test with non-null instance
        String instance1 = "example";
        Supplier<String> instanceSupplier1 = Suppliers.ofInstance(instance1);
        assertEquals("example", instanceSupplier1.get());

        // Test with null instance
        String instance2 = null;
        Supplier<String> instanceSupplier2 = Suppliers.ofInstance(instance2);
        assertNull(instanceSupplier2.get());
    }

    // Test synchronizedSupplier() method
    @Test
    public void testSynchronizedSupplier() {
        // Test with non-null delegate
        Supplier<String> delegate1 = () -> "example";
        Supplier<String> synchronizedSupplier1 = Suppliers.synchronizedSupplier(delegate1);
        assertEquals("example", synchronizedSupplier1.get());

        // Test with null delegate
        Supplier<String> delegate2 = null;
        Supplier<String> synchronizedSupplier2 = Suppliers.synchronizedSupplier(delegate2);
        assertNull(synchronizedSupplier2);
    }

    // Test supplierFunction() method
    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier1 = () -> "example";
        Function<Supplier<String>, String> function1 = Suppliers.supplierFunction();
        assertEquals("example", function1.apply(supplier1));

        Supplier<String> supplier2 = null;
        Function<Supplier<String>, String> function2 = Suppliers.supplierFunction();
        assertNull(function2.apply(supplier2));
    }
}
