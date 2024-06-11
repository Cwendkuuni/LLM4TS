import static org.junit.Assert.*;
import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testCSVPrinterConstructor() {
        // Test constructor with valid parameters
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        assertNotNull(csvPrinter);
    }

    @Test
    public void testPrint() {
        // Test printing a basic String value
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        
        try {
            csvPrinter.print("Hello, World!");
            assertEquals("Hello, World!", out.toString());
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
    }

    // Add more test cases for other public methods in CSVPrinter

}