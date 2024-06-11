import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return input.toString();
            }
        };
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<String> result = Suppliers.compose(function, supplier);
        assertEquals("5", result.get());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 10;
            }
        };
        Supplier<Integer> result = Suppliers.memoize(delegate);
        assertEquals(10, result.get().intValue());
    }

    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 15;
            }
        };
        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        Supplier<Integer> result = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        assertEquals(15, result.get().intValue());
    }

    @Test
    public void testOfInstance() {
        String instance = "test";
        Supplier<String> result = Suppliers.ofInstance(instance);
        assertEquals("test", result.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 20;
            }
        };
        Supplier<Integer> result = Suppliers.synchronizedSupplier(delegate);
        assertEquals(20, result.get().intValue());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 25;
            }
        };
        Function<Supplier<Integer>, Integer> result = Suppliers.supplierFunction();
        assertEquals(25, result.apply(supplier).intValue());
    }

    @Test
    public void testSupplierComposition() {
        Function<Integer, String> function = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return input.toString();
            }
        };
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 30;
            }
        };

        Suppliers.SupplierComposition<Integer, String> result = new Suppliers.SupplierComposition<>(function, supplier);
        assertEquals("30", result.get());
    }

    @Test
    public void testMemoizingSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 35;
            }
        };

        Suppliers.MemoizingSupplier<Integer> result = new Suppliers.MemoizingSupplier<>(delegate);
        assertEquals(35, result.get().intValue());
    }

    @Test
    public void testExpiringMemoizingSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 40;
            }
        };
        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;

        Suppliers.ExpiringMemoizingSupplier<Integer> result = new Suppliers.ExpiringMemoizingSupplier<>(delegate, duration, unit);
        assertEquals(40, result.get().intValue());
    }

    @Test
    public void testSupplierOfInstance() {
        String instance = "test";
        Suppliers.SupplierOfInstance<String> result = new Suppliers.SupplierOfInstance<>(instance);
        assertEquals("test", result.get());
    }

    @Test
    public void testThreadSafeSupplier() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 45;
            }
        };

        Suppliers.ThreadSafeSupplier<Integer> result = new Suppliers.ThreadSafeSupplier<>(delegate);
        assertEquals(45, result.get().intValue());
    }
}