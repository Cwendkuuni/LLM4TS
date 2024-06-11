import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringBuilder stringBuilder;

    @Before
    public void setUp() throws Exception {
        stringBuilder = new StringBuilder();
        csvPrinter = new CSVPrinter(stringBuilder, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint_withNullValue() throws Exception {
        csvPrinter.print(null);
        assertEquals("null", stringBuilder.toString());
    }

    @Test
    public void testPrint_withStringValue() throws Exception {
        csvPrinter.print("Hello");
        assertEquals("Hello", stringBuilder.toString());
    }

    @Test
    public void testPrint_withNumberValue() throws Exception {
        csvPrinter.print(123);
        assertEquals("123", stringBuilder.toString());
    }

    @Test
    public void testPrint_withCharValue() throws Exception {
        csvPrinter.print('A');
        assertEquals("A", stringBuilder.toString());
    }

    @Test
    public void testPrint_withEscapeChar() throws Exception {
        csvPrinter.print("This is a \"quoted\" value");
        assertEquals("\"This is a \"\"quoted\"\" value\"", stringBuilder.toString());
    }

    @Test
    public void testPrint_withDelimiterChar() throws Exception {
        csvPrinter.print("value1,value2");
        assertEquals("value1,value2", stringBuilder.toString());
    }

    @Test
    public void testPrint_withNewLineChar() throws Exception {
        csvPrinter.print("value1\nvalue2");
        assertEquals("value1\nvalue2", stringBuilder.toString());
    }

    @Test
    public void testPrintComment() throws Exception {
        csvPrinter.printComment("This is a comment");
        assertEquals("# This is a comment\n", stringBuilder.toString());
    }

    @Test
    public void testPrintRecord_withIterable() throws Exception {
        csvPrinter.printRecord(Arrays.asList("value1", "value2", "value3"));
        assertEquals("value1,value2,value3\n", stringBuilder.toString());
    }

    @Test
    public void testPrintRecord_withArray() throws Exception {
        csvPrinter.printRecord("value1", "value2", "value3");
        assertEquals("value1,value2,value3\n", stringBuilder.toString());
    }

    @Test
    public void testPrintRecords_withIterableOfArrays() throws Exception {
        csvPrinter.printRecords(Arrays.asList(new Object[]{"value1", "value2", "value3"}, new Object[]{"value4", "value5", "value6"}));
        assertEquals("value1,value2,value3\nvalue4,value5,value6\n", stringBuilder.toString());
    }

    @Test
    public void testPrintRecords_withArrayOfArrays() throws Exception {
        csvPrinter.printRecords(new Object[][]{{"value1", "value2", "value3"}, {"value4", "value5", "value6"}});
        assertEquals("value1,value2,value3\nvalue4,value5,value6\n", stringBuilder.toString());
    }

    @Test
    public void testPrintRecords_withResultSet() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("value1");
        when(resultSet.getString(2)).thenReturn("value2");
        when(resultSet.getString(3)).thenReturn("value3");

        csvPrinter.printRecords(resultSet);
        assertEquals("value1,value2,value3\nvalue1,value2,value3\n", stringBuilder.toString());
    }

    @Test
    public void testGetOut() throws Exception {
        Appendable appendable = csvPrinter.getOut();
        assertEquals(stringBuilder, appendable);
    }

    @Test
    public void testFlush() throws Exception {
        Flushable flushable = (Flushable) csvPrinter.getOut();
        flushable.flush();
        // No assertion, just making sure the flush method is called without throwing any exception
    }

    @Test
    public void testClose() throws Exception {
        Closeable closeable = (Closeable) csvPrinter.getOut();
        closeable.close();
        // No assertion, just making sure the close method is called without throwing any exception
    }
}