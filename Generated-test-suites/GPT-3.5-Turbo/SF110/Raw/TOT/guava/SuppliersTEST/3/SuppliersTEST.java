import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    // Test cases for the compose method
    @Test
    public void testCompose_typicalCase() {
        // Given
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        
        // When
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        
        // Then
        assertNotNull(composedSupplier);
        assertEquals(new Integer(5), composedSupplier.get());
    }
    
    @Test(expected = NullPointerException.class)
    public void testCompose_nullFunction() {
        // Given
        Function<String, Integer> function = null;
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        
        // When
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        
        // Then
        // Expect NullPointerException
    }
    
    @Test(expected = NullPointerException.class)
    public void testCompose_nullSupplier() {
        // Given
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        Supplier<String> supplier = null;
        
        // When
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        
        // Then
        // Expect NullPointerException
    }
    
    // Test cases for the memoize method
    @Test
    public void testMemoize_typicalCase() {
        // Given
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        
        // When
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        
        // Then
        assertNotNull(memoizedSupplier);
        assertEquals("Hello", memoizedSupplier.get());
    }
    
    @Test(expected = NullPointerException.class)
    public void testMemoize_nullSupplier() {
        // Given
        Supplier<String> delegate = null;
        
        // When
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        
        // Then
        // Expect NullPointerException
    }
    
    // Test cases for the memoizeWithExpiration method
    @Test
    public void testMemoizeWithExpiration_typicalCase() throws InterruptedException {
        // Given
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        long duration = 1000L;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        // When
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);

        // Then
        assertNotNull(memoizedSupplier);
        assertEquals("Hello", memoizedSupplier.get());
        Thread.sleep(2000); // Wait for expiration
        assertNotNull(memoizedSupplier.get());
    }
    
    @Test(expected = NullPointerException.class)
    public void testMemoizeWithExpiration_nullSupplier() {
        // Given
        Supplier<String> delegate = null;
        long duration = 1000L;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        // When
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);

        // Then
        // Expect NullPointerException
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithExpiration_invalidDuration() {
        // Given
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        long duration = -1000L;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        // When
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);

        // Then
        // Expect IllegalArgumentException
    }
    
    // Test cases for the ofInstance method
    @Test
    public void testOfInstance_typicalCase() {
        // Given
        String instance = "Hello";
        
        // When
        Supplier<String> supplier = Suppliers.ofInstance(instance);
        
        // Then
        assertNotNull(supplier);
        assertEquals("Hello", supplier.get());
    }
    
    // Test cases for the synchronizedSupplier method
    @Test
    public void testSynchronizedSupplier_typicalCase() {
        // Given
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        
        // When
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        
        // Then
        assertNotNull(synchronizedSupplier);
        assertEquals("Hello", synchronizedSupplier.get());
    }
    
    @Test(expected = NullPointerException.class)
    public void testSynchronizedSupplier_nullSupplier() {
        // Given
        Supplier<String> delegate = null;
        
        // When
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        
        // Then
        // Expect NullPointerException
    }
    
    // Test case for the supplierFunction method
    @Test
    public void testSupplierFunction_typicalCase() {
        // Given
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        
        // When
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        String result = supplierFunction.apply(supplier);
        
        // Then
        assertEquals("Hello", result);
    }
}