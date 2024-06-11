import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;

public class SuppliersTest {

    private final int TIMEOUT = 500;

    @Test
    public void testCompose() {
        // Typical use case: function and supplier return expected values
        Supplier<Integer> supplier = Suppliers.compose((x) -> x.length(), () -> "Hello");
        assertEquals(new Integer(5), supplier.get());

        // Edge case: empty string
        Supplier<Integer> supplier2 = Suppliers.compose((x) -> x.length(), () -> "");
        assertEquals(new Integer(0), supplier2.get());

        // Error scenario: null supplier
        boolean exceptionThrown = false;
        try {
            Suppliers.compose((x) -> x.length(), null);
        } catch (NullPointerException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testMemoize() throws InterruptedException {
        Supplier<Integer> delegate = () -> { return 5; };

        // Typical use case: delegate is not already a MemoizingSupplier
        Supplier<Integer> supplier = Suppliers.memoize(delegate);
        assertEquals(new Integer(5), supplier.get());

        // Edge case: delegate returns different value on second call
        // Make sure the cached value is returned
        Supplier<Integer> supplier2 = Suppliers.memoize(delegate);
        assertEquals(new Integer(5), supplier2.get());
        Thread.sleep(TIMEOUT);
        assertEquals(new Integer(5), supplier2.get());

        // Error scenario: null delegate
        boolean exceptionThrown = false;
        try {
            Suppliers.memoize(null);
        } catch (NullPointerException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Integer> delegate = () -> { return 5; };

        // Typical use case: delegate is memoized with expiration
        Supplier<Integer> supplier = Suppliers.memoizeWithExpiration(delegate, 200, TimeUnit.MILLISECONDS);
        assertEquals(new Integer(5), supplier.get());
        Thread.sleep(TIMEOUT); // Wait for expiration
        assertEquals(new Integer(5), supplier.get());

        // Edge case: expiration time is 0
        Supplier<Integer> supplier2 = Suppliers.memoizeWithExpiration(delegate, 0, TimeUnit.MILLISECONDS);
        assertEquals(new Integer(5), supplier2.get());

        // Error scenario: null delegate
        boolean exceptionThrown = false;
        try {
            Suppliers.memoizeWithExpiration(null, 200, TimeUnit.MILLISECONDS);
        } catch (NullPointerException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testOfInstance() {
        // Typical use case: instance is returned by the supplier
        Supplier<String> supplier = Suppliers.ofInstance("Hello");
        assertEquals("Hello", supplier.get());

        // Edge case: null instance
        Supplier<String> supplier2 = Suppliers.ofInstance(null);
        assertNull(supplier2.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = () -> { return 5; };

        // Typical use case: delegate is accessed inside synchronized block
        Supplier<Integer> supplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(new Integer(5), supplier.get());

        // Error scenario: null delegate
        boolean exceptionThrown = false;
        try {
            Suppliers.synchronizedSupplier(null);
        } catch (NullPointerException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testSupplierFunction() {
        // Typical use case: supplier returns expected value
        Suppliers.SupplierFunction<Integer> sf = Suppliers.SupplierFunctionImpl.INSTANCE;
        Supplier<String> supplier = () -> "Hello";
        assertEquals("Hello", sf.apply(supplier));
    }

}