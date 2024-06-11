import java.io.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonCLITest {
    
    @Test
    public void testMain() {
        // Test case to check if the main method runs without any exception
        String[] args = {"property_file", "interval"};
        LagoonCLI.main(args);
    }
    
    @Test
    public void testGetProperty() throws LagoonException {
        // Test case to check if getProperty method returns the expected value
        String name = "targetURL";
        String expected = "http://example.com";
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty(name, expected);
        String actual = LagoonCLI.getProperty(name);
        assertEquals(expected, actual);
    }
    
    @Test(expected = LagoonException.class)
    public void testGetPropertyWithMissingProperty() throws LagoonException {
        // Test case to check if getProperty method throws LagoonException when a property is missing
        String name = "targetURL";
        LagoonCLI.properties = new Properties();
        LagoonCLI.getProperty(name);
    }
    
    @Test(expected = LagoonException.class)
    public void testGetPropertyWithEmptyValue() throws LagoonException {
        // Test case to check if getProperty method throws LagoonException when a property has empty value
        String name = "targetURL";
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty(name, "");
        LagoonCLI.getProperty(name);
    }
    
    @Test
    public void testShowTimeWithMillis() {
        // Test case to check if showTime method displays time in milliseconds correctly
        long ms = 5000;
        String expected = "in 5000 ms";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(ms);
        String actual = outContent.toString().trim();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testShowTimeWithSeconds() {
        // Test case to check if showTime method displays time in seconds correctly
        long ms = 15000;
        String expected = "in 15 s";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(ms);
        String actual = outContent.toString().trim();
        assertEquals(expected, actual);
    }
}