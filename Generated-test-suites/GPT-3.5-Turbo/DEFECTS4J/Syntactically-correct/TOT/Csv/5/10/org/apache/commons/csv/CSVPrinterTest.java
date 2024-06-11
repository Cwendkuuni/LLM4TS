import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    private StringWriter writer;
    private CSVPrinter csvPrinter;

    @Before
    public void setUp() {
        writer = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        csvPrinter = new CSVPrinter(writer, format);
    }

    @Test
    public void testPrint_singleValue() throws IOException {
        csvPrinter.print("value");
        csvPrinter.flush();
        assertEquals("value" + System.lineSeparator(), writer.toString());
    }

    @Test
    public void testPrint_nullValue() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        String expected = "";
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrint_ObjectValue() throws IOException {
        Object obj = new Object();
        csvPrinter.print(obj);
        csvPrinter.flush();
        String expected = obj.toString() + System.lineSeparator();
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrint_multipleValues() throws IOException {
        csvPrinter.print("value1");
        csvPrinter.print("value2");
        csvPrinter.flush();
        String expected = "value1,value2" + System.lineSeparator();
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrint_emptyValues() throws IOException {
        csvPrinter.print("");
        csvPrinter.flush();
        String expected = "" + System.lineSeparator();
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrint_comment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        String expected = "# This is a comment" + System.lineSeparator();
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrintRecord_iterable() throws IOException {
        csvPrinter.printRecord(Arrays.asList("value1", "value2", "value3"));
        csvPrinter.flush();
        String expected = "value1,value2,value3" + System.lineSeparator();
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrintRecord_array() throws IOException {
        String[] values = {"value1", "value2", "value3"};
        csvPrinter.printRecord((Object[]) values);
        csvPrinter.flush();
        String expected = "value1,value2,value3" + System.lineSeparator();
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrintRecord_emptyArray() throws IOException {
        String[] values = {};
        csvPrinter.printRecord((Object[]) values);
        csvPrinter.flush();
        String expected = System.lineSeparator();
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrintRecord_JDBCResultSet() throws IOException {
        ResultSet resultSet = createMockResultSet();
        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();
        String expected = "value1,value2,value3" + System.lineSeparator();
        assertEquals(expected, writer.toString());
    }

    /*
     * Helper method to create a mock ResultSet
     */
    private ResultSet createMockResultSet() {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenReturn(mock(ResultSetMetaData.class));
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(1)).thenReturn("value1");
        when(resultSet.getString(2)).thenReturn("value2");
        when(resultSet.getString(3)).thenReturn("value3");
        return resultSet;
    }

}