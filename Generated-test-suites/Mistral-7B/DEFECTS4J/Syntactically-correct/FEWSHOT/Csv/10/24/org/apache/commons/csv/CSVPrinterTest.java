package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testPrintQuotes() throws IOException {
        CSVFormat format = new CSVFormatBuilder().setQuoteMode(CSVFormat.QUOTE_MINIMAL).build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        Object[] values = new Object[] {"foo", "'bar'", "baz\"qux", "baz'qux", "\"qux\"bar", "\"qux\"bar\"qux"};

        for (Object value : values) {
            printer.print(value);
            printer.flush();
            assertEquals("\"foo\"", printer.getOut().toString());
            printer.getOut().setLength(0);

            printer.print(value);
            printer.flush();
            assertEquals("\"foo\",\"bar\"", printer.getOut().toString());
            printer.getOut().setLength(0);
        }
    }

    @Test
    public void testPrintEscapes() throws IOException {
        CSVFormat format = new CSVFormatBuilder().setEscapeMode(CSVFormat.ESCAPE_ALL).build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        Object[] values = new Object[] {"foo", "bar\n", "bar\r", "bar\r\n", "bar,", "bar\"", "bar\\", "bar\\\\", "bar\\b", "bar\\f", "bar\\t", "bar\\n"};

        for (Object value : values) {
            printer.print(value);
            printer.flush();
            assertEquals("\"foo\",\"bar\\n\",\"bar\\r\",\"bar\\r\\n\",\"bar,\",\"bar\\\"\",\"bar\\\\\",\"bar\\\\\\\",\"bar\\b\",\"bar\\f\",\"bar\\t\",\"bar\\n\"", printer.getOut().toString());
            printer.getOut().setLength(0);
        }
    }

    @Test
    public void testPrintComment() throws IOException {
        CSVFormat format = new CSVFormatBuilder().setComment("-- comment").build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        String comment = "This is a comment";

        printer.printComment(comment);
        printer.flush();
        assertEquals("-- comment\n-- This is a comment\n", printer.getOut().toString());
        printer.getOut().setLength(0);
    }

    @Test
    public void testPrintRecord() throws IOException {
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        Object[] values = new Object[] {"foo", "bar"};

        printer.printRecord(values);
        printer.flush();
        assertEquals("foo,bar\n", printer.getOut().toString());
        printer.getOut().setLength(0);
    }

    @Test
    public void testPrintRecords() throws IOException {
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        Object[] values1 = new Object[] {"foo1", "bar1"};
        Object[] values2 = new Object[] {"foo2", "bar2", "baz"};

        printer.printRecords(Arrays.asList(values1, values2));
        printer.flush();
        assertEquals("foo1,bar1\nfoo2,bar2,baz\n", printer.getOut().toString());
        printer.getOut().setLength(0);
    }

    @Test
    public void testPrintRecordWithNullValues() throws IOException {
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        Object[] values = new Object[] {null, null};

        printer.printRecord(values);
        printer.flush();
        assertEquals("", printer.getOut().toString());
        printer.getOut().setLength(0);

        // with non-empty string as first value
        printer.printRecord(new Object[] {null, "bar"});
        printer.flush();
        assertEquals("", printer.getOut().toString());
        printer.getOut().setLength(0);

        // with non-empty string as second value
        printer.printRecord(new Object[] {"foo", null});
        printer.flush();
        assertEquals("foo\n", printer.getOut().toString());
        printer.getOut().setLength(0);

        // with both values null
        printer.printRecord(new Object[] {null, null});
        printer.flush();
        assertEquals("", printer.getOut().toString());
        printer.getOut().setLength(0);
    }

    @Test
    public void testFlushAndClose() throws IOException {
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        Object[] values = new Object[] {"foo", "bar"};

        printer.printRecord(values);
        printer.flush();
        assertEquals("foo,bar\n", printer.getOut().toString());
        printer.getOut().setLength(0);

        printer.close();
        printer.flush();
        assertFalse(printer.isOpen());
    }

    @Test(expected = IOException.class)
    public void testPrintAfterClose() throws IOException {
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        Object[] values = new Object[] {"foo", "bar"};

        printer.printRecord(values);
        printer.flush();
        assertEquals("foo,bar\n", printer.getOut().toString());
        printer.getOut().setLength(0);

        printer.close();
        printer.printRecord(values);

    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        // null out
        printer.printRecord(null);
        printer.flush();
        assertEquals("", printer.getOut().toString());
        printer.getOut().setLength(0);

        // null format
        printer.printRecord("foo,bar".split(Constants.COMMA), null);
        printer.flush();
        assertEquals("", printer.getOut().toString());
        printer.getOut().setLength(0);
    }

    @Test
    public void testPrintRecordsWithEmptyValues() throws IOException {
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        Object[] values1 = new Object[] {"", ""};
        Object[] values2 = new Object[] {"", "bar"};
        Object[] values3 = new Object[] {"foo", ""};

        printer.printRecords(Arrays.asList(values1, values2, values3));
        printer.flush();
        assertEquals(",\n,\nfoo,\n", printer.getOut().toString());
        printer.getOut().setLength(0);

        Object[] values4 = new Object[] {"", "", ""};

        printer.printRecords(Arrays.asList(values4));
        printer.flush();
        assertEquals("", printer.getOut().toString());
        printer.getOut().setLength(0);
    }

    @Test
    public void testPrintRecordsWithEmptyIterable() throws IOException {
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        Iterable<Object[]> emptyIterable = () -> new Object[0][];

        printer.printRecords(emptyIterable);
        printer.flush();
        assertEquals("", printer.getOut().toString());
        printer.getOut().setLength(0);
    }

    @Test
    public void testPrintRecordsWithEmptyArray() throws IOException {
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        Object[] emptyArray = new Object[0];

        printer.printRecords(emptyArray);
        printer.flush();
        assertEquals("", printer.getOut().toString());
        printer.getOut().setLength(0);
    }

    @Test
    public void testPrintRecordsWithEmptyResultSet() throws IOException, SQLException {
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        ResultSet emptyResultSet = (ResultSet) new Object(){};

        printer.printRecords(emptyResultSet);
        printer.flush();
        assertEquals("", printer.getOut().toString());
        printer.getOut().setLength(0);
    }

    @Test
    public void testPrintRecordsWithSingleNullValue() throws IOException {
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        Object[] values = new Object[] {"foo", null};

        printer.printRecords(Arrays.asList(values));
        printer.flush();
        assertEquals("foo\n", printer.getOut().toString());
        printer.getOut().setLength(0);
    }

    @Test
    public void testPrintRecordsWithSingleEmptyValue() throws IOException {
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        Object[] values = new Object[] {"foo", ""};

        printer.printRecords(Arrays.asList(values));
        printer.flush();
        assertEquals("foo,\n", printer.getOut().toString());
        printer.getOut().setLength(0);
    }

    @Test
    public void testPrintRecordsWithMixedEmptyAndNonEmptyValues() throws IOException {
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        Object[] values1 = new Object[] {"foo1", "", "bar1"};
        Object[] values2 = new Object[] {"foo2", "bar2", "baz"};

        printer.printRecords(Arrays.asList(values1, values2));
        printer.flush();
        assertEquals("foo1,\nfoo2,bar2,baz\n", printer.getOut().toString());
        printer.getOut().setLength(0);
    }

    @Test
    public void testPrintRecordsWithSingleValueArray() throws IOException {
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), format);

        Object[] values = new Object[] {"foo"};

        printer.printRecords(Arrays.asList(values));
        printer.flush();
        assertEquals("foo\n", printer.getOut().toString());
        printer.getOut().setLength(0);
    }

}