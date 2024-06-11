import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class CSVPrinterTest {
    private CSVPrinter csvPrinter;
    private StringWriter stringWriter;

    @Before
    public void setUp() {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("abc");
        assertEquals("abc", stringWriter.toString());
    }

    @Test
    public void testPrint_NullValue() throws IOException {
        csvPrinter.print(null);
        assertEquals("", stringWriter.toString());
    }

    @Test
    public void testPrintAndQuote() throws IOException {
        csvPrinter.print("a,b, c");
        assertEquals("\"a,b, c\"", stringWriter.toString());
    }

    @Test
    public void testPrintAndEscape() throws IOException {
        csvPrinter.print("\"abc\", \"def\"");
        assertEquals("\"\"\"abc\"\"\", \"\"\"def\"\"\"", stringWriter.toString());
    }

    @Test
    public void testPrintAndQuote_ObjectIsNumber() throws IOException {
        csvPrinter.print(123);
        assertEquals("123", stringWriter.toString());
    }

    @Test
    public void testPrintAndQuote_ObjectIsNumber_QuotePolicyNonNumeric() throws IOException {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withQuotePolicy(CSVFormat.QuotePolicy.NON_NUMERIC);
        csvPrinter = new CSVPrinter(stringWriter, csvFormat);
        csvPrinter.print(123);
        assertEquals("123", stringWriter.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        assertEquals("# This is a comment\n", stringWriter.toString());
    }

    @Test
    public void testPrintComment_MultipleLineComment() throws IOException {
        csvPrinter.printComment("This is a comment\nThis is a second line");
        assertEquals("# This is a comment\n# This is a second line\n", stringWriter.toString());
    }

    @Test
    public void testPrintln() throws IOException {
        csvPrinter.println();
        assertEquals("\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("abc", "def", "ghi");
        assertEquals("abc,def,ghi\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecord_Iterable() throws IOException {
        csvPrinter.printRecord(new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return Arrays.asList("abc", "def", "ghi").iterator();
            }
        });
        assertEquals("abc,def,ghi\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecord_ObjectArray() throws IOException {
        csvPrinter.printRecord(new Object[]{"abc", "def", "ghi"});
        assertEquals("abc,def,ghi\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords_IterableOfObjectArray() throws IOException {
        csvPrinter.printRecords(Arrays.asList(
                new Object[]{"abc", "def", "ghi"},
                new Object[]{"123", "456", "789"}
        ));
        assertEquals("abc,def,ghi\n123,456,789\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords_ObjectArrayOfIterable() throws IOException {
        csvPrinter.printRecords(new Object[]{
                Arrays.asList("abc", "def", "ghi"),
                Arrays.asList("123", "456", "789")
        });
        assertEquals("abc,def,ghi\n123,456,789\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords_ResultSet() throws IOException, SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSetMetaData.getColumnCount()).thenReturn(3);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getString(1)).thenReturn("abc");
        when(resultSet.getString(2)).thenReturn("def");
        when(resultSet.getString(3)).thenReturn("ghi");

        csvPrinter.printRecords(resultSet);
        assertEquals("abc,def,ghi\n", stringWriter.toString());
    }

    @Test
    public void testGetOut() {
        Appendable out = csvPrinter.getOut();
        assertEquals(stringWriter, out);
    }
}