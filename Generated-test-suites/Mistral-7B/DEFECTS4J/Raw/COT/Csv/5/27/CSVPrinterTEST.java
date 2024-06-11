package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVPrinterImpl;
import org.apache.commons.csv.Quote;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;

/**
 * Tests for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter printer;

    @Before
    public void setup() {
        out = new ByteArrayOutputStream();
        format = new CSVFormat(CSVFormat.DEFAULT.withDelimiter(',').withQuote('"'));
        printer = new CSVPrinterImpl(out, format);
    }

    @Test
    public void testBasicPrint() throws IOException {
        printer.printRecord("value1", "value2", "value3");
        printer.flush();
        String actual = new String(out.toString().getBytes(), "UTF-8");
        String expected = "value1,\"value2\",value3" + CR + LF;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testQuoting() throws IOException {
        printer.printRecord("value with quote", "value, with quote, in it");
        printer.flush();
        String actual = new String(out.toString().getBytes(), "UTF-8");
        String expected = "value with quote,\"value, with quote, in it\"" + CR + LF;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEscape() throws IOException {
        printer.printRecord("value with CR LF, and quote");
        printer.flush();
        String actual = new String(out.toString().getBytes(), "UTF-8");
        String expected = "value with CR\\rLF,\\r\\nand quote" + CR + LF;
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.printRecord("value1", null, "value3");
        printer.flush();
        String actual = new String(out.toString().getBytes(), "UTF-8");
        String expected = "value1,,\"\"\r\n";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() {
        new CSVPrinter(new OutputStreamWriter(System.out), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOut() {
        new CSVPrinter(null, format);
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("this is a comment");
        printer.println();
        printer.flush();
        String actual = new String(out.toString().getBytes(), "UTF-8");
        String expected = "# this is a comment\r\n" +
                "#\r\n";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPrintCommentWithValue() throws IOException {
        printer.printRecord("value", "this is a comment");
        printer.flush();
        String actual = new String(out.toString().getBytes(), "UTF-8");
        String expected = "value,\"this is a comment\"\r\n";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPrintRecordWithValuesFromResultSet() throws IOException, SQLException {
        ResultSet resultSet = MockResultSet.create();
        printer.printRecords(resultSet);
        printer.flush();
        String actual = new String(out.toString().getBytes(), "UTF-8");
        String expected = "value1,\"value2\",value3\r\n" +
                "value4,\"value5\",value6\r\n";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPrintRecordWithValuesFromArray() throws IOException {
        String[] values = {"value1", "value2", "value3"};
        printer.printRecords(values);
        printer.flush();
        String actual = new String(out.toString().getBytes(), "UTF-8");
        String expected = "value1,\"value2\",value3\r\n";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPrintRecordWithValuesFromIterable() throws IOException {
        Iterable<String> values = Arrays.asList("value1", "value2", "value3");
        printer.printRecords(values);
        printer.flush();
        String actual = new String(out.toString().getBytes(), "UTF-8");
        String expected = "value1,\"value2\",value3\r\n";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testQuotePolicyWithMinimal() throws IOException {
        CSVFormat minimalFormat = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(Quote.MINIMAL));
        CSVPrinter minimalPrinter = new CSVPrinterImpl(out, minimalFormat);
        minimalPrinter.printRecord("value1", "123 value2");
        minimalPrinter.flush();
        String actual = new String(out.toString().getBytes(), "UTF-8");
        String expected = "value1,\"123 value2\"\r\n";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testQuotePolicyWithNone() throws IOException {
        CSVFormat noneFormat = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(Quote.NONE));
        CSVPrinter nonePrinter = new CSVPrinterImpl(out, noneFormat);
        nonePrinter.printRecord("value1", "123 value2");
        nonePrinter.flush();
        String actual = new String(out.toString().getBytes(), "UTF-8");
        String expected = "value1,123 value2\r\n";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testQuotePolicyWithAll() throws IOException {
        CSVFormat allFormat = new CSVFormat(CSVFormat.DEFAULT.withQuotePolicy(Quote.ALL));
        CSVPrinter allPrinter = new CSVPrinterImpl(out, allFormat);
        allPrinter.printRecord("value1", "123 value2");
        allPrinter.flush();
        String actual = new String(out.toString().getBytes(), "UTF-8");
        String expected = "\"value1\",\"123 value2\"\r\n";
        Assert.assertEquals(expected, actual);
    }
}