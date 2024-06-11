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
            public Integer apply(String str) {
                return Integer.parseInt(str);
            }
        };

        Supplier<String> supplier = new Supplier<String>() {
            public String get() {
                return "123";
            }
        };

        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        assertNotNull(composedSupplier);
        assertEquals(new Integer(123), composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Supplier<String> delegate = new Supplier<String>() {
            public String get() {
                return "foo";
            }
        };

        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        assertNotNull(memoizedSupplier);
        assertEquals("foo", memoizedSupplier.get());
        assertEquals("foo", memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> delegate = new Supplier<String>() {
            public String get() {
                return "bar";
            }
        };

        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertNotNull(memoizedSupplier);
        assertEquals("bar", memoizedSupplier.get());
        assertEquals("bar", memoizedSupplier.get());
    }

    @Test
    public void testOfInstance() {
        Supplier<String> instanceSupplier = Suppliers.ofInstance("baz");
        assertNotNull(instanceSupplier);
        assertEquals("baz", instanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            public Integer get() {
                return 456;
            }
        };

        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertNotNull(synchronizedSupplier);
        assertEquals(new Integer(456), synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 789;
            }
        };

        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertNotNull(supplierFunction);
        assertEquals(new Integer(789), supplierFunction.apply(supplier));
    }
}