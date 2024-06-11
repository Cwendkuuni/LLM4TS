import static org.junit.Assert.*;
import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Test when function and supplier are not null
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "hello world";
            }
        };
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals(new Integer(11), composedSupplier.get());

        // Test when function is null
        try {
            Suppliers.compose(null, supplier);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }

        // Test when supplier is null
        try {
            Suppliers.compose(function, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }
    }

    @Test
    public void testMemoize() {
        // Test when delegate is not a MemoizingSupplier
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 10;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals(new Integer(10), memoizedSupplier.get());

        // Test when delegate is a MemoizingSupplier
        Supplier<Integer> memoizedDelegate = Suppliers.memoize(delegate);
        memoizedSupplier = Suppliers.memoize(memoizedDelegate);
        assertEquals(new Integer(10), memoizedSupplier.get());

        // Test when delegate is null
        try {
            Suppliers.memoize(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Test when delegate, duration, and unit are valid
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 10;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals(new Integer(10), memoizedSupplier.get());

        // Test when delegate is null
        try {
            Suppliers.memoizeWithExpiration(null, 1, TimeUnit.SECONDS);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }

        // Test when duration is not positive
        try {
            Suppliers.memoizeWithExpiration(delegate, 0, TimeUnit.SECONDS);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testOfInstance() {
        // Test when instance is not null
        String instance = "hello";
        Supplier<String> supplier = Suppliers.ofInstance(instance);
        assertEquals("hello", supplier.get());

        // Test when instance is null
        instance = null;
        supplier = Suppliers.ofInstance(instance);
        assertNull(supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        // Test when delegate is not null
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 10;
            }
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(new Integer(10), synchronizedSupplier.get());

        // Test when delegate is null
        try {
            Suppliers.synchronizedSupplier(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        }
    }

    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 10;
            }
        };
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(new Integer(10), supplierFunction.apply(supplier));
    }
}