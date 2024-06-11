package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
        assertNotNull(composedSupplier);
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
        assertNotNull(memoizedSupplier);
        assertEquals("test", memoizedSupplier.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        final Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "test";
            }
        };
        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        Supplier<String> expiringSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        assertNotNull(expiringSupplier);
        assertEquals("test", expiringSupplier.get());
        Thread.sleep(unit.toMillis(duration + 1));
        assertEquals("test", expiringSupplier.get());
    }
    
    @Test
    public void testOfInstance() {
        String instance = "test";
        Supplier<String> instanceSupplier = Suppliers.ofInstance(instance);
        assertNotNull(instanceSupplier);
        assertEquals("test", instanceSupplier.get());
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
        assertNotNull(synchronizedSupplier);
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
        assertNotNull(supplierFunction);
        assertEquals("test", supplierFunction.apply(supplier));
    }
    
    @Test
    public void testSupplierComposition() {
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
        Suppliers.SupplierComposition<String, Integer> supplierComposition = new Suppliers.SupplierComposition<>(function, supplier);
        assertNotNull(supplierComposition);
        assertEquals(Integer.valueOf(4), supplierComposition.get());
    }
    
    @Test
    public void testMemoizingSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "test";
            }
        };
        Suppliers.MemoizingSupplier<String> memoizingSupplier = new Suppliers.MemoizingSupplier<>(delegate);
        assertNotNull(memoizingSupplier);
        assertEquals("test", memoizingSupplier.get());
    }
    
    @Test
    public void testExpiringMemoizingSupplier() throws InterruptedException {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "test";
            }
        };
        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        Suppliers.ExpiringMemoizingSupplier<String> expiringMemoizingSupplier = new Suppliers.ExpiringMemoizingSupplier<>(delegate, duration, unit);
        assertNotNull(expiringMemoizingSupplier);
        assertEquals("test", expiringMemoizingSupplier.get());
        Thread.sleep(unit.toMillis(duration + 1));
        assertEquals("test", expiringMemoizingSupplier.get());
    }
    
    @Test
    public void testSupplierOfInstance() {
        String instance = "test";
        Suppliers.SupplierOfInstance<String> supplierOfInstance = new Suppliers.SupplierOfInstance<>(instance);
        assertNotNull(supplierOfInstance);
        assertEquals("test", supplierOfInstance.get());
    }
    
    @Test
    public void testThreadSafeSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "test";
            }
        };
        Suppliers.ThreadSafeSupplier<String> threadSafeSupplier = new Suppliers.ThreadSafeSupplier<>(delegate);
        assertNotNull(threadSafeSupplier);
        assertEquals("test", threadSafeSupplier.get());
    }
}