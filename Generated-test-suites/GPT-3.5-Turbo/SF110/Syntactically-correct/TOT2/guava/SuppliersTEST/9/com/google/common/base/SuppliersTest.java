package com.google.common.base;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Test typical functionality
        Supplier<String> supplier = () -> "Hello";
        Function<String, String> function = s -> s + " World";
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals("Hello World", composedSupplier.get());
        
        // Test edge case: function returns null
        Function<String, String> nullFunction = s -> null;
        Supplier<String> nullSupplier = Suppliers.compose(nullFunction, supplier);
        assertNull(nullSupplier.get());
        
        // Test exception handling: null function
        try {
            Suppliers.compose(null, supplier);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test exception handling: null supplier
        try {
            Suppliers.compose(function, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Test other public methods in a similar manner
}