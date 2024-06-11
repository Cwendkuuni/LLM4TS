import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class CSVPrinterTest {

    @Test
    public void testPrintString() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
        csvPrinter.print("Hello, World!");
        csvPrinter.flush();
        Assert.assertEquals("Hello, World!\r\n", stringWriter.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
        csvPrinter.print(null);
        csvPrinter.flush();
        Assert.assertEquals("\r\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordIterable() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
        csvPrinter.printRecord(Arrays.asList("John", "Doe", 30));
        csvPrinter.flush();
        Assert.assertEquals("John,Doe,30\r\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordArray() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
        csvPrinter.printRecord("John", "Doe", 30);
        csvPrinter.flush();
        Assert.assertEquals("John,Doe,30\r\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsIterable() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
        csvPrinter.printRecords(Arrays.asList(
                Arrays.asList("John", "Doe", 30),
                Arrays.asList("Jane", "Smith", 25)
        ));
        csvPrinter.flush();
        Assert.assertEquals("John,Doe,30\r\nJane,Smith,25\r\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsArray() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
        csvPrinter.printRecords(new Object[][]{
                {"John", "Doe", 30},
                {"Jane", "Smith", 25}
        });
        csvPrinter.flush();
        Assert.assertEquals("John,Doe,30\r\nJane,Smith,25\r\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsResultSet() throws SQLException, IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
        ResultSet resultSet = null; // Create a mock ResultSet object
        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();
        // Assert the output against the expected output
    }

    @Test
    public void testPrintComment() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withCommentMarker('#'));
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        Assert.assertEquals("# This is a comment\r\n", stringWriter.toString());
    }

    @Test
    public void testPrintMultipleValues() throws IOException {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
        csvPrinter.print("John");
        csvPrinter.print("Doe");
        csvPrinter.print(30);
        csvPrinter.flush();
        Assert.assertEquals("John,Doe,30\r\n", stringWriter.toString());
    }

    // Additional test cases can be added to cover more scenarios

}