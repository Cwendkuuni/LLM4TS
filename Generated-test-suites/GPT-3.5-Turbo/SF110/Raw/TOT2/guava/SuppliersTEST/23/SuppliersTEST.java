import static org.junit.Assert.*;

import org.junit.Test;

public class SuppliersTest {

    @Test
    public void composeTest() {
        // Test normal functionality of compose method
        Function<Integer, String> function = new Function<Integer, String>() {
            public String apply(Integer input) {
                return String.valueOf(input);
            }
        };
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 10;
            }
        };

        Supplier<String> result = Suppliers.compose(function, supplier);
        assertEquals("10", result.get());
    }

    @Test
    public void memoizeTest() {
        // Test memoize method with non-memoized supplier
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 5;
            }
        };

        Supplier<Integer> result = Suppliers.memoize(supplier);
        assertEquals(Integer.valueOf(5), result.get());

        // Test memoize method with already memoized supplier
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(Integer.valueOf(5), memoizedSupplier.get());
    }

    @Test
    public void memoizeWithExpirationTest() {
        // Test memoizeWithExpiration method
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 20;
            }
        };

        Supplier<Integer> result = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
        assertEquals(Integer.valueOf(20), result.get());
    }

    @Test
    public void ofInstanceTest() {
        // Test ofInstance method
        Supplier<String> result = Suppliers.ofInstance("test");
        assertEquals("test", result.get());
    }

    @Test
    public void synchronizedSupplierTest() {
        // Test synchronizedSupplier method
        Supplier<Integer> supplier = new Supplier<Integer>() {
            public Integer get() {
                return 15;
            }
        };

        Supplier<Integer> result = Suppliers.synchronizedSupplier(supplier);
        assertEquals(Integer.valueOf(15), result.get());
    }

    @Test
    public void supplierFunctionTest() {
        // Test supplierFunction method
        Supplier<String> supplier = new Supplier<String>() {
            public String get() {
                return "Function Test";
            }
        };

        Function<Supplier<String>, String> result = Suppliers.supplierFunction();
        assertEquals("Function Test", result.apply(supplier));
    }

    // Additional test cases for edge cases and exception handling scenarios can be added here

}