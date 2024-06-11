import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVPrinterTest {

    private CSVFormat format;
    private StringBuilder out;
    private CSVPrinter csvPrinter;

    @Before
    public void setup() {
        format = CSVFormat.DEFAULT;
        out = new StringBuilder();
        csvPrinter = new CSVPrinter(out, format);
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("value1");
        assertEquals("value1", out.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        assertEquals("# This is a comment\n", out.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("value1", "value2");
        assertEquals("value1,value2\n", out.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        csvPrinter.printRecords(new String[]{"value1", "value2"});
        assertEquals("value1,value2\n", out.toString());
    }

    @Test
    public void testPrintRecordsFromResultSet() throws Exception {
        // Mock ResultSet
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("value1");
        when(resultSet.getString(2)).thenReturn("value2");

        csvPrinter.printRecords(resultSet);
        assertEquals("value1,value2\n", out.toString());
    }

    @Test
    public void testFlush() throws IOException {
        csvPrinter.flush();
        // No assertion, just checking that flush method does not throw exceptions
    }

    @Test
    public void testClose() throws IOException {
        csvPrinter.close();
        // No assertion, just checking that close method does not throw exceptions
    }

}