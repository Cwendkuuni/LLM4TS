import org.apache.commons.csv.CSVFormat;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.Assert.*;

public class CSVPrinterTest {

    @Test
    public void testPrint_String() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.print("Hello");
        csvPrinter.close();
        assertEquals("Hello\n", writer.toString());
    }

    @Test
    public void testPrint_Object() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.print(123);
        csvPrinter.close();
        assertEquals("123\n", writer.toString());
    }

    @Test
    public void testPrint_Null() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.print(null);
        csvPrinter.close();
        assertEquals("\n", writer.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.printComment("This is a comment");
        csvPrinter.close();
        assertEquals("# This is a comment\n", writer.toString());
    }

    @Test
    public void testPrintRecord_ObjectArray() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.printRecord("John", "Doe", 30);
        csvPrinter.close();
        assertEquals("John,Doe,30\n", writer.toString());
    }

    @Test
    public void testPrintRecord_Iterable() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.printRecord(new String[]{"John", "Doe", "30"});
        csvPrinter.close();
        assertEquals("John,Doe,30\n", writer.toString());
    }

    @Test
    public void testPrintRecords_ObjectArrayArray() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.printRecords(new Object[][]{{"John", "Doe"}, {"Jane", "Smith"}});
        csvPrinter.close();
        assertEquals("John,Doe\nJane,Smith\n", writer.toString());
    }

    @Test
    public void testPrintRecords_Iterable() throws IOException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        csvPrinter.printRecords(new Iterable<String[]>(){
            @Override
            public Iterator<String[]> iterator() {
                return Arrays.asList(new String[]{"John", "Doe"}, new String[]{"Jane", "Smith"}).iterator();
            }
        });
        csvPrinter.close();
        assertEquals("John,Doe\nJane,Smith\n", writer.toString());
    }

    @Test
    public void testPrintRecords_ResultSet() throws IOException, SQLException {
        StringWriter writer = new StringWriter();
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        ResultSet resultSet = // Get ResultSet from somewhere
        csvPrinter.printRecords(resultSet);
        csvPrinter.close();
        // Compare the expected output with writer.toString()
    }
}