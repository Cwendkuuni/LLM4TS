import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;

    @Before
    public void setUp() {
        // Initialize CSVPrinter before each test
        csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint_NullValue() {
        try {
            csvPrinter.print(null);
            assertEquals("Empty string should be printed when null value is provided", Constants.EMPTY, csvPrinter.getOut().toString());
        } catch (IOException e) {
            fail("IOException should not be thrown when printing null value");
        }
    }

    @Test
    public void testPrint_EmptyValue() {
        try {
            csvPrinter.print("");
            assertEquals("Empty string should be printed as is", Constants.EMPTY, csvPrinter.getOut().toString());
        } catch (IOException e) {
            fail("IOException should not be thrown when printing empty value");
        }
    }

    @Test
    public void testPrint_Comment() {
        try {
            csvPrinter.printComment("This is a comment");
            assertEquals("Comment should be printed correctly with proper formatting", "# This is a comment" + Constants.RECORD_SEPARATOR, csvPrinter.getOut().toString());
        } catch (IOException e) {
            fail("IOException should not be thrown when printing a comment");
        }
    }

    @Test
    public void testPrintRecord_SingleValue() {
        try {
            csvPrinter.printRecord("value1");
            assertEquals("Single value should be printed correctly with separator and new line", "value1" + Constants.RECORD_SEPARATOR, csvPrinter.getOut().toString());
        } catch (IOException e) {
            fail("IOException should not be thrown when printing a record with single value");
        }
    }

    @Test
    public void testPrintRecord_MultipleValues() {
        try {
            csvPrinter.printRecord("value1", "value2");
            assertEquals("Multiple values should be printed correctly with separator and new line", "value1,value2" + Constants.RECORD_SEPARATOR, csvPrinter.getOut().toString());
        } catch (IOException e) {
            fail("IOException should not be thrown when printing a record with multiple values");
        }
    }

    @Test
    public void testPrintRecords_EmptyIterable() {
        try {
            csvPrinter.printRecords(new ArrayList<>());
            assertEquals("Empty iterable should not change the output", Constants.EMPTY, csvPrinter.getOut().toString());
        } catch (IOException e) {
            fail("IOException should not be thrown when printing records from an empty iterable");
        }
    }

    @Test
    public void testPrintRecords_NullArray() {
        try {
            csvPrinter.printRecords((Object[]) null);
            assertEquals("Null array should not change the output", Constants.EMPTY, csvPrinter.getOut().toString());
        } catch (IOException e) {
            fail("IOException should not be thrown when printing records from a null array");
        }
    }

    @Test
    public void testPrintRecords_NullResultSet() {
        try {
            csvPrinter.printRecords((ResultSet) null);
            assertEquals("Null result set should not change the output", Constants.EMPTY, csvPrinter.getOut().toString());
        } catch (IOException | SQLException e) {
            fail("IOException or SQLException should not be thrown when printing records from a null result set");
        }
    }

}