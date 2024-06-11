package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

/**
 * Test cases for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(output, format);
        // check that the printer was initialized correctly
        assertEquals(output, printer.getOut());
        assertEquals(format, printer.getFormat());
        assertFalse(printer.newRecord);
    }

    /**
     * Test print method with null value.
     */
    @Test
    public void testPrintNull() throws IOException {
        // setup
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(output, format);

        // execute
        printer.print(null);

        // verify
        assertEquals("\"\",", output.toString().trim());
    }

    /**
     * Test print method with non-null value.
     */
    @Test
    public void testPrintValue() throws IOException {
        // setup
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(output, format);

        // execute
        printer.print("foo");

        // verify
        assertEquals("foo", output.toString().trim());
    }

    /**
     * Test print method with empty string.
     */
    @Test
    public void testPrintEmptyString() throws IOException {
        // setup
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(output, format);

        // execute
        printer.print("");

        // verify
        assertEquals("\"\"", output.toString().trim());
    }

    /**
     * Test print method with empty array.
     */
    @Test
    public void testPrintEmptyArray() throws IOException {
        // setup
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(output, format);

        // execute
        printer.print((Object[]) new Object[0]);

        // verify
        assertEquals("", output.toString().trim());
    }

    /**
     * Test print method with non-empty array.
     */
    @Test
    public void testPrintArray() throws IOException {
        // setup
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(output, format);

        // execute
        printer.print(new String[]{"foo", "bar"});

        // verify
        assertEquals("foo,bar", output.toString().trim());
    }

    /**
     * Test print method with non-empty iterable.
     */
    @Test
    public void testPrintIterable() throws IOException {
        // setup
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(output, format);

        // execute
        printer.print(Arrays.asList(new String[]{"foo", "bar"}));

        // verify
        assertEquals("foo,bar", output.toString().trim());
    }

    /**
     * Test print method with object.
     */
    @Test
    public void testPrintObject() throws IOException {
        // setup
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(output, format);

        // execute
        printer.print(new StringBuilder("foo"));

        // verify
        assertEquals("foo", output.toString().trim());
    }

    /**
     * Test print method with char sequence.
     */
    @Test
    public void testPrintCharSequence() throws IOException {
        // setup
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(output, format);

        // execute
        printer.print("foo");

        // verify
        assertEquals("foo", output.toString().trim());
    }

    /**
     * Test print method with special characters.
     */
    @Test
    public void testPrintSpecialCharacters() throws IOException {
        // setup
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(output, format);

        // execute
        printer.print("\f\n\r\t\b\b\b\"\"");

        // verify
        assertEquals("\"\\f\",\"\\n\",\"\\r\",\"\\t\",\"\\b\",\"\\b\",\"\\\"\\\"", output.toString().trim());
    }

    /**
     * Test print method with quoting enabled.
     */
    @Test
    public void testPrintQuotingEnabled() throws IOException {
        // setup
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.ALL);
        final CSVPrinter printer = new CSVPrinter(output, format);

        // execute
        printer.print("foo,bar");

        // verify
        assertEquals("\"foo,\"\"bar\"", output.toString().trim());
    }

    /**
     * Test print method with escaping enabled.
     */
    @Test
    public void testPrintEscapingEnabled() throws IOException {
        // setup
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.NONE);
        final CSVPrinter printer = new CSVPrinter(output, format);

        // execute
        printer.print("foo,\"bar,\"baz");

        // verify
        assertEquals("foo,\"bar,\"baz", output.toString().trim());
    }

    /**
     * Test print method with commenting enabled.
     */
    @Test
    public void testPrintCommentingEnabled() throws IOException {
        // setup
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(output, format);

        // execute
        printer.printComment("This is a comment");

        // verify
        String expected = format.getCommentStart().charValue() + SP + "This is a comment" + CR + format.getCommentStart().charValue() + SP;
        assertEquals(expected, output.toString().trim());
    }

    /**
     * Test print method with record separator.
     */
    @Test
    public void testPrintRecordSeparator() throws IOException {
        // setup
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(output, format);

        // execute
        printer.println();

        // verify
        assertEquals(format.getRecordSeparator(), output.toString().trim());
    }

    /**
     * Test print method with multiple records.
     */
    @Test
    public void testPrintMultipleRecords() throws IOException, SQLException {
        // setup
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(output, format);

        // execute
        printer.printRecords(Arrays.asList(new String[]{"foo", "bar"}, new String[]{"baz", "qux"}));

        // verify
        String expected = "foo,bar" + CR + "baz,qux";
        assertEquals(expected, output.toString().trim());
    }

    /**
     * Test print method with null values in records.
     */
    @Test
    public void testPrintNullValues() throws IOException {
        // setup
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(output, format);

        // execute
        printer.printRecords(Arrays.asList(new Object[]{null, "bar"}, new Object[]{null}));

        // verify
        String expected = "\"\",\"bar\"" + CR + "\"\",";
        assertEquals(expected, output.toString().trim());
    }

    /**
     * Test print method with multiple types of records.
     */
    @Test
    public void testPrintMixedTypesRecords() throws IOException, SQLException {
        // setup
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(output, format);

        // execute
        printer.printRecords(Arrays.asList(new String[]{"foo", "bar"}, new Integer[]{1, 2}, new ResultSet[]{null}));

        // verify
        String expected = "foo,bar" + CR + "1,2" + CR + "";
        assertEquals(expected, output.toString().trim());
    }

}