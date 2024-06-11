import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;

    @Before
    public void setUp() {
        // Initialize CSVPrinter with a StringBuilder and default CSVFormat
        csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint() {
        try {
            csvPrinter.print("value");
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        assertEquals("value", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintNull() {
        try {
            csvPrinter.print(null);
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        assertEquals("", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintComment() {
        try {
            csvPrinter.printComment("This is a comment");
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        assertEquals("# This is a comment\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecord() {
        try {
            csvPrinter.printRecord("value1", "value2", "value3");
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        assertEquals("value1,value2,value3\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecords() {
        try {
            csvPrinter.printRecords(new String[]{"value1", "value2", "value3"});
        } catch (IOException e) {
            fail("Unexpected IOException");
        }
        assertEquals("value1,value2,value3\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecordsResultSet() {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData().getColumnCount()).thenReturn(3);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("value1");
        when(resultSet.getString(2)).thenReturn("value2");
        when(resultSet.getString(3)).thenReturn("value3");

        try {
            csvPrinter.printRecords(resultSet);
        } catch (SQLException | IOException e) {
            fail("Unexpected exception");
        }
        assertEquals("value1,value2,value3\n", csvPrinter.getOut().toString());
    }

}