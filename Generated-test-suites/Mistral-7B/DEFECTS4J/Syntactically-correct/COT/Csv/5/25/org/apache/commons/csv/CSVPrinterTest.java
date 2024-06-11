package org.apache.commons.csv;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/**
 * Tests for the CSVPrinter class.
 */
@RunWith(JUnitParamsRunner.class)
public class CSVPrinterTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat();
        CSVPrinter printer = new CSVPrinter(out, format);
        assertNotNull(printer);
    }

    /**
     * Test print method with basic functionality.
     */
    @Test
    public void testPrintBasic() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("Hello");
        printer.flush();
        assertEquals("Hello", out.toString());
    }

    /**
     * Test print method with quoting.
     */
    @Test
    public void testPrintQuoting() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("Hello\" World");
        printer.print("Hello\" \"World\"");
        printer.flush();

        CSVRecord record = new CSVRecord(format, out.toString());
        assertEquals("Hello\"", record.get(0));
        assertEquals("Hello\" \"World\"", record.get(1));
    }

    /**
     * Test print method with escaping.
     */
    @Test
    public void testPrintEscaping() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(',', Quote.NONE, '\'');
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("Hello, World");
        printer.print("Hello,\"World\"");
        printer.print("Hello,\"\"World\"");
        printer.print("Hello,\"Hello,\"World\"");
        printer.print("Hello,\"Hello,\"\"World\"");
        printer.print("Hello,\"Hello,\"\"\"World\"");
        printer.flush();

        CSVRecord record = new CSVRecord(format, out.toString());
        assertEquals("Hello, World", record.get(0));
        assertEquals("Hello,\"World\"", record.get(1));
        assertEquals("Hello,\"\"World\"", record.get(2));
        assertEquals("Hello,\"Hello,\"World\"", record.get(3));
        assertEquals("Hello,\"Hello,\"\"World\"", record.get(4));
        assertEquals("Hello,\"Hello,\"\"\"World\"", record.get(5));
    }

    /**
     * Test print method with comments.
     */
    @Test
    public void testPrintComment() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printComment("This is a comment");
        printer.flush();

        assertEquals("\"#This is a comment\"\r\n", out.toString());
    }

    /**
     * Test print method with print empty record.
     */
    @Test
    public void testPrintEmptyRecord() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat();
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.printRecord(new Object[0]);
        printer.flush();

        assertEquals("", out.toString());
    }

    /**
     * Test print method with print record with single object.
     */
    @Test
    public void testPrintRecordSingleObject() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(new String[]{"Hello"});
        printer.flush();

        assertEquals("Hello", out.toString());
    }

    /**
     * Test print method with print record with multiple objects.
     */
    @Test
    public void testPrintRecordMultipleObjects() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',');
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(new String[]{"Hello", "World"});
        printer.flush();

        assertEquals("Hello, World", out.toString());
    }

    /**
     * Test print method with print records.
     */
    @Test
    public void testPrintRecords() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',');
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecords(new String[][]{{"Hello", "World"}, {"Hello1", "World1"}});
        printer.flush();

        CSVRecord record = new CSVRecord(format, out.toString());
        assertEquals("Hello, World", record.get(0));
        assertEquals("Hello1, World1", record.get(1));
    }

    /**
     * Test print method with print records from an Iterable.
     */
    @Test
    public void testPrintRecordsIterable() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',');
        CSVPrinter printer = new CSVPrinter(out, format);

        Iterable<String[]> iterable = () -> new String[][]{{"Hello", "World"}, {"Hello1", "World1"}};
        printer.printRecords(iterable);
        printer.flush();

        CSVRecord record = new CSVRecord(format, out.toString());
        assertEquals("Hello, World", record.get(0));
        assertEquals("Hello1, World1", record.get(1));
    }

    /**
     * Test print method with print records from an Array.
     */
    @Test
    public void testPrintRecordsArray() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',');
        CSVPrinter printer = new CSVPrinter(out, format);

        String[][] records = new String[][]{{"Hello", "World"}, {"Hello1", "World1"}};
        printer.printRecords(records);
        printer.flush();

        CSVRecord record = new CSVRecord(format, out.toString());
        assertEquals("Hello, World", record.get(0));
        assertEquals("Hello1, World1", record.get(1));
    }

    /**
     * Test print method with print records from a ResultSet.
     */
    @Test
    public void testPrintRecordsResultSet() throws IOException, SQLException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(',');
        CSVPrinter printer = new CSVPrinter(out, format);

        String sql = "SELECT 'Hello', 'World' FROM test";
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getString(1)).thenReturn("Hello");
        Mockito.when(resultSet.getString(2)).thenReturn("World");

        printer.printRecords(resultSet);
        printer.flush();

        CSVRecord record = new CSVRecord(format, out.toString());
        assertEquals("Hello, World", record.get(0));

        Mockito.verify(resultSet).close();
    }

    /**
     * Test print method with print record with null value.
     */
    @Test
    public void testPrintRecordNullValue() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(new String[]{"", "Hello"});
        printer.flush();

        CSVRecord record = new CSVRecord(format, out.toString());
        assertEquals("\"\",Hello", record.get(0));
        assertEquals("Hello", record.get(1));
    }

    /**
     * Test print method with print record with empty string value.
     */
    @Test
    public void testPrintRecordEmptyStringValue() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(new String[]{"", "Hello"});
        printer.flush();

        CSVRecord record = new CSVRecord(format, out.toString());
        assertEquals("\"\",Hello", record.get(0));
        assertEquals("Hello", record.get(1));
    }

    /**
     * Test print method with print record with empty record.
     */
    @Test
    public void testPrintRecordEmptyRecord() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = new CSVFormat(',');
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord(new String[0]);
        printer.flush();

        assertEquals("", out.toString());
    }

    /**
     * Test print method with exception when no CSVFormat is given.
     */
    @Test
    public void testPrintExceptionNoCSVFormat() {
        assertThrows(IllegalArgumentException.class, () -> new CSVPrinter(new ByteArrayOutputStream(), null));
    }

    /**
     * Test print method with exception when no output stream is given.
     */
    @Test
    public void testPrintExceptionNoOutput() {
        assertThrows(IllegalArgumentException.class, () -> new CSVPrinter(null, new CSVFormat()));
    }

    /**
     * Test flush method with basic functionality.
     */
    @Test
    public void testFlush() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.flush();
        assertEquals("", out.toString());

        printer.print("Hello");
        printer.flush();
        assertEquals("Hello", out.toString());
    }

    /**
     * Test close method with basic functionality.
     */
    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.close();
        assertEquals("", out.toString());
    }

    /**
     * Test print method with exception when the output stream is closed.
     */
    @Test(expected = IOException.class)
    public void testPrintExceptionClosedStream() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.close();
        printer.print("Hello");
    }

    /**
     * Test print method with exception when the format is inconsistent.
     */
    @Parameters(method = "inconsistentFormatData")
    @Test(expected = IllegalArgumentException.class)
    public void testPrintExceptionInconsistentFormat(String delimiter, String delimiter2) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(delimiter);
        format.setDelimiter(delimiter2);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("Hello");
    }

    private Object[] inconsistentFormatData() {
        return new Object[][]{
                new Object[]{',', ';'},
                new Object[]{';', ','},
                new Object[]{' ', ','},
                new Object[]{' ', ';'},
                new Object[]{null, ','},
                new Object[]{'', ','},
                new Object[]{',', null},
                new Object[]{',', ''},
                new Object[]{',', '\t'},
                new Object[]{',', '\r'},
                new Object[]{',', '\n'},
                new Object[]{',', '\f'},
                new Object[]{',', '\b'},
                new Object[]{',', '\\'},
                new Object[]{',', '\''},
                new Object[]{',', '"'}
        };
    }

    /**
     * Test print method with exception when the output stream is not closeable.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testPrintExceptionNotCloseable() throws IOException {
        final byte[] bytes = new byte[1];
        CSVFormat format = new CSVFormat();
        CSVPrinter printer = new CSVPrinter(bytes, format);

        printer.close();
        printer.print("Hello");
    }

    /**
     * Test print method with exception when the output stream is not flushable.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testPrintExceptionNotFlushable() throws IOException {
        final StringWriter writer = new StringWriter();
        CSVFormat format = new CSVFormat();
        CSVPrinter printer = new CSVPrinter(writer, format);

        printer.flush();
        printer.print("Hello");
    }

    /**
     * Test print method with exception when the output stream has an error.
     */
    @Test(expected = IOException.class)
    public void testPrintExceptionStreamError() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new IOException("Write error");
            }
        };
        CSVFormat format = new CSVFormat();
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("Hello");
    }

    /**
     * Test print method with exception when the output stream does not support write.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testPrintExceptionNotSupportWrite() throws IOException {
        final StringWriter writer = new StringWriter() {
            @Override
            public void write(int c) throws IOException {
                throw new UnsupportedOperationException("Write operation not supported");
            }
        };
        CSVFormat format = new CSVFormat();
        CSVPrinter printer = new CSVPrinter(writer, format);

        printer.print("Hello");
    }
}