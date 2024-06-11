import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.common.base.Suppliers;
import com.google.common.base.Suppliers.SupplierComposition;
import com.google.common.base.Suppliers.MemoizingSupplier;
import com.google.common.base.Suppliers.ExpiringMemoizingSupplier;
import com.google.common.base.Suppliers.SupplierOfInstance;
import com.google.common.base.Suppliers.ThreadSafeSupplier;
import com.google.common.base.Suppliers.SupplierFunctionImpl;

import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import com.google.common.base.Function;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<String, Integer> function = Integer::parseInt;
        Supplier<String> supplier = () -> "5";
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        
        assertEquals(Integer.valueOf(5), composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Supplier<String> delegate = () -> "value";
        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);

        assertEquals("value", memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<String> delegate = () -> "value";
        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);

        assertEquals("value", memoizedSupplier.get());

        Thread.sleep(2000);

        assertEquals("value", memoizedSupplier.get());
    }

    @Test
    public void testOfInstance() {
        String instance = "value";
        Supplier<String> supplier = Suppliers.ofInstance(instance);

        assertEquals("value", supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = () -> "value";
        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);

        assertEquals("value", synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = () -> "value";
        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();

        assertEquals("value", supplierFunction.apply(supplier));
    }
}