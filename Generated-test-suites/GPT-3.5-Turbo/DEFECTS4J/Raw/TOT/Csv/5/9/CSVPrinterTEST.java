import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringWriter stringWriter;

    private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.withDelimiter(',').withQuoteChar('"');

    @Before
    public void setUp() throws IOException {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSV_FORMAT);
    }

    @Test
    public void testPrintWithNullValue() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();

        Assert.assertEquals("\"\"\n", stringWriter.toString());
    }

    @Test
    public void testPrintWithNumberValue() throws IOException {
        csvPrinter.print(123.45);
        csvPrinter.flush();

        Assert.assertEquals("123.45\n", stringWriter.toString());
    }

    @Test
    public void testPrintWithEmptyValue() throws IOException {
        csvPrinter.print("");
        csvPrinter.flush();

        Assert.assertEquals("\"\"\n", stringWriter.toString());
    }

    @Test
    public void testPrintWithQuotingAndEscaping() throws IOException {
        csvPrinter = new CSVPrinter(stringWriter, CSV_FORMAT.withQuoting(true).withEscape('\\'));
        csvPrinter.print("Value,with,delimiter");
        csvPrinter.flush();

        Assert.assertEquals("\"Value\\,with\\,delimiter\"\n", stringWriter.toString());
    }

    @Test
    public void testPrintWithQuotingAndEscapingAndNullString() throws IOException {
        csvPrinter = new CSVPrinter(stringWriter, CSV_FORMAT.withQuoting(true).withEscape('\\').withNullString("NULL"));
        csvPrinter.print(null);
        csvPrinter.flush();

        Assert.assertEquals("\"NULL\"\n", stringWriter.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("Test comment");
        csvPrinter.flush();

        Assert.assertEquals("# Test comment\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordWithIterable() throws IOException {
        csvPrinter.printRecord("1", "2", "3");
        csvPrinter.flush();

        Assert.assertEquals("1,2,3\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordWithArray() throws IOException {
        String[] values = {"1", "2", "3"};
        csvPrinter.printRecord((Object[]) values);
        csvPrinter.flush();

        Assert.assertEquals("1,2,3\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordWithJDBCResultSet() throws IOException {
        // Assume resultSet is mocked with a single row containing values "1", "2", "3"
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getMetaData()).thenReturn(Mockito.mock(ResultSetMetaData.class));
        
        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();

        Assert.assertEquals("1,2,3\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsWithIterableOfArrays() throws IOException {
        String[] array1 = {"1", "2", "3"};
        String[] array2 = {"4", "5", "6"};

        csvPrinter.printRecords(Arrays.asList(array1, array2));
        csvPrinter.flush();

        Assert.assertEquals("1,2,3\n4,5,6\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsWithArray() throws IOException {
        String[] array1 = {"1", "2", "3"};
        String[] array2 = {"4", "5", "6"};

        csvPrinter.printRecords((Object[]) new Object[]{array1, array2});
        csvPrinter.flush();

        Assert.assertEquals("1,2,3\n4,5,6\n", stringWriter.toString());
    }

    @Test
    public void testGetOut() {
        Assert.assertEquals(stringWriter, csvPrinter.getOut());
    }

    @Test
    public void testClose() throws IOException {
        csvPrinter.close();

        Assert.assertTrue(stringWriter.toString().isEmpty());
    }

    @Test
    public void testFlush() throws IOException {
        csvPrinter.flush();

        Assert.assertTrue(stringWriter.toString().isEmpty());
    }

    // Additional Edge Case and Error Scenario tests can be added here

}