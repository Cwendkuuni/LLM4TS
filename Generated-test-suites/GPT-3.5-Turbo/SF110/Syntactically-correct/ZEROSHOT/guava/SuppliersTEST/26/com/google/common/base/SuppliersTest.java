
package com.google.common.base;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Object, Integer> function = new Function<Object, Integer>() {
            public Integer apply(Object o) {
                return o.hashCode();
            }
        };
        Supplier<Object> supplier = new Supplier<Object>() {
            public Object get() {
                return new Object();
            }
        };
        
        Supplier<Integer> composed = Suppliers.compose(function, supplier);
        assertNotNull(composed);
    }

    @Test
    public void testMemoize() {
        final Object obj = new Object();
        Supplier<Object> delegate = new Supplier<Object>() {
            public Object get() {
                return obj;
            }
        };
        
        Supplier<Object> memoized = Suppliers.memoize(delegate);
        assertNotNull(memoized);
        assertSame(obj, memoized.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        final Object obj = new Object();
        Supplier<Object> delegate = new Supplier<Object>() {
            public Object get() {
                return obj;
            }
        };
        
        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        Supplier<Object> memoized = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        assertNotNull(memoized);
        assertSame(obj, memoized.get());
    }

    @Test
    public void testOfInstance() {
        Object obj = new Object();
        
        Supplier<Object> instance = Suppliers.ofInstance(obj);
        assertNotNull(instance);
        assertSame(obj, instance.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        final Object obj = new Object();
        Supplier<Object> delegate = new Supplier<Object>() {
            public Object get() {
                return obj;
            }
        };
        
        Supplier<Object> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertNotNull(synchronizedSupplier);
        assertSame(obj, synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        SupplierFunction<Object> supplierFunction = Suppliers.supplierFunction();
        assertNotNull(supplierFunction);
    }
}
