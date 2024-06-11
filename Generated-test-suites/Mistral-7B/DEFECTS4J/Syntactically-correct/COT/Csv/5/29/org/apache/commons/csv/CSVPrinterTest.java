package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVPrinter.COMMENT;
import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
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

    private ByteArrayOutputStream out;
    private CSVPrinter printer;
    private CSVFormat format;

    @Before
    public void setUp() throws IOException {
        out = new ByteArrayOutputStream();
        format = CSVFormat.DEFAULT;
        printer = new CSVPrinter(new OutputStreamWriter(out), format);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    // Test constructor --------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullOut() {
        new CSVPrinter(null, format);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullFormat() {
        new CSVPrinter(out, null);
    }

    // Test close --------------------------------------------------

    @Test
    public void testClose() throws IOException {
        printer.close();
        assertFalse(printer.isOpen());
    }

    // Test flush --------------------------------------------------

    @Test
    public void testFlush() throws IOException {
        printer.flush();
    }

    // Test print --------------------------------------------------

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals(Constants.EMPTY, result);
    }

    @Test
    public void testPrintEmpty() throws IOException {
        printer.print("");
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals(Constants.EMPTY, result);
    }

    @Test
    public void testPrintChar() throws IOException {
        printer.print((char) 0xFFFD);
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("\uFFFD", result);
    }

    @Test
    public void testPrintNumber() throws IOException {
        printer.print(123);
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("123", result);
    }

    @Test
    public void testPrintString() throws IOException {
        printer.print("Hello");
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("Hello", result);
    }

    @Test
    public void testPrintQuoteString() throws IOException {
        printer.format = new CSVFormat('"', ',', Quote.MINIMAL);
        printer.print("Hello");
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("\"Hello\"", result);
    }

    @Test
    public void testPrintEscapedString() throws IOException {
        printer.format = CSVFormat.DEFAULT.withQuote('"').withEscape('\u001E');
        printer.print("Hello\u001Eworld");
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("\"Hello\\u001Eworld\"", result);
    }

    // Test print with multiple values --------------------------------------------------

    @Test
    public void testPrintMultipleValues() throws IOException {
        printer.print(123);
        printer.print("Hello");
        printer.print(false);
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("123,Hello,false", result);
    }

    @Test
    public void testPrintMultipleValuesQuote() throws IOException {
        printer.format = new CSVFormat('"', ',', Quote.ALL);
        printer.print(123);
        printer.print("Hello");
        printer.print(false);
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("\"123\",\"Hello\",\"false\"", result);
    }

    // Test print record --------------------------------------------------

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(Arrays.asList("123", "Hello", "false"));
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("123,Hello,false", result);
    }

    @Test
    public void testPrintRecordQuote() throws IOException {
        printer.format = new CSVFormat('"', ',', Quote.ALL);
        printer.printRecord(Arrays.asList("123", "Hello", "false"));
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("\"123\",\"Hello\",\"false\"", result);
    }

    @Test
    public void testPrintRecordStringArray() throws IOException {
        printer.printRecord(new String[] {"123", "Hello", "false"});
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("123,Hello,false", result);
    }

    @Test
    public void testPrintRecordStringArrayQuote() throws IOException {
        printer.format = new CSVFormat('"', ',', Quote.ALL);
        printer.printRecord(new String[] {"123", "Hello", "false"});
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("\"123\",\"Hello\",\"false\"", result);
    }

    @Test
    public void testPrintRecordIterable() throws IOException {
        List<String> list = Collections.singletonList("Hello");
        printer.printRecord(list);
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("\"Hello\"", result);
    }

    @Test
    public void testPrintRecordIterableQuote() throws IOException {
        printer.format = new CSVFormat('"', ',', Quote.ALL);
        List<String> list = Collections.singletonList("Hello");
        printer.printRecord(list);
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("\"Hello\"", result);
    }

    // Test printRecord with Object[] --------------------------------------------------

    @Test
    public void testPrintRecordObjectArray() throws IOException {
        printer.printRecord(new Object[] {123, "Hello", false});
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("123,Hello,false", result);
    }

    @Test
    public void testPrintRecordObjectArrayQuote() throws IOException {
        printer.format = new CSVFormat('"', ',', Quote.ALL);
        printer.printRecord(new Object[] {123, "Hello", false});
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("\"123\",\"Hello\",\"false\"", result);
    }

    // Test printRecords --------------------------------------------------

    @Test
    public void testPrintRecords() throws IOException {
        printer.printRecords(Arrays.asList(Arrays.asList("123", "Hello"), Arrays.asList("456", "World")));
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("123,Hello\n456,World", result);
    }

    @Test
    public void testPrintRecordsQuote() throws IOException {
        printer.format = new CSVFormat('"', ',', Quote.ALL);
        printer.printRecords(Arrays.asList(Arrays.asList("123", "Hello"), Arrays.asList("456", "World")));
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("\"123\",\"Hello\"\n\"456\",\"World\"", result);
    }

    @Test
    public void testPrintRecordsIterable() throws IOException {
        List<List<String>> list = Arrays.asList(Collections.singletonList("123"), Collections.singletonList("Hello"),
                Collections.singletonList("false"));
        printer.printRecords(list);
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("123\nHello\nfalse", result);
    }

    @Test
    public void testPrintRecordsIterableQuote() throws IOException {
        printer.format = new CSVFormat('"', ',', Quote.ALL);
        List<List<String>> list = Arrays.asList(Collections.singletonList("123"), Collections.singletonList("Hello"),
                Collections.singletonList("false"));
        printer.printRecords(list);
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("\"123\"\n\"Hello\"\n\"false\"", result);
    }

    @Test
    public void testPrintRecordsObjectArray() throws IOException {
        Object[][] array = new Object[][] { {123, "Hello"}, {456, "World"} };
        printer.printRecords(array);
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("123,Hello\n456,World", result);
    }

    @Test
    public void testPrintRecordsObjectArrayQuote() throws IOException {
        printer.format = new CSVFormat('"', ',', Quote.ALL);
        Object[][] array = new Object[][] { {123, "Hello"}, {456, "World"} };
        printer.printRecords(array);
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("\"123\",\"Hello\"\n\"456\",\"World\"", result);
    }

    // Test printRecords with ResultSet --------------------------------------------------

    @Test
    public void testPrintRecordsResultSet() throws SQLException, IOException {
        String query = "SELECT * FROM TEST";
        ResultSet rs = TestHelper.getTestResultSet(query);
        printer.printRecords(rs);
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals(
                "\"A\",1\n\"B\",2\n\"C\",3\n\"D\",4\n\"E\",5\n\"F\",6\n\"G\",7\n\"H\",8", result);
    }

    @Test
    public void testPrintRecordsResultSetQuote() throws SQLException, IOException {
        printer.format = new CSVFormat('"', ',', Quote.ALL);
        String query = "SELECT * FROM TEST";
        ResultSet rs = TestHelper.getTestResultSet(query);
        printer.printRecords(rs);
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals(
                "\"A\"\n\"1\"\n\"B\"\n\"2\"\n\"C\"\n\"3\"\n\"D\"\n\"4\"\n\"E\"\n\"5\"\n\"F\"\n\"6\"\n\"G\"\n\"7\"\n\"H\"\n\"8\"", result);
    }

    // Test printComment --------------------------------------------------

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        printer.println();
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals(COMMENT + SP + "This is a comment" + LF, result);
    }

    // Test println --------------------------------------------------

    @Test
    public void testPrintln() throws IOException {
        printer.println();
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals(format.getRecordSeparator(), result);
    }

    // Test println with multiple records --------------------------------------------------

    @Test
    public void testPrintlnMultipleRecords() throws IOException {
        printer.println("Record 1");
        printer.println("Record 2");
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals(format.getRecordSeparator() + "Record 1" + LF + format.getRecordSeparator() + "Record 2", result);
    }

    // Test printRecord with multiple records --------------------------------------------------

    @Test
    public void testPrintRecordMultipleRecords() throws IOException {
        printer.printRecord(Arrays.asList("Record 1", "Record 2"));
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("Record 1,Record 2" + LF, result);
    }

    // Test printRecords with multiple records --------------------------------------------------

    @Test
    public void testPrintRecordsMultipleRecords() throws IOException {
        printer.printRecords(Arrays.asList(Arrays.asList("Record 1"), Arrays.asList("Record 2")));
        printer.flush();
        String result = new String(out.toByteArray());
        assertEquals("Record 1" + LF + "Record 2" + LF, result);
    }

    // Test getOut --------------------------------------------------

    @Test
    public void testGetOut() throws IOException {
        assertSame(out, printer.getOut());
    }
}