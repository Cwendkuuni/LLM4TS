package org.apache.commons.csv;
import org.junit.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws Exception {
        Appendable out = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.print("value");
        assert(out.toString().equals("value"));
    }
    
    @Test
    public void testPrint_NullValue() throws Exception {
        Appendable out = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.print(null);
        assert(out.toString().equals(""));
    }
    
    @Test
    public void testPrintComment() throws Exception {
        Appendable out = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printComment("This is a comment");
        assert(out.toString().equals("# This is a comment\n"));
    }
    
    @Test
    public void testPrintRecord() throws Exception {
        Appendable out = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printRecord("value1", "value2");
        assert(out.toString().equals("value1,value2\n"));
    }
    
    @Test
    public void testPrintRecords() throws Exception {
        Appendable out = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printRecords(new String[] {"value1", "value2"});
        assert(out.toString().equals("value1,value2\n"));
    }
    
    @Test
    public void testPrintRecords_NullValue() throws Exception {
        Appendable out = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printRecords(new String[] {"value1", null, "value2"});
        assert(out.toString().equals("value1,,value2\n"));
    }
    
    @Test
    public void testClose() throws Exception {
        Appendable out = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.close();
        // No assertion possible. Just check that the method does not throw an exception.
    }
    
    @Test
    public void testFlush() throws Exception {
        Appendable out = new StringBuilder();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.flush();
        // No assertion possible. Just check that the method does not throw an exception.
    }

}