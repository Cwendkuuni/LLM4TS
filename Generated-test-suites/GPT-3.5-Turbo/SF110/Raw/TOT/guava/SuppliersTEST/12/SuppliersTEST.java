import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

public class SuppliersTest {

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void testCompose() {
        // Typical use case
        Function<String, Integer> function = new Function<String, Integer>() {
            @Nullable
            @Override
            public Integer apply(@Nullable String s) {
                return s.length();
            }
        };
        Supplier<String> supplier = new Supplier<String>() {
            @Nullable
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<Integer> resultSupplier = Suppliers.compose(function, supplier);
        Integer expected = 5;
        Integer actual = resultSupplier.get();
        assertEquals(expected, actual);

        // Edge case
        function = new Function<String, Integer>() {
            @Nullable
            @Override
            public Integer apply(@Nullable String s) {
                return (s == null) ? 0 : s.length();
            }
        };
        supplier = new Supplier<String>() {
            @Nullable
            @Override
            public String get() {
                return null;
            }
        };
        resultSupplier = Suppliers.compose(function, supplier);
        expected = 0;
        actual = resultSupplier.get();
        assertEquals(expected, actual);

        // Error scenario
        function = null;
        supplier = new Supplier<String>() {
            @Nullable
            @Override
            public String get() {
                return "Hello";
            }
        };
        try {
            resultSupplier = Suppliers.compose(function, supplier);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testMemoize() {
        // Typical use case
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Nullable
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> resultSupplier = Suppliers.memoize(delegate);
        Integer expected = 5;
        Integer actual = resultSupplier.get();
        assertEquals(expected, actual);

        // Edge case
        delegate = new Supplier<Integer>() {
            @Nullable
            @Override
            public Integer get() {
                return null;
            }
        };
        resultSupplier = Suppliers.memoize(delegate);
        expected = null;
        actual = resultSupplier.get();
        assertEquals(expected, actual);

        // Error scenario
        delegate = null;
        try {
            resultSupplier = Suppliers.memoize(delegate);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Typical use case
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Nullable
            @Override
            public Integer get() {
                return 5;
            }
        };
        long duration = 1;
        TimeUnit unit = TimeUnit.SECONDS;
        Supplier<Integer> resultSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        Integer expected = 5;
        Integer actual = resultSupplier.get();
        assertEquals(expected, actual);

        // Edge case
        delegate = new Supplier<Integer>() {
            @Nullable
            @Override
            public Integer get() {
                return null;
            }
        };
        resultSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        expected = null;
        actual = resultSupplier.get();
        assertEquals(expected, actual);

        // Error scenario
        delegate = null;
        try {
            resultSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
        duration = -1;
        try {
            resultSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testOfInstance() {
        // Typical use case
        Integer instance = 10;
        Supplier<Integer> resultSupplier = Suppliers.ofInstance(instance);
        Integer expected = 10;
        Integer actual = resultSupplier.get();
        assertEquals(expected, actual);

        // Edge case
        instance = null;
        resultSupplier = Suppliers.ofInstance(instance);
        expected = null;
        actual = resultSupplier.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testSynchronizedSupplier() {
        // Typical use case
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Nullable
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> resultSupplier = Suppliers.synchronizedSupplier(delegate);
        Integer expected = 5;
        Integer actual = resultSupplier.get();
        assertEquals(expected, actual);

        // Edge case
        delegate = new Supplier<Integer>() {
            @Nullable
            @Override
            public Integer get() {
                return null;
            }
        };
        resultSupplier = Suppliers.synchronizedSupplier(delegate);
        expected = null;
        actual = resultSupplier.get();
        assertEquals(expected, actual);

        // Error scenario
        delegate = null;
        try {
            resultSupplier = Suppliers.synchronizedSupplier(delegate);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Pass
        }
    }

    @Test
    public void testSupplierFunction() {
        // Typical use case
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Nullable
            @Override
            public Integer get() {
                return 5;
            }
        };
        Function<Supplier<Integer>, Integer> function = Suppliers.supplierFunction();
        Integer expected = 5;
        Integer actual = function.apply(supplier);
        assertEquals(expected, actual);

        // Edge case
        supplier = new Supplier<Integer>() {
            @Nullable
            @Override
            public Integer get() {
                return null;
            }
        };
        function = Suppliers.supplierFunction();
        expected = null;
        actual = function.apply(supplier);
        assertEquals(expected, actual);
    }

}