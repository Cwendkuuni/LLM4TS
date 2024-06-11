package com.google.common.base;

import org.junit.Test;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import java.io.Serializable;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SuppliersTest {
    
    @Test
    public void testCompose() {
        // Test with a function that adds 1 to an Integer and a supplier that provides 1
        Supplier<Integer> supplier = () -> 1;
        Function<Integer, Integer> function = (Integer num) -> num + 1;
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals(2, composedSupplier.get().intValue());

        // Test with a function that converts a String to an Integer and a supplier that provides a String
        supplier = () -> "123";
        Function<String, Integer> stringToIntegerFunction = (String str) -> Integer.parseInt(str);
        composedSupplier = Suppliers.compose(stringToIntegerFunction, supplier);
        assertEquals(123, composedSupplier.get().intValue());
    }
    
    @Test
    public void testMemoize() {
        // Test with a supplier that always returns the same value
        Supplier<Integer> supplier = () -> 1;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(1, memoizedSupplier.get().intValue());
        assertEquals(1, memoizedSupplier.get().intValue());
        assertEquals(1, memoizedSupplier.get().intValue());
        
        // Test with a supplier that returns different values
        int count = 1;
        supplier = () -> count++;
        memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(1, memoizedSupplier.get().intValue());
        assertEquals(1, memoizedSupplier.get().intValue());
        assertEquals(1, memoizedSupplier.get().intValue());
        
        // Test with a supplier that returns null
        supplier = () -> null;
        memoizedSupplier = Suppliers.memoize(supplier);
        assertNull(memoizedSupplier.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        // Test with a supplier that always returns the same value
        Supplier<Integer> supplier = () -> 1;
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        assertEquals(1, memoizedSupplier.get().intValue());
        assertEquals(1, memoizedSupplier.get().intValue());
        assertEquals(1, memoizedSupplier.get().intValue());
        
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertEquals(1, memoizedSupplier.get().intValue());
        
        // Test with a supplier that returns different values
        int count = 1;
        supplier = () -> count++;
        memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        assertEquals(1, memoizedSupplier.get().intValue());
        assertEquals(1, memoizedSupplier.get().intValue());
        assertEquals(1, memoizedSupplier.get().intValue());
        
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        assertEquals(2, memoizedSupplier.get().intValue());
        
        // Test with a supplier that returns null
        supplier = () -> null;
        memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        assertNull(memoizedSupplier.get());
    }
    
    @Test
    public void testOfInstance() {
        // Test with a non-null instance
        Integer instance = 10;
        Supplier<Integer> supplier = Suppliers.ofInstance(instance);
        assertEquals(10, supplier.get().intValue());
        
        // Test with a null instance
        instance = null;
        supplier = Suppliers.ofInstance(instance);
        assertNull(supplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Test with a non-null supplier
        Supplier<Integer> supplier = () -> 1;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertEquals(1, synchronizedSupplier.get().intValue());
        
        // Test with a null supplier
        supplier = () -> null;
        synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertNull(synchronizedSupplier.get());
    }
    
    @Test
    public void testSupplierFunction() {
        // Test with a supplier that returns 1
        Supplier<Integer> supplier = () -> 1;
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(1, supplierFunction.apply(supplier).intValue());
        
        // Test with a supplier that returns null
        supplier = () -> null;
        supplierFunction = Suppliers.supplierFunction();
        assertNull(supplierFunction.apply(supplier));
    }
    
}