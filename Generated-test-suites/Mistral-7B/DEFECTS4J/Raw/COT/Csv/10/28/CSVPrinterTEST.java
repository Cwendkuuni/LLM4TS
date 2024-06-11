package org.apache.commons.csv; // make sure to import necessary packages

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.apache.commons.io.output.StringBuilderWriter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Test class for {@link CSVPrinter}.
 */
public class CSVPrinterTest {

    private CSVPrinter printer;

    @Before
    public void setUp() throws Exception {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, Quote.MINIMAL);
        StringBuilderWriter out = new StringBuilderWriter();
        printer = new CSVPrinter(out, format);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    @Test
    public void testPrintNullValues() throws IOException {
        printer.printNull();
        Assert.assertEquals("\"\",\n", printer.getOut().toString());
    }

    @Test
    public void testPrintSingleValue() throws IOException {
        printer.print("Hello");
        Assert.assertEquals("Hello\n", printer.getOut().toString());
    }

    @Test
    public void testPrintQuotedValue() throws IOException {
        printer.format = new CSVFormat(CSVFormat.DEFAULT, Quote.ALL);
        printer.print("Hello");
        Assert.assertEquals("\"Hello\",\n", printer.getOut().toString());
    }

    @Test
    public void testPrintQuotedValueWithEscapedCharacters() throws IOException {
        printer.format = new CSVFormat(CSVFormat.DEFAULT, Quote.ALL);
        printer.print("\"Hello, World!\"");
        Assert.assertEquals("\"\"Hello, World!\"\",\n", printer.getOut().toString());
    }

    @Test
    public void testPrintMultipleValues() throws IOException {
        printer.print("Hello");
        printer.print("World");
        printer.println();
        Assert.assertEquals("Hello,World\n", printer.getOut().toString());
    }

    @Test
    public void testPrintMultipleQuotedValues() throws IOException {
        printer.format = new CSVFormat(CSVFormat.DEFAULT, Quote.ALL);
        printer.print("\"Hello\"");
        printer.print("\"World\"");
        printer.println();
        Assert.assertEquals("\"Hello\",\"World\"\n", printer.getOut().toString());
    }

    @Test
    public void testPrintMultipleQuotedValuesWithEscapedCharacters() throws IOException {
        printer.format = new CSVFormat(CSVFormat.DEFAULT, Quote.ALL);
        printer.print("\"Hello,\"World!\"");
        printer.println();
        Assert.assertEquals("\"Hello,\"World!\",\n", printer.getOut().toString());
    }

    @Test
    public void testPrintMultipleRecords() throws IOException {
        printer.printRecord(Arrays.asList("Hello1", "World1"));
        printer.printRecord(Arrays.asList("Hello2", "World2"));
        printer.println();
        Assert.assertEquals("Hello1,World1\nHello2,World2\n", printer.getOut().toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("Hello Comment");
        Assert.assertEquals("/*\n * Hello Comment\n */\n", printer.getOut().toString());
    }

    @Test
    public void testPrintRecordWithNullValues() throws IOException, SQLException {
        Statement statement = TestHelper.getTestStatement();
        ResultSet resultSet = statement.executeQuery("SELECT null, null, null FROM testTable");
        printer.printRecords(resultSet);
        Assert.assertEquals("\"\",\"\",\"\"\n\"\",\"\",\"\"\n\"\",\"\",\"\"\n", printer.getOut().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidFormat() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT, null);
        printer = new CSVPrinter(new StringBuilderWriter(), format);
    }
}