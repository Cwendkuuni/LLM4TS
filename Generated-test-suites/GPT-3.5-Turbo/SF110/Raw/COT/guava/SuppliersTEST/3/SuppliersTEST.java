
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import com.google.common.base.Supplier;
import com.google.common.base.Function;

public class SuppliersTest {
    // Test compose method with valid arguments
    @Test
    public void testCompose_validArguments() {
        Supplier<Integer> supplier1 = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(@Nullable Integer input) {
                return String.valueOf(input);
            }
        };
        
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier1);
        assertEquals("5", composedSupplier.get());
    }
    
    // Test memoize method with valid arguments
    @Test
    public void testMemoize_validArguments() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals(5, memoizedSupplier.get().intValue());
    }
    
    // Test memoizeWithExpiration method with valid arguments
    @Test
    public void testMemoizeWithExpiration_validArguments() throws InterruptedException {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals(5, memoizedSupplier.get().intValue());
        
        Thread.sleep(2000); // Sleep for 2 seconds
        
        // The value should be re-computed after expiration
        assertEquals(5, memoizedSupplier.get().intValue());
    }
    
    // Test ofInstance method with null instance
    @Test
    public void testOfInstance_nullInstance() {
        Supplier<String> supplier = Suppliers.ofInstance(null);
        assertNull(supplier.get());
    }
    
    // Test ofInstance method with non-null instance
    @Test
    public void testOfInstance_nonNullInstance() {
        Supplier<Integer> supplier = Suppliers.ofInstance(5);
        assertEquals(5, supplier.get().intValue());
    }
    
    // Test synchronizedSupplier method with valid arguments
    @Test
    public void testSynchronizedSupplier_validArguments() {
        final Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(5, synchronizedSupplier.get().intValue());
    }
    
    // Test supplierFunction method with valid arguments
    @Test
    public void testSupplierFunction_validArguments() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(5, supplierFunction.apply(supplier).intValue());
    }
}
