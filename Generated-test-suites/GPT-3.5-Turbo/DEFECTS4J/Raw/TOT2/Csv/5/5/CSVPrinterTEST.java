import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    private StringWriter stringWriter;
    private CSVPrinter csvPrinter;

    @Before
    public void setUp() {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("hello");
        csvPrinter.flush();
        assertEquals("hello\n", stringWriter.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        assertEquals("\n", stringWriter.toString());
    }

    @Test
    public void testPrintEmpty() throws IOException {
        csvPrinter.print("");
        csvPrinter.flush();
        assertEquals("\n", stringWriter.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        assertEquals("# This is a comment\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("value1", 2, "value3");
        csvPrinter.flush();
        assertEquals("value1,2,value3\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        Object[] record1 = {"1", "2", "3"};
        Object[] record2 = {"a", "b", "c"};
        csvPrinter.printRecords(new Object[]{record1, record2});
        csvPrinter.flush();
        assertEquals("1,2,3\na,b,c\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsFromResultSet() throws IOException {
        // Mock ResultSet for testing
        ResultSet resultSet = mockResultSet();
        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();
        assertEquals("column1,column2,column3\nvalue1,value2,value3\nvalue4,value5,value6\n", stringWriter.toString());
    }

    private ResultSet mockResultSet() {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("value1", "value4");
        when(resultSet.getString(2)).thenReturn("value2", "value5");
        when(resultSet.getString(3)).thenReturn("value3", "value6");
        return resultSet;
    }
}