import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.SP;

public class CSVPrinterTest {

    private CSVPrinter csvPrinter;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        CSVFormat format = new CSVFormat(CSVFormat.DEFAULT.withQuoteCharacter('"'));
        outputStream = new ByteArrayOutputStream();
        csvPrinter = new CSVPrinter(new PrintWriter(outputStream), format);
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("Hello, World!");
        Assert.assertEquals("\"Hello, World!\"\n", outputStream.toString());
    }

    @Test
    public void testPrintNullValue() throws IOException {
        csvPrinter.print(null);
        Assert.assertEquals("\"\"\n", outputStream.toString());
        outputStream.reset();
        csvPrinter.format().setNullString("N/A");
        csvPrinter.print(null);
        Assert.assertEquals("\"N/A\"\n", outputStream.toString());
    }

    @Test
    public void testPrintQuotedValue() throws IOException {
        csvPrinter.format().setQuotePolicy(Quote.MINIMAL);
        csvPrinter.print("O'Reilly");
        Assert.assertEquals("\"O'Reilly\"\n", outputStream.toString());
        outputStream.reset();
        csvPrinter.format().setQuotePolicy(Quote.ALL);
        csvPrinter.print("O'Reilly");
        Assert.assertEquals("\"O’Reilly\"\n", outputStream.toString());
    }

    @Test
    public void testPrintEscapedValue() throws IOException {
        csvPrinter.format().setEscapingEnabled(true);
        String value = "line\nwith\nCR\nand\nLF\nand\"quote\"\nand\tab\tand\\backslash\n";
        csvPrinter.print(value);
        String expected = "\"line\nwith\nCR\r\nand\nLF\n\r\nand\"quote\"\nand\tab\tand\\\\backslash\\n\"\n";
        Assert.assertEquals(expected, outputStream.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.format().setCommentingEnabled(true);
        csvPrinter.printComment("This is a comment");
        Assert.assertEquals("\"#This is a comment\"\n", outputStream.toString());
    }

    @Test
    public void testPrintln() throws IOException {
        csvPrinter.print("Hello");
        csvPrinter.println();
        csvPrinter.print("World");
        Assert.assertEquals("\"Hello\"\n\"World\"\n", outputStream.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        Object[] record = {"Hello", "World"};
        csvPrinter.printRecord(record);
        Assert.assertEquals("\"Hello\",\"World\"\n", outputStream.toString());
    }

    @Test
    public void testPrintRecordsIterable() throws IOException, SQLException {
        ResultSet resultSet = createResultSet();
        csvPrinter.printRecords(resultSet);
        Assert.assertEquals(
                "\"name1,age1\"\n\"name2,age2\"\n\"name3,age3\"\n",
                outputStream.toString());
    }

    @Test
    public void testPrintRecordsObject() throws IOException {
        Object[] records = new Object[]{"name1,age1", "name2,age2", "name3,age3"};
        csvPrinter.printRecords(records);
        Assert.assertEquals(
                "\"name1,age1\"\n\"name2,age2\"\n\"name3,age3\"\n",
                outputStream.toString());
    }

    @Test
    public void testPrintRecordsResultSet() throws IOException, SQLException {
        ResultSet resultSet = createResultSet();
        csvPrinter.printRecords(resultSet);
        Assert.assertEquals(
                "\"name1,age1\"\n\"name2,age2\"\n\"name3,age3\"\n",
                outputStream.toString());
    }

    private ResultSet createResultSet() throws SQLException {
        return new ResultSetProxy(new Object[]{"name1", "age1", "name2", "age2", "name3", "age3"});
    }

    private static class ResultSetProxy implements ResultSet {
        private final Object[] data;
        private int index;

        ResultSetProxy(Object[] data) {
            this.data = data;
        }

        @Override
        public boolean next() throws SQLException {
            return index++ < data.length;
        }

        @Override
        public String getString(int columnIndex) throws SQLException {
            return (String) data[columnIndex];
        }

        @Override
        public int getMetaData() throws SQLException {
            throw new UnsupportedOperationException();
        }
    }
}