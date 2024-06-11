import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {
  
  private CSVPrinter csvPrinter;

  @Before
  public void setUp() {
    csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
  }

  // Test case for print method with null value
  @Test
  public void testPrintNullValue() throws IOException {
    csvPrinter.print(null);
    assertEquals("", csvPrinter.getOut().toString());
  }

  // Test case for print method with non-null value
  @Test
  public void testPrintNonNullValue() throws IOException {
    csvPrinter.print("test");
    assertEquals("test", csvPrinter.getOut().toString());
  }

  // Test case for printComment method with empty comment
  @Test
  public void testPrintEmptyComment() throws IOException {
    csvPrinter.printComment("");
    assertEquals("", csvPrinter.getOut().toString());
  }

  // Test case for printComment method with non-empty comment
  @Test
  public void testPrintNonEmptyComment() throws IOException {
    csvPrinter.printComment("Sample comment");
    assertEquals("# Sample comment\n", csvPrinter.getOut().toString());
  }

  // Test case for println method
  @Test
  public void testPrintln() throws IOException {
    csvPrinter.println();
    assertEquals("\n", csvPrinter.getOut().toString());
  }

  // Test case for printRecord method with single value
  @Test
  public void testPrintRecordWithSingleValue() throws IOException {
    csvPrinter.printRecord("value");
    assertEquals("value\n", csvPrinter.getOut().toString());
  }

  // Test case for printRecord method with multiple values as array
  @Test
  public void testPrintRecordWithArrayValues() throws IOException {
    csvPrinter.printRecord(new String[]{"value1", "value2", "value3"});
    assertEquals("value1,value2,value3\n", csvPrinter.getOut().toString());
  }

  // Test case for printRecord method with multiple values as iterable
  @Test
  public void testPrintRecordWithIterableValues() throws IOException {
    csvPrinter.printRecord(List.of("value1", "value2", "value3"));
    assertEquals("value1,value2,value3\n", csvPrinter.getOut().toString());
  }

  // Test case for printRecords method with Iterable of array objects
  @Test
  public void testPrintRecordsWithIterableOfArrayObjects() throws IOException {
    csvPrinter.printRecords(List.of(new String[]{"value1", "value2"}, new String[]{"value3", "value4"}));
    assertEquals("value1,value2\nvalue3,value4\n", csvPrinter.getOut().toString());
  }

  // Test case for printRecords method with array of array objects
  @Test
  public void testPrintRecordsWithArrayOfArrayObjects() throws IOException {
    csvPrinter.printRecords(new Object[]{new String[]{"value1", "value2"}, new String[]{"value3", "value4"}});
    assertEquals("value1,value2\nvalue3,value4\n", csvPrinter.getOut().toString());
  }

  // Test case for printRecords method with ResultSet
  @Test
  public void testPrintRecordsWithResultSet() throws SQLException, IOException {
    ResultSet resultSet = getTestResultSet();
    csvPrinter.printRecords(resultSet);
    assertEquals("value1,value2,value3\nvalue4,value5,value6\n", csvPrinter.getOut().toString());
  }

  // Helper method to create test ResultSet
  private ResultSet getTestResultSet() {
    // create a stub ResultSet for testing
    ResultSet resultSet = Mockito.mock(ResultSet.class);
    try {
      Mockito.when(resultSet.getMetaData().getColumnCount()).thenReturn(3);
      Mockito.when(resultSet.next()).thenReturn(true, false);
      Mockito.when(resultSet.getString(1)).thenReturn("value1");
      Mockito.when(resultSet.getString(2)).thenReturn("value2");
      Mockito.when(resultSet.getString(3)).thenReturn("value3");
    } catch(SQLException e) {
      e.printStackTrace();
    }
    return resultSet;
  }

}