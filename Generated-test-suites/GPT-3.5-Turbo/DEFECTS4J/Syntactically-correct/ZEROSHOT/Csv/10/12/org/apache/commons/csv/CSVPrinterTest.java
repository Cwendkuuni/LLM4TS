import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;
import java.io.StringWriter;
import java.io.IOException;

public class CSVPrinterTest {

    @Test
    public void testPrint() {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = null;
        try {
            printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
            printer.print("Hello");
        } catch (IOException e) {
            fail("IOException occurred");
        } finally {
            try {
                printer.close();
            } catch (IOException e) {
                fail("IOException occurred");
            }
        }
        assertEquals("Hello", sw.toString());
    }

    @Test
    public void testPrint_NullValue() {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = null;
        try {
            printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
            printer.print(null);
        } catch (IOException e) {
            fail("IOException occurred");
        } finally {
            try {
                printer.close();
            } catch (IOException e) {
                fail("IOException occurred");
            }
        }
        assertEquals("", sw.toString());
    }

    @Test
    public void testPrintComment() {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = null;
        try {
            printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
            printer.printComment("This is a comment.");
        } catch (IOException e) {
            fail("IOException occurred");
        } finally {
            try {
                printer.close();
            } catch (IOException e) {
                fail("IOException occurred");
            }
        }
        assertEquals("# This is a comment.\n", sw.toString());
    }

    @Test
    public void testPrintComment_DisabledCommenting() {
        StringWriter sw = new StringWriter();
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker(null);
        CSVPrinter printer = null;
        try {
            printer = new CSVPrinter(sw, format);
            printer.printComment("This is a comment.");
        } catch (IOException e) {
            fail("IOException occurred");
        } finally {
            try {
                printer.close();
            } catch (IOException e) {
                fail("IOException occurred");
            }
        }
        assertEquals("", sw.toString());
    }

    @Test
    public void testPrintln() {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = null;
        try {
            printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
            printer.println();
        } catch (IOException e) {
            fail("IOException occurred");
        } finally {
            try {
                printer.close();
            } catch (IOException e) {
                fail("IOException occurred");
            }
        }
        assertEquals("\n", sw.toString());
    }
    
    @Test
    public void testPrintRecord() {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = null;
        try {
            printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
            printer.printRecord("John", "Doe", "johndoe@example.com");
        } catch (IOException e) {
            fail("IOException occurred");
        } finally {
            try {
                printer.close();
            } catch (IOException e) {
                fail("IOException occurred");
            }
        }
        assertEquals("John,Doe,johndoe@example.com\n", sw.toString());
    }

    @Test
    public void testPrintRecords() {
        StringWriter sw = new StringWriter();
        CSVPrinter printer = null;
        String[] record1 = {"John", "Doe", "johndoe@example.com"};
        String[] record2 = {"Jane", "Smith", "janesmith@example.com"};
        try {
            printer = new CSVPrinter(sw, CSVFormat.DEFAULT);
            Object[] records = {record1, record2};
            printer.printRecords(records);
        } catch (IOException e) {
            fail("IOException occurred");
        } finally {
            try {
                printer.close();
            } catch (IOException e) {
                fail("IOException occurred");
            }
        }
        assertEquals("John,Doe,johndoe@example.com\nJane,Smith,janesmith@example.com\n", sw.toString());
    }

}