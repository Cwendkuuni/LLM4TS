package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class SuppliersTest {
    @Test
    public void testCompose() {
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "test";
            }
        };

        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);

        assertEquals(4, composedSupplier.get().intValue());
    }

    @Test
    public void testMemoize() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "test";
            }
        };

        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);

        assertTrue(delegate == memoizedSupplier);
    }

    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "test";
            }
        };

        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;

        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);

        assertEquals("test", memoizedSupplier.get());

        Thread.sleep(2000);

        assertEquals("test", memoizedSupplier.get());
    }

    @Test
    public void testOfInstance() {
        String instance = "test";

        Supplier<String> supplier = Suppliers.ofInstance(instance);

        assertEquals("test", supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "test";
            }
        };

        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);

        assertEquals("test", synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "test";
            }
        };

        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();

        assertEquals("test", supplierFunction.apply(supplier));
    }
}