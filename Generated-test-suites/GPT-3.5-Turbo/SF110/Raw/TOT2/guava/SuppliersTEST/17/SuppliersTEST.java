import static org.junit.Assert.*;
import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Test typical scenario
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        Supplier<Integer> composedSupplier = Suppliers.compose(Object::toString, supplier);
        assertEquals("Hello", composedSupplier.get());

        // Test edge case where function returns null
        Supplier<String> nullSupplier = Suppliers.ofInstance(null);
        Supplier<Integer> nullComposedSupplier = Suppliers.compose(Object::hashCode, nullSupplier);
        assertNull(nullComposedSupplier.get());
    }

    @Test
    public void testMemoize() {
        // Test typical scenario
        Supplier<String> supplier = Suppliers.ofInstance("MemoizeTest");
        Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals("MemoizeTest", memoizedSupplier.get());

        // Test edge case where supplier returns null
        Supplier<String> nullSupplier = Suppliers.ofInstance(null);
        Supplier<String> nullMemoizedSupplier = Suppliers.memoize(nullSupplier);
        assertNull(nullMemoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Test typical scenario
        Supplier<String> supplier = Suppliers.ofInstance("MemoizeExpireTest");
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        assertEquals("MemoizeExpireTest", memoizedSupplier.get());

        // Test edge case where supplier returns null
        Supplier<String> nullSupplier = Suppliers.ofInstance(null);
        Supplier<String> nullMemoizedSupplier = Suppliers.memoizeWithExpiration(nullSupplier, 1, TimeUnit.SECONDS);
        assertNull(nullMemoizedSupplier.get());
    }

    @Test
    public void testOfInstance() {
        // Test typical scenario
        Supplier<String> supplier = Suppliers.ofInstance("OfInstanceTest");
        assertEquals("OfInstanceTest", supplier.get());

        // Test edge case where instance is null
        Supplier<String> nullSupplier = Suppliers.ofInstance(null);
        assertNull(nullSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        // Test typical scenario
        Supplier<String> supplier = Suppliers.ofInstance("SynchronizedTest");
        Supplier<String> syncedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertEquals("SynchronizedTest", syncedSupplier.get());

        // Test edge case where synchronized supplier is null
        Supplier<String> nullSupplier = Suppliers.ofInstance(null);
        Supplier<String> nullSyncedSupplier = Suppliers.synchronizedSupplier(nullSupplier);
        assertNull(nullSyncedSupplier.get());
    }
}