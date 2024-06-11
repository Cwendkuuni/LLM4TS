package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

/**
 * Test cases for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    private static Connection getConnection() throws SQLException {
        final String url = "jdbc:hsqldb:mem:testdb";
        final String user = "sa";
        final String password = "";

        try {
            Class.forName("org.hsqldb.jdbcDriver");
            return DriverManager.getConnection(url, user, password);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "age" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);
        assertSame(format, printer.getFormat());
        assertSame(out, printer.getOut());
        assertFalse(printer.isNewRecord());
    }

    /**
     * Test print method with null value.
     */
    @Test
    public void testPrintNull() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "age" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.print(null);
        printer.close();

        final String expected = "";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print method with empty string value.
     */
    @Test
    public void testPrintEmpty() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "age" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("");
        printer.close();

        final String expected = "";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print method with non-empty string value.
     */
    @Test
    public void testPrintString() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "age" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("foo");
        printer.close();

        final String expected = "\"foo\"";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print method with integer value.
     */
    @Test
    public void testPrintInt() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "age" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.print(42);
        printer.close();

        final String expected = "42";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print method with double value.
     */
    @Test
    public void testPrintDouble() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "age" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.print(Math.PI);
        printer.close();

        final String expected = "3.141592653589793";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print method with boolean value.
     */
    @Test
    public void testPrintBoolean() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "boolean" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.print(true);
        printer.close();

        final String expected = "true";
        assertEquals(expected, out.toString());

        printer.print(false);
        printer.close();

        expected = "false";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print method with character value.
     */
    @Test
    public void testPrintChar() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "char" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.print('a');
        printer.close();

        final String expected = "'a'";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print method with array values.
     */
    @Test
    public void testPrintArray() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "values" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final String[] values = { "foo", "bar", "baz" };
        printer.print(values);
        printer.close();

        final String expected = "\"foo\",\"bar\",\"baz\"";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print method with iterable values.
     */
    @Test
    public void testPrintIterable() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "values" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final List<String> values = Collections.singletonList("foo");
        printer.print(values);
        printer.close();

        final String expected = "\"foo\"";
        assertEquals(expected, out.toString());

        final List<String> values2 = Arrays.asList("foo", "bar", "baz");
        printer = new CSVPrinter(out, format);
        printer.print(values2);
        printer.close();

        expected = "\"foo\",\"bar\",\"baz\"";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print method with null iterable values.
     */
    @Test
    public void testPrintIterableNull() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "values" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.print(null);
        printer.close();

        assertEquals("", out.toString());
    }

    /**
     * Test print method with array of arrays values.
     */
    @Test
    public void testPrintArrayOfArrays() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "values" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final String[][] values = { { "foo", "bar" }, { "baz" } };
        printer.print(values);
        printer.close();

        final String expected = "\"foo\",\"bar\"" + CR + "\"baz\"";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print method with null array of arrays values.
     */
    @Test
    public void testPrintArrayOfArraysNull() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "values" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.print(null);
        printer.close();

        assertEquals("", out.toString());
    }

    /**
     * Test print method with JDBC result set values.
     */
    @Test
    public void testPrintResultSet() throws SQLException, IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "value" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);

        try (Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT name, age FROM users")) {
            printer.printRecords(resultSet);
            printer.close();
        }

        final String expected = "\"John\",42" + CR + "\"Mary\",30" + CR;
        assertEquals(expected, out.toString());
    }

    /**
     * Test print method with CR and LF characters.
     */
    @Test
    public void testPrintCRandLF() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "values" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("\r\n");
        printer.close();

        final String expected = CR + LF;
        assertEquals(expected, out.toString());
    }

    /**
     * Test printComment method.
     */
    @Test
    public void testPrintComment() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "comment" }, Quote.MINIMAL, '\r', '\n');
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.printComment("This is a test comment.");
        printer.close();

        final String expected = "\r\n" + format.getCommentStart() + " This is a test comment.\r\n";
        assertEquals(expected, out.toString());
    }

    /**
     * Test flush method.
     */
    @Test
    public void testFlush() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "age" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("foo");
        printer.flush();
        assertEquals("", out.toString());

        printer.print("bar");
        printer.flush();
        assertEquals("\"" + Constants.CR + "\"foo\"" + Constants.CR, out.toString());

        printer.print("baz");
        printer.flush();
        assertEquals("\"" + Constants.CR + "\"foo\"" + Constants.CR + "\"" + Constants.CR + "\"bar\"" + Constants.CR, out.toString());

        printer.print("qux");
        printer.flush();
        assertEquals("\"" + Constants.CR + "\"foo\"" + Constants.CR + "\"" + Constants.CR + "\"bar\"" + Constants.CR + "\"" + Constants.CR + "\"qux\"" + Constants.CR, out.toString());

        printer.close();
    }

    /**
     * Test close method.
     */
    @Test
    public void testClose() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(new String[] { "name", "age" }, Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("foo");
        printer.print("bar");
        printer.print("baz");
        printer.close();

        final String expected = "\"" + Constants.CR + "\"foo\"" + Constants.CR + "\"" + Constants.CR + "\"bar\"" + Constants.CR + "\"" + Constants.CR + "\"baz\"" + Constants.CR;
        assertEquals(expected, out.toString());

        try {
            printer.print("qux");
            fail("Expected I/O exception");
        } catch (final IOException e) {
            // Expected
        }
    }
}