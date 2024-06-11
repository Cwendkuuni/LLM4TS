package org.apache.commons.csv.test;

import org.apache.commons.csv.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

class CSVPrinterTest {

    @Test
    void testEmptyCSVPrinter() throws IOException, SQLException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final CSVFormat format = new CSVFormat(CSVFormat.DEFAULT);
        final CSVPrinter printer = new CSVPrinter(outputStream, format);

        printer.close();
        final String actualCSV = outputStream.toString();
        final String expectedCSV = "";
        Assertions.assertEquals(expectedCSV, actualCSV);
    }

    @Test
    void testPrintValues() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String delimiter = ",";
        final String recordSeparator = "\n";
        final CSVFormat format = new CSVFormat(new CSVFormat.Builder()
            .withDelimiter(delimiter)
            .withRecordSeparator(recordSeparator)
            .build());
        final CSVPrinter printer = new CSVPrinter(outputStream, format);

        printer.print(1);
        printer.print("test");
        printer.print(true);
        printer.close();

        final String actualCSV = outputStream.toString();
        final String expectedCSV = "1,\"test\",true\n";
        Assertions.assertEquals(expectedCSV, actualCSV);
    }

    @Test
    void testPrintValuesWithQuotes() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String delimiter = ",";
        final String recordSeparator = "\n";
        final CSVFormat format = new CSVFormat(new CSVFormat.Builder()
            .withDelimiter(delimiter)
            .withRecordSeparator(recordSeparator)
            .withQuote('"')
            .build());
        final CSVPrinter printer = new CSVPrinter(outputStream, format);

        printer.print("test, \"test with quotes\", test with quotes");
        printer.close();

        final String actualCSV = outputStream.toString();
        final String expectedCSV = "\"test\",\"test, \"test with quotes\", test with quotes\",\"test with quotes\"\n";
        Assertions.assertEquals(expectedCSV, actualCSV);
    }

    @Test
    void testPrintValuesWithEscapes() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String delimiter = ",";
        final String recordSeparator = "\n";
        final CSVFormat format = new CSVFormat(new CSVFormat.Builder()
            .withDelimiter(delimiter)
            .withRecordSeparator(recordSeparator)
            .withEscape('\\')
            .build());
        final CSVPrinter printer = new CSVPrinter(outputStream, format);

        printer.print("test, value, \"test, value\"");
        printer.close();

        final String actualCSV = outputStream.toString();
        final String expectedCSV = "test,\"value\",\"test,\" value \""
                + "\n";
        Assertions.assertEquals(expectedCSV, actualCSV);
    }

    @Test
    void testPrintValuesWithNulls() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String delimiter = ",";
        final String recordSeparator = "\n";
        final CSVFormat format = new CSVFormat(new CSVFormat.Builder()
            .withDelimiter(delimiter)
            .withRecordSeparator(recordSeparator)
            .build());
        final CSVPrinter printer = new CSVPrinter(outputStream, format);

        printer.print(null);
        printer.print("test");
        printer.print(null);
        printer.close();

        final String actualCSV = outputStream.toString();
        final String expectedCSV = ",\"test\",,\n";
        Assertions.assertEquals(expectedCSV, actualCSV);
    }

    @Test
    void testPrintValuesWithComments() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String delimiter = ",";
        final String recordSeparator = "\n";
        final String comment = "test comment";
        final CSVFormat format = new CSVFormat(new CSVFormat.Builder()
            .withDelimiter(delimiter)
            .withRecordSeparator(recordSeparator)
            .withCommentPrefix(Constants.COMMENT)
            .build());
        final CSVPrinter printer = new CSVPrinter(outputStream, format);

        printer.print("test");
        printer.printComment(comment);
        printer.print("another test");
        printer.close();

        final String actualCSV = outputStream.toString();
        final String expectedCSV = "test# test comment\nanother test\n";
        Assertions.assertEquals(expectedCSV, actualCSV);
    }

    @Test
    void testPrintRecords() throws IOException, SQLException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String delimiter = ",";
        final String recordSeparator = "\n";
        final String[] array = {"test1", "test2", "test3"};
        final CSVFormat format = new CSVFormat(new CSVFormat.Builder()
            .withDelimiter(delimiter)
            .withRecordSeparator(recordSeparator)
            .build());
        final CSVPrinter printer = new CSVPrinter(outputStream, format);

        printer.printRecord(array);
        printer.close();

        final String actualCSV = outputStream.toString();
        final String expectedCSV = "test1,test2,test3\n";
        Assertions.assertEquals(expectedCSV, actualCSV);
    }

    @Test
    void testPrintRecordsWithNulls() throws IOException, SQLException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String delimiter = ",";
        final String recordSeparator = "\n";
        final String[] array = {"test1", null, "test3"};
        final CSVFormat format = new CSVFormat(new CSVFormat.Builder()
            .withDelimiter(delimiter)
            .withRecordSeparator(recordSeparator)
            .build());
        final CSVPrinter printer = new CSVPrinter(outputStream, format);

        printer.printRecords(array);
        printer.close();

        final String actualCSV = outputStream.toString();
        final String expectedCSV = "test1,,\"test3\"\n";
        Assertions.assertEquals(expectedCSV, actualCSV);
    }

    @Test
    void testPrintRecordsWithIterable() throws IOException, SQLException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String delimiter = ",";
        final String recordSeparator = "\n";
        final Iterable<String> iterable = Arrays.asList("test1", "test2", "test3");
        final CSVFormat format = new CSVFormat(new CSVFormat.Builder()
            .withDelimiter(delimiter)
            .withRecordSeparator(recordSeparator)
            .build());
        final CSVPrinter printer = new CSVPrinter(outputStream, format);

        printer.printRecords(iterable);
        printer.close();

        final String actualCSV = outputStream.toString();
        final String expectedCSV = "test1,test2,test3\n";
        Assertions.assertEquals(expectedCSV, actualCSV);
    }

    @Test
    void testPrintRecordsWithEmptyIterable() throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String delimiter = ",";
        final String recordSeparator = "\n";
        final Iterable<String> iterable = Collections.emptyList();
        final CSVFormat format = new CSVFormat(new CSVFormat.Builder()
            .withDelimiter(delimiter)
            .withRecordSeparator(recordSeparator)
            .build());
        final CSVPrinter printer = new CSVPrinter(outputStream, format);

        printer.printRecords(iterable);
        printer.close();

        final String actualCSV = outputStream.toString();
        Assertions.assertEquals("", actualCSV);
    }

    @Test
    void testPrintRecordsWithJDBCResultSet() throws IOException, SQLException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final String delimiter = ",";
        final String recordSeparator = "\n";
        final String[] array = {"test1", "test2", "test3"};
        final CSVFormat format = new CSVFormat(new CSVFormat.Builder()
            .withDelimiter(delimiter)
            .withRecordSeparator(recordSeparator)
            .build());
        final CSVPrinter printer = new CSVPrinter(outputStream, format);

        final String[][] records = {{"test11", "test12", "test13"}, {"test21", "test22", "test23"}};
        final ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getString(anyInt())).thenAnswer(i -> array[i]);

        printer.printRecords(resultSet);
        printer.close();

        final String actualCSV = outputStream.toString();
        final String expectedCSV = "test11,test12,test13\n test21,test22,test23\n";
        Assertions.assertEquals(expectedCSV, actualCSV);
    }
}