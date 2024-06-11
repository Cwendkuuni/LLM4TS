package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException, SQLException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder() //
                .setDelimiter(';') //
                .setHeader("header1", "header2") //
                .build();

        final CSVPrinter printer = new CSVPrinter(out, format);

        // null
        printer.print(null);
        printer.flush();
        assertEquals("", out.toString());

        // empty string
        printer.print("");
        printer.flush();
        assertEquals("", out.toString());

        // simple strings
        Object[] values = new Object[] {"foo", "bar"};
        printer.printRecord(values);
        printer.flush();
        assertEquals("header1,header2\nfoo,bar\n", out.toString());

        // number
        printer.print(123);
        printer.flush();
        assertEquals("123\n", out.toString());

        // comment
        printer.printComment("this is a test comment");
        printer.println();
        printer.printRecord(values);
        printer.flush();
        assertEquals("header1,header2\n# this is a test comment\nfoo,bar\n", out.toString());

        // record separator
        printer = new CSVPrinter(out, format);
        printer.println();
        printer.printRecord(values);
        printer.println();
        printer.flush();
        assertEquals("header1,header2\n\nheader1,header2\n", out.toString());

        // single value record
        printer = new CSVPrinter(out, format);
        printer.printRecord(values[0]);
        printer.flush();
        assertEquals("header1\nfoo\n", out.toString());

        // multiple value record
        printer = new CSVPrinter(out, format);
        printer.printRecord(values);
        printer.flush();
        assertEquals("header1,header2\nfoo,bar\n", out.toString());

        // quoting
        String value = "foo,\"bar\"";
        printer = new CSVPrinter(out, format.withQuotePolicy(Quote.MINIMAL));
        printer.printRecord(value);
        printer.flush();
        assertEquals("header1,header2\nfoo,\"bar\"\n", out.toString());

        // escaping
        value = "foo\\,bar";
        printer = new CSVPrinter(out, format.withQuotePolicy(Quote.MINIMAL).withEscape('\u0026'));
        printer.printRecord(value);
        printer.flush();
        assertEquals("header1,header2\nfoo\\,bar\n", out.toString());

        // escaping and quoting
        value = "foo\\,\"bar\"";
        printer = new CSVPrinter(out, format.withQuotePolicy(Quote.MINIMAL).withEscape('\u0026').withQuote('\''));
        printer.printRecord(value);
        printer.flush();
        assertEquals("header1,header2\nfoo\\,\"bar\"\n", out.toString());

        // escaping and quoting, backslash must be escaped
        value = "foo\\\\,bar";
        printer = new CSVPrinter(out, format.withQuotePolicy(Quote.MINIMAL).withEscape('\u0026').withQuote('\'')
                .withEscape('\\'));
        printer.printRecord(value);
        printer.flush();
        assertEquals("header1,header2\nfoo\\\\,bar\n", out.toString());

        // quoting and comment
        value = "foo#comment";
        printer = new CSVPrinter(out, format.withQuotePolicy(Quote.MINIMAL).withComment('%'));
        printer.printComment("comment line");
        printer.printRecord(value);
        printer.flush();
        assertEquals("header1,header2\n% comment line\nfoo#comment\n", out.toString());

        // quoting, escaping and comment
        value = "foo#comment\\,\"bar\"";
        printer = new CSVPrinter(out, format.withQuotePolicy(Quote.MINIMAL).withComment('%').withQuote('\'')
                .withEscape('\\').withEscape('\\u0026'));
        printer.printComment("comment line");
        printer.printRecord(value);
        printer.flush();
        assertEquals("header1,header2\n% comment line\nfoo#comment\\,\"bar\"\n", out.toString());

        // empty record
        printer = new CSVPrinter(out, format);
        printer.println();
        printer.flush();
        assertEquals("header1,header2\n", out.toString());

        // multiple records
        printer = new CSVPrinter(out, format);
        Object[] values1 = new Object[] {"foo1", "bar1"};
        Object[] values2 = new Object[] {"foo2", "bar2"};
        printer.printRecord(values1);
        printer.println();
        printer.printRecord(values2);
        printer.println();
        printer.flush();
        assertEquals("header1,header2\nfoo1,bar1\nheader1,header2\nfoo2,bar2\n", out.toString());

        // printRecord with Iterable<Object>
        printer = new CSVPrinter(out, format);
        Iterable<Object> valuesIterable = () -> Arrays.asList(values1).iterator();
        printer.printRecords(valuesIterable);
        printer.flush();
        assertEquals("header1,header2\nfoo1,bar1\nheader1,header2\nfoo1,bar1\n", out.toString());

        // printRecord with Object[]
        printer = new CSVPrinter(out, format);
        Object[] valuesArray = values1;
        printer.printRecords(valuesArray);
        printer.flush();
        assertEquals("header1,header2\nfoo1,bar1\nheader1,header2\nfoo1,bar1\n", out.toString());

        // printRecords with ResultSet
        printer = new CSVPrinter(out, format);
        ResultSet rs = ResultSetFactory.createJdbc4ResultSet(new Jdbc4ConnectionContext(new MockConnection()),
                new Object[] {"1", "foo1", "bar1", "comment1"});
        printer.printRecords(rs);
        printer.flush();
        assertEquals("header1,header2\n1,foo1\nheader1,header2\n1,foo1\n# comment1\n", out.toString());
    }

    @Test
    public void testFlush() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(';').build();
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.flush();
        assertEquals("", out.toString());
    }

    @Test
    public void testClose() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(';').build();
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.close();
        assertFalse(printer.isOpen());
    }

    @Test
    public void testCloseTwice() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(';').build();
        final CSVPrinter printer = new CSVPrinter(out, format);
        printer.close();
        printer.close();
        assertFalse(printer.isOpen());
    }

    @Test
    public void testNewRecord() {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(';').build();
        final CSVPrinter printer = new CSVPrinter(out, format);
        assertTrue(printer.isNewRecord());
        printer.println();
        printer.close();
        assertFalse(printer.isNewRecord());
        printer = new CSVPrinter(out, format);
        assertTrue(printer.isNewRecord());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullOut() {
        new CSVPrinter(null, new CSVFormatBuilder().build());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullFormat() {
        new CSVPrinter(new ByteArrayOutputStream(), null);
    }

    @Test(expected = IOException.class)
    public void testPrintRecordNullOut() throws IOException {
        final CSVFormat format = new CSVFormatBuilder().build();
        new CSVPrinter(null, format).printRecord(new Object[] {"foo", "bar"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintRecordNullFormat() throws IOException {
        new CSVPrinter(new ByteArrayOutputStream(), null).printRecord(new Object[] {"foo", "bar"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintRecordNullValues() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().build();
        new CSVPrinter(out, format).printRecord(null);
    }

    @Test(expected = IOException.class)
    public void testPrintRecordArrayEmpty() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormatBuilder().build();
        new CSVPrinter(out, format).printRecord(new Object[0]);
    }

    public static void main(String[] args) throws Exception {
        org.junit.runner.JUnitCore.main("CSVPrinterTest");
    }
}

enum Quote {
    MINIMAL, // quoting is only used for values that contain a delimiter or the quote character or a space
    NON_NUMERIC, // quoting is used for all values except numbers
    ALL, // quoting is used for all values
    NONE; // quoting is not used
}