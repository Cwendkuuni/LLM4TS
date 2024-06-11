import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;

    @Before
    public void setUp() throws Exception {
        csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws Exception {
        csvPrinter.close();
    }

    @Test
    public void testPrintNull() throws IOException {
        csvPrinter.print(null);
        assertEquals("null", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintString() throws IOException {
        csvPrinter.print("Hello, World");
        assertEquals("Hello, World", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintEmptyValue() throws IOException {
        csvPrinter.print("");
        assertEquals("", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintAndEscape() throws IOException {
        csvPrinter.print("Esc,,''ape");
        assertEquals("Esc,,'',\"ape", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintAndQuote() throws IOException {
        csvPrinter.print("Quote\"");
        assertEquals("\"Quote\"\"\"", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        assertEquals("# This is a comment\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecordWithIterable() throws IOException {
        csvPrinter.printRecord(Arrays.asList("A", "B", "C"));
        assertEquals("A,B,C\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecordWithArray() throws IOException {
        csvPrinter.printRecord(new String[]{"X", "Y", "Z"});
        assertEquals("X,Y,Z\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecordsWithIterable() throws IOException {
        csvPrinter.printRecords(Arrays.asList("1,2,3", new String[]{"4", "5", "6"}));
        assertEquals("1,2,3\n4,5,6\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecordsWithArray() throws IOException {
        csvPrinter.printRecords(new Object[]{"7,8,9", new String[]{"10", "11", "12"}});
        assertEquals("7,8,9\n10,11,12\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecordsWithResultSet() throws SQLException, IOException {
        // mock the ResultSet
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("John", "Doe");

        csvPrinter.printRecords(resultSet);

        verify(resultSet, times(2)).getString(anyInt());
        assertEquals("John,Doe\n", csvPrinter.getOut().toString());
    }
}