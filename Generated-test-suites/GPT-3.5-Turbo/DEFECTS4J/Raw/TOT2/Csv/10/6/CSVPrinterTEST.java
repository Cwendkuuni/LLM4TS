import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;

    @Before
    public void setUp() throws IOException {
        StringWriter out = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        csvPrinter = new CSVPrinter(out, format);
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("value1");
        csvPrinter.print("value2");
        assertEquals("value1,value2", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        assertEquals("# This is a comment\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("record1", "record2");
        assertEquals("record1,record2\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        Object[] records = {"record1", "record2"};
        csvPrinter.printRecords(records);
        assertEquals("record1,record2\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecordsFromResultSet() throws IOException {
        // Mocking a ResultSet for testing
        ResultSetMock resultSet = new ResultSetMock();
        resultSet.addRow("value1", "value2");
        csvPrinter.printRecords(resultSet);
        assertEquals("value1,value2\n", csvPrinter.getOut().toString());
    }

    // Additional tests for edge cases and exceptional scenarios can be added here

}