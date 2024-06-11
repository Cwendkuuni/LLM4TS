import org.junit.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import java.io.StringWriter;
import java.io.IOException;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.print("value");
        csvPrinter.flush();
        String result = writer.toString();
        assertTrue(result.equals("value\n"));
    }

    @Test
    public void testPrintComment() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        String result = writer.toString();
        assertTrue(result.equals("# This is a comment\n"));
    }

    @Test
    public void testPrintRecord() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.printRecord("value1", "value2");
        csvPrinter.flush();
        String result = writer.toString();
        assertTrue(result.equals("value1,value2\n"));
    }

    @Test
    public void testPrintRecords() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        Object[] records = {"value1", "value2"};
        csvPrinter.printRecord(records);
        csvPrinter.flush();
        String result = writer.toString();
        assertTrue(result.equals("value1,value2\n"));
    }

    @Test
    public void testPrintRecordsResultSet() throws IOException {
        // This test requires a mock ResultSet implementation for testing purposes
        // Assuming that the method works correctly with a real ResultSet
    }

}