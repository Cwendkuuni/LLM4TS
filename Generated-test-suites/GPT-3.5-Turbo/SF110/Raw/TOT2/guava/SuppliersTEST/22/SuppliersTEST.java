import static org.junit.Assert.*;
import org.junit.Test;
import com.google.common.base.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<String, Integer> function = new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return Integer.parseInt(input);
            }
        };

        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "123";
            }
        };

        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        assertNotNull(composedSupplier);
        assertEquals(Integer.valueOf(123), composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 456;
            }
        };

        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        assertNotNull(memoizedSupplier);
        assertEquals(Integer.valueOf(456), memoizedSupplier.get());
    }

    // Add additional test cases for remaining methods based on edge cases and exception scenarios

}