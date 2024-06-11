import com.google.common.base.Suppliers;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose_typical() {
        Suppliers.SupplierComposition<Integer, Integer> supplierComposition = new Suppliers.SupplierComposition<>(x -> x * 2, () -> 5);
        assertEquals(Integer.valueOf(10), supplierComposition.get());
    }

    @Test
    public void testCompose_edge() {
        Suppliers.SupplierComposition<Integer, Integer> supplierComposition = new Suppliers.SupplierComposition<>(x -> x + 1, () -> Integer.MAX_VALUE);
        assertEquals(Integer.valueOf(Integer.MAX_VALUE + 1), supplierComposition.get());
    }

    @Test
    public void testCompose_error() {
        Suppliers.SupplierComposition<Integer, Integer> supplierComposition = new Suppliers.SupplierComposition<>(x -> x / 0, () -> 5);
        assertThrows(ArithmeticException.class, supplierComposition::get);
    }

    @Test
    public void testMemoize_typical() {
        IntegerSupplier supplier = new IntegerSupplier();
        Suppliers.MemoizingSupplier<Integer> memoizingSupplier = new Suppliers.MemoizingSupplier<>(supplier);

        assertEquals(Integer.valueOf(5), memoizingSupplier.get());
        assertEquals(Integer.valueOf(5), memoizingSupplier.get());
        assertEquals(1, supplier.getCallCount());
    }

    @Test
    public void testMemoize_edge() {
        IntegerSupplier supplier = new IntegerSupplier();
        Suppliers.MemoizingSupplier<Integer> memoizingSupplier = new Suppliers.MemoizingSupplier<>(supplier);

        assertEquals(Integer.valueOf(0), memoizingSupplier.get());
        assertEquals(Integer.valueOf(0), memoizingSupplier.get());
        assertEquals(1, supplier.getCallCount());
    }

    @Test
    public void testMemoize_error() {
        Suppliers.MemoizingSupplier<Integer> memoizingSupplier = new Suppliers.MemoizingSupplier<>(() -> 1 / 0);
        assertThrows(ArithmeticException.class, memoizingSupplier::get);
    }

    @Test
    public void testMemoizeWithExpiration_typical() throws InterruptedException {
        IntegerSupplier supplier = new IntegerSupplier();
        Suppliers.ExpiringMemoizingSupplier<Integer> expiringMemoizingSupplier = new Suppliers.ExpiringMemoizingSupplier<>(supplier, 1000, java.util.concurrent.TimeUnit.MILLISECONDS);

        assertEquals(Integer.valueOf(5), expiringMemoizingSupplier.get());
        Thread.sleep(2000);
        assertEquals(Integer.valueOf(5), expiringMemoizingSupplier.get());
        assertEquals(2, supplier.getCallCount());
    }

    @Test
    public void testMemoizeWithExpiration_edge() {
        IntegerSupplier supplier = new IntegerSupplier();
        Suppliers.ExpiringMemoizingSupplier<Integer> expiringMemoizingSupplier = new Suppliers.ExpiringMemoizingSupplier<>(supplier, 0, java.util.concurrent.TimeUnit.MILLISECONDS);

        assertEquals(Integer.valueOf(5), expiringMemoizingSupplier.get());
        assertEquals(Integer.valueOf(5), expiringMemoizingSupplier.get());
        assertEquals(2, supplier.getCallCount());
    }

    @Test
    public void testMemoizeWithExpiration_error() {
        Suppliers.ExpiringMemoizingSupplier<Integer> expiringMemoizingSupplier = new Suppliers.ExpiringMemoizingSupplier<>(() -> 1 / 0, 1000, java.util.concurrent.TimeUnit.MILLISECONDS);
        assertThrows(ArithmeticException.class, expiringMemoizingSupplier::get);
    }

    @Test
    public void testOfInstance_typical() {
        Suppliers.SupplierOfInstance<Integer> supplierOfInstance = new Suppliers.SupplierOfInstance<>(5);
        assertEquals(Integer.valueOf(5), supplierOfInstance.get());
    }

    @Test
    public void testOfInstance_edge() {
        Suppliers.SupplierOfInstance<Integer> supplierOfInstance = new Suppliers.SupplierOfInstance<>(null);
        assertNull(supplierOfInstance.get());
    }

    @Test
    public void testSynchronizedSupplier_typical() {
        IntegerSupplier supplier = new IntegerSupplier();
        Suppliers.ThreadSafeSupplier<Integer> synchronizedSupplier = new Suppliers.ThreadSafeSupplier<>(supplier);

        assertEquals(Integer.valueOf(5), synchronizedSupplier.get());
        assertEquals(1, supplier.getCallCount());
    }

    @Test
    public void testSynchronizedSupplier_error() {
        Suppliers.ThreadSafeSupplier<Integer> synchronizedSupplier = new Suppliers.ThreadSafeSupplier<>(() -> 1 / 0);
        assertThrows(ArithmeticException.class, synchronizedSupplier::get);
    }

    @Test
    public void testSupplierFunction_typical() {
        Suppliers.SupplierOfInstance<Integer> supplierOfInstance = new Suppliers.SupplierOfInstance<>(5);
        Suppliers.SupplierFunction<Integer> supplierFunction = Suppliers.supplierFunction();

        assertEquals(Integer.valueOf(5), supplierFunction.apply(supplierOfInstance));
    }

    private static class IntegerSupplier implements com.google.common.base.Supplier<Integer> {
        private int callCount = 0;

        @Override
        public Integer get() {
            callCount++;
            return 5;
        }

        public int getCallCount() {
            return callCount;
        }
    }
}