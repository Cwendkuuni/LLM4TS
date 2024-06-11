package org.apache.commons.csv.test;

import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.quoting.Quote;
import org.apache.commons.csv.util.CsvUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CSVPrinter
 */
public class CSVPrinterTest {

    private CSVFormat format;
    private CSVPrinter printer;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        format = CSVFormat.DEFAULT.withQuoteAll(true);
        outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(outputStream, format);
    }

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        // Test null
        assertNull(new CSVPrinter(null, format));
        assertNull(new CSVPrinter(outputStream, null));

        // Test valid
        assertSame(outputStream, new CSVPrinter(outputStream, format).getOut());
        assertSame(format, new CSVPrinter(outputStream, format).getFormat());
    }

    /**
     * Test print method with null value
     */
    @Test
    public void testPrintNullValue() throws IOException {
        printer.print(null);
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("", csv);
    }

    /**
     * Test print method with a valid value
     */
    @Test
    public void testPrintValue() throws IOException {
        printer.print("Hello");
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("Hello", csv);
    }

    /**
     * Test print method with a number
     */
    @Test
    public void testPrintNumber() throws IOException {
        printer.print(123);
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("123", csv);
    }

    /**
     * Test print method with a boolean
     */
    @Test
    public void testPrintBoolean() throws IOException {
        printer.print(true);
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("true", csv);

        printer.print(false);
        printer.flush();
        final String csvFalse = outputStream.toString();
        assertEquals("false", csvFalse);
    }

    /**
     * Test print method with a null array
     */
    @Test
    public void testPrintNullArray() throws IOException {
        Object[] arr = null;
        printer.print(arr);
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("", csv);
    }

    /**
     * Test print method with a valid array
     */
    @Test
    public void testPrintArray() throws IOException {
        Object[] arr = new Object[]{"Hello1", 123, "Hello2"};
        printer.print(arr);
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("Hello1,123,Hello2", csv);
    }

    /**
     * Test print method with a String array
     */
    @Test
    public void testPrintStringArray() throws IOException {
        String[] arr = new String[]{"Hello1", "123", "Hello2"};
        printer.print(arr);
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("Hello1,123,Hello2", csv);
    }

    /**
     * Test printComment method
     */
    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("#This is a comment\r\n", csv);
    }

    /**
     * Test println method
     */
    @Test
    public void testPrintln() throws IOException {
        printer.println();
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals(CR + LF, csv);
    }

    /**
     * Test printRecord method with null Iterable
     */
    @Test
    public void testPrintRecordNullIterable() throws IOException {
        printer.printRecord(null);
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("", csv);
    }

    /**
     * Test printRecord method with empty Iterable
     */
    @Test
    public void testPrintRecordEmptyIterable() throws IOException {
        printer.printRecord(Collections.emptyList());
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("", csv);
    }

    /**
     * Test printRecord method with a single value Iterable
     */
    @Test
    public void testPrintRecordSingleValueIterable() throws IOException {
        List<Object> list = Arrays.asList("Hello");
        printer.printRecord(list);
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("Hello", csv);
    }

    /**
     * Test printRecord method with a single value Array
     */
    @Test
    public void testPrintRecordSingleValueArray() throws IOException {
        Object[] arr = new Object[]{"Hello"};
        printer.printRecord(arr);
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("Hello", csv);
    }

    /**
     * Test printRecord method with multiple values Iterable
     */
    @Test
    public void testPrintRecordMultipleValuesIterable() throws IOException {
        List<Object> list = Arrays.asList("Hello1", "123", "Hello2");
        printer.printRecord(list);
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("Hello1,123,Hello2\r\n", csv);
    }

    /**
     * Test printRecord method with multiple values Array
     */
    @Test
    public void testPrintRecordMultipleValuesArray() throws IOException {
        Object[] arr = new Object[]{"Hello1", "123", "Hello2"};
        printer.printRecord(arr);
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("Hello1,123,Hello2\r\n", csv);
    }

    /**
     * Test printRecords method with null Iterable
     */
    @Test
    public void testPrintRecordsNullIterable() throws IOException, SQLException {
        printer.printRecords(null);
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("", csv);
    }

    /**
     * Test printRecords method with empty Iterable
     */
    @Test
    public void testPrintRecordsEmptyIterable() throws IOException, SQLException {
        printer.printRecords(Collections.emptyList());
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("", csv);
    }

    /**
     * Test printRecords method with a single value Iterable
     */
    @Test
    public void testPrintRecordsSingleValueIterable() throws IOException, SQLException {
        List<Object> list = Arrays.asList("Hello");
        printer.printRecords(list);
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("Hello", csv);
    }

    /**
     * Test printRecords method with multiple values Iterable
     */
    @Test
    public void testPrintRecordsMultipleValuesIterable() throws IOException, SQLException {
        List<Object> list = Arrays.asList(new Object[]{"Hello1", "123", "Hello2"});
        printer.printRecords(list);
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("Hello1,123,Hello2\r\n", csv);
    }

    /**
     * Test printRecords method with a single value Array
     */
    @Test
    public void testPrintRecordsSingleValueArray() throws IOException, SQLException {
        Object[] arr = new Object[]{"Hello"};
        printer.printRecords(arr);
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("Hello", csv);
    }

    /**
     * Test printRecords method with multiple values Array
     */
    @Test
    public void testPrintRecordsMultipleValuesArray() throws IOException, SQLException {
        Object[] arr = new Object[]{"Hello1", "123", "Hello2"};
        printer.printRecords(arr);
        printer.flush();
        final String csv = outputStream.toString();
        assertEquals("Hello1,123,Hello2\r\n", csv);
    }

    /**
     * Test printRecords method with a JDBC ResultSet
     */
    @Test
    public void testPrintRecordsResultSet() throws IOException, SQLException {
        String query = "SELECT COL1, COL2 FROM TEST_TABLE";
        try (ResultSet rs = CsvUtils.getSqlResultSet(query);
                CSVParser parser = new CSVParserBuilder().buildParser();
                CSVPrinter printer = new CSVPrinterBuilder().buildPrinter()) {
            printer.printRecords(rs, parser);
            printer.flush();
            final String csv = outputStream.toString();
            assertEquals("COL1,COL2\r\nHELLO1,HELLO2\r\n", csv);
        }
    }

    /**
     * Test getOut method
     */
    @Test
    public void testGetOut() {
        assertSame(outputStream, printer.getOut());
    }

    /**
     * Test isNewRecord method
     */
    @Test
    public void testIsNewRecord() throws IOException {
        assertTrue(printer.isNewRecord());
        printer.println();
        printer.flush();
        assertFalse(printer.isNewRecord());
        printer.println();
        printer.flush();
        assertTrue(printer.isNewRecord());
    }
}