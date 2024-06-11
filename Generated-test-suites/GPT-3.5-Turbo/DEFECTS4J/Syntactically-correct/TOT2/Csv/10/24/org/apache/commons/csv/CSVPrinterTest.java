import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        csvPrinter.print("Value1");
        csvPrinter.print("Value2");
        csvPrinter.print(123);

        csvPrinter.flush();

        assertEquals("Value1,Value2,123", out.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT.withCommentingEnabled('#');
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        csvPrinter.printComment("This is a comment");

        assertEquals("# This is a comment\r\n", out.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        csvPrinter.printRecord("Value1", "Value2", 123);
        csvPrinter.printRecord("Value3", "Value4", 456);

        assertEquals("Value1,Value2,123\r\nValue3,Value4,456\r\n", out.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);

        csvPrinter.printRecords(new Object[] { "Value1", "Value2", 123 });
        csvPrinter.println();
        csvPrinter.printRecords(new Object[] { "Value3", "Value4", 456 });

        assertEquals("Value1,Value2,123\r\nValue3,Value4,456\r\n", out.toString());
    }

    @Test
    public void testPrintRecordsFromResultSet() throws IOException {
        // Assuming there is a mock ResultSet available for testing purposes
    }

}