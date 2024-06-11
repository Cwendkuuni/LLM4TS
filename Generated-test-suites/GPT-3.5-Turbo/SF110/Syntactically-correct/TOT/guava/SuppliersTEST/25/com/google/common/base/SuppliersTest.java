import java.util.concurrent.TimeUnit;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Typical use case
        Supplier<Integer> supplier1 = Suppliers.ofInstance(5);
        Function<Integer, String> function1 = new Function<Integer, String>() {
            public String apply(Integer i) {
                return Integer.toString(i);
            }
        };
        Supplier<String> composed1 = Suppliers.compose(function1, supplier1);
        assertEquals("5", composed1.get());
        
        // Edge case
        Supplier<Integer> supplier2 = Suppliers.ofInstance(null);
        Function<Integer, String> function2 = new Function<Integer, String>() {
            public String apply(Integer i) {
                return Integer.toString(i);
            }
        };
        Supplier<String> composed2 = Suppliers.compose(function2, supplier2);
        assertNull(composed2.get());
        
        // Error scenario
        Supplier<String> composed3 = Suppliers.compose(null, supplier1);
        try {
            composed3.get();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }
    
    @Test
    public void testMemoize() {
        // Typical use case
        Supplier<Integer> supplier1 = Suppliers.ofInstance(5);
        Supplier<Integer> memoized1 = Suppliers.memoize(supplier1);
        assertEquals(5, (int) memoized1.get());
        
        // Edge case
        Supplier<Integer> supplier2 = Suppliers.ofInstance(null);
        Supplier<Integer> memoized2 = Suppliers.memoize(supplier2);
        assertNull(memoized2.get());
        
        // Error scenario
        Supplier<Integer> memoized3 = Suppliers.memoize(null);
        try {
            memoized3.get();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        // Typical use case
        Supplier<Integer> supplier1 = new Supplier<Integer>() {
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> memoized1 = Suppliers.memoizeWithExpiration(supplier1, 100, TimeUnit.MILLISECONDS);
        assertEquals(5, (int) memoized1.get());
        
        // Edge case
        Supplier<Integer> supplier2 = Suppliers.ofInstance(null);
        Supplier<Integer> memoized2 = Suppliers.memoizeWithExpiration(supplier2, 100, TimeUnit.MILLISECONDS);
        assertNull(memoized2.get());
        
        // Error scenario
        Supplier<Integer> memoized3 = Suppliers.memoizeWithExpiration(null, 100, TimeUnit.MILLISECONDS);
        try {
            memoized3.get();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }
    
    @Test
    public void testOfInstance() {
        // Typical use case
        Supplier<Integer> instanceSupplier1 = Suppliers.ofInstance(5);
        assertEquals(5, (int) instanceSupplier1.get());
        
        // Edge case
        Supplier<Integer> instanceSupplier2 = Suppliers.ofInstance(null);
        assertNull(instanceSupplier2.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Typical use case
        Supplier<Integer> supplier1 = Suppliers.ofInstance(5);
        Supplier<Integer> synchronizedSupplier1 = Suppliers.synchronizedSupplier(supplier1);
        assertEquals(5, (int) synchronizedSupplier1.get());
        
        // Edge case
        Supplier<Integer> supplier2 = Suppliers.ofInstance(null);
        Supplier<Integer> synchronizedSupplier2 = Suppliers.synchronizedSupplier(supplier2);
        assertNull(synchronizedSupplier2.get());
        
        // Error scenario
        Supplier<Integer> synchronizedSupplier3 = Suppliers.synchronizedSupplier(null);
        try {
            synchronizedSupplier3.get();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected
        }
    }
    
    @Test
    public void testSupplierFunction() {
        // Typical use case
        Supplier<Integer> supplier1 = Suppliers.ofInstance(5);
        Function<Supplier<Integer>, Integer> supplierFunction1 = Suppliers.supplierFunction();
        assertEquals(5, (int) supplierFunction1.apply(supplier1));
        
        // Edge case
        Supplier<Integer> supplier2 = Suppliers.ofInstance(null);
        Function<Supplier<Integer>, Integer> supplierFunction2 = Suppliers.supplierFunction();
        assertNull(supplierFunction2.apply(supplier2));
    }
}