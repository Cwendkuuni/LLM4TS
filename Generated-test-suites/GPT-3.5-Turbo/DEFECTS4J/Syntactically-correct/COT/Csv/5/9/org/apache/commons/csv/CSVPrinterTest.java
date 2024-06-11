import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVPrinterTest {
    
    private CSVPrinter csvPrinter;
    private StringBuilder stringBuilder;
    
    @Before
    public void setup() {
        stringBuilder = new StringBuilder();
        csvPrinter = new CSVPrinter(stringBuilder, CSVFormat.DEFAULT);
    }
    
    @Test
    public void testPrint() {
        String value = "Hello";
        try {
            csvPrinter.print(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("Hello", stringBuilder.toString());
    }
    
    @Test
    public void testPrintNullValue() {
        String value = null;
        try {
            csvPrinter.print(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("", stringBuilder.toString());
    }
    
    // Add more test cases for other methods
    
    @Test
    public void testPrintComment() {
        String comment = "This is a comment";
        try {
            csvPrinter.printComment(comment);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("# This is a comment\n", stringBuilder.toString());
    }
    
    @Test
    public void testPrintCommentWithNewline() {
        String comment = "This is a comment\nwith a newline";
        try {
            csvPrinter.printComment(comment);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("# This is a comment\n# with a newline\n", stringBuilder.toString());
    }
    
    // Add more test cases for other methods

}