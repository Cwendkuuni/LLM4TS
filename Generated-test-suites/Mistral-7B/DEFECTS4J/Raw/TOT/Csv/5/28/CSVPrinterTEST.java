package org.apache.commons.csv.test;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CSVPrinterTest {

    private ByteArrayOutputStream outputStream;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(new OutputStreamWriter(outputStream), CSVFormat.DEFAULT);
    }

    @Test
    public void testPrintSingleValue() throws IOException {
        printer.print("Hello, World!");
        printer.flush();
        String expected = "Hello, World!\n";
        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testPrintNullValue() throws IOException {
        printer.print(null);
        printer.flush();
        String expected = CSVFormat.DEFAULT.getNullString();
        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testPrintMultipleValuesOnSingleLine() throws IOException {
        printer.print("First, ");
        printer.print("Second");
        printer.println();
        String expected = "First,\"Second\n";
        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testPrintMultipleValuesOnMultipleLines() throws IOException {
        printer.print("First, ");
        printer.print("Second");
        printer.println();
        printer.print("Third, ");
        printer.print("Fourth");
        printer.println();
        String expected = "First,\"Second\nThird,\"Fourth\n";
        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testPrintEmptyLine() throws IOException {
        printer.println();
        String expected = CSVFormat.DEFAULT.getRecordSeparator();
        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() {
        CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outputStream), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOutputStream() {
        CSVPrinter printer = new CSVPrinter(null, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.println();
        printer.printComment("# This is a comment.");
        printer.flush();
        String expected = CSVFormat.DEFAULT.getCommentStart() + SP + "# This is a comment.\n";
        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testPrintEscapedValues() throws IOException {
        printer.setEscapeMode(CSVPrinter.ESCAPE_ALL);
        printer.print("Hello, World!\n");
        printer.println();
        printer.print("First, \"Second\", \"Third\", \"Fourth\", \"Fifth\", \"Sixth\", \"Seventh\"");
        printer.println();
        printer.print("\"Hello, World!\"");
        printer.println();
        printer.print("\r\n");
        printer.println();
        printer.print("\r\n\f\t");
        printer.println();
        String expected = "\"Hello, World!\",\"First\",\"Second\",\"Third\",\"Fourth\",\"Fifth\",\"Sixth\",\"Seventh\",\"Hello, World!\",\"\",\"\",\"\\r\\n\",\"\\r\\n\\f\\t\"\n";
        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testPrintQuotedValues() throws IOException {
        printer.setQuoteMode(CSVPrinter.QUOTE_MINIMAL);
        printer.print("Hello, World!\n");
        printer.println();
        printer.print("First, \"Second\", \"Third\", \"Fourth\", \"Fifth\", \"Sixth\", \"Seventh\"");
        printer.println();
        printer.print("\"Hello, World!\"");
        printer.println();
        printer.print("\r\n");
        printer.println();
        printer.print("\r\n\f\t");
        printer.println();
        String expected = "Hello, World!\n\"First\",Second,Third,Fourth,Fifth,Sixth,Seventh\",\"Hello, World!\",\"\",\"\",\"\\r\\n\",\"\\r\\n\\f\\t\"\n";
        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        String[] values = {"First, Second", "Third, Fourth", "Fifth, Sixth"};
        printer.printRecords(values);
        String expected = "First, Second\nThird, Fourth\nFifth, Sixth\n";
        Assert.assertEquals(expected, outputStream.toString());

        ResultSet resultSet = TestHelper.createMockResultSet("SELECT name, age FROM people");
        printer.printRecords(resultSet);
        expected = "name,age\nJohn Doe,30\nJane Doe,25\n";
        Assert.assertEquals(expected, outputStream.toString());
    }
}