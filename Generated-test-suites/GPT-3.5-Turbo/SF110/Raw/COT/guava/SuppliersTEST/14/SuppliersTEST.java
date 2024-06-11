import org.junit.Test;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import com.google.common.base.*;
import com.google.common.annotations.*;
import static org.junit.Assert.*;

public class SuppliersTest {

    // Basic test for compose() method
    @Test
    public void testCompose() {
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        int result = composedSupplier.get();
        assertEquals(12, result);
    }

    // Basic test for memoize() method
    @Test
    public void testMemoize() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<String> memoizedSupplier = Suppliers.memoize(delegate);
        String result1 = memoizedSupplier.get();
        String result2 = memoizedSupplier.get();
        assertEquals("Hello World!", result1);
        assertEquals(result1, result2);
    }

    // Basic test for memoizeWithExpiration() method
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, 1, TimeUnit.SECONDS);
        String result1 = memoizedSupplier.get();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result2 = memoizedSupplier.get();
        assertEquals("Hello World!", result1);
        assertNotEquals(result1, result2);
    }

    // Basic test for ofInstance() method
    @Test
    public void testOfInstance() {
        Supplier<Integer> instanceSupplier = Suppliers.ofInstance(123);
        int result = instanceSupplier.get();
        assertEquals(123, result);
    }

    // Basic test for synchronizedSupplier() method
    @Test
    public void testSynchronizedSupplier() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        String result = synchronizedSupplier.get();
        assertEquals("Hello World!", result);
    }

    // Basic test for supplierFunction() method
    @Test
    public void testSupplierFunction() {
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Function<Supplier<String>, String> supplierFunction = Suppliers.supplierFunction();
        String result = supplierFunction.apply(supplier);
        assertEquals("Hello World!", result);
    }

    // Test for SupplierComposition.equals() method
    @Test
    public void testSupplierCompositionEquals() {
        Function<String, Integer> function1 = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        Supplier<String> supplier1 = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Function<String, Integer> function2 = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        Supplier<String> supplier2 = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<Integer> composedSupplier1 = Suppliers.compose(function1, supplier1);
        Supplier<Integer> composedSupplier2 = Suppliers.compose(function2, supplier2);

        assertTrue(composedSupplier1.equals(composedSupplier2));
    }

    // Test for SupplierComposition.hashCode() method
    @Test
    public void testSupplierCompositionHashCode() {
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);

        int hashCode = composedSupplier.hashCode();
        assertEquals(792, hashCode);
    }

    // Test for SupplierComposition.toString() method
    @Test
    public void testSupplierCompositionToString() {
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);

        String toString = composedSupplier.toString();
        assertEquals("Suppliers.compose(" + function + ", " + supplier + ")", toString);
    }

    // Test for MemoizingSupplier.equals() method
    @Test
    public void testMemoizingSupplierEquals() {
        Supplier<String> delegate1 = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<String> delegate2 = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<String> memoizingSupplier1 = new Suppliers.MemoizingSupplier<>(delegate1);
        Supplier<String> memoizingSupplier2 = new Suppliers.MemoizingSupplier<>(delegate2);

        assertTrue(memoizingSupplier1.equals(memoizingSupplier2));
    }

    // Test for MemoizingSupplier.hashCode() method
    @Test
    public void testMemoizingSupplierHashCode() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<String> memoizingSupplier = new Suppliers.MemoizingSupplier<>(delegate);

        int hashCode = memoizingSupplier.hashCode();
        assertEquals(4524412, hashCode);
    }

    // Test for MemoizingSupplier.toString() method
    @Test
    public void testMemoizingSupplierToString() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<String> memoizingSupplier = new Suppliers.MemoizingSupplier<>(delegate);

        String toString = memoizingSupplier.toString();
        assertEquals("Suppliers.memoize(" + delegate + ")", toString);
    }

    // Test for ExpiringMemoizingSupplier.equals() method
    @Test
    public void testExpiringMemoizingSupplierEquals() {
        Supplier<String> delegate1 = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<String> delegate2 = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<String> expiringMemoizingSupplier1 = new Suppliers.ExpiringMemoizingSupplier<>(delegate1, 1, TimeUnit.SECONDS);
        Supplier<String> expiringMemoizingSupplier2 = new Suppliers.ExpiringMemoizingSupplier<>(delegate2, 1, TimeUnit.SECONDS);

        assertTrue(expiringMemoizingSupplier1.equals(expiringMemoizingSupplier2));
    }

    // Test for ExpiringMemoizingSupplier.hashCode() method
    @Test
    public void testExpiringMemoizingSupplierHashCode() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<String> expiringMemoizingSupplier = new Suppliers.ExpiringMemoizingSupplier<>(delegate, 1, TimeUnit.SECONDS);

        int hashCode = expiringMemoizingSupplier.hashCode();
        assertEquals(3209284, hashCode);
    }

    // Test for ExpiringMemoizingSupplier.toString() method
    @Test
    public void testExpiringMemoizingSupplierToString() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<String> expiringMemoizingSupplier = new Suppliers.ExpiringMemoizingSupplier<>(delegate, 1, TimeUnit.SECONDS);

        String toString = expiringMemoizingSupplier.toString();
        assertEquals("Suppliers.memoizeWithExpiration(" + delegate + ", 1000000000, NANOS)", toString);
    }

    // Test for SupplierOfInstance.equals() method
    @Test
    public void testSupplierOfInstanceEquals() {
        Supplier<Integer> supplier1 = new Suppliers.SupplierOfInstance<>(123);
        Supplier<Integer> supplier2 = new Suppliers.SupplierOfInstance<>(123);

        assertTrue(supplier1.equals(supplier2));
    }

    // Test for SupplierOfInstance.hashCode() method
    @Test
    public void testSupplierOfInstanceHashCode() {
        Supplier<Integer> supplier = new Suppliers.SupplierOfInstance<>(123);

        int hashCode = supplier.hashCode();
        assertEquals(123, hashCode);
    }

    // Test for SupplierOfInstance.toString() method
    @Test
    public void testSupplierOfInstanceToString() {
        Supplier<Integer> supplier = new Suppliers.SupplierOfInstance<>(123);

        String toString = supplier.toString();
        assertEquals("Suppliers.ofInstance(123)", toString);
    }

    // Test for ThreadSafeSupplier.get() method
    @Test
    public void testThreadSafeSupplierGet() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<String> threadSafeSupplier = new Suppliers.ThreadSafeSupplier<>(delegate);

        String result = threadSafeSupplier.get();
        assertEquals("Hello World!", result);
    }

    // Test for ThreadSafeSupplier.toString() method
    @Test
    public void testThreadSafeSupplierToString() {
        Supplier<String> delegate = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World!";
            }
        };

        Supplier<String> threadSafeSupplier = new Suppliers.ThreadSafeSupplier<>(delegate);

        String toString = threadSafeSupplier.toString();
        assertEquals("Suppliers.synchronizedSupplier(" + delegate + ")", toString);
    }
}