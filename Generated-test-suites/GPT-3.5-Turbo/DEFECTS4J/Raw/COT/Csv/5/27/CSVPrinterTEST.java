
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {
    private CSVPrinter csvPrinter;
    private StringWriter stringWriter;

    @Before
    public void setUp() {
        stringWriter = new StringWriter();
        csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
    }

    @After
    public void tearDown() throws IOException {
        csvPrinter.close();
        stringWriter.close();
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("Hello");
        csvPrinter.print(123);
        csvPrinter.print(3.14);
        csvPrinter.flush();

        assertEquals("Hello,123,3.14", stringWriter.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        csvPrinter.flush();

        assertEquals("# This is a comment\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("Hello", 123, 3.14);
        csvPrinter.flush();

        assertEquals("Hello,123,3.14\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        Object[] record1 = {"Hello", 123, 3.14};
        Object[] record2 = {"World", 456, 6.28};

        csvPrinter.printRecords(new Object[]{record1, record2});
        csvPrinter.flush();

        assertEquals("Hello,123,3.14\nWorld,456,6.28\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsIterable() throws IOException {
        Object[] record1 = {"Hello", 123, 3.14};
        Object[] record2 = {"World", 456, 6.28};

        csvPrinter.printRecords(new Object[]{record1});
        csvPrinter.printRecords(new Object[]{record2});
        csvPrinter.flush();

        assertEquals("Hello,123,3.14\nWorld,456,6.28\n", stringWriter.toString());
    }

    @Test
    public void testPrintRecordsResultSet() throws IOException {
        // Create a dummy ResultSet
        DummyResultSet resultSet = new DummyResultSet();
        resultSet.addColumn(1, "John");
        resultSet.addColumn(2, 25);
        resultSet.addColumn(3, 180.5);

        csvPrinter.printRecords(resultSet);
        csvPrinter.flush();

        assertEquals("John,25,180.5\n", stringWriter.toString());
    }

    @Test
    public void testGetOut() {
        assertEquals(stringWriter, csvPrinter.getOut());
    }
}
