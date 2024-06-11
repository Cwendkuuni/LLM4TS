package org.apache.commons.csv.test;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.Quote;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for CSVPrinter
 */
public class CSVPrinterTest {

    private ByteArrayOutputStream output;
    private CSVPrinter printer;

    @Before
    public void setUp() throws Exception {
        output = new ByteArrayOutputStream();
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(',').withHeader("Header1", "Header2"));
        printer = new CSVPrinter(output, format);
    }

    @After
    public void tearDown() throws Exception {
        printer.close();
    }

    @Test
    public void testConstructor() throws IOException {
        // Basic test
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint() throws IOException {
        printer.print("Test string");
        String expected = "Test string" + Constants.CR;
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        String expected = Constants.EMPTY + Constants.CR;
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        List<Object> record = Arrays.asList("String1", "String2", null);
        printer.printRecord(record);
        String expected = "String1,\"String2\",\"" + Constants.EMPTY + "\"" + Constants.CR;
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecordsIterable() throws IOException {
        List<Object> record1 = Arrays.asList("String1", "String2");
        List<Object> record2 = Arrays.asList("String3", null);
        printer.printRecords(Collections.singletonList(record1));
        printer.printRecords(Collections.singletonList(record2));
        String expected = "String1,String2\r\nString3,\"\r\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecordsArray() throws IOException {
        Object[] record1 = new Object[]{"String1", "String2"};
        Object[] record2 = new Object[]{null, "String3"};
        printer.printRecords(record1);
        printer.printRecords(record2);
        String expected = "String1,String2\r\nnull,\"String3\"\r\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecordsResultSet() throws SQLException, IOException {
        String[][] data = {{"1", "Two", "Three"}, {"Four", "Five", "Six"}};
        ByteArrayOutputStream outputForResultSet = new ByteArrayOutputStream();
        CSVPrinter printerForResultSet = new CSVPrinter(outputForResultSet, CSVFormat.DEFAULT.withHeader("Id", "Header1", "Header2"));

        ResultSet resultSet = TestUtil.createMockResultSet(data);
        printerForResultSet.printRecords(resultSet);
        String expected = "Id,Header1,Header2\r\n1,Two,Three\r\n4,Four,Five\r\n";
        assertEquals(expected, outputForResultSet.toString());

        printerForResultSet.close();
        resultSet.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintWithNullFormat() throws IOException {
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintWithNullOutput() throws IOException {
        CSVPrinter printer = new CSVPrinter(null, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.println();
        printer.printComment("This is a comment");
        String expected = CR + COMMENT + SP + "This is a comment" + CR + CR;
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecordWithQuote() throws IOException {
        printer.printRecord(Arrays.asList("String with quote", "\"String with quote\""));
        String expected = "String with quote,\"String with quote\"\r\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecordWithEscape() throws IOException {
        printer.printRecord(Arrays.asList("String with CR", "String with LF", "String with CR,LF"));
        String expected = "String with CR,\"String with LF\",\"String with CR,LF\"\r\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecordWithEscapeDoubleQuote() throws IOException {
        printer.printRecord(Arrays.asList("String with double quote", "\"String with double quote\""));
        String expected = "String with double quote,\"String with double quote\"\r\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecordWithEscapeBackslash() throws IOException {
        printer.printRecord(Arrays.asList("String with backslash", "String with backslash\\"));
        String expected = "String with backslash,\"String with backslash\\\"\r\n";
        assertEquals(expected, output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintRecordWithEmptyIterable() throws IOException {
        printer.printRecord(Collections.emptyList());
    }

    @Test
    public void testPrintRecordWithEmptyArray() throws IOException {
        printer.printRecord(new Object[0]);
        String expected = Constants.CR;
        assertEquals(expected, output.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testPrintRecordWithNullObject() throws IOException {
        printer.printRecord(Collections.singletonList(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintRecordWithQuotePolicyNoneAndNumber() throws IOException {
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), CSVFormat.DEFAULT.withQuotePolicy(Quote.NONE)
                .withHeader("Number1", "Number2"));
        printer.printRecord(Arrays.asList(1, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintRecordWithQuotePolicyNoneAndNullNumber() throws IOException {
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), CSVFormat.DEFAULT.withQuotePolicy(Quote.NONE)
                .withHeader("Number1", "Number2"));
        printer.printRecord(Arrays.asList(null, 2));
    }

    @Test
    public void testPrintRecordWithQuotePolicyAll() throws IOException {
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), CSVFormat.DEFAULT.withQuotePolicy(Quote.ALL)
                .withHeader("String1", "String2"));
        printer.printRecord(Arrays.asList("String1", "String2"));
        String expected = "\"String1\",\"String2\"\r\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testPrintRecordWithQuotePolicyMinimal() throws IOException {
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), CSVFormat.DEFAULT.withQuotePolicy(Quote.MINIMAL)
                .withHeader("String1", "String2"));
        printer.printRecord(Arrays.asList("String1", "String2"));
        String expected = "String1,String2\r\n";
        assertEquals(expected, output.toString());

        printer.printRecord(Arrays.asList("String1 with comma", "String2 with comma"));
        String expectedWithCommas = "String1 with comma,\"String2 with comma\"\r\n";
        assertEquals(expectedWithCommas, output.toString());
    }

    @Test
    public void testPrintRecordWithQuotePolicyMinimalAndNull() throws IOException {
        CSVPrinter printer = new CSVPrinter(new ByteArrayOutputStream(), CSVFormat.DEFAULT.withQuotePolicy(Quote.MINIMAL)
                .withHeader("String1", "String2"));
        printer.printRecord(Arrays.asList("String1", null));
        String expected = "String1,\"\"\r\n";
        assertEquals(expected, output.toString());
    }
}