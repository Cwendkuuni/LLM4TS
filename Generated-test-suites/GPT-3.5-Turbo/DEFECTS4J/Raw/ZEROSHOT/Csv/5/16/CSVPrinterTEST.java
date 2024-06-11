import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class CSVPrinterTest {

    private Appendable mockOut;
    private CSVFormat mockFormat;
    private CSVPrinter csvPrinter;

    @Before
    public void setUp() {
        mockOut = mock(Appendable.class);
        mockFormat = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(mockOut, mockFormat);
    }

    @Test
    public void testClose() throws Exception {
        Closeable closeableMock = mock(Closeable.class);
        when(mockOut instanceof Closeable).thenReturn(true);
        when((Closeable) mockOut).thenReturn(closeableMock);

        csvPrinter.close();

        verify(closeableMock).close();
    }

    @Test
    public void testFlush() throws Exception {
        Flushable flushableMock = mock(Flushable.class);
        when(mockOut instanceof Flushable).thenReturn(true);
        when((Flushable) mockOut).thenReturn(flushableMock);
        
        csvPrinter.flush();

        verify(flushableMock).flush();
    }

    @Test
    public void testPrint_NullValue() throws Exception {
        when(mockFormat.getNullString()).thenReturn(null);

        csvPrinter.print(null);

        verify(mockOut).append(Constants.EMPTY);
    }

    @Test
    public void testPrint_NonNullValue() throws Exception {
        String value = "testValue";

        csvPrinter.print(value);

        verify(mockOut).append(value);
    }

    @Test
    public void testPrintAndEscape() throws Exception {
        CharSequence value = "testValue";
        int offset = 0;
        int len = value.length();

        csvPrinter.printAndEscape(value, offset, len);

        verify(mockOut).append(value, offset, offset + len);
    }

    @Test
    public void testPrintAndQuote_QuotePolicyAll() throws Exception {
        Object object = mock(Object.class);
        int offset = 0;
        int len = 5;

        when(mockFormat.getQuotePolicy()).thenReturn(Quote.ALL);

        csvPrinter.printAndQuote(object, "value", offset, len);

        verify(mockOut, times(2)).append(any(CharSequence.class));
    }

    @Test
    public void testPrintComment() throws Exception {
        String comment = "test comment";
        String recordSeparator = "test record separator";

        when(mockFormat.isCommentingEnabled()).thenReturn(true);
        when(mockFormat.getCommentStart()).thenReturn('C');
        when(mockFormat.getRecordSeparator()).thenReturn(recordSeparator);

        csvPrinter.printComment(comment);

        verify(mockOut).append('C');
        verify(mockOut).append(' ');
        verify(mockOut, times(comment.length())).append(anyChar());
        verify(mockOut).append(recordSeparator);
    }

    @Test
    public void testPrintln() throws Exception {
        String recordSeparator = "test record separator";

        when(mockFormat.getRecordSeparator()).thenReturn(recordSeparator);

        csvPrinter.println();

        verify(mockOut).append(recordSeparator);
    }

    @Test
    public void testPrintRecord_Iterable() throws Exception {
        Iterable<?> values = mock(Iterable.class);

        csvPrinter.printRecord(values);

        verify(csvPrinter).print(any(Object.class));
        verify(csvPrinter).println();
    }

    @Test
    public void testPrintRecord_ObjectArray() throws Exception {
        Object[] values = new Object[]{1, 2, 3};

        csvPrinter.printRecord(values);

        verify(csvPrinter, times(values.length)).print(any(Object.class));
        verify(csvPrinter).println();
    }

    @Test
    public void testPrintRecords_Iterable() throws Exception {
        Iterable<?> values = mock(Iterable.class);

        csvPrinter.printRecords(values);

        verify(csvPrinter).printRecord(any(Iterable.class));
    }

    @Test
    public void testPrintRecords_ObjectArray() throws Exception {
        Object[] values = new Object[]{1, 2, 3};

        csvPrinter.printRecords(values);

        verify(csvPrinter).printRecord(any(Object[].class));
    }

    @Test
    public void testPrintRecords_ResultSet() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);

        csvPrinter.printRecords(resultSet);

        verify(csvPrinter).printRecord(anyString());
        verify(csvPrinter).println();
    }

    @Test
    public void testGetOut() throws Exception {
        Appendable out = csvPrinter.getOut();

        assertSame(mockOut, out);
    }

}