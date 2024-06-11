import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import com.google.common.base.Suppliers.*;

public class SuppliersTest {

    // Test for the compose method
    @Test
    public void testCompose() {
        Function<Integer, String> function = (Integer i) -> {
            if (i == null) {
                return "null";
            } else if (i < 0) {
                return "negative";
            } else if (i > 0) {
                return "positive";
            } else {
                return "zero";
            }
        };

        Supplier<Integer> supplier = () -> {
            return 5;
        };

        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);

        String expected = "positive";
        String actual = composedSupplier.get();

        assertEquals(expected, actual);
    }

    // Test for the memoize method
    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = () -> {
            return 5;
        };

        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);

        int expected = 5;
        int actual = memoizedSupplier.get();

        assertEquals(expected, actual);
    }

    // Test for the memoizeWithExpiration method
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Integer> delegate = () -> {
            return 5;
        };

        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;

        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);

        int expected = 5;
        int actual = memoizedSupplier.get();

        assertEquals(expected, actual);

        Thread.sleep(2000); // Wait for cache to expire

        actual = memoizedSupplier.get();

        assertEquals(expected, actual);
    }

    // Test for the ofInstance method
    @Test
    public void testOfInstance() {
        String instance = "test";

        Supplier<String> supplier = Suppliers.ofInstance(instance);

        String expected = "test";
        String actual = supplier.get();

        assertEquals(expected, actual);
    }

    // Test for the synchronizedSupplier method
    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = () -> {
            return 5;
        };

        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);

        int expected = 5;
        int actual = synchronizedSupplier.get();

        assertEquals(expected, actual);
    }

    // Test for the supplierFunction method
    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = () -> {
            return 5;
        };

        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();

        int expected = 5;
        int actual = supplierFunction.apply(supplier);

        assertEquals(expected, actual);
    }

    // Test for SupplierComposition equals method
    @Test
    public void testSupplierCompositionEquals() {
        Function<Integer, String> function = (Integer i) -> {
            return String.valueOf(i);
        };

        Supplier<Integer> supplier = () -> {
            return 5;
        };

        Supplier<String> composedSupplier1 = Suppliers.compose(function, supplier);
        Supplier<String> composedSupplier2 = Suppliers.compose(function, supplier);

        assertTrue(composedSupplier1.equals(composedSupplier2));
    }

    // Test for SupplierComposition hashCode method
    @Test
    public void testSupplierCompositionHashCode() {
        Function<Integer, String> function = (Integer i) -> {
            return String.valueOf(i);
        };

        Supplier<Integer> supplier = () -> {
            return 5;
        };

        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);

        int expected = composedSupplier.hashCode();
        int actual = composedSupplier.hashCode();

        assertEquals(expected, actual);
    }

    // Test for SupplierComposition toString method
    @Test
    public void testSupplierCompositionToString() {
        Function<Integer, String> function = (Integer i) -> {
            return String.valueOf(i);
        };

        Supplier<Integer> supplier = () -> {
            return 5;
        };

        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);

        String expected = "Suppliers.compose(Integer.toString(), Supplier)";
        String actual = composedSupplier.toString();

        assertEquals(expected, actual);
    }

    // Test for MemoizingSupplier equals method
    @Test
    public void testMemoizingSupplierEquals() {
        Supplier<Integer> delegate1 = () -> {
            return 5;
        };

        Supplier<Integer> delegate2 = () -> {
            return 5;
        };

        Supplier<Integer> memoizedSupplier1 = Suppliers.memoize(delegate1);
        Supplier<Integer> memoizedSupplier2 = Suppliers.memoize(delegate2);

        assertTrue(memoizedSupplier1.equals(memoizedSupplier2));
    }

    // Test for MemoizingSupplier hashCode method
    @Test
    public void testMemoizingSupplierHashCode() {
        Supplier<Integer> delegate = () -> {
            return 5;
        };

        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);

        int expected = memoizedSupplier.hashCode();
        int actual = memoizedSupplier.hashCode();

        assertEquals(expected, actual);
    }

    // Test for MemoizingSupplier toString method
    @Test
    public void testMemoizingSupplierToString() {
        Supplier<Integer> delegate = () -> {
            return 5;
        };

        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);

        String expected = "Suppliers.memoize(Supplier)";
        String actual = memoizedSupplier.toString();

        assertEquals(expected, actual);
    }

    // Test for ExpiringMemoizingSupplier equals method
    @Test
    public void testExpiringMemoizingSupplierEquals() {
        Supplier<Integer> delegate1 = () -> {
            return 5;
        };

        Supplier<Integer> delegate2 = () -> {
            return 5;
        };

        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;

        Supplier<Integer> memoizedSupplier1 = Suppliers.memoizeWithExpiration(delegate1, duration, unit);
        Supplier<Integer> memoizedSupplier2 = Suppliers.memoizeWithExpiration(delegate2, duration, unit);

        assertTrue(memoizedSupplier1.equals(memoizedSupplier2));
    }

    // Test for ExpiringMemoizingSupplier hashCode method
    @Test
    public void testExpiringMemoizingSupplierHashCode() {
        Supplier<Integer> delegate = () -> {
            return 5;
        };

        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;

        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);

        int expected = memoizedSupplier.hashCode();
        int actual = memoizedSupplier.hashCode();

        assertEquals(expected, actual);
    }

    // Test for ExpiringMemoizingSupplier toString method
    @Test
    public void testExpiringMemoizingSupplierToString() {
        Supplier<Integer> delegate = () -> {
            return 5;
        };

        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;

        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);

        String expected = "Suppliers.memoizeWithExpiration(Supplier, 1, SECONDS)";
        String actual = memoizedSupplier.toString();

        assertEquals(expected, actual);
    }

    // Test for SupplierOfInstance equals method
    @Test
    public void testSupplierOfInstanceEquals() {
        String instance = "test";

        Supplier<String> supplier1 = Suppliers.ofInstance(instance);
        Supplier<String> supplier2 = Suppliers.ofInstance(instance);

        assertTrue(supplier1.equals(supplier2));
    }

    // Test for SupplierOfInstance hashCode method
    @Test
    public void testSupplierOfInstanceHashCode() {
        String instance = "test";

        Supplier<String> supplier = Suppliers.ofInstance(instance);

        int expected = supplier.hashCode();
        int actual = supplier.hashCode();

        assertEquals(expected, actual);
    }

    // Test for SupplierOfInstance toString method
    @Test
    public void testSupplierOfInstanceToString() {
        String instance = "test";

        Supplier<String> supplier = Suppliers.ofInstance(instance);

        String expected = "Suppliers.ofInstance(\"test\")";
        String actual = supplier.toString();

        assertEquals(expected, actual);
    }

    // Test for ThreadSafeSupplier equals method
    @Test
    public void testThreadSafeSupplierEquals() {
        Supplier<Integer> delegate1 = () -> {
            return 5;
        };

        Supplier<Integer> delegate2 = () -> {
            return 5;
        };

        Supplier<Integer> synchronizedSupplier1 = Suppliers.synchronizedSupplier(delegate1);
        Supplier<Integer> synchronizedSupplier2 = Suppliers.synchronizedSupplier(delegate2);

        assertTrue(synchronizedSupplier1.equals(synchronizedSupplier2));
    }

    // Test for ThreadSafeSupplier hashCode method
    @Test
    public void testThreadSafeSupplierHashCode() {
        Supplier<Integer> delegate = () -> {
            return 5;
        };

        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);

        int expected = synchronizedSupplier.hashCode();
        int actual = synchronizedSupplier.hashCode();

        assertEquals(expected, actual);
    }

    // Test for ThreadSafeSupplier toString method
    @Test
    public void testThreadSafeSupplierToString() {
        Supplier<Integer> delegate = () -> {
            return 5;
        };

        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);

        String expected = "Suppliers.synchronizedSupplier(Supplier)";
        String actual = synchronizedSupplier.toString();

        assertEquals(expected, actual);
    }

}