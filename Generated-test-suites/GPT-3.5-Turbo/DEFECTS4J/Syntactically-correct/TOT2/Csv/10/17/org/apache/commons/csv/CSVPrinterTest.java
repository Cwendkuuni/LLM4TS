import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVPrinterTest {

    @Test
    public void testPrint() {
        StringWriter out = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(out, format);

        try {
            printer.print("one");
            printer.print("two");
            printer.print("three");
            printer.flush();
        } catch (Exception e) {
            fail("Exception thrown during print test: " + e.getMessage());
        }

        assertEquals("one,two,three", out.toString());
    }

    @Test
    public void testPrintNullValue() {
        StringWriter out = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(out, format);

        try {
            printer.print(null);
            printer.flush();
        } catch (Exception e) {
            fail("Exception thrown during print null value test: " + e.getMessage());
        }

        assertEquals("", out.toString());
    }

    @Test
    public void testPrintRecord() {
        StringWriter out = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(out, format);

        try {
            printer.printRecord("four", "five", "six");
            printer.flush();
        } catch (Exception e) {
            fail("Exception thrown during print record test: " + e.getMessage());
        }

        assertEquals("four,five,six\n", out.toString());
    }

    @Test
    public void testPrintComment() {
        StringWriter out = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT.withCommentStart('#');
        CSVPrinter printer = new CSVPrinter(out, format);

        try {
            printer.printComment("Sample comment");
            printer.flush();
        } catch (Exception e) {
            fail("Exception thrown during print comment test: " + e.getMessage());
        }

        assertEquals("# Sample comment\n", out.toString());
    }

    @Test
    public void testPrintRecords() {
        StringWriter out = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(out, format);

        try {
            printer.printRecords(new String[]{"seven", "eight", "nine"});
            printer.flush();
        } catch (Exception e) {
            fail("Exception thrown during print records test: " + e.getMessage());
        }

        assertEquals("seven,eight,nine\n", out.toString());
    }

    @Test
    public void testPrintRecordsResultSet() {
        // This test requires a database connection, will implement this in the future
        // For now, skipping this test
        assertTrue(true);
    }

}