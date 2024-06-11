import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.io.IOException;

public class CSVPrinterTest {

    private CSVFormat format;
    private StringWriter writer;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        format = CSVFormat.DEFAULT;
        writer = new StringWriter();
        printer = new CSVPrinter(writer, format);
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord("value1", "value2", "value3");
        printer.flush();
        assertEquals("\"value1\",\"value2\",\"value3\"\r\n", writer.toString());
    }

    @Test
    public void testPrintNullValue() throws IOException {
        printer.print(null);
        printer.flush();
        assertEquals("\"\",\r\n", writer.toString());
    }

    @Test
    public void testPrintWithEscaping() throws IOException {
        format = CSVFormat.DEFAULT.withEscape('\\');
        writer = new StringWriter();
        printer = new CSVPrinter(writer, format);

        printer.print("this, has, commas");
        printer.flush();
        assertEquals("\"this\\, has\\, commas\"\r\n", writer.toString());
    }

}