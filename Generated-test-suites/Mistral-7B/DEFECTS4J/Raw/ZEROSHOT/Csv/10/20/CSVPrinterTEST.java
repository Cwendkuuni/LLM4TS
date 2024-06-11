import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.Quote;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CSVPrinterTest {

    private CSVPrinter printer;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        printer = new CSVPrinter(new StringWriter(outputStream), CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        printer.close();
    }

    @Test
    public void testCreatePrintAndClose() throws IOException {
        printer.print("test");
        printer.println();
        printer.close();

        String expected = "test\n";
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testNullInput() throws IOException {
        printer.print(null);
        printer.println();
        printer.close();

        String expected = Constants.EMPTY;
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintOneObject() throws IOException {
        printer.print(1);
        printer.println();
        printer.close();

        String expected = "1\n";
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintObjectAndArray() throws IOException {
        Object[] objects = {"test1", "test2", null, 1, "test3"};

        printer.printRecord(objects);
        printer.println();
        printer.close();

        String expected = "test1,test2,,1,test3\n";
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintIterable() throws IOException {
        String[] array = {"test1", "test2", null, "test4", "test5"};

        printer.printRecords(array);
        printer.println();
        printer.close();

        String expected = "test1,test2,,test4,test5\n";
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintObjectArray() throws IOException {
        Object[] objects = {new Object[]{"test1", "test2", null, 1, "test3"}, {"test4", "test5", null, 1, "test6"}};

        printer.printRecords(objects);
        printer.println();
        printer.close();

        String expected = "test1,test2,,1,test3\n" +
                "test4,test5,,1,test6\n";
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testPrintResultSet() throws SQLException, IOException {
        ResultSet resultSet = ResultSetCreator.createResultSet();
        resultSet.addRow(new Object[]{"test1", "test2", null});
        resultSet.addRow(new Object[]{"test3", "test4", 1});
        resultSet.addRow(new Object[]{"test5", "test6", null});

        printer.printRecords(resultSet);
        printer.println();
        printer.close();

        String expected = "test1,test2,,test5,test6\n" +
                "test3,test4,1,test1,test2\n";
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testQuoting() throws IOException {
        printer.setQuotePolicy(Quote.ALL);
        printer.print("test,test \"test\",test'test' test\"test\n");
        printer.println();
        printer.close();

        String expected = "\"test\",\"test \"test\",\"test'test'\",\"test\"test\n";
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testNoQuoting() throws IOException {
        printer.setQuotePolicy(Quote.NONE);
        printer.print("test,test \"test\",test'test' test\"test\n");
        printer.println();
        printer.close();

        String expected = "test,test \"test\",test'test' test\"test\n";
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testComment() throws IOException {
        printer.setCommentingEnabled(true);
        printer.printComment("#Test Comment");
        printer.println();
        printer.close();

        String expected = "#Test Comment\n";
        String actual = outputStream.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullFormat() throws IOException {
        printer = new CSVPrinter(outputStream, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOutput() throws IOException {
        printer = new CSVPrinter(null, CSVFormat.DEFAULT);
    }

    @Test(expected = IOException.class)
    public void testInvalidFormat() throws IOException {
        printer = new CSVPrinter(outputStream, new CSVFormat() {{
            setRecordSeparator("");
        }});
    }
}