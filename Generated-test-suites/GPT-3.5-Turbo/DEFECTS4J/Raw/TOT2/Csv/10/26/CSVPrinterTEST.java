import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.StringWriter;

@RunWith(JUnit4.class)
public class CSVPrinterTest {

    private StringWriter stringWriter;
    private CSVPrinter csvPrinter;

    @Before
    public void setUp() {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("test");
        csvPrinter.flush();
        assert(stringWriter.toString().equals("test\n"));
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        assert(stringWriter.toString().equals("# This is a comment\n"));
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("value1", "value2", "value3");
        csvPrinter.flush();
        assert(stringWriter.toString().equals("value1,value2,value3\n"));
    }

    @Test
    public void testPrintRecords() throws IOException {
        csvPrinter.printRecords(new Object[]{"a", "b", "c"}, new Object[]{"x", "y", "z"});
        csvPrinter.flush();
        assert(stringWriter.toString().equals("a,b,c\nx,y,z\n"));
    }

    @Test
    public void testPrintRecordsWithNull() throws IOException {
        csvPrinter.printRecords(new Object[]{"a", null, "c"});
        csvPrinter.flush();
        assert(stringWriter.toString().equals("a,,c\n"));
    }
}