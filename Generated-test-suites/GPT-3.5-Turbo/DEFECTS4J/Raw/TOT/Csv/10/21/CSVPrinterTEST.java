import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class CSVPrinterTest {

    @Test
    public void testPrintValue() throws IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("Hello");
        verify(out).append("Hello");
    }

    @Test
    public void testPrintNullValue() throws IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print(null);
        verify(out).append(Constants.EMPTY);
    }

    @Test
    public void testPrintAndEscapeValue() throws IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = mock(CSVFormat.class);
        when(format.isEscaping()).thenReturn(true);

        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("Hello,World");
        verify(out).append("Hello\\,World");
    }

    @Test
    public void testPrintAndQuoteValue() throws IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = mock(CSVFormat.class);
        when(format.isQuoting()).thenReturn(true);

        CSVPrinter printer = new CSVPrinter(out, format);

        printer.print("Hello,World");
        verify(out).append("\"Hello,World\"");
    }

    @Test
    public void testPrintComment() throws IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = mock(CSVFormat.class);
        when(format.isCommentingEnabled()).thenReturn(true);
        when(format.getCommentStart()).thenReturn('#');

        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printComment("This is a comment");
        verify(out).append("# This is a comment");
    }

    @Test
    public void testPrintRecord() throws IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.printRecord("Hello", "World");
        verify(out).append("Hello,World");
    }

    @Test
    public void testPrintRecords() throws IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(out, format);

        Iterable<String> values = new ArrayList<>();
        values.add("Hello");
        values.add("World");

        printer.printRecords(values);
        verify(out).append("Hello,World");
    }

    @Test
    public void testClose() throws IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.close();
        verify((Closeable) out).close();
    }

    @Test
    public void testFlush() throws IOException {
        Appendable out = mock(Appendable.class);
        CSVFormat format = mock(CSVFormat.class);
        CSVPrinter printer = new CSVPrinter(out, format);

        printer.flush();
        verify((Flushable) out).flush();
    }

}