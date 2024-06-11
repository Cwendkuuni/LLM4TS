import org.junit.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws Exception {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.print("123");
        printer.println();
        assertEquals("123\n", writer.toString());
    }

    @Test
    public void testPrintComment() throws Exception {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.printComment("This is a comment");
        assertEquals("# This is a comment\n", writer.toString());
    }

    @Test
    public void testPrintRecord() throws Exception {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.printRecord("1", "2", "3");
        assertEquals("1,2,3\n", writer.toString());
    }

    @Test
    public void testFlush() throws Exception {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.flush();
        // No exception should be thrown
    }

    @Test
    public void testClose() throws Exception {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.close();
        // No exception should be thrown
    }

    // Add more test methods for other public methods as needed

}