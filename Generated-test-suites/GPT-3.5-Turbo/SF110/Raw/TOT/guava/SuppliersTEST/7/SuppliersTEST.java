import static org.junit.Assert.*;
import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Typical use case
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        int result = composedSupplier.get();
        assertEquals(5, result);
        
        // Edge case: supplier returns null
        supplier = Suppliers.ofInstance(null);
        composedSupplier = Suppliers.compose(function, supplier);
        assertNull(composedSupplier.get());
        
        // Error scenario: null function
        supplier = Suppliers.ofInstance("Hello");
        try {
            composedSupplier = Suppliers.compose(null, supplier);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
        
        // Error scenario: null supplier
        function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        try {
            composedSupplier = Suppliers.compose(function, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
    
    @Test
    public void testMemoize() {
        // Typical use case
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        String result = memoizedSupplier.get();
        assertEquals("Hello", result);
        result = memoizedSupplier.get(); // Calling it again should use the cached value
        assertEquals("Hello", result);
        
        // Edge case: delegate returns null
        delegate = new Supplier<String>() {
            @Override
            public String get() {
                return null;
            }
        };
        memoizedSupplier = Suppliers.memoize(delegate);
        assertNull(memoizedSupplier.get());
        
        // Error scenario: null delegate
        try {
            memoizedSupplier = Suppliers.memoize(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        // Typical use case
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        String result = memoizedSupplier.get();
        assertEquals("Hello", result);
        try {
            Thread.sleep(2000); // Wait for expiration
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result = memoizedSupplier.get(); // Calling it again should call the delegate again
        assertEquals("Hello", result);
        
        // Edge case: delegate returns null
        delegate = new Supplier<String>() {
            @Override
            public String get() {
                return null;
            }
        };
        memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertNull(memoizedSupplier.get());
        
        // Error scenario: null delegate
        try {
            memoizedSupplier = Suppliers.memoizeWithExpiration(null, 1, TimeUnit.SECONDS);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
        
        // Error scenario: negative duration
        delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        try {
            memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, -1, TimeUnit.SECONDS);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testOfInstance() {
        // Typical use case
        Supplier<String> instanceSupplier = Suppliers.ofInstance("Hello");
        String result = instanceSupplier.get();
        assertEquals("Hello", result);
        
        // Edge case: null instance
        instanceSupplier = Suppliers.ofInstance(null);
        assertNull(instanceSupplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Typical use case
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        String result = synchronizedSupplier.get();
        assertEquals("Hello", result);
        
        // Error scenario: null delegate
        try {
            synchronizedSupplier = Suppliers.synchronizedSupplier(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
    
    @Test
    public void testSupplierFunction() {
        // Typical use case
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        String result = supplierFunction.apply(supplier);
        assertEquals("Hello", result);
    }
}