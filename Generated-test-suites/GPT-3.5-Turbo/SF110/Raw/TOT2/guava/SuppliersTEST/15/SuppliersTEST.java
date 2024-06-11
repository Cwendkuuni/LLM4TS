import static org.junit.Assert.*;
import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Typical use case
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 10;
            }
        };

        Supplier<String> composedSupplier = Suppliers.compose(new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return "Number: " + input;
            }
        }, supplier);

        assertEquals("Number: 10", composedSupplier.get());
        
        // Edge case - null function
        try {
           Suppliers.compose(null, supplier);
           fail("Expected NullPointerException");
        } catch (NullPointerException e) {
           // Expected
        }
    }

    @Test
    public void testMemoize() {
        // Typical use case
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };

        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals(5, memoizedSupplier.get().intValue());
        
        // Edge case - memoize twice
        Supplier<Integer> memoizedTwiceSupplier = Suppliers.memoize(memoizedSupplier);
        assertSame(memoizedSupplier, memoizedTwiceSupplier);
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Typical use case
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };

        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.MINUTES);

        assertEquals("Hello", memoizedSupplier.get());
        
        // Edge case - duration less than 0
        try {
            Suppliers.memoizeWithExpiration(supplier, -1, TimeUnit.MINUTES);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOfInstance() {
        // Typical use case
        Supplier<Double> instanceSupplier = Suppliers.ofInstance(3.14);

        assertEquals(3.14, instanceSupplier.get(), 0.0);

        // Edge case - null instance
        Supplier<Object> nullInstanceSupplier = Suppliers.ofInstance(null);
        assertNull(nullInstanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        // Typical use case
        Supplier<String> originalSupplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Original";
            }
        };

        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(originalSupplier);

        assertEquals("Original", synchronizedSupplier.get());
        
        // Edge case - synchronized access
        Supplier<Integer> syncSupplier = new Supplier<Integer>() {
            private int value = 0;

            @Override
            public Integer get() {
                synchronized (this) {
                    value++;
                    return value;
                }
            }
        };

        Supplier<Integer> synchronizedIntegerSupplier = Suppliers.synchronizedSupplier(syncSupplier);
        assertEquals(1, synchronizedIntegerSupplier.get().intValue());
        assertEquals(2, synchronizedIntegerSupplier.get().intValue());
    }

    @Test
    public void testSupplierFunction() {
        // Typical use case
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 100;
            }
        };

        Function<Supplier<Integer>, Integer> function = Suppliers.supplierFunction();

        assertEquals(100, function.apply(supplier).intValue());
    }
}