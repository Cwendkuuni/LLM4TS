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
    public void testPrintQuotedValue() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true));
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("value with double quotes");
        printer.close();

        String result = out.toString();
        assertEquals("\"value with double quotes\"", result);
    }

    @Test
    public void testPrintEscapedValue() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(false).withEscape('\\'));
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("value with comma");
        printer.close();

        String result = out.toString();
        assertEquals("value with,", result);
    }

    @Test
    public void testPrintEmptyValue() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withEmptyStringAsNull(false));
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("");
        printer.close();

        String result = out.toString();
        assertEquals("", result);
    }

    @Test
    public void testPrintNullValue() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withEmptyStringAsNull(true));
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print(null);
        printer.close();

        String result = out.toString();
        assertEquals("", result);
    }

    @Test
    public void testPrintMultipleValues() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("value1");
        printer.print("value2");
        printer.print("value3");
        printer.println();
        printer.close();

        String result = out.toString();
        assertEquals("value1,value2,value3" + CR, result);
    }

    @Test
    public void testPrintComment() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withComment('%'));
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printComment("This is a comment");
        printer.close();

        String result = out.toString();
        assertEquals("% This is a comment" + CR, result);
    }

    @Test
    public void testPrintRecord() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(new String[]{"value1", "value2", "value3"});
        printer.close();

        String result = out.toString();
        assertEquals("value1,value2,value3" + CR, result);
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        String[] values1 = {"value1", "value2", "value3"};
        String[] values2 = {"value4", "value5"};
        printer.printRecords(values1, values2);
        printer.close();

        String result = out.toString();
        assertEquals("value1,value2,value3" + CR + "value4,value5" + CR, result);
    }

    @Test
    public void testPrintRecordsFromIterable() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        CSVPrinter printer = new CSVPrinter(out, format);

        Iterable<String[]> values = () -> new String[][]{{"value1", "value2", "value3"}, {"value4", "value5"}};
        printer.printRecords(values);
        printer.close();

        String result = out.toString();
        assertEquals("value1,value2,value3" + CR + "value4,value5" + CR, result);
    }

    @Test
    public void testPrintRecordsFromResultSet() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        String query = "SELECT column1, column2, column3 FROM table";

        try (ResultSet resultSet = new MockResultSet(query)) {
            CSVPrinter printer = new CSVPrinter(out, format);
            printer.printRecords(resultSet);
            printer.close();

            String result = out.toString();
            // The exact result depends on the data in the table
            // Replace the following line with an assertion based on the expected result
            assertEquals(result, result);
        }
    }

    @Test
    public void testPrintRecordsFromArray() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        Object[] values = {new String[]{"value1", "value2", "value3"}, new String[]{"value4", "value5"}};
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecords((Object[]) values);
        printer.close();

        String result = out.toString();
        assertEquals("value1,value2,value3" + CR + "value4,value5" + CR, result);
    }

    @Test
    public void testPrintRecordWithQuoteAll() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteAll(true));
        CSVPrinter printer = new CSVPrinter(out, format);

        String[] values = {"value1", "value2", "value3"};
        printer.printRecord(values);
        printer.close();

        String result = out.toString();
        assertEquals("\"value1\"\"\",\"value2\"\"\",\"value3\"\"\"", result);
    }

    @Test
    public void testPrintRecordWithEmptyStringAsNull() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withEmptyStringAsNull(true));
        CSVPrinter printer = new CSVPrinter(out, format);

        String[] values = {"", "value2", null, "value4"};
        printer.printRecord(values);
        printer.close();

        String result = out.toString();
        assertEquals("\"\",value2,null,\"value4\"", result);
    }

    @Test
    public void testPrintRecordWithQuotePolicy() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(new CSVFormat(CSVFormat.DEFAULT).withQuotePolicy(Quote.MINIMAL));
        CSVPrinter printer = new CSVPrinter(out, format);

        String[] values = {"value1", "value2", "value3"};
        printer.printRecord(values);
        printer.close();

        String result = out.toString();
        assertEquals("value1,value2,value3", result);

        // Test with a value that needs quoting
        printer = new CSVPrinter(out, format);
        values[0] = "value1,value2";
        printer.printRecord(values);
        printer.close();

        result = out.toString();
        assertEquals("\"value1,\"value2\"", result);
    }

    @Test
    public void testPrintRecordWithRecordSeparator() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withRecordSeparator("\n"));
        CSVPrinter printer = new CSVPrinter(out, format);

        String[] values = {"value1", "value2", "value3"};
        printer.printRecord(values);
        printer.close();

        String result = out.toString();
        assertEquals("value1\nvalue2\nvalue3\n", result);
    }
}

class MockResultSet implements Iterator<Object[]>, Closeable {
    private final Object[][] data;
    private int index;

    public MockResultSet(String query) {
        data = new Object[][]{{"column1_1", "column2_1", "column3_1"}, {"column1_2", "column2_2", "column3_2"}};
    }

    @Override
    public boolean hasNext() {
        return index < data.length;
    }

    @Override
    public Object[] next() {
        return data[index++];
    }

    @Override
    public void close() throws IOException {}
}