package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    /**
     * Test method for {@link CSVPrinter#CSVPrinter(Appendable, CSVFormat)}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testCSVPrinter() throws Exception {
        final CSVFormat format = CSVFormat.DEFAULT.withQuote('"');

        try (final ByteArrayOutputStream output = new ByteArrayOutputStream();
             final OutputStreamWriter writer = new OutputStreamWriter(output)) {

            final CSVPrinter printer = new CSVPrinter(writer, format);
            printer.printRecord(new String[]{"apple", "banana"});
            printer.close();

            final String expected = "apple,\"banana\"\n";
            assertEquals(expected, output.toString());
        }
    }

    /**
     * Test method for {@link CSVPrinter#print(Object)}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testPrint() throws Exception {
        final CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), format)) {
            printer.print("Hello");
            printer.println();
            printer.print(42);
            printer.println();
            printer.print(null);
            printer.println();
            printer.printRecord(new String[]{"apple", "banana"});
            printer.close();

            final String expected = "Hello\n42\nnull\napple,\"banana\"\n";
            assertEquals(expected, output.toString());
        }
    }

    /**
     * Test method for {@link CSVPrinter#print(CharSequence, int, int)}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testPrintWithOffsetAndLength() throws Exception {
        final CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), format)) {
            printer.print("abcdefghijklmnopqrstuvwxyz", 0, 5);
            printer.println();
            printer.print("abcdefghijklmnopqrstuvwxyz", 6, 9);
            printer.println();
            printer.print("abcdefghijklmnopqrstuvwxyz", 10, 5);
            printer.println();
            printer.printRecord(new String[]{"apple", "banana"});
            printer.close();

            final String expected = "abcde\nanpple,\"banana\"\n";
            assertEquals(expected, output.toString());
        }
    }

    /**
     * Test method for {@link CSVPrinter#printAndQuote(Object, CharSequence, int, int)}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testPrintAndQuote() throws Exception {
        final CSVFormat format = CSVFormat.DEFAULT.withQuote('"').withQuotePolicy(Quote.MINIMAL);
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), format)) {
            printer.printAndQuote("Hello", null, 0, 0);
            printer.println();
            printer.printAndQuote("Hello", "Hellö", 0, 5);
            printer.println();
            printer.printAndQuote("Hello", "Hellö", 6, 2);
            printer.println();
            printer.printAndQuote("Hello", "Hellö, World!", 0, 14);
            printer.println();
            printer.printRecord(new String[]{"apple", "banana"});
            printer.close();

            final String expected = "\"Hello\"\n\"Hellö\"\n\"Hell\"\n\"Hellö, World!\"\napple,\"banana\"\n";
            assertEquals(expected, output.toString());
        }
    }

    /**
     * Test method for {@link CSVPrinter#printAndEscape(CharSequence, int, int)}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testPrintAndEscape() throws Exception {
        final CSVFormat format = CSVFormat.DEFAULT.withQuote(null).withEscape('\\');
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), format)) {
            printer.printAndEscape("Hello", 0, 0);
            printer.println();
            printer.printAndEscape("Hello\nworld", 0, 6);
            printer.println();
            printer.printAndEscape("Hello\"world", 0, 7);
            printer.println();
            printer.printAndEscape("Hello\\\"world", 0, 8);
            printer.println();
            printer.printRecord(new String[]{"apple", "banana"});
            printer.close();

            final String expected = "Hello\nHello\\nHello\\\"\nHello\\\\\"\napple,\"banana\"\n";
            assertEquals(expected, output.toString());
        }
    }

    /**
     * Test method for {@link CSVPrinter#printComment(String)}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testPrintComment() throws Exception {
        final CSVFormat format = CSVFormat.DEFAULT.withComment('C');
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), format)) {
            printer.printComment("# This is a comment");
            printer.println();
            printer.printRecord(new String[]{"apple", "banana"});
            printer.close();

            final String expected = "C # This is a comment\napple,\"banana\"\n";
            assertEquals(expected, output.toString());
        }
    }

    /**
     * Test method for {@link CSVPrinter#println()}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testPrintln() throws Exception {
        final CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), format)) {
            printer.print("Hello");
            printer.println();
            printer.print("world");
            printer.println();
            printer.printRecord(new String[]{"apple", "banana"});
            printer.close();

            final String expected = "Hello\nworld\napple,\"banana\"\n";
            assertEquals(expected, output.toString());
        }
    }

    /**
     * Test method for {@link CSVPrinter#printRecord(Iterable)}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testPrintRecord() throws Exception {
        final CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), format)) {
            printer.printRecord(Arrays.asList("apple", "banana"));
            printer.close();

            final String expected = "apple,\"banana\"\n";
            assertEquals(expected, output.toString());
        }
    }

    /**
     * Test method for {@link CSVPrinter#printRecord(Object[])}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testPrintRecordArray() throws Exception {
        final CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), format)) {
            printer.printRecord(new String[]{"apple", "banana"});
            printer.close();

            final String expected = "apple,\"banana\"\n";
            assertEquals(expected, output.toString());
        }
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(Iterable)}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testPrintRecords() throws Exception {
        final CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), format)) {
            printer.printRecords(Arrays.asList(Arrays.asList("apple1", "banana1"), Arrays.asList("apple2", "banana2")));
            printer.close();

            final String expected = "apple1,\"banana1\"\napple2,\"banana2\"\n";
            assertEquals(expected, output.toString());
        }
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(Object[])}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testPrintRecordsArray() throws Exception {
        final CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
        final ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), format)) {
            printer.printRecords(new String[][]{
                    {"apple1", "banana1"},
                    {"apple2", "banana2"}
            });
            printer.close();

            final String expected = "apple1,\"banana1\"\napple2,\"banana2\"\n";
            assertEquals(expected, output.toString());
        }
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(ResultSet)}.
     *
     * @throws Exception if an error occurs
     */
    @org.junit.Test
    public void testPrintRecordsResultSet() throws Exception {
        final String sql = "SELECT name, age FROM users";

        try (final Connection connection = DriverManager.getConnection("jdbc:hsqldb:mem:testdb");
             final Statement statement = connection.createStatement()) {

            statement.executeUpdate("CREATE TABLE users (name VARCHAR(10), age INT)");
            statement.executeUpdate("INSERT INTO users VALUES ('Alice', 30)");
            statement.executeUpdate("INSERT INTO users VALUES ('Bob', 40)");
            statement.executeQuery(sql);

            final CSVFormat format = CSVFormat.DEFAULT.withQuote('"');
            final ByteArrayOutputStream output = new ByteArrayOutputStream();

            try (final CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(output), format)) {
                printer.printRecords(statement.getResultSet());
                printer.close();

                final String expected = "name,\"age\"\nAlice,\"30\"\nBob,\"40\"\n";
                assertEquals(expected, output.toString());
            }
        }
    }
}