import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;

    @Before
    public void setUp() {
        StringWriter stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    /*
     * Tests for print(Object value) method
     */

    @Test
    public void testPrint_StringValue() throws IOException {
        String value = "Hello";
        csvPrinter.print(value);
        csvPrinter.flush();
        assertEquals("Hello", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrint_NullValue() throws IOException {
        Object value = null;
        csvPrinter.print(value);
        csvPrinter.flush();
        assertEquals("", csvPrinter.getOut().toString());
    }

    /*
     * Tests for printComment(String comment) method
     */

    @Test
    public void testPrintComment() throws IOException {
        String comment = "This is a comment";
        csvPrinter.printComment(comment);
        csvPrinter.flush();
        assertEquals("# This is a comment\n", csvPrinter.getOut().toString());
    }

    /*
     * Tests for println() method
     */

    @Test
    public void testPrintln() throws IOException {
        csvPrinter.println();
        csvPrinter.flush();
        assertEquals("\n", csvPrinter.getOut().toString());
    }

    /*
     * Tests for printRecord(Iterable<?> values) method
     */

    @Test
    public void testPrintRecord_StringValues() throws IOException {
        String[] values = {"Value1", "Value2", "Value3"};
        csvPrinter.printRecord(values);
        csvPrinter.flush();
        assertEquals("Value1,Value2,Value3\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecord_NullValues() throws IOException {
        Object[] values = {null, null, null};
        csvPrinter.printRecord(values);
        csvPrinter.flush();
        assertEquals(",,\n", csvPrinter.getOut().toString());
    }

    /*
     * Tests for printRecords(Iterable<?> values) method
     */

    @Test
    public void testPrintRecords_StringValues() throws IOException {
        String[] values1 = {"Value1", "Value2", "Value3"};
        String[] values2 = {"Value4", "Value5", "Value6"};
        String[] values3 = {"Value7", "Value8", "Value9"};

        String[][] records = {values1, values2, values3};
        csvPrinter.printRecords(records);
        csvPrinter.flush();
        assertEquals("Value1,Value2,Value3\nValue4,Value5,Value6\nValue7,Value8,Value9\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecords_NullValues() throws IOException {
        Object[] values1 = {null, null, null};
        Object[] values2 = {null, null, null};
        Object[] values3 = {null, null, null};

        Object[][] records = {values1, values2, values3};
        csvPrinter.printRecords(records);
        csvPrinter.flush();
        assertEquals(",,\n,,\n,,\n", csvPrinter.getOut().toString());
    }

    /*
     * Tests for printRecords(Object[] values) method
     */

    @Test
    public void testPrintRecords_StringValues() throws IOException {
        String[] values1 = {"Value1", "Value2", "Value3"};
        String[] values2 = {"Value4", "Value5", "Value6"};
        String[] values3 = {"Value7", "Value8", "Value9"};

        String[][] records = {values1, values2, values3};
        csvPrinter.printRecords(records);
        csvPrinter.flush();
        assertEquals("Value1,Value2,Value3\nValue4,Value5,Value6\nValue7,Value8,Value9\n", csvPrinter.getOut().toString());
    }

    @Test
    public void testPrintRecords_NullValues() throws IOException {
        Object[] values1 = {null, null, null};
        Object[] values2 = {null, null, null};
        Object[] values3 = {null, null, null};

        Object[][] records = {values1, values2, values3};
        csvPrinter.printRecords(records);
        csvPrinter.flush();
        assertEquals(",,\n,,\n,,\n", csvPrinter.getOut().toString());
    }

}