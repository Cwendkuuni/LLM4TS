import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringWriter stringWriter;

    @Before
    public void setup() throws IOException {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        csvPrinter.close();
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("Value1");
        csvPrinter.print("Value2");
        csvPrinter.print(123);
        csvPrinter.flush();
        Assert.assertEquals("Value1,Value2,123", stringWriter.toString());
    }

    @Test
    public void testPrint_NullValue() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        Assert.assertEquals("", stringWriter.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.println();
        csvPrinter.print("Value1");
        csvPrinter.flush();
        Assert.assertEquals("# This is a comment\nValue1", stringWriter.toString());
    }

    @Test
    public void testPrintRecord_WithIterable() throws IOException {
        List<String> values = Arrays.asList("Value1", "Value2", "Value3");
        csvPrinter.printRecord(values);
        csvPrinter.flush();
        Assert.assertEquals("Value1,Value2,Value3", stringWriter.toString());
    }

    @Test
    public void testPrintRecord_WithArray() throws IOException {
        String[] values = {"Value1", "Value2", "Value3"};
        csvPrinter.printRecord(values);
        csvPrinter.flush();
        Assert.assertEquals("Value1,Value2,Value3", stringWriter.toString());
    }

    @Test
    public void testPrintRecords_WithIterable() throws IOException {
        List<String[]> records = Arrays.asList(
                new String[]{"Value1", "Value2", "Value3"},
                new String[]{"Value4", "Value5", "Value6"}
        );
        csvPrinter.printRecords(records);
        csvPrinter.flush();
        Assert.assertEquals("Value1,Value2,Value3\nValue4,Value5,Value6", stringWriter.toString());
    }

    @Test
    public void testPrintRecords_WithArray() throws IOException {
        String[][] records = {
                {"Value1", "Value2", "Value3"},
                {"Value4", "Value5", "Value6"}
        };
        csvPrinter.printRecords(records);
        csvPrinter.flush();
        Assert.assertEquals("Value1,Value2,Value3\nValue4,Value5,Value6", stringWriter.toString());
    }

    @Test
    public void testPrintRecords_WithResultSet() throws IOException {
        ResultSet resultSet = createResultSet();
        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();
        Assert.assertEquals("Value1,Value2,Value3\nValue4,Value5,Value6", stringWriter.toString());
    }

    private ResultSet createResultSet() {
        // TODO: Implement a fake ResultSet for testing purposes
    }
}