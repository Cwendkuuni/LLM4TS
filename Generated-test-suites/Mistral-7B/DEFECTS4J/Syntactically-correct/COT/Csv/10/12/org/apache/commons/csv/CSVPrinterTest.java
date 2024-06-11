package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVPrinter.COMMENT;
import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CSVPrinter.
 */
public class CSVPrinterTest {

    private ByteArrayOutputStream outputStream;
    private CSVPrinter printer;
    private CSVFormat format;

    @Before
    public void setUp() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withHeader());
        outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(new StringWriter(outputStream), format);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    /**
     * Test constructor.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testConstructor() throws IOException {
        CSVPrinter printer = new CSVPrinter(new StringWriter(new ByteArrayOutputStream()), format);
    }

    /**
     * Test print method with object as argument.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintMethodObject() throws IOException {
        printer.print("Hello");
        String result = outputStream.toString();
        assertEquals("Hello", result);

        printer.print(null);
        result = outputStream.toString();
        assertEquals(format.getNullString(), result);

        printer.print(42);
        result = outputStream.toString();
        assertEquals("42", result);

        printer.print(new Object[] { 1, "two", 3.0 });
        result = outputStream.toString();
        assertEquals("1,two,3.0", result);
    }

    /**
     * Test print method with CharSequence as argument.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintMethodCharSequence() throws IOException {
        printer.print("Hello");
        String result = outputStream.toString();
        assertEquals("Hello", result);

        printer.print(null);
        result = outputStream.toString();
        assertEquals(format.getNullString(), result);

        printer.print("Hello, World!");
        result = outputStream.toString();
        assertEquals("Hello, World!", result);

        printer.print(new StringBuilder("Hello, World!").reverse().toString());
        result = outputStream.toString();
        assertEquals("World! dlroW ,olleH", result);

        printer.print("123" + LF + "456");
        result = outputStream.toString();
        assertEquals("123\n456", result);

        printer.print("123" + CR + "456");
        result = outputStream.toString();
        assertEquals("123\r456", result);
    }

    /**
     * Test print method with Object and CharSequence as arguments.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintMethodObjectAndCharSequence() throws IOException {
        printer.print("Hello", "world");
        String result = outputStream.toString();
        assertEquals("Hello,world", result);
    }

    /**
     * Test println method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintlnMethod() throws IOException {
        printer.println("Hello");
        String result = outputStream.toString();
        assertEquals("Hello\n", result);

        printer.println();
        result = outputStream.toString();
        assertEquals(format.getRecordSeparator(), result);

        printer.println("Hello", "World!");
        result = outputStream.toString();
        assertEquals("Hello\nHello,World!", result);
    }

    /**
     * Test printRecord method with Iterable as argument.
     *
     * @throws IOException if an I/O error occurs
     * @throws SQLException if a database access error occurs
     */
    @Test
    public void testPrintRecordIterable() throws IOException, SQLException {
        ResultSet resultSet = TestHelpers.createMockResultSet(new Object[] { "1", "two", "three" });
        printer.printRecords(resultSet);
        String result = outputStream.toString();
        assertEquals("1,two,three\n", result);
    }

    /**
     * Test printRecord method with Object array as argument.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintRecordObjectArray() throws IOException {
        printer.printRecord(new Object[] { "1", "two", "three" });
        String result = outputStream.toString();
        assertEquals("1,two,three\n", result);

        printer.printRecord(new String[] { "1", "two", "three" });
        result = outputStream.toString();
        assertEquals("1,two,three\n", result);
    }

    /**
     * Test printRecord method with Iterable of Object arrays as argument.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintRecordIterableObjectArray() throws IOException {
        printer.printRecords(new Object[][] { { "1", "two", "three" }, { "four", "five", "six" } });
        String result = outputStream.toString();
        assertEquals("1,two,three\nfour,five,six\n", result);
    }

    /**
     * Test printComment method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintCommentMethod() throws IOException {
        printer.printComment("Test comment");
        String result = outputStream.toString();
        assertEquals("# Test comment\n", result);

        printer.printComment(COMMENT + "Test comment");
        result = outputStream.toString();
        assertEquals("# Test comment\n", result);
    }

    /**
     * Test flush method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testFlushMethod() throws IOException {
        printer.print("Hello");
        printer.flush();
        String result = outputStream.toString();
        assertEquals("Hello\n", result);

        printer.close();
    }

    /**
     * Test close method.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testCloseMethod() throws IOException {
        printer.close();
        assertThrows(IOException.class, () -> printer.print("Hello"));
    }

    /**
     * Test print method with escaping enabled.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintEscaping() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withEscapingEnabled(true));
        printer = new CSVPrinter(new StringWriter(outputStream), format);

        printer.print("Hello, World!");
        String result = outputStream.toString();
        assertEquals("Hello,World!\n", result);

        printer.print("\"Hello, World!\"");
        result = outputStream.toString();
        assertEquals("\"Hello, World!\",Hello, World!\n", result);

        printer.print("123\r456");
        result = outputStream.toString();
        assertEquals("123\r456\r", result);

        printer.print("123\n456");
        result = outputStream.toString();
        assertEquals("123\n456\n", result);
    }

    /**
     * Test print method with quoting enabled.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintQuoting() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withQuoting(Quote.ALL));
        printer = new CSVPrinter(new StringWriter(outputStream), format);

        printer.print('"' + "Hello, World!" + '"');
        String result = outputStream.toString();
        assertEquals("\"Hello, World!\"\n", result);

        printer.print("123\r456");
        result = outputStream.toString();
        assertEquals("\"123\"\r\"456\"\r", result);

        printer.print("123" + CR + "456");
        result = outputStream.toString();
        assertEquals("\"123\"\r\"456\"\r", result);
    }

    /**
     * Test print method with different quoting policies.
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testPrintQuotingPolicies() throws IOException {
        format = new CSVFormat(CSVFormat.DEFAULT.withQuoting(Quote.NONE));
        printer = new CSVPrinter(new StringWriter(outputStream), format);

        printer.print("Hello, World!");
        String result = outputStream.toString();
        assertEquals("Hello, World!", result);

        format = new CSVFormat(CSVFormat.DEFAULT.withQuoting(Quote.NON_NUMERIC));
        printer = new CSVPrinter(new StringWriter(outputStream), format);

        printer.print("Hello, World!");
        result = outputStream.toString();
        assertEquals("\"Hello, World!\",Hello, World!", result);

        printer.print("123, 456");
        result = outputStream.toString();
        assertEquals("123, 456", result);

        format = new CSVFormat(CSVFormat.DEFAULT.withQuoting(Quote.MINIMAL));
        printer = new CSVPrinter(new StringWriter(outputStream), format);

        printer.print("Hello, World!");
        result = outputStream.toString();
        assertEquals("Hello, World!\n", result);

        printer.print("123\r456");
        result = outputStream.toString();
        assertEquals("\"123\"\r\"456\"\r", result);
    }
}