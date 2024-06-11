package org.apache.commons.csv;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

/**
 * Test case for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    /**
     * Test the constructor.
     */
    @Test
    public void testConstructor() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(';').build();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);
        assertEquals("CSVPrinter[out=ByteArrayOutputStream@1b4d7422, format=CSVFormat{...}]", printer.toString());

        printer.close();
    }

    /**
     * Test printing a single value.
     */
    @Test
    public void testPrint() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(',').build();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        // test null value
        printer.print(null);
        assertEquals("\"\",", out.toString());

        // test empty string
        printer.print("");
        assertEquals("\"\",", out.toString());

        // test simple value
        printer.print("foo");
        assertEquals("foo", out.toString());

        printer.flush();
        printer.close();
    }

    /**
     * Test printing multiple values.
     *
     * @throws IOException
     */
    @Test
    public void testPrintMultipleValues() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(',').build();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.print("foo");
        printer.print("bar");
        printer.print("baz");
        printer.println();

        assertEquals("foo,bar,baz\n", out.toString());

        printer.flush();
        printer.close();
    }

    /**
     * Test quoting values.
     *
     * @throws IOException
     */
    @Test
    public void testPrintQuoting() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(',')
                .setQuoteChar('"').build();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.print("foo bar baz");
        assertEquals("\"foo bar baz\",", out.toString());

        printer.print("foo,\"bar baz\"");
        assertEquals("\"foo\",\"bar baz\",", out.toString());

        printer.print("foo,\"bar \"baz\"");
        assertEquals("\"foo\",\"bar \\\"baz\\\"\",", out.toString());

        printer.print("foo,\"bar\"baz");
        assertEquals("\"foo\",\"bar\"baz,", out.toString());

        printer.print("foo,\"bar\"baz,\"qux\"");
        assertEquals("\"foo\",\"bar\"baz,\"qux\",", out.toString());

        printer.print("foo,\"bar\"baz,\"qux\",quux");
        assertEquals("\"foo\",\"bar\"baz,\"qux\",\"quux\",", out.toString());

        printer.flush();
        printer.close();
    }

    /**
     * Test escaping values.
     *
     * @throws IOException
     */
    @Test
    public void testPrintEscaping() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(',')
                .setEscapeChar('\\').build();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.print("foo,bar,baz");
        assertEquals("foo,bar,baz", out.toString());

        printer.print("foo,bar,\"baz\"");
        assertEquals("foo,bar,\"baz\",", out.toString());

        printer.print("foo,bar,\"baz\\\"");
        assertEquals("foo,bar,\"baz\\\",", out.toString());

        printer.print("foo,bar,\"baz\\\"qux\"");
        assertEquals("foo,bar,\"baz\\\"qux\",", out.toString());

        printer.print("foo,bar,\"baz\\\"qux\\\"");
        assertEquals("foo,bar,\"baz\\\"qux\\\"", out.toString());

        printer.print("foo,bar,\"baz\\\"qux\\\"quuz\"");
        assertEquals("foo,bar,\"baz\\\"qux\\\"quuz\",", out.toString());

        printer.print("foo,bar,\"baz\\\"qux\\\"quuz\",quux");
        assertEquals("foo,bar,\"baz\\\"qux\\\"quuz\",\"quux\",", out.toString());

        printer.print("foo,bar,\"baz\\\"qux\\\"quuz\",quux,quux");
        assertEquals(
                "foo,bar,\"baz\\\"qux\\\"quuz\",\"quux\",\"quux\",", out.toString());

        printer.flush();
        printer.close();
    }

    /**
     * Test printing with comments.
     *
     * @throws IOException
     */
    @Test
    public void testPrintComments() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(',')
                .setCommentStart('#').build();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.printComment("This is a comment");
        assertEquals("#This is a comment\n", out.toString());

        printer.print("foo");
        assertEquals("foo", out.toString());

        printer.println();

        printer.printComment("Another comment");
        assertEquals("#Another comment\nfoo,", out.toString());

        printer.print("bar");
        assertEquals("foo,bar\n", out.toString());

        printer.println();

        printer.printComment("Final comment");
        assertEquals("#Final comment\nfoo,bar\n", out.toString());

        printer.print("baz");
        assertEquals("foo,bar,baz\n", out.toString());

        printer.flush();
        printer.close();
    }

    /**
     * Test printing multiple records.
     *
     * @throws IOException
     */
    @Test
    public void testPrintMultipleRecords() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(',')
                .setRecordSeparator("\n").build();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.print("foo");
        printer.print("bar");
        printer.println();

        printer.print("baz");
        printer.print("qux");
        printer.println();

        printer.flush();
        printer.close();

        assertArrayEquals(new String[]{"foo,bar\n", "baz,qux\n"}, out.toString().split("\n"));
    }

    /**
     * Test printing an empty record.
     *
     * @throws IOException
     */
    @Test
    public void testPrintEmptyRecord() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(',')
                .setRecordSeparator("\n").build();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.printRecord(Collections.emptyList());
        printer.println();

        printer.flush();
        printer.close();

        assertTrue(out.toString().isEmpty());
    }

    /**
     * Test printing an array of values.
     *
     * @throws IOException
     */
    @Test
    public void testPrintArray() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(',')
                .setRecordSeparator("\n").build();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.printRecord(new String[]{"foo", "bar", "baz"});
        printer.println();

        printer.flush();
        printer.close();

        assertEquals("foo,bar,baz\n", out.toString());
    }

    /**
     * Test printing an Iterable of values.
     *
     * @throws IOException
     */
    @Test
    public void testPrintIterable() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(',')
                .setRecordSeparator("\n").build();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.printRecord(Arrays.asList("foo", "bar", "baz"));
        printer.println();

        printer.flush();
        printer.close();

        assertEquals("foo,bar,baz\n", out.toString());
    }

    /**
     * Test printing a Collection of values.
     *
     * @throws IOException
     */
    @Test
    public void testPrintCollection() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(',')
                .setRecordSeparator("\n").build();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        final Collection<String> values = new ArrayList<>();
        values.add("foo");
        values.add("bar");
        values.add("baz");

        printer.printRecords(values);
        printer.println();

        printer.flush();
        printer.close();

        assertEquals("foo,bar,baz\n", out.toString());
    }

    /**
     * Test printing a JDBC ResultSet.
     *
     * @throws IOException
     * @throws SQLException
     */
    @Test
    public void testPrintResultSet() throws IOException, SQLException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out),
                new CSVFormatBuilder().setHeaderNames(true).build());

        final ResultSet rs = createMockResultSet();
        printer.printRecords(rs);

        printer.flush();
        printer.close();

        // verify header
        assertEquals("Name,Age,City\n", out.toString().substring(0, out.toString().indexOf("\n") + 1));

        // verify data
        assertEquals(
                "Alice,25,New York\nBob,30,Boston\nJim,35,Chicago\n", out.toString().substring(out.toString().indexOf("\n") + 1));
    }

    /**
     * Test flushing the printer.
     *
     * @throws IOException
     */
    @Test
    public void testFlush() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(',')
                .setRecordSeparator("\n").build();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.print("foo");
        printer.print("bar");
        printer.println();

        printer.flush();

        assertEquals("foo,bar\n", out.toString());
        assertEquals(0, out.size());

        printer.close();
    }

    /**
     * Test closing the printer.
     *
     * @throws IOException
     */
    @Test
    public void testClose() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(',')
                .setRecordSeparator("\n").build();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.print("foo");
        printer.print("bar");
        printer.println();

        printer.close();

        assertEquals("foo,bar\n", out.toString());
        assertTrue(out.toString().isEmpty());
    }

    /**
     * Test that an exception is thrown when the output stream is closed.
     *
     * @throws IOException
     */
    @Test(expected = IOException.class)
    public void testClosedOutput() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(',')
                .setRecordSeparator("\n").build();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        out.close();
        printer.print("foo");
    }

    /**
     * Test that an exception is thrown when the format is null.
     *
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), null);
    }

    /**
     * Test that an exception is thrown when the output is null.
     *
     * @throws IOException
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullOutput() throws IOException {
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(',')
                .setRecordSeparator("\n").build();
        final CSVPrinter printer = new CSVPrinter(null, format);
    }

    /**
     * Create a mock ResultSet.
     *
     * @return a mock ResultSet
     * @throws SQLException
     */
    private ResultSet createMockResultSet() throws SQLException {
        final ResultSet resultSet = Mockito.mock(ResultSet.class);
        final String[] headers = {"Name", "Age", "City"};
        final Object[][] data = {{"Alice", new Integer(25), "New York"}, {"Bob", new Integer(30), "Boston"},
                {"Jim", new Integer(35), "Chicago"}};

        Mockito.when(resultSet.getMetaData()).thenReturn(new MockResultSetMetaData(headers));
        Mockito.when(resultSet.next()).thenAnswer(i -> {
            boolean last = (i > data.length - 1);
            if (!last) {
                Mockito.when(resultSet.getString(1)).thenReturn(data[i][0].toString());
                Mockito.when(resultSet.getInt(2)).thenReturn(data[i][1].hashCode());
                Mockito.when(resultSet.getString(3)).thenReturn(data[i][2].toString());
            }
            return !last;
        });

        return resultSet;
    }
}