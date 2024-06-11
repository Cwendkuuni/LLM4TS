import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Test typical use case
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        Function<Integer, String> function = (num) -> String.valueOf(num + 5);
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals("15", composedSupplier.get());

        // Test edge case when supplier returns null
        supplier = Suppliers.ofInstance(null);
        composedSupplier = Suppliers.compose(function, supplier);
        assertNull(composedSupplier.get());

        // Test error scenario when function throws exception
        supplier = Suppliers.ofInstance(10);
        function = (num) -> {
            throw new IllegalArgumentException();
        };
        composedSupplier = Suppliers.compose(function, supplier);
        try {
            composedSupplier.get();
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testMemoize() {
        // Test typical use case
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(Integer.valueOf(10), memoizedSupplier.get());
        assertEquals(Integer.valueOf(10), memoizedSupplier.get());

        // Test edge case when supplier returns null
        supplier = Suppliers.ofInstance(null);
        memoizedSupplier = Suppliers.memoize(supplier);
        assertNull(memoizedSupplier.get());
        assertNull(memoizedSupplier.get());

        // Test error scenario when supplier throws exception
        supplier = () -> {
            throw new IllegalArgumentException();
        };
        memoizedSupplier = Suppliers.memoize(supplier);
        try {
            memoizedSupplier.get();
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
        try {
            memoizedSupplier.get();
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Test typical use case
        Supplier<Integer> supplier = () -> 10;
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        assertEquals(Integer.valueOf(10), memoizedSupplier.get());
        assertEquals(Integer.valueOf(10), memoizedSupplier.get());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Ignore
        }
        assertEquals(Integer.valueOf(10), memoizedSupplier.get());

        // Test edge case when supplier returns null
        supplier = Suppliers.ofInstance(null);
        memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        assertNull(memoizedSupplier.get());
        assertNull(memoizedSupplier.get());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Ignore
        }
        assertNull(memoizedSupplier.get());

        // Test error scenario when supplier throws exception
        supplier = () -> {
            throw new IllegalArgumentException();
        };
        memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        try {
            memoizedSupplier.get();
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
        try {
            memoizedSupplier.get();
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // Ignore
        }
        try {
            memoizedSupplier.get();
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testOfInstance() {
        // Test typical use case
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        assertEquals(Integer.valueOf(10), supplier.get());

        // Test edge case when instance is null
        supplier = Suppliers.ofInstance(null);
        assertNull(supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        // Test typical use case
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertEquals(Integer.valueOf(10), synchronizedSupplier.get());

        // Test edge case when supplier returns null
        supplier = Suppliers.ofInstance(null);
        synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertNull(synchronizedSupplier.get());

        // Test error scenario when supplier throws exception
        supplier = () -> {
            throw new IllegalArgumentException();
        };
        synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        try {
            synchronizedSupplier.get();
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testSupplierFunction() {
        // Test typical use case
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(Integer.valueOf(10), supplierFunction.apply(supplier));

        // Test edge case when supplier returns null
        supplier = Suppliers.ofInstance(null);
        assertEquals(null, supplierFunction.apply(supplier));
    }

}