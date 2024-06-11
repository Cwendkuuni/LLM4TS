
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class CSVPrinterTest {

    private CSVPrinter csvPrinter;

    private Appendable out;

    private CSVFormat format;

    @Before
    public void setUp() {
        out = mock(Appendable.class);
        format = mock(CSVFormat.class);
        csvPrinter = new CSVPrinter(out, format);
    }

    @Test
    public void testClose() throws IOException {
        csvPrinter.close();
        verify(out).close();
    }

    @Test
    public void testFlush() throws IOException {
        csvPrinter.flush();
        verify(out).flush();
    }

    @Test
    public void testPrint() throws IOException {
        Object value = "test value";
        csvPrinter.print(value);
        verify(out).append(value.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        String comment = "test comment";
        csvPrinter.printComment(comment);
        verify(out).append("##TEST COMMENT##");
        verify(out).append(System.getProperty("line.separator"));
    }

    @Test
    public void testPrintln() throws IOException {
        csvPrinter.println();
        verify(out).append(format.getRecordSeparator());
    }

    // Additional test cases for edge cases and exception handling scenarios
    

}


