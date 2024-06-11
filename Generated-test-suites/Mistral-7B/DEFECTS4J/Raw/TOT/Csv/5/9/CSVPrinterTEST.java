package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

public class CSVPrinterTest {

    @Test
    public void testPrintSimpleValues() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, '\u0001');
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.printRecord("Alice", "Bob", "Charlie");
        printer.close();

        final String expected = "Alice\u0001Bob\u0001Charlie\n";
        Assert.assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintEscapedValues() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuote('"'), '\u0001');
        final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.printRecord("Alice,Bob,\"Charlie,Dave\"");
        printer.close();

        final String expected = "Alice,\"Charlie,Dave\"\u0001Bob\n";
        Assert.assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintEmptyValues() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, '\u0001');

        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format)) {
            printer.printRecord("", "", "");
            printer.close();
        }

        final String expected = "\u0001\u0001\u0001\n";
        Assert.assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintNullValues() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, '\u0001');

        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format)) {
            printer.printRecord("Alice", null, "Charlie");
            printer.close();
        }

        final String expected = "Alice\u0001,,\u0001Charlie\n";
        Assert.assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintQuotedNullValues() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuote('"'), '\u0001');

        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format)) {
            printer.printRecord("Alice", null, "Charlie");
            printer.close();
        }

        final String expected = "Alice,\"\",Charlie\n";
        Assert.assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintQuotedEmptyValues() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuote('"'), '\u0001');

        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format)) {
            printer.printRecord("Alice", "", "Charlie");
            printer.close();
        }

        final String expected = "Alice,\"\",Charlie\n";
        Assert.assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintMixedValues() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, '\u0001');

        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format)) {
            printer.printRecord("Alice", "Bob", null, "Charlie");
            printer.close();
        }

        final String expected = "Alice\u0001Bob\u0001,\u0001,,\u0001Charlie\n";
        Assert.assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintRecordsFromCollection() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, '\u0001');

        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format)) {
            List<List<Object>> records = Arrays.asList(
                    Arrays.asList("Alice", "Bob", "Charlie"),
                    Arrays.asList("Alice1", null, "Charlie1"),
                    Arrays.asList("Alice2", "Bob1", "Charlie2"));
            printer.printRecords(records);
            printer.close();
        }

        final String expected = "Alice\u0001Bob\u0001Charlie\n"
                + "Alice1,\u0001,\u0001Charlie1\n"
                + "Alice2\u0001Bob1\u0001Charlie2\n";
        Assert.assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintRecordsFromArray() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, '\u0001');

        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format)) {
            Object[][] records = {{"Alice", "Bob", "Charlie"}, {"Alice1", null, "Charlie1"}, {"Alice2", "Bob1", "Charlie2"}};
            printer.printRecords(records);
            printer.close();
        }

        final String expected = "Alice\u0001Bob\u0001Charlie\n"
                + "Alice1,\u0001,\u0001Charlie1\n"
                + "Alice2\u0001Bob1\u0001Charlie2\n";
        Assert.assertEquals(expected, out.toString());
    }

    @Test
    public void testPrintRecordsFromJdbcResultSet() throws IOException, SQLException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, '\u0001');

        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format)) {
            final String sql = "SELECT col1, col2, col3 FROM my_table";
            try (java.sql.Connection connection = DriverManager.getConnection("jdbc:my_database", "user", "password");
                 PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet resultSet = statement.executeQuery()) {
                printer.printRecords(resultSet);
            }
            printer.close();
        }

        final String expected = "col1_val1\u0001col2_val1\u0001col3_val1\n"
                + "col1_val2\u0001col2_val2\u0001col3_val2\n"
                + "col1_val3\u0001col2_val3\u0001col3_val3\n";
        Assert.assertEquals(expected, out.toString());
    }
}