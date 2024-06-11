package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return Integer.toString(input);
            }
        };

        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };

        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);

        assertEquals("5", composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };

        Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals("Hello", memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };

        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);

        assertEquals("Hello", memoizedSupplier.get());
    }

    @Test
    public void testOfInstance() {
        String instance = "Hello";
        Supplier<String> instanceSupplier = Suppliers.ofInstance(instance);

        assertEquals("Hello", instanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };

        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        assertEquals("Hello", synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };

        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();

        assertEquals("Hello", supplierFunction.apply(supplier));
    }
}