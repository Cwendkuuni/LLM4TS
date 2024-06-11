package org.apache.commons.csv;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    private ByteArrayOutputStream outputStream;
    private CSVFormat format;

    @Before
    public void setUp() throws IOException {
        format = new CSVFormat(';', Quote.MINIMAL, null);
        outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(outputStream, format);
    }

    @Test
    public void testClose() throws IOException {
        printer.close();
    }

    @Test
    public void testFlush() throws IOException {
        printer.flush();
    }

    @Test
    public void testPrint() throws IOException {
        Object[] values = { "foo", 1, "bar" };
        printer.printRecord(values);
        CSVRecord record = new CSVRecord(format.getParser().parse(outputStream.toString()));
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i].toString(), record.get(i).toString());
        }
    }

    @Test
    public void testPrintQuotedValue() throws IOException {
        Object[] values = { "foo\"bar" };
        printer.printRecord(values);
        CSVRecord record = new CSVRecord(format.getParser().parse(outputStream.toString()));
        assertEquals("foo\"bar", record.get(0).toString());
    }

    @Test
    public void testPrintEscapedValue() throws IOException {
        Object[] values = { "foo\\tbar" };
        printer.printRecord(values);
        CSVRecord record = new CSVRecord(format.getParser().parse(outputStream.toString()));
        assertEquals("foo\\tbar", record.get(0).toString());
    }

    @Test
    public void testPrintEmptyValue() throws IOException {
        printer.print();
        CSVRecord record = new CSVRecord(format.getParser().parse(outputStream.toString()));
        assertNull(record.get(0));
    }

    @Test
    public void testPrintNullValue() throws IOException {
        printer.print(null);
        CSVRecord record = new CSVRecord(format.getParser().parse(outputStream.toString()));
        assertEquals(format.getNullString(), record.get(0).toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment.");
        String output = outputStream.toString();
        assertEquals(format.getCommentStart().charValue() + SP + "This is a comment." + CR + LF, output.substring(0, output.length() - 1));
    }

    @Test
    public void testPrintln() throws IOException {
        printer.println();
        String output = outputStream.toString();
        assertEquals(format.getRecordSeparator(), output.substring(output.length() - format.getRecordSeparator().length()));
    }

    @Test
    public void testPrintRecord() throws IOException {
        Object[] values = { null, "", "foo", "'bar'", "123", "123\t456" };
        printer.printRecord(values);
        CSVRecord record = new CSVRecord(format.getParser().parse(outputStream.toString()));
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                assertNull(record.get(i));
            } else {
                assertEquals(values[i].toString(), record.get(i).toString());
            }
        }
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(1)).thenReturn("foo");
        when(resultSet.getString(2)).thenReturn("bar");

        printer.printRecords(resultSet);
        CSVRecord record = new CSVRecord(format.getParser().parse(outputStream.toString()));
        assertEquals("foo", record.get(0).toString());
        assertEquals("bar", record.get(1).toString());

        assertThrows(SQLException.class, () -> printer.printRecords(mock(ResultSet.class)));
    }
}