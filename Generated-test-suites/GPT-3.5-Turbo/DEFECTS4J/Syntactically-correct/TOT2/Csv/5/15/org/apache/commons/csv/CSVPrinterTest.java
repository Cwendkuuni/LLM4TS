import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringWriter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws Exception {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
        csvPrinter.print("value1");
        assertEquals("value1\n", stringWriter.toString());
    }

    @Test
    public void testPrintNull() throws Exception {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
        csvPrinter.print(null);
        assertEquals("\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecord() throws Exception {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
        csvPrinter.printRecord("value1", "value2", "value3");
        assertEquals("value1,value2,value3\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords() throws Exception {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
        csvPrinter.printRecords(new Object[]{"value1", "value2", "value3"});
        assertEquals("value1,value2,value3\n", stringWriter.toString());
    }

    @Test
    public void testPrintComment() throws Exception {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
        csvPrinter.printComment("This is a comment");
        assertEquals("# This is a comment\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordWithNull() throws Exception {
        StringWriter stringWriter = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
        csvPrinter.printRecord("value1", null, "value3");
        assertEquals("value1,,value3\n", stringWriter.toString());
    }
}