
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {
    @Test
    public void testCompose() {
        // Test case for compose method
        Supplier<String> supplier = () -> "Hello";
        Function<String, Integer> function = Integer::valueOf;
        Supplier<Integer> composed = Suppliers.compose(function, supplier);
        Integer result = composed.get();
        assertEquals(Integer.valueOf(5), result);
    }
    
    @Test
    public void testMemoize() {
        // Test case for memoize method
        Supplier<String> supplier = () -> "Hello";
        Supplier<String> memoized = Suppliers.memoize(supplier);
        String result1 = memoized.get();
        String result2 = memoized.get();
        assertEquals("Hello", result1);
        assertEquals(result1, result2);
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        // Test case for memoizeWithExpiration method
        Supplier<String> supplier = () -> "Hello";
        long duration = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        Supplier<String> memoized = Suppliers.memoizeWithExpiration(supplier, duration, unit);
        String result1 = memoized.get();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result2 = memoized.get();
        assertEquals("Hello", result1);
        assertNotEquals(result1, result2);
    }
    
    @Test
    public void testOfInstance() {
        // Test case for ofInstance method
        String instance = "Hello";
        Supplier<String> supplier = Suppliers.ofInstance(instance);
        String result = supplier.get();
        assertEquals("Hello", result);
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Test case for synchronizedSupplier method
        Supplier<String> supplier = () -> "Hello";
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        String result1 = synchronizedSupplier.get();
        String result2 = synchronizedSupplier.get();
        assertEquals("Hello", result1);
        assertEquals(result1, result2);
    }
    
    @Test
    public void testSupplierFunction() {
        // Test case for supplierFunction method
        Supplier<String> supplier = () -> "Hello";
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        String result = supplierFunction.apply(supplier);
        assertEquals("Hello", result);
    }
}
