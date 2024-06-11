import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Test case 1
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        assertEquals(25, Suppliers.compose(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * input;
            }
        }, supplier).get().intValue());

        // Test case 2
        Supplier<String> supplier2 = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        assertEquals("Hello World", Suppliers.compose(new Function<String, String>() {
            @Override
            public String apply(String input) {
                return input + " World";
            }
        }, supplier2).get());
    }

    @Test
    public void testMemoize() {
        // Test case 1
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
        assertEquals(5, memoizedSupplier.get().intValue());
        assertEquals(5, memoizedSupplier.get().intValue());

        // Test case 2
        Supplier<String> supplier2 = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<String> memoizedSupplier2 = Suppliers.memoize(supplier2);
        assertEquals("Hello", memoizedSupplier2.get());
        assertEquals("Hello", memoizedSupplier2.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Test case 1
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1000, TimeUnit.MILLISECONDS);
        assertEquals(5, memoizedSupplier.get().intValue());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(5, memoizedSupplier.get().intValue());

        // Test case 2
        Supplier<String> supplier2 = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<String> memoizedSupplier2 = Suppliers.memoizeWithExpiration(supplier2, 1000, TimeUnit.MILLISECONDS);
        assertEquals("Hello", memoizedSupplier2.get());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals("Hello", memoizedSupplier2.get());
    }

    @Test
    public void testOfInstance() {
        // Test case 1
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> instanceSupplier = Suppliers.ofInstance(supplier.get());
        assertEquals(5, instanceSupplier.get().intValue());

        // Test case 2
        Supplier<String> supplier2 = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<String> instanceSupplier2 = Suppliers.ofInstance(supplier2.get());
        assertEquals("Hello", instanceSupplier2.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        // Test case 1
        Supplier<Integer> supplier = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
        assertEquals(5, synchronizedSupplier.get().intValue());

        // Test case 2
        Supplier<String> supplier2 = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<String> synchronizedSupplier2 = Suppliers.synchronizedSupplier(supplier2);
        assertEquals("Hello", synchronizedSupplier2.get());
    }

    @Test
    public void testSupplierFunction() {
        // Test case 1
        assertEquals("Hello", Suppliers.supplierFunction().apply(new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        }));

        // Test case 2
        assertEquals(5, Suppliers.supplierFunction().apply(new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        }).intValue());
    }
}