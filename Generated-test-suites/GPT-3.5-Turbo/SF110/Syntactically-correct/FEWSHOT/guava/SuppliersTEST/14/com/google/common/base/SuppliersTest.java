import org.junit.Test;
import org.junit.Assert;
import com.google.common.base.Suppliers;

public class SuppliersTest {
    
    @Test
    public void testCompose() {
        // Test case 1
        Function<Integer, String> function = (num) -> (num * 10) + "";
        Supplier<Integer> supplier = () -> 5;
        Supplier<String> expected = Suppliers.compose(function, supplier);
        
        Assert.assertEquals("50", expected.get());
        
        // Test case 2
        Function<String, Integer> function2 = (str) -> Integer.parseInt(str) + 2;
        Supplier<String> supplier2 = () -> "8";
        Supplier<Integer> expected2 = Suppliers.compose(function2, supplier2);
        
        Assert.assertEquals(10, (int) expected2.get());
    }
    
    @Test
    public void testMemoize() {
        // Test case 1
        Supplier<Integer> delegate = () -> 5;
        Supplier<Integer> expected = Suppliers.memoize(delegate);
        
        Assert.assertEquals(5, (int) expected.get());
        
        // Test case 2
        Supplier<String> delegate2 = () -> "Hello";
        Supplier<String> expected2 = Suppliers.memoize(delegate2);
        
        Assert.assertEquals("Hello", expected2.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        // Test case 1
        Supplier<Integer> delegate = () -> 5;
        long duration = 1000;
        Supplier<Integer> expected = Suppliers.memoizeWithExpiration(delegate, duration, TimeUnit.MILLISECONDS);
        
        Assert.assertEquals(5, (int) expected.get());
        
        // Test case 2
        Supplier<String> delegate2 = () -> "Hello";
        long duration2 = 5000;
        Supplier<String> expected2 = Suppliers.memoizeWithExpiration(delegate2, duration2, TimeUnit.MILLISECONDS);
        
        Assert.assertEquals("Hello", expected2.get());
    }
    
    @Test
    public void testOfInstance() {
        // Test case 1
        Integer instance = 5;
        Supplier<Integer> expected = Suppliers.ofInstance(instance);
        
        Assert.assertEquals(5, (int) expected.get());
        
        // Test case 2
        String instance2 = "Hello";
        Supplier<String> expected2 = Suppliers.ofInstance(instance2);
        
        Assert.assertEquals("Hello", expected2.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Test case 1
        Supplier<Integer> delegate = () -> 5;
        Supplier<Integer> expected = Suppliers.synchronizedSupplier(delegate);
        
        Assert.assertEquals(5, (int) expected.get());
        
        // Test case 2
        Supplier<String> delegate2 = () -> "Hello";
        Supplier<String> expected2 = Suppliers.synchronizedSupplier(delegate2);
        
        Assert.assertEquals("Hello", expected2.get());
    }
    
    @Test
    public void testSupplierFunction() {
        // Test case 1
        Supplier<Integer> supplier = () -> 5;
        Function<Supplier<Integer>, Integer> expected = Suppliers.supplierFunction();
        
        Assert.assertEquals(5, (int) expected.apply(supplier));
        
        // Test case 2
        Supplier<String> supplier2 = () -> "Hello";
        Function<Supplier<String>, String> expected2 = Suppliers.supplierFunction();
        
        Assert.assertEquals("Hello", expected2.apply(supplier2));
    }
}