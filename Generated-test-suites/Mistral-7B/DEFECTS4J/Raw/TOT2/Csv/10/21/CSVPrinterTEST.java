package org.apache.commons.csv;

import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

/**
 * Test cases for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    /**
     * Tests that a new CSVPrinter instance can be created and that values can be printed.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintValues() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.print("Alice");
        printer.print("Bob");
        printer.println();
        printer.print("Charlie");
        printer.println();

        // check output
        String csv = out.toString();
        CSVRecord record = CSVFormat.DEFAULT.withHeader("Name").parse(csv);
        assertEquals("Name[0]", "Alice", record.get(0));
        assertEquals("Name[1]", "Bob", record.get(1));
        assertEquals("Name[2]", "Charlie", record.get(2));

        // reset for next tests
        out.reset();
        printer.close();
    }

    /**
     * Tests that null values are handled correctly.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintNullValues() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL, null);
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.print("Alice");
        printer.print(null);
        printer.print("Bob");
        printer.println();

        // check output
        String csv = out.toString();
        CSVRecord record = CSVFormat.DEFAULT.parse(csv);
        assertEquals("Size: 3", 3, record.size());
        assertEquals("Get[0]", "Alice", record.get(0));
        assertNull("Get[1]", record.get(1));
        assertEquals("Get[2]", "Bob", record.get(2));

        // reset for next tests
        out.reset();
        printer.close();
    }

    /**
     * Tests that quotes are handled correctly.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintQuotes() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.EXPLICIT_QUOTES;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.print("Alice");
        printer.print(format.getQuoteChar(), "Bob");
        printer.println();

        // check output
        String csv = out.toString();
        CSVRecord record = CSVFormat.DEFAULT.parse(csv);
        assertEquals("Size: 3", 3, record.size());
        assertEquals("Get[0]", "Alice", record.get(0));
        assertEquals("Get[1]", "\"Bob\"", record.get(1));

        // reset for next tests
        out.reset();
        printer.close();
    }

    /**
     * Tests that escape characters are handled correctly.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintEscapedValues() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.EXPLICIT_QUOTES.withEscapeCharacter('\\');
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.print("Alice");
        printer.print(format.getEscapeCharacter() + "Bob");
        printer.println();

        // check output
        String csv = out.toString();
        CSVRecord record = CSVFormat.DEFAULT.parse(csv);
        assertEquals("Size: 3", 3, record.size());
        assertEquals("Get[0]", "Alice", record.get(0));
        assertEquals("Get[1]", "Bob", record.get(1));

        // reset for next tests
        out.reset();
        printer.close();
    }

    /**
     * Tests that records with different numbers of values are handled correctly.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordsWithDifferentNumbersOfValues() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.printRecord(new String[]{"Alice"});
        printer.printRecord(new String[]{"Alice", "Bob"});
        printer.println();

        // check output
        String csv = out.toString();
        CSVRecord firstRecord = CSVFormat.DEFAULT.parse(csv).getRecord(0);
        assertEquals("Size: 1", 1, firstRecord.size());
        assertEquals("Get[0]", "Alice", firstRecord.get(0));

        CSVRecord secondRecord = CSVFormat.DEFAULT.parse(csv).getRecord(1);
        assertEquals("Size: 2", 2, secondRecord.size());
        assertEquals("Get[0]", "Alice", secondRecord.get(0));
        assertEquals("Get[1]", "Bob", secondRecord.get(1));

        // reset for next tests
        out.reset();
        printer.close();
    }

    /**
     * Tests that comments are handled correctly.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintComments() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.printComment("# This is a comment");
        printer.println();
        printer.printRecord(new String[]{"Alice", "Bob"});
        printer.println();
        printer.printComment("# Another comment");
        printer.println();

        // check output
        String csv = out.toString();
        assertEquals("# This is a comment" + LF + CR + "Alice" + CR + "Bob" + LF + "# Another comment" + LF + CR, csv);

        // reset for next tests
        out.reset();
        printer.close();
    }

    /**
     * Tests that the record separator is handled correctly.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordSeparator() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(';', null, null);
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.printRecord(new String[]{"Alice", "Bob"});
        printer.println();

        // check output
        String csv = out.toString();
        String recordSeparator = format.getRecordSeparator();
        assertEquals(recordSeparator + "Alice;" + recordSeparator + "Bob" + LF, csv);

        // reset for next tests
        out.reset();
        printer.close();
    }

    /**
     * Tests that empty records are handled correctly.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintEmptyRecords() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.printComment("# Empty record test");
        printer.println();
        printer.printRecord();
        printer.println();

        // check output
        String csv = out.toString();
        assertEquals("# Empty record test" + LF + CR + LF, csv.substring(0, 23));
        assertEquals(Integer.toString(3), Integer.toString(csv.split(LF).length));

        // reset for next tests
        out.reset();
        printer.close();
    }

    /**
     * Tests that records with leading, trailing, and embedded whitespace are handled correctly.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testPrintRecordsWithWhitespace() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.printRecord(new String[]{"Alice", " Bob "});
        printer.println();

        // check output
        String csv = out.toString();
        String expected = "Alice, Bob";
        assertEquals(expected, csv.substring(0, csv.indexOf(LF)));

        // reset for next tests
        out.reset();
        printer.close();
    }

    /**
     * Tests that an empty CSVPrinter can be created.
     */
    @org.junit.Test
    public void testCreateEmptyCSVPrinter() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(System.out), format);
        printer.close();
    }

    /**
     * Tests that an I/O error is thrown when the output stream is closed before the printer is.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test(expected = IOException.class)
    public void testPrintValuesAfterClose() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);
        printer.close();
        printer.print("Alice");
    }

    /**
     * Tests that an I/O error is thrown when the output stream is not writable.
     *
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test(expected = IOException.class)
    public void testPrintValuesToNonWritableOutputStream() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.close();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);
        printer.print("Alice");
    }

    /**
     * Tests that an SQL exception is thrown when the result set is empty.
     *
     * @throws IOException
     *             If an I/O error occurs
     * @throws SQLException if a database access error occurs
     */
    @org.junit.Test(expected = SQLException.class)
    public void testPrintRecordsFromEmptyResultSet() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(out), format);
        ResultSet resultSet = new StubResultSet();
        printer.printRecords(resultSet);
    }

    /**
     * A stub result set that always returns false when calling next().
     */
    public static class StubResultSet extends ResultSet {

        @Override
        public boolean next() throws SQLException {
            throw new SQLException("Result set is empty.");
        }
    }

}