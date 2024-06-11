
import com.google.common.base.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, Integer> function = new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * 2;
            }
        };

        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };

        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        assertEquals(Integer.valueOf(10), composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        assertEquals(Integer.valueOf(5), memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };

        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        assertEquals(Integer.valueOf(5), memoizedSupplier.get());
        Thread.sleep(2000);
        assertEquals(Integer.valueOf(5), memoizedSupplier.get());
    }

    @Test
    public void testOfInstance() {
        String instance = "Hello";
        Supplier<String> supplier = Suppliers.ofInstance(instance);
        assertEquals(instance, supplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        assertEquals(Integer.valueOf(5), synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
        assertEquals(Integer.valueOf(5), supplierFunction.apply(supplier));
    }
}
