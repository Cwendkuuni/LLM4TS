package com.google.common.base;

import org.junit.Test;
import org.junit.Assert;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            public String apply(Integer i) {
                return Integer.toString(i);
            }
        };

        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };

        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);

        String result = composedSupplier.get();

        Assert.assertEquals("10", result);
    }

    @Test
    public void testMemoize() {
        Supplier<String> delegate = new Supplier<String>() {
            public String get() {
                return "memoize";
            }
        };

        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);

        String result1 = memoizedSupplier.get();
        String result2 = memoizedSupplier.get();

        Assert.assertEquals("memoize", result1);
        Assert.assertEquals("memoize", result2);
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> delegate = new Supplier<String>() {
            public String get() {
                return "memoizeWithExpiration";
            }
        };

        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;

        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);

        String result1 = memoizedSupplier.get();
        String result2 = memoizedSupplier.get();

        Assert.assertEquals("memoizeWithExpiration", result1);
        Assert.assertEquals("memoizeWithExpiration", result2);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String result3 = memoizedSupplier.get();

        Assert.assertNotEquals("memoizeWithExpiration", result3);
    }

    @Test
    public void testOfInstance() {
        String instance = "ofInstance";

        Supplier<String> supplier = Suppliers.ofInstance(instance);

        String result = supplier.get();

        Assert.assertEquals("ofInstance", result);
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            public String get() {
                return "synchronizedSupplier";
            }
        };

        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);

        String result = synchronizedSupplier.get();

        Assert.assertEquals("synchronizedSupplier", result);
    }

    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = new Supplier<String>() {
            public String get() {
                return "supplierFunction";
            }
        };

        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();

        String result = supplierFunction.apply(supplier);

        Assert.assertEquals("supplierFunction", result);
    }
}