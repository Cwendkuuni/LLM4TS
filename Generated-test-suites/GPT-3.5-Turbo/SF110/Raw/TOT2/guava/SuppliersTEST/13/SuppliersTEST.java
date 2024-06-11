import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import com.google.common.base.Supplier;
import com.google.common.base.Function;
import com.google.common.base.Suppliers;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Supplier<String> supplier = Suppliers.ofInstance("test");
        Supplier<Integer> composedSupplier = Suppliers.compose(String::length, supplier);
        assertEquals(4, composedSupplier.get().intValue());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = () -> 5;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(5, memoizedSupplier.get().intValue());
    }

    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Integer> supplier = () -> 10;
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        assertEquals(10, memoizedSupplier.get().intValue());
        Thread.sleep(2000);
        assertEquals(10, memoizedSupplier.get().intValue());
    }

    @Test
    public void testOfInstance() {
        Supplier<String> instanceSupplier = Suppliers.ofInstance("test");
        assertEquals("test", instanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> supplier = () -> 15;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertEquals(15, synchronizedSupplier.get().intValue());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = Suppliers.ofInstance("test");
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        assertEquals("test", supplierFunction.apply(supplier));
    }
}