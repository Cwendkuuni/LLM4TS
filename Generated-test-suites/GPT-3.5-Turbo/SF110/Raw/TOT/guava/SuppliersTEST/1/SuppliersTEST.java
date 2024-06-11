import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SuppliersTest {

    @Mock
    private Function<String, Integer> mockFunction;

    @Mock
    private Supplier<String> mockSupplier;

    @Mock
    private Supplier<Integer> mockDelegate;

    @Test
    public void testCompose() {
        // Test with valid inputs
        Supplier<Integer> expected = new Suppliers.SupplierComposition<>(mockFunction, mockSupplier);
        Supplier<Integer> result = Suppliers.compose(mockFunction, mockSupplier);
        assertEquals(expected, result);

        // Test with null function
        try {
            Suppliers.compose(null, mockSupplier);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }

        // Test with null supplier
        try {
            Suppliers.compose(mockFunction, null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testMemoize() {
        // Test with delegate of type MemoizingSupplier
        Supplier<Integer> expected = mockDelegate;
        Supplier<Integer> result = Suppliers.memoize(mockDelegate);
        assertEquals(expected, result);

        // Test with delegate of any other type
        Supplier<Integer> delegate = () -> 123;
        result = Suppliers.memoize(delegate);
        assertTrue(result instanceof Suppliers.MemoizingSupplier);
        assertSame(delegate, ((Suppliers.MemoizingSupplier) result).delegate);

        // Test with null delegate
        try {
            Suppliers.memoize(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Test with valid inputs
        Supplier<Integer> expected = new Suppliers.ExpiringMemoizingSupplier<>(mockDelegate, 1000L, TimeUnit.MILLISECONDS);
        Supplier<Integer> result = Suppliers.memoizeWithExpiration(mockDelegate, 1000L, TimeUnit.MILLISECONDS);
        assertEquals(expected, result);

        // Test with delegate of any other type
        Supplier<Integer> delegate = () -> 123;
        result = Suppliers.memoizeWithExpiration(delegate, 1000L, TimeUnit.MILLISECONDS);
        assertTrue(result instanceof Suppliers.ExpiringMemoizingSupplier);
        assertSame(delegate, ((Suppliers.ExpiringMemoizingSupplier) result).delegate);
        assertEquals(1000L, ((Suppliers.ExpiringMemoizingSupplier) result).durationNanos);

        // Test with null delegate
        try {
            Suppliers.memoizeWithExpiration(null, 1000L, TimeUnit.MILLISECONDS);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }

        // Test with negative duration
        try {
            Suppliers.memoizeWithExpiration(mockDelegate, -1000L, TimeUnit.MILLISECONDS);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testOfInstance() {
        // Test with valid instance
        Integer instance = 123;
        Supplier<Integer> expected = new Suppliers.SupplierOfInstance<>(instance);
        Supplier<Integer> result = Suppliers.ofInstance(instance);
        assertEquals(expected, result);

        // Test with null instance
        assertNull(Suppliers.ofInstance(null));
    }

    @Test
    public void testSynchronizedSupplier() {
        // Test with valid inputs
        Supplier<Integer> expected = new Suppliers.ThreadSafeSupplier<>(mockDelegate);
        Supplier<Integer> result = Suppliers.synchronizedSupplier(mockDelegate);
        assertEquals(expected, result);

        // Test with null delegate
        try {
            Suppliers.synchronizedSupplier(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testSupplierFunction() {
        // Test with valid inputs
        Suppliers.SupplierFunction<Integer> expected = Suppliers.SupplierFunctionImpl.INSTANCE;
        Suppliers.SupplierFunction<Integer> result = Suppliers.supplierFunction();
        assertEquals(expected, result);
    }
}