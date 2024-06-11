
import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {
   
   @Test
   public void testCompose() {
      Function<Integer, String> function = (Integer num) -> num.toString();
      Supplier<Integer> supplier = () -> 5;
      
      Supplier<String> supplierResult = Suppliers.compose(function, supplier);
      assertEquals("5", supplierResult.get());
   }
   
   @Test
   public void testMemoize() {
      Supplier<Integer> delegate = () -> 5;
      
      Supplier<Integer> supplierResult = Suppliers.memoize(delegate);
      assertEquals(5, (int) supplierResult.get());
   }
   
   @Test
   public void testMemoizeWithExpiration() {
      Supplier<Integer> delegate = () -> 5;
      long duration = 1;
      TimeUnit unit = TimeUnit.SECONDS;
      
      Supplier<Integer> supplierResult = Suppliers.memoizeWithExpiration(delegate, duration, unit);
      assertEquals(5, (int) supplierResult.get());
   }
   
   @Test
   public void testOfInstance() {
      String instance = "test";
      
      Supplier<String> supplierResult = Suppliers.ofInstance(instance);
      assertEquals(instance, supplierResult.get());
   }
   
   @Test
   public void testSynchronizedSupplier() {
      Supplier<Integer> delegate = () -> 5;
      
      Supplier<Integer> supplierResult = Suppliers.synchronizedSupplier(delegate);
      assertEquals(5, (int) supplierResult.get());
   }
   
   @Test
   public void testSupplierFunction() {
      Supplier<Integer> supplier = () -> 5;
      
      Function<Supplier<Integer>, Integer> function = Suppliers.supplierFunction();
      assertEquals(5, (int) function.apply(supplier));
   }
}
