
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Basic functionality test
        Suppliers.compose(new Function<Integer, String>() {
            @Override
            public String apply(Integer input) {
                return input.toString();
            }
        }, new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 1;
            }
        });
    }

    @Test
    public void testMemoize() {
        // Basic functionality test
        Suppliers.memoize(new Supplier<String>() {
            @Override
            public String get() {
                return "memoize";
            }
        });
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Basic functionality test
        Suppliers.memoizeWithExpiration(new Supplier<String>() {
            @Override
            public String get() {
                return "memoize with expiration";
            }
        }, 10, TimeUnit.SECONDS);
    }

    @Test
    public void testOfInstance() {
        // Basic functionality test
        Suppliers.ofInstance("instance");
    }

    @Test
    public void testSynchronizedSupplier() {
        // Basic functionality test
        Suppliers.synchronizedSupplier(new Supplier<String>() {
            @Override
            public String get() {
                return "synchronized supplier";
            }
        });
    }

    @Test
    public void testSupplierFunction() {
        // Basic functionality test
        Suppliers.supplierFunction();
    }

}
