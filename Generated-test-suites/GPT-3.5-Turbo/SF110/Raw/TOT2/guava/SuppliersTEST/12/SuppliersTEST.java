package com.google.common.base;

import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Test basic functionality
        Supplier<String> supplier = Suppliers.compose(String::toUpperCase, () -> "hello");
        assertEquals("HELLO", supplier.get());
        
        // Test edge case where function returns null
        supplier = Suppliers.compose(input -> null, () -> "world");
        assertNull(supplier.get());
        
        // Test exception handling
        assertThrows(NullPointerException.class, () -> Suppliers.compose(null, () -> "test").get());
        assertThrows(NullPointerException.class, () -> Suppliers.compose(String::toUpperCase, null).get());
    }

    @Test
    public void testMemoize() {
        // Test basic functionality
        Supplier<Integer> supplier = Suppliers.memoize(() -> 42);
        assertEquals(42, supplier.get());
        
        // Test edge case for memoizing null value
        supplier = Suppliers.memoize(() -> null);
        assertNull(supplier.get());
    }

    // Continue testing for other methods...
}