import static org.junit.Assert.*;
import org.junit.Test;
import java.util.concurrent.*;
import javax.annotation.*;
import java.io.*;
import com.google.common.annotations.*;

public class SuppliersTest {

    @Test
    public void testCompose() {
        // Test case 1
        
        // Test input
        Function<Integer, String> function = (Integer i) -> Integer.toString(i);
        Supplier<Integer> supplier = () -> 5;
  
        // Expected output
        Supplier<String> expectedOutput = () -> function.apply(supplier.get());
  
        // Actual output
        Supplier<String> actualOutput = Suppliers.compose(function, supplier);
  
        // Assert statement
        assertEquals(expectedOutput.get(), actualOutput.get());
        
        // Test case 2
        
        // Test input
        Supplier<Integer> supplier2 = () -> -10;
  
        // Expected output
        Supplier<String> expectedOutput2 = () -> function.apply(supplier2.get());
  
        // Actual output
        Supplier<String> actualOutput2 = Suppliers.compose(function, supplier2);
  
        // Assert statement
        assertEquals(expectedOutput2.get(), actualOutput2.get());
    }
  
    @Test
    public void testMemoize() {
        // Test case 1
        
        // Test input
        Supplier<String> delegate = () -> "Hello";
  
        // Expected output
        Supplier<String> expectedOutput = delegate;
  
        // Actual output
        Supplier<String> actualOutput = Suppliers.memoize(delegate);
  
        // Assert statement
        assertEquals(expectedOutput.get(), actualOutput.get());
        
        // Test case 2
        
        // Test input
        Supplier<String> delegate2 = () -> "World";
  
        // Expected output
        Supplier<String> expectedOutput2 = delegate2;
  
        // Actual output
        Supplier<String> actualOutput2 = Suppliers.memoize(delegate2);
  
        // Assert statement
        assertEquals(expectedOutput2.get(), actualOutput2.get());
    }
  
    @Test
    public void testMemoizeWithExpiration() {
        // Test case 1
        
        // Test input
        Supplier<String> delegate = () -> "Hello";
        long duration = 10;
        TimeUnit unit = TimeUnit.SECONDS;
  
        // Expected output
        Supplier<String> expectedOutput = delegate;
  
        // Actual output
        Supplier<String> actualOutput = Suppliers.memoizeWithExpiration(delegate, duration, unit);
  
        // Assert statement
        assertEquals(expectedOutput.get(), actualOutput.get());
        
        // Test case 2
        
        // Test input
        Supplier<String> delegate2 = () -> "World";
        long duration2 = 5;
  
        // Expected output
        Supplier<String> expectedOutput2 = delegate2;
  
        // Actual output
        Supplier<String> actualOutput2 = Suppliers.memoizeWithExpiration(delegate2, duration2, unit);
  
        // Assert statement
        assertEquals(expectedOutput2.get(), actualOutput2.get());
    }
  
    @Test
    public void testOfInstance() {
        // Test case 1
        
        // Test input
        String instance = "Hello World";
  
        // Expected output
        Supplier<String> expectedOutput = () -> instance;
  
        // Actual output
        Supplier<String> actualOutput = Suppliers.ofInstance(instance);
  
        // Assert statement
        assertEquals(expectedOutput.get(), actualOutput.get());
        
        // Test case 2
        
        // Test input
        String instance2 = "";
  
        // Expected output
        Supplier<String> expectedOutput2 = () -> instance2;
  
        // Actual output
        Supplier<String> actualOutput2 = Suppliers.ofInstance(instance2);
  
        // Assert statement
        assertEquals(expectedOutput2.get(), actualOutput2.get());
    }
  
    @Test
    public void testSynchronizedSupplier() {
        // Test case 1
        
        // Test input
        Supplier<String> delegate = () -> "Hello";
  
        // Expected output
        Supplier<String> expectedOutput = delegate;
  
        // Actual output
        Supplier<String> actualOutput = Suppliers.synchronizedSupplier(delegate);
  
        // Assert statement
        assertEquals(expectedOutput.get(), actualOutput.get());
        
        // Test case 2
        
        // Test input
        Supplier<String> delegate2 = () -> "World";
  
        // Expected output
        Supplier<String> expectedOutput2 = delegate2;
  
        // Actual output
        Supplier<String> actualOutput2 = Suppliers.synchronizedSupplier(delegate2);
  
        // Assert statement
        assertEquals(expectedOutput2.get(), actualOutput2.get());
    }
  
    @Test
    public void testSupplierFunction() {
        // Test case 1
        
        // Test input
        Supplier<String> input = () -> "Hello";
  
        // Expected output
        String expectedOutput = input.get();
  
        // Actual output
        String actualOutput = Suppliers.supplierFunction().apply(input);
  
        // Assert statement
        assertEquals(expectedOutput, actualOutput);
        
        // Test case 2
        
        // Test input
        Supplier<String> input2 = () -> "World";
  
        // Expected output
        String expectedOutput2 = input2.get();
  
        // Actual output
        String actualOutput2 = Suppliers.supplierFunction().apply(input2);
  
        // Assert statement
        assertEquals(expectedOutput2, actualOutput2);
    }
}