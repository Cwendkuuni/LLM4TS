package org.apache.commons.csv;

import static org.apache.commons.csv.CSVPrinter.COMMENT;
import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link CSVPrinter} class.
 */
public class CSVPrinterTest {

    private CSVFormat format;
    private CSVPrinter printer;
    private ByteArrayOutputStream out;

    @Before
    public void setup() throws IOException {
        format = new CSVFormat(',', Quote.MINIMAL);
        out = new ByteArrayOutputStream();
        printer = new CSVPrinter(new OutputStreamWriter(out), format);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        assertNotNull(printer);
    }

    /**
     * Test close method.
     */
    @Test
    public void testClose() throws IOException {
        printer.close();

        assertFalse(printer.isOpen());
    }

    /**
     * Test print method with null value.
     */
    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);

        String result = printer.getOut().toString();
        assertEquals(format.getNullString(), result.trim());
    }

    /**
     * Test print method with String value.
     */
    @Test
    public void testPrintString() throws IOException {
        printer.print("hello");

        String result = printer.getOut().toString();
        assertEquals("hello", result.trim());
    }

    /**
     * Test print method with Object value.
     */
    @Test
    public void testPrintObject() throws IOException {
        printer.print(new StringBuilder("hello"));

        String result = printer.getOut().toString();
        assertEquals("hello", result.trim());
    }

    /**
     * Test print method with numeric value.
     */
    @Test
    public void testPrintNumber() throws IOException {
        printer.print(123);

        String result = printer.getOut().toString();
        assertEquals("123", result.trim());
    }

    /**
     * Test print method with escape character.
     */
    @Test
    public void testPrintEscapeCharacter() throws IOException {
        printer.print(CR);
        printer.print(LF);

        String result = printer.getOut().toString();
        assertEquals(String.format("%c%c", CR, LF), result.trim());
    }

    /**
     * Test print method with quote character.
     */
    @Test
    public void testPrintQuoteCharacter() throws IOException {
        format.setQuoteChar('"');
        printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.print("\"hello\"");

        String result = printer.getOut().toString();
        assertEquals("\"hello\"", result.trim());
    }

    /**
     * Test print method with quote and escape characters.
     */
    @Test
    public void testPrintQuoteEscapeCharacter() throws IOException {
        format.setQuoteChar('"');
        format.setEscape('\\');
        printer = new CSVPrinter(new OutputStreamWriter(out), format);

        printer.print("\"hello\\hello\"");

        String result = printer.getOut().toString();
        assertEquals("\"hello\\\"hello\"", result.trim());
    }

    /**
     * Test printComment method.
     */
    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");

        String result = printer.getOut().toString();
        assertEquals(format.getCommentStart() + SP + "This is a comment" + LF, result.trim());
    }

    /**
     * Test println method.
     */
    @Test
    public void testPrintln() throws IOException {
        printer.println();

        String result = printer.getOut().toString();
        assertEquals(format.getRecordSeparator(), result.trim());
        result = printer.getOut().toString();
        assertTrue(result.isEmpty());
    }

    /**
     * Test printRecord method.
     */
    @Test
    public void testPrintRecordStringArray() throws IOException {
        String[] values = {"hello1", "hello2", "hello3"};
        printer.printRecord(values);

        String result = printer.getOut().toString();
        List<String> expected = Arrays.asList(values);

        CSVParser parser = new CSVParser(new StringReader(result), format);

        List<String[]> records = parser.getRecords();
        assertEquals(1, records.size());
        assertEquals(values.length, records.get(0).length);
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], records.get(0)[i]);
        }

        result = printer.getOut().toString();
        assertTrue(result.isEmpty());
    }

    /**
     * Test printRecord method.
     */
    @Test
    public void testPrintRecordList() throws IOException {
        List<String> values = Arrays.asList("hello1", "hello2", "hello3");
        printer.printRecord(values);

        String result = printer.getOut().toString();
        List<String> expected = Arrays.asList(values);

        CSVParser parser = new CSVParser(new StringReader(result), format);

        List<String[]> records = parser.getRecords();
        assertEquals(1, records.size());
        assertEquals(values.size(), records.get(0).length);
        for (int i = 0; i < values.size(); i++) {
            assertEquals(values.get(i), records.get(0)[i]);
        }

        result = printer.getOut().toString();
        assertTrue(result.isEmpty());
    }

    /**
     * Test printRecords method.
     */
    @Test
    public void testPrintRecordsStringArray() throws IOException, SQLException {
        String[][] values = {{"hello1", "hello2", "hello3"}, {"hello4", "hello5", "hello6"}};
        printer.printRecords(Arrays.asList(values));

        String result = printer.getOut().toString();
        List<String> expectedRecords = Arrays.asList(Arrays.stream(values).map(record -> String.join(format.getDelimiter(), record)).toList());

        CSVParser parser = new CSVParser(new StringReader(result), format);

        List<String[]> records = parser.getRecords();
        assertEquals(expectedRecords.size(), records.size());
        for (int i = 0; i < expectedRecords.size(); i++) {
            String expectedRecord = expectedRecords.get(i);
            String[] actualRecord = records.get(i);

            assertEquals(expectedRecord, String.join(format.getDelimiter(), Arrays.asList(actualRecord)));
        }

        result = printer.getOut().toString();
        assertTrue(result.isEmpty());
    }

    /**
     * Test printRecords method with ResultSet.
     */
    @Test
    public void testPrintRecordsResultSet() throws IOException, SQLException {
        String[][] values = {{"1", "hello1"}, {"2", "hello2"}, {"3", "hello3"}};

        ResultSet resultSet = new MockResultSet(values);
        printer.printRecords(resultSet);

        String result = printer.getOut().toString();
        List<String> expectedRecords = Arrays.asList(Arrays.stream(values).map(record -> String.join(format.getDelimiter(), record)).toList());

        CSVParser parser = new CSVParser(new StringReader(result), format);

        List<String[]> records = parser.getRecords();
        assertEquals(expectedRecords.size(), records.size());
        for (int i = 0; i < expectedRecords.size(); i++) {
            String expectedRecord = expectedRecords.get(i);
            String[] actualRecord = records.get(i);

            assertEquals(expectedRecord, String.join(format.getDelimiter(), Arrays.asList(actualRecord)));
        }

        result = printer.getOut().toString();
        assertTrue(result.isEmpty());
    }

}

class MockResultSet implements ResultSet {

    private final String[][] values;
    private int index;

    public MockResultSet(String[][] values) {
        this.values = values;
    }

    @Override
    public boolean next() throws SQLException {
        return ++index < values.length;
    }

    @Override
    public int getMetaData() throws SQLException {
        throw new UnsupportedOperationException("This method is not supported.");
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        return values[index][columnIndex];
    }

    @Override
    public int getColumnCount() throws SQLException {
        return values[0].length;
    }

}