package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SuppliersTest {
    @Test
    public void testCompose() {
        // Test case 1: function and supplier are not null
        assertEquals("bar", Suppliers.compose(String::toUpperCase, () -> "foo").get());
        
        // Test case 2: function is null
        assertNull(Suppliers.compose(null, () -> "foo"));
        
        // Test case 3: supplier is null
        assertNull(Suppliers.compose(String::toUpperCase, null));
    }
    
    @Test
    public void testMemoize() {
        // Test case 1: delegate is not MemoizingSupplier
        Supplier<String> delegate = () -> "foo";
        assertEquals("foo", Suppliers.memoize(delegate).get());
        
        // Test case 2: delegate is null
        assertNull(Suppliers.memoize(null));
        
        // Test case 3: delegate is MemoizingSupplier
        Supplier<String> memoizingSupplier = Suppliers.memoize(delegate);
        assertEquals("foo", Suppliers.memoize(memoizingSupplier).get());
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        // Test case 1: delegate is not null, duration and unit are positive
        Supplier<String> delegate = () -> "foo";
        assertEquals("foo", Suppliers.memoizeWithExpiration(delegate, 5, TimeUnit.SECONDS).get());
        
        // Test case 2: delegate is null
        assertNull(Suppliers.memoizeWithExpiration(null, 5, TimeUnit.SECONDS));
        
        // Test case 3: duration is 0 and unit is NANOS
        assertEquals("foo", Suppliers.memoizeWithExpiration(delegate, 0, TimeUnit.NANOSECONDS).get());
    }
    
    @Test
    public void testOfInstance() {
        // Test case 1: instance is not null
        assertEquals("foo", Suppliers.ofInstance("foo").get());
        
        // Test case 2: instance is null
        assertNull(Suppliers.ofInstance(null).get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        // Test case 1: delegate is not null
        Supplier<String> delegate = () -> "foo";
        assertEquals("foo", Suppliers.synchronizedSupplier(delegate).get());
        
        // Test case 2: delegate is null
        assertNull(Suppliers.synchronizedSupplier(null).get());
    }
    
    @Test
    public void testSupplierFunction() {
        // Test case 1: input is not null
        Supplier<String> input = () -> "foo";
        assertEquals("foo", Suppliers.supplierFunction().apply(input));
        
        // Test case 2: input is null
        assertNull(Suppliers.supplierFunction().apply(null));
    }
}