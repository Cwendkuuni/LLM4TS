import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import com.google.common.base.Supplier;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        Supplier<String> composedSupplier = Suppliers.compose(this::convertToString, supplier);
        
        assertEquals("10", composedSupplier.get());
    }
    
    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        
        assertEquals(10, memoizedSupplier.get().intValue());
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 2, TimeUnit.SECONDS);
        
        assertEquals(10, memoizedSupplier.get().intValue());
        Thread.sleep(3000); // Wait for expiration
        assertEquals(10, memoizedSupplier.get().intValue());
    }
    
    @Test
    public void testOfInstance() {
        Integer instance = 10;
        Supplier<Integer> supplier = Suppliers.ofInstance(instance);
        
        assertEquals(instance, supplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        
        assertEquals(10, synchronizedSupplier.get().intValue());
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        Integer result = Suppliers.supplierFunction().apply(supplier);
        
        assertEquals(10, result.intValue());
    }
    
    @Test
    public void testEquals() {
        Supplier<Integer> supplier1 = Suppliers.ofInstance(10);
        Supplier<Integer> supplier2 = Suppliers.ofInstance(10);
        
        assertTrue(supplier1.equals(supplier2));
    }
    
    @Test
    public void testHashCode() {
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        
        assertEquals(10, supplier.hashCode());
    }
    
    private String convertToString(Integer input) {
        return String.valueOf(input);
    }
}