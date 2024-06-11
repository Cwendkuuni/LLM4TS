import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Test case 1
        Function<? super Integer, String> function1 = new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return "Number is " + input;
            }
        };
        Supplier<Integer> supplier1 = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 10;
            }
        };
        Supplier<String> composeResult1 = Suppliers.compose(function1, supplier1);
        assertEquals("Number is 10", composeResult1.get());
        
        // Test case 2
        Function<? super String, Integer> function2 = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        };
        Supplier<String> supplier2 = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello World";
            }
        };
        Supplier<Integer> composeResult2 = Suppliers.compose(function2, supplier2);
        assertEquals(11, (int)composeResult2.get());
    }

    @Test
    public void testMemoize() {
        // Test case 1
        Supplier<Integer> delegate1 = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> memoizeResult1 = Suppliers.memoize(delegate1);
        assertEquals(5, (int)memoizeResult1.get());
        
        // Test case 2
        Supplier<String> delegate2 = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<String> memoizeResult2 = Suppliers.memoize(delegate2);
        assertEquals("Hello", memoizeResult2.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Test case 1
        Supplier<Integer> delegate1 = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> memoizeResult1 = Suppliers.memoizeWithExpiration(delegate1, 1, TimeUnit.MINUTES);
        assertEquals(5, (int)memoizeResult1.get());
        
        // Test case 2
        Supplier<String> delegate2 = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<String> memoizeResult2 = Suppliers.memoizeWithExpiration(delegate2, 10, TimeUnit.SECONDS);
        assertEquals("Hello", memoizeResult2.get());
    }

    @Test
    public void testOfInstance() {
        // Test case 1
        Supplier<Integer> instanceSupplier1 = Suppliers.ofInstance(5);
        assertEquals(5, (int)instanceSupplier1.get());
        
        // Test case 2
        Supplier<String> instanceSupplier2 = Suppliers.ofInstance("Hello");
        assertEquals("Hello", instanceSupplier2.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        // Test case 1
        Supplier<Integer> delegate1 = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Supplier<Integer> synchronizedSupplier1 = Suppliers.synchronizedSupplier(delegate1);
        assertEquals(5, (int)synchronizedSupplier1.get());
        
        // Test case 2
        Supplier<String> delegate2 = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Supplier<String> synchronizedSupplier2 = Suppliers.synchronizedSupplier(delegate2);
        assertEquals("Hello", synchronizedSupplier2.get());
    }

    @Test
    public void testSupplierFunction() {
        // Test case 1
        Supplier<Integer> supplier1 = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 5;
            }
        };
        Function<Supplier<Integer>, Integer> supplierFunction1 = Suppliers.supplierFunction();
        assertEquals(5, (int)supplierFunction1.apply(supplier1));
        
        // Test case 2
        Supplier<String> supplier2 = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };
        Function<Supplier<String>, String> supplierFunction2 = Suppliers.supplierFunction();
        assertEquals("Hello", supplierFunction2.apply(supplier2));
    }
}