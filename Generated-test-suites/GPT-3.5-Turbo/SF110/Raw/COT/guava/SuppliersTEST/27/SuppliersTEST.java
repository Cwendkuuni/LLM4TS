
package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;

@GwtCompatible
public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<String, Integer> function = Integer::parseInt;
        Supplier<String> supplier = () -> "123";
        
        Supplier<Integer> composed = Suppliers.compose(function, supplier);
        assertEquals(Integer.valueOf(123), composed.get());
    }
    
    @Test
    public void testCompose_nullFunction() {
        Supplier<String> supplier = () -> "123";
        
        try {
            Suppliers.compose(null, supplier);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            //pass
        }
    }
    
    @Test
    public void testCompose_nullSupplier() {
        Function<String, Integer> function = Integer::parseInt;
        
        try {
            Suppliers.compose(function, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            //pass
        }
    }
    
    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = () -> 123;
        Supplier<Integer> memoized = Suppliers.memoize(delegate);
        
        assertEquals(Integer.valueOf(123), memoized.get());
    }
    
    @Test
    public void testMemoize_existingMemoizingSupplier() {
        Supplier<Integer> delegate = () -> 123;
        Supplier<Integer> memoized = new Suppliers.MemoizingSupplier<>(delegate);
        
        assertSame(delegate, Suppliers.memoize(memoized));
    }
    
    @Test
    public void testMemoize_nullDelegate() {
        try {
            Suppliers.memoize(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            //pass
        }
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Integer> delegate = () -> 123;
        Supplier<Integer> memoized = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        
        assertEquals(Integer.valueOf(123), memoized.get());
        Thread.sleep(2000);
        assertEquals(Integer.valueOf(123), memoized.get());
    }
    
    @Test
    public void testMemoizeWithExpiration_negativeDuration() {
        Supplier<Integer> delegate = () -> 123;
        
        try {
            Suppliers.memoizeWithExpiration(delegate, -1, TimeUnit.SECONDS);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    
    @Test
    public void testOfInstance() {
        Integer instance = 123;
        Supplier<Integer> supplier = Suppliers.ofInstance(instance);
        
        assertEquals(Integer.valueOf(123), supplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = () -> 123;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        
        assertEquals(Integer.valueOf(123), synchronizedSupplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier_threadSafety() throws InterruptedException {
        Supplier<Integer> delegate = () -> {
            Thread.sleep(1000);
            return 123;
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        
        ExecutorService executor = Executors.newFixedThreadPool(2);
        
        // Run two threads concurrently to test thread safety
        Future<Integer> future1 = executor.submit(synchronizedSupplier::get);
        Future<Integer> future2 = executor.submit(synchronizedSupplier::get);
        
        assertEquals(Integer.valueOf(123), future1.get());
        assertEquals(Integer.valueOf(123), future2.get());
        
        executor.shutdown();
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = () -> 123;
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        
        assertEquals(Integer.valueOf(123), supplierFunction.apply(supplier));
    }
}
