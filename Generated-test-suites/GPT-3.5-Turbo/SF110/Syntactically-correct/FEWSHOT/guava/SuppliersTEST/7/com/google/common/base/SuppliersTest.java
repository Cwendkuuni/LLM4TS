package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SuppliersTest {
    @Test
    public void testCompose() {
        Function<String, Integer> function = new Function<String, Integer>() {
            public Integer apply(String input) {
                return input.length();
            }
        };
        Supplier<String> supplier = new Supplier<String>() {
            public String get() {
                return "Hello";
            }
        };
      
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals(5, composedSupplier.get().intValue());
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
    }
  
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> delegate = new Supplier<String>() {
            public String get() {
                return "Hello";
            }
        };
        
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals("Hello", memoizedSupplier.get());
    }
  
    @Test
    public void testOfInstance() {
        String instance = "Hello";
        Supplier<String> supplier = Suppliers.ofInstance(instance);
        assertEquals("Hello", supplier.get());
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
        Supplier<String> supplier = new Supplier<String>() {
            public String get() {
                return "Hello";
            }
        };
        
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        assertEquals("Hello", supplierFunction.apply(supplier));
    }

}