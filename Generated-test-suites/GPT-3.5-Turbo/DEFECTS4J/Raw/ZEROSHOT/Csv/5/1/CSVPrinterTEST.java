import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.*;

public class CSVPrinterTest {
    private CSVPrinter csvPrinter;
    private StringBuilder sb;

    @Before
    public void setUp() throws Exception {
        sb = new StringBuilder();
        csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws Exception {
        csvPrinter.close();
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("Test String");
        assertEquals("Test String", sb.toString());
    }

    @Test
    public void testPrint_NullValue() throws IOException {
        csvPrinter.print(null);
        assertEquals("", sb.toString());
    }

    @Test
    public void testPrintAndEscape() throws IOException {
        csvPrinter.print("Test \n String");
        assertEquals("Test \\n String", sb.toString());
    }

    @Test
    public void testPrintAndQuote() throws IOException {
        csvPrinter.print("\"Test\"");
        assertEquals("\"\"\"Test\"\"\"", sb.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        assertEquals("# This is a comment\n", sb.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("Value 1", "Value 2", "Value 3");
        assertEquals("Value 1,Value 2,Value 3\n", sb.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        csvPrinter.printRecords(new Object[]{"Value 1", "Value 2", "Value 3"});
        assertEquals("Value 1,Value 2,Value 3\n", sb.toString());
    }

    @Test
    public void testPrintRecords_NullValue() throws IOException {
        csvPrinter.printRecords(new Object[]{null});
        assertEquals("\n", sb.toString());
    }

    @Test
    public void testPrintRecords_JDBCResultSet() throws IOException {
        ResultSet resultSet = null;
        csvPrinter.printRecords(resultSet);
        assertEquals("", sb.toString());
    }

    @Test
    public void testGetOut() {
        Appendable out = csvPrinter.getOut();
        assertNotNull(out);
    }

    @Test
    public void testFlush() throws IOException {
        csvPrinter.flush();
        // No assertion as this method does not alter the output
    }
}