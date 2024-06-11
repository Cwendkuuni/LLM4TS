
import static org.junit.Assert.*;
import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Test case to check the functionality of compose method
        // Setup
        Function<String, Integer> function = s -> s.length();
        Supplier<String> supplier = () -> "Hello";
        
        // Execution
        Supplier<Integer> result = Suppliers.compose(function, supplier);
        
        // Assertion
        assertEquals(5, result.get().intValue());
    }
    
    @Test
    public void testMemoize() {
        // Test case to check the functionality of memoize method
        // Setup
        Supplier<String> delegate = () -> "Hello";
        
        // Execution
        Supplier<String> result = Suppliers.memoize(delegate);
        
        // Assertion
        assertEquals("Hello", result.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        // Test case to check the functionality of memoizeWithExpiration method
        // Setup
        Supplier<String> delegate = () -> "Hello";
        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        
        // Execution
        Supplier<String> result = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        
        // Assertion
        assertEquals("Hello", result.get());
        Thread.sleep(2000); // Wait for cache to expire
        assertNotEquals("Hello", result.get());
    }
    
    @Test
    public void testOfInstance() {
        // Test case to check the functionality of ofInstance method
        // Setup
        String instance = "Hello";
        
        // Execution
        Supplier<String> result = Suppliers.ofInstance(instance);
        
        // Assertion
        assertEquals("Hello", result.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Test case to check the functionality of synchronizedSupplier method
        // Setup
        Supplier<String> delegate = () -> "Hello";
        
        // Execution
        Supplier<String> result = Suppliers.synchronizedSupplier(delegate);
        
        // Assertion
        assertEquals("Hello", result.get());
    }
    
    @Test
    public void testSupplierFunction() {
        // Test case to check the functionality of supplierFunction method
        // Setup
        Supplier<Integer> supplier = () -> 5;
        
        // Execution
        Function<Supplier<Integer>, Integer> result = Suppliers.supplierFunction();
        
        // Assertion
        assertEquals(5, result.apply(supplier).intValue());
    }
}
