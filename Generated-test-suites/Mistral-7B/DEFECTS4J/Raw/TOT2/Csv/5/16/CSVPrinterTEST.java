package org.apache.commons.csv;

import static org.apache.commons.csv.CSVPrinter.COMMENT;
import static org.apache.commons.csv.CSVPrinter.CR;
import static org.apache.commons.csv.CSVPrinter.LF;
import static org.apache.commons.csv.CSVPrinter.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

/**
 * Test class for the CSVPrinter class.
 */
public class CSVPrinterTest {

    /**
     * Test method for {@link CSVPrinter#CSVPrinter(Appendable, CSVFormat)}.
     */
    @org.junit.Test
    public void testConstructor() {
        final String testString = "Test";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuote ('"'));
        CSVPrinter printer = new CSVPrinter(outputStream, format);
        printer.print(testString);
        printer.close();
        String result = outputStream.toString();
        assertEquals(testString, result);
    }

    /**
     * Test method for {@link CSVPrinter#print(Object)}.
     */
    @org.junit.Test
    public void testPrintObject() throws IOException {
        StringWriter writer = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(writer, format);
        printer.print("Test");
        printer.close();
        String result = writer.toString();
        String expected = "\"Test\"";
        assertEquals(expected, result);

        printer = new CSVPrinter(writer, format);
        printer.print(null);
        printer.close();
        result = writer.toString();
        expected = format.getNullString();
        assertEquals(expected, result);

        printer = new CSVPrinter(writer, format);
        Object[] values = new Object[] {"Test", 123, null, 4.5};
        printer.printRecord(values);
        printer.close();
        result = writer.toString();
        String delimiter = format.getDelimiter().toString();
        expected = String.format("%s\"Test\"%s%s123%s%s%snull%s%s%s%s4.5", delimiter, delimiter, delimiter);
        assertEquals(expected, result);

        // Test exceptions
        printer = new CSVPrinter(null, format);
        assertThrows(IOException.class, printer::print);

        printer = new CSVPrinter(writer, null);
        assertThrows(IllegalArgumentException.class, printer::print);
    }

    /**
     * Test method for {@link CSVPrinter#printAndEscape(CharSequence, int, int)}.
     */
    @org.junit.Test
    public void testPrintAndEscape() throws IOException {
        StringWriter writer = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT.withEscaping(true);
        CSVPrinter printer = new CSVPrinter(writer, format);
        printer.print("Test\r\n");
        printer.close();
        String result = writer.toString();
        String expected = "Test\\r\\n";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CSVPrinter#print(CharSequence, int, int)}.
     */
    @org.junit.Test
    public void testPrintCharSequence() throws IOException {
        StringWriter writer = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(writer, format);
        printer.print("Test", 0, 3);
        printer.close();
        String result = writer.toString();
        String expected = "Test";
        assertEquals(expected, result);

        printer = new CSVPrinter(writer, format);
        printer.print("Test" + COMMENT, 0, 3);
        printer.close();
        result = writer.toString();
        expected = "Test" + COMMENT;
        assertEquals(expected, result);

        printer = new CSVPrinter(writer, format);
        printer.print("Test" + LF, 0, 1);
        printer.close();
        result = writer.toString();
        expected = "Test" + LF;
        assertEquals(expected, result);

        printer = new CSVPrinter(writer, format);
        printer.print("Test" + CR, 0, 1);
        printer.close();
        result = writer.toString();
        expected = "Test" + CR;
        assertEquals(expected, result);

        printer = new CSVPrinter(writer, format);
        printer.print("Test" + SP, 0, 1);
        printer.close();
        result = writer.toString();
        expected = "Test" + SP;
        assertEquals(expected, result);

        printer = new CSVPrinter(writer, format);
        printer.print("Test" + "\u0001", 0, 1);
        printer.close();
        result = writer.toString();
        expected = "Test\\u0001";
        assertEquals(expected, result);

        // Test exception for invalid offset and length
        printer = new CSVPrinter(writer, format);
        assertThrows(IndexOutOfBoundsException.class, () -> printer.print("Test", 5, 3));

        // Test exception for null CharSequence
        printer = new CSVPrinter(writer, format);
        assertThrows(NullPointerException.class, () -> printer.print(null, 0, 3));
    }

    /**
     * Test method for {@link CSVPrinter#printAndQuote(Object, CharSequence, int, int)}.
     */
    @org.junit.Test
    public void testPrintAndQuote() throws IOException {
        StringWriter writer = new StringWriter();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuote('"'));
        CSVPrinter printer = new CSVPrinter(writer, format);
        printer.print("Test", 0, 3);
        printer.close();
        String result = writer.toString();
        String expected = "Test";
        assertEquals(expected, result);

        printer = new CSVPrinter(writer, format);
        printer.print(new Integer(123), 0, 3);
        printer.close();
        result = writer.toString();
        expected = "123";
        assertEquals(expected, result);

        printer = new CSVPrinter(writer, format);
        printer.print("Test\"Test", 0, 5);
        printer.close();
        result = writer.toString();
        expected = "\"Test\"Test";
        assertEquals(expected, result);

        printer = new CSVPrinter(writer, format);
        printer.print("Test\"Test\"Test", 0, 7);
        printer.close();
        result = writer.toString();
        expected = "\"Test\"\"Test\"Test";
        assertEquals(expected, result);

        printer = new CSVPrinter(writer, format);
        printer.print("Test\"Test\"Test\"", 0, 9);
        printer.close();
        result = writer.toString();
        expected = "\"Test\"\"Test\"\"";
        assertEquals(expected, result);

        printer = new CSVPrinter(writer, format);
        printer.print("Test" + COMMENT, 0, 4);
        printer.close();
        result = writer.toString();
        expected = "Test" + COMMENT;
        assertEquals(expected, result);

        // Test exception for invalid offset and length
        printer = new CSVPrinter(writer, format);
        assertThrows(IndexOutOfBoundsException.class, () -> printer.print("Test", 5, 3));

        // Test exception for null Object
        printer = new CSVPrinter(writer, format);
        assertThrows(NullPointerException.class, () -> printer.print(null, 0, 3));
    }

    /**
     * Test method for {@link CSVPrinter#printComment(String)}.
     */
    @org.junit.Test
    public void testPrintComment() throws IOException {
        StringWriter writer = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT.withCommentingEnabled(true);
        CSVPrinter printer = new CSVPrinter(writer, format);
        printer.printComment("Test");
        printer.close();
        String result = writer.toString();
        String expected = format.getCommentStart() + " Test";
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CSVPrinter#println()}.
     */
    @org.junit.Test
    public void testPrintln() throws IOException {
        StringWriter writer = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(writer, format);
        printer.println();
        printer.close();
        String result = writer.toString();
        String expected = CSVFormat.DEFAULT.getRecordSeparator();
        assertEquals(expected, result);

        printer = new CSVPrinter(writer, format);
        printer.println("Test");
        printer.close();
        result = writer.toString();
        expected = CSVFormat.DEFAULT.getRecordSeparator() + "Test";
        assertEquals(expected, result);

        printer = new CSVPrinter(writer, format);
        printer.println("Test" + COMMENT);
        printer.close();
        result = writer.toString();
        expected = CSVFormat.DEFAULT.getRecordSeparator() + "Test" + COMMENT;
        assertEquals(expected, result);
    }

    /**
     * Test method for {@link CSVPrinter#printRecord(Iterable)}.
     */
    @org.junit.Test
    public void testPrintRecordIterable() throws IOException {
        StringWriter writer = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(writer, format);
        Object[] values = new Object[] {"Test1", "Test2", null, "Test3"};
        printer.printRecord(values);
        printer.close();
        String result = writer.toString();
        String expected = "Test1,Test2,null,Test3";
        assertEquals(expected, result);

        // Test exception for null Iterable
        printer = new CSVPrinter(writer, format);
        assertThrows(NullPointerException.class, () -> printer.printRecord(null));
    }

    /**
     * Test method for {@link CSVPrinter#printRecord(Object[])}.
     */
    @org.junit.Test
    public void testPrintRecordObjectArray() throws IOException {
        StringWriter writer = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(writer, format);
        Object[] values = new Object[] {"Test1", "Test2", null, "Test3"};
        printer.printRecord(values);
        printer.close();
        String result = writer.toString();
        String expected = "Test1,Test2,null,Test3";
        assertEquals(expected, result);

        // Test exception for null Object array
        printer = new CSVPrinter(writer, format);
        assertThrows(NullPointerException.class, () -> printer.printRecord(null));
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(Iterable)}.
     */
    @org.junit.Test
    public void testPrintRecordsIterable() throws IOException, SQLException {
        StringWriter writer = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(writer, format);
        Object[][] values = new Object[][] {{"Test1", "Test2"}, {"Test3", "Test4"}, {"Test5", "Test6"}};
        printer.printRecords(values);
        printer.close();
        String result = writer.toString();
        String expected = "Test1,Test2\nTest3,Test4\nTest5,Test6";
        assertEquals(expected, result);

        // Test exception for null Iterable
        printer = new CSVPrinter(writer, format);
        assertThrows(NullPointerException.class, () -> printer.printRecords(null));
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(Object[])}.
     */
    @org.junit.Test
    public void testPrintRecordsObjectArray() throws IOException, SQLException {
        StringWriter writer = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(writer, format);
        Object[][] values = new Object[][] {{"Test1", "Test2"}, {"Test3", "Test4"}, {"Test5", "Test6"}};
        printer.printRecords(values);
        printer.close();
        String result = writer.toString();
        String expected = "Test1,Test2\nTest3,Test4\nTest5,Test6";
        assertEquals(expected, result);

        // Test exception for null Object array
        printer = new CSVPrinter(writer, format);
        assertThrows(NullPointerException.class, () -> printer.printRecords(null));
    }

    /**
     * Test method for {@link CSVPrinter#printRecords(ResultSet)}.
     */
    @org.junit.Test
    public void testPrintRecordsResultSet() throws IOException, SQLException {
        StringWriter writer = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(writer, format);
        String[][] expected = new String[][] {{"Test1", "Test2"}, {"Test3", "Test4"}, {"Test5", "Test6"}};
        ResultSet resultSet = TestHelper.getTestResultSet();
        printer.printRecords(resultSet);
        printer.close();
        String result = writer.toString();
        for (int i = 0; i < expected.length; i++) {
            String[] record = expected[i];
            CSVRecord parsedRecord = printer.getOut().toString().split(System.lineSeparator())[i].split(format.getRecordSeparator());
            for (int j = 0; j < record.length; j++) {
                assertEquals(record[j], parsedRecord[j]);
            }
        }
    }

    /**
     * Test method for {@link CSVPrinter#getOut()}.
     */
    @org.junit.Test
    public void testGetOut() throws IOException {
        StringWriter writer = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(writer, format);
        printer.printRecord(new Object[] {"Test1", "Test2"});
        printer.close();
        String result = printer.getOut().toString();
        String expected = "Test1,Test2";
        assertEquals(expected, result);
    }
}