import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class CSVPrinterTest {

    @Test
    public void testPrint() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        
        csvPrinter.print("Value 1");
        csvPrinter.print("Value 2");
        csvPrinter.println();
        csvPrinter.print("Value 3");
        csvPrinter.flush();
        
        String expectedOutput = "Value 1,Value 2" + System.lineSeparator() + "Value 3" + System.lineSeparator();
        Assert.assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        
        csvPrinter.printComment("This is a comment");
        csvPrinter.println();
        csvPrinter.print("Value 1");
        csvPrinter.print("Value 2");
        csvPrinter.println();
        csvPrinter.print("Value 3");
        csvPrinter.flush();
        
        String expectedOutput = "# This is a comment" + System.lineSeparator() +
                "Value 1,Value 2" + System.lineSeparator() +
                "Value 3" + System.lineSeparator();
        Assert.assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        
        csvPrinter.printRecord("Value 1", "Value 2");
        csvPrinter.printRecord("Value 3");
        csvPrinter.flush();
        
        String expectedOutput = "Value 1,Value 2" + System.lineSeparator() + "Value 3" + System.lineSeparator();
        Assert.assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        
        csvPrinter.printRecords(new Object[] {"Value 1", "Value 2"}, new Object[]{"Value 3"});
        csvPrinter.flush();
        
        String expectedOutput = "Value 1,Value 2" + System.lineSeparator() + "Value 3" + System.lineSeparator();
        Assert.assertEquals(expectedOutput, writer.toString());
    }

    @Test
    public void testPrintRecordsFromResultSet() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        
        // Assume resultSet is a valid ResultSet object
        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();
        
        String expectedOutput = // expected output from ResultSet values
        Assert.assertEquals(expectedOutput, writer.toString());
    }
}