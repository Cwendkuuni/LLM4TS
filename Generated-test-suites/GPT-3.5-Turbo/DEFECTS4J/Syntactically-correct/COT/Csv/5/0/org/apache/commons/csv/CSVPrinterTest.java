import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CSVPrinterTest {

    private static final Appendable OUT = mock(Appendable.class);
    private static final CSVFormat FORMAT = CSVFormat.DEFAULT;

    @Test
    public void testConstructor() {
        CSVPrinter printer = new CSVPrinter(OUT, FORMAT);
        assertNotNull(printer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullOut() {
        new CSVPrinter(null, FORMAT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullFormat() {
        new CSVPrinter(OUT, null);
    }

    @Test
    public void testClose() throws IOException {
        CSVPrinter printer = new CSVPrinter(OUT, FORMAT);
        printer.close();
        verify(OUT).close();
    }

    @Test
    public void testFlush() throws IOException {
        CSVPrinter printer = new CSVPrinter(OUT, FORMAT);
        printer.flush();
        verify(OUT).flush();
    }

    @Test
    public void testPrint() throws IOException {
        CSVPrinter printer = new CSVPrinter(OUT, FORMAT);
        printer.print("hello");
        verify(OUT).append("hello");
    }

    @Test
    public void testPrintNullValue() throws IOException {
        CSVPrinter printer = new CSVPrinter(OUT, FORMAT);
        printer.print(null);
        verify(OUT).append("");
    }

    @Test
    public void testPrintWithQuoting() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withQuoteChar('"').withQuoting(true);
        CSVPrinter printer = new CSVPrinter(OUT, format);
        printer.print("hello");
        verify(OUT).append("\"hello\"");
    }

    @Test
    public void testPrintWithEscaping() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withEscapeChar('\\').withEscaping(true);
        CSVPrinter printer = new CSVPrinter(OUT, format);
        printer.print("hello");
        verify(OUT).append("hello");
    }

    @Test
    public void testPrintComment() throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withCommentStart('#').withCommentingEnabled(true);
        CSVPrinter printer = new CSVPrinter(OUT, format);
        printer.printComment("This is a comment");
        verify(OUT).append("# This is a comment\n");
    }

    @Test
    public void testPrintRecordWithIterable() throws IOException {
        CSVPrinter printer = new CSVPrinter(OUT, FORMAT);
        printer.printRecord(Arrays.asList("hello", "world"));
        verify(OUT).append("hello");
        verify(OUT).append(",");
        verify(OUT).append("world");
        verify(OUT).append("\n");
    }

    @Test
    public void testPrintRecordWithArray() throws IOException {
        CSVPrinter printer = new CSVPrinter(OUT, FORMAT);
        printer.printRecord("hello", "world");
        verify(OUT).append("hello");
        verify(OUT).append(",");
        verify(OUT).append("world");
        verify(OUT).append("\n");
    }

    @Test
    public void testPrintRecordsWithIterable() throws IOException {
        CSVPrinter printer = new CSVPrinter(OUT, FORMAT);
        Iterable<Object> records = Arrays.asList("hello", "world");
        printer.printRecords(records);
        verify(OUT).append("hello");
        verify(OUT).append(",");
        verify(OUT).append("world");
        verify(OUT).append("\n");
    }

    @Test
    public void testPrintRecordsWithArray() throws IOException {
        CSVPrinter printer = new CSVPrinter(OUT, FORMAT);
        Object[] records = {"hello", "world"};
        printer.printRecords(records);
        verify(OUT).append("hello");
        verify(OUT).append(",");
        verify(OUT).append("world");
        verify(OUT).append("\n");
    }

    @Test
    public void testPrintRecordsWithResultSet() throws IOException, SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getMetaData()).thenReturn(mock(ResultSetMetaData.class));
        when(resultSet.getString(anyInt())).thenReturn("hello", "world");
        
        CSVPrinter printer = new CSVPrinter(OUT, FORMAT);
        printer.printRecords(resultSet);
        verify(resultSet, times(2)).getString(anyInt());
        verify(OUT).append("hello");
        verify(OUT).append(",");
        verify(OUT).append("world");
        verify(OUT, times(2)).append("\n");
    }

}