import org.junit.Test;
import org.junit.After;
import org.junit.Before;

import java.io.StringWriter;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringWriter out;

    @Before
    public void setUp() {
        out = new StringWriter();
        csvPrinter = new CSVPrinter(out, CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        csvPrinter.close();
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("Hello");
        csvPrinter.flush();
        assertEquals("Hello", out.toString().trim());
    }

    @Test
    public void testPrintNullValue() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        assertEquals("", out.toString().trim());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        assertEquals("# This is a comment", out.toString().trim());
    }

    // Add more test cases for other methods

}