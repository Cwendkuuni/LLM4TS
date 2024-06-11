import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringWriter stringWriter;

    @Before
    public void setUp() throws IOException {
        stringWriter = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        csvPrinter = new CSVPrinter(stringWriter, csvFormat);
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("Hello");
        csvPrinter.print(1);
        csvPrinter.print(3.14);
        csvPrinter.flush();
        assertEquals("Hello,1,3.14", stringWriter.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.print("Hello");
        csvPrinter.print(1);
        csvPrinter.print(3.14);
        csvPrinter.println();
        assertEquals("# This is a comment\nHello,1,3.14\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("Hello", 1, 3.14);
        assertEquals("Hello,1,3.14\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        String[] record1 = {"Hello", "World"};
        String[] record2 = {"John", "Doe"};
        csvPrinter.printRecords(record1, record2);
        assertEquals("Hello,World\nJohn,Doe\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsWithIterable() throws IOException {
        Iterable<Integer> values = List.of(1, 2, 3, 4);
        csvPrinter.printRecords(values);
        assertEquals("1,2,3,4\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsWithResultSet() throws IOException, SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenReturn(mock(ResultSetMetaData.class));
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(anyInt())).thenReturn("John", "Doe");
        csvPrinter.printRecords(resultSet);
        assertEquals("John,Doe\n", stringWriter.toString());
    }

    // Add test cases for edge cases and exception handling scenarios here

}