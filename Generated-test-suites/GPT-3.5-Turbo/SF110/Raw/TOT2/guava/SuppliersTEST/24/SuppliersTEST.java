import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.*;

public class SuppliersTest {

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
                return 5;
            }
        };
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals("5", composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 10;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals(10, (int) memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals("Hello", memoizedSupplier.get());
    }

    @Test
    public void testOfInstance() {
        Supplier<String> instanceSupplier = Suppliers.ofInstance("Test");
        assertEquals("Test", instanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 8;
            }
        };
        Supplier<Integer> syncSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(8, (int) syncSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "SupplierFunctionTest";
            }
        };
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        assertEquals("SupplierFunctionTest", supplierFunction.apply(supplier));
    }

    // Additional test cases for edge cases and error scenarios can be added here

}