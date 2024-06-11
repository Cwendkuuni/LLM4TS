import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

    private StringWriter writer;
    private CSVPrinter csvPrinter;

    @Before
    public void setUp() throws IOException {
        writer = new StringWriter();
        csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
    }

    @Test
    public void testPrint() throws IOException {
        csvPrinter.print("Value1");
        csvPrinter.print("Value2");
        assertEquals("Value1,Value2", writer.toString());
    }

    @Test
    public void testPrintComment() throws IOException {
        csvPrinter.printComment("This is a comment");
        assertEquals("# This is a comment\n", writer.toString());
    }

    @Test
    public void testPrintRecord() throws IOException {
        csvPrinter.printRecord("ValueA", "ValueB", "ValueC");
        assertEquals("ValueA,ValueB,ValueC\n", writer.toString());
    }

    @Test
    public void testPrintRecords() throws IOException {
        Object[] record1 = {"Record1Value1", "Record1Value2"};
        Object[] record2 = {"Record2Value1", "Record2Value2"};
        csvPrinter.printRecords(new Object[]{record1, record2});
        assertEquals("Record1Value1,Record1Value2\nRecord2Value1,Record2Value2\n", writer.toString());
    }

    @Test
    public void testPrintRecordsFromResultSet() throws IOException {
        // Assuming resultSet has two columns: Column1, Column2
        // Data: Row1: A, B - Row2: C, D
        // Mock ResultSet for testing purposes
        MockResultSet resultSet = new MockResultSet();
        resultSet.addRow("A", "B");
        resultSet.addRow("C", "D");

        csvPrinter.printRecords(resultSet);
        assertEquals("A,B\nC,D\n", writer.toString());
    }

    @Test
    public void testFlush() throws IOException {
        csvPrinter.flush();
        // Since StringWriter doesn't have actual flush logic, no assertion is made, just ensuring no exceptions are thrown
    }

    @Test
    public void testClose() throws IOException {
        csvPrinter.close();
        // Since StringWriter doesn't have actual close logic, no assertion is made, just ensuring no exceptions are thrown
    }

    // Mock ResultSet class for testing printRecords from ResultSet
    private class MockResultSet {
        private Object[][] rows;
        private int currentRowIndex;

        public MockResultSet() {
            rows = new Object[0][0];
            currentRowIndex = -1;
        }

        public void addRow(Object... rowData) {
            Object[][] newRows = new Object[rows.length + 1][rowData.length];
            System.arraycopy(rows, 0, newRows, 0, rows.length);
            newRows[rows.length] = rowData;
            rows = newRows;
        }

        public boolean next() {
            currentRowIndex++;
            return currentRowIndex < rows.length;
        }

        public String getString(int columnIndex) {
            return (String) rows[currentRowIndex][columnIndex - 1];
        }
    }
}