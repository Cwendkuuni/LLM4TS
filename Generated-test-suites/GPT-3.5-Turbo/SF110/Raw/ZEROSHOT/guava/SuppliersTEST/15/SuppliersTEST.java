import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;
import com.google.common.base.Suppliers.*;
import com.google.common.base.Suppliers.*;

public class SuppliersTest {
    
    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            public String apply(Integer i) {
                return String.valueOf(i);
            }
        };
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 5;
            }
        };
        
        Supplier<String> result = Suppliers.compose(function, supplier);
        assertEquals("5", result.get());
    }
    
    @Test
    public void testMemoize() {
        Supplier<String> delegate = new Supplier<String>() {
            public String get() {
                return "Hello";
            }
        };
        
        Supplier<String> result = Suppliers.memoize(delegate);
        assertEquals("Hello", result.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> delegate = new Supplier<String>() {
            public String get() {
                return "Hello";
            }
        };
        
        Supplier<String> result = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals("Hello", result.get());
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertEquals("Hello", result.get());
    }
    
    @Test
    public void testOfInstance() {
        String instance = "Hello";
        Supplier<String> result = Suppliers.ofInstance(instance);
        assertEquals("Hello", result.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            public String get() {
                return "Hello";
            }
        };
        
        Supplier<String> result = Suppliers.synchronizedSupplier(delegate);
        assertEquals("Hello", result.get());
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = new Supplier<String>() {
            public String get() {
                return "Hello";
            }
        };
        
        Function<Supplier<String>, String> function = Suppliers.supplierFunction();
        assertEquals("Hello", function.apply(supplier));
    }
}