import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CSVPrinterTest {

    @Test
    public void testPrintNullValue() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(outputStream, CSVFormat.DEFAULT);
        printer.print(null);
        printer.flush();
        String result = outputStream.toString();
        Assert.assertEquals("", result);
    }

    @Test
    public void testPrintEmptyString() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(outputStream, CSVFormat.DEFAULT);
        printer.print("");
        printer.flush();
        String result = outputStream.toString();
        Assert.assertEquals(",", result);
    }

    @Test
    public void testPrintString() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(outputStream, CSVFormat.DEFAULT);
        printer.print("Hello");
        printer.flush();
        String result = outputStream.toString();
        Assert.assertEquals("Hello,", result);
    }

    @Test
    public void testPrintAndEscapeString() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.EXCEL.withDelimiter(';').withEscape('\\');
        CSVPrinter printer = new CSVPrinter(outputStream, format);
        printer.print("Hello; World");
        printer.flush();
        String result = outputStream.toString();
        Assert.assertEquals("\"Hello; World\";", result);
    }

    @Test
    public void testPrintAndQuoteString() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVFormat format = CSVFormat.EXCEL.withDelimiter(';').withQuoteChar('\'');
        CSVPrinter printer = new CSVPrinter(outputStream, format);
        printer.print("Hello");
        printer.flush();
        String result = outputStream.toString();
        Assert.assertEquals("'Hello';", result);
    }

    @Test
    public void testPrintRecordWithIterable() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(outputStream, CSVFormat.DEFAULT);
        printer.printRecord("Hello", "World");
        printer.flush();
        String result = outputStream.toString();
        Assert.assertEquals("Hello,World\r\n", result);
    }

    @Test
    public void testPrintRecordWithArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(outputStream, CSVFormat.DEFAULT);
        printer.printRecord(new String[]{"Hello", "World"});
        printer.flush();
        String result = outputStream.toString();
        Assert.assertEquals("Hello,World\r\n", result);
    }

    @Test
    public void testPrintRecordsWithIterable() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(outputStream, CSVFormat.DEFAULT);
        printer.printRecords(new String[]{"Hello", "World"});
        printer.flush();
        String result = outputStream.toString();
        Assert.assertEquals("Hello,World\r\n", result);
    }

    @Test
    public void testPrintRecordsWithArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(outputStream, CSVFormat.DEFAULT);
        printer.printRecords(new String[]{"Hello", "World"});
        printer.flush();
        String result = outputStream.toString();
        Assert.assertEquals("Hello,World\r\n", result);
    }

    @Test
    public void testPrintRecordsWithResultSet() throws IOException, SQLException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CSVPrinter printer = new CSVPrinter(outputStream, CSVFormat.DEFAULT);
        ResultSet resultSet = getTestDataFromDatabase();
        printer.printRecords(resultSet);
        printer.flush();
        String result = outputStream.toString();
        Assert.assertEquals("1,John Doe\r\n2,Jane Smith\r\n", result);
    }

    public ResultSet getTestDataFromDatabase() {
        // Mock implementation to return test data from database
        return new ResultSet() {
            @Override
            public boolean next() throws SQLException {
                // Returns true if there is more data
                return false;
            }

            @Override
            public String getString(int columnIndex) throws SQLException {
                // Returns the string value at the specified column index
                return null;
            }

            // Other ResultSet methods
        };
    }
}