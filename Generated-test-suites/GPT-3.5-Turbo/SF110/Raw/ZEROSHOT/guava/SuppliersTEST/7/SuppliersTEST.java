import org.junit.*;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Test case 1
        Function<Integer, String> function = (Integer num) -> num.toString();
        Supplier<Integer> supplier = () -> 10;
        Supplier<String> result = Suppliers.compose(function, supplier);
        assertEquals("10", result.get());
        
        // Test case 2
        Function<String, Integer> function2 = (String word) -> word.length();
        Supplier<String> supplier2 = () -> "Hello";
        Supplier<Integer> result2 = Suppliers.compose(function2, supplier2);
        assertEquals(5, (int)result2.get());
    }

    @Test
    public void testMemoize() {
        // Test case 1
        Supplier<String> delegate = () -> "test";
        Supplier<String> result = Suppliers.memoize(delegate);
        assertEquals("test", result.get());

        // Test case 2
        Supplier<Integer> delegate2 = () -> 5;
        Supplier<Integer> result2 = Suppliers.memoize(delegate2);
        assertEquals(5, (int)result2.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Test case 1
        Supplier<String> delegate = () -> "test";
        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        Supplier<String> result = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        assertEquals("test", result.get());

        // Test case 2
        Supplier<Integer> delegate2 = () -> 5;
        Supplier<Integer> result2 = Suppliers.memoizeWithExpiration(delegate2, duration, unit);
        assertEquals(5, (int)result2.get());
    }

    @Test
    public void testOfInstance() {
        // Test case 1
        Supplier<String> result = Suppliers.ofInstance("test");
        assertEquals("test", result.get());

        // Test case 2
        Supplier<Integer> result2 = Suppliers.ofInstance(5);
        assertEquals(5, (int)result2.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        // Test case 1
        Supplier<String> delegate = () -> "test";
        Supplier<String> result = Suppliers.synchronizedSupplier(delegate);
        assertEquals("test", result.get());

        // Test case 2
        Supplier<Integer> delegate2 = () -> 5;
        Supplier<Integer> result2 = Suppliers.synchronizedSupplier(delegate2);
        assertEquals(5, (int)result2.get());
    }

    @Test
    public void testSupplierFunction() {
        // Test case 1
        Supplier<String> supplier = () -> "test";
        Function<Supplier<String>, String> result = Suppliers.supplierFunction();
        assertEquals("test", result.apply(supplier));

        // Test case 2
        Supplier<Integer> supplier2 = () -> 5;
        Function<Supplier<Integer>, Integer> result2 = Suppliers.supplierFunction();
        assertEquals(5, (int)result2.apply(supplier2));
    }
}