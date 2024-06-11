
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class CSVPrinterTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullOut() throws IOException {
        new CSVPrinter(null, CSVFormat.DEFAULT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullFormat() throws IOException {
        new CSVPrinter(new StringWriter(), null);
    }

    @Test
    public void testConstructor() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        assertNotNull(printer);
        assertNotNull(printer.getOut());
    }

    @Test
    public void testClose() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.close();
        writer.close();
    }

    @Test
    public void testFlush() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.flush();
        writer.close();
    }

    @Test
    public void testPrintNullValue() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.print(null);
        assertEquals("", writer.toString());
        writer.close();
    }

    @Test
    public void testPrintValue() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.print("Hello");
        assertEquals("Hello", writer.toString());
        writer.close();
    }

    @Test
    public void testPrintEmptyLine() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.println();
        assertEquals("\n", writer.toString());
        writer.close();
    }

    @Test
    public void testPrintComment() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.printComment("This is a comment");
        assertEquals("# This is a comment\n", writer.toString());
        writer.close();
    }

    @Test
    public void testPrintRecord() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
        printer.printRecord("John", "Doe", 25);
        assertEquals("John,Doe,25\n", writer.toString());
        writer.close();
    }
}
