import org.junit.*;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Typical use case
        Function<String, Integer> function = s -> s.length();
        Supplier<String> supplier = () -> "hello";
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals(5, composedSupplier.get().intValue());
        
        // Edge case
        Supplier<Integer> nullSupplier = null;
        Supplier<Integer> composedSupplier2 = Suppliers.compose(function, nullSupplier);
        assertNull(composedSupplier2);
        
        // Error scenario
        Function<String, Integer> nullFunction = null;
        Supplier<String> supplier2 = () -> "hello";
        Supplier<Integer> composedSupplier3 = Suppliers.compose(nullFunction, supplier2);
        assertNull(composedSupplier3);
    }
    
    @Test
    public void testMemoize() {
        // Typical use case
        Supplier<String> delegate = () -> "hello";
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals("hello", memoizedSupplier.get());
        
        // Edge case
        Supplier<String> nullDelegate = null;
        Supplier<String> memoizedSupplier2 = Suppliers.memoize(nullDelegate);
        assertNull(memoizedSupplier2);
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        // Typical use case
        Supplier<String> delegate = () -> "hello";
        Supplier<String> expiringSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals("hello", expiringSupplier.get());
        Thread.sleep(2000); // Wait for expiration
        assertEquals("hello", expiringSupplier.get());
        
        // Edge case
        Supplier<String> nullDelegate = null;
        Supplier<String> expiringSupplier2 = Suppliers.memoizeWithExpiration(nullDelegate, 1, TimeUnit.SECONDS);
        assertNull(expiringSupplier2);
    }
    
    @Test
    public void testOfInstance() {
        // Typical use case
        Integer instance = 100;
        Supplier<Integer> instanceSupplier = Suppliers.ofInstance(instance);
        assertEquals(100, instanceSupplier.get().intValue());
        
        // Edge case
        Integer nullInstance = null;
        Supplier<Integer> nullInstanceSupplier = Suppliers.ofInstance(nullInstance);
        assertNull(nullInstanceSupplier);
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Typical use case
        Supplier<String> delegate = () -> "hello";
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals("hello", synchronizedSupplier.get());
        
        // Edge case
        Supplier<String> nullDelegate = null;
        Supplier<String> synchronizedSupplier2 = Suppliers.synchronizedSupplier(nullDelegate);
        assertNull(synchronizedSupplier2);
    }
    
    @Test
    public void testSupplierFunction() {
        // Typical use case
        Supplier<Integer> supplier = () -> 100;
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(100, supplierFunction.apply(supplier).intValue());
        
        // Edge case
        Supplier<Integer> nullSupplier = null;
        assertNull(supplierFunction.apply(nullSupplier));
    }

}