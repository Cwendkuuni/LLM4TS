package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SuppliersTest {
    
    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            public String apply(Integer input) {
                return String.valueOf(input);
            }
        };
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        
        assertEquals("10", composedSupplier.get());
    }
    
    @Test
    public void testMemoize() {
        Supplier<String> delegate = new Supplier<String>() {
            public String get() {
                return "Hello";
            }
        };
        
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        
        assertEquals("Hello", memoizedSupplier.get());
        assertEquals("Hello", memoizedSupplier.get());
        assertEquals("Hello", memoizedSupplier.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<String> delegate = new Supplier<String>() {
            public String get() {
                return "Hello";
            }
        };
        
        long duration = 100;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        
        assertEquals("Hello", memoizedSupplier.get());
        assertEquals("Hello", memoizedSupplier.get());
        
        Thread.sleep(200);
        
        assertEquals("Hello", memoizedSupplier.get());
    }
    
    @Test
    public void testOfInstance() {
        String instance = "Hello";
        
        Supplier<String> instanceSupplier = Suppliers.ofInstance(instance);
        
        assertEquals("Hello", instanceSupplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            public String get() {
                return "Hello";
            }
        };
        
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        
        assertEquals("Hello", synchronizedSupplier.get());
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        
        assertEquals(new Integer(10), supplierFunction.apply(supplier));
    }
}