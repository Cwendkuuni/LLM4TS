package org.apache.commons.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class CSVPrinterTest {

    private StringWriter writer;
    private CSVPrinter csvPrinter;

    @Before
    public void setUp() {
        writer = new StringWriter();
        csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        csvPrinter.close();
        writer.close();
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("Value1");
        csvPrinter.flush();
        Assert.assertEquals("Value1", writer.toString());
    }

    @Test
    public void testPrint_WithNullValue() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        Assert.assertEquals("# This is a comment\n", writer.toString());
    }

    @Test
    public void testPrintRecord_WithMultipleValues() throws IOException {
        csvPrinter.printRecord("Value1", "Value2", "Value3");
        csvPrinter.flush();
        Assert.assertEquals("Value1,Value2,Value3\n", writer.toString());
    }

    @Test
    public void testPrintRecord_WithIterable() throws IOException {
        csvPrinter.printRecord(new Iterable<Object>() {
            @Override
            public java.util.Iterator<Object> iterator() {
                return new java.util.Iterator<Object>() {
                    private int count = 0;

                    @Override
                    public boolean hasNext() {
                        return count < 3;
                    }

                    @Override
                    public Object next() {
                        count++;
                        return "Value" + count;
                    }

                    @Override
                    public void remove() {
                        // Not implemented
                    }
                };
            }
        });
        csvPrinter.flush();
        Assert.assertEquals("Value1,Value2,Value3\n", writer.toString());
    }

    @Test
    public void testPrintRecords_WithArray() throws IOException {
        csvPrinter.printRecords(new Object[]{"Value1", "Value2", "Value3"});
        csvPrinter.flush();
        Assert.assertEquals("Value1,Value2,Value3\n", writer.toString());
    }

    @Test
    public void testPrintRecords_WithIterable() throws IOException {
        csvPrinter.printRecords(new Iterable<Object>() {
            @Override
            public java.util.Iterator<Object> iterator() {
                return new java.util.Iterator<Object>() {
                    private int count = 0;

                    @Override
                    public boolean hasNext() {
                        return count < 3;
                    }

                    @Override
                    public Object next() {
                        count++;
                        return new Object[]{"Value" + count, "Value" + (count + 1), "Value" + (count + 2)};
                    }

                    @Override
                    public void remove() {
                        // Not implemented
                    }
                };
            }
        });
        csvPrinter.flush();
        Assert.assertEquals("Value1,Value2,Value3\nValue2,Value3,Value4\nValue3,Value4,Value5\n", writer.toString());
    }
}
