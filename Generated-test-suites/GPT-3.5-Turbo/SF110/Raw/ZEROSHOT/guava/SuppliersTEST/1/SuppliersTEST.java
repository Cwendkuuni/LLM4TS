import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Test case for compose method
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.parseInt(input);
            }
        };
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "100";
            }
        };
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals(Integer.valueOf(100), composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        // Test case for memoize method
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals("Hello", memoizedSupplier.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        // Test case for memoizeWithExpiration method
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        long duration = 5;
        TimeUnit unit = TimeUnit.SECONDS;
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        assertEquals("Hello", memoizedSupplier.get());
    }
    
    @Test
    public void testOfInstance() {
        // Test case for ofInstance method
        Integer instance = 100;
        Supplier<Integer> supplier = Suppliers.ofInstance(instance);
        assertEquals(Integer.valueOf(100), supplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Test case for synchronizedSupplier method
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
        // Test case for supplierFunction method
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 100;
            }
        };
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(Integer.valueOf(100), supplierFunction.apply(supplier));
    }
}