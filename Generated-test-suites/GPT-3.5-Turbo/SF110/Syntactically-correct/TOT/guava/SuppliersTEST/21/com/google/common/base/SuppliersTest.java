
package com.google.common.base;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose_typical() {
        Function<Integer, String> function = num -> "Number: " + num;
        Supplier<Integer> supplier = () -> 10;

        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);

        assertEquals("Number: 10", composedSupplier.get());
    }

    @Test
    public void testCompose_edge() {
        Function<Integer, String> function = num -> "Number: " + num;
        Supplier<Integer> supplier = () -> null;

        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);

        assertNull(composedSupplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testCompose_nullFunction() {
        Supplier<Integer> supplier = () -> 10;

        Suppliers.compose(null, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testCompose_nullSupplier() {
        Function<Integer, String> function = num -> "Number: " + num;

        Suppliers.compose(function, null);
    }

    @Test
    public void testMemoize_typical() {
        Supplier<String> delegate = () -> "Memoized Value";
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);

        assertEquals("Memoized Value", memoizedSupplier.get());
    }

    @Test
    public void testMemoize_edge() {
        Supplier<String> delegate = () -> null;
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);

        assertNull(memoizedSupplier.get());
    }

    @Test
    public void testOfInstance_typical() {
        String instance = "Test Instance";
        Supplier<String> supplier = Suppliers.ofInstance(instance);

        assertEquals("Test Instance", supplier.get());
    }

    @Test
    public void testOfInstance_null() {
        Supplier<String> supplier = Suppliers.ofInstance(null);

        assertNull(supplier.get());
    }

    @Test
    public void testSynchronizedSupplier_typical() {
        Supplier<Integer> delegate = () -> 20;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);

        assertEquals(new Integer(20), synchronizedSupplier.get());
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testSynchronizedSupplier_nullDelegate() {
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(null);

        assertNull(synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction_typical() {
        Supplier<Integer> supplier = () -> 30;
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();

        assertEquals(new Integer(30), supplierFunction.apply(supplier));
    }

    @Test
    public void testSupplierFunction_nullSupplier() {
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();

        assertNull(supplierFunction.apply(null));
    }

    @Test
    public void testMemoizeWithExpiration_typical() {
        Supplier<String> delegate = () -> "Value";
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 5, TimeUnit.SECONDS);

        assertEquals("Value", memoizedSupplier.get());
    }

    // Test case for checking expiration time
    @Test
    public void testMemoizeWithExpiration_expired() throws InterruptedException {
        Supplier<String> delegate = () -> "Value";
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals("Value", memoizedSupplier.get());

        Thread.sleep(2000);

        // The value should be re-computed as the expiration time has passed
        assertEquals("Value", memoizedSupplier.get());
    }

}

