
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;

public class SuppliersTest {
    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return input.toString();
            }
        };
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals("5", composedSupplier.get());
    }
    
    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals(Integer.valueOf(5), memoizedSupplier.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        assertEquals(Integer.valueOf(5), memoizedSupplier.get());
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
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(Integer.valueOf(5), synchronizedSupplier.get());
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(Integer.valueOf(5), supplierFunction.apply(supplier));
    }
}
