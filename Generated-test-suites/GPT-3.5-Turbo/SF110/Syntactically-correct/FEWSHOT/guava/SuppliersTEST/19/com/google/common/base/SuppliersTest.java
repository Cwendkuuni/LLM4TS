import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

public class SuppliersTest {
    @Test
    public void testCompose() {
        Supplier<Integer> supplier1 = Suppliers.ofInstance(10);
        Function<Integer, String> function = new Function<Integer, String>() {
            public String apply(Integer input) {
                return input.toString();
            }
        };
        Supplier<String> supplier2 = Suppliers.compose(function, supplier1);
        assertEquals("10", supplier2.get());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertNotNull(memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        long duration = 5;
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, duration, TimeUnit.SECONDS);
        assertNotNull(memoizedSupplier.get());
        try {
            Thread.sleep(duration * 1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        assertNull(memoizedSupplier.get());
    }

    @Test
    public void testOfInstance() {
        String instance = "test";
        Supplier<String> supplier = Suppliers.ofInstance(instance);
        assertEquals(instance, supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertNotNull(synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = Suppliers.ofInstance(10);
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(10, (int)supplierFunction.apply(supplier));
    }
}