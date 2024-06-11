package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.Test;

/**
 * Test cases for CSVPrinter class.
 */
public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException {
        final CSVFormat format = new CSVFormat(new CSVParserDefaultSetup(), true);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new PrintWriter(new StringWriter(out)), format);

        printer.print(123);
        printer.print("foo");
        printer.print("bar");
        printer.close();

        final String expected = "123,\"foo\",bar\n";
        final String actual = out.toString();
        assertTrue("Unexpected output:\n" + actual, expected.equals(actual));
    }

    @Test
    public void testPrintQuotedValue() throws IOException {
        final CSVFormat format = new CSVFormat(new CSVParserDefaultSetup(), true, Quote.MINIMAL);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new PrintWriter(new StringWriter(out)), format);

        printer.print("foo,bar");
        printer.print("\"foo,bar\"");
        printer.close();

        final String expected = "foo,bar,\"foo,bar\"\n";
        final String actual = out.toString();
        assertTrue("Unexpected output:\n" + actual, expected.equals(actual));
    }

    @Test
    public void testPrintEscapedValue() throws IOException {
        final CSVFormat format = new CSVFormat(new CSVParserDefaultSetup(), false, ',', '\\', '"');
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new PrintWriter(new StringWriter(out)), format);

        printer.print("foo,bar");
        printer.print("foo,bar");
        printer.print("foo,bar");
        printer.print("foo,bar");
        printer.print("foo,bar");
        printer.print("foo\"bar");
        printer.print("foo,bar\\");
        printer.print("foo,bar\"");
        printer.print("foo,bar\\\"");
        printer.print("foo,bar");
        printer.print("foo,bar");
        printer.print("foo,bar");
        printer.print("foo,bar");
        printer.print("foo,bar");
        printer.print("foo,bar");
        printer.print("foo,bar");
        printer.print("foo,bar");
        printer.print("foo,bar");
        printer.print("foo,bar");
        printer.close();

        final String expected = "foo,bar,\"foo,bar\",foo,bar,foo\"bar\",foo,bar\\,foo,bar\"\\,\"foo,bar\",foo,bar,foo,bar,foo,bar,foo,bar,foo,bar,foo,bar,foo,bar,foo,bar,foo,bar,foo,bar,foo,bar,foo,bar,foo,bar,foo,bar\n";
        final String actual = out.toString();
        assertTrue("Unexpected output:\n" + actual, expected.equals(actual));
    }

    @Test
    public void testPrintComment() throws IOException {
        final CSVFormat format = new CSVFormat(new CSVParserDefaultSetup(), true);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new PrintWriter(new StringWriter(out)), format);

        printer.printComment("This is a comment.");
        printer.println();
        printer.printComment("This is another comment.\nwith a line break.");
        printer.println();
        printer.close();

        final String expected = "-- This is a comment.\n-- This is another comment.\n";
        final String actual = out.toString();
        assertTrue("Unexpected output:\n" + actual, expected.equals(actual));
    }

    @Test
    public void testPrintRecord() throws IOException, SQLException {
        final CSVFormat format = new CSVFormat(new CSVParserDefaultSetup(), true);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new PrintWriter(new StringWriter(out)), format);

        final Object[] record1 = new Object[] { 1, "foo" };
        final Object[] record2 = new Object[] { 2, "bar" };
        printer.printRecord(record1);
        printer.printRecord(record2);
        printer.close();

        final String expected = "1,\"foo\",2,\"bar\"\n";
        final String actual = out.toString();
        assertTrue("Unexpected output:\n" + actual, expected.equals(actual));
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        final CSVFormat format = new CSVFormat(new CSVParserDefaultSetup(), true);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new PrintWriter(new StringWriter(out)), format);

        final Object[] record1 = new Object[] { 1, "foo" };
        final Object[] record2 = new Object[] { 2, "bar" };
        final Object[] record3 = new Object[] { 3, "baz" };
        printer.printRecords(Arrays.asList(record1, record2, record3));
        printer.close();

        final String expected = "1,\"foo\"\n2,\"bar\"\n3,\"baz\"\n";
        final String actual = out.toString();
        assertTrue("Unexpected output:\n" + actual, expected.equals(actual));
    }

    @Test
    public void testPrintRecordsFromResultSet() throws IOException, SQLException {
        final CSVFormat format = new CSVFormat(new CSVParserDefaultSetup(), true);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new PrintWriter(new StringWriter(out)), format);

        final String sql = "SELECT 1 as id, 'foo' as name FROM mytable";
        final Connection connection = mock(Connection.class);
        final PreparedStatement statement = mock(PreparedStatement.class);
        when(connection.prepareStatement(sql)).thenReturn(statement);
        final ResultSet resultSet = mock(ResultSet.class);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString("name")).thenAnswer(i -> Integer.toString(i.getIntValue(0)) + "," + "name" + i.getString(1));

        printer.printRecords(resultSet);
        printer.close();

        final String expected = "1,\"1,name foo\"\n";
        final String actual = out.toString();
        assertTrue("Unexpected output:\n" + actual, expected.equals(actual));
    }

    @Test
    public void testClose() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new PrintWriter(new StringWriter(out)), new CSVFormat(new CSVParserDefaultSetup(), true));

        printer.print(123);
        printer.print("foo");
        printer.print("bar");
        printer.close();

        final String actual = out.toString();
        assertTrue("Unexpected output:\n" + actual, actual.endsWith("\n"));
        assertFalse("CSVPrinter should be closed:\n" + actual, printer.isOpen());
    }

    @Test(expected = IllegalStateException.class)
    public void testPrintAfterClose() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(new PrintWriter(new StringWriter(out)), new CSVFormat(new CSVParserDefaultSetup(), true));

        printer.print(123);
        printer.print("foo");
        printer.print("bar");
        printer.close();
        printer.print("baz");
    }
}