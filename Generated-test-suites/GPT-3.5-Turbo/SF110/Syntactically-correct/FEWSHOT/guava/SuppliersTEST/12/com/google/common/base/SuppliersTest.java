import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SuppliersTest {
    @Test
    public void testCompose() {
        // Create the necessary test objects
        Function<Object, Integer> function = new Function<Object, Integer>() {
            @Override
            public Integer apply(Object obj) {
                return obj.hashCode();
            }
        };
        Supplier<Object> supplier = new Supplier<Object>() {
            @Override
            public Object get() {
                return new Object();
            }
        };
        
        // Test the method
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        Integer result = composedSupplier.get();
        
        // Verify the result
        assertEquals(supplier.get().hashCode(), result.intValue());
    }
    
    @Test
    public void testMemoize() {
        // Create the necessary test objects
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "hello";
            }
        };
        
        // Test the method
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        String result1 = memoizedSupplier.get();
        String result2 = memoizedSupplier.get();
        
        // Verify the result
        assertEquals("hello", result1);
        assertEquals("hello", result2);
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        // Create the necessary test objects
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "hello";
            }
        };
        
        // Test the method
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        String result1 = memoizedSupplier.get();
        try {
            Thread.sleep(2000);  // Wait for the cache to expire
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result2 = memoizedSupplier.get();
        
        // Verify the result
        assertEquals("hello", result1);
        assertEquals("hello", result2);
    }
    
    @Test
    public void testOfInstance() {
        // Create the necessary test objects
        String instance = "hello";
        
        // Test the method
        Supplier<String> instanceSupplier = Suppliers.ofInstance(instance);
        String result = instanceSupplier.get();
        
        // Verify the result
        assertEquals(instance, result);
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Create the necessary test objects
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "hello";
            }
        };
        
        // Test the method
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        String result = synchronizedSupplier.get();
        
        // Verify the result
        assertEquals("hello", result);
    }
    
    @Test
    public void testSupplierFunction() {
        // Create the necessary test objects
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "hello";
            }
        };
        
        // Test the method
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        String result = supplierFunction.apply(supplier);
        
        // Verify the result
        assertEquals("hello", result);
    }
}