import com.google.common.annotations.Beta;
import com.google.common.base.Suppliers;
import org.junit.Test;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SuppliersTest {

    //Test case for Suppliers.compose()
    @Test
    public void testCompose() {
        //Typical use case
        Suppliers.compose(Function.identity(), () -> "Hello").get(); //Expected: "Hello"

        //Edge case
        Suppliers.compose((Function<String, Integer>) Integer::valueOf, () -> "123").get(); //Expected: 123

        //Error scenario
        Suppliers.compose(Function.identity(), null); //Expected: NullPointerException
    }

    //Test case for Suppliers.memoize()
    @Test
    public void testMemoize() {
        //Typical use case
        Suppliers.memoize(() -> "Hello").get(); //Expected: "Hello"

        //Edge case
        Suppliers.memoize(() -> {
            throw new RuntimeException("Error");
        }); //Expected: RuntimeException

        //Error scenario
        Suppliers.memoize(null); //Expected: NullPointerException
    }

    //Test case for Suppliers.memoizeWithExpiration()
    @Test
    public void testMemoizeWithExpiration() throws InterruptedException {
        //Typical use case
        Suppliers.memoizeWithExpiration(() -> "Hello", 1, TimeUnit.SECONDS).get(); //Expected: "Hello"

        //Edge case
        Suppliers.memoizeWithExpiration(() -> "Hello", 0, TimeUnit.SECONDS); //Expected: IllegalArgumentException

        //Error scenario
        Suppliers.memoizeWithExpiration(null, 1, TimeUnit.SECONDS); //Expected: NullPointerException
    }

    //Test case for Suppliers.ofInstance()
    @Test
    public void testOfInstance() {
        //Typical use case
        Suppliers.ofInstance("Hello").get(); //Expected: "Hello"

        //Edge case
        Suppliers.ofInstance(null).get(); //Expected: null
    }

    //Test case for Suppliers.synchronizedSupplier()
    @Test
    public void testSynchronizedSupplier() {
        //Typical use case
        Suppliers.synchronizedSupplier(() -> "Hello").get(); //Expected: "Hello"

        //Edge case
        Suppliers.synchronizedSupplier(() -> {
            throw new RuntimeException("Error");
        }); //Expected: RuntimeException

        //Error scenario
        Suppliers.synchronizedSupplier(null); //Expected: NullPointerException
    }

    //Test case for Suppliers.supplierFunction()
    @Test
    @Beta
    public void testSupplierFunction() {
        SupplierFunction<String> sf = Suppliers.supplierFunction();
        Supplier<String> supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn("Hello");
        assertEquals("Hello", sf.apply(supplier));
    }
}