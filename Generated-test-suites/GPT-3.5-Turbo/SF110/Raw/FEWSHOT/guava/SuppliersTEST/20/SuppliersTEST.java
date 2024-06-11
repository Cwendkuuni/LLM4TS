package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import org.junit.Test;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Suppliers.ExpiringMemoizingSupplier;
import com.google.common.base.Suppliers.MemoizingSupplier;
import com.google.common.base.Suppliers.SupplierComposition;
import com.google.common.base.Suppliers.SupplierOfInstance;
import com.google.common.base.Suppliers.ThreadSafeSupplier;

@GwtCompatible
public class SuppliersTest {

    // Test compose method
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
        assertEquals("10", composedSupplier.get());
    }

    // Test memoize method
    @Test
    public void testMemoize() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "foo";
            }
        };

        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        assertTrue(memoizedSupplier instanceof MemoizingSupplier);
        assertEquals("foo", memoizedSupplier.get());
    }

    // Test memoizeWithExpiration method
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "foo";
            }
        };

        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;

        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        assertTrue(memoizedSupplier instanceof ExpiringMemoizingSupplier);
        assertEquals("foo", memoizedSupplier.get());

        Thread.sleep(2000); // Wait for expiration

        assertEquals("foo", memoizedSupplier.get());
    }

    // Test ofInstance method
    @Test
    public void testOfInstance() {
        String instance = "foo";
        Supplier<String> instanceSupplier = Suppliers.ofInstance(instance);
        assertTrue(instanceSupplier instanceof SupplierOfInstance);
        assertEquals(instance, instanceSupplier.get());
    }

    // Test synchronizedSupplier method
    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "foo";
            }
        };

        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertTrue(synchronizedSupplier instanceof ThreadSafeSupplier);
        assertEquals("foo", synchronizedSupplier.get());
    }

    // Test supplierFunction method
    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "foo";
            }
        };
        
        SupplierFunction<String> supplierFunction = Suppliers.supplierFunction();
        
        assertEquals("foo", supplierFunction.apply(supplier));
    }

}