import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CSVPrinterTest {

    @Test
    public void testPrint() {
        // Test typical case
        // Setup
        Appendable out = mock(Appendable.class);
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        
        // Exercise
        try {
            csvPrinter.print("value");
        } catch (Exception e) {
            // Verify
            fail("Exception not expected");
        }
        
        // Verify
        verify(out).append("value");
    }

    @Test
    public void testPrintComment() {
        // Test typical case
        // Setup
        Appendable out = mock(Appendable.class);
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        
        // Exercise
        try {
            csvPrinter.printComment("comment");
        } catch (Exception e) {
            // Verify
            fail("Exception not expected");
        }
        
        // Verify
        verify(out).append("# comment");
    }

    @Test
    public void testPrintRecord() {
        // Test typical case
        // Setup
        Appendable out = mock(Appendable.class);
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        
        // Exercise
        try {
            csvPrinter.printRecord("value1", "value2");
        } catch (Exception e) {
            // Verify
            fail("Exception not expected");
        }
        
        // Verify
        verify(out).append("value1,value2");
    }

    @Test
    public void testPrintRecord_Exception() {
        // Test edge case - Null values
        // Setup
        Appendable out = mock(Appendable.class);
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        
        // Exercise & Verify
        assertThrows(IOException.class, () -> {
            csvPrinter.printRecord(null, null);
        });
    }

    @Test
    public void testPrintRecords() {
        // Test typical case
        // Setup
        Appendable out = mock(Appendable.class);
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        
        // Exercise
        try {
            csvPrinter.printRecords(new String[] {"value1", "value2"});
        } catch (Exception e) {
            // Verify
            fail("Exception not expected");
        }
        
        // Verify
        verify(out).append("value1,value2");
    }

    @Test
    public void testPrintRecords_Exception() {
        // Test error scenario - Invalid input type
        // Setup
        Appendable out = mock(Appendable.class);
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter csvPrinter = new CSVPrinter(out, format);
        
        // Exercise & Verify
        assertThrows(IOException.class, () -> {
            csvPrinter.printRecords(new Object());
        });
    }
}