import org.junit.Assert;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import com.google.common.base.*;

public class SuppliersTest {

    @Test
    public void testCompose_givenValidFunctionAndSupplier_shouldReturnCorrectValue() {
        // Arrange
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

        // Act
        Supplier<Integer> composedSupplier = Suppliers.compose(function, supplier);
        Integer result = composedSupplier.get();

        // Assert
        Assert.assertEquals(new Integer(123), result);
    }

    @Test
    public void testMemoize_givenSupplierReturnsSameValueMultipleTimes_shouldReturnCachedValue() {
        // Arrange
        Supplier<Integer> delegate = new Supplier<Integer>() {
            private int count = 0;

            @Override
            public Integer get() {
                count++;
                return count;
            }
        };

        // Act
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(delegate);
        Integer result1 = memoizedSupplier.get();
        Integer result2 = memoizedSupplier.get();
        Integer result3 = memoizedSupplier.get();

        // Assert
        Assert.assertEquals(new Integer(1), result1);
        Assert.assertEquals(new Integer(1), result2);
        Assert.assertEquals(new Integer(1), result3);
    }

    @Test
    public void testMemoizeWithExpiration_givenSupplierWithExpiryAndValidDuration_shouldReturnCorrectValue() throws InterruptedException {
        // Arrange
        Supplier<Integer> delegate = new Supplier<Integer>() {
            @Override
            public Integer get() {
                return 123;
            }
        };
        long duration = 1000L;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        // Act
        Supplier<Integer> memoizedSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);
        Integer result1 = memoizedSupplier.get();
        Thread.sleep(duration + 100); // Wait for cache to expire
        Integer result2 = memoizedSupplier.get();

        // Assert
        Assert.assertEquals(new Integer(123), result1);
        Assert.assertEquals(new Integer(123), result2);
    }

    @Test
    public void testOfInstance_givenNullInstance_shouldReturnNullValue() {
        // Arrange
        Object instance = null;

        // Act
        Supplier<Object> instanceSupplier = Suppliers.ofInstance(instance);
        Object result = instanceSupplier.get();

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testSynchronizedSupplier_givenSynchronizedDelegateSupplier_shouldReturnCorrectValue() {
        // Arrange
        Supplier<Integer> delegate = new Supplier<Integer>() {
            private int count = 0;

            @Override
            public synchronized Integer get() {
                count++;
                return count;
            }
        };

        // Act
        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
        Integer result1 = synchronizedSupplier.get();
        Integer result2 = synchronizedSupplier.get();
        Integer result3 = synchronizedSupplier.get();

        // Assert
        Assert.assertEquals(new Integer(1), result1);
        Assert.assertEquals(new Integer(2), result2);
        Assert.assertEquals(new Integer(3), result3);
    }

    @Test
    public void testSupplierFunction_givenSupplier_shouldReturnCorrectValue() {
        // Arrange
        Supplier<String> supplier = new Supplier<String>() {
            @Override
            public String get() {
                return "Test";
            }
        };

        // Act
        Function<Supplier<String>, String> function = Suppliers.supplierFunction();
        String result = function.apply(supplier);

        // Assert
        Assert.assertEquals("Test", result);
    }
}