import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private StringWriter stringWriter;

    @Before
    public void setup() {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("1");
        csvPrinter.flush();
        Assert.assertEquals("1", stringWriter.toString());
    }

    @Test
    public void testPrint_NullValue() throws IOException {
        csvPrinter.print(null);
        csvPrinter.flush();
        Assert.assertEquals("", stringWriter.toString());
    }

    @Test
    public void testPrint_ObjectValue() throws IOException {
        csvPrinter.print(new Object());
        csvPrinter.flush();
        Assert.assertEquals("java.lang.Object@<hashcode>", stringWriter.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();
        Assert.assertEquals("# This is a comment\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("1", "2", "3");
        csvPrinter.flush();
        Assert.assertEquals("1,2,3\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        csvPrinter.printRecords(new Object[]{"1", "2", "3"});
        csvPrinter.flush();
        Assert.assertEquals("1,2,3\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords_Iterable() throws IOException {
        csvPrinter.printRecords(Arrays.asList("1", "2", "3"));
        csvPrinter.flush();
        Assert.assertEquals("1,2,3\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords_JDBCResultSet() throws IOException, SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData metaData = mock(ResultSetMetaData.class);
        when(resultSet.getMetaData()).thenReturn(metaData);
        when(metaData.getColumnCount()).thenReturn(3);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getString(1)).thenReturn("1");
        when(resultSet.getString(2)).thenReturn("2");
        when(resultSet.getString(3)).thenReturn("3");

        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();

        Assert.assertEquals("1,2,3\n", stringWriter.toString());
    }

    @Test
    public void testGetOut() {
        Writer writer = csvPrinter.getOut();
        Assert.assertEquals(stringWriter, writer);
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

    @Test
    public void testPrint_multilineValue() throws IOException {
        csvPrinter.print("Hello\nWorld");
        csvPrinter.flush();
        Assert.assertEquals("\"Hello\nWorld\"", stringWriter.toString());
    }
}