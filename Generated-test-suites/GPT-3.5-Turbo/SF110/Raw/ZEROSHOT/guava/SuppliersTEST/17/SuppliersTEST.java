package com.google.common.base;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;

@GwtCompatible
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
                return "Hello World!";
            }
        };
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        Integer result = composedSupplier.get();
        assertEquals(Integer.valueOf(12), result);
    }
    
    @Test
    public void testMemoize() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        String result1 = memoizedSupplier.get();
        String result2 = memoizedSupplier.get();
        assertEquals("Hello World!", result1);
        assertEquals("Hello World!", result2);
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };
        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        Supplier<String> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        String result1 = expiringMemoizingSupplier.get();
        try {
            Thread.sleep(duration * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result2 = expiringMemoizingSupplier.get();
        assertEquals("Hello World!", result1);
        assertNull(result2);
    }
    
    @Test
    public void testOfInstance() {
        String instance = "Hello World!";
        Supplier<String> supplier = Suppliers.ofInstance(instance);
        String result = supplier.get();
        assertEquals("Hello World!", result);
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        String result = synchronizedSupplier.get();
        assertEquals("Hello World!", result);
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        String result = supplierFunction.apply(supplier);
        assertEquals("Hello World!", result);
    }
    
    @Test
    public void testSupplierComposition() {
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };
        Suppliers.SupplierComposition<String, Integer> supplierComposition = new Suppliers.SupplierComposition<>(function, supplier);
        Integer result = supplierComposition.get();
        assertEquals(Integer.valueOf(12), result);
    }
    
    @Test
    public void testMemoizingSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };
        Suppliers.MemoizingSupplier<String> memoizingSupplier = new Suppliers.MemoizingSupplier<>(delegate);
        String result1 = memoizingSupplier.get();
        String result2 = memoizingSupplier.get();
        assertEquals("Hello World!", result1);
        assertEquals("Hello World!", result2);
    }
    
    @Test
    public void testExpiringMemoizingSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };
        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        Suppliers.ExpiringMemoizingSupplier<String> expiringMemoizingSupplier = new Suppliers.ExpiringMemoizingSupplier<>(delegate, duration, unit);
        String result1 = expiringMemoizingSupplier.get();
        try {
            Thread.sleep(duration * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result2 = expiringMemoizingSupplier.get();
        assertEquals("Hello World!", result1);
        assertNull(result2);
    }
    
    @Test
    public void testSupplierOfInstance() {
        String instance = "Hello World!";
        Suppliers.SupplierOfInstance<String> supplierOfInstance = new Suppliers.SupplierOfInstance<>(instance);
        String result = supplierOfInstance.get();
        assertEquals("Hello World!", result);
    }
    
    @Test
    public void testThreadSafeSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };
        Suppliers.ThreadSafeSupplier<String> threadSafeSupplier = new Suppliers.ThreadSafeSupplier<>(delegate);
        String result = threadSafeSupplier.get();
        assertEquals("Hello World!", result);
    }
}