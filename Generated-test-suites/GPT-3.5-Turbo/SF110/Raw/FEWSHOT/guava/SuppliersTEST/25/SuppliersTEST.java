package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SuppliersTest {
    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            public String apply(Integer input) {
                return input.toString();
            }
        };
        
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        
        Supplier<String> result = Suppliers.compose(function, supplier);
        
        assertEquals("10", result.get());
    }
    
    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        
        Supplier<Integer> result = Suppliers.memoize(delegate);
        
        assertEquals(Integer.valueOf(10), result.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        
        Supplier<Integer> result = Suppliers.memoizeWithExpiration(delegate, 5, TimeUnit.SECONDS);
        
        assertEquals(Integer.valueOf(10), result.get());
    }
    
    @Test
    public void testOfInstance() {
        Integer instance = 10;
        
        Supplier<Integer> result = Suppliers.ofInstance(instance);
        
        assertEquals(Integer.valueOf(10), result.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        
        Supplier<Integer> result = Suppliers.synchronizedSupplier(delegate);
        
        assertEquals(Integer.valueOf(10), result.get());
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };
        
        Function<Supplier<Integer>, Integer> result = Suppliers.supplierFunction();
        
        assertEquals(Integer.valueOf(10), result.apply(supplier));
    }
}