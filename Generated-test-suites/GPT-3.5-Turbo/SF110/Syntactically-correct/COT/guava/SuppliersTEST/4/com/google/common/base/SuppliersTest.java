
import org.junit.Assert;
import org.junit.Test;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Arrange
        Function<Object, Object> function = Object::toString;
        Supplier<Object> supplier = () -> new Object();

        // Act
        Supplier<Object> composedSupplier = Suppliers.compose(function, supplier);

        // Assert
        Assert.assertNotNull(composedSupplier);
        Assert.assertEquals(supplier.get().toString(), composedSupplier.get());
    }

    @Test
    public void testMemoize() {
        // Arrange
        Supplier<Object> delegate = () -> new Object();

        // Act
        Supplier<Object> memoizedSupplier = Suppliers.memoize(delegate);

        // Assert
        Assert.assertNotNull(memoizedSupplier);
        Assert.assertEquals(delegate.get(), memoizedSupplier.get());
    }

    @Test
    public void testMemoizeWithExpiration() {
        // Arrange
        Supplier<Object> delegate = () -> new Object();
        long duration = 1000;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        // Act
        Supplier<Object> expiringSupplier = Suppliers.memoizeWithExpiration(delegate, duration, unit);

        // Sleep for a duration longer than the expiration time
        try {
            Thread.sleep(duration * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Assert
        Assert.assertNotNull(expiringSupplier);
        Assert.assertEquals(delegate.get(), expiringSupplier.get());
    }

    @Test
    public void testOfInstance() {
        // Arrange
        Object instance = new Object();

        // Act
        Supplier<Object> instanceSupplier = Suppliers.ofInstance(instance);

        // Assert
        Assert.assertNotNull(instanceSupplier);
        Assert.assertEquals(instance, instanceSupplier.get());
    }

    @Test
    public void testSynchronizedSupplier() {
        // Arrange
        Supplier<Object> delegate = () -> new Object();

        // Act
        Supplier<Object> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);

        // Assert
        Assert.assertNotNull(synchronizedSupplier);
        Assert.assertEquals(delegate.get(), synchronizedSupplier.get());
    }

    @Test
    public void testSupplierFunction() {
        // Arrange
        Supplier<Object> supplier = () -> new Object();

        // Act
        Object result = Suppliers.supplierFunction().apply(supplier);

        // Assert
        Assert.assertNotNull(result);
        Assert.assertEquals(supplier.get(), result);
    }

    // Additional edge cases and exception handling scenarios can be tested here

}
