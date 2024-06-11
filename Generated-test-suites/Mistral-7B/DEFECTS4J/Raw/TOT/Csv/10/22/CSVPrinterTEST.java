package org.apache.commons.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link CSVPrinter} class.
 */
public class CSVPrinterTest {

    private Connection connection;

    @Before
    public void setUp() throws Exception {
        Class.forName("org.hsqldb.jdbcDriver");
        connection = DriverManager.getConnection("jdbc:hsqldb:mem:testdb", "sa", "");
        createTable();
    }

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    /**
     * Test constructor with null arguments.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullArguments() {
        new CSVPrinter(null, null);
    }

    /**
     * Test print record with simple values.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testPrintRecordSimpleValues() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(1, 2.5f, "three", 'x');

        printer.flush();

        final String expected = "1,2.5,three,x\n";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print record with null values.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testPrintRecordNullValues() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(null, null, null, null);

        printer.flush();

        final String expected = Constants.EMPTY + Constants.EMPTY + Constants.EMPTY + Constants.EMPTY + "\n";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print with escaping.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testPrintWithEscaping() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder()
                .setEscapingChar('\\')
                .build();
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("Hello,\"world\"\n");
        printer.flush();

        final String expected = "Hello,\"world\"\n";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print with quoting.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testPrintWithQuoting() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder()
                .setQuoteMode(QuoteMode.MINIMAL)
                .build();
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("field,\"field with \"quotes\"\n");
        printer.flush();

        final String expected = "field,\"field with \"quotes\"\"\n";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print comment.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testPrintComment() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.printComment("This is a comment");
        printer.flush();

        final String expected = format.getCommentStart() + SP + "This is a comment\n";
        assertEquals(expected, out.toString());
    }

    /**
     * Test flush.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testFlush() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(1, 2.5f, "three", 'x');
        printer.flush();

        final String expected = "1,2.5,three,x\n";
        assertEquals(expected, out.toString());

        printer.printComment("This is a comment");
        printer.flush();

        final String result = out.toString();
        final String expectedWithComment = expected + format.getCommentStart() + SP + "This is a comment\n";
        assertEquals(expectedWithComment, result);
    }

    /**
     * Test close.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testClose() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(1, 2.5f, "three", 'x');
        printer.printComment("This is a comment");
        printer.close();

        final String expected = "1,2.5,three,x\n";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print record with large number of values.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testPrintRecordLargeNumberOfValues() throws IOException {
        final StringWriter out = new StringWriter();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);

        final Object[] values = new Object[1000];
        for (int i = 0; i < values.length; i++) {
            values[i] = Integer.toString(i);
        }
        printer.printRecord(values);
        printer.flush();

        final String expected = String.join(",", values) + "\n";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print records with Iterable.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testPrintRecordsIterable() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecords(Arrays.asList(1, 2.5f, "three", 'x'));
        printer.flush();

        final String expected = "1,2.5,three,x\n";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print records with Object array.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @Test
    public void testPrintRecordsObjectArray() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecords(new Object[] { 1, 2.5f, "three", 'x' });
        printer.flush();

        final String expected = "1,2.5,three,x\n";
        assertEquals(expected, out.toString());
    }

    /**
     * Test print records with JDBC ResultSet.
     *
     * @throws IOException
     *             If an I/O error occurs
     * @throws SQLException
     *             If a database access error occurs
     */
    @Test
    public void testPrintRecordsResultSet() throws SQLException, IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = CSVFormat.DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);

        final String sql = "SELECT 1 AS col1, 2.5 AS col2, 'three' AS col3, 'x' AS col4 FROM dummy";
        final Statement statement = connection.createStatement();
        final ResultSet resultSet = statement.executeQuery(sql);

        printer.printRecords(resultSet);
        printer.flush();

        final String expected = "1,2.5,three,x\n";
        assertEquals(expected, out.toString());
    }

    /**
     * Create table for tests.
     *
     * @throws SQLException
     *             If a database access error occurs
     */
    private void createTable() throws SQLException {
        final String sql = "CREATE TABLE IF NOT EXISTS dummy (col1 INT PRIMARY KEY, col2 FLOAT, col3 VARCHAR(10), col4 CHAR(1))";
        final Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }
}