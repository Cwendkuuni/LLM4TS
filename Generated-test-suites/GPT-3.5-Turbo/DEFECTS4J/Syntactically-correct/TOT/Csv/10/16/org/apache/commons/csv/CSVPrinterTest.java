import org.junit.Assert;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CSVPrinterTest {

    // Test print() method with null value
    @Test
    public void testPrintWithNullValue() throws IOException {
        StringWriter out = new StringWriter();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.print(null);
        printer.flush();
        Assert.assertEquals("", out.toString());
    }

    // Test print() method with normal value
    @Test
    public void testPrintWithNormalValue() throws IOException {
        StringWriter out = new StringWriter();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.print("Hello");
        printer.flush();
        Assert.assertEquals("Hello", out.toString());
    }

    // Test print() method with value that needs escaping
    @Test
    public void testPrintWithEscapingValue() throws IOException {
        StringWriter out = new StringWriter();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.print("Hello,world");
        printer.flush();
        Assert.assertEquals("\"Hello,world\"", out.toString());
    }

    // Test printComment() method
    @Test
    public void testPrintComment() throws IOException {
        StringWriter out = new StringWriter();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printComment("This is a comment");
        printer.flush();
        Assert.assertEquals("# This is a comment\n", out.toString());
    }

    // Test println() method
    @Test
    public void testPrintln() throws IOException {
        StringWriter out = new StringWriter();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.println();
        printer.flush();
        Assert.assertEquals("\n", out.toString());
    }

    // Test printRecord() method with Iterable
    @Test
    public void testPrintRecordWithIterable() throws IOException {
        StringWriter out = new StringWriter();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printRecord(Arrays.asList("Hello", "World"));
        printer.flush();
        Assert.assertEquals("Hello,World\n", out.toString());
    }

    // Test printRecord() method with array of objects
    @Test
    public void testPrintRecordWithArray() throws IOException {
        StringWriter out = new StringWriter();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printRecord("Hello", "World");
        printer.flush();
        Assert.assertEquals("Hello,World\n", out.toString());
    }

    // Test printRecords() method with Iterable
    @Test
    public void testPrintRecordsWithIterable() throws IOException {
        StringWriter out = new StringWriter();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printRecords(Arrays.asList(new Object[] {"Hello", "World"}, new Object[] {"Foo", "Bar"}));
        printer.flush();
        Assert.assertEquals("Hello,World\nFoo,Bar\n", out.toString());
    }

    // Test printRecords() method with array of objects
    @Test
    public void testPrintRecordsWithArray() throws IOException {
        StringWriter out = new StringWriter();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printRecords(new Object[] {"Hello", "World"}, new Object[] {"Foo", "Bar"});
        printer.flush();
        Assert.assertEquals("Hello,World\nFoo,Bar\n", out.toString());
    }

    // Test printRecords() method with ResultSet
    @Test
    public void testPrintRecordsWithResultSet() throws SQLException, IOException {
        StringWriter out = new StringWriter();
        ResultSet resultSet = new MockResultSet();
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT);
        printer.printRecords(resultSet);
        printer.flush();
        Assert.assertEquals("col1,col2\nval1,val2\n", out.toString());
    }

    // Mock ResultSet for testing
    private class MockResultSet implements ResultSet {
        private boolean isFirst = true;

        public boolean next() {
            if (isFirst) {
                isFirst = false;
                return true;
            }
            return false;
        }

        public String getString(int columnIndex) {
            if (isFirst && columnIndex == 1) return "val1";
            if (isFirst && columnIndex == 2) return "val2";
            return null;
        }
        // Other ResultSet methods are not needed for testing
    }

}