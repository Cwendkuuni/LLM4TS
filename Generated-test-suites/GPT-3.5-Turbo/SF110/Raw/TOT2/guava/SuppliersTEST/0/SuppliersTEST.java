package com.google.common.base;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = (n) -> String.valueOf(n);
        Supplier<Integer> supplier = () -> 5;
        Supplier<String> composed = Suppliers.compose(function, supplier);

        assertNotNull(composed);
        assertEquals("5", composed.get());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = () -> 10;
        Supplier<Integer> memoized = Suppliers.memoize(delegate);

        assertNotNull(memoized);
        assertEquals(Integer.valueOf(10), memoized.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Integer> delegate = () -> 15;
        Supplier<Integer> memoized = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);

        assertNotNull(memoized);
        assertEquals(Integer.valueOf(15), memoized.get());
    }

    @Test
    public void testOfInstance() {
        String instance = "sample";
        Supplier<String> supplier = Suppliers.ofInstance(instance);

        assertNotNull(supplier);
        assertEquals("sample", supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = () -> 20;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);

        assertNotNull(synchronizedSupplier);
        assertEquals(Integer.valueOf(20), synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = () -> 25;
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();

        assertNotNull(supplierFunction);
        assertEquals(Integer.valueOf(25), supplierFunction.apply(supplier));
    }

    // Additional edge cases and exception handling scenarios can be added here

}