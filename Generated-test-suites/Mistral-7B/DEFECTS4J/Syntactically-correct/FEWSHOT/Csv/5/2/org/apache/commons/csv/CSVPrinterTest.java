package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream outContent;

    @Before
    public void setUp() {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteAll();
        outContent = new ByteArrayOutputStream();
        printer = new CSVPrinter(outContent, format);
    }

    @Test
    public void testPrintQuoted() throws IOException {
        printer.print("\"foo\"");
        printer.flush();
        String expected = "\"" + Constants.QUOTE + "foo" + Constants.QUOTE + '"';
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintQuotedWithEscape() throws IOException {
        printer.print("\"foo\\\"bar\"");
        printer.flush();
        String expected = "\"foo\\\\\"bar\"";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintEscaped() throws IOException {
        printer.print("foo\\bar");
        printer.flush();
        String expected = "foo\\bar";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintEscapedQuoted() throws IOException {
        printer.print("foo\\\"bar");
        printer.flush();
        String expected = "foo\\\"bar";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        printer.flush();
        String expected = Constants.NULL_STRING;
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintEmptyArray() throws IOException {
        printer.close();
        printer = new CSVPrinter(outContent, CSVFormat.DEFAULT);
        printer.println();
        printer.close();

        String expected = System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintArray() throws IOException {
        Object[] values = { "foo", "bar", "baz" };
        printer.printRecord(values);
        printer.flush();

        CSVRecord record = CSVFormat.DEFAULT.parse(outContent.toString());
        String[] expected = { "foo", "bar", "baz" };
        assertArrayEquals(expected, record.toArray());
    }

    @Test
    public void testPrintArrayQuoted() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteAll();
        printer = new CSVPrinter(outContent, format);
        Object[] values = { "foo", "\"bar\"", "baz" };
        printer.printRecord(values);
        printer.flush();

        CSVRecord record = CSVFormat.DEFAULT.parse(outContent.toString());
        String[] expected = { "\"" + Constants.QUOTE + "foo" + Constants.QUOTE, "\"" + Constants.QUOTE + "bar" + Constants.QUOTE + '"', "\"" + Constants.QUOTE + "baz" + Constants.QUOTE };
        assertArrayEquals(expected, record.toArray());
    }

    @Test
    public void testPrintIterable() throws IOException, SQLException {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteAll();
        printer = new CSVPrinter(outContent, format);

        String[][] data = { { "foo", "\"bar\"" }, { "baz", null } };
        printer.printRecords(data);
        printer.flush();

        CSVRecord[] records = CSVFormat.DEFAULT.parse(outContent.toString()).toArray(CSVRecord[].class);

        for (int i = 0; i < records.length; i++) {
            String[] expected = data[i];
            String[] actual = records[i].toArray(String[].class);
            assertArrayEquals(expected, actual);
        }
    }

    @Test
    public void testPrintResultSet() throws IOException, SQLException {
        String[][] expected = { { "foo", "\"bar\"" }, { "baz", "qux" } };
        ResultSet resultSet = getTestResultSet();

        printer.printRecords(resultSet);
        printer.flush();

        CSVRecord[] records = CSVFormat.DEFAULT.parse(outContent.toString()).toArray(CSVRecord[].class);

        for (int i = 0; i < records.length; i++) {
            String[] actual = records[i].toArray(String[].class);
            String[] expectedRecord = expected[i];
            assertArrayEquals(expectedRecord, actual);
        }
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a test");
        printer.println();
        printer.flush();

        String expected = Constants.COMMENT_START + ' ' + "This is a test" + System.lineSeparator() + Constants.COMMENT_START + ' ';
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintQuotePolicy() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(Quote.MINIMAL);
        printer = new CSVPrinter(outContent, format);

        printer.print("foo bar baz");
        printer.flush();

        String expected = "foo bar baz";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintQuotePolicyAll() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(Quote.ALL);
        printer = new CSVPrinter(outContent, format);

        printer.print("foo bar baz");
        printer.flush();

        String expected = "\"" + Constants.QUOTE + "foo" + Constants.QUOTE + '\'' + Constants.DELIMITER + "\"" + Constants.QUOTE + "bar" + Constants.QUOTE + '\'' + Constants.DELIMITER + "\"" + Constants.QUOTE + "baz" + Constants.QUOTE;
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintQuotePolicyNone() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(Quote.NONE);
        printer = new CSVPrinter(outContent, format);

        printer.print("foo bar baz");
        printer.flush();

        String expected = "foo bar baz";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintQuotePolicyMinimal() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withQuotePolicy(Quote.MINIMAL);
        printer = new CSVPrinter(outContent, format);

        printer.print("foo bar baz");
        printer.flush();

        String expected = "foo,\" bar \",baz";
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintRecordSeparators() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator(System.lineSeparator());
        printer = new CSVPrinter(outContent, format);

        Object[] values = { "foo", "bar", "baz" };
        printer.printRecord(values);
        printer.flush();

        String expected = "foo" + System.lineSeparator() + "bar" + System.lineSeparator() + "baz" + System.lineSeparator();
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testPrintRecordSeparatorsWithQuotes() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteAll().withRecordSeparator(";");
        printer = new CSVPrinter(outContent, format);

        Object[] values = { "foo", "\"bar\"" };
        printer.printRecord(values);
        printer.flush();

        String expected = "\"" + Constants.QUOTE + "foo" + Constants.QUOTE + ';' + "\"" + Constants.QUOTE + "bar" + Constants.QUOTE + '"';
        assertEquals(expected, outContent.toString());
    }

    private ResultSet getTestResultSet() throws SQLException {
        return new ResultSetMock() {{
            addColumnMetaData("COLUMN1", String.class);
            addColumnMetaData("COLUMN2", String.class);
            setString(1, "foo");
            setString(2, "\"bar\"");
        }};
    }

}

class ResultSetMock implements ResultSet {

    private Object[] data;
    private int index;

    public void addColumnMetaData(String columnName, Class<?> columnType) {
    }

    public boolean next() throws SQLException {
        if (index + 1 < data.length) {
            index++;
            return true;
        }
        return false;
    }

    public String getString(int columnIndex) throws SQLException {
        return (String) data[index][columnIndex];
    }
}