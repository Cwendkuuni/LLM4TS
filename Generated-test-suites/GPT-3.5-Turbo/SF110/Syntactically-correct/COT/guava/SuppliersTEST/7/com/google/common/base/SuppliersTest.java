
import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.Suppliers;
import com.google.common.base.Suppliers.SupplierComposition;
import com.google.common.base.Suppliers.MemoizingSupplier;
import com.google.common.base.Suppliers.ExpiringMemoizingSupplier;
import com.google.common.base.Suppliers.SupplierOfInstance;
import com.google.common.base.Suppliers.ThreadSafeSupplier;

public class SuppliersTest {

    @Test
    public void testCompose() {
        SupplierComposition<String, Integer> supplier = new SupplierComposition<>(String::length, () -> "Hello");
        assertEquals(5, supplier.get().intValue());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = () -> 42;
        MemoizingSupplier<Integer> supplier = new MemoizingSupplier<>(delegate);
        assertEquals(42, supplier.get().intValue());
        assertEquals(42, supplier.get().intValue());
    }

    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Integer> delegate = () -> 42;
        ExpiringMemoizingSupplier<Integer> supplier = new ExpiringMemoizingSupplier<>(delegate, 1, TimeUnit.SECONDS);
        assertEquals(42, supplier.get().intValue());
        Thread.sleep(2000);
        assertEquals(42, supplier.get().intValue());
    }

    @Test
    public void testOfInstance() {
        Supplier<String> supplier = new SupplierOfInstance<>("Hello");
        assertEquals("Hello", supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = () -> 42;
        ThreadSafeSupplier<Integer> supplier = new ThreadSafeSupplier<>(delegate);
        assertEquals(42, supplier.get().intValue());
    }

    @Test
    public void testSupplierFunction() {
        Suppliers.SupplierFunction<Integer> function = Suppliers.supplierFunction();
        Supplier<Integer> supplier = () -> 42;
        assertEquals(42, function.apply(supplier).intValue());
    }
}
