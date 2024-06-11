import org.junit.*;
import static org.junit.Assert.*;

public class SuppliersTest {

    private static final String TEST_STRING = "testString";
    private static final long TEST_DURATION = 1000L;
    
    @Test
    public void testCompose() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 10;
            }
        };
        
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return String.valueOf(input);
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
                return TEST_STRING;
            }
        };
        
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        String result1 = memoizedSupplier.get();
        String result2 = memoizedSupplier.get();
        
        assertEquals(TEST_STRING, result1);
        assertEquals(TEST_STRING, result2);
        assertSame(result1, result2);
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return TEST_STRING;
            }
        };
        
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, TEST_DURATION, TimeUnit.MILLISECONDS);
        String result1 = memoizedSupplier.get();
        Thread.sleep(TEST_DURATION);
        String result2 = memoizedSupplier.get();
        
        assertEquals(TEST_STRING, result1);
        assertEquals(TEST_STRING, result2);
        assertNotSame(result1, result2);
    }
    
    @Test
    public void testOfInstance() {
        Supplier<String> instanceSupplier = Suppliers.ofInstance(TEST_STRING);
        
        assertEquals(TEST_STRING, instanceSupplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            int count = 0;
            
            @Override
            public Integer get() {
                return count++;
            }
        };
        
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        Integer result1 = synchronizedSupplier.get();
        Integer result2 = synchronizedSupplier.get();
        
        assertEquals(0, result1.intValue());
        assertEquals(1, result2.intValue());
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return TEST_STRING;
            }
        };
        
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        String result = supplierFunction.apply(supplier);
        
        assertEquals(TEST_STRING, result);
    }
}