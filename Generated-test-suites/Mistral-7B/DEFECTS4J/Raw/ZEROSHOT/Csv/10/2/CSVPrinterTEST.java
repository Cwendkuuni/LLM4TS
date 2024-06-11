package org.apache.commons.csv;

import static org.apache.commons.csv.CSVPrinter.COMMENT;
import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVPrinter.Quote;
import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Test class for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    private CSVFormat format;
    private CSVPrinter printer;
    private ByteArrayOutputStream outputStream;

    /**
     * Sets up the test.
     */
    @Before
    public void setUp() {
        format = new CSVFormat(',', Quote.MINIMAL);
        outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(new PrintStream(outputStream), format);
    }

    /**
     * Tests CSVPrinter construction with null out and format.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructionWithNullOutOrFormat() {
        new CSVPrinter(null, format);
    }

    /**
     * Tests CSVPrinter construction with null format.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructionWithNullFormat() {
        new CSVPrinter(outputStream, null);
    }

    /**
     * Tests print method with null value.
     */
    @Test
    public void testPrintNullValue() throws IOException {
        printer.print(null);
        printer.flush();
        final String result = outputStream.toString();
        assertEquals("\"\"", result);
    }

    /**
     * Tests print method with non-null value.
     */
    @Test
    public void testPrintValue() throws IOException {
        printer.print("test");
        printer.flush();
        final String result = outputStream.toString();
        assertEquals("test", result);
    }

    /**
     * Tests print method with escaping enabled.
     */
    @Test
    public void testPrintValueWithEscaping() throws IOException {
        format = format.withEscaping('\\');
        printer = new CSVPrinter(outputStream, format);
        printer.print("test\"\"test");
        printer.flush();
        final String result = outputStream.toString();
        assertEquals("test\\\"test", result);
    }

    /**
     * Tests print method with quoting enabled.
     */
    @Test
    public void testPrintValueWithQuoting() throws IOException {
        format = format.withQuoteMode(QuoteMode.ALL);
        printer = new CSVPrinter(outputStream, format);
        printer.print("test\"test");
        printer.flush();
        final String result = outputStream.toString();
        assertEquals("\"test\"\"test\"", result);
    }

    /**
     * Tests printRecord method with null values.
     */
    @Test
    public void testPrintRecordWithNullValues() throws IOException {
        printer.printRecord(null);
        printer.flush();
        final String result = outputStream.toString();
        assertEquals("\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"", result);
    }

    /**
     * Tests printRecord method with null and non-null values.
     */
    @Test
    public void testPrintRecordWithNullAndNonNullValues() throws IOException {
        printer.printRecord("test", null);
        printer.flush();
        final String result = outputStream.toString();
        assertEquals("test,\"\",", result);
    }

    /**
     * Tests printRecord method with an iterable.
     */
    @Test
    public void testPrintRecordWithIterable() throws IOException {
        printer.printRecord(new String[]{"test1", "test2"});
        printer.flush();
        final String result = outputStream.toString();
        assertEquals("test1,test2", result);
    }

    /**
     * Tests printComment method.
     */
    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("test comment");
        printer.flush();
        final String result = outputStream.toString();
        assertEquals(COMMENT + SP + "test comment\r\n" + COMMENT + SP, result);
    }

    /**
     * Tests println method.
     */
    @Test
    public void testPrintln() throws IOException {
        printer.println();
        printer.flush();
        final String result = outputStream.toString();
        assertEquals(format.getRecordSeparator(), result);
    }

    /**
     * Tests printRecord method with an iterable and the record separator set.
     */
    @Test
    public void testPrintRecordWithIterableAndRecordSeparator() throws IOException {
        format.setRecordSeparator("test-record-separator");
        printer = new CSVPrinter(outputStream, format);
        printer.printRecord(new String[]{"test1", "test2"});
        printer.flush();
        final String result = outputStream.toString();
        assertEquals("test1,test2" + format.getRecordSeparator(), result);
    }

    /**
     * Tests printRecords method with an iterable.
     */
    @Test
    public void testPrintRecordsWithIterable() throws IOException, SQLException {
        final String[] values = {"test1", "test2"};
        printer.printRecords(values);
        printer.flush();
        final String result = outputStream.toString();
        assertEquals("test1,test2\r\n", result);
    }

    /**
     * Tests printRecords method with a JDBC result set.
     */
    @Test
    public void testPrintRecordsWithJDBCResultSet() throws IOException, SQLException {
        final ResultSet resultSet = mock(ResultSet.class);
        final ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        final String[] values = new String[]{"test1", "test2"};

        // Set up a mock ResultSet to return specified values when getString method is called
        for (int i = 1; i <= 2; i++) {
            verify(resultSet).getString(i);
            when(resultSet.getString(i)).thenReturn(values[i - 1]);
        }

        printer.printRecords(resultSet);
        printer.flush();
        final String result = outputStream.toString();
        assertEquals("test1,test2\r\n", result);
    }
}