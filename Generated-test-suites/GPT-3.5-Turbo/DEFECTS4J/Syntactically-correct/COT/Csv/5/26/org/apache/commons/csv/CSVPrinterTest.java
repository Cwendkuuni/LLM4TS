import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CSVPrinterTest {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private CSVPrinter csvPrinter;

    @Mock
    private StringWriter out;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint_NullValue() throws IOException {
        csvPrinter.print(null);
        verify(out).append("");
    }

    @Test
    public void testPrint_NonNullValue() throws IOException {
        csvPrinter.print("value");
        verify(out).append("value");
    }

    @Test
    public void testPrint_CRLFValue() throws IOException {
        csvPrinter.print("\r\n");
        verify(out).append("\r\n");
    }

    @Test
    public void testPrint_QuoteValue() throws IOException {
        csvPrinter.print("value with,comma");
        verify(out).append("\"value with,comma\"");
    }

    @Test
    public void testPrintRecord_NullValue() throws IOException {
        csvPrinter.printRecord((Object) null);
        verify(out).append("");
        verify(out).append(", ");
    }

    @Test
    public void testPrintRecord_NonNullValue() throws IOException {
        csvPrinter.printRecord("value");
        verify(out).append("value");
        verify(out).append(LINE_SEPARATOR);
    }

    @Test
    public void testPrintRecord_MultipleValues() throws IOException {
        csvPrinter.printRecord("value1", "value2", "value3");
        verify(out).append("value1,value2,value3");
        verify(out).append(LINE_SEPARATOR);
    }

    @Test
    public void testPrintRecords_EmptyIterator() throws IOException {
        csvPrinter.printRecords(new Iterable<Object>() {
            @Override
            public Iterator<Object> iterator() {
                return Collections.emptyIterator();
            }
        });
        verify(out).append(LINE_SEPARATOR);
    }

    @Test
    public void testPrintRecords_NullArrayValue() throws IOException {
        csvPrinter.printRecords((Object) null);
        verify(out).append(LINE_SEPARATOR);
    }

    @Test
    public void testPrintRecords_EmptyArrayValue() throws IOException {
        csvPrinter.printRecords(new Object[0]);
        verify(out).append(LINE_SEPARATOR);
    }

    @Test
    public void testPrintRecords_NonEmptyArrayValue() throws IOException {
        csvPrinter.printRecords(new Object[] {"value1", "value2"});
        verify(out).append("value1,value2");
        verify(out).append(LINE_SEPARATOR);
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        verify(out).append("# This is a comment");
        verify(out).append(LINE_SEPARATOR);
    }

    @Test
    public void testPrintComment_MultiLine() throws IOException {
        csvPrinter.printComment("This is a\nmulti-line\ncomment");
        verify(out).append("# This is a");
        verify(out).append(LINE_SEPARATOR);
        verify(out).append("# multi-line");
        verify(out).append(LINE_SEPARATOR);
        verify(out).append("# comment");
        verify(out).append(LINE_SEPARATOR);
    }

    @Test
    public void testPrintln() throws IOException {
        csvPrinter.println();
        verify(out).append(LINE_SEPARATOR);
    }

    @Test
    public void testClose_Closeable() throws IOException {
        Closeable closeable = mock(Closeable.class);
        csvPrinter = new CSVPrinter(closeable, CSVFormat.DEFAULT);
        csvPrinter.close();
        verify(closeable).close();
    }

    @Test
    public void testFlush_Flushable() throws IOException {
        Flushable flushable = mock(Flushable.class);
        csvPrinter = new CSVPrinter(flushable, CSVFormat.DEFAULT);
        csvPrinter.flush();
        verify(flushable).flush();
    }

    @Test
    public void testGetOut() {
        StringWriter expectedOut = new StringWriter();
        assertEquals(expectedOut, csvPrinter.getOut());
    }
}