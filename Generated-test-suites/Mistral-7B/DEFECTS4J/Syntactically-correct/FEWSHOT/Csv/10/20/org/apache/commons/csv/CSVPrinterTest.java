package org.apache.commons.csv;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private Appendable out;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormatBuilder().setDelimiter(';').build();
        printer = new CSVPrinter(out, format);
    }

    @Test
    public void testPrint() throws IOException {
        printer.print("foo");
        printer.print("bar");
        assertEquals("foo;bar\n", out.toString());
    }

    @Test
    public void testPrintWithQuotes() throws IOException {
        printer.format.setQuotePolicy(Quote.MINIMAL);
        printer.print("foo bar");
        assertEquals("\"foo bar\"\n", out.toString());

        printer.format.setQuotePolicy(Quote.ALL);
        printer.print("foo bar");
        assertEquals("\"foo\" \"bar\"\n", out.toString());
    }

    @Test
    public void testPrintWithEscapes() throws IOException {
        printer.format.setEscaping(true);
        printer.print("foo,bar");
        assertEquals("foo,bar\n", out.toString());

        printer.print("foo,bar\n");
        assertEquals("foo,bar\r\n", out.toString());

        printer.print("foo,bar\r");
        assertEquals("foo,bar\r", out.toString());

        printer.print("foo,\"bar\"");
        assertEquals("foo,\"bar\"\n", out.toString());

        printer.print("foo,\"bar\"\r");
        assertEquals("foo,\"bar\"\r", out.toString());

        printer.print("foo,\"bar\"\r\n");
        assertEquals("foo,\"bar\"\r\n", out.toString());

        printer.print("foo,\"bar\"\""); // double quotes inside quotes
        assertEquals("foo,\"bar\"\"\n", out.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.format.setCommentingEnabled(true);
        printer.printComment("This is a comment");
        assertEquals("--This is a comment\r\n--\n", out.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord("foo", "bar");
        assertEquals("foo;bar\n", out.toString());

        printer.format.setQuotePolicy(Quote.ALL);
        printer.printRecord("foo", "bar");
        assertEquals("\"foo\" \"bar\"\n", out.toString());
    }

    @Test
    public void testPrintRecordCollection() throws IOException {
        Iterable<Object> values = Arrays.asList("foo", "bar");
        printer.printRecords(values);
        assertEquals("foo;bar\n", out.toString());

        printer.format.setQuotePolicy(Quote.ALL);
        printer.printRecords(values);
        assertEquals("\"foo\" \"bar\"\n", out.toString());
    }

    @Test
    public void testPrintRecordArray() throws IOException {
        Object[] values = new Object[] { "foo", "bar" };
        printer.printRecords(values);
        assertEquals("foo;bar\n", out.toString());

        printer.format.setQuotePolicy(Quote.ALL);
        printer.printRecords(values);
        assertEquals("\"foo\" \"bar\"\n", out.toString());
    }

    @Test
    public void testPrintRecordResultSet() throws IOException, SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString(1)).thenReturn("foo");
        when(resultSet.getString(2)).thenReturn("bar");
        printer.printRecords(resultSet);
        assertEquals("foo;bar\n", out.toString());
    }

    @Test
    public void testFlush() throws IOException {
        printer.flush();
        assertEquals("", out.toString());
    }

    @Test
    public void testClose() throws IOException {
        printer.close();
        assertEquals("", out.toString());
    }
}