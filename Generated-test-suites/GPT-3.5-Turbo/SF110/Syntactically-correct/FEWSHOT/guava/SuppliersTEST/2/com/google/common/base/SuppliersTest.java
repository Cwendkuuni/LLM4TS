package com.google.common.base;
import static org.junit.Assert.*;

import org.junit.Test;

public class SuppliersTest {
    private static final String STRING = "test";
    private static final Integer INTEGER = Integer.valueOf(42);
    
    @Test
    public void testCompose() {
        Supplier<String> supplier1 = Suppliers.ofInstance(STRING);
        Supplier<Integer> supplier2 = Suppliers.ofInstance(INTEGER);
        Function<String,Integer> function = new Function<String,Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier1);
        assertEquals(Integer.valueOf(4), composedSupplier.get());
        
        composedSupplier = Suppliers.compose(function, supplier2);
        assertEquals(Integer.valueOf(2), composedSupplier.get());
    }
    
    @Test
    public void testMemoize() {
        Supplier<String> delegate = Suppliers.ofInstance(STRING);
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals(STRING, memoizedSupplier.get());
        
        delegate = new Supplier<String>() {
            @Override
            public String get() {
                return STRING + " " + STRING;
            }
        };
        memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals(STRING + " " + STRING, memoizedSupplier.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> delegate = Suppliers.ofInstance(STRING);
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals(STRING, memoizedSupplier.get());
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertEquals(STRING, memoizedSupplier.get());
        
        delegate = new Supplier<String>() {
            @Override
            public String get() {
                return STRING + " " + STRING;
            }
        };
        memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals(STRING + " " + STRING, memoizedSupplier.get());
        
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertEquals(STRING + " " + STRING, memoizedSupplier.get());
    }
    
    @Test
    public void testOfInstance() {
        Supplier<String> supplier = Suppliers.ofInstance(STRING);
        assertEquals(STRING, supplier.get());
        
        supplier = Suppliers.ofInstance(null);
        assertNull(supplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = Suppliers.ofInstance(STRING);
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(STRING, synchronizedSupplier.get());
        
        delegate = new Supplier<String>() {
            @Override
            public String get() {
                return STRING + " " + STRING;
            }
        };
        synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(STRING + " " + STRING, synchronizedSupplier.get());
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = Suppliers.ofInstance(STRING);
        Function<Supplier<String>, String> function = Suppliers.supplierFunction();
        assertEquals(STRING, function.apply(supplier));
    }
}