import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.Suppliers;

public class SuppliersTest {
    
    @Test
    public void testCompose() {
        Function<Integer, String> function = num -> String.valueOf(num);
        Supplier<Integer> supplier = () -> 5;
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        
        assertEquals("5", composedSupplier.get());
    }
    
    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = () -> 10;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        
        assertEquals(10, memoizedSupplier.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Integer> delegate = () -> 20;
        Supplier<Integer> expiringMemoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        
        assertEquals(20, expiringMemoizedSupplier.get());
    }
    
    @Test
    public void testOfInstance() {
        Supplier<String> instanceSupplier = Suppliers.ofInstance("Hello");
        
        assertEquals("Hello", instanceSupplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = () -> 15;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        
        assertEquals(15, synchronizedSupplier.get());
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = () -> 25;
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        
        assertEquals(25, supplierFunction.apply(supplier).intValue());
    }
    
    // Additional edge cases and exceptions scenarios should be added for thorough testing
    
}