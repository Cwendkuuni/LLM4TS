import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import com.google.common.base.*;

public class SuppliersTest {
    
    Suppliers supplier;
    
    @Before
    public void setUp() {
        supplier = new Suppliers();
    }
    
    // Test for compose() method
    
    @Test
    public void testCompose() {
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
        
        Supplier<String> result = supplier.compose(function, supplier);
        assertEquals("10", result.get());
    }
    
    // Test for memoize() method
    
    @Test
    public void testMemoize() {
        Supplier<String> delegate = new Supplier<String>() {
            public String get() {
                return "Hello";
            }
        };
        
        Supplier<String> result = supplier.memoize(delegate);
        assertEquals("Hello", result.get());
    }
    
    // Test for memoizeWithExpiration() method
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<String> delegate = new Supplier<String>() {
            public String get() {
                return "Hello";
            }
        };
        
        Supplier<String> result = supplier.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals("Hello", result.get());
        
        Thread.sleep(2000); // Wait for 2 seconds
        
        assertNotEquals("Hello", result.get());
    }
    
    // Test for ofInstance() method
    
    @Test
    public void testOfInstance() {
        String instance = "Hello";
        
        Supplier<String> result = supplier.ofInstance(instance);
        assertEquals(instance, result.get());
    }
    
    // Test for synchronizedSupplier() method
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            public String get() {
                return "Hello";
            }
        };
        
        Supplier<String> result = supplier.synchronizedSupplier(delegate);
        assertEquals("Hello", result.get());
    }
    
    // Test for supplierFunction() method
    
    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = new Supplier<String>() {
            public String get() {
                return "Hello";
            }
        };
        
        Function<Supplier<String>, String> result = supplier.supplierFunction();
        assertEquals("Hello", result.apply(supplier));
    }
    
    // Additional Test for equals() and hashCode() methods
    
    @Test
    public void testEqualsAndHashCode() {
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
        
        Supplier<String> result1 = supplier.compose(function, supplier);
        Supplier<String> result2 = supplier.compose(function, supplier);
        
        assertEquals(result1, result2); // Check equals() method
        assertEquals(result1.hashCode(), result2.hashCode()); // Check hashCode() method
    }
    
    // Test for edge case with null instance
    
    @Test
    public void testOfInstanceWithNull() {
        Supplier<String> result = supplier.ofInstance(null);
        assertNull(result.get());
    }
    
}