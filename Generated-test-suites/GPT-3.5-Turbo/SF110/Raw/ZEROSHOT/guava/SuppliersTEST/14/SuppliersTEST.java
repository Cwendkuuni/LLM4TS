import org.junit.*;
import static org.junit.Assert.*;

public class SuppliersTest {
    
    @Test
    public void testCompose() {
        // Test case for compose() method
        
        // Test when function and supplier are not null
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return String.valueOf(input);
            }
        };
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<String> result = Suppliers.compose(function, supplier);
        assertNotNull(result);
        assertEquals("5", result.get());
        
        // Test when function is null
        function = null;
        try {
            result = Suppliers.compose(function, supplier);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            // expected
        }
        
        // Test when supplier is null
        function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return String.valueOf(input);
            }
        };
        supplier = null;
        try {
            result = Suppliers.compose(function, supplier);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }
    
    @Test
    public void testMemoize() {
        // Test case for memoize() method
        
        // Test when delegate is not an instance of MemoizingSupplier
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<String> result = Suppliers.memoize(delegate);
        assertNotNull(result);
        assertEquals("Hello", result.get());
        
        // Test when delegate is an instance of MemoizingSupplier
        delegate = result; // reuse the previous result
        result = Suppliers.memoize(delegate);
        assertNotNull(result);
        assertEquals("Hello", result.get());
        
        // Test when delegate is null
        delegate = null;
        try {
            result = Suppliers.memoize(delegate);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        // Test case for memoizeWithExpiration() method
        
        // Test when delegate, duration and unit are valid
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        long duration = 1L;
        TimeUnit unit = TimeUnit.SECONDS;
        Supplier<String> result = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        assertNotNull(result);
        assertEquals("Hello", result.get());
        
        // Test when duration is 0L
        delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        duration = 0L;
        try {
            result = Suppliers.memoizeWithExpiration(delegate, duration, unit);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // expected
        }
        
        // Test when delegate is null
        delegate = null;
        duration = 1L;
        try {
            result = Suppliers.memoizeWithExpiration(delegate, duration, unit);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            // expected
        }
        
        // Test when unit is null
        delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        duration = 1L;
        unit = null;
        try {
            result = Suppliers.memoizeWithExpiration(delegate, duration, unit);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }
    
    @Test
    public void testOfInstance() {
        // Test case for ofInstance() method
        
        // Test when instance is not null
        String instance = "Hello";
        Supplier<String> result = Suppliers.ofInstance(instance);
        assertNotNull(result);
        assertEquals("Hello", result.get());
        
        // Test when instance is null
        instance = null;
        result = Suppliers.ofInstance(instance);
        assertNull(result.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Test case for synchronizedSupplier() method
        
        // Test when delegate is not null
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<String> result = Suppliers.synchronizedSupplier(delegate);
        assertNotNull(result);
        assertEquals("Hello", result.get());
        
        // Test when delegate is null
        delegate = null;
        try {
            result = Suppliers.synchronizedSupplier(delegate);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            // expected
        }
    }
    
    @Test
    public void testSupplierFunction() {
        // Test case for supplierFunction() method
        
        Function<Supplier<Integer>, Integer> result = Suppliers.supplierFunction();
        assertNotNull(result);
        
        // Test applying the function
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        int value = result.apply(supplier);
        assertEquals(5, value);
    }
}