import org.junit.Before;
import org.junit.Test;
import java.io.StringWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.csv.CSVFormat;

public class CSVPrinterTest {

    private StringWriter writer;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        writer = new StringWriter();
        printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint() throws IOException {
        String value = "Hello, World!";
        printer.print(value);
        printer.flush();
        assert(writer.toString().equals("Hello, World!"));
    }

    @Test
    public void testPrintComment() throws IOException {
        String comment = "This is a comment";
        printer.printComment(comment);
        printer.flush();
        assert(writer.toString().equals("# This is a comment\n"));
    }

    @Test
    public void testPrintln() throws IOException {
        printer.println();
        printer.flush();
        assert(writer.toString().equals("\n"));
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord("John", "Doe", 30);
        printer.flush();
        assert(writer.toString().equals("John,Doe,30\n"));
    }

    @Test
    public void testPrintRecords() throws IOException {
        Iterable<Object[]> records = new ArrayList<>();
        records.add(new Object[] {"John", "Doe"});
        records.add(new Object[] {"Jane", "Smith"});
        printer.printRecords(records);
        printer.flush();
        assert(writer.toString().equals("John,Doe\nJane,Smith\n"));
    }

    @Test
    public void testPrintRecordsResultSet() throws SQLException, IOException {
        ResultSet resultSet = createMockResultSet();
        printer.printRecords(resultSet);
        printer.flush();
        assert(writer.toString().equals("John,Doe\nJane,Smith\n"));
    }

    private ResultSet createMockResultSet() {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenReturn(mock(ResultSetMetaData.class));
        when(resultSet.getMetaData().getColumnCount()).thenReturn(2);
        try {
            when(resultSet.next()).thenReturn(true, true, false);
            when(resultSet.getString(anyInt())).thenReturn("John", "Doe", "Jane", "Smith");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}