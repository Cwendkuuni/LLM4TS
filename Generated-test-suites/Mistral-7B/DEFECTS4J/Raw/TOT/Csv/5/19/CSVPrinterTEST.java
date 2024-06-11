package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

/**
 * Tests for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    /**
     * Test of CSVPrinter construction.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testCSVPrinterConstruction() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteAll();
        final CSVPrinter printer = new CSVPrinter(out, format);
    }

    /**
     * Test of print method.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testPrint() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.print("hello");
        printer.flush();
        final String result = out.toString();
        assertEquals("hello", result);
    }

    /**
     * Test of print method with null value.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testPrintNull() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.print(null);
        printer.flush();
        final String result = out.toString();
        assertTrue(result.isEmpty() || result.matches("^[,\\n]$"));
    }

    /**
     * Test of print method with escape character.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testPrintEscapeCharacter() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.print("hello\\world");
        printer.flush();
        final String result = out.toString();
        assertEquals("hello\\world", result);
    }

    /**
     * Test of print method with quote character.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testPrintQuoteCharacter() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.print("hello\"world");
        printer.flush();
        final String result = out.toString();
        assertEquals("hello\"world", result);
    }

    /**
     * Test of printAndQuote method.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testPrintAndQuote() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.printAndQuote("hello\"world");
        printer.flush();
        final String result = out.toString();
        assertEquals("\"hello\"\"world\"", result);
    }

    /**
     * Test of printAndQuote method with null value.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testPrintAndQuoteNull() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.printAndQuote(null);
        printer.flush();
        final String result = out.toString();
        assertNull(result);
    }

    /**
     * Test of println method.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testPrintln() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.println("hello");
        printer.flush();
        final String result = out.toString();
        assertEquals("hello\n", result);
    }

    /**
     * Test of println method with empty line.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testPrintlnEmptyLine() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.println();
        printer.flush();
        final String result = out.toString();
        assertTrue(result.isEmpty() || result.matches("^[,\\n]$"));
    }

    /**
     * Test of println method with comment.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testPrintlnComment() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT.withComment('#');
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.println("# This is a comment");
        printer.flush();
        final String result = out.toString();
        assertEquals("# This is a comment\n", result);
    }

    /**
     * Test of printRecord method with single object.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testPrintRecordSingleObject() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.printRecord(new Object[] {"hello"});
        printer.flush();
        final String result = out.toString();
        assertEquals("hello", result);
    }

    /**
     * Test of printRecord method with multiple objects.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testPrintRecordMultipleObjects() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.printRecord(new Object[] {"hello", "world"});
        printer.flush();
        final String result = out.toString();
        assertEquals("hello,world", result);
    }

    /**
     * Test of printRecord method with null object.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testPrintRecordNullObject() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.printRecord(new Object[] {null});
        printer.flush();
        final String result = out.toString();
        assertTrue(result.isEmpty() || result.matches("^[,\\n]$"));
    }

    /**
     * Test of printRecords method with single iterable.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testPrintRecordsSingleIterable() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.printRecords(Arrays.asList(new Object[] {"hello"}));
        printer.flush();
        final String result = out.toString();
        assertEquals("hello", result);
    }

    /**
     * Test of printRecords method with multiple iterables.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testPrintRecordsMultipleIterables() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.printRecords(Arrays.asList(new Object[] {"hello"}, new Object[] {"world"}));
        printer.flush();
        final String result = out.toString();
        assertEquals("hello,world", result);
    }

    /**
     * Test of printRecords method with null iterable.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testPrintRecordsNullIterable() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.printRecords(null);
        printer.flush();
        final String result = out.toString();
        assertTrue(result.isEmpty() || result.matches("^[,\\n]$"));
    }

    /**
     * Test of printRecords method with result set.
     *
     * @throws Exception
     *             if an error occurs
     */
    @org.junit.Test
    public void testPrintRecordsResultSet() throws Exception {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);
        final ResultSet resultSet = ResultSetCreator.createResultSet();
        printer.printRecords(resultSet);
        printer.flush();
        final String result = out.toString();
        assertFalse(result.isEmpty());
        final CSVRecord record = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(result);
        final List<String> headers = record.toList();
        assertEquals(Arrays.asList("COLUMN_1", "COLUMN_2"), headers);
    }
}