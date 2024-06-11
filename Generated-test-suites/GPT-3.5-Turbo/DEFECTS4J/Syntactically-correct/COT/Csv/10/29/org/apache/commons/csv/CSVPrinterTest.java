
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        csvPrinter = new CSVPrinter(new OutputStreamWriter(outputStream), CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("Hello");
        csvPrinter.flush();
        String expected = "Hello";
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintNull() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        String expected = ""; // null string is empty by default
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        String expected = "# This is a comment" + System.getProperty("line.separator");
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("John", "Doe", 25);
        csvPrinter.flush();
        String expected = "John,Doe,25" + System.getProperty("line.separator");
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintRecords() throws IOException {
        Object[] record1 = {"John", "Doe", 25};
        Object[] record2 = {"Jane", "Smith", 30};
        csvPrinter.printRecords(record1, record2);
        csvPrinter.flush();
        String expected = "John,Doe,25" + System.getProperty("line.separator") +
                          "Jane,Smith,30" + System.getProperty("line.separator");
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintRecordsWithIterable() throws IOException {
        Iterable<Object> record1 = List.of("John", "Doe", 25);
        Iterable<Object> record2 = List.of("Jane", "Smith", 30);
        csvPrinter.printRecords(record1, record2);
        csvPrinter.flush();
        String expected = "John,Doe,25" + System.getProperty("line.separator") +
                          "Jane,Smith,30" + System.getProperty("line.separator");
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintRecordsWithJDBCResultSet() throws IOException, SQLException {
        ResultSet resultSet = createMockResultSet();
        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();
        String expected = "Jane,Doe,25" + System.getProperty("line.separator") +
                          "John,Smith,30" + System.getProperty("line.separator");
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    private ResultSet createMockResultSet() throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);
        Mockito.when(resultSet.getMetaData()).thenReturn(metaData);
        Mockito.when(metaData.getColumnCount()).thenReturn(3);
        Mockito.when(resultSet.next())
                .thenReturn(true)
                .thenReturn(true)
                .thenReturn(false);
        Mockito.when(resultSet.getString(1)).thenReturn("Jane");
        Mockito.when(resultSet.getString(2)).thenReturn("Doe");
        Mockito.when(resultSet.getString(3)).thenReturn("25");
        Mockito.when(resultSet.getString(1)).thenReturn("John");
        Mockito.when(resultSet.getString(2)).thenReturn("Smith");
        Mockito.when(resultSet.getString(3)).thenReturn("30");
        return resultSet;
    }
}

