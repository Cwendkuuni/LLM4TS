package org.apache.commons.csv;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordBuilder;
import org.apache.commons.csv.Quote;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link CSVPrinter} class.
 */
public class CSVPrinterTest {

    private CSVFormat format;
    private CSVPrinter printer;
    private ByteArrayOutputStream out;

    @Before
    public void setUp() throws IOException {
        format = new CSVFormatBuilder() //
                .setDelimiter(',') //
                .setQuoteChar('"') //
                .setNullString("NULL") //
                .build();
        out = new ByteArrayOutputStream();
        printer = new CSVPrinter(out, format);
    }

    @Test
    public void testPrintString() throws IOException {
        printer.print("Hello, World!");
        assertEquals("\"Hello, World!\"", out.toString());
    }

    @Test
    public void testPrintInteger() throws IOException {
        printer.print(123);
        assertEquals("123", out.toString());
    }

    @Test
    public void testPrintBoolean() throws IOException {
        printer.print(true);
        assertEquals("TRUE", out.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        assertEquals("NULL", out.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        CSVRecord record = new CSVRecordBuilder() //
                .add("Alice") //
                .add("25") //
                .add(true) //
                .build();
        printer.printRecord(record);
        assertEquals("\"Alice\",25,TRUE", out.toString());
    }

    @Test
    public void testPrintRecordWithQuoting() throws IOException {
        CSVFormat format = new CSVFormatBuilder() //
                .setDelimiter(',') //
                .setQuoteChar('\'') //
                .build();
        CSVPrinter printer = new CSVPrinter(out, format);
        CSVRecord record = new CSVRecordBuilder() //
                .add("John, \"Doe\"") //
                .build();
        printer.printRecord(record);
        assertEquals("\"John, \'Doe\'\"", out.toString());
        printer.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapNullFormat() throws IOException {
        printer = new CSVPrinter(out, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapNullOut() throws IOException {
        printer = new CSVPrinter(null, format);
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("# This is a comment");
        assertEquals("# This is a comment\n" + CR, out.toString());
        printer.println();
        printer.print("Hello, World!");
        assertEquals("\"Hello, World!\"\n" + CR, out.toString());
    }

    @Test
    public void testPrintRecordWithComment() throws IOException {
        CSVFormat format = new CSVFormatBuilder() //
                .setDelimiter(',') //
                .setCommentPrefix("#") //
                .build();
        CSVPrinter printer = new CSVPrinter(out, format);
        CSVRecord record = new CSVRecordBuilder() //
                .add("Alice") //
                .add("# This is a comment") //
                .build();
        printer.printRecord(record);
        assertEquals("\"Alice\",# This is a comment\n" + CR, out.toString());
        printer.close();
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        String[] data = { "Alice,25,TRUE", "Bob,23,FALSE", "Charlie,30,TRUE" };
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.printRecords(data);
        assertEquals(getStringFromResource("/testData.csv"), out.toString());
        printer.close();
    }

    @Test
    public void testPrintRecordsFromIterable() throws IOException, SQLException {
        String[][] data = { { "Alice", "25", "TRUE" }, { "Bob", "23", "FALSE" }, { "Charlie", "30", "TRUE" } };
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.printRecords(CSVRecord.arrayIterator(data));
        assertEquals(getStringFromResource("/testData.csv"), out.toString());
        printer.close();
    }

    @Test
    public void testPrintRecordsFromResultSet() throws IOException, SQLException {
        String sql = "SELECT name, age, is_employed FROM employees";
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(1)).thenReturn("Alice");
        when(resultSet.getInt(2)).thenReturn(25);
        when(resultSet.getBoolean(3)).thenReturn(true);
        when(resultSet.getString(2)).thenReturn("23");
        when(resultSet.getBoolean(3)).thenReturn(false);
        when(resultSet.getString(1)).thenReturn(null);
        printer.printRecords(resultSet);
        assertEquals(getStringFromResource("/testDataWithNull.csv"), out.toString());
        printer.close();
    }

    private String getStringFromResource(String resourcePath) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new String(classLoader.getResourceAsStream(resourcePath));
    }
}