import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;

    @Before
    public void setUp() {
        csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint() {
        try {
            csvPrinter.print("testPrint");
            String output = csvPrinter.getOut().toString();
            assertEquals("testPrint", output);
        } catch (Exception e) {
            fail("Unexpected exception in testPrint: " + e.getMessage());
        }
    }

    @Test
    public void testPrintComment() {
        try {
            csvPrinter.printComment("This is a comment");
            String output = csvPrinter.getOut().toString();
            assertTrue(output.contains("# This is a comment"));
        } catch (Exception e) {
            fail("Unexpected exception in testPrintComment: " + e.getMessage());
        }
    }

    @Test
    public void testPrintRecord() {
        try {
            csvPrinter.printRecord("Value1", "Value2");
            String output = csvPrinter.getOut().toString();
            assertEquals("Value1,Value2\n", output);
        } catch (Exception e) {
            fail("Unexpected exception in testPrintRecord: " + e.getMessage());
        }
    }

    @Test
    public void testPrintRecords() {
        try {
            Object[] values = { "Val1", "Val2" };
            csvPrinter.printRecords(values);
            String output = csvPrinter.getOut().toString();
            assertEquals("Val1,Val2\n", output);
        } catch (Exception e) {
            fail("Unexpected exception in testPrintRecords: " + e.getMessage());
        }
    }

    // Additional test cases for edge cases and exceptions can be added here

}