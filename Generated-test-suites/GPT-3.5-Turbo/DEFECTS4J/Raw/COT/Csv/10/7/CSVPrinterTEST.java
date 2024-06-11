import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringWriter stringWriter;

    @Before
    public void setUp() {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        csvPrinter.close();
    }

    @Test
    public void testPrintNullValue() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();

        assertEquals("", stringWriter.toString());
    }

    @Test
    public void testPrintStringValue() throws IOException {
        csvPrinter.print("Hello, World!");
        csvPrinter.flush();

        assertEquals("Hello, World!", stringWriter.toString());
    }

    @Test
    public void testPrintIntValue() throws IOException {
        csvPrinter.print(42);
        csvPrinter.flush();

        assertEquals("42", stringWriter.toString());
    }

    @Test
    public void testPrintDoubleValue() throws IOException {
        csvPrinter.print(3.14);
        csvPrinter.flush();

        assertEquals("3.14", stringWriter.toString());
    }
    
    // Add more test cases to cover all the public methods in the CSVPrinter class

}

