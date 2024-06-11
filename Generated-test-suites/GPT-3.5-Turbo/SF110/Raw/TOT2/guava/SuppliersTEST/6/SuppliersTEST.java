import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = Object::toString;
        Supplier<Integer> supplier = () -> 10;

        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);

        assertEquals("10", composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Supplier<String> delegate = () -> "memoized";

        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);

        assertEquals("memoized", memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> delegate = () -> "expiring";
        long duration = 2;
        TimeUnit unit = TimeUnit.SECONDS;

        Supplier<String> expiringSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);

        assertEquals("expiring", expiringSupplier.get());
    }

    @Test
    public void testOfInstance() {
        String instance = "instance";

        Supplier<String> instanceSupplier = Suppliers.ofInstance(instance);

        assertEquals("instance", instanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = () -> "synchronized";

        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);

        assertEquals("synchronized", synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = () -> "function";

        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();

        assertEquals("function", supplierFunction.apply(supplier));
    }
}