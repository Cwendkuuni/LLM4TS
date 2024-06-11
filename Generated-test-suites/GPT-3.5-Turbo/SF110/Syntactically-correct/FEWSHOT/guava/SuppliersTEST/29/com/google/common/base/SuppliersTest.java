package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SuppliersTest {
    @Test
    public void testCompose() {
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.parseInt(input);
            }
        };
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "10";
            }
        };
        assertEquals(10, Suppliers.compose(function, supplier).get().intValue());
    }
    
    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> memoized = Suppliers.memoize(delegate);
        assertEquals(5, memoized.get().intValue());
        assertEquals(5, memoized.get().intValue());
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> memoized = Suppliers.memoizeWithExpiration(delegate, 100, TimeUnit.MILLISECONDS);
        assertEquals(5, memoized.get().intValue());
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(5, memoized.get().intValue());
    }
    
    @Test
    public void testOfInstance() {
        String instance = "Test";
        Supplier<String> supplier = Suppliers.ofInstance(instance);
        assertEquals(instance, supplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(5, synchronizedSupplier.get().intValue());
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Test";
            }
        };
        assertEquals("Test", Suppliers.supplierFunction().apply(supplier));
    }
}