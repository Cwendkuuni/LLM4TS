import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class SuppliersTest {

    private Suppliers.SupplierComposition<String, Integer> compositionSupplier;
    private Suppliers.MemoizingSupplier<String> memoizingSupplier;
    private Suppliers.ExpiringMemoizingSupplier<Integer> expiringMemoizingSupplier;
    private Suppliers.SupplierOfInstance<Integer> instanceSupplier;
    private Suppliers.ThreadSafeSupplier<Boolean> threadSafeSupplier;
    private Suppliers.SupplierFunction<Integer> functionSupplier;

    @Before
    public void setUp() {
        compositionSupplier = new Suppliers.SupplierComposition<>(s -> s.length(), () -> "test");
        memoizingSupplier = new Suppliers.MemoizingSupplier<>(() -> "memoize");
        expiringMemoizingSupplier = new Suppliers.ExpiringMemoizingSupplier<>(() -> 100, 5, TimeUnit.SECONDS);
        instanceSupplier = new Suppliers.SupplierOfInstance<Integer>(10);
        threadSafeSupplier = new Suppliers.ThreadSafeSupplier<>(() -> true);
        functionSupplier = Suppliers.supplierFunction();
    }

    @After
    public void tearDown() {
        compositionSupplier = null;
        memoizingSupplier = null;
        expiringMemoizingSupplier = null;
        instanceSupplier = null;
        threadSafeSupplier = null;
        functionSupplier = null;
    }

    @Test
    public void testCompose() {
        Integer result = compositionSupplier.get();
        assertEquals(result, new Integer(4));
    }

    @Test
    public void testMemoize() {
        String result = memoizingSupplier.get();
        assertEquals(result, "memoize");
    }

    @Test
    public void testMemoizeWithExpiration() {
        Integer result = expiringMemoizingSupplier.get();
        assertEquals(result, new Integer(100));
    }

    @Test
    public void testOfInstance() {
        Integer result = instanceSupplier.get();
        assertEquals(result, new Integer(10));
    }

    @Test
    public void testSynchronizedSupplier() {
        Boolean result = threadSafeSupplier.get();
        assertTrue(result);
    }

    @Test
    public void testSupplierFunction() {
        Integer result = functionSupplier.apply(() -> 5);
        assertEquals(result, new Integer(5));
    }
}