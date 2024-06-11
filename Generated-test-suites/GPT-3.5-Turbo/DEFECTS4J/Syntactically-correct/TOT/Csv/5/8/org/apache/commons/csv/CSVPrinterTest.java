import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringBuilder stringBuilder;
    private CSVFormat csvFormat;

    @Before
    public void setUp() {
        stringBuilder = new StringBuilder();
        csvFormat = CSVFormat.DEFAULT.withDelimiter(',').withQuoteChar('"');
        csvPrinter = new CSVPrinter(stringBuilder, csvFormat);
    }

    @Test
    public void testPrint_NullValue_PrintsEmptyString() {
        String expected = "";
        csvPrinter.print(null);
        csvPrinter.flush();
        String actual = stringBuilder.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrint_ValueWithDelimiterValue_PrintsQuotedValue() {
        String expected = "\"Hello,World\"";
        csvPrinter.print("Hello,World");
        csvPrinter.flush();
        String actual = stringBuilder.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrint_RecordWithMultipleValues_PrintsAllValues() {
        String expected = "1,John,Doe";
        csvPrinter.printRecord(1, "John", "Doe");
        csvPrinter.flush();
        String actual = stringBuilder.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintRecord_EmptyRecord_PrintsEmptyLine() {
        String expected = System.lineSeparator();
        csvPrinter.printRecord();
        csvPrinter.flush();
        String actual = stringBuilder.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintComment_CommentWithNewLine_PrintsCommentOnMultipleLines() {
        String expected = "# This is a comment" + System.lineSeparator() +
                "# with multiple lines" + System.lineSeparator();
        csvPrinter.printComment("This is a comment\nwith multiple lines");
        csvPrinter.flush();
        String actual = stringBuilder.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintRecords_ArrayOfArrays_PrintsAllValues() {
        String expected = "1,John,Doe" + System.lineSeparator() +
                "\"2,3,4\",Jane,Smith" + System.lineSeparator();
        Object[] records = new Object[]{new Object[]{1, "John", "Doe"},
                new Object[]{"2,3,4", "Jane", "Smith"}};
        csvPrinter.printRecords(records);
        csvPrinter.flush();
        String actual = stringBuilder.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintRecords_NullValueInRecord_PrintsEmptyString() {
        String expected = "1,,Doe" + System.lineSeparator();
        Object[] record = new Object[]{1, null, "Doe"};
        csvPrinter.printRecords(record);
        csvPrinter.flush();
        String actual = stringBuilder.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintRecords_ResultSetWithMultipleRows_PrintsAllRows() throws SQLException {
        String expected = "1,John,Doe" + System.lineSeparator() +
                "2,Jane,Smith" + System.lineSeparator();
        ResultSet resultSet = createMockResultSet();
        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();
        String actual = stringBuilder.toString();
        assertEquals(expected, actual);
    }

    private ResultSet createMockResultSet() throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(true, true, false);
        Mockito.when(resultSet.getString(1)).thenReturn("1", "2");
        Mockito.when(resultSet.getString(2)).thenReturn("John", "Jane");
        Mockito.when(resultSet.getString(3)).thenReturn("Doe", "Smith");
        return resultSet;
    }
}