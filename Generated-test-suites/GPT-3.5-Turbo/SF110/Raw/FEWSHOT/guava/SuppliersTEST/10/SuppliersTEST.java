package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Object, String> function = mock(Function.class);
        Supplier<Object> supplier = mock(Supplier.class);
        Object input = new Object();
        String expected = "result";
        when(supplier.get()).thenReturn(input);
        when(function.apply(input)).thenReturn(expected);
        
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals(expected, composedSupplier.get());
    }
    
    @Test
    public void testMemoize() {
        Supplier<String> delegate = mock(Supplier.class);
        String expected = "result";
        when(delegate.get()).thenReturn(expected);
        
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals(expected, memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> delegate = mock(Supplier.class);
        String expected = "result";
        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        when(delegate.get()).thenReturn(expected);
        
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        assertEquals(expected, memoizedSupplier.get());
    }

    @Test
    public void testOfInstance() {
        String instance = "test";
        
        Supplier<String> supplier = Suppliers.ofInstance(instance);
        assertEquals(instance, supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = mock(Supplier.class);
        String expected = "result";
        when(delegate.get()).thenReturn(expected);
        
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(expected, synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = mock(Supplier.class);
        String expected = "result";
        when(supplier.get()).thenReturn(expected);
        
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        assertEquals(expected, supplierFunction.apply(supplier));
    }

    @Test
    public void testSupplierComposition() {
        Function<Object, String> function = mock(Function.class);
        Supplier<Object> supplier = mock(Supplier.class);
        Object input = new Object();
        String expected = "result";
        when(supplier.get()).thenReturn(input);
        when(function.apply(input)).thenReturn(expected);
        
        Suppliers.SupplierComposition<Object, String> supplierComposition = new Suppliers.SupplierComposition<>(function, supplier);
        assertEquals(expected, supplierComposition.get());
    }
    
    @Test
    public void testMemoizingSupplier() {
        Supplier<String> delegate = mock(Supplier.class);
        String expected = "result";
        when(delegate.get()).thenReturn(expected);
        
        Suppliers.MemoizingSupplier<String> memoizingSupplier = new Suppliers.MemoizingSupplier<>(delegate);
        assertEquals(expected, memoizingSupplier.get());
    }
    
    @Test
    public void testExpiringMemoizingSupplier() {
        Supplier<String> delegate = mock(Supplier.class);
        String expected = "result";
        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        when(delegate.get()).thenReturn(expected);
        
        Suppliers.ExpiringMemoizingSupplier<String> expiringMemoizingSupplier = new Suppliers.ExpiringMemoizingSupplier<>(delegate, duration, unit);
        assertEquals(expected, expiringMemoizingSupplier.get());
    }
    
    @Test
    public void testSupplierOfInstance() {
        String instance = "test";
        
        Suppliers.SupplierOfInstance<String> supplierOfInstance = new Suppliers.SupplierOfInstance<>(instance);
        assertEquals(instance, supplierOfInstance.get());
    }
    
    @Test
    public void testThreadSafeSupplier() {
        Supplier<String> delegate = mock(Supplier.class);
        String expected = "result";
        when(delegate.get()).thenReturn(expected);
        
        Suppliers.ThreadSafeSupplier<String> threadSafeSupplier = new Suppliers.ThreadSafeSupplier<>(delegate);
        assertEquals(expected, threadSafeSupplier.get());
    }
}