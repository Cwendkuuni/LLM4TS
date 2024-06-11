
import static org.junit.Assert.*;
import org.junit.Test;

public class SuppliersTest {
    
    @Test
    public void testCompose() {
        // Test basic functionality
        // Case when functionApply and supplierGet are called
        Suppliers.SupplierComposition<String, Integer> supplier = new Suppliers.SupplierComposition<>(s -> s.length(), () -> "test");
        assertEquals(Integer.valueOf(4), supplier.get());
    }
    
    @Test
    public void testMemoize() {
        // Test basic functionality
        // Case when delegate.get is called only once
        Suppliers.MemoizingSupplier<String> supplier = new Suppliers.MemoizingSupplier<>(() -> "test");
        assertEquals("test", supplier.get());
        assertEquals("test", supplier.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        // Test basic functionality
        // Case when delegate.get is called before expiration duration
        Suppliers.ExpiringMemoizingSupplier<String> supplier = new Suppliers.ExpiringMemoizingSupplier<>(() -> "test", 1, TimeUnit.SECONDS);
        assertEquals("test", supplier.get());
        Thread.sleep(500);
        assertEquals("test", supplier.get());
        Thread.sleep(1000);
        assertEquals("test", supplier.get());
        
        // Case when delegate.get is called after expiration duration
        supplier = new Suppliers.ExpiringMemoizingSupplier<>(() -> "test", 1, TimeUnit.SECONDS);
        assertEquals("test", supplier.get());
        Thread.sleep(2000);
        assertEquals("test", supplier.get());
    }
    
    @Test
    public void testOfInstance() {
        // Test basic functionality
        // Case when instance is null
        Suppliers.SupplierOfInstance<String> supplier = new Suppliers.SupplierOfInstance<>(null);
        assertNull(supplier.get());
        
        // Case when instance is not null
        supplier = new Suppliers.SupplierOfInstance<>("test");
        assertEquals("test", supplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Test basic functionality
        // Case when delegate.get is called once
        Suppliers.ThreadSafeSupplier<String> supplier = new Suppliers.ThreadSafeSupplier<>(() -> "test");
        assertEquals("test", supplier.get());
    }
    
    @Test
    public void testSupplierFunction() {
        // Test basic functionality
        Suppliers.SupplierFunction<String> supplierFunction = new Suppliers.SupplierFunctionImpl();
        Suppliers.SupplierOfInstance<String> supplier = new Suppliers.SupplierOfInstance<>("test");
        assertEquals("test", supplierFunction.apply(supplier));
    }
}
