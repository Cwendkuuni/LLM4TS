package org.apache.commons.csv; // for importing the class under test

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Test CSVPrinter class.
 */
public class CSVPrinterTest {

    private CSVFormat format;
    private ByteArrayOutputStream outStream;
    private CSVPrinter printer;

    @Before
    public void setUp() throws IOException {
        format = CSVFormat.DEFAULT;
        outStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(new StringWriter(outStream), format);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    @Test
    public void testPrint() throws IOException {
        printer.print("Hello World");
        Assert.assertEquals("Hello World\n", outStream.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        Assert.assertEquals("\"\"\n", outStream.toString());
    }

    @Test
    public void testPrintArrayOfStrings() throws IOException {
        String[] values = {"Hello", "World"};
        printer.printRecords(Arrays.asList(values));
        Assert.assertEquals("Hello,World\n", outStream.toString());
    }

    @Test
    public void testPrintIterableOfStrings() throws IOException {
        Iterable<String> values = Arrays.asList("Hello", "World");
        printer.printRecords(values);
        Assert.assertEquals("Hello,World\n", outStream.toString());
    }

    @Test
    public void testPrintObject() throws IOException {
        printer.print("42");
        Assert.assertEquals("42\n", outStream.toString());
    }

    @Test
    public void testPrintObjectQuoted() throws IOException {
        printer.format = new CSVFormat(format.getSeparator(), Quote.DOUBLE_QUOTE);
        printer.print('"');
        Assert.assertEquals("\"\"\n", outStream.toString());
    }

    @Test
    public void testPrintObjectWithQuoteInside() throws IOException {
        printer.format = new CSVFormat(format.getSeparator(), Quote.DOUBLE_QUOTE);
        printer.print("\"Hello World\"");
        Assert.assertEquals("\"Hello World\"\n", outStream.toString());
    }

    @Test
    public void testPrintEscapedChars() throws IOException {
        printer.format = new CSVFormat(format.getSeparator(), Quote.NONE, '\u001E');
        printer.print("Hello\u001EWorld");
        Assert.assertEquals("Hello\u001EWorld\n", outStream.toString());
    }

    @Test
    public void testPrintRecordSeparator() throws IOException {
        printer.format = new CSVFormat(format.getRecordSeparator(), Quote.NONE);
        printer.printRecords(Arrays.asList("Hello", "World"));
        Assert.assertEquals("Hello" + format.getRecordSeparator() + "World\n", outStream.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("Hello World");
        Assert.assertEquals("\\# Hello World\n\n", outStream.toString());
    }

    @Test(expected = IOException.class)
    public void testPrintException() throws IOException, SQLException {
        ResultSet resultSet = null;
        try {
            printer.printRecords(resultSet);
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
    }
}