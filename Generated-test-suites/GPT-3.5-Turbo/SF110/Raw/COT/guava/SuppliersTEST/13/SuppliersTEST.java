
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {
    
    @Test
    public void testCompose() {
        // Create a function and supplier
        Function<Integer, String> function = new Function<Integer, String>() {
            public String apply(Integer input) {
                return input.toString();
            }
        };
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        
        // Compose the function and supplier
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        
        // Test the composed supplier
        assertEquals("10", composedSupplier.get());
    }
    
    @Test
    public void testMemoize() {
        // Create a supplier
        Supplier<Integer> delegate = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        
        // Memoize the supplier
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        
        // Test the memoized supplier
        assertEquals(10, memoizedSupplier.get().intValue());
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        // Create a supplier
        Supplier<Integer> delegate = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        
        // Memoize the supplier with an expiration duration of 1 second
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        
        // Test the memoized supplier
        assertEquals(10, memoizedSupplier.get().intValue());
        
        // Wait for the expiration duration
        Thread.sleep(1000);
        
        // Test the memoized supplier again
        assertEquals(10, memoizedSupplier.get().intValue());
    }
    
    @Test
    public void testOfInstance() {
        // Create an instance
        Integer instance = 10;
        
        // Create a supplier of the instance
        Supplier<Integer> supplier = Suppliers.ofInstance(instance);
        
        // Test the supplier
        assertEquals(10, supplier.get().intValue());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Create a synchronized supplier
        Supplier<Integer> delegate = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        
        // Test the synchronized supplier
        assertEquals(10, synchronizedSupplier.get().intValue());
    }
    
    @Test
    public void testSupplierFunction() {
        // Create a supplier
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        
        // Create a supplier function
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        
        // Test the supplier function
        assertEquals(10, supplierFunction.apply(supplier).intValue());
    }
}
