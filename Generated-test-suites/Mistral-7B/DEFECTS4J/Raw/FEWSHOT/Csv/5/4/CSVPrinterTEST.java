package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVRecordIterator;
import org.apache.commons.csv.Quote;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException, SQLException {
        // Given
        final CSVFormat format = new CSVFormatBuilder().setDelimiter(';').build();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Appendable out = new PrintStream(outputStream);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final String value1 = "value1";
        final String value2 = "value2";
        final Object object1 = new Object() {
            @Override
            public String toString() {
                return value1;
            }
        };
        final Object object2 = new Object() {
            @Override
            public String toString() {
                return value2;
            }
        };

        // When
        printer.print(value1);
        printer.print(value2);
        printer.println();

        // Then
        assertEquals("value1;value2\n", outputStream.toString());

        // Given
        printer.reset();
        printer.print(object1);
        printer.print(object2);
        printer.println();

        // Then
        assertEquals("value1;value2\n", outputStream.toString());

        // Given
        final ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString(1)).thenReturn(value1);
        when(resultSet.getString(2)).thenReturn(value2);

        // When
        printer.printRecords(resultSet);

        // Then
        assertEquals("value1;value2\nvalue1;value2\n", outputStream.toString());

        // Given
        printer.reset();
        final Iterable<Object> iterable = new Object[] { object1, object2 };

        // When
        printer.printRecords(iterable);

        // Then
        assertEquals("value1;value2\nvalue1;value2\n", outputStream.toString());
    }

    @Test
    public void testPrintQuoting() throws IOException {
        // Given
        final CSVFormat format = new CSVFormatBuilder().setQuotePolicy(Quote.ALL).build();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Appendable out = new PrintStream(outputStream);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final String value = "value\"with quote\"";

        // When
        printer.print(value);
        printer.println();

        // Then
        assertEquals("\"value\"with quote\"\n", outputStream.toString());
    }

    @Test
    public void testPrintEscaping() throws IOException {
        // Given
        final CSVFormat format = new CSVFormatBuilder().setEscaping(true).build();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Appendable out = new PrintStream(outputStream);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final String value = "value\\with escape\\";

        // When
        printer.print(value);
        printer.println();

        // Then
        assertEquals("value\\with escape\\\n", outputStream.toString());
    }

    @Test
    public void testPrintQuotingAndEscaping() throws IOException {
        // Given
        final CSVFormat format = new CSVFormatBuilder().setQuotePolicy(Quote.ALL).setEscaping(true).build();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Appendable out = new PrintStream(outputStream);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final String value = "value\"with quote\"\\with escape\\";

        // When
        printer.print(value);
        printer.println();

        // Then
        assertEquals("\"value\"\"with quote\"\\with escape\\\n", outputStream.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        // Given
        final CSVFormat format = new CSVFormatBuilder().setCommentingEnabled(true).build();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Appendable out = new PrintStream(outputStream);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final String comment = "comment";

        // When
        printer.printComment(comment);

        // Then
        assertEquals("# comment\n", outputStream.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        // Given
        final CSVFormat format = new CSVFormatBuilder().build();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Appendable out = new PrintStream(outputStream);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final Object[] record = new Object[] { "value1", "value2" };

        // When
        printer.printRecord(record);

        // Then
        assertEquals("value1;value2\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordWithNullValue() throws IOException {
        // Given
        final CSVFormat format = new CSVFormatBuilder().build();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Appendable out = new PrintStream(outputStream);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final Object[] record = new Object[] { null, "value2" };

        // When
        printer.printRecord(record);

        // Then
        assertEquals(";value2\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordWithEmptyValue() throws IOException {
        // Given
        final CSVFormat format = new CSVFormatBuilder().build();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Appendable out = new PrintStream(outputStream);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final Object[] record = new Object[] { "", "value2" };

        // When
        printer.printRecord(record);

        // Then
        assertEquals(";value2\n", outputStream.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        // Given
        final CSVFormat format = new CSVFormatBuilder().build();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Appendable out = new PrintStream(outputStream);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final Object[][] records = new Object[][] { { "value1", "value2" }, { "value3", "value4" } };

        // When
        printer.printRecords(records);

        // Then
        assertEquals("value1;value2\nvalue3;value4\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordsWithNullValue() throws IOException {
        // Given
        final CSVFormat format = new CSVFormatBuilder().build();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Appendable out = new PrintStream(outputStream);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final Object[][] records = new Object[][] { { null, "value2" }, { "value3", null } };

        // When
        printer.printRecords(records);

        // Then
        assertEquals(";value2\nvalue3;;\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordsWithEmptyValue() throws IOException {
        // Given
        final CSVFormat format = new CSVFormatBuilder().build();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Appendable out = new PrintStream(outputStream);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final Object[][] records = new Object[][] { { "", "value2" }, { "value3", "" } };

        // When
        printer.printRecords(records);

        // Then
        assertEquals(";value2\nvalue3;;\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordsWithMixedTypes() throws IOException {
        // Given
        final CSVFormat format = new CSVFormatBuilder().build();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Appendable out = new PrintStream(outputStream);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final Object[][] records = new Object[][] { { 1, "value2" }, { "value3", 3 } };

        // When
        printer.printRecords(records);

        // Then
        assertEquals("1;value2\nvalue3;3\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordsWithEmptyIterator() throws IOException {
        // Given
        final CSVFormat format = new CSVFormatBuilder().build();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Appendable out = new PrintStream(outputStream);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final Iterable<Object[]> iterator = new Iterator<Object[]>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object[] next() {
                return null;
            }
        };

        // When
        printer.printRecords(iterator);

        // Then
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testPrintRecordsWithNullIterator() throws IOException {
        // Given
        final CSVFormat format = new CSVFormatBuilder().build();
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final Appendable out = new PrintStream(outputStream);
        final CSVPrinter printer = new CSVPrinter(out, format);

        final Iterable<Object[]> iterator = null;

        // When
        printer.printRecords(iterator);

        // Then
        assertNull(outputStream.toString());
    }
}