package org.apache.commons.csv; // make sure the test class is in the same package as the tested class

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVPrinter.Quote;
import org.apache.commons.csv.QuoteMode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Test class for CSVPrinter.
 */
public class CSVPrinterTest {

    private CSVFormat format;
    private CSVPrinter printer;

    @Before
    public void setUp() throws Exception {
        format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.ALL);
    }

    @Test
    public void testCSVPrinter_BasicUsage() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(outputStream, format);
        printer.print("Hello, ");
        printer.print("World!");
        printer.println();

        String expectedOutput = "\"Hello, \",\"World!\"" + LF;
        Assert.assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testCSVPrinter_QuoteChar() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(outputStream, format.withQuoteChar('"'));
        printer.print("Hello, ");
        printer.print("World!");
        printer.println();

        String expectedOutput = "\"Hello, \",\"World!\"" + LF;
        Assert.assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testCSVPrinter_NullValue() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(outputStream, format);
        printer.print(null);
        printer.println();

        String expectedOutput = format.getNullString() + LF;
        Assert.assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testCSVPrinter_EmptyValue() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(outputStream, format);
        printer.print("");
        printer.println();

        String expectedOutput = format.getNullString() + LF;
        Assert.assertEquals(expectedOutput, outputStream.toString());

        outputStream.reset();
        printer.print("");
        printer.print(" ");
        printer.println();

        String expectedOutputWithSpace = format.getNullString() + SP + LF;
        Assert.assertEquals(expectedOutputWithSpace, outputStream.toString());
    }

    @Test
    public void testCSVPrinter_PrintRecord() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(outputStream, format);

        printer.printRecord(new String[]{"Hello", "World"});
        String expectedOutput = "\"Hello\",\"World\"" + LF;
        Assert.assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testCSVPrinter_PrintRecords() throws IOException, SQLException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(outputStream, format);

        Statement statement = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "").createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT 'Hello', 'World' FROM test");

        printer.printRecords(resultSet);

        String expectedOutput = "\"Hello\",\"World\"" + LF;
        Assert.assertEquals(expectedOutput, outputStream.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCSVPrinter_NullFormat() throws IOException {
        new CSVPrinter(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void flush() throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override
            public void close() throws IOException {
                throw new UnsupportedOperationException();
            }
        }, null);
    }

    @Test(expected = IOException.class)
    public void testCSVPrinter_NullOut() throws IOException {
        new CSVPrinter(null, format);
    }
}