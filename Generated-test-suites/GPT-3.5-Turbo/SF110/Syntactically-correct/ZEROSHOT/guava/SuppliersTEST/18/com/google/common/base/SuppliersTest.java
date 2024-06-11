package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {
    // Test cases for compose() method
    @Test
    public void testCompose() {
        // Test case 1
        Function<Integer, String> function = new Function<Integer, String>() {
            public String apply(Integer input) {
                return String.valueOf(input);
            }
        };
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 5;
            }
        };
        Supplier<String> composed = Suppliers.compose(function, supplier);
        assertEquals("5", composed.get());
        
        // Test case 2
        function = new Function<Integer, String>() {
            public String apply(Integer input) {
                return String.valueOf(input + 1);
            }
        };
        supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        composed = Suppliers.compose(function, supplier);
        assertEquals("11", composed.get());
    }

    // Test case for memoize() method
    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> memoized = Suppliers.memoize(supplier);
        assertEquals(5, (int) memoized.get());
    }

    // Test case for memoizeWithExpiration() method
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> memoized = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        assertEquals(5, (int) memoized.get());
    }

    // Test case for ofInstance() method
    @Test
    public void testOfInstance() {
        Supplier<Integer> instance = Suppliers.ofInstance(5);
        assertEquals(5, (int) instance.get());
    }

    // Test case for synchronizedSupplier() method
    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertEquals(5, (int) synchronizedSupplier.get());
    }

    // Test case for supplierFunction() method
    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 5;
            }
        };
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(5, (int) supplierFunction.apply(supplier));
    }
}