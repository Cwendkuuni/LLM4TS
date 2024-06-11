
package com.google.common.base;

import org.junit.Test;
import org.junit.Assert;

import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return String.valueOf(input);
            }
        };
        
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 42;
            }
        };
        
        Supplier<String> result = Suppliers.compose(function, supplier);
        Assert.assertEquals("42", result.get());
    }
    
    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            private int value = 42;

            @Override
            public Integer get() {
                return value++;
            }
        };
        
        Supplier<Integer> result = Suppliers.memoize(delegate);
        Assert.assertEquals(Integer.valueOf(42), result.get());
        Assert.assertEquals(Integer.valueOf(42), result.get());
        Assert.assertEquals(Integer.valueOf(42), result.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            private int value = 42;

            @Override
            public Integer get() {
                return value++;
            }
        };
        
        Supplier<Integer> result = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        Assert.assertEquals(Integer.valueOf(42), result.get());
        Thread.sleep(1500);
        Assert.assertEquals(Integer.valueOf(43), result.get());
        Assert.assertEquals(Integer.valueOf(43), result.get());
    }
    
    @Test
    public void testOfInstance() {
        Supplier<Integer> result = Suppliers.ofInstance(42);
        Assert.assertEquals(Integer.valueOf(42), result.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            private int value = 42;

            @Override
            public Integer get() {
                return value++;
            }
        };
        
        Supplier<Integer> result = Suppliers.synchronizedSupplier(delegate);
        Assert.assertEquals(Integer.valueOf(42), result.get());
        Assert.assertEquals(Integer.valueOf(43), result.get());
        Assert.assertEquals(Integer.valueOf(44), result.get());
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            private int value = 42;

            @Override
            public Integer get() {
                return value++;
            }
        };
        
        Function<Supplier<Integer>, Integer> result = Suppliers.supplierFunction();
        Assert.assertEquals(Integer.valueOf(42), result.apply(supplier));
        Assert.assertEquals(Integer.valueOf(43), result.apply(supplier));
        Assert.assertEquals(Integer.valueOf(44), result.apply(supplier));
    }
}
