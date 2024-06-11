import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class SuppliersTest {

    @Test
    public void compose_validFunctionAndSupplier_returnsComposedSupplier() {
        Function<Integer, String> function = num -> String.valueOf(num);
        Supplier<Integer> supplier = () -> 10;
        
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        
        String expected = "10";
        String actual = composedSupplier.get();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void memoize_notInitializedSupplier_returnsMemoizedValue() {
        Supplier<String> delegate = () -> "Memoized value";
        
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        
        String expected = "Memoized value";
        String actual = memoizedSupplier.get();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void memoize_alreadyMemoizedSupplier_returnsSameValue() {
        Supplier<String> delegate = () -> "Memoized value";
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        
        String expected = memoizedSupplier.get();
        String actual = memoizedSupplier.get();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void memoizeWithExpiration_notExpiredSupplier_returnsSuppliedValue() throws InterruptedException {
        Supplier<String> delegate = () -> "Memoized value";
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        
        String expected = "Memoized value";
        String actual = memoizedSupplier.get();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void memoizeWithExpiration_expiredSupplier_returnsNewValue() throws InterruptedException {
        Supplier<Integer> delegate = () -> 10;
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        
        int expected = 10;
        int actual = memoizedSupplier.get();
        
        Thread.sleep(2000);
        
        int newExpected = 10;
        int newActual = memoizedSupplier.get();
        
        assertEquals(expected, actual);
        assertNotEquals(newExpected, newActual);
    }
    
    @Test
    public void ofInstance_nullInstance_returnsNull() {
        Supplier<String> instanceSupplier = Suppliers.ofInstance(null);
        
        assertNull(instanceSupplier.get());
    }
    
    @Test
    public void ofInstance_validInstance_returnsInstance() {
        Supplier<String> instanceSupplier = Suppliers.ofInstance("Test instance");
        
        String expected = "Test instance";
        String actual = instanceSupplier.get();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void synchronizedSupplier_validDelegate_returnsDelegateValue() {
        Supplier<String> delegate = () -> "Delegate value";
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        
        String expected = "Delegate value";
        String actual = synchronizedSupplier.get();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void supplierFunction_validSupplier_returnsSupplierValue() {
        Supplier<String> supplier = () -> "Supplier value";
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        
        String expected = "Supplier value";
        String actual = supplierFunction.apply(supplier);
        
        assertEquals(expected, actual);
    }

}