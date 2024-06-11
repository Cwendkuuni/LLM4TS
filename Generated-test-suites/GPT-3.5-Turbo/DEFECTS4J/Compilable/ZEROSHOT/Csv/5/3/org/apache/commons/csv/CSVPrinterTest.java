package org.apache.commons.csv;
import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.util.Arrays;

public class CSVPrinterTest {
    
    @Test
    public void testPrint() throws IOException {
        StringBuilder expected = new StringBuilder("1234,abcd,12.34");
        StringBuilder actual = new StringBuilder();
        
        CSVPrinter printer = new CSVPrinter(actual, CSVFormat.DEFAULT);
        printer.print(1234);
        printer.print("abcd");
        printer.print(12.34);
        
        printer.close();
        
        Assert.assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testPrintComment() throws IOException {
        StringBuilder expected = new StringBuilder("### This is a comment ###\n");
        StringBuilder actual = new StringBuilder();
        
        CSVPrinter printer = new CSVPrinter(actual, CSVFormat.DEFAULT);
        printer.printComment("This is a comment");
        printer.close();
        
        Assert.assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testPrintln() throws IOException {
        StringBuilder expected = new StringBuilder("1234,abcd,12.34\n");
        StringBuilder actual = new StringBuilder();
        
        CSVPrinter printer = new CSVPrinter(actual, CSVFormat.DEFAULT);
        printer.print(1234);
        printer.print("abcd");
        printer.print(12.34);
        printer.println();
        
        printer.close();
        
        Assert.assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testPrintRecord() throws IOException {
        StringBuilder expected = new StringBuilder("1234,abcd,12.34\n");
        StringBuilder actual = new StringBuilder();
        
        CSVPrinter printer = new CSVPrinter(actual, CSVFormat.DEFAULT);
        printer.printRecord(Arrays.asList(1234, "abcd", 12.34));
        
        printer.close();
        
        Assert.assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testPrintRecords() throws IOException {
        StringBuilder expected = new StringBuilder("1234,abcd,12.34\n");
        StringBuilder actual = new StringBuilder();
        
        CSVPrinter printer = new CSVPrinter(actual, CSVFormat.DEFAULT);
        printer.printRecords(Arrays.asList(Arrays.asList(1234, "abcd", 12.34)));
        
        printer.close();
        
        Assert.assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testGetOut() throws IOException {
        StringBuilder expected = new StringBuilder("");
        StringBuilder actual = new StringBuilder();
        
        CSVPrinter printer = new CSVPrinter(actual, CSVFormat.DEFAULT);
        Appendable out = printer.getOut();
        
        Assert.assertEquals(expected.toString(), out.toString());
    }
}