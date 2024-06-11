package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

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
        assertEquals(Integer.valueOf(4), composedSupplier.get());
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
        assertNotNull(memoizedSupplier.get());
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
        Supplier<String> memoizedWithExpirationSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        assertNotNull(memoizedWithExpirationSupplier.get());
        Thread.sleep(unit.toMillis(duration));
        assertNull(memoizedWithExpirationSupplier.get());
    }

    @Test
    public void testOfInstance() {
        String instance = "test";
        Supplier<String> instanceSupplier = Suppliers.ofInstance(instance);
        assertEquals(instance, instanceSupplier.get());
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
        assertNotNull(synchronizedSupplier.get());
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