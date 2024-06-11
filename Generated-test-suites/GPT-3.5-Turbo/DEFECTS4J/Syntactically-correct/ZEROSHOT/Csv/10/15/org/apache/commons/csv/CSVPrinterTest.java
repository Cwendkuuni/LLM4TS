import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class CSVPrinterTest {

    private Appendable out;
    private CSVFormat format;
    private CSVPrinter csvPrinter;

    @Before
    public void setUp() throws Exception {
        out = Mockito.mock(Appendable.class);
        format = Mockito.mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(out, format);
    }

    @After
    public void tearDown() throws Exception {
        csvPrinter.close();
    }

    @Test
    public void testPrint() throws Exception {
        Object value = "test";
        csvPrinter.print(value);
        Mockito.verify(out, times(1)).append(eq(value.toString()));
    }

    @Test
    public void testPrintWithNullValue() throws Exception {
        csvPrinter.print(null);
        Mockito.verify(out, times(1)).append(eq(""));
    }

    @Test
    public void testPrintWithNonDefaultNullString() throws Exception {
        when(format.getNullString()).thenReturn("NULL");
        csvPrinter.print(null);
        Mockito.verify(out, times(1)).append("NULL");
    }

    @Test
    public void testPrintWithQuotingEnabled() throws Exception {
        when(format.isQuoting()).thenReturn(true);
        Object value = "test";
        csvPrinter.print(value);
        Mockito.verify(out, times(1)).append(eq("\"" + value.toString() + "\""));
    }

    @Test
    public void testPrintWithEscapingEnabled() throws Exception {
        when(format.isEscaping()).thenReturn(true);
        Object value = "test\\";
        csvPrinter.print(value);
        Mockito.verify(out, times(1)).append(eq("test\\\\"));
    }

    @Test
    public void testPrintAndQuote() throws Exception {
        Object object = "";
        CharSequence value = "test";
        int offset = 0;
        int len = value.length();
        csvPrinter.printAndQuote(object, value, offset, len);
        Mockito.verify(out, times(1)).append(eq("\"" + value.toString() + "\""));
    }

    @Test
    public void testPrintAndEscape() throws Exception {
        CharSequence value = "test\n";
        int offset = 0;
        int len = value.length();
        csvPrinter.printAndEscape(value, offset, len);
        Mockito.verify(out, times(1)).append(eq("test\\n"));
    }

    @Test
    public void testPrintComment() throws Exception {
        when(format.isCommentingEnabled()).thenReturn(true);
        String comment = "This is a comment";
        csvPrinter.printComment(comment);
        Mockito.verify(out, times(1)).append(eq("# This is a comment\n"));
    }

    @Test
    public void testPrintln() throws Exception {
        csvPrinter.println();
        Mockito.verify(out, times(1)).append(eq(System.lineSeparator()));
    }

    @Test
    public void testPrintRecordWithIterable() throws Exception {
        Iterable<?> values = Mockito.mock(Iterable.class);
        csvPrinter.printRecord(values);
        Mockito.verify(out, times(1)).append(anyString());
    }

    @Test
    public void testPrintRecordWithObjectArray() throws Exception {
        Object[] values = {1, 2, 3};
        csvPrinter.printRecord(values);
        Mockito.verify(out, times(3)).append(anyString());
    }

    @Test
    public void testPrintRecordsWithIterable() throws Exception {
        Iterable<?> values = Mockito.mock(Iterable.class);
        csvPrinter.printRecords(values);
        Mockito.verify(out, times(1)).append(anyString());
    }

    @Test
    public void testPrintRecordsWithObjectArray() throws Exception {
        Object[] values = {1, 2, 3};
        csvPrinter.printRecords(values);
        Mockito.verify(out, times(3)).append(anyString());
    }

    @Test
    public void testPrintRecordsWithResultSet() throws Exception {
        java.sql.ResultSet resultSet = Mockito.mock(java.sql.ResultSet.class);
        java.sql.ResultSetMetaData resultSetMetaData = Mockito.mock(java.sql.ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSetMetaData.getColumnCount()).thenReturn(3);
        when(resultSet.getString(1)).thenReturn("1");
        when(resultSet.getString(2)).thenReturn("2");
        when(resultSet.getString(3)).thenReturn("3");
        csvPrinter.printRecords(resultSet);
        Mockito.verify(out, times(9)).append(anyString());
    }

    @Test
    public void testGetOut() throws Exception {
        Appendable result = csvPrinter.getOut();
        assert(result == out);
    }
}