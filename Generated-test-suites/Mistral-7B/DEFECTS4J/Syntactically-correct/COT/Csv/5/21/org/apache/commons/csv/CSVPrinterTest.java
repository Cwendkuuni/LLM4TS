package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVPrinter.QUOTE_MINIMAL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

/**
 * Test cases for {@link CSVPrinter} class.
 */
public class CSVPrinterTest {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testConstructor() throws Exception {
        final StringWriter writer = new StringWriter();
        final CSVFormat format = DEFAULT;
        final CSVPrinter printer = new CSVPrinter(writer, format);
        printer.close();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testPrint() throws Exception {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.print("Hi");
        printer.print(123);
        printer.print(true);
        printer.close();

        final String expected = "\"Hi\"123,true\n";
        assertEquals(expected, outStream.toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testPrintAndQuote() throws Exception {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT.withQuotePolicy(QUOTE_MINIMAL);
        final CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.print("Hi, there!");
        printer.close();

        final String expected = "\"Hi, there!\"\n";
        assertEquals(expected, outStream.toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testPrintAndEscape() throws Exception {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT.withEscapeChar('\\').withQuoteMode(CSVFormat.QUOTE_ALL);
        final CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.print("John,\"Do\\'e\" Smith, \"James,O'Grady\"");
        printer.close();

        final String expected = "John,\"Do\\'e\" Smith, \"James,O'Grady\"\n";
        assertEquals(expected, outStream.toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testPrintComment() throws Exception {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT.withCommentingEnabled(true);
        final CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.printComment("This is a test comment.");
        printer.close();

        final String expected = "#This is a test comment.\n";
        assertEquals(expected, outStream.toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testPrintln() throws Exception {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.println("Record 1");
        printer.println("Record 2");
        printer.close();

        final String expected = "Record 1\nRecord 2\n";
        assertEquals(expected, outStream.toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testPrintRecord() throws Exception {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.printRecord(Arrays.asList("Value 1", "Value 2"));
        printer.close();

        final String expected = "Value 1,Value 2\n";
        assertEquals(expected, outStream.toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testPrintRecordObject() throws Exception {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.printRecord(Collections.singletonList(new Object[] { "Value 1", "Value 2" }));
        printer.close();

        final String expected = "Value 1,Value 2\n";
        assertEquals(expected, outStream.toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testPrintRecords() throws Exception {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.printRecords(Arrays.asList(Arrays.asList("Record 1-1", "Record 1-2"),
                Arrays.asList("Record 2-1", "Record 2-2")));
        printer.close();

        final String expected = "Record 1-1,Record 1-2\nRecord 2-1,Record 2-2\n";
        assertEquals(expected, outStream.toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testPrintRecordsObject() throws Exception {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.printRecords(Arrays.asList(new Object[][] { { "Record 1-1", "Record 1-2" },
                { "Record 2-1", "Record 2-2" } }));
        printer.close();

        final String expected = "Record 1-1,Record 1-2\nRecord 2-1,Record 2-2\n";
        assertEquals(expected, outStream.toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testPrintRecordsResultSet() throws Exception {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outStream, format);

        final String sql = "SELECT * FROM test_data";
        final ResultSet resultSet = getResultSet(sql);

        printer.printRecords(resultSet);
        printer.close();

        final String expected = "Name,Age,City\nJohn Doe,25,New York\nJane Doe,30,Los Angeles\n";
        assertEquals(expected, outStream.toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testFlush() throws Exception {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.print("Test");
        printer.flush();
        printer.close();

        final String expected = "Test\n";
        assertEquals(expected, outStream.toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test
    public void testClose() throws Exception {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.print("Test");
        printer.close();

        final String expected = "Test\n";
        assertEquals(expected, outStream.toString());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test(expected = IOException.class)
    public void testCloseTwice() throws Exception {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat format = DEFAULT;
        final CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.print("Test");
        printer.close();
        printer.close();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws Exception {
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        final CSVFormat format = null;

        assertThrows(IllegalArgumentException.class, () -> new CSVPrinter(outStream, format));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testInvalidOutput() throws Exception {
        final String output = null;
        final CSVFormat format = DEFAULT;

        assertThrows(IllegalArgumentException.class, () -> new CSVPrinter(output, format));
    }

    private ResultSet getResultSet(String sql) throws SQLException {
        // Implement this method to return a mock ResultSet for testing.
        throw new UnsupportedOperationException("getResultSet not implemented");
    }
}