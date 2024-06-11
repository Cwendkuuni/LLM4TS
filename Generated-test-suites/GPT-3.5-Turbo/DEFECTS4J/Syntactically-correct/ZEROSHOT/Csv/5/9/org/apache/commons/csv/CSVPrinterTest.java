import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class CSVPrinterTest {

    @Mock
    private Appendable outMock;
    private CSVFormat format;
    private CSVPrinter csvPrinter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        format = CSVFormat.DEFAULT;
        csvPrinter = new CSVPrinter(outMock, format);
    }

    @Test
    public void testPrint_NullValue() throws Exception {
        csvPrinter.print(null);

        verify(outMock, times(1)).append(format.getNullString() == null ? Constants.EMPTY : format.getNullString());
    }

    @Test
    public void testPrint_NumberValue() throws Exception {
        csvPrinter.print(123);

        verify(outMock, times(1)).append("123");
    }

    @Test
    public void testPrint_StringValue() throws Exception {
        csvPrinter.print("test");

        verify(outMock, times(1)).append("test");
    }

    @Test
    public void testPrintComment_CommentEnabled() throws Exception {
        format = CSVFormat.DEFAULT.withCommentStart('#');
        csvPrinter = new CSVPrinter(outMock, format);

        csvPrinter.printComment("This is a comment");

        verify(outMock, times(3)).append(System.lineSeparator());
        verify(outMock, times(1)).append("# This is a comment");
        verify(outMock, times(1)).append(" ");
    }

    @Test
    public void testPrintComment_CommentDisabled() throws Exception {
        csvPrinter.printComment("This is a comment");

        verifyZeroInteractions(outMock);
    }

    @Test
    public void testPrintRecord() throws Exception {
        csvPrinter.printRecord("A", "B", "C");

        verify(outMock, times(1)).append("A,B,C" + format.getRecordSeparator());
    }

    @Test
    public void testPrintRecords() throws Exception {
        Iterable<Object> records = new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return Arrays.asList("A", "B", "C").iterator();
            }
        };

        csvPrinter.printRecords(records);

        verify(outMock, times(1)).append("A,B,C" + format.getRecordSeparator());
    }

    @Test
    public void testPrintRecords_WithNestedArrays() throws Exception {
        Object[] records = {1, new Object[]{"A", "B", "C"}, "D"};

        csvPrinter.printRecords(records);

        verify(outMock, times(1)).append("1,A,B,C,D" + format.getRecordSeparator());
    }

    @Test
    public void testPrintRecords_WithNestedCollections() throws Exception {
        Iterable<Object> nestedCollection = new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return Arrays.asList("A", "B", "C").iterator();
            }
        };

        Iterable<Object> records = new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return Arrays.asList(1, nestedCollection, "D").iterator();
            }
        };

        csvPrinter.printRecords(records);

        verify(outMock, times(1)).append("1,A,B,C,D" + format.getRecordSeparator());
    }

    @Test
    public void testPrintRecords_WithResultSet() throws Exception {
        ResultSet resultSetMock = mock(ResultSet.class);
        when(resultSetMock.getMetaData()).thenReturn(mock(ResultSetMetaData.class));
        when(resultSetMock.next()).thenReturn(true, true, false);
        when(resultSetMock.getString(anyInt())).thenReturn("A", "B", "C");

        csvPrinter.printRecords(resultSetMock);

        verify(outMock, times(1)).append("A,B,C" + format.getRecordSeparator());
    }

    @Test
    public void testFlush() throws Exception {
        csvPrinter.flush();

        verify(outMock, times(1)).flush();
    }

    @Test
    public void testClose() throws Exception {
        csvPrinter.close();

        verify(outMock, times(1)).close();
    }

    @Test
    public void testGetOut() throws Exception {
        Appendable result = csvPrinter.getOut();

        Assert.assertEquals(outMock, result);
    }
}