package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private ByteArrayOutputStream out;
    private CSVFormat format;
    private CSVPrinter printer;

    @Before
    public void setup() {
        out = new ByteArrayOutputStream();
        format = CSVFormat.DEFAULT.withQuoteAll(true);
        printer = new CSVPrinter(out, format);
    }

    @Test
    public void testPrintString() throws IOException {
        printer.print("foo");
        assertEquals("foo", out.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        assertEquals("", out.toString());
    }

    @Test
    public void testPrintObject() throws IOException {
        printer.print("foo" + LF + "bar");
        assertEquals("foo" + LF + "bar", out.toString());
        printer.print("foo");
        assertEquals("foo" + LF + "foo", out.toString());

        Object[] arr = {"foo", "bar"};
        printer.printRecords(arr);
        assertEquals("foo" + LF + "bar" + LF + "foo" + LF + "bar", out.toString());
    }

    @Test
    public void testPrintObjectArray() throws IOException {
        Object[] arr = {"foo", "bar"};
        printer.printRecords(arr);
        assertEquals("foo" + LF + "bar" + LF + "foo" + LF + "bar", out.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord("foo", "bar");
        assertEquals("foo" + LF + "bar", out.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        assertEquals("" + format.getCommentStart() + SP + "This is a comment" + LF + format.getRecordSeparator(), out.toString());
    }

    @Test
    public void testPrintEscapedValues() throws IOException {
        printer = new CSVPrinter(out, format.withEscape('\\'));
        printer.print("foo\rbar\\\"baz");
        assertEquals("foo\\rbar\\\\\"baz", out.toString());
    }

    @Test
    public void testPrintQuotedValues() throws IOException {
        printer = new CSVPrinter(out, format.withQuoteAll(true));
        printer.print("foo\"bar\"baz");
        assertEquals("foo\"bar\"baz", out.toString());
    }

    @Test
    public void testPrintQuotedValuesWithEscapedQuotes() throws IOException {
        printer = new CSVPrinter(out, format.withQuoteAll(true).withQuoteEscaped(true));
        printer.print("foo\"\"bar\"\"baz");
        assertEquals("foo\"\"bar\"\"baz", out.toString());
    }

    @Test
    public void testPrintQuotedValuesWithEscapedQuotesAndEscapedCharacters() throws IOException {
        printer = new CSVPrinter(out, format.withQuoteAll(true).withQuoteEscaped(true).withEscape('\\'));
        printer.print("foo\\\"bar\\\"baz\\\\\\");
        assertEquals("foo\\\"bar\\\"baz\\\\\\", out.toString());
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        ByteArrayOutputStream expected = new ByteArrayOutputStream();
        expected.write("foo\rbar\r".getBytes());
        expected.write("baz\rqux\r".getBytes());
        expected.writeTo(System.out);

        ResultSet rs = ResultSetTest.createMockResultSet("foo", "bar", "baz", "qux");
        printer.printRecords(rs);

        assertEquals(expected.toString(), out.toString());
    }
}