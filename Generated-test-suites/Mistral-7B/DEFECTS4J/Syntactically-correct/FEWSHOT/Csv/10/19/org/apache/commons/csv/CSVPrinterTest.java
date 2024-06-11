package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testPrintAndQuote() throws IOException, SQLException {
        CharSequence value = "this is a test";

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormatBuilder().setQuoteChar('"').build();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("field1", value, 0, value.length());
        printer.close();

        String result = out.toString();
        assertEquals("\"this is a test\"", result);

        value = "\"this is a test\"";
        printer = new CSVPrinter(out, format);
        printer.print("field1", value, 0, value.length());
        printer.close();

        result = out.toString();
        assertEquals("\"\\\"this is a test\\\"\"", result);

        printer = new CSVPrinter(out, format);
        printer.print("field1", null);
        printer.close();

        result = out.toString();
        assertTrue(format.getNullString().equals(result) || result.isEmpty());

        // Test the case where quoting is not enabled
        format = new CSVFormatBuilder().build();
        printer = new CSVPrinter(out, format);
        printer.print("field1", value, 0, value.length());
        printer.close();

        result = out.toString();
        assertEquals("this is a test", result);
    }

    @Test
    public void testPrintAndEscape() throws IOException {
        CharSequence value = "this is a test with CR, LF and \t";

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormatBuilder().setEscapeCharacter('\\').build();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("field1", value, 0, value.length());
        printer.close();

        String result = out.toString();
        assertEquals("this is a test with CR, LF and \t", result);

        result = out.toString();
        assertFalse(result.contains("CR"));
        assertFalse(result.contains("LF"));
        assertFalse(result.contains("\t"));

        result = out.toString();
        assertEquals("this is a test with CR,\\nLF,\\t", result);

        // Test the case where escaping is not enabled
        format = new CSVFormatBuilder().build();
        printer = new CSVPrinter(out, format);
        printer.print("field1", value, 0, value.length());
        printer.close();

        result = out.toString();
        assertEquals("this is a test with CR, LF and \t", result);
    }

    @Test
    public void testPrintComment() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormatBuilder().setComment("--")
                .build();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printComment("This is a comment");
        printer.println();
        printer.println();
        printer.printComment("Another comment");
        printer.println();
        printer.close();

        String result = out.toString();
        assertEquals("--This is a comment\r\n--Another comment\r\n", result);
    }

    @Test
    public void testPrintln() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.println();
        printer.close();

        String result = out.toString();
        assertTrue(result.isEmpty());

        printer = new CSVPrinter(out, format);
        printer.println();
        printer.println();
        printer.close();

        result = out.toString();
        assertEquals(format.getRecordSeparator(), result);
    }

    @Test
    public void testPrintRecord() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(new String[]{"field1", "field2"});
        printer.close();

        String result = out.toString();
        assertEquals(format.getRecordSeparator(), result.substring(0, format.getRecordSeparator().length()));
        assertEquals("field1,field2", result.substring(format.getRecordSeparator().length()));

        printer = new CSVPrinter(out, format);
        Object[] record = new Object[]{"field1", "field2"};
        printer.printRecord(record);
        printer.close();

        result = out.toString();
        assertEquals(format.getRecordSeparator(), result.substring(0, format.getRecordSeparator().length()));
        assertEquals("field1,field2", result.substring(format.getRecordSeparator().length()));

        printer = new CSVPrinter(out, format);
        Iterable<Object> recordIterable = new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return new Iterator<Object>() {
                    private int index = 0;

                    @Override
                    public boolean hasNext() {
                        return index < 2;
                    }

                    @Override
                    public Object next() {
                        return "field" + (index + 1);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
        printer.printRecords(recordIterable);
        printer.close();

        result = out.toString();
        assertEquals(format.getRecordSeparator(), result.substring(0, format.getRecordSeparator().length()));
        assertEquals("field1,field2", result.substring(format.getRecordSeparator().length()));
        assertEquals(format.getRecordSeparator(), result.substring(result.length() - format.getRecordSeparator().length()));

        result = out.toString();
        assertEquals(format.getRecordSeparator(), result.substring(0, format.getRecordSeparator().length()));
        assertEquals("field1,field2", result.substring(format.getRecordSeparator().length()));
        assertEquals(format.getRecordSeparator(), result.substring(result.length() - format.getRecordSeparator().length()));

        printer = new CSVPrinter(out, format);
        printer.printComment("Comment");
        printer.printRecord(new String[]{"field1", "field2"});
        printer.close();

        result = out.toString();
        assertEquals("--Comment\r\n", result.substring(0, result.indexOf("\r\n")));
        assertEquals("field1,field2", result.substring(result.indexOf("\r\n") + format.getRecordSeparator().length()));
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormatBuilder().build();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecords(new Object[][]{{"field1", "field2"}, {"field3", "field4"}});
        printer.close();

        String result = out.toString();
        assertEquals(format.getRecordSeparator(), result.substring(0, format.getRecordSeparator().length()));
        assertEquals("field1,field2", result.substring(format.getRecordSeparator().length(), result.lastIndexOf(format.getRecordSeparator())));
        assertEquals("field3,field4", result.substring(result.lastIndexOf(format.getRecordSeparator()) + format.getRecordSeparator().length()));

        printer = new CSVPrinter(out, format);
        printer.printComment("Comment");
        printer.printRecords(new Object[][]{{"field1", "field2"}, {"field3", "field4"}});
        printer.close();

        result = out.toString();
        assertEquals("--Comment\r\n", result.substring(0, result.indexOf("\r\n")));
        assertEquals(format.getRecordSeparator(), result.substring(result.indexOf("\r\n") + format.getRecordSeparator().length()));
        assertEquals("field1,field2", result.substring(format.getRecordSeparator().length(), result.lastIndexOf(format.getRecordSeparator())));
        assertEquals("field3,field4", result.substring(result.lastIndexOf(format.getRecordSeparator()) + format.getRecordSeparator().length()));

        ResultSet resultSet = null; // need to mock ResultSet to test this
        printer = new CSVPrinter(out, format);
        printer.printRecords(resultSet);
        printer.close();

        result = out.toString();
        assertTrue(result.isEmpty());
    }

}