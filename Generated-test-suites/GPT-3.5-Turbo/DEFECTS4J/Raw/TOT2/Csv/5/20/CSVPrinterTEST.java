import org.junit.Test;
import static org.junit.Assert.*;

public class CSVPrinterTest {

    @Test
    public void testCSVPrinterConstructor() {
        // Test constructor with valid parameters
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        assertNotNull(printer);
    }

    @Test
    public void testPrint() {
        // Test printing a null value
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        assertDoesNotThrow(() -> printer.print(null));
        assertEquals("", printer.getOut().toString());
        
        // Test printing a non-null value
        assertDoesNotThrow(() -> printer.print("test"));
        assertEquals("test", printer.getOut().toString());
    }

    // Add more test cases for other methods in CSVPrinter class here

    @Test
    public void testFlush() {
        // Test flushing the output stream
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        assertDoesNotThrow(() -> printer.flush());
    }

    @Test
    public void testClose() {
        // Test closing the output stream
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        assertDoesNotThrow(() -> printer.close());
    }

    @Test
    public void testPrintComment() {
        // Test printing a comment
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        assertDoesNotThrow(() -> printer.printComment("This is a comment"));
        assertTrue(printer.getOut().toString().contains("# This is a comment"));
    }

    @Test
    public void testPrintRecord() {
        // Test printing a record with multiple values
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        assertDoesNotThrow(() -> printer.printRecord("value1", "value2", "value3"));
        assertEquals("value1,value2,value3" + System.lineSeparator(), printer.getOut().toString());
    }

    @Test
    public void testPrintRecords() {
        // Test printing multiple records
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        assertDoesNotThrow(() -> printer.printRecords(new String[]{"val1", "val2"}, new String[]{"val3", "val4"}));
        assertEquals("val1,val2" + System.lineSeparator() + "val3,val4" + System.lineSeparator(), printer.getOut().toString());
    }
}