import static org.junit.Assert.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringWriter stringWriter;

    @Before
    public void setUp() throws IOException {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        csvPrinter.close();
        stringWriter.close();
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("Value");
        csvPrinter.flush();
        String expectedOutput = "Value";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    @Test
    public void testPrint_NullValue() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        String expectedOutput = "";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("Test comment");
        csvPrinter.flush();
        String expectedOutput = "# Test comment\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("Value1", "Value2", "Value3");
        csvPrinter.flush();
        String expectedOutput = "Value1,Value2,Value3\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    @Test
    public void testPrintRecord_EmptyValues() throws IOException {
        csvPrinter.printRecord("", null, "Value3");
        csvPrinter.flush();
        String expectedOutput = ",,Value3\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    @Test
    public void testPrintRecord_IterableValues() throws IOException {
        Iterable<String> values = new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return Arrays.asList("Value1", "Value2", "Value3").iterator();
            }
        };
        csvPrinter.printRecord(values);
        csvPrinter.flush();
        String expectedOutput = "Value1,Value2,Value3\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        csvPrinter.printRecords(Arrays.asList("Value1", "Value2", "Value3"));
        csvPrinter.flush();
        String expectedOutput = "Value1\nValue2\nValue3\n";
        assertEquals(expectedOutput, stringWriter.toString());
    }
}