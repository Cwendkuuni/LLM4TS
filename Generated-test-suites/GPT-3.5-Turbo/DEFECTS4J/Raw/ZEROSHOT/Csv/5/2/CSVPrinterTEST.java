import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;

public class CSVPrinterTest {

    @Test
    public void testClose() throws IOException {
        // Create an Appendable object for testing
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);
        csvPrinter.close();
        // Assert that the Appendable object was closed
        Assert.assertEquals(true, sb instanceof AutoCloseable);
    }

    @Test
    public void testFlush() throws IOException {
        // Create an Appendable object for testing
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);
        csvPrinter.flush();
        // Assert that the Appendable object was flushed
        Assert.assertEquals("", sb.toString());
    }

    @Test
    public void testPrint() throws IOException {
        // Create an Appendable object for testing
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);
        csvPrinter.print("test");
        // Assert that the value was printed correctly
        Assert.assertEquals("test", sb.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        // Create an Appendable object for testing
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);
        csvPrinter.printComment("test comment");
        // Assert that the comment was printed correctly
        Assert.assertEquals("# test comment\n", sb.toString());
    }

    @Test
    public void testPrintln() throws IOException {
        // Create an Appendable object for testing
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);
        csvPrinter.println();
        // Assert that the record separator was printed correctly
        Assert.assertEquals("\n", sb.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        // Create an Appendable object for testing
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);
        csvPrinter.printRecord("value1", "value2", "value3");
        // Assert that the record was printed correctly
        Assert.assertEquals("value1,value2,value3\n", sb.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        // Create an Appendable object for testing
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);
        csvPrinter.printRecords(new String[]{"value1", "value2", "value3"});
        // Assert that the records were printed correctly
        Assert.assertEquals("value1,value2,value3\n", sb.toString());
    }

    @Test
    public void testGetOut() {
        // Create an Appendable object for testing
        StringBuilder sb = new StringBuilder();
        CSVPrinter csvPrinter = new CSVPrinter(sb, CSVFormat.DEFAULT);
        Appendable appendable = csvPrinter.getOut();
        // Assert that the correct Appendable object was returned
        Assert.assertEquals(sb, appendable);
    }
}