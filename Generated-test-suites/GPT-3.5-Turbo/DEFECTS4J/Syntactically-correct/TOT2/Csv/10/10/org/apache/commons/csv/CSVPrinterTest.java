package org.apache.commons.csv;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

    private StringBuilder sb;
    private CSVPrinter printer;

    @Before
    public void setUp() throws IOException {
        sb = new StringBuilder();
        printer = new CSVPrinter(sb, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint() throws IOException {
        printer.print("Hello");
        assertEquals("Hello", sb.toString());
    }

    @Test
    public void testPrintNull() throws IOException {
        printer.print(null);
        assertEquals("", sb.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        printer.printComment("This is a comment");
        assertEquals("# This is a comment\n", sb.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        printer.printRecord("A", "B", "C");
        assertEquals("A,B,C\n", sb.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        printer.printRecords(new Object[] {"X", "Y", "Z"});
        assertEquals("X,Y,Z\n", sb.toString());
    }

    @Test
    public void testPrintRecordsWithArray() throws IOException {
        printer.printRecords(new Object[][] {{"1", "2", "3"}, {"4", "5", "6"}});
        assertEquals("1,2,3\n4,5,6\n", sb.toString());
    }

    @Test
    public void testPrintRecordsWithIterable() throws IOException {
        printer.printRecords(Arrays.asList(new Object[] {"A", "B", "C"}, new Object[] {"D", "E", "F"}));
        assertEquals("A,B,C\nD,E,F\n", sb.toString());
    }

    @Test
    public void testPrintRecordsWithResultSet() throws IOException, SQLException {
        // Implement a test scenario with a mock ResultSet to test this method
    }

    @Test
    public void testFlush() throws IOException {
        printer.flush();
        // Cannot assert flushed content as Appendable interface doesn't allow retrieval
    }

    @Test
    public void testClose() throws IOException {
        printer.close();
        // Cannot assert closed status as Appendable interface doesn't provide a method to check
    }

}