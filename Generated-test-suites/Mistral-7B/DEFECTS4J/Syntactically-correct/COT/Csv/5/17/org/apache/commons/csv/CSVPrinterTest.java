import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;

/**
 * Test class for CSVPrinter.
 */
public class CSVPrinterTest {

    @Test
    public void testCSVPrinterConstruction() throws IOException, SQLException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(outStream, format);
        Assert.assertNotNull(printer);
    }

    @Test
    public void testPrint() throws IOException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.print("Hello World");
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals("Hello World", result);
    }

    @Test
    public void testPrintQuotedValue() throws IOException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.print(new StringBuilder().append("\"Hello World\"").toString());
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals("\"Hello World\"", result);
    }

    @Test
    public void testPrintEscapedValue() throws IOException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, ',', '\u001A');
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.print("\"Hello,\"World\"");
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals("\"Hello,\"World\"", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPrintNull() throws IOException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.print(null);
        printer.close();
    }

    @Test
    public void testPrintMultipleValues() throws IOException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.print("Hello");
        printer.print("World");
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals("Hello,World", result);
    }

    @Test
    public void testPrintMultipleQuotedValues() throws IOException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.print("\"Hello\"");
        printer.print("\"World\"");
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals("\"Hello\",\"World\"", result);
    }

    @Test
    public void testPrintMultipleEscapedValues() throws IOException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, ',', '\u001A');
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.print("\"Hello,\"");
        printer.print("World\"");
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals("\"Hello,\",\"World\"", result);
    }

    @Test
    public void testPrintMultipleValuesWithDelimiter() throws IOException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.print("Hello");
        printer.print("World");
        printer.print(",");
        printer.print("CSVPrinter");
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals("Hello,World,CSVPrinter", result);
    }

    @Test
    public void testPrintMultipleQuotedValuesWithDelimiter() throws IOException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.print("\"Hello\"");
        printer.print("\"World\"");
        printer.print(",");
        printer.print("\"CSVPrinter\"");
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals("\"Hello\",\"World\",\"CSVPrinter\"", result);
    }

    @Test
    public void testPrintMultipleEscapedValuesWithDelimiter() throws IOException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, ',', '\u001A');
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.print("\"Hello,\"");
        printer.print("World\"");
        printer.print(",");
        printer.print("\"CSVPrinter\"");
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals("\"Hello,\",\"World\",\"CSVPrinter\"", result);
    }

    @Test
    public void testPrintComment() throws IOException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        format.setComment(";");
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.printComment("This is a comment");
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals(";This is a comment\n", result);
    }

    @Test
    public void testPrintRecord() throws IOException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.printRecord("Hello,World");
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals("Hello,World\n", result);
    }

    @Test
    public void testPrintRecordQuotedValues() throws IOException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.printRecord("\"Hello\",\"World\"");
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals("\"Hello\",\"World\"\n", result);
    }

    @Test
    public void testPrintRecordEscapedValues() throws IOException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, ',', '\u001A');
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.printRecord("\"Hello,\"World\"");
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals("\"Hello,\"World\"\n", result);
    }

    @Test
    public void testPrintRecords() throws IOException, SQLException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.printRecords(new String[]{"Hello", "World"});
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals("Hello,World\n", result);
    }

    @Test
    public void testPrintRecordsQuotedValues() throws IOException, SQLException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.printRecords(new String[]{"\"Hello\"", "\"World\""});
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals("\"Hello\",\"World\"\n", result);
    }

    @Test
    public void testPrintRecordsEscapedValues() throws IOException, SQLException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE, ',', '\u001A');
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        printer.printRecords(new String[]{"\"Hello,\"", "\"World\""});
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals("\"Hello,\",\"World\"\n", result);
    }

    @Test
    public void testPrintRecordsFromResultSet() throws IOException, SQLException {
        CSVFormat format = new CSVFormat(',', Quote.DOUBLE_QUOTE);
        StringWriter outWriter = new StringWriter();
        CSVPrinter printer = new CSVPrinter(outWriter, format);
        String query = "SELECT 'Hello', 'World' FROM DUAL";
        DatabaseUtils.queryPrint(new JdbcConnectionSource(), printer, query);
        printer.close();
        String result = outWriter.toString();
        Assert.assertEquals("Hello,World\n", result);
    }
}