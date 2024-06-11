
import com.google.common.base.Suppliers;
import org.junit.Assert;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public class SuppliersTest {

    @Test
    public void testCompose() {
        Function<Integer, String> function = (Integer num) -> {
            if (num % 2 == 0) {
                return "Even";
            } else {
                return "Odd";
            }
        };

        Supplier<Integer> supplier = () -> 4;

        Supplier<String> composedSupplier = Suppliers.compose(function, supplier);

        Assert.assertEquals("Even", composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        Supplier<String> supplier = () -> {
            //Expensive operation
            return "Memoized Value";
        };

        Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);

        Assert.assertEquals("Memoized Value", memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        Supplier<String> supplier = () -> {
            //Expensive operation
            return "Memoized Value";
        };

        Supplier<String> memoizedSupplier = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);

        Assert.assertEquals("Memoized Value", memoizedSupplier.get());

        Thread.sleep(2000); // Wait for cache to expire

        Assert.assertEquals("Memoized Value", memoizedSupplier.get());
    }

    @Test
    public void testOfInstance() {
        Integer number = 10;

        Supplier<Integer> instanceSupplier = Suppliers.ofInstance(number);

        Assert.assertEquals(number, instanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        Supplier<Integer> supplier = () -> 5;

        Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);

        Assert.assertEquals(5, synchronizedSupplier.get().intValue());
    }

    @Test
    public void testSupplierFunction() {
        Supplier<Integer> supplier = () -> 10;

        Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();

        Assert.assertEquals(10, supplierFunction.apply(supplier).intValue());
    }

}
