package org.apache.commons.csv;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private ByteArrayOutputStream outputStream;
    private CSVPrinter printer;
    private CSVFormat format;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(',').withHeader());
        printer = new CSVPrinter(new PrintStream(outputStream), format);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    @Test
    public void testClose() throws IOException {
        printer.close();
        printer = null;
    }

    @Test
    public void testFlush() throws IOException {
        printer.flush();
        printer.print("test");
        printer.flush();

        String output = outputStream.toString();
        String expected = "test\n";
        assertTrue(output.equals(expected));
    }

    @Test
    public void testPrint() throws IOException {
        printer.print("test");

        String output = outputStream.toString();
        String expected = "test\n";
        assertTrue(output.equals(expected));
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);

        String output = outputStream.toString();
        String expected = Constants.EMPTY;
        assertTrue(output.equals(expected));
    }

    @Test
    public void testPrintQuote() throws IOException {
        printer.print("test\"");

        String output = outputStream.toString();
        String expected = "\"test\"\n";
        assertTrue(output.equals(expected));
    }

    @Test
    public void testPrintEscape() throws IOException {
        printer.print("test\\");

        String output = outputStream.toString();
        String expected = "test\\\\\n";
        assertTrue(output.equals(expected));
    }

    @Test
    public void testPrintDelimiter() throws IOException {
        printer.print("test,");
        printer.print("next");

        String output = outputStream.toString();
        String expected = "test,\next\n";
        assertTrue(output.equals(expected));
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord(new String[]{"test", "next"});

        String output = outputStream.toString();
        String expected = "test,next\n";
        assertTrue(output.equals(expected));
    }

    @Test
    public void testPrintRecordIterable() throws IOException {
        printer.printRecords(Arrays.asList(new String[]{"test", "next"}));

        String output = outputStream.toString();
        String expected = "test,next\n";
        assertTrue(output.equals(expected));
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("comment");

        String output = outputStream.toString();
        String expected = Constants.COMMENT + Constants.LF + Constants.COMMENT + " comment" + Constants.LF;
        assertTrue(output.equals(expected));
    }

    @Test
    public void testPrintln() throws IOException {
        printer.println();

        String output = outputStream.toString();
        String expected = Constants.CR + Constants.LF;
        assertTrue(output.equals(expected));
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getString(1)).thenReturn("test1");
        when(resultSet.getString(2)).thenReturn("test2");

        printer.printRecords(resultSet);

        String output = outputStream.toString();
        String expected = "test1,\ttest2\n";
        assertTrue(output.equals(expected));
    }

    @Test
    public void testPrintRecordsWithObjects() throws IOException {
        printer.printRecords(new Object[]{"test1", "test2"});

        String output = outputStream.toString();
        String expected = "test1,\ttest2\n";
        assertTrue(output.equals(expected));
    }

    @Test
    public void testPrintRecordsWithArrays() throws IOException {
        printer.printRecords(new Object[][]{new Object[]{"test1", "test2"}});

        String output = outputStream.toString();
        String expected = "test1,\ttest2\n";
        assertTrue(output.equals(expected));
    }

    @Test
    public void testQuotePolicyAll() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(Quote.ALL));
        CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        printer.print("test");
        printer.print("test\"");
        printer.print("test\\");

        String output = outputStream.toString();
        String expected = "\"test\",\"test\"\"\",\"test\\\\\"\n";
        assertTrue(output.equals(expected));
    }

    @Test
    public void testQuotePolicyMinimal() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(Quote.MINIMAL));
        CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        printer.print("test");
        printer.print("test\"");
        printer.print("test\\");

        String output = outputStream.toString();
        String expected = "test,\"test\"\",test\\\n";
        assertTrue(output.equals(expected));
    }

    @Test
    public void testQuotePolicyNone() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(Quote.NONE));
        CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        printer.print("test");
        printer.print("test\"");
        printer.print("test\\");

        String output = outputStream.toString();
        String expected = "test,test\",test\\\n";
        assertTrue(output.equals(expected));
    }

    @Test
    public void testQuotePolicyNoneWithNull() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(Quote.NONE));
        CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        printer.print(null);
        printer.print("test");
        printer.print("test\"");
        printer.print("test\\");

        String output = outputStream.toString();
        String expected = Constants.EMPTY + ",test,\"test\"\",test\\\n";
        assertTrue(output.equals(expected));
    }

    @Test
    public void testQuotePolicyNoneWithNullAndEmpty() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(Quote.NONE));
        CSVPrinter printer = new CSVPrinter(new PrintStream(outputStream), format);

        printer.print(null);
        printer.print(Constants.EMPTY);
        printer.print("test");
        printer.print("test\"");
        printer.print("test\\");

        String output = outputStream.toString();
        String expected = Constants.EMPTY + Constants.EMPTY + ",test,\"test\"\",test\\\n";
        assertTrue(output.equals(expected));
    }
}