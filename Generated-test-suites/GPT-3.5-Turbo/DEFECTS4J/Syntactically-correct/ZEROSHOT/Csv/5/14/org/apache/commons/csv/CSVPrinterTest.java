import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CSVPrinterTest {
    private CSVPrinter csvPrinter;
    private ByteArrayOutputStream outputStream;
    private OutputStreamWriter writer;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
        csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrintStringNull() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testPrintStringEmpty() throws IOException {
        csvPrinter.print("");
        csvPrinter.flush();
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testPrintString() throws IOException {
        csvPrinter.print("Hello");
        csvPrinter.flush();
        assertEquals("Hello", outputStream.toString());
    }

    @Test
    public void testPrintObjectNull() throws IOException {
        csvPrinter.print((Object)null);
        csvPrinter.flush();
        assertEquals("", outputStream.toString());
    }

    @Test
    public void testPrintObjectString() throws IOException {
        csvPrinter.print((Object)"Hello");
        csvPrinter.flush();
        assertEquals("Hello", outputStream.toString());
    }

    @Test
    public void testPrintObjectNumber() throws IOException {
        csvPrinter.print((Object)123);
        csvPrinter.flush();
        assertEquals("123", outputStream.toString());
    }

    @Test
    public void testPrintAndQuote() throws IOException {
        csvPrinter.printAndQuote("Hello");
        csvPrinter.flush();
        assertEquals("\"Hello\"", outputStream.toString());
    }

    @Test
    public void testPrintAndEscape() throws IOException {
        csvPrinter.printAndEscape("Hello");
        csvPrinter.flush();
        assertEquals("Hello", outputStream.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        assertEquals("# This is a comment\n", outputStream.toString());
    }

    @Test
    public void testPrintln() throws IOException {
        csvPrinter.println();
        csvPrinter.flush();
        assertEquals("\r\n", outputStream.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("Hello", "World");
        csvPrinter.flush();
        assertEquals("Hello,World\r\n", outputStream.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        Object[] records = new Object[] {"Hello", "World"};
        csvPrinter.printRecords(records);
        csvPrinter.flush();
        assertEquals("Hello,World\r\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordsIterable() throws IOException {
        Iterable<String> records = List.of("Hello", "World");
        csvPrinter.printRecords(records);
        csvPrinter.flush();
        assertEquals("Hello,World\r\n", outputStream.toString());
    }
    
    @Test
    public void testPrintRecordsResultSet() throws IOException, SQLException {
        ResultSet resultSet = new MockResultSet();
        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();
        assertEquals("Hello,World\r\n", outputStream.toString());
    }

    @Test
    public void testGetOut() {
        assertEquals(writer, csvPrinter.getOut());
    }
    
    private class MockResultSet implements ResultSet {
        private boolean hasNext = true;
        private int columnCount = 2;

        @Override
        public boolean next() throws SQLException {
            boolean current = hasNext;
            hasNext = false;
            return current;
        }

        @Override
        public String getString(int columnIndex) throws SQLException {
            return columnIndex == 1 ? "Hello" : "World";
        }

        // other interface methods are not implemented
    }
}