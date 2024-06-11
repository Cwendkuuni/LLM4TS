import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringWriter;
import java.io.IOException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVPrinterTest {

    @Test
    public void testPrintRecordSingleValue() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        printer.printRecord("value1");
        assertEquals("value1\n", sw.toString());
    }

    @Test
    public void testPrintRecordMultipleValues() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        printer.printRecord("value1", "value2", "value3");
        assertEquals("value1,value2,value3\n", sw.toString());
    }

    @Test
    public void testPrintRecordWithEscapeCharacters() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        printer.printRecord("value,1", "value\"2", "value\n3");
        assertEquals("value,1,\"value\"\"2\",\"value\n3\"\n", sw.toString());
    }

    @Test
    public void testPrintRecordsIterable() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        printer.printRecords(Arrays.asList("value1", "value2", "value3"));
        assertEquals("value1,value2,value3\n", sw.toString());
    }

    @Test
    public void testPrintRecordsArray() throws IOException {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        printer.printRecords(new String[] {"value1", "value2", "value3"});
        assertEquals("value1,value2,value3\n", sw.toString());
    }

    @Test
    public void testPrintRecordsResultSet() throws IOException, SQLException {
        // Additional setup to create a mock ResultSet can be added here
        StringWriter sw = new StringWriter();
        CSVPrinter printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
        printer.printRecords(mockResultSet);
        // Additional verification of the output can be added here
    }
}