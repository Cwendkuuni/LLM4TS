import static org.junit.Assert.*;
import org.junit.Test;

public class CSVPrinterTest {

    @Test
    public void testConstructor() {
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        assertNotNull(csvPrinter);
    }

    @Test
    public void testPrint() {
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            csvPrinter.print("Value1");
            csvPrinter.print("Value2");
            csvPrinter.print("Value3");
            assertEquals("Value1,Value2,Value3", csvPrinter.getOut().toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testPrintNullValue() {
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            csvPrinter.print(null);
            assertEquals("", csvPrinter.getOut().toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testPrintComment() {
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            csvPrinter.printComment("Comment1");
            assertEquals("# Comment1\n", csvPrinter.getOut().toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testPrintRecord() {
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            csvPrinter.printRecord("Value1", "Value2", "Value3");
            assertEquals("Value1,Value2,Value3\n", csvPrinter.getOut().toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testPrintRecords() {
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            String[] record1 = {"Value1", "Value2", "Value3"};
            String[] record2 = {"Value4", "Value5", "Value6"};
            csvPrinter.printRecords(record1, record2);
            assertEquals("Value1,Value2,Value3\nValue4,Value5,Value6\n", csvPrinter.getOut().toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testPrintRecordsWithIterable() {
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            String[] record1 = {"Value1", "Value2", "Value3"};
            String[] record2 = {"Value4", "Value5", "Value6"};
            Iterable<Object> records = Arrays.asList(record1, record2);
            csvPrinter.printRecords(records);
            assertEquals("Value1,Value2,Value3\nValue4,Value5,Value6\n", csvPrinter.getOut().toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testPrintRecordsWithJDBCResultSet() {
        CSVPrinter csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
        try {
            ResultSet resultSet = createDummyResultSet();
            csvPrinter.printRecords(resultSet);
            assertEquals("Value1,Value2,Value3\n", csvPrinter.getOut().toString());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    private ResultSet createDummyResultSet() {
        ResultSet resultSet = mock(ResultSet.class);
        try {
            when(resultSet.getMetaData().getColumnCount()).thenReturn(3);
            when(resultSet.getString(1)).thenReturn("Value1");
            when(resultSet.getString(2)).thenReturn("Value2");
            when(resultSet.getString(3)).thenReturn("Value3");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}