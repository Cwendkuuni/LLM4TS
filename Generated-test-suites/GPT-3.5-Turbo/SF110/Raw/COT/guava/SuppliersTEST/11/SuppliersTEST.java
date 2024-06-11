
import static org.junit.Assert.*;

import org.junit.Test;

public class SuppliersTest {
    
    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return String.valueOf(input);
            }
        };
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 10;
            }
        };
        
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        
        assertEquals("10", composedSupplier.get());
    }
    
    @Test
    public void testMemoize() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        
        assertEquals("Hello", memoizedSupplier.get());
        assertEquals("Hello", memoizedSupplier.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        
        assertEquals("Hello", memoizedSupplier.get());
        Thread.sleep(2000);
        assertEquals("Hello", memoizedSupplier.get());
    }
    
    @Test
    public void testOfInstance() {
        String instance = "Hello";
        
        Supplier<String> instanceSupplier = Suppliers.ofInstance(instance);
        
        assertEquals("Hello", instanceSupplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        
        assertEquals("Hello", synchronizedSupplier.get());
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 10;
            }
        };
        
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        
        assertEquals(10, supplierFunction.apply(supplier).intValue());
    }

}
