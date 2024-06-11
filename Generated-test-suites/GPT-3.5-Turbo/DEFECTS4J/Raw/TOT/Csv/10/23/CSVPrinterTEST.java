import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    CSVPrinter csvPrinter;

    @Before
    public void setUp() throws Exception {
        csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint_NullValue() throws Exception {
        csvPrinter.print(null);
        assertEquals("\"\"", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrint_NotNullValue() throws Exception {
        csvPrinter.print("Hello");
        assertEquals("Hello", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintAndQuote_QuoteNeeded() throws Exception {
        csvPrinter.printAndQuote("Hello, World!");
        assertEquals("\"Hello, World!\"", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintAndQuote_QuoteNotNeeded() throws Exception {
        csvPrinter.printAndQuote("Hello World");
        assertEquals("Hello World", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintAndEscape_EscapeNeeded() throws Exception {
        csvPrinter.printAndEscape("Hello\nWorld");
        assertEquals("Hello\\nWorld", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintAndEscape_EscapeNotNeeded() throws Exception {
        csvPrinter.printAndEscape("Hello World");
        assertEquals("Hello World", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintComment() throws Exception {
        csvPrinter.printComment("This is a comment");
        assertEquals("# This is a comment\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintln() throws Exception {
        csvPrinter.println();
        assertEquals("\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecord_Iterable() throws Exception {
        csvPrinter.printRecord(Arrays.asList("Value1", "Value2", "Value3"));
        assertEquals("Value1,Value2,Value3\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecord_Array() throws Exception {
        csvPrinter.printRecord("Value1", "Value2", "Value3");
        assertEquals("Value1,Value2,Value3\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecords_IterableOfStringArrays() throws Exception {
        csvPrinter.printRecords(Arrays.asList(new String[]{"Value1", "Value2"}, new String[]{"Value3", "Value4"}));
        assertEquals("Value1,Value2\nValue3,Value4\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecords_ArrayOfObjects() throws Exception {
        csvPrinter.printRecords(new Object[]{new String[]{"Value1", "Value2"}, new String[]{"Value3", "Value4"}});
        assertEquals("Value1,Value2\nValue3,Value4\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecords_ResultSet() throws Exception {
        // Stub for ResultSet
        ResultSet resultSet = null;

        csvPrinter.printRecords(resultSet);
        // Validate the output
    }
}