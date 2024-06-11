package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SuppliersTest {
    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return Integer.toString(input);
            }
        };
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals("5", composedSupplier.get());
    }
    
    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals(Integer.valueOf(5), memoizedSupplier.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 100, TimeUnit.MILLISECONDS);
        assertEquals(Integer.valueOf(5), memoizedSupplier.get());
        Thread.sleep(200);
        assertEquals(Integer.valueOf(5), memoizedSupplier.get());
    }
    
    @Test
    public void testOfInstance() {
        Supplier<String> instanceSupplier = Suppliers.ofInstance("Hello");
        assertEquals("Hello", instanceSupplier.get());
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
        assertEquals(Integer.valueOf(5), synchronizedSupplier.get());
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        assertEquals("Hello", supplierFunction.apply(supplier));
    }
}