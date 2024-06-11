package net.kencochrane.a4j.DAO;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import net.kencochrane.a4j.DAO.Product;

public class ProductTest {
    
    private Product product;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    
    @Before
    public void setUp() {
        product = new Product();
        System.setOut(new PrintStream(outContent));
    }
    
    @After
    public void tearDown() {
        System.setOut(originalOut);
    }
    
    @Test
    public void testGetProduct() {
        // Typical Use Case
        assertNotNull(product.getProduct("ASIN123", "offer", "page"));
        
        // Edge Cases
        // Test when fileIn is null
        assertNull(product.getProduct("", "", ""));

        // Error Scenario
        // Test when an exception is thrown
        assertNotNull(product.getProduct(null, null, null));
        assertTrue(outContent.toString().contains("Exception"));
    }
}