package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    private static final String TEST_FORMAT = "{\"header\":[\"Name\",\"Age\",\"City\"],\"comment.line.prefix\":\"#\"}";
    private static final CSVFormat FORMAT = CSVFormat.builder().setFormat(TEST_FORMAT).build();
    private CSVPrinter printer;
    private ByteArrayOutputStream out;

    @Before
    public void setUp() throws SQLException, IOException {
        out = new ByteArrayOutputStream();
        printer = new CSVPrinter(new StringWriter(new StringWriter(out)), FORMAT);
    }

    @Test
    public void testPrintEmptyString() throws IOException {
        printer.print("");
        Assert.assertEquals("", out.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        Assert.assertEquals(FORMAT.getNullString(), out.toString());
    }

    @Test
    public void testPrintSingleValue() throws IOException {
        printer.print("Single Value");
        Assert.assertEquals("\"Single Value\"", out.toString());
    }

    @Test
    public void testPrintMultipleValues() throws IOException {
        printer.print("First Value", "Second Value");
        Assert.assertEquals("\"First Value\",\"Second Value\"", out.toString());
    }

    @Test
    public void testPrintMultipleValuesWithQuotes() throws IOException {
        printer.print("First \"Value\"", "Second \"Value\"");
        Assert.assertEquals("\"First \\\"Value\\\"\",\"Second \\\"Value\\\"\"", out.toString());
    }

    @Test
    public void testPrintNewLine() throws IOException {
        printer.println();
        Assert.assertEquals("\n", out.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("#This is a comment");
        Assert.assertEquals("#This is a comment\n", out.toString());
    }

    @Test
    public void testPrintEmptyRecord() throws IOException {
        printer.println();
        printer.println();
        Assert.assertEquals("\n\n", out.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(Arrays.asList("Record 1", 1, "Record 3"));
        Assert.assertEquals("\"Record 1\",1,\"Record 3\"\n", out.toString());
    }

    @Test
    public void testPrintRecordWithQuotes() throws IOException {
        printer.printRecord(Arrays.asList("\"Record 1\"", 1, "\"Record 3\""));
        Assert.assertEquals("\"Record 1\",1,\"Record 3\"\n", out.toString());
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        final String sql = "SELECT name, age, city FROM users";
        final Statement statement = DbUtils.createStatement();
        final ResultSet resultSet = statement.executeQuery(sql);
        printer.printRecords(resultSet);

        Assert.assertEquals(expectedPrintRecords(), out.toString());
    }

    @Test
    public void testPrintRecordsWithQuotes() throws IOException, SQLException {
        final String sql = "SELECT name, age, city FROM users";
        final Statement statement = DbUtils.createStatement();
        final ResultSet resultSet = statement.executeQuery(sql);
        printer.setQuotePolicy(Quote.ALL);
        printer.printRecords(resultSet);

        Assert.assertEquals(expectedPrintRecordsWithQuotes(), out.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentNullFormat() throws IOException {
        new CSVPrinter(new StringWriter(new StringWriter(out)), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentNullOut() throws IOException {
        new CSVPrinter(null, FORMAT);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(outputStream, FORMAT);
        printer.close();
        printer.print("Value");
    }

    @Test(expected = SQLException.class)
    public void testSQLException() throws IOException, SQLException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new StringWriter(new StringWriter(outputStream)), FORMAT);
        final Statement statement = DbUtils.createStatement();
        statement.execute("-- Comment");
        printer.printRecords(new ResultSetReturningStatement(statement));
    }

    private static String expectedPrintRecords() {
        return "NAME,AGE,CITY\n"
                + "John Doe,30,City1\n"
                + "Jane Doe,25,City2\n"
                + "Jim Brown,45,City3\n";
    }

    private static String expectedPrintRecordsWithQuotes() {
        return "\"NAME\",\"AGE\",\"CITY\"\n"
                + "\"John Doe\",30,\"City1\"\n"
                + "\"Jane Doe\",25,\"City2\"\n"
                + "\"Jim Brown\",45,\"City3\"\n";
    }

}