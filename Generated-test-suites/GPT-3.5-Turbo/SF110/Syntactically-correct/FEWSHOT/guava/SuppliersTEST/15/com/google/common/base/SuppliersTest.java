package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
                return "hello";
            }
        };
        
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        assertNotNull(composedSupplier);
        assertEquals(Integer.valueOf(5), composedSupplier.get());
    }
    
    @Test
    public void testMemoize() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "hello";
            }
        };
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        assertNotNull(memoizedSupplier);
        assertEquals("hello", memoizedSupplier.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "hello";
            }
        };
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertNotNull(memoizedSupplier);
        assertEquals("hello", memoizedSupplier.get());
        Thread.sleep(1000);
        assertNull(memoizedSupplier.get());
    }
    
    @Test
    public void testOfInstance() {
        Supplier<String> instanceSupplier = Suppliers.ofInstance("hello");
        assertNotNull(instanceSupplier);
        assertEquals("hello", instanceSupplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "hello";
            }
        };
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertNotNull(synchronizedSupplier);
        assertEquals("hello", synchronizedSupplier.get());
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "hello";
            }
        };
        
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        assertNotNull(supplierFunction);
        assertEquals("hello", supplierFunction.apply(supplier));
    }
}