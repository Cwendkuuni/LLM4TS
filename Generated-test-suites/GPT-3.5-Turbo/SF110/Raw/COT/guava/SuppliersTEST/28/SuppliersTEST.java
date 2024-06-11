
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Test case for compose method
        // Create a Function and a Supplier
        // Call compose method and assert the result
        Suppliers.Function<String, Integer> function = new SomeFunction();
        Suppliers.Supplier<String> supplier = new SomeSupplier<>();
        
        Suppliers.Supplier<Integer> composed = Suppliers.compose(function, supplier);
        
        Integer result = composed.get();
        
        assertEquals(result, Integer.valueOf(10));
    }
    
    @Test
    public void testMemoize() {
        // Test case for memoize method
        // Create a Supplier
        // Call memoize method and assert the result
        Suppliers.Supplier<String> delegate = new SomeSupplier<>();
        
        Suppliers.Supplier<String> memoized = Suppliers.memoize(delegate);
        
        String result1 = memoized.get();
        String result2 = memoized.get();
        
        assertEquals(result1, result2);
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        // Test case for memoizeWithExpiration method
        // Create a Supplier
        // Call memoizeWithExpiration method with duration and unit and assert the result
        Suppliers.Supplier<String> delegate = new SomeSupplier<>();
        
        Suppliers.Supplier<String> memoized = Suppliers.memoizeWithExpiration(delegate, 500, TimeUnit.MILLISECONDS);
        
        String result1 = memoized.get();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result2 = memoized.get();
        
        assertNotEquals(result1, result2);
    }
    
    @Test
    public void testOfInstance() {
        // Test case for ofInstance method
        // Create an instance
        // Call ofInstance method and assert the result
        String instance = "Test";
        
        Suppliers.Supplier<String> ofInstance = Suppliers.ofInstance(instance);
        
        String result = ofInstance.get();
        
        assertEquals(result, instance);
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Test case for synchronizedSupplier method
        // Create a Supplier
        // Call synchronizedSupplier method and assert the result
        Suppliers.Supplier<String> delegate = new SomeSupplier<>();
        
        Suppliers.Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        
        String result = synchronizedSupplier.get();
        
        assertEquals(result, "Some Supplier");
    }
    
    @Test
    public void testSupplierFunction() {
        // Test case for supplierFunction method
        // Create a Supplier
        // Call supplierFunction method and assert the result
        Suppliers.Supplier<String> supplier = new SomeSupplier<>();
        
        Suppliers.Function<Suppliers.Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        
        String result = supplierFunction.apply(supplier);
        
        assertEquals(result, "Some Supplier");
    }
    
    private class SomeFunction implements Suppliers.Function<String, Integer> {
        @Override
        public Integer apply(String input) {
            return input.length();
        }
    }
    
    private class SomeSupplier<T> implements Suppliers.Supplier<T> {
        @Override
        public T get() {
            return (T) "Some Supplier";
        }
    }
}
