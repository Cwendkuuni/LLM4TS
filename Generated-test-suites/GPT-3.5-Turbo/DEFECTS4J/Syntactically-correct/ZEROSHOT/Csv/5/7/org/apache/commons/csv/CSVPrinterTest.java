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
    public void setup() {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint_nullValue_shouldPrintEmptyString() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        assertEquals("", stringWriter.toString());
    }

    @Test
    public void testPrint_stringValue_shouldPrintStringValue() throws IOException {
        csvPrinter.print("Hello");
        csvPrinter.flush();
        assertEquals("Hello", stringWriter.toString());
    }

    @Test
    public void testPrint_intValue_shouldPrintIntValue() throws IOException {
        csvPrinter.print(123);
        csvPrinter.flush();
        assertEquals("123", stringWriter.toString());
    }

    @Test
    public void testPrint_doubleValue_shouldPrintDoubleValue() throws IOException {
        csvPrinter.print(3.14);
        csvPrinter.flush();
        assertEquals("3.14", stringWriter.toString());
    }

    @Test
    public void testPrintObject_quotingEnabled_shouldPrintQuotedValue() throws IOException {
        CSVFormat format = CSVFormat.newFormat(',').withQuote('"').withQuotingEnabled();
        csvPrinter = new CSVPrinter(stringWriter, format);
        csvPrinter.printObject("Hello");
        csvPrinter.flush();
        assertEquals("\"Hello\"", stringWriter.toString());
    }

    @Test
    public void testPrintObject_escapingEnabled_shouldPrintEscapedValue() throws IOException {
        CSVFormat format = CSVFormat.newFormat(',').withEscape('\\').withEscapingEnabled();
        csvPrinter = new CSVPrinter(stringWriter, format);
        csvPrinter.printObject("A,B");
        csvPrinter.flush();
        assertEquals("A\\,B", stringWriter.toString());
    }

    @Test
    public void testPrintComment_shouldPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        assertEquals("# This is a comment\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordIterable_shouldPrintRecord() throws IOException {
        csvPrinter.printRecord("John", "Doe", 25);
        csvPrinter.flush();
        assertEquals("John,Doe,25\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordArray_shouldPrintRecord() throws IOException {
        csvPrinter.printRecord(new Object[]{"John", "Doe", 25});
        csvPrinter.flush();
        assertEquals("John,Doe,25\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordArrayWithNull_shouldPrintNullAsEmptyString() throws IOException {
        csvPrinter.printRecord(new Object[]{"John", null, 25});
        csvPrinter.flush();
        assertEquals("John,,25\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsIterable_shouldPrintRecords() throws IOException {
        csvPrinter.printRecords(new String[]{"John", "Doe", "25"});
        csvPrinter.flush();
        assertEquals("John,Doe,25\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsArray_shouldPrintRecords() throws IOException {
        csvPrinter.printRecords(new Object[][]{{"John", "Doe", 25}});
        csvPrinter.flush();
        assertEquals("John,Doe,25\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsResultSet_shouldPrintRecords() throws IOException {
        ResultSet resultSet = mockResultSet();
        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();
        assertEquals("John,Doe,25\nJane,Smith,30\n", stringWriter.toString());
    }

    private ResultSet mockResultSet() throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getMetaData()).thenReturn(mockMetaData());
        Mockito.when(resultSet.next()).thenReturn(true, true, false);
        Mockito.when(resultSet.getString(1)).thenReturn("John", "Jane");
        Mockito.when(resultSet.getString(2)).thenReturn("Doe", "Smith");
        Mockito.when(resultSet.getString(3)).thenReturn("25", "30");
        return resultSet;
    }

    private ResultSetMetaData mockMetaData() throws SQLException {
        ResultSetMetaData metaData = Mockito.mock(ResultSetMetaData.class);
        Mockito.when(metaData.getColumnCount()).thenReturn(3);
        return metaData;
    }
}