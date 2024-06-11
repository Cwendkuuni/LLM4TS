package com.google.common.base;

import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {
    
    @Test
    public void testCompose() {
        Suppliers.SupplierComposition<Integer, String> composition = new Suppliers.SupplierComposition<>(new Function<Integer,String>() {
            public String apply(Integer input) {
                return input.toString();
            }
        }, new Suppliers.Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        });
        assertEquals("10", composition.get());
    }
    
    @Test
    public void testMemoize() {
        Suppliers.MemoizingSupplier<Integer> memoizingSupplier = new Suppliers.MemoizingSupplier<>(new Suppliers.Supplier<Integer>() {
            public Integer get() {
                return 5;
            }
        });
        assertEquals(Integer.valueOf(5), memoizingSupplier.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        Suppliers.ExpiringMemoizingSupplier<Integer> expiringSupplier = new Suppliers.ExpiringMemoizingSupplier<>(new Suppliers.Supplier<Integer>() {
            public Integer get() {
                return 5;
            }
        }, 10, TimeUnit.SECONDS);
        assertEquals(Integer.valueOf(5), expiringSupplier.get());
    }
    
    @Test
    public void testOfInstance() {
        Suppliers.SupplierOfInstance<String> instanceSupplier = new Suppliers.SupplierOfInstance<>("Hello");
        assertEquals("Hello", instanceSupplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Suppliers.ThreadSafeSupplier<Integer> synchronizedSupplier = new Suppliers.ThreadSafeSupplier<>(new Suppliers.Supplier<Integer>() {
            public Integer get() {
                return 5;
            }
        });
        assertEquals(Integer.valueOf(5), synchronizedSupplier.get());
    }
    
    @Test
    public void testSupplierFunction() {
        Suppliers.SupplierFunction<String> supplierFunction = Suppliers.SupplierFunctionImpl.INSTANCE;
        Suppliers.Supplier<String> supplier = new Suppliers.Supplier<String>() {
            public String get() {
                return "Hello";
            }
        };
        assertEquals("Hello", supplierFunction.apply(supplier));
    }
}