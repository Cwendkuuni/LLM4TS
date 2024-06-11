import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class SuppliersTest {

    // Testing compose method
    
    @Test
    public void compose_typicalCase() {
        // Test with a function that increments by 1 and a supplier of 5
        Function<Integer, Integer> function = (Integer num) -> num + 1;
        Supplier<Integer> supplier = () -> 5;
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        
        assertEquals(Integer.valueOf(6), composedSupplier.get());
    }
    
    @Test
    public void compose_nullFunction() {
        // Test with a null function and a supplier of 5
        Function<Integer, Integer> function = null;
        Supplier<Integer> supplier = () -> 5;
        
        try {
            Suppliers.compose(function, supplier);
        } catch (NullPointerException e) {
            return;
        }
        
        fail("Expected NullPointerException was not thrown.");
    }
    
    @Test
    public void compose_nullSupplier() {
        // Test with a function that increments by 1 and a null supplier
        Function<Integer, Integer> function = (Integer num) -> num + 1;
        Supplier<Integer> supplier = null;
        
        try {
            Suppliers.compose(function, supplier);
        } catch (NullPointerException e) {
            return;
        }
        
        fail("Expected NullPointerException was not thrown.");
    }
    
    // Testing memoize method
    
    @Test
    public void memoize_typicalCase() {
        // Test with a supplier that always returns 5
        Supplier<Integer> supplier = () -> 5;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        
        assertEquals(Integer.valueOf(5), memoizedSupplier.get());
    }
    
    @Test
    public void memoize_alreadyMemoized() {
        // Test with a memoized supplier
        Supplier<Integer> supplier = () -> 5;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        Supplier<Integer> doubleMemoizedSupplier = Suppliers.memoize(memoizedSupplier);
        
        assertEquals(Integer.valueOf(5), doubleMemoizedSupplier.get());
    }
    
    // Testing memoizeWithExpiration method
    
    @Test
    public void memoizeWithExpiration_typicalCase() throws InterruptedException {
        // Test with a supplier that always returns 5, with expiration of 1 second
        Supplier<Integer> supplier = () -> 5;
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        
        assertEquals(Integer.valueOf(5), memoizedSupplier.get());
        Thread.sleep(1500);
        assertEquals(Integer.valueOf(5), memoizedSupplier.get());
    }
    
    @Test
    public void memoizeWithExpiration_zeroDuration() {
        // Test with a supplier that always returns 5, with zero duration
        Supplier<Integer> supplier = () -> 5;
        
        try {
            Suppliers.memoizeWithExpiration(supplier, 0, TimeUnit.SECONDS);
        } catch (IllegalArgumentException e) {
            return;
        }
        
        fail("Expected IllegalArgumentException was not thrown.");
    }
    
    // Testing ofInstance method
    
    @Test
    public void ofInstance_typicalCase() {
        // Test with an instance of 5
        Integer instance = 5;
        Supplier<Integer> instanceSupplier = Suppliers.ofInstance(instance);
        
        assertEquals(Integer.valueOf(5), instanceSupplier.get());
    }
    
    @Test
    public void ofInstance_nullInstance() {
        // Test with a null instance
        Integer instance = null;
        Supplier<Integer> instanceSupplier = Suppliers.ofInstance(instance);
        
        assertNull(instanceSupplier.get());
    }
    
    // Testing synchronizedSupplier method
    
    @Test
    public void synchronizedSupplier_typicalCase() {
        // Test with a supplier that always returns 5
        Supplier<Integer> supplier = () -> 5;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        
        assertEquals(Integer.valueOf(5), synchronizedSupplier.get());
    }
    
    @Test
    public void synchronizedSupplier_nullSupplier() {
        // Test with a null supplier
        Supplier<Integer> supplier = null;
        
        try {
            Suppliers.synchronizedSupplier(supplier);
        } catch (NullPointerException e) {
            return;
        }
        
        fail("Expected NullPointerException was not thrown.");
    }
    
    // Testing supplierFunction method
    
    @Test
    public void supplierFunction_typicalCase() {
        // Test with a supplier that always returns 5
        Supplier<Integer> supplier = () -> 5;
        Function<Supplier<Integer>, Integer> supplierToValueFunction = Suppliers.supplierFunction();
        
        assertEquals(Integer.valueOf(5), supplierToValueFunction.apply(supplier));
    }
    
    @Test
    public void supplierFunction_nullSupplier() {
        // Test with a null supplier
        Supplier<Integer> supplier = null;
        Function<Supplier<Integer>, Integer> supplierToValueFunction = Suppliers.supplierFunction();
        
        try {
            supplierToValueFunction.apply(supplier);
        } catch (NullPointerException e) {
            return;
        }
        
        fail("Expected NullPointerException was not thrown.");
    }

}