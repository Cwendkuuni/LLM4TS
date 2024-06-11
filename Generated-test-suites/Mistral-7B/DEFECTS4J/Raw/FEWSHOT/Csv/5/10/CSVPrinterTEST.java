package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.apache.commons.csv.Constants;

/**
 * Test cases for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException {
        // given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        // when
        printer.print("Hello");

        // then
        assertEquals("Hello", outputStream.toString());
    }

    @Test
    public void testPrintQuoted() throws IOException {
        // given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        // when
        printer.print("\"Hello\"");

        // then
        assertEquals("\"Hello\"", outputStream.toString());
    }

    @Test
    public void testPrintEscaped() throws IOException {
        // given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        // when
        printer.print("Hello, World");

        // then
        assertEquals("Hello, World", outputStream.toString());

        // when
        printer.print("Hello, World\n");

        // then
        assertEquals("Hello, World\r\n", outputStream.toString());

        // when
        printer.print("Hello,\"World\"");

        // then
        assertEquals("Hello,\"World\"", outputStream.toString());

        // when
        printer.print("Hello,\"World\"\n");

        // then
        assertEquals("Hello,\"World\"\r\n", outputStream.toString());

        // when
        printer.print("Hello, \"World\"");

        // then
        assertEquals("Hello, \"World\"", outputStream.toString());

        // when
        printer.print("Hello, \"World\"\n");

        // then
        assertEquals("Hello, \"World\"\r\n", outputStream.toString());

        // when
        printer.print("Hello, World,\"");

        // then
        assertEquals("Hello, World,\"", outputStream.toString());

        // when
        printer.print("Hello, World,\"");

        // then
        assertEquals("Hello, World,\"", outputStream.toString());

        // when
        printer.print("Hello, World,\"");

        // then
        assertEquals("Hello, World,\"", outputStream.toString());

        // when
        printer.print("Hello, World,\"Hello\"");

        // then
        assertEquals("Hello, World,\"Hello\"", outputStream.toString());

        // when
        printer.print("Hello, World,\"Hello\"\n");

        // then
        assertEquals("Hello, World,\"Hello\"\r\n", outputStream.toString());

        // when
        printer.print("Hello, World,\"Hello\",");

        // then
        assertEquals("Hello, World,\"Hello\",", outputStream.toString());

        // when
        printer.print("Hello, World,\"Hello\",");

        // then
        assertEquals("Hello, World,\"Hello\",", outputStream.toString());

        // when
        printer.print("Hello, World,\"Hello\",");

        // then
        assertEquals("Hello, World,\"Hello\",", outputStream.toString());

        // when
        printer.print("Hello, World,\"Hello\",Hello");

        // then
        assertEquals("Hello, World,\"Hello\",Hello", outputStream.toString());

        // when
        printer.print("Hello, World,\"Hello\",Hello\n");

        // then
        assertEquals("Hello, World,\"Hello\",Hello\r\n", outputStream.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        // given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.MINIMAL).withComment("--COMMENT--");
        final CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        // when
        printer.printComment("Hello, World");

        // then
        assertEquals("--COMMENT--\r\nHello, World\r\n--COMMENT--\r\n", outputStream.toString());

        // when
        printer.printComment("\n");

        // then
        assertEquals("--COMMENT--\r\n\r\n--COMMENT--\r\n", outputStream.toString());

        // when
        printer.printComment("\r\n");

        // then
        assertEquals("--COMMENT--\r\n\r\n--COMMENT--\r\n", outputStream.toString());

        // when
        printer.printComment("--COMMENT--");

        // then
        assertEquals("--COMMENT--\r\n--COMMENT--\r\n--COMMENT--\r\n", outputStream.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        // given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        // when
        printer.printRecord(Arrays.asList("Hello", "World"));

        // then
        assertEquals("Hello,World\r\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordQuoted() throws IOException {
        // given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        // when
        printer.printRecord(Arrays.asList("\"Hello\"", "World"));

        // then
        assertEquals("\"Hello\",World\r\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordEscaped() throws IOException {
        // given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        // when
        printer.printRecord(Arrays.asList("Hello, World", "Hello, World\n"));

        // then
        assertEquals("Hello, World,Hello, World\r\n", outputStream.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        // given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        // when
        printer.printRecords(Arrays.asList(Arrays.asList("Hello", "World"), Arrays.asList("Hello2", "World2")));

        // then
        assertEquals("Hello,World\r\nHello2,World2\r\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordsQuoted() throws IOException {
        // given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        // when
        printer.printRecords(Arrays.asList(Arrays.asList("\"Hello\"", "World"), Arrays.asList("\"Hello2\"", "World2")));

        // then
        assertEquals("\"Hello\",World\r\n\"Hello2\",World2\r\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordsEscaped() throws IOException {
        // given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        // when
        printer.printRecords(Arrays.asList(Arrays.asList("Hello, World", "Hello, World\n"), Arrays.asList("Hello, World", "Hello, World\n")));

        // then
        assertEquals("Hello, World,Hello, World\r\nHello, World,Hello, World\r\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordsEmpty() throws IOException {
        // given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        // when
        printer.printRecords(Arrays.asList());

        // then
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testPrintRecordsNull() throws IOException {
        // given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        // when
        printer.printRecords(null);

        // then
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testPrintRecordsResultSet() throws IOException, SQLException {
        // given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        final ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString(1)).thenReturn("Hello");
        when(resultSet.getString(2)).thenReturn("World");

        // when
        printer.printRecords(resultSet);

        // then
        verify(resultSet, times(1)).next();
        assertEquals("Hello,World\r\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordsResultSetEmpty() throws IOException, SQLException {
        // given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        final ResultSet resultSet = mock(ResultSet.class);

        // when
        printer.printRecords(resultSet);

        // then
        verify(resultSet, times(1)).next();
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testPrintRecordsResultSetNull() throws IOException, SQLException {
        // given
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(',', Quote.MINIMAL);
        final CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        // when
        printer.printRecords(null);

        // then
        assertEquals("", outputStream.toString());
    }
}