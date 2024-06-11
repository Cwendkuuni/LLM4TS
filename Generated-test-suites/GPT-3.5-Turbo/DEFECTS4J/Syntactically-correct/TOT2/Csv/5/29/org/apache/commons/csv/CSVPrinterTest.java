import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringWriter stringWriter;

    @Before
    public void setup() {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrintString() throws IOException {
        csvPrinter.print("Hello, World!");
        csvPrinter.flush();
        assertEquals("Hello, World!\r\n", stringWriter.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        assertEquals(",\r\n", stringWriter.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        assertEquals("# This is a comment\r\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("Alice", "Bob", "Carol");
        csvPrinter.flush();
        assertEquals("Alice,Bob,Carol\r\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        csvPrinter.printRecords(new String[] {"D", "E", "F"}, new String[] {"G", "H", "I"});
        csvPrinter.flush();
        assertEquals("D,E,F\r\nG,H,I\r\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsFromResultSet() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData().getColumnCount()).thenReturn(3);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString(1)).thenReturn("1");
        when(resultSet.getString(2)).thenReturn("2");
        when(resultSet.getString(3)).thenReturn("3");

        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();
        assertEquals("1,2,3\r\n", stringWriter.toString());
    }

    // Add more test cases here for edge cases and exception handling

}