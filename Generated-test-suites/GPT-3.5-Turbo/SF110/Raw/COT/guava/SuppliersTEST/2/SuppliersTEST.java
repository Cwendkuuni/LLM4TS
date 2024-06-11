
package com.google.common.base;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

@GwtCompatible
public final class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return String.valueOf(input);
            }
        };

        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 10;
            }
        };

        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        String result = composedSupplier.get();
        Assert.assertEquals(result, "10");
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 20;
            }
        };

        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        Integer result1 = memoizedSupplier.get();
        Integer result2 = memoizedSupplier.get();
        Assert.assertEquals(result1, 20);
        Assert.assertEquals(result2, 20);
    }

    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 30;
            }
        };

        long duration = 1000;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        Integer result1 = memoizedSupplier.get();
        Thread.sleep(2000);
        Integer result2 = memoizedSupplier.get();
        Assert.assertEquals(result1, 30);
        Assert.assertEquals(result2, 30);
    }

    @Test
    public void testOfInstance() {
        Integer instance = 40;
        Supplier<Integer> supplier = Suppliers.ofInstance(instance);
        Integer result = supplier.get();
        Assert.assertEquals(result, 40);
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 50;
            }
        };

        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        Integer result = synchronizedSupplier.get();
        Assert.assertEquals(result, 50);
    }

    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 60;
            }
        };

        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        Integer result = supplierFunction.apply(supplier);
        Assert.assertEquals(result, 60);
    }

    // Additional test cases for edge cases and exception handling scenarios
    // ...

}
