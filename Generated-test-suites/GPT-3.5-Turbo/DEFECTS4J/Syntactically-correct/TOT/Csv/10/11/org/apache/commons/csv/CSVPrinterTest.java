import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class CSVPrinterTest {

    private StringWriter stringWriter;
    private CSVPrinter csvPrinter;
    
    @Before
    public void setup() throws IOException {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint_withNullValue_printsCorrectly() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        Assert.assertEquals("empty string expected", "", stringWriter.toString());
    }

    @Test
    public void testPrint_withString_printsCorrectly() throws IOException {
        csvPrinter.print("Hello World");
        csvPrinter.flush();
        Assert.assertEquals("Hello World expected", "Hello World", stringWriter.toString());
    }

    @Test
    public void testPrint_withInteger_printsCorrectly() throws IOException {
        csvPrinter.print(42);
        csvPrinter.flush();
        Assert.assertEquals("42 expected", "42", stringWriter.toString());
    }

    @Test
    public void testPrint_withDouble_printsCorrectly() throws IOException {
        csvPrinter.print(3.14);
        csvPrinter.flush();
        Assert.assertEquals("3.14 expected", "3.14", stringWriter.toString());
    }

    @Test
    public void testPrintRecord_withStringList_printsCorrectly() throws IOException {
        List<String> values = Arrays.asList("one", "two", "three");
        csvPrinter.printRecord(values);
        csvPrinter.flush();
        Assert.assertEquals("one,two,three\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecord_withStringArray_printsCorrectly() throws IOException {
        String[] values = {"one", "two", "three"};
        csvPrinter.printRecord(values);
        csvPrinter.flush();
        Assert.assertEquals("one,two,three\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecord_withIterable_printsCorrectly() throws IOException {
        List<String> values = Arrays.asList("one", "two", "three");
        csvPrinter.printRecord(values);
        csvPrinter.flush();
        Assert.assertEquals("one,two,three\n", stringWriter.toString());
    }

    @Test
    public void testPrintComment_withComment_printsCorrectly() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        Assert.assertEquals("# This is a comment\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords_withListOfArrays_printsCorrectly() throws IOException {
        List<String[]> records = Arrays.asList(
                new String[]{"one", "two", "three"},
                new String[]{"four", "five", "six"}
        );
        csvPrinter.printRecords(records);
        csvPrinter.flush();
        Assert.assertEquals("one,two,three\nfour,five,six\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords_withArrayOfArrays_printsCorrectly() throws IOException {
        String[][] records = {
                {"one", "two", "three"},
                {"four", "five", "six"}
        };
        csvPrinter.printRecords(records);
        csvPrinter.flush();
        Assert.assertEquals("one,two,three\nfour,five,six\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords_withResultSet_printsCorrectly() throws SQLException, IOException {
        ResultSet resultSet = // mock resultSet here
        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();
        Assert.assertEquals("expected result from the resultSet", stringWriter.toString());
    }

}