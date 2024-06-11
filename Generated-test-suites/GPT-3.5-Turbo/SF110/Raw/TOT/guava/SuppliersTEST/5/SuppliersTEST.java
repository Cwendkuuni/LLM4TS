import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Typical use case: Valid function and supplier provided
        Function<Integer, String> function = new Function<Integer, String>(){
            @Override
            public String apply(Integer input) {
                return input.toString();
            }
        };
        Supplier<Integer> supplier = new Supplier<Integer>(){
            @Override
            public Integer get() {
                return 10;
            }
        };
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        assertNotNull(composedSupplier);
        assertEquals("10", composedSupplier.get());

        // Edge case: Supplier returns null
        Supplier<Integer> nullSupplier = new Supplier<Integer>(){
            @Override
            public Integer get() {
                return null;
            }
        };
        Supplier<String> composedSupplierNull = Suppliers.compose(function, nullSupplier);
        assertNotNull(composedSupplierNull);
        assertNull(composedSupplierNull.get());
        
        // Error scenario: Null function provided
        Function<Integer, String> nullFunction = null;
        try {
            Suppliers.compose(nullFunction, supplier);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("Function cannot be null", e.getMessage());
        }
    }
    
    @Test
    public void testMemoize() {
        // Typical use case: Supplier returns non-null value
        Supplier<Integer> supplier = new Supplier<Integer>(){
            @Override
            public Integer get() {
                return 10;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertNotNull(memoizedSupplier);
        assertEquals(new Integer(10), memoizedSupplier.get());

        // Edge case: Supplier returns null
        Supplier<Integer> nullSupplier = new Supplier<Integer>(){
            @Override
            public Integer get() {
                return null;
            }
        };
        Supplier<Integer> memoizedSupplierNull = Suppliers.memoize(nullSupplier);
        assertNotNull(memoizedSupplierNull);
        assertNull(memoizedSupplierNull.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        // Typical use case: Supplier returns non-null value, expiration time not reached
        Supplier<Integer> supplier = new Supplier<Integer>(){
            @Override
            public Integer get() {
                return 10;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 5, TimeUnit.SECONDS);
        assertNotNull(memoizedSupplier);
        assertEquals(new Integer(10), memoizedSupplier.get());
        // Sleep for 2 seconds so that expiration time is not reached
        Thread.sleep(2000);
        assertEquals(new Integer(10), memoizedSupplier.get());

        // Typical use case: Supplier returns non-null value, expiration time reached
        Supplier<Integer> supplier2 = new Supplier<Integer>(){
            @Override
            public Integer get() {
                return 20;
            }
        };
        Supplier<Integer> memoizedSupplier2 = Suppliers.memoizeWithExpiration(supplier2, 1, TimeUnit.SECONDS);
        assertNotNull(memoizedSupplier2);
        assertEquals(new Integer(20), memoizedSupplier2.get());
        // Sleep for 2 seconds so that expiration time is reached
        Thread.sleep(2000);
        assertEquals(new Integer(20), memoizedSupplier2.get());
    }
    
    @Test
    public void testOfInstance() {
        // Typical use case: Non-null instance provided
        String instance = "test";
        Supplier<String> supplier = Suppliers.ofInstance(instance);
        assertNotNull(supplier);
        assertEquals("test", supplier.get());

        // Edge case: Null instance provided
        String nullInstance = null;
        Supplier<String> supplierNull = Suppliers.ofInstance(nullInstance);
        assertNotNull(supplierNull);
        assertNull(supplierNull.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Typical use case: Non-null delegate provided
        Supplier<Integer> delegate = new Supplier<Integer>(){
            @Override
            public Integer get() {
                return 10;
            }
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertNotNull(synchronizedSupplier);
        assertEquals(new Integer(10), synchronizedSupplier.get());

        // Edge case: Null delegate provided
        Supplier<Integer> nullDelegate = null;
        try {
            Suppliers.synchronizedSupplier(nullDelegate);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            assertEquals("Delegate supplier cannot be null", e.getMessage());
        }
    }

    @Test
    public void testSupplierFunction() {
        // Typical use case
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertNotNull(supplierFunction);
        Supplier<Integer> supplier = new Supplier<Integer>(){
            @Override
            public Integer get() {
                return 10;
            }
        };
        assertEquals(new Integer(10), supplierFunction.apply(supplier));
    }
}