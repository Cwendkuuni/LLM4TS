package com.google.common.base;

import org.junit.Test;
import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;
import static org.junit.Assert.*;

public class SuppliersTest {

    // compose method test cases
    @Test
    public void testCompose_typicalCase() {
        Function<Integer, String> function = new Function<Integer, String>() {
            public String apply(Integer input) {
                return input.toString();
            }
        };
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };

        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals("10", composedSupplier.get());
    }

    // memoize method test cases
    @Test
    public void testMemoize_typicalCase() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };

        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(10, (int) memoizedSupplier.get());
    }

    // memoizeWithExpiration method test cases
    @Test
    public void testMemoizeWithExpiration_typicalCase() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };

        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        assertEquals(10, (int) memoizedSupplier.get());
    }

    // ofInstance method test cases
    @Test
    public void testOfInstance_typicalCase() {
        String instance = "test";

        Supplier<String> instanceSupplier = Suppliers.ofInstance(instance);
        assertEquals("test", instanceSupplier.get());
    }

    // synchronizedSupplier method test cases
    @Test
    public void testSynchronizedSupplier_typicalCase() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };

        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertEquals(10, (int) synchronizedSupplier.get());
    }

    // supplierFunction method test cases
    @Test
    public void testSupplierFunction_typicalCase() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };

        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(10, (int) supplierFunction.apply(supplier));
    }

    // SupplierComposition class test cases
    @Test
    public void testSupplierComposition_typicalCase() {
        Function<Integer, String> function = new Function<Integer, String>() {
            public String apply(Integer input) {
                return input.toString();
            }
        };
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };

        Suppliers.SupplierComposition<Integer, String> supplierComposition = new Suppliers.SupplierComposition<>(function, supplier);
        assertEquals("10", supplierComposition.get());
    }

    // MemoizingSupplier class test cases
    @Test
    public void testMemoizingSupplier_typicalCase() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };

        Suppliers.MemoizingSupplier<Integer> memoizingSupplier = new Suppliers.MemoizingSupplier<>(delegate);
        assertEquals(10, (int) memoizingSupplier.get());
    }

    // ExpiringMemoizingSupplier class test cases
    @Test
    public void testExpiringMemoizingSupplier_typicalCase() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };

        Suppliers.ExpiringMemoizingSupplier<Integer> expiringMemoizingSupplier = new Suppliers.ExpiringMemoizingSupplier<>(delegate, 1, TimeUnit.SECONDS);
        assertEquals(10, (int) expiringMemoizingSupplier.get());
    }

    // SupplierOfInstance class test cases
    @Test
    public void testSupplierOfInstance_typicalCase() {
        String instance = "test";

        Suppliers.SupplierOfInstance<String> supplierOfInstance = new Suppliers.SupplierOfInstance<>(instance);
        assertEquals("test", supplierOfInstance.get());
    }

    // ThreadSafeSupplier class test cases
    @Test
    public void testThreadSafeSupplier_typicalCase() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };

        Suppliers.ThreadSafeSupplier<Integer> threadSafeSupplier = new Suppliers.ThreadSafeSupplier<>(supplier);
        assertEquals(10, (int) threadSafeSupplier.get());
    }
}