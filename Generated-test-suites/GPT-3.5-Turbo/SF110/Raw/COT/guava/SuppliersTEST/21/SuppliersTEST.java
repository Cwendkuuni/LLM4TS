
import org.junit.Test;
import static org.junit.Assert.*;

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
                return 123;
            }
        };
        
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        String result = composedSupplier.get();
        
        assertEquals("123", result);
    }
    
    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 123;
            }
        };
        
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        Integer result1 = memoizedSupplier.get();
        Integer result2 = memoizedSupplier.get();
        
        assertEquals(123, result1.intValue());
        assertEquals(123, result2.intValue());
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 123;
            }
        };
        
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        Integer result1 = memoizedSupplier.get();
        Thread.sleep(2000);
        Integer result2 = memoizedSupplier.get();
        
        assertEquals(123, result1.intValue());
        assertEquals(123, result2.intValue());
    }
    
    @Test
    public void testOfInstance() {
        Integer instance = 123;
        Supplier<Integer> instanceSupplier = Suppliers.ofInstance(instance);
        
        Integer result = instanceSupplier.get();
        
        assertEquals(instance, result);
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 123;
            }
        };
        
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        Integer result = synchronizedSupplier.get();
        
        assertEquals(123, result.intValue());
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 123;
            }
        };
        
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        Integer result = supplierFunction.apply(supplier);
        
        assertEquals(123, result.intValue());
    }
}
