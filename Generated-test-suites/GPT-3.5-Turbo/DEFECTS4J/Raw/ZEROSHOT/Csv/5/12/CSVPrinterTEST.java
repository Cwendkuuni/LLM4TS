import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

public class CSVPrinterTest {

    private CSVFormat format;
    private StringWriter writer;
    private CSVPrinter printer;

    /**
     * Create a new CSVPrinter before each test.
     */
    @Before
    public void setUp() {
        format = CSVFormat.DEFAULT;
        writer = new StringWriter();
        printer = new CSVPrinter(writer, format);
    }

    /**
     * Test print method with null value.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintWithNullValue() throws IOException {
        printer.print(null);
        String expected = ",";
        assertEquals(expected, writer.toString());
    }

    /**
     * Test print method with non-null value.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintWithNonNullValue() throws IOException {
        printer.print("testValue");
        String expected = "testValue,";
        assertEquals(expected, writer.toString());
    }

    /**
     * Test printComment method.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        String expected = "# This is a comment" + System.getProperty("line.separator");
        assertEquals(expected, writer.toString());
    }

    /**
     * Test println method.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintln() throws IOException {
        printer.println();
        String expected = System.getProperty("line.separator");
        assertEquals(expected, writer.toString());
    }

    /**
     * Test printRecord method with Iterable.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintRecordWithIterable() throws IOException {
        List<String> values = Arrays.asList("value1", "value2", "value3");
        printer.printRecord(values);
        String expected = "value1,value2,value3" + System.getProperty("line.separator");
        assertEquals(expected, writer.toString());
    }

    /**
     * Test printRecord method with varargs.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintRecordWithVarargs() throws IOException {
        printer.printRecord("value1", "value2", "value3");
        String expected = "value1,value2,value3" + System.getProperty("line.separator");
        assertEquals(expected, writer.toString());
    }

    /**
     * Test printRecords method with Iterable of arrays.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintRecordsWithIterableArray() throws IOException {
        List<Object[]> records = Arrays.asList(
                new Object[]{"value1", "value2"},
                new Object[]{"value3", "value4"},
                new Object[]{"value5", "value6"}
        );
        printer.printRecords(records);
        String expected = "value1,value2" + System.getProperty("line.separator") +
                          "value3,value4" + System.getProperty("line.separator") +
                          "value5,value6" + System.getProperty("line.separator");
        assertEquals(expected, writer.toString());
    }

    /**
     * Test printRecords method with array of arrays.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintRecordsWithArrayArray() throws IOException {
        Object[][] records = {
                {"value1", "value2"},
                {"value3", "value4"},
                {"value5", "value6"}
        };
        printer.printRecords(records);
        String expected = "value1,value2" + System.getProperty("line.separator") +
                          "value3,value4" + System.getProperty("line.separator") +
                          "value5,value6" + System.getProperty("line.separator");
        assertEquals(expected, writer.toString());
    }

    /**
     * Test printRecords method with ResultSet.
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintRecordsWithResultSet() throws IOException {
        ResultSet resultSet = new MockResultSet();
        printer.printRecords(resultSet);
        String expected = "value1,value2" + System.getProperty("line.separator") +
                          "value3,value4" + System.getProperty("line.separator") +
                          "value5,value6" + System.getProperty("line.separator");
        assertEquals(expected, writer.toString());
    }
}