package org.apache.commons.csv;
import org.apache.commons.csv.*;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class CSVPrinterTest {

    private StringWriter writer;
    private CSVPrinter printer;

    @Before
    public void setUp() throws IOException {
        writer = new StringWriter();
        printer = new CSVPrinter(writer, CSVFormat.DEFAULT);
    }

    @Test
    public void close() throws IOException {
        printer.close();
        assertTrue(writer.getBuffer().length() > 0);
    }

    @Test
    public void flush() throws IOException {
        printer.flush();
        assertTrue(writer.getBuffer().length() == 0);
    }

    @Test
    public void printObject() throws IOException {
        printer.print("Hello");
        printer.print("World");
        printer.println();
        assertEquals("Hello,World" + System.lineSeparator(), writer.toString());
    }

    @Test
    public void printComment() throws IOException {
        printer.printComment("This is a comment");
        assertEquals("# This is a comment" + System.lineSeparator(), writer.toString());
    }

    @Test
    public void println() throws IOException {
        printer.println();
        assertEquals(System.lineSeparator(), writer.toString());
    }

    @Test
    public void printRecordIterable() throws IOException {
        printer.printRecord(new Object[]{"Hello", "World"});
        assertEquals("Hello,World" + System.lineSeparator(), writer.toString());
    }

    @Test
    public void printRecordArray() throws IOException {
        printer.printRecord("Hello", "World");
        assertEquals("Hello,World" + System.lineSeparator(), writer.toString());
    }

    @Test
    public void printRecordsIterable() throws IOException {
        printer.printRecords(new Object[]{new Object[]{"Hello", "World"}, new Object[]{"Goodbye", "World"}});
        assertEquals("Hello,World" + System.lineSeparator() +
                     "Goodbye,World" + System.lineSeparator(), writer.toString());
    }

    @Test
    public void printRecordsArray() throws IOException {
        printer.printRecords(new Object[]{new Object[]{"Hello", "World"}, new Object[]{"Goodbye", "World"}});
        assertEquals("Hello,World" + System.lineSeparator() +
                     "Goodbye,World" + System.lineSeparator(), writer.toString());
    }

    @Test
    public void printRecordsResultSet() throws IOException {
        // Not testable without having a real ResultSet
    }

    @Test
    public void getOut() {
        assertEquals(writer, printer.getOut());
    }
}