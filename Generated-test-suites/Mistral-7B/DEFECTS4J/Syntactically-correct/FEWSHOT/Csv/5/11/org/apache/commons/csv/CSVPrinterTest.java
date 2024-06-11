package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

public class CSVPrinterTest {

    @Test
    public void testCSVPrinter() {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, Quote.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter printer = new CSVPrinter(new StringWriter(out), format)) {
            Object[] values = {"foo", "bar", 123, null, true};
            printer.printRecords(values);
            String result = out.toString();
            String expected = "foo,bar,123,null,true\n";
            assertEquals(expected, result);
        }
    }

    @Test
    public void testCSVPrinterQuote() {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, Quote.ALL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter printer = new CSVPrinter(new StringWriter(out), format)) {
            Object[] values = {"foo", "bar\"qux\"", "'qux'", "\"qux\"'s qux\"", null, 123, true};
            printer.printRecords(values);
            String result = out.toString();
            String expected = "\"foo\",\"bar\"\"qux\"\",\"'qux'\",\"\"qux\"'s qux\"\",,,123,true\n";
            assertEquals(expected, result);
        }
    }

    @Test
    public void testCSVPrinterEscape() {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, Quote.NONE, '\u001E');
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter printer = new CSVPrinter(new StringWriter(out), format)) {
            Object[] values = {"foo\u001Ebar", "foo\u001Ebar\u001Ebaz", "foo\u001Ebar\u001E\u001Ebaz"};
            printer.printRecords(values);
            String result = out.toString();
            String expected = "foo\u001Ebar,\"foo\u001Ebar\u001Ebaz\",\"foo\u001Ebar\u001E\u001Ebaz\"\n";
            assertEquals(expected, result);
        }
    }

    @Test
    public void testCSVPrinterQuotePolicy() {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, null, null, Quote.NON_NUMERIC);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter printer = new CSVPrinter(new StringWriter(out), format)) {
            Object[] values = {"foo123", "bar", "123", null, true};
            printer.printRecords(values);
            String result = out.toString();
            String expected = "\"foo123\",bar,123,null,true\n";
            assertEquals(expected, result);
        }
    }

    @Test
    public void testCSVPrinterEmptyArray() {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, Quote.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter printer = new CSVPrinter(new StringWriter(out), format)) {
            Object[] values = {};
            printer.printRecords(values);
            String result = out.toString();
            assertTrue(result.isEmpty());
        }
    }

    @Test
    public void testCSVPrinterIterable() {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, Quote.MINIMAL);
        List<List<String>> records = Arrays.asList(Arrays.asList("foo", "bar"), Collections.singletonList("baz"));
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter printer = new CSVPrinter(new StringWriter(out), format)) {
            printer.printRecords(records);
            String result = out.toString();
            String expected = "foo,bar\nbaz\n";
            assertEquals(expected, result);
        }
    }

    @Test
    public void testCSVPrinterObjectArray() {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, Quote.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter printer = new CSVPrinter(new StringWriter(out), format)) {
            Object[][] values = {{"foo", "bar"}, {"baz"}};
            printer.printRecords(values);
            String result = out.toString();
            String expected = "foo,bar\nbaz\n";
            assertEquals(expected, result);
        }
    }

    @Test
    public void testCSVPrinterResultSet() throws SQLException, IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, Quote.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter printer = new CSVPrinter(new StringWriter(out), format)) {
            String query = "SELECT * FROM test";
            try (ResultSet resultSet = TestUtils.getResultSetForQuery(query)) {
                printer.printRecords(resultSet);
            }
            String result = out.toString();
            // Assumes test table has columns "id", "name", "age"
            String expected = "1,foo,25\n2,bar,30\n";
            assertEquals(expected, result);
        }
    }

    @Test
    public void testCSVPrinterComment() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, Quote.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter printer = new CSVPrinter(new StringWriter(out), format)) {
            printer.printComment("This is a comment.");
            String result = out.toString();
            String expected = "#This is a comment.\n";
            assertEquals(expected, result);
        }
    }

    @Test
    public void testCSVPrinterCommentWithRecord() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, Quote.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter printer = new CSVPrinter(new StringWriter(out), format)) {
            printer.printComment("This is a comment.");
            printer.printRecord(Arrays.asList("foo", "bar"));
            String result = out.toString();
            String expected = "#This is a comment.\nfoo,bar\n";
            assertEquals(expected, result);
        }
    }

    @Test
    public void testCSVPrinterFlush() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, Quote.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter printer = new CSVPrinter(new StringWriter(out), format)) {
            Object[] values = {"foo", "bar", 123, null, true};
            printer.printRecords(values);
            printer.flush();
            String result = out.toString();
            String expected = "foo,bar,123,null,true\n";
            assertEquals(expected, result);
        }
    }

    @Test
    public void testCSVPrinterClose() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, Quote.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter printer = new CSVPrinter(new StringWriter(out), format)) {
            Object[] values = {"foo", "bar", 123, null, true};
            printer.printRecords(values);
            printer.close();
            String result = out.toString();
            String expected = "foo,bar,123,null,true\n";
            assertEquals(expected, result);
        }
    }

    @Test(expected = IOException.class)
    public void testCSVPrinterNull() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, Quote.MINIMAL);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter printer = new CSVPrinter(null, format)) {
            Object[] values = {"foo", "bar", 123, null, true};
            printer.printRecords(values);
        }
    }
}