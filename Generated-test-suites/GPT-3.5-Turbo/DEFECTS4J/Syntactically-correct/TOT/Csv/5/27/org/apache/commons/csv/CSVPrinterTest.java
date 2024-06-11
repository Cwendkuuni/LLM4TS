import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class CSVPrinterTest {

    @Test
    public void testPrintWithNullValue() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.print(null);
        printer.flush();
        assertEquals("null", writer.toString());
    }

    @Test
    public void testPrintWithIntegerValue() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.print(123);
        printer.flush();
        assertEquals("123", writer.toString());
    }

    @Test
    public void testPrintWithEmptyString() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.print("");
        printer.flush();
        assertEquals("", writer.toString());
    }

    @Test
    public void testPrintWithQuotingEnabled() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withQuoting(true));
        printer.print("hello, world");
        printer.flush();
        assertEquals("\"hello, world\"", writer.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.printRecord("John", 25, "Male");
        printer.flush();
        assertEquals("John,25,Male\n", writer.toString());
    }

    @Test
    public void testPrintRecordsWithIterable() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.printRecords(Arrays.asList("John", 25, "Male"), Arrays.asList("Jane", 30, "Female"));
        printer.flush();
        assertEquals("John,25,Male\nJane,30,Female\n", writer.toString());
    }

    @Test
    public void testPrintRecordsWithArray() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.printRecords(new Object[]{"John", 25, "Male"}, new Object[]{"Jane", 30, "Female"});
        printer.flush();
        assertEquals("John,25,Male\nJane,30,Female\n", writer.toString());
    }

    @Test
    public void testPrintRecordsWithResultSet() throws IOException {
        // Assuming the following data in the ResultSet:
        // -----------------------------------------------
        // | Name  | Age | Gender |
        // -----------------------------------------------
        // | John  | 25  | Male   |
        // | Jane  | 30  | Female |
        // -----------------------------------------------
        
        // Mock the ResultSet object with a mock library (e.g., Mockito)
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("John", "Jane");
        when(resultSet.getString(2)).thenReturn("25", "30");
        when(resultSet.getString(3)).thenReturn("Male", "Female");
        
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.printRecords(resultSet);
        printer.flush();
        assertEquals("John,25,Male\nJane,30,Female\n", writer.toString());
    }
}