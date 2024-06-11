package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SuppliersTest {
    @Test
    public void testCompose() {
        //Test1
        Function<Object, String> function1 = new Function<Object, String>() {
            @Override
            public String apply(Object input) {
                return input.toString();
            }
        };
        Supplier<Object> supplier1 = new Supplier<Object>() {
            @Override
            public Object get() {
                return "test";
            }
        };
        Supplier<String> composition1 = Suppliers.compose(function1, supplier1);
        assertEquals("test", composition1.get());

        //Test2: Test with null input
        Supplier<String> composition2 = Suppliers.compose(null, supplier1);
        assertNull(composition2);
        
        //Test3: Test with null supplier
        Supplier<String> composition3 = Suppliers.compose(function1, null);
        assertNull(composition3);
    }

    @Test
    public void testMemoize() {
        //Test1
        Supplier<String> delegate1 = new Supplier<String>() {
            @Override
            public String get() {
                return "test";
            }
        };
        Supplier<String> memoized1 = Suppliers.memoize(delegate1);
        assertEquals("test", memoized1.get());
        
        //Test2: Test memoized supplier
        Supplier<String> memoized2 = Suppliers.memoize(memoized1);
        assertEquals("test", memoized2.get());
        
        //Test3: Test with null delegate
        Supplier<String> memoized3 = Suppliers.memoize(null);
        assertNull(memoized3);
    }

    @Test
    public void testMemoizeWithExpiration() {
        //Test1
        Supplier<String> delegate1 = new Supplier<String>() {
            @Override
            public String get() {
                return "test";
            }
        };
        Supplier<String> memoized1 = Suppliers.memoizeWithExpiration(delegate1, 1, TimeUnit.MILLISECONDS);
        assertEquals("test", memoized1.get());
        
        //Test2: Test with null delegate
        Supplier<String> memoized2 = Suppliers.memoizeWithExpiration(null, 1, TimeUnit.MILLISECONDS);
        assertNull(memoized2);
        
        //Test3: Test with negative duration
        Supplier<String> memoized3 = Suppliers.memoizeWithExpiration(delegate1, -1, TimeUnit.MILLISECONDS);
        assertNull(memoized3);
    }

    @Test
    public void testOfInstance() {
        //Test1
        Supplier<String> instance1 = Suppliers.ofInstance("test");
        assertEquals("test", instance1.get());
        
        //Test2: Test with null input
        Supplier<String> instance2 = Suppliers.ofInstance(null);
        assertNull(instance2);
    }
    
    @Test
    public void testSynchronizedSupplier() {
        //Test1
        Supplier<String> delegate1 = new Supplier<String>() {
            @Override
            public String get() {
                return "test";
            }
        };
        Supplier<String> synchronizedSupplier1 = Suppliers.synchronizedSupplier(delegate1);
        assertEquals("test", synchronizedSupplier1.get());
        
        //Test2: Test with null delegate
        Supplier<String> synchronizedSupplier2 = Suppliers.synchronizedSupplier(null);
        assertNull(synchronizedSupplier2);
    }

    @Test
    public void testSupplierFunction() {
        //Test1
        Supplier<Object> supplier1 = new Supplier<Object>() {
            @Override
            public Object get() {
                return "test";
            }
        };
        Function<Supplier<Object>, Object> supplierFunction1 = Suppliers.supplierFunction();
        assertEquals("test", supplierFunction1.apply(supplier1));
        
        //Test2: Test with null input
        Function<Supplier<Object>, Object> supplierFunction2 = Suppliers.supplierFunction();
        assertNull(supplierFunction2.apply(null));
    }
}