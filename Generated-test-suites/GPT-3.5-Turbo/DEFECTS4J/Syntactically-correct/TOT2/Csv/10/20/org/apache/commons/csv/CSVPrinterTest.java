
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testPrintSingleRecord() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.printRecord("John Doe", 30, "New York");

        String expected = "John Doe,30,New York" + System.lineSeparator();
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrintEmptyRecord() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.printRecord();

        String expected = "" + System.lineSeparator();
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrintEscapedValue() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.print("Hello, World!");

        String expected = "\"Hello, World!\"" + System.lineSeparator();
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.printComment("This is a comment");

        String expected = "# This is a comment" + System.lineSeparator();
        assertEquals(expected, writer.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.printRecords(new Object[] { "Alice", 25, "London" }, new Object[] { "Bob", 35, "Paris" });

        String expected = "Alice,25,London" + System.lineSeparator() + "Bob,35,Paris" + System.lineSeparator();
        assertEquals(expected, writer.toString());
    }

    @Test(expected = IOException.class)
    public void testIOExceptionOnPrint() throws IOException {
        CSVPrinter printer = new CSVPrinter(new BrokenWriter(), CSVFormat.DEFAULT);
        printer.print("Test");
    }

    private static class BrokenWriter extends StringWriter {
        @Override
        public void append(CharSequence csq, int start, int end) {
            throw new RuntimeException("IOException");
        }
    }
}
