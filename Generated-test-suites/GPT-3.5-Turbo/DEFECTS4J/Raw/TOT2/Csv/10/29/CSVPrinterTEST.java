import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.csv.*;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws Exception {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.print("Hello");
        assertEquals("Hello", out.toString());
    }

    @Test
    public void testPrintNullValue() throws Exception {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.print(null);
        assertEquals("", out.toString());
    }

    @Test
    public void testPrintComment() throws Exception {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT.withCommentMarker('#');
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.printComment("This is a comment");
        assertEquals("# This is a comment\n", out.toString());
    }

    @Test
    public void testPrintRecord() throws Exception {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.printRecord("A", "B", "C");
        assertEquals("A,B,C\n", out.toString());
    }

    @Test
    public void testPrintRecords() throws Exception {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(out, format);
        Iterable<String> values = new ArrayList<>();
        values.add("X");
        values.add("Y");
        values.add("Z");
        printer.printRecords(values);
        assertEquals("X,Y,Z\n", out.toString());
    }

    @Test
    public void testPrintRecordsEmptyValue() throws Exception {
        Appendable out = new StringBuilder();
        CSVFormat format = CSVFormat.DEFAULT;
        CSVPrinter printer = new CSVPrinter(out, format);
        printer.printRecords(new Object[]{});
        assertEquals("\n", out.toString());
    }

}