import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class CSVPrinterTest {

    @Test
    public void testClose() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.EXCEL);
        try {
            printer.close();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testFlush() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.EXCEL);
        try {
            printer.flush();
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testPrint() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.EXCEL);
        try {
            printer.print("value");
            assertEquals("value", printer.getOut().toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testPrintComment() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.EXCEL);
        try {
            printer.printComment("This is a comment");
            assertEquals("# This is a comment", printer.getOut().toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testPrintln() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.EXCEL);
        try {
            printer.println();
            assertEquals(System.lineSeparator(), printer.getOut().toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testPrintRecordWithIterable() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.EXCEL);
        try {
            printer.printRecord(Arrays.asList("value1", "value2", "value3"));
            assertEquals("value1,value2,value3" + System.lineSeparator(), printer.getOut().toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testPrintRecordWithArray() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.EXCEL);
        try {
            printer.printRecord("value1", "value2", "value3");
            assertEquals("value1,value2,value3" + System.lineSeparator(), printer.getOut().toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testPrintRecordsWithIterable() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.EXCEL);
        try {
            printer.printRecords(Arrays.asList(
                    Arrays.asList("value1", "value2", "value3"),
                    Arrays.asList("value4", "value5", "value6")
            ));
            assertEquals("value1,value2,value3" + System.lineSeparator() +
                    "value4,value5,value6" + System.lineSeparator(), printer.getOut().toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testPrintRecordsWithArray() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.EXCEL);
        try {
            printer.printRecords(new Object[] {
                    new Object[] {"value1", "value2", "value3"},
                    new Object[] {"value4", "value5", "value6"}
            });
            assertEquals("value1,value2,value3" + System.lineSeparator() +
                    "value4,value5,value6" + System.lineSeparator(), printer.getOut().toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testPrintRecordsWithResultSet() {
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.EXCEL);
        try {
            // Assuming a valid ResultSet with 3 columns: columnName1, columnName2, columnName3
            ResultSet resultSet = null;
            printer.printRecords(resultSet);
            // Add verification for expected output
        } catch (SQLException e) {
            fail("SQLException occurred");
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
}