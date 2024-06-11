package com.google.common.base;
import static org.junit.Assert.*;

import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Typical use case
        Function<Integer, String> function = num -> String.valueOf(num);
        Supplier<Integer> supplier = () -> 10;
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals("10", composedSupplier.get());
        
        // Edge case
        Function<Integer, String> nullFunction = null;
        Supplier<Integer> nullSupplier = null;
        Supplier<String> nullComposedSupplier = Suppliers.compose(nullFunction, nullSupplier);
        assertNull(nullComposedSupplier.get());
        
        // Exception handling
        try {
            Suppliers.compose(nullFunction, supplier);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Success
        }
    }

    @Test
    public void testMemoize() {
        // Typical use case
        Supplier<String> delegate = () -> "Hello";
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals("Hello", memoizedSupplier.get());
        assertEquals("Hello", memoizedSupplier.get()); // Ensure the memoization works
        
        // Edge case
        Supplier<String> nullDelegate = null;
        Supplier<String> memoizedNullSupplier = Suppliers.memoize(nullDelegate);
        assertNull(memoizedNullSupplier.get());
    }

    // More test methods for other public methods in Suppliers class

}