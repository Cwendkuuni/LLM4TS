import com.google.common.base.Suppliers;
import com.google.common.base.Supplier;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Supplier<Integer> s1 = Suppliers.compose(x -> x + 1, () -> 10);
        assertNotNull(s1);
        assertEquals(Integer.valueOf(11), s1.get());
        
        Supplier<String> s2 = Suppliers.compose(Object::toString, () -> 10);
        assertNotNull(s2);
        assertEquals("10", s2.get());
    }
    
    @Test
    public void testMemoize() {
        Supplier<Integer> s1 = Suppliers.memoize(() -> 10);
        assertNotNull(s1);
        assertEquals(Integer.valueOf(10), s1.get());
        
        Supplier<String> s2 = Suppliers.memoize(Object::toString);
        assertNotNull(s2);
        assertEquals("10", s2.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Integer> s1 = Suppliers.memoizeWithExpiration(() -> 10, 1, java.util.concurrent.TimeUnit.SECONDS);
        assertNotNull(s1);
        assertEquals(Integer.valueOf(10), s1.get());
        
        Supplier<String> s2 = Suppliers.memoizeWithExpiration(Object::toString, 1, java.util.concurrent.TimeUnit.SECONDS);
        assertNotNull(s2);
        assertEquals("10", s2.get());
    }
    
    @Test
    public void testOfInstance() {
        Supplier<Integer> s1 = Suppliers.ofInstance(10);
        assertNotNull(s1);
        assertEquals(Integer.valueOf(10), s1.get());
        
        Supplier<String> s2 = Suppliers.ofInstance("test");
        assertNotNull(s2);
        assertEquals("test", s2.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> s1 = Suppliers.synchronizedSupplier(() -> 10);
        assertNotNull(s1);
        assertEquals(Integer.valueOf(10), s1.get());
        
        Supplier<String> s2 = Suppliers.synchronizedSupplier(Object::toString);
        assertNotNull(s2);
        assertEquals("10", s2.get());
    }
    
    @Test
    public void testSupplierFunction() {
        Suppliers.SupplierFunction<Integer> sf1 = Suppliers.supplierFunction();
        assertNotNull(sf1);
        
        Supplier<Integer> s1 = Suppliers.ofInstance(10);
        assertNotNull(s1);
        assertEquals(Integer.valueOf(10), sf1.apply(s1));
        
        Suppliers.SupplierFunction<String> sf2 = Suppliers.supplierFunction();
        assertNotNull(sf2);
        
        Supplier<String> s2 = Suppliers.ofInstance("test");
        assertNotNull(s2);
        assertEquals("test", sf2.apply(s2));
    }
}