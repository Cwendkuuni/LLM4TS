
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = Object::toString;
        Supplier<Integer> supplier = () -> 5;
        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals("5", composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = () -> 5;
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals(5, memoizedSupplier.get().intValue());
    }

    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Integer> delegate = () -> 5;
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals(5, memoizedSupplier.get().intValue());
        Thread.sleep(2000);
        assertEquals(5, memoizedSupplier.get().intValue());
    }

    @Test
    public void testOfInstance() {
        String instance = "test";
        Supplier<String> instanceSupplier = Suppliers.ofInstance(instance);
        assertEquals("test", instanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = () -> 5;
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(5, synchronizedSupplier.get().intValue());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = () -> 5;
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(5, supplierFunction.apply(supplier).intValue());
    }
}
