import static org.junit.Assert.*;
import org.junit.Test;

public class SuppliersTest {
    
    @Test
    public void testCompose_typicalCase() {
        // Arrange
        Function<String, String> function = s -> s.toUpperCase();
        Supplier<String> supplier = () -> "hello";
        
        // Act
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        String result = composedSupplier.get();
        
        // Assert
        assertEquals("HELLO", result);
    }
    
    @Test
    public void testMemoize_typicalCase() {
        // Arrange
        Supplier<Integer> delegate = () -> 5;
        
        // Act
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        int result1 = memoizedSupplier.get();
        int result2 = memoizedSupplier.get();
        
        // Assert
        assertEquals(5, result1);
        assertEquals(5, result2);
    }
    
    @Test
    public void testMemoize_delegateIsMemoizingSupplier() {
        // Arrange
        Supplier<String> delegate = () -> "hello";
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        
        // Act
        Supplier<String> result = Suppliers.memoize(memoizedSupplier);
        
        // Assert
        assertSame(memoizedSupplier, result);
    }
    
    @Test
    public void testMemoizeWithExpiration_typicalCase() throws InterruptedException {
        // Arrange
        Supplier<Integer> delegate = () -> 5;
        long duration = 1000;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        
        // Act
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        int result1 = memoizedSupplier.get();
        Thread.sleep(duration);
        int result2 = memoizedSupplier.get();
        
        // Assert
        assertEquals(5, result1);
        assertNotEquals(5, result2);
    }
    
    @Test
    public void testOfInstance_typicalCase() {
        // Arrange
        String instance = "hello";
        
        // Act
        Supplier<String> instanceSupplier = Suppliers.ofInstance(instance);
        String result = instanceSupplier.get();
        
        // Assert
        assertEquals("hello", result);
    }
    
    @Test
    public void testSynchronizedSupplier_typicalCase() {
        // Arrange
        Supplier<String> delegate = () -> "hello";
        
        // Act
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        String result = synchronizedSupplier.get();
        
        // Assert
        assertEquals("hello", result);
    }
    
    @Test
    public void testSupplierFunction_typicalCase() {
        // Arrange
        Supplier<String> supplier = () -> "hello";
        
        // Act
        String result = Suppliers.supplierFunction().apply(supplier);
        
        // Assert
        assertEquals("hello", result);
    }
}