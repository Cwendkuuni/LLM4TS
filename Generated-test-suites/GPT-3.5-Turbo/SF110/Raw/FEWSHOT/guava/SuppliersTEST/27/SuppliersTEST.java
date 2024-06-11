package com.google.common.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.BeforeClass;
import org.junit.Test;

public class SuppliersTest {
    
    private static Integer instance;
    
    @BeforeClass
    public static void setUp() {
        instance = 10;
    }
    
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
                return instance;
            }
        };
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals("10", composedSupplier.get());
    }
    
    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return instance;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        assertNotNull(memoizedSupplier.get());
    }
    
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return instance;
            }
        };
        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        Supplier<Integer> expiringSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        assertNotNull(expiringSupplier.get());
        Thread.sleep(unit.toMillis(duration + 1));
        assertNull(expiringSupplier.get());
    }
    
    @Test
    public void testOfInstance() {
        Supplier<Integer> instanceSupplier = Suppliers.ofInstance(instance);
        assertNotNull(instanceSupplier.get());
    }
    
    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return instance;
            }
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertNotNull(synchronizedSupplier.get());
    }
    
    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return instance;
            }
        };
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(instance, supplierFunction.apply(supplier));
    }
}