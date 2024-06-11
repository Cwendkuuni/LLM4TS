import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testPrint() {
        // Test case for printing a non-null value
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.print("Hello");
            printer.flush();
            assertEquals("Hello", printer.getOut().toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testPrint_NullValue() {
        // Test case for printing a null value
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.print(null);
            printer.flush();
            assertEquals("", printer.getOut().toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testPrintComment() {
        // Test case for printing a comment
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.printComment("This is a comment");
            printer.flush();
            assertEquals("# This is a comment\n", printer.getOut().toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testPrintComment_MultipleLines() {
        // Test case for printing a comment with multiple lines
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.printComment("This is a\nmultiline\ncomment");
            printer.flush();
            assertEquals("# This is a\n# multiline\n# comment\n", printer.getOut().toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testPrintRecord() {
        // Test case for printing a single line of delimiter separated values
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.printRecord("Value1", "Value2", "Value3");
            printer.flush();
            assertEquals("Value1,Value2,Value3\n", printer.getOut().toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testPrintRecords() {
        // Test case for printing multiple lines of delimiter separated values
        CSVPrinter printer = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            printer.printRecords(new String[]{"Value1", "Value2"}, new String[]{"Value3", "Value4"});
            printer.flush();
            assertEquals("Value1,Value2\nValue3,Value4\n", printer.getOut().toString());
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }
}