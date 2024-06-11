package org.apache.commons.csv.test;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;
import org.apache.commons.csv.test.util.TestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link CSVPrinter} class.
 */
public class CSVPrinterTest {

    private CSVPrinter printer;
    private Appendable out;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() throws Exception {
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, '\u0001');
        outContent = new ByteArrayOutputStream();
        out = new StringWriter(outContent);
        printer = new CSVPrinter(out, format);
    }

    @After
    public void tearDown() throws Exception {
        if (printer != null) {
            printer.close();
        }
    }

    // Test constructor --------------------------------------------------

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullOut() {
        new CSVPrinter(null, CSVFormat.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullFormat() {
        new CSVPrinter(out, null);
    }

    @Test
    public void testConstructValid() {
        new CSVPrinter(out, CSVFormat.DEFAULT);
    }

    // Test print methods ------------------------------------------------

    @Test
    public void testPrintNullValue() throws IOException {
        printer.print(null);
        printer.flush();
        assertEquals(CSVFormat.DEFAULT.getNullString(), outContent.toString());
    }

    @Test
    public void testPrintStringValue() throws IOException {
        printer.print("test");
        printer.flush();
        assertEquals("test", outContent.toString());
    }

    @Test
    public void testPrintObjectValue() throws IOException {
        printer.print(new String("test"));
        printer.flush();
        assertEquals("test", outContent.toString());
    }

    @Test
    public void testPrintEmptyString() throws IOException {
        printer.print("");
        printer.flush();
        assertEquals("", outContent.toString());
    }

    @Test
    public void testPrintEmptyArray() throws IOException {
        Object[] objArray = new Object[0];
        printer.printRecord(objArray);
        printer.flush();
        assertEquals(Collections.EMPTY_LIST.toString(), outContent.toString());
    }

    @Test
    public void testPrintArray() throws IOException {
        Object[] objArray = new Object[] {"test", 123};
        printer.printRecord(objArray);
        printer.flush();
        List<String> expected = Arrays.asList(new String[] {"test", "123"});
        assertEquals(expected.toString(), outContent.toString());
    }

    @Test
    public void testPrintIterable() throws IOException {
        List<String> list = Arrays.asList(new String[] {"test1", "test2"});
        printer.printRecords(list);
        printer.flush();
        List<String> expected = Arrays.asList(new String[] {"test1", "test2"});
        assertEquals(expected.toString(), outContent.toString());
    }

    @Test
    public void testPrintResultSet() throws SQLException, IOException {
        ResultSet rs = TestUtil.getTestResultSet();
        printer.printRecords(rs);
        printer.flush();
        String expected = "test1,test2\n" +
                "test3,test4\n";
        assertEquals(expected, outContent.toString());
    }

    // Test print methods with quoting -------------------------------------

    @Test
    public void testPrintQuotedStringValue() throws IOException {
        printer.print("test\"quoted\"value");
        printer.flush();
        assertEquals("test\"quoted\"value", outContent.toString());
    }

    @Test
    public void testPrintQuotedObjectValue() throws IOException {
        printer.print(new String("test\"quoted\"value"));
        printer.flush();
        assertEquals("test\"quoted\"value", outContent.toString());
    }

    @Test
    public void testPrintQuotedEmptyString() throws IOException {
        printer.print("\"\"");
        printer.flush();
        assertEquals("\"\"", outContent.toString());
    }

    @Test
    public void testPrintQuotedEmptyArray() throws IOException {
        Object[] objArray = new Object[0];
        printer.printRecord(objArray);
        printer.flush();
        assertEquals(Collections.EMPTY_LIST.toString(), outContent.toString());
    }

    @Test
    public void testPrintQuotedArray() throws IOException {
        Object[] objArray = new Object[] {"test\"quoted\"value", "test\"quoted2\"value"};
        printer.printRecord(objArray);
        printer.flush();
        List<String> expected = Arrays.asList(new String[] {"test\"quoted\"value", "test\"quoted2\"value"});
        assertEquals(expected.toString(), outContent.toString());
    }

    @Test
    public void testPrintQuotedIterable() throws IOException {
        List<String> list = Arrays.asList(new String[] {"test\"quoted1\"value", "test\"quoted2\"value"});
        printer.printRecords(list);
        printer.flush();
        List<String> expected = Arrays.asList(new String[] {"test\"quoted1\"value", "test\"quoted2\"value"});
        assertEquals(expected.toString(), outContent.toString());
    }

    @Test
    public void testPrintQuotedResultSet() throws SQLException, IOException {
        ResultSet rs = TestUtil.getTestResultSet();
        printer.printRecords(rs);
        printer.flush();
        String expected = "test1\"quoted1\"value,test2\"quoted2\"value\n" +
                "test3\"quoted3\"value,test4\"quoted4\"value\n";
        assertEquals(expected, outContent.toString());
    }

    // Test print methods with escaping -------------------------------------

    @Test
    public void testPrintEscapedStringValue() throws IOException {
        printer.print("test\\value");
        printer.flush();
        assertEquals("test\\value", outContent.toString());
    }

    @Test
    public void testPrintEscapedObjectValue() throws IOException {
        printer.print(new String("test\\value"));
        printer.flush();
        assertEquals("test\\value", outContent.toString());
    }

    @Test
    public void testPrintEscapedDelimiter() throws IOException {
        printer.printRecord(new String[] {"test\\" + CSVFormat.DEFAULT.getDelimiter() + "value"});
        printer.flush();
        assertEquals("test\u0001value", outContent.toString());
    }

    @Test
    public void testPrintEscapedComment() throws IOException {
        printer.printComment("test\\comment");
        printer.flush();
        assertEquals("test\\comment" + CR + LF, outContent.toString());
    }

    @Test
    public void testPrintQuotedAndEscapedStringValue() throws IOException {
        printer.print("test\"value\\value");
        printer.flush();
        assertEquals("test\"value\\value", outContent.toString());
    }

    @Test
    public void testPrintQuotedAndEscapedObjectValue() throws IOException {
        printer.print(new String("test\"value\\value"));
        printer.flush();
        assertEquals("test\"value\\value", outContent.toString());
    }

    @Test
    public void testPrintQuotedAndEscapedDelimiter() throws IOException {
        printer.print("test\"\\" + CSVFormat.DEFAULT.getDelimiter() + "value");
        printer.flush();
        assertEquals("test\"\\u0001value", outContent.toString());
    }

    @Test
    public void testPrintQuotedAndEscapedComment() throws IOException {
        printer.printComment("test\"comment");
        printer.flush();
        assertEquals("test\"comment" + CR + LF, outContent.toString());
    }

    @Test
    public void testPrintQuotedAndEscapedArray() throws IOException {
        Object[] objArray = new Object[] {"test\"value\\value", "test\"value\\value2"};
        printer.printRecord(objArray);
        printer.flush();
        List<String> expected = Arrays.asList(new String[] {"test\"value\\value", "test\"value\\value2"});
        assertEquals(expected.toString(), outContent.toString());
    }

    @Test
    public void testPrintQuotedAndEscapedIterable() throws IOException {
        List<String> list = Arrays.asList(new String[] {"test\"value\\value", "test\"value\\value2"});
        printer.printRecords(list);
        printer.flush();
        List<String> expected = Arrays.asList(new String[] {"test\"value\\value", "test\"value\\value2"});
        assertEquals(expected.toString(), outContent.toString());
    }

    @Test
    public void testPrintQuotedAndEscapedResultSet() throws SQLException, IOException {
        ResultSet rs = TestUtil.getTestResultSet();
        printer.printRecords(rs);
        printer.flush();
        String expected = "test1\"quoted1\"value\\quoted1\"value,\"test2\"quoted2\"value\\quoted2\"value\n" +
                "test3\"quoted3\"value\\quoted3\"value,\"test4\"quoted4\"value\\quoted4\"value\n";
        assertEquals(expected, outContent.toString());
    }

    // Test printRecord method with various record separators --------------

    @Test
    public void testPrintRecordDefaultRecordSeparator() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, '\u0001');
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new StringWriter(outContent), format);
        printer.printRecord(new String[] {"test"});
        printer.flush();
        assertEquals("test", outContent.toString());
    }

    @Test
    public void testPrintRecordCustomRecordSeparator() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, '\u0002');
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(new StringWriter(outContent), format);
        printer.printRecord(new String[] {"test"});
        printer.flush();
        assertEquals("test\u0002", outContent.toString());
    }

    // Test printComment method --------------------------------------------

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("test comment");
        printer.println();
        printer.flush();
        assertEquals("test comment" + CR + LF, outContent.toString());
    }

    @Test
    public void testPrintCommentWithEscapedCharacter() throws IOException {
        printer.printComment("test\\comment");
        printer.println();
        printer.flush();
        assertEquals("test\\comment" + CR + LF, outContent.toString());
    }

    // Test flush method --------------------------------------------------

    @Test
    public void testFlush() throws IOException {
        printer.print("test");
        printer.flush();
        assertEquals("test", outContent.toString());
    }

    // Test close method --------------------------------------------------

    @Test
    public void testClose() throws IOException {
        printer.close();
        assertFalse(printer.isOpen());
    }
}