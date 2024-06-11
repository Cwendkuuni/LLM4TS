import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;

public class SuppliersTest {
    
    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            public String apply(Integer input) {
                return String.valueOf(input);
            }
        };
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals("10", composedSupplier.get());
    }
    
    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(Integer.valueOf(10), memoizedSupplier.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        assertEquals(Integer.valueOf(10), memoizedSupplier.get());
        Thread.sleep(2000);
        assertEquals(Integer.valueOf(10), memoizedSupplier.get());
    }
    
    @Test
    public void testOfInstance() {
        String instance = "Hello";
        Supplier<String> supplier = Suppliers.ofInstance(instance);
        assertEquals("Hello", supplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(Integer.valueOf(10), synchronizedSupplier.get());
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(Integer.valueOf(10), supplierFunction.apply(supplier));
    }
    
}