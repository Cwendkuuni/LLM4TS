import org.junit.Test;
import static org.junit.Assert.*;

public class Expert1SuppliersTest {

    @Test
    public void testCompose() {
        Supplier<T> supplier = () -> 5;
        Function<? super Integer, Integer> function = input -> input * 2;
        Supplier<T> composedSupplier = Suppliers.compose(function, supplier);
        
        assertEquals(10, composedSupplier.get());
    }
    
}