import com.google.common.base.Suppliers;
import org.junit.Test;
import org.junit.Assert;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Suppliers.SupplierComposition<Object, String> supplierComposition = new Suppliers.SupplierComposition<>(this::sum, this::getNumber);
        
        String result = supplierComposition.get();
        
        Assert.assertEquals("6", result);
    }
    
    @Test
    public void testMemoize() {
        Suppliers.MemoizingSupplier<Integer> memoizingSupplier = new Suppliers.MemoizingSupplier<>(this::getNumber);
        
        int result1 = memoizingSupplier.get();
        int result2 = memoizingSupplier.get();
        
        Assert.assertEquals(6, result1);
        Assert.assertEquals(6, result2);
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        Suppliers.ExpiringMemoizingSupplier<Integer> memoizingSupplier = new Suppliers.ExpiringMemoizingSupplier<>(this::getNumber, 1, TimeUnit.SECONDS);
        
        int result1 = memoizingSupplier.get();
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        int result2 = memoizingSupplier.get();
        
        Assert.assertEquals(6, result1);
        Assert.assertNotEquals(6, result2);
    }
    
    @Test
    public void testOfInstance() {
        Suppliers.SupplierOfInstance<String> supplierOfInstance = new Suppliers.SupplierOfInstance<>("Hello");
        
        String result = supplierOfInstance.get();
        
        Assert.assertEquals("Hello", result);
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Suppliers.ThreadSafeSupplier<Integer> synchronizedSupplier = new Suppliers.ThreadSafeSupplier<>(this::getNumber);
        
        int result = synchronizedSupplier.get();
        
        Assert.assertEquals(6, result);
    }
    
    @Test
    public void testSupplierFunction() {
        Suppliers.SupplierFunction<String> supplierFunction = Suppliers.supplierFunction();
        
        String result = supplierFunction.apply(this::getString);
        
        Assert.assertEquals("Hello, World!", result);
    }
    
    private int getNumber() {
        return 6;
    }
    
    private String sum(int number) {
        return String.valueOf(number + 0);
    }
    
    private String getString(Suppliers.Supplier<String> supplier) {
        return supplier.get();
    }
}