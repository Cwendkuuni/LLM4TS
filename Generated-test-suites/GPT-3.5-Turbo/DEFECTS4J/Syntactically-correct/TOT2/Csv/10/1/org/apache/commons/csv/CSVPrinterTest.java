import org.junit.Test;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    @Test
    public void testPrintRecord() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        
        csvPrinter.printRecord("John", "Doe", 30);
        csvPrinter.flush();
        
        assertEquals("John,Doe,30\n", writer.toString());
    }

    @Test
    public void testPrintEmptyRecord() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        
        csvPrinter.printRecord();
        csvPrinter.flush();
        
        assertEquals("\n", writer.toString());
    }

    @Test
    public void testPrintWithQuotesAndCommas() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        
        csvPrinter.printRecord("Alice, Bob", "30,000");
        csvPrinter.flush();
        
        assertEquals("\"Alice, Bob\",\"30,000\"\n", writer.toString());
    }

    @Test
    public void testPrintWithEscapeCharacter() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withEscape('\\'));
        
        csvPrinter.printRecord("This\\ is\\ a\\ test");
        csvPrinter.flush();
        
        assertEquals("This\\ is\\ a\\ test\n", writer.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withComment('#'));
        
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        
        assertEquals("# This is a comment\n", writer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCSVPrinterCreation() throws IOException {
        CSVPrinter csvPrinter = new CSVPrinter(null, null);
    }

}