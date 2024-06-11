package org.apache.commons.csv;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;
import java.io.StringReader;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;
import java.util.Arrays;

public class CSVPrinterTest {

    private StringWriter writer;
    private CSVFormat format;
    private CSVPrinter printer;

    @Before
    public void setUp() {
        writer = new StringWriter();
        format = CSVFormat.DEFAULT;
        printer = new CSVPrinter(writer, format);
    }

    @Test
    public void testPrintString() {
        String value = "Test";
        try {
            printer.print(value);
            String expected = "\"Test\"";
            assertEquals(expected, writer.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testPrintNull() {
        String value = null;
        try {
            printer.print(value);
            String expected = "";
            assertEquals(expected, writer.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testPrintComment() {
        String comment = "This is a comment";
        try {
            printer.printComment(comment);
            String expected = "# This is a comment \r\n";
            assertEquals(expected, writer.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        try {
            printer.close();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}