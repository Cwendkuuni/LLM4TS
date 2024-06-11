import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Basic test case for compose()
        // Test with a function that adds 1 and a supplier that returns 1, expected result is 2
        int expected = 2;
        int actual = Suppliers.compose(n -> n + 1, () -> 1).get();
        assertEquals(expected, actual);
    }

    @Test
    public void testMemoize() {
        // Basic test case for memoize()
        // Test with a supplier that returns a constant value, expected result is always the same value
        String expected = "hello";
        Supplier<String> supplier = Suppliers.memoize(() -> expected);
        assertEquals(expected, supplier.get());
        assertEquals(expected, supplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Basic test case for memoizeWithExpiration()
        // Test with a supplier that returns a constant value, with an expiration time of 1 second
        // After 1 second, the value should be recomputed, so the expected result is different
        String expected = "hello";
        Supplier<String> supplier = Suppliers.memoizeWithExpiration(() -> expected, 1, TimeUnit.SECONDS);
        assertEquals(expected, supplier.get());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertNotEquals(expected, supplier.get());
    }

    @Test
    public void testOfInstance() {
        // Basic test case for ofInstance()
        // Test with a null instance, expected result is null
        Object expected = null;
        Supplier<Object> supplier = Suppliers.ofInstance(null);
        assertEquals(expected, supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        // Basic test case for synchronizedSupplier()
        // Test with a supplier that returns a counter, make sure the counter is incremented in a thread-safe manner
        int expected = 0;
        Supplier<Integer> supplier = Suppliers.synchronizedSupplier(() -> expected++);
        assertEquals(expected, supplier.get().intValue());
        assertEquals(expected, supplier.get().intValue());
    }

    @Test
    public void testSupplierFunction() {
        // Basic test case for supplierFunction()
        // Test with a supplier that returns a constant value, expected result is the same as the value
        String expected = "hello";
        Supplier<String> supplier = () -> expected;
        assertEquals(expected, Suppliers.supplierFunction().apply(supplier));
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test(expected = NullPointerException.class)
    public void testCompose_NullFunction() {
        // Test with a null function, expect it to throw a NullPointerException
        Suppliers.compose(null, () -> 1);
    }

    @Test(expected = NullPointerException.class)
    public void testCompose_NullSupplier() {
        // Test with a null supplier, expect it to throw a NullPointerException
        Suppliers.compose(n -> n + 1, null);
    }

    @Test(expected = NullPointerException.class)
    public void testMemoize_NullDelegate() {
        // Test with a null delegate, expect it to throw a NullPointerException
        Suppliers.memoize(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithExpiration_NegativeDuration() {
        // Test with a negative duration, expect it to throw an IllegalArgumentException
        Suppliers.memoizeWithExpiration(() -> "hello", -1, TimeUnit.SECONDS);
    }

    @Test(expected = NullPointerException.class)
    public void testOfInstance_NullInstance() {
        // Test with a null instance, expect it to throw a NullPointerException
        Suppliers.ofInstance(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSynchronizedSupplier_NullDelegate() {
        // Test with a null delegate, expect it to throw a NullPointerException
        Suppliers.synchronizedSupplier(null);
    }
}