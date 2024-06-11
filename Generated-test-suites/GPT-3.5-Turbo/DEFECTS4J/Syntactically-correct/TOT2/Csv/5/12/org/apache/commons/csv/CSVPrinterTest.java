import static org.junit.Assert.*;
import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testPrint() {
        // Test case for printing a null value
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            csvPrinter.print(null);
            assertEquals("null values should be printed as empty strings", "", csvPrinter.getOut().toString());
        } catch (IOException e) {
            fail("IOException should not be thrown in this test case");
        }
    }

    @Test
    public void testPrintComment() {
        // Test case for printing a comment with line breaks and quoting
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT.withCommentStart('#'));
        try {
            csvPrinter.printComment("This is a\ncomment with#special chars");
            assertEquals("# This is a\n# comment with##special chars\n", csvPrinter.getOut().toString());
        } catch (IOException e) {
            fail("IOException should not be thrown in this test case");
        }
    }

    @Test
    public void testPrintRecord() {
        // Test case for printing a single line of records
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            csvPrinter.printRecord("value1", "value2", "value3");
            assertEquals("value1,value2,value3\n", csvPrinter.getOut().toString());
        } catch (IOException e) {
            fail("IOException should not be thrown in this test case");
        }
    }

    @Test
    public void testPrintRecords() {
        // Test case for printing multiple lines of records from an array
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        Object[] records = {new Object[]{"r1v1", "r1v2"}, new Object[]{"r2v1", "r2v2"}};
        try {
            csvPrinter.printRecords(records);
            assertEquals("r1v1,r1v2\nr2v1,r2v2\n", csvPrinter.getOut().toString());
        } catch (IOException e) {
            fail("IOException should not be thrown in this test case");
        }
    }

    @Test
    public void testPrintRecordsFromResultSet() {
        // Test case for printing records from a mocked ResultSet
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        ResultSet resultSet = mock(ResultSet.class);
        try {
            when(resultSet.next()).thenReturn(true, true, false);
            when(resultSet.getString(1)).thenReturn("value1", "value2");
            csvPrinter.printRecords(resultSet);
            assertEquals("value1,value2\n", csvPrinter.getOut().toString());
        } catch (SQLException e) {
            fail("SQLException should not be thrown in this test case");
        } catch (IOException e) {
            fail("IOException should not be thrown in this test case");
        }
    }
}