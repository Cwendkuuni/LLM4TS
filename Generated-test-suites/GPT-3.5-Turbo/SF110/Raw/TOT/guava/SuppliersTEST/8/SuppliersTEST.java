import org.junit.Test;
import static org.junit.Assert.*;

public class SuppliersTest {

  @Test
  public void testCompose() {
    // Typical use case
    Supplier<String> supplier = Suppliers.compose((x) -> "Hello " + x, () -> "World");
    assertEquals("Hello World", supplier.get());

    // Edge case
    supplier = Suppliers.compose((x) -> String.valueOf(x.charAt(0)), () -> "World");
    assertEquals("W", supplier.get());

    // Error scenario
    try {
      Suppliers.compose(null, () -> "World");
      fail("NullPointerException not thrown for null function");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void testMemoize() {
    // Typical use case
    Supplier<String> delegate = () -> "Hello World";
    Supplier<String> supplier = Suppliers.memoize(delegate);
    assertEquals("Hello World", supplier.get());
    assertEquals("Hello World", supplier.get());

    // Edge case
    delegate = () -> "Hello World";
    supplier = Suppliers.memoize(delegate);
    assertSame(supplier.get(), supplier.get()); // Ensure same instance is returned

    // Error scenario
    try {
      Suppliers.memoize(null);
      fail("NullPointerException not thrown for null delegate");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void testMemoizeWithExpiration() throws InterruptedException {
    // Typical use case
    Supplier<String> delegate = () -> "Hello World";
    Supplier<String> supplier = Suppliers.memoizeWithExpiration(delegate, 2, TimeUnit.SECONDS);
    assertEquals("Hello World", supplier.get());
    Thread.sleep(3000); // Wait for expiration
    assertEquals("Hello World", supplier.get());

    // Edge case
    delegate = () -> "Hello World";
    supplier = Suppliers.memoizeWithExpiration(delegate, 0, TimeUnit.SECONDS);
    assertEquals("Hello World", supplier.get());
    Thread.sleep(100); // Wait for expiration
    assertEquals("Hello World", supplier.get());

    // Error scenario
    try {
      Suppliers.memoizeWithExpiration(null, 2, TimeUnit.SECONDS);
      fail("NullPointerException not thrown for null delegate");
    } catch (NullPointerException e) {
      // expected
    }
    try {
      Suppliers.memoizeWithExpiration(delegate, -1, TimeUnit.SECONDS);
      fail("IllegalArgumentException not thrown for negative duration");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }

  @Test
  public void testOfInstance() {
    // Typical use case
    Supplier<String> supplier = Suppliers.ofInstance("Hello World");
    assertEquals("Hello World", supplier.get());

    // Edge case
    supplier = Suppliers.ofInstance(null);
    assertNull(supplier.get());

    // No error scenario
  }

  @Test
  public void testSynchronizedSupplier() {
    // Typical use case
    Supplier<String> delegate = () -> "Hello World";
    Supplier<String> supplier = Suppliers.synchronizedSupplier(delegate);
    assertEquals("Hello World", supplier.get());

    // Edge case
    delegate = () -> "Hello World";
    supplier = Suppliers.synchronizedSupplier(delegate);
    assertSame(supplier.get(), supplier.get()); // Ensure same instance is returned

    // Error scenario
    try {
      Suppliers.synchronizedSupplier(null);
      fail("NullPointerException not thrown for null delegate");
    } catch (NullPointerException e) {
      // expected
    }
  }

  @Test
  public void testSupplierFunction() {
    // Typical use case
    Supplier<String> supplier = () -> "Hello World";
    Function<Supplier<String>, String> function = Suppliers.supplierFunction();
    assertEquals("Hello World", function.apply(supplier));

    // Edge case
    supplier = () -> null;
    function = Suppliers.supplierFunction();
    assertNull(function.apply(supplier));

    // No error scenario
  }
}