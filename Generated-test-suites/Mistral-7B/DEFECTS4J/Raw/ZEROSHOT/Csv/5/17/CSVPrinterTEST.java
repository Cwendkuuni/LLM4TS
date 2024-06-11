import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the CSVPrinter class.
 */
public class CSVPrinterTest {

    private ByteArrayOutputStream outputStream;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true));
        printer = new CSVPrinter(new OutputStreamWriter(outputStream), format);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    /**
     * Test printing a single string value.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testPrintString() throws IOException {
        printer.print("Test");
        CSVRecord record = CSVRecord.parse(outputStream.toString());
        List<String> values = record.toList();
        assertEquals("Test", values.get(0));
    }

    /**
     * Test printing a null value.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        CSVRecord record = CSVRecord.parse(outputStream.toString());
        List<String> values = record.toList();
        assertEquals("", values.get(0));
    }

    /**
     * Test printing multiple string values.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testPrintStrings() throws IOException {
        printer.print("Test1");
        printer.print("Test2");
        printer.println();

        CSVRecord record = CSVRecord.parse(outputStream.toString());
        List<String> values1 = record.toList();
        assertEquals("Test1", values1.get(0));
        assertEquals("Test2", values1.get(1));

        printer.flush();
        outputStream.reset();
    }

    /**
     * Test printing multiple string values with quotes.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testPrintStringsWithQuotes() throws IOException {
        printer.print("\"Test1\"");
        printer.print("Test2");
        printer.println();

        CSVRecord record = CSVRecord.parse(outputStream.toString());
        List<String> values1 = record.toList();
        assertEquals("\"Test1\"", values1.get(0));
        assertEquals("Test2", values1.get(1));

        printer.flush();
        outputStream.reset();

        printer.print("Test1");
        printer.print("\"Test2\"");
        printer.println();

        CSVRecord record2 = CSVRecord.parse(outputStream.toString());
        List<String> values2 = record2.toList();
        assertEquals("Test1", values2.get(0));
        assertEquals("\"Test2\"", values2.get(1));

        printer.flush();
        outputStream.reset();
    }

    /**
     * Test printing an array of string values.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testPrintStringArray() throws IOException {
        String[] values = {"Test1", "Test2"};
        printer.printRecord(values);
        CSVRecord record = CSVRecord.parse(outputStream.toString());
        List<String> values1 = record.toList();
        assertEquals(Arrays.asList(values), values1);

        printer.flush();
        outputStream.reset();
    }

    /**
     * Test printing a single null value in an array of values.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testPrintStringArrayWithNull() throws IOException {
        String[] values = {"Test1", null, "Test3"};
        printer.printRecord(values);
        CSVRecord record = CSVRecord.parse(outputStream.toString());
        List<String> values1 = record.toList();
        assertEquals(Arrays.asList(values), values1);

        printer.flush();
        outputStream.reset();
    }

    /**
     * Test printing multiple null values in an array of values.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testPrintStringArrayWithMultipleNulls() throws IOException {
        String[] values = {null, null, "Test3"};
        printer.printRecord(values);
        CSVRecord record = CSVRecord.parse(outputStream.toString());
        List<String> values1 = record.toList();
        assertEquals(Arrays.asList(values), values1);

        printer.flush();
        outputStream.reset();
    }

    /**
     * Test printing multiple records.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testPrintRecords() throws IOException {
        String[][] values = {{"Test11", "Test12"}, {"Test21", "Test22"}};
        printer.printRecords(Arrays.asList(values));
        CSVRecord record = null;
        while ((record = CSVRecord.parse(outputStream.toString())) != null) {
            List<String> values1 = record.toList();
            assertEquals(Arrays.asList(values[0]), values1);
            printer.println();
        }

        printer.flush();
        outputStream.reset();
    }

    /**
     * Test printing a comment.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a test comment");
        String output = outputStream.toString();
        String expected = "# This is a test comment\n";
        assertEquals(expected, output);
    }

    /**
     * Test flushing the output stream.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test
    public void testFlush() throws IOException {
        printer.print("Test1");
        printer.println();
        printer.flush();

        String expected = "Test1\n";
        String output = outputStream.toString();
        assertEquals(expected, output);

        outputStream.reset();
    }

    /**
     * Test closing the output stream.
     *
     * @throws IOException If an I/O error occurs
     */
    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        printer.close();
        printer.print("Test1");
    }
}