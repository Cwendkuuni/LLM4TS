package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CSVPrinterTest {

    @Test
    public void testPrintSimpleValues() throws IOException, SQLException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT.withQuoteAll(true);
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.print("foo");
        printer.print("bar");
        printer.println();

        assertEquals("foo,bar\n", outStream.toString());
    }

    @Test
    public void testPrintNullValues() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT.withQuoteAll(true);
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.print(null);
        printer.print("foo");
        printer.println();

        assertEquals("\"\",foo\n", outStream.toString());
    }

    @Test
    public void testPrintValuesWithEscaping() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT.withQuoteAll(false).withEscape("\u0001");
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.print("foo,");
        printer.print("bar,baz,");
        printer.print(",qux");
        printer.println();

        assertEquals("foo,\"bar,baz,\",\"\",qux\n", outStream.toString());
    }

    @Test
    public void testPrintValuesWithQuoting() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT.withQuoteAll(true).withQuote('\u0002');
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.print("foo,");
        printer.print("bar,baz,");
        printer.print(",qux");
        printer.println();

        assertEquals("foo,\"bar\",\"baz\",\"qux\"\n", outStream.toString());
    }

    @Test
    public void testPrintValuesWithMixedQuotingAndEscaping() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT.withQuoteAll(false)
                .withEscape("\u0001")
                .withQuote('\u0002');
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.print("foo,");
        printer.print("bar,baz,\"qux\",\"quux\",");
        printer.print("quw, qux");
        printer.println();

        assertEquals("foo,\"bar,baz\",\"qux\",\"quux\",\"quw\",\"qux\"\n", outStream.toString());
    }

    @Test
    public void testPrintValuesWithQuotePolicy() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormatBuilder()
                .setDelimiter(';')
                .setHeader("Name", Quote.MINIMAL)
                .setHeader("Age", Quote.ALL)
                .build();
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.printRecord(new Object[]{"John Doe", 30});
        printer.println();

        assertEquals("Name;Age\nJohn Doe;\"30\"\n", outStream.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT.withCommentLine("#");
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.printComment("This is a comment");
        printer.println();

        assertEquals("#This is a comment\n", outStream.toString());
    }

    @Test
    public void testPrintRecord() throws IOException, SQLException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT.withQuoteAll(true);
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.printRecord(new Object[]{"foo", "bar", "baz"});
        printer.println();

        assertEquals("foo,bar,baz\n", outStream.toString());
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT.withQuoteAll(true);
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.printRecords(new Object[][]{
                {"foo1", "bar1", "baz1"},
                {"foo2", "bar2", "baz2"},
                {"foo3", "bar3", "baz3"}
        });

        assertEquals("foo1,bar1,baz1\nfoo2,bar2,baz2\nfoo3,bar3,baz3\n", outStream.toString());
    }

    @Test
    public void testPrintRecordsFromResultSet() throws IOException, SQLException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT.withQuoteAll(true);
        CSVPrinter printer = new CSVPrinter(outStream, format);

        String query = "SELECT name, age FROM people";
        ResultSet resultSet = ConnectionUtil.getConnection().createStatement()
                .executeQuery(query);

        printer.printRecords(resultSet);

        assertTrue(resultSet.next());
        assertEquals("name,age\n\"John\",\"25\"\n", outStream.toString());

        assertFalse(resultSet.next());
    }

    @Test
    public void testFlushAndClose() throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT.withQuoteAll(true);
        CSVPrinter printer = new CSVPrinter(outStream, format);

        printer.print("foo");
        printer.print("bar");
        printer.println();

        printer.flush();

        assertEquals("foo,bar\n", outStream.toString());
        printer.close();
    }
}