import org.apache.commons.csv.Assertions;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Constants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;

    @Before
    public void setUp() throws IOException {
        StringWriter stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    @Test
    public void testClose() throws IOException {
        csvPrinter.close();
        Assert.assertTrue(csvPrinter.getOut().close());
    }

    @Test
    public void testFlush() throws IOException {
        csvPrinter.flush();
        Assert.assertTrue(csvPrinter.getOut().flush());
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("Test");
        csvPrinter.flush();
        String result = csvPrinter.getOut().toString();
        Assert.assertEquals("Test", result);
    }

    @Test
    public void testPrintNull() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        String result = csvPrinter.getOut().toString();
        Assert.assertEquals("", result);
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        String result = csvPrinter.getOut().toString();
        Assert.assertEquals("# This is a comment\r\n", result);
    }

    @Test
    public void testPrintln() throws IOException {
        csvPrinter.println();
        String result = csvPrinter.getOut().toString();
        Assert.assertEquals(System.lineSeparator(), result);
    }

    @Test
    public void testPrintRecordIterable() throws IOException {
        csvPrinter.printRecord(new String[]{"1", "2", "3"});
        csvPrinter.flush();
        String result = csvPrinter.getOut().toString();
        Assert.assertEquals("1,2,3\r\n", result);
    }

    @Test
    public void testPrintRecordArray() throws IOException {
        csvPrinter.printRecord("1", "2", "3");
        csvPrinter.flush();
        String result = csvPrinter.getOut().toString();
        Assert.assertEquals("1,2,3\r\n", result);
    }

    @Test
    public void testPrintRecordsIterable() throws IOException {
        csvPrinter.printRecords(new Object[]{"1", "2", "3"});
        csvPrinter.flush();
        String result = csvPrinter.getOut().toString();
        Assert.assertEquals("1,2,3\r\n", result);
    }

    @Test
    public void testPrintRecordsArray() throws IOException {
        csvPrinter.printRecords(new String[]{"1", "2", "3"});
        csvPrinter.flush();
        String result = csvPrinter.getOut().toString();
        Assert.assertEquals("1,2,3\r\n", result);
    }

    @Test
    public void testPrintRecordsResultSet() throws IOException {
        csvPrinter.printRecords(null);
        Assert.assertEquals("", csvPrinter.getOut().toString());
    }
}