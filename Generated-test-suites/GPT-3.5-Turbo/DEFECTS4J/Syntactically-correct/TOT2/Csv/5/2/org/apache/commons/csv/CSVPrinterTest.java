import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;

import java.io.StringWriter;
import java.io.IOException;

public class CSVPrinterTest {

    private StringWriter writer;
    private CSVPrinter csvPrinter;

    @Before
    public void setUp() {
        writer = new StringWriter();
        csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        csvPrinter.flush();
        csvPrinter.close();
        writer.close();
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("Hello, World");
        csvPrinter.flush();
        String output = writer.toString();
        assert "Hello, World".equals(output);
    }

    @Test
    public void testPrintNullValue() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        String output = writer.toString();
        assert "".equals(output); // Null values should be treated as empty strings
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("Name", "Age", "City");
        csvPrinter.printRecord("Alice", 25, "New York");
        csvPrinter.flush();
        String output = writer.toString();
        assert "Name,Age,City\nAlice,25,New York\n".equals(output);
    }

    @Test
    public void testPrintRecords() throws IOException {
        csvPrinter.printRecords(new Object[] {"Name", "Age", "City"}, new Object[] {"Alice", 25, "New York"});
        csvPrinter.flush();
        String output = writer.toString();
        assert "Name,Age,City\nAlice,25,New York\n".equals(output);
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.print("Value1");
        csvPrinter.printComment("This is a comment");
        csvPrinter.print("Value2");
        csvPrinter.flush();
        String output = writer.toString();
        assert "Value1\n# This is a comment\nValue2".equals(output);
    }

    @Test
    public void testPrintRecordsFromResultSet() throws IOException {
        // Mock ResultSet should be used for testing this method
        // Example: ResultSet resultSet = mock(ResultSet.class);
        // When(resultSet.next()).thenReturn(true, true, false);
        // When(resultSet.getString(1)).thenReturn("Alice");
        // When(resultSet.getInt(2)).thenReturn(25);
        // When(resultSet.getString(3)).thenReturn("New York");
        
        // csvPrinter.printRecords(resultSet);
    }
}