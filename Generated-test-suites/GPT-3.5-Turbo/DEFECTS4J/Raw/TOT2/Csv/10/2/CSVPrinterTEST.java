import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class CSVPrinterTest {

    @Test
    public void testPrint() {
        StringWriter sw = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = null;
        try {
            printer = new CSVPrinter(sw, format);
            printer.print("value1");
        } catch (IOException e) {
            fail("Unexpected IOException");
        } finally {
            if (printer != null) {
                try {
                    printer.close();
                } catch (IOException e) {
                    fail("Unexpected IOException during close");
                }
            }
        }

        assertEquals("value1", sw.toString());
    }

    @Test
    public void testPrintComment() {
        StringWriter sw = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT.withCommentStart('#');
        CSVPrinter printer = null;
        try {
            printer = new CSVPrinter(sw, format);
            printer.printComment("This is a comment");
        } catch (IOException e) {
            fail("Unexpected IOException");
        } finally {
            if (printer != null) {
                try {
                    printer.close();
                } catch (IOException e) {
                    fail("Unexpected IOException during close");
                }
            }
        }

        String expected = "# This is a comment" + System.lineSeparator();
        assertEquals(expected, sw.toString());
    }

    @Test
    public void testPrintRecord() {
        StringWriter sw = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = null;
        try {
            printer = new CSVPrinter(sw, format);
            printer.printRecord("value1", "value2", "value3");
        } catch (IOException e) {
            fail("Unexpected IOException");
        } finally {
            if (printer != null) {
                try {
                    printer.close();
                } catch (IOException e) {
                    fail("Unexpected IOException during close");
                }
            }
        }

        String expected = "value1,value2,value3" + System.lineSeparator();
        assertEquals(expected, sw.toString());
    }

    // Add more test cases for other methods and edge cases

}