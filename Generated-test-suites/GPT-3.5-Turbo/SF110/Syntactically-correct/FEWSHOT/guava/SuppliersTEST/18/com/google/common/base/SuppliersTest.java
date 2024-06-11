
package com.google.common.base;

import static org.junit.Assert.assertEquals;

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
        assertEquals("test", memoizedSupplier.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "test";
            }
        };
        
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals("test", memoizedSupplier.get());
    }
    
    @Test
    public void testOfInstance() {
        Supplier<String> instanceSupplier = Suppliers.ofInstance("test");
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
        assertEquals("test", supplierFunction.apply(supplier));
    }
}
