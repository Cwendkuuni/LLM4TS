import org.junit.*;
import static org.junit.Assert.*;
import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;

@GwtCompatible
public class SuppliersTest {
    
    @Test
    public void testCompose() {
        Function<Object, String> function = new Function<Object, String>() {
            @Override
            public String apply(Object input) {
                return input.toString();
            }
        };
        Supplier<Object> supplier = new Supplier<Object>() {
            @Override
            public Object get() {
                return new Object();
            }
        };
        Supplier<String> result = Suppliers.compose(function, supplier);
        assertNotNull(result);
    }
    
    @Test
    public void testMemoize() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<String> result = Suppliers.memoize(delegate);
        assertNotNull(result);
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        long duration = 10;
        TimeUnit unit = TimeUnit.SECONDS;
        Supplier<String> result = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        assertNotNull(result);
    }
    
    @Test
    public void testOfInstance() {
        String instance = "Hello";
        Supplier<String> result = Suppliers.ofInstance(instance);
        assertNotNull(result);
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<String> result = Suppliers.synchronizedSupplier(delegate);
        assertNotNull(result);
    }
    
    @Test
    public void testSupplierFunction() {
        Function<Supplier<Object>, Object> result = Suppliers.supplierFunction();
        assertNotNull(result);
    }
}