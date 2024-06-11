import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.Suppliers;
import java.util.concurrent.TimeUnit;

public class SuppliersTest {
    
    // Test compose method
    @Test
    public void testCompose() {
        Function<Integer, Integer> function = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * input;
            }
        };
        
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        
        Supplier<Integer> result = Suppliers.compose(function, supplier);
        assertEquals(Integer.valueOf(25), result.get());
    }
    
    // Test memoize method
    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 10;
            }
        };
        
        Supplier<Integer> result = Suppliers.memoize(delegate);
        assertEquals(Integer.valueOf(10), result.get());
    }
    
    // Test memoizeWithExpiration method
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        
        Supplier<Integer> result = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals(Integer.valueOf(5), result.get());
    }
    
    // Test ofInstance method
    @Test
    public void testOfInstance() {
        Supplier<String> result = Suppliers.ofInstance("Hello");
        assertEquals("Hello", result.get());
    }
    
    // Test synchronizedSupplier method
    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 100;
            }
        };
        
        Supplier<Integer> result = Suppliers.synchronizedSupplier(delegate);
        assertEquals(Integer.valueOf(100), result.get());
    }
    
    // Test supplierFunction method
    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 50;
            }
        };
        
        Function<Supplier<Integer>, Integer> result = Suppliers.supplierFunction();
        assertEquals(Integer.valueOf(50), result.apply(supplier));
    }
}