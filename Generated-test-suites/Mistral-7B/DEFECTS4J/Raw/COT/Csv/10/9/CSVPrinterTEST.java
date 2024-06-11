package org.apache.commons.csv.test;

import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
 * Test the functionality of the CSVPrinter class.
 *
 * @author Stephen Colebourn
 */
public class CSVPrinterTest {

    private ByteArrayOutputStream outputStream;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        try {
            printer = new CSVPrinter(outputStream, CSVFormat.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assert null != printer;
    }

    /**
     * Test print method.
     */
    @Test
    public void testPrint() throws IOException {
        printer.print("Hello World");
        String result = outputStream.toString();
        assertEquals("Hello World", result);
    }

    /**
     * Test print method with null value.
     */
    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        String result = outputStream.toString();
        assertEquals("", result);
    }

    /**
     * Test print method with escape character.
     */
    @Test
    public void testPrintEscape() throws IOException {
        printer.print("Hello\rWorld");
        String result = outputStream.toString();
        assertEquals("Hello\rWorld", result);
    }

    /**
     * Test print method with quote character.
     */
    @Test
    public void testPrintQuote() throws IOException {
        printer.print("Hello\"World");
        String result = outputStream.toString();
        assertEquals("Hello\"World", result);
    }

    /**
     * Test print method with quote and escape character.
     */
    @Test
    public void testPrintQuoteAndEscape() throws IOException {
        printer.print("Hello\"World\\");
        String result = outputStream.toString();
        assertEquals("Hello\"World\\", result);
    }

    /**
     * Test println method.
     */
    @Test
    public void testPrintln() throws IOException {
        printer.println("Hello World");
        String result = outputStream.toString();
        assertEquals(LF + "Hello World", result);
    }

    /**
     * Test println method with empty line.
     */
    @Test
    public void testPrintlnEmpty() throws IOException {
        printer.println();
        String result = outputStream.toString();
        assertEquals(LF, result);
    }

    /**
     * Test println method with comment.
     */
    @Test
    public void testPrintlnComment() throws IOException {
        printer.setComment("test comment");
        printer.println();
        printer.printComment("test comment");
        printer.println();
        String result = outputStream.toString();
        assertEquals(LF + COMMENT + SP + "test comment" + LF + LF, result);
    }

    /**
     * Test print record method.
     */
    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(new Object[] { "Hello", "World" });
        String result = outputStream.toString();
        assertEquals("Hello,World" + LF, result);
    }

    /**
     * Test print record method with null value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPrintRecordNull() throws IOException {
        printer.printRecord(new Object[] { "Hello", null });
    }

    /**
     * Test print record method with empty array.
     */
    @Test
    public void testPrintRecordEmpty() throws IOException {
        printer.printRecord(new Object[] {});
        String result = outputStream.toString();
        assertEquals("", result);
    }

    /**
     * Test printRecords method.
     */
    @Test
    public void testPrintRecords() throws IOException {
        printer.printRecords(new Object[] { "Hello", "World" });
        String result = outputStream.toString();
        assertEquals("Hello,World" + LF, result);
    }

    /**
     * Test printRecords method with empty array.
     */
    @Test
    public void testPrintRecordsEmpty() throws IOException {
        printer.printRecords(new Object[] {});
        String result = outputStream.toString();
        assertEquals("", result);
    }

    /**
     * Test printRecords method with null values.
     */
    @Test
    public void testPrintRecordsNull() throws IOException {
        printer.printRecords(new Object[] { "Hello", null });
        String result = outputStream.toString();
        assertEquals("Hello,", result);
    }

    /**
     * Test printRecords method with Iterable.
     */
    @Test
    public void testPrintRecordsIterable() throws IOException {
        printer.printRecords(Arrays.asList(new Object[] { "Hello", "World" }));
        String result = outputStream.toString();
        assertEquals("Hello,World" + LF, result);
    }

    /**
     * Test printRecords method with empty Iterable.
     */
    @Test
    public void testPrintRecordsIterableEmpty() throws IOException {
        printer.printRecords(Arrays.asList(new Object[] {}));
        String result = outputStream.toString();
        assertEquals("", result);
    }

    /**
     * Test printRecords method with null Iterable.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPrintRecordsIterableNull() throws IOException {
        printer.printRecords(null);
    }

    /**
     * Test printRecords method with JDBC result set.
     */
    @Test
    public void testPrintRecordsResultSet() throws SQLException, IOException {
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(resultStream, CSVFormat.DEFAULT);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.getString(1)).thenReturn("Hello");
        when(resultSet.getString(2)).thenReturn("World");
        when(resultSet.next()).thenReturn(true);
        printer.printRecords(resultSet);
        String result = resultStream.toString();
        assertEquals("Hello,World" + LF, result);
    }

    /**
     * Test printRecords method with empty JDBC result set.
     */
    @Test
    public void testPrintRecordsResultSetEmpty() throws SQLException, IOException {
        ByteArrayOutputStream resultStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(resultStream, CSVFormat.DEFAULT);
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        when(resultSet.next()).thenReturn(false);
        printer.printRecords(resultSet);
        String result = resultStream.toString();
        assertEquals("", result);
    }

    /**
     * Test printComment method.
     */
    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("test comment");
        String result = outputStream.toString();
        assertEquals(LF + COMMENT + SP + "test comment" + LF, result);
    }

    /**
     * Test printComment method with empty comment.
     */
    @Test
    public void testPrintCommentEmpty() throws IOException {
        printer.printComment("");
        String result = outputStream.toString();
        assertEquals(LF + COMMENT + SP + "" + LF, result);
    }

    /**
     * Test printComment method with null comment.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testPrintCommentNull() throws IOException {
        printer.printComment(null);
    }

    /**
     * Test close method.
     */
    @Test
    public void testClose() throws IOException {
        printer.close();
        assertThrows(IllegalStateException.class, () -> printer.println());
    }

    /**
     * Test flush method.
     */
    @Test
    public void testFlush() throws IOException {
        printer.flush();
    }
}