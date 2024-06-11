package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Suppliers;
import com.google.common.base.Suppliers.SupplierComposition;
import com.google.common.base.Suppliers.MemoizingSupplier;
import com.google.common.base.Suppliers.ExpiringMemoizingSupplier;
import com.google.common.base.Suppliers.SupplierOfInstance;
import com.google.common.base.Suppliers.ThreadSafeSupplier;
import com.google.common.base.Suppliers.SupplierFunctionImpl;

@GwtCompatible
public class SuppliersTest {
    
    @Test
    public void testCompose() {
        Supplier<String> supplier1 = Suppliers.ofInstance("foo");
        Supplier<Integer> supplier2 = Suppliers.ofInstance(5);

        Supplier<String> composedSupplier = Suppliers.compose(new Function<Integer, String>() {
            public String apply(Integer input) {
                return String.valueOf(input);
            }
        }, supplier2);

        assertEquals("5", composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Supplier<String> supplier = Suppliers.ofInstance("foo");
        Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);

        assertEquals("foo", memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<String> supplier = Suppliers.ofInstance("foo");
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);

        assertEquals("foo", memoizedSupplier.get());

        Thread.sleep(2000);

        assertEquals("foo", memoizedSupplier.get());
    }

    @Test
    public void testOfInstance() {
        String instance = "foo";
        Supplier<String> supplier = Suppliers.ofInstance(instance);

        assertEquals("foo", supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> supplier = Suppliers.ofInstance("foo");
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        assertEquals("foo", synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        // Test with Supplier<String>
        Supplier<String> supplier = Suppliers.ofInstance("foo");
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();

        assertEquals("foo", supplierFunction.apply(supplier));
    }
    
    @Test
    public void testSupplierComposition() {
        Supplier<String> supplier1 = Suppliers.ofInstance("foo");
        Supplier<String> supplier2 = Suppliers.ofInstance("bar");

        Supplier<String> composedSupplier = new SupplierComposition<String, String>(new Function<String, String>() {
            public String apply(String input) {
                return input.toUpperCase();
            }
        }, supplier2);

        assertEquals("BAR", composedSupplier.get());
    }
    
    @Test
    public void testMemoizingSupplier() {
        Supplier<String> supplier = Suppliers.ofInstance("foo");
        MemoizingSupplier<String> memoizingSupplier = new MemoizingSupplier<String>(supplier);

        assertEquals("foo", memoizingSupplier.get());
    }
    
    @Test
    public void testExpiringMemoizingSupplier() throws InterruptedException {
        Supplier<String> supplier = Suppliers.ofInstance("foo");
        ExpiringMemoizingSupplier<String> expiringMemoizingSupplier = new ExpiringMemoizingSupplier<String>(supplier, 1, TimeUnit.SECONDS);

        assertEquals("foo", expiringMemoizingSupplier.get());

        Thread.sleep(2000);

        assertEquals("foo", expiringMemoizingSupplier.get());
    }
    
    @Test
    public void testSupplierOfInstance() {
        String instance = "foo";
        Supplier<String> supplier = new SupplierOfInstance<String>(instance);

        assertEquals("foo", supplier.get());
    }
    
    @Test
    public void testThreadSafeSupplier() {
        Supplier<String> supplier = Suppliers.ofInstance("foo");
        ThreadSafeSupplier<String> threadSafeSupplier = new ThreadSafeSupplier<String>(supplier);

        assertEquals("foo", threadSafeSupplier.get());
    }
}