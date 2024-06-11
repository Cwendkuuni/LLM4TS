
import org.junit.*;
import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import java.io.Serializable;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Suppliers.*;

@GwtCompatible
public class SuppliersTest {

    @Test
    public void testComposeTypicalCase() {
        Function<Integer, String> function = (num) -> Integer.toString(num);
        Supplier<Integer> supplier = () -> 10;
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals("10", composedSupplier.get());
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullFunction() {
        Supplier<Integer> supplier = () -> 10;
        Supplier<String> composedSupplier = Suppliers.compose(null, supplier);
    }

    @Test(expected = NullPointerException.class)
    public void testComposeNullSupplier() {
        Function<Integer, String> function = (num) -> Integer.toString(num);
        Supplier<String> composedSupplier = Suppliers.compose(function, null);
    }

    @Test
    public void testMemoizeTypicalCase() {
        Supplier<Integer> delegate = () -> 10;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals(Integer.valueOf(10), memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpirationTypicalCase() throws InterruptedException {
        Supplier<Integer> delegate = () -> 10;
        long duration = 2;
        TimeUnit unit = TimeUnit.SECONDS;
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        assertEquals(Integer.valueOf(10), memoizedSupplier.get());
        Thread.sleep(3000);
        assertEquals(Integer.valueOf(10), memoizedSupplier.get());
    }

    @Test
    public void testOfInstanceTypicalCase() {
        Integer instance = 10;
        Supplier<Integer> instanceSupplier = Suppliers.ofInstance(instance);
        assertEquals(Integer.valueOf(10), instanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplierTypicalCase() {
        Supplier<Integer> delegate = () -> 10;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(Integer.valueOf(10), synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunctionTypicalCase() {
        Supplier<Integer> supplier = () -> 10;
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(Integer.valueOf(10), supplierFunction.apply(supplier));
    }

}
