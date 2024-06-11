import static org.junit.Assert.*;
import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Typical use case
        // Create a function and supplier
        Function<String, Integer> function = s -> s.length();
        Supplier<String> supplier = () -> "Hello";
        
        // Test compose method
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        assertNotNull(composedSupplier);
        assertEquals(Integer.valueOf(5), composedSupplier.get());
        
        // Edge case - function returns null
        Function<String, Integer> nullFunction = s -> null;
        Supplier<String> nullSupplier = () -> "World";
        
        Supplier<Integer> nullComposedSupplier = Suppliers.compose(nullFunction, nullSupplier);
        assertNotNull(nullComposedSupplier);
        assertNull(nullComposedSupplier.get());
        
        // Exception handling - null function or supplier
        assertThrows(NullPointerException.class, () -> Suppliers.compose(null, supplier));
        assertThrows(NullPointerException.class, () -> Suppliers.compose(function, null));
    }

    @Test
    public void testMemoize() {
        // Typical use case
        Supplier<String> delegate = () -> "Hello";
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        
        assertNotNull(memoizedSupplier);
        assertEquals("Hello", memoizedSupplier.get());
        
        // Edge case - memoize twice
        Supplier<String> twiceMemoizedSupplier = Suppliers.memoize(memoizedSupplier);
        assertNotNull(twiceMemoizedSupplier);
        assertEquals("Hello", twiceMemoizedSupplier.get());
        
        // Exception handling - null delegate
        assertThrows(NullPointerException.class, () -> Suppliers.memoize(null));
    }

    // Add more test cases for other methods in the Suppliers class as needed

}