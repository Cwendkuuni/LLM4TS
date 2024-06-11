package org.apache.commons.csv.test;

import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.commons.csv.CSVParser.parse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;

/**
 * Test class for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    /**
     * Test CSVPrinter with default settings.
     *
     * @throws SQLException
     *             If an SQL error occurs
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testCSVPrinterDefault() throws SQLException, IOException {
        final StringWriter out = new StringWriter();
        final CSVFormat format = DEFAULT;
        final CSVPrinter printer = new CSVPrinter(out, format);

        // print some values
        printer.print("Hello");
        printer.print(1);
        printer.print(1.0);
        printer.print(true);
        printer.println();
        printer.printRecord("Test", 1, 1.1, true);
        printer.println();
        printer.printComment("This is a comment");
        printer.close();

        // parse the result
        final CSVFormat formatForParsing = DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        final CSVRecord record = parse(out.toString(), formatForParsing).getRecords().get(0);

        // assert the result
        assertEquals("Hello", record.get("0"));
        assertEquals("1", record.get("1"));
        assertNull(record.get("2")); // no Number can be directly parsed into String
        assertEquals("1.0", record.get("3"));
        assertTrue(record.get("4").isBoolean());
        assertEquals("Test", record.get("5"));
        assertEquals("1", record.get("6"));
        assertEquals("1.1", record.get("7"));
        assertTrue(record.get("8").isBoolean());
        assertEquals("#This is a comment", record.get("9"));

        // flush the comment line
        final String result = out.toString();
        assertEquals(result.substring(0, result.lastIndexOf("\n")), result.substring(0, result.lastIndexOf("\n")) + "\n#This is a comment");
    }

    /**
     * Test CSVPrinter with custom settings.
     *
     * @throws SQLException
     *             If an SQL error occurs
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testCSVPrinterCustom() throws SQLException, IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(';', '\"') // semicolon as delimiter, double quotes as quoting character
                .withQuoteMode(QuoteMode.ALL) // quote all values
                .withCommentPrefix("//"); // comments start with double slashes
        final CSVPrinter printer = new CSVPrinter(out, format);

        // print some values
        printer.print("Hello");
        printer.print(1);
        printer.print(1.0);
        printer.print(true);
        printer.println();
        printer.printRecord("Test", 1, 1.1, true);
        printer.println();
        printer.printComment("This is a comment");
        printer.close();

        // parse the result
        final CSVFormat formatForParsing = new CSVFormat(';', '\"') // semicolon as delimiter, double quotes as quoting character
                .withQuoteMode(QuoteMode.MINIMAL);
        final CSVRecord record = parse(new String(out.toByteArray()), formatForParsing).getRecords().get(0);

        // assert the result
        assertEquals("\"Hello\"", record.get("0"));
        assertEquals("1", record.get("1"));
        assertNull(record.get("2")); // no Number can be directly parsed into String
        assertEquals("1.0", record.get("3"));
        assertTrue(record.get("4").isBoolean());
        assertEquals("Test", record.get("5"));
        assertEquals("1", record.get("6"));
        assertEquals("1.1", record.get("7"));
        assertTrue(record.get("8").isBoolean());
        assertEquals("//This is a comment", record.get("9"));

        // check that quoting was applied
        final String result = new String(out.toByteArray());
        assertEquals(result.substring(0, result.lastIndexOf("\n")), result.substring(0, result.lastIndexOf("\n")) + "\";\"Hello\";\"");
        assertEquals(result.substring(result.lastIndexOf("\n")), "\";\"Test\";1;\";1.1\";\"true\";//This is a comment");
    }

    /**
     * Test CSVPrinter with illegal arguments.
     *
     * @throws SQLException
     *             If an SQL error occurs
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testCSVPrinterIllegalArguments() throws SQLException, IOException {
        // null stream
        new CSVPrinter(null, DEFAULT);

        // null format
        new CSVPrinter(new ByteArrayOutputStream(), null);

        // inconsistent format
        new CSVPrinter(new ByteArrayOutputStream(), new CSVFormat().withDelimiter(';').withEscapingCharacter('"'));

        // print record with too many elements
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final CSVPrinter printer = new CSVPrinter(out, DEFAULT);
        printer.printRecord(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        printer.close();
    }

    /**
     * Test CSVPrinter with SQL result set.
     *
     * @throws SQLException
     *             If an SQL error occurs
     * @throws IOException
     *             If an I/O error occurs
     */
    @org.junit.Test
    public void testCSVPrinterWithSQLResultSet() throws SQLException, IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final String sql = "SELECT name, age, salary, is_employed FROM employees";
        final Statement stmt = new java.sql.Connection(new java.sql.DriverManager("jdbc:derby:testdb;create=true")).createStatement();
        final ResultSet rs = stmt.executeQuery(sql);
        final CSVPrinter printer = new CSVPrinter(out, DEFAULT);
        printer.printRecords(rs);
        printer.close();

        // parse the result
        final CSVFormat formatForParsing = DEFAULT.withQuoteMode(QuoteMode.MINIMAL);
        final CSVRecord[] records = parse(new String(out.toByteArray()), formatForParsing).getRecords().toArray(new CSVRecord[0]);

        // assert the result
        assertEquals("name", records[0].get("0"));
        assertEquals(35, records[0].get("1").intValue());
        assertEquals(45000.0, records[0].get("2").doubleValue(), 0.0);
        assertTrue(records[0].get("3").isBoolean());
        assertEquals("age", records[1].get("0"));
        assertEquals(28, records[1].get("1").intValue());
        assertEquals(32000.0, records[1].get("2").doubleValue(), 0.0);
        assertTrue(records[1].get("3").isBoolean());
        // ... continue for all records
    }
}