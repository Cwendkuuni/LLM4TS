package org.apache.commons.csv;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;

import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testCSVPrinter() {
        CSVFormat format = new CSVFormatBuilder().setDelimiter(';').build();
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), format);
        assertEquals("ByteArrayOutputStream", printer.getOut().getClass().getName());
        assertNull(printer.format);
        printer.close();
    }

    @Test
    public void testPrint() throws IOException {
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), format);

        // test null value
        printer.print(null);
        String result = printer.getOut().toString();
        assertEquals("", result);

        // test String value
        printer.print("test");
        result = printer.getOut().toString();
        assertEquals("test", result);

        // test Number value
        printer.print(123);
        result = printer.getOut().toString();
        assertEquals("123", result);

        // test Object value
        String objValue = new String("test");
        printer.print(objValue);
        result = printer.getOut().toString();
        assertEquals("test", result);

        // test multiple values
        printer.print("test1");
        printer.print("test2");
        printer.print(123);
        result = printer.getOut().toString();
        assertEquals("test1,test2,123", result);

        printer.close();
    }

    @Test
    public void testPrintAndQuote() throws IOException {
        CSVFormat format = new CSVFormatBuilder().setQuotePolicy(Quote.ALL).build();
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), format);

        // test String value
        printer.print("test");
        String result = printer.getOut().toString();
        assertEquals("\"test\"", result);

        // test Number value
        printer.print(123);
        result = printer.getOut().toString();
        assertEquals("123", result);

        // test Object value
        String objValue = new String("test");
        printer.print(objValue);
        result = printer.getOut().toString();
        assertEquals("\"test\"", result);

        // test multiple values
        printer.print("test1");
        printer.print("test2");
        printer.print(123);
        result = printer.getOut().toString();
        assertEquals("\"test1\",\"test2\",123", result);

        printer.close();
    }

    @Test
    public void testPrintAndEscape() throws IOException {
        CSVFormat format = new CSVFormatBuilder().setEscapingEnabled(true).build();
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), format);

        // test String value with CR character
        printer.print("test\r");
        String result = printer.getOut().toString();
        assertEquals("test\\r", result);

        // test String value with LF character
        printer.print("test\n");
        result = printer.getOut().toString();
        assertEquals("test\\n", result);

        // test String value with delimiter character
        printer.print("test;");
        result = printer.getOut().toString();
        assertEquals("test\\;", result);

        // test String value with escape character
        printer.print("test\\");
        result = printer.getOut().toString();
        assertEquals("test\\\\", result);

        // test multiple values
        printer.print("test1");
        printer.print("test2");
        printer.print("test3\r");
        printer.print("test4\n");
        printer.print("test5;");
        printer.print("test6\\");
        result = printer.getOut().toString();
        assertEquals("test1,test2,test3\\r,test4\\n,test5\\;,test6\\\\", result);

        printer.close();
    }

    @Test
    public void testPrintComment() throws IOException {
        CSVFormat format = new CSVFormatBuilder().setCommentingEnabled(true).build();
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), format);

        // test empty comment
        printer.printComment("");
        String result = printer.getOut().toString();
        assertEquals("", result);

        // test comment with CR character
        printer.printComment("\r");
        result = printer.getOut().toString();
        assertEquals("\r", result);

        // test comment with LF character
        printer.printComment("\n");
        result = printer.getOut().toString();
        assertEquals("\n", result);

        // test comment with text
        printer.printComment("test");
        result = printer.getOut().toString();
        assertEquals("#test\r\n", result);

        // test multiple records with comments
        printer.println();
        printer.printRecord(new String[]{"test11", "test12", "test13"});
        printer.printComment("test comment1");
        printer.println();
        printer.printRecord(new String[]{"test21", "test22", "test23"});
        printer.printComment("test comment2");
        result = printer.getOut().toString();
        assertEquals("#test comment1\r\n"
                + "test11,test12,test13\r\n"
                + "#test comment2\r\n"
                + "test21,test22,test23\r\n", result);

        printer.close();
    }

    @Test
    public void testPrintRecord() throws IOException, SQLException {
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), format);

        // test print record with single String value
        printer.printRecord(new String[]{"test"});
        String result = printer.getOut().toString();
        assertEquals("test", result);

        // test print record with multiple String values
        printer.printRecord(new String[]{"test1", "test2", "test3"});
        result = printer.getOut().toString();
        assertEquals("test1,test2,test3", result);

        // test print record with multiple Object values
        String objValue1 = new String("test1");
        String objValue2 = new String("test2");
        String objValue3 = new String("test3");
        printer.printRecord(new Object[]{objValue1, objValue2, objValue3});
        result = printer.getOut().toString();
        assertEquals(objValue1 + "," + objValue2 + "," + objValue3, result);

        // test print record with multiple Iterable values
        Iterable<String> iterableValue1 = () -> new String[]{"test1_1", "test1_2"};
        Iterable<String> iterableValue2 = () -> new String[]{"test2_1", "test2_2"};
        printer.printRecord(iterableValue1, iterableValue2);
        result = printer.getOut().toString();
        assertEquals(iterableValue1.toString() + "," + iterableValue2.toString(), result);

        // test print record with multiple Object[] values
        Object[] objectArrayValue1 = new Object[]{"test1_1", "test1_2"};
        Object[] objectArrayValue2 = new Object[]{"test2_1", "test2_2"};
        printer.printRecord(objectArrayValue1, objectArrayValue2);
        result = printer.getOut().toString();
        assertEquals(Arrays.deepToString(objectArrayValue1) + "," + Arrays.deepToString(objectArrayValue2), result);

        // test print record with multiple ResultSet values
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString(1)).thenReturn("test1_1");
        when(resultSet.getString(2)).thenReturn("test1_2");
        when(resultSet.getString(3)).thenReturn("test1_3");
        printer.printRecords(resultSet);
        result = printer.getOut().toString();
        assertEquals("test1_1,test1_2,test1_3\r\n", result);

        printer.close();
    }
}