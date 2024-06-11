import org.junit.Test;
import static org.junit.Assert.*;

public class CSVPrinterTest {

    @Test
    public void testPrintNullValue() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.print(null);
            String result = printer.getOut().toString();
            assertEquals("", result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testPrintStringValue() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.print("Hello");
            String result = printer.getOut().toString();
            assertEquals("Hello", result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testPrintIntegerValue() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.print(123);
            String result = printer.getOut().toString();
            assertEquals("123", result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testPrintFloatingPointValue() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.print(3.14);
            String result = printer.getOut().toString();
            assertEquals("3.14", result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testPrintRecord() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.printRecord("John", "Doe", 25);
            String result = printer.getOut().toString();
            assertEquals("John,Doe,25", result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testPrintComment() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.printComment("This is a comment");
            String result = printer.getOut().toString();
            assertEquals("# This is a comment\n", result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testPrintln() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.println();
            String result = printer.getOut().toString();
            assertEquals("\n", result);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.close();
            // No assertions needed as it just calls the close method on Appendable
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFlush() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.flush();
            // No assertions needed as it just calls the flush method on Appendable
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}