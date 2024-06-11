import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.*;

public class SuppliersTest {

    // Test for compose method
    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                if (input % 2 == 0) {
                    return "Even";
                } else {
                    return "Odd";
                }
            }
        };
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        
        assertEquals("Odd", composedSupplier.get());
    }
    
    // Test for memoize method
    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 10;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        
        assertEquals(new Integer(10), memoizedSupplier.get());
    }
    
    // Test for memoizeWithExpiration method
    @Test(timeout = 200)
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                try {
                    Thread.sleep(100); // Simulate long running process
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 20;
            }
        };
        Supplier<Integer> expiringMemoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 50, TimeUnit.MILLISECONDS);
        
        assertEquals(new Integer(20), expiringMemoizedSupplier.get());
    }
    
    // Test for ofInstance method
    @Test
    public void testOfInstance() {
        Supplier<String> instanceSupplier = Suppliers.ofInstance("Hello");
        
        assertEquals("Hello", instanceSupplier.get());
    }
    
    // Test for synchronizedSupplier method
    @Test
    public void testSynchronizedSupplier() throws InterruptedException {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                try {
                    Thread.sleep(100); // Simulate long running process
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 30;
            }
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        
        assertEquals(new Integer(30), synchronizedSupplier.get());
    }
    
    // Test for supplierFunction method
    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 40;
            }
        };
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        
        assertEquals(new Integer(40), supplierFunction.apply(supplier));
    }
}