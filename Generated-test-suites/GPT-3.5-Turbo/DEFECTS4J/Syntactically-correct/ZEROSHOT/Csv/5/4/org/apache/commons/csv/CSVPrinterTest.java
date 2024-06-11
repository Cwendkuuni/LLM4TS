import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVPrinterTest {
  
  private CSVPrinter csvPrinter;
  
  @Before
  public void setup() {
    // Create a mock Appendable object
    Appendable mockOut = new Appendable() {
      StringBuilder sb = new StringBuilder();
      
      public Appendable append(CharSequence csq) throws IOException {
        sb.append(csq);
        return this;
      }
      
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        sb.append(csq, start, end);
        return this;
      }
      
      public Appendable append(char c) throws IOException {
        sb.append(c);
        return this;
      }
      
      public String toString() {
        return sb.toString();
      }
    };
    
    // Create a mock CSVFormat object
    CSVFormat mockFormat = new CSVFormat('\u0001')
        .withDelimiter(',')
        .withQuoteChar('"')
        .withNullString("NULL");
    
    csvPrinter = new CSVPrinter(mockOut, mockFormat);
  }
  
  @Test
  public void testPrint_NullValue_PrintsNullString() throws IOException {
    csvPrinter.print(null);
    String expected = "NULL";
    String actual = csvPrinter.getOut().toString();
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testPrint_QuotingEnabled_PrintsQuotedValue() throws IOException {
    csvPrinter.print("test");
    String expected = "\"test\"";
    String actual = csvPrinter.getOut().toString();
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testPrint_QuotingAndEscapingEnabled_PrintsEscapedValue() throws IOException {
    csvPrinter.print("te\"st");
    String expected = "\"te\"\"st\"";
    String actual = csvPrinter.getOut().toString();
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testPrint_RecordSeparatorAdded() throws IOException {
    csvPrinter.println();
    String expected = "\n";
    String actual = csvPrinter.getOut().toString();
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testPrintComment_CommentAdded() throws IOException {
    csvPrinter.printComment("This is a comment");
    String expected = "# This is a comment\n";
    String actual = csvPrinter.getOut().toString();
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testPrintRecord_IterableValues_RecordPrinted() throws IOException {
    List<String> values = Arrays.asList("A", "B", "C");
    csvPrinter.printRecord(values);
    String expected = "A,B,C\n";
    String actual = csvPrinter.getOut().toString();
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testPrintRecord_VarargsValues_RecordPrinted() throws IOException {
    csvPrinter.printRecord("A", "B", "C");
    String expected = "A,B,C\n";
    String actual = csvPrinter.getOut().toString();
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testPrintRecords_IterableValues_AllRecordsPrinted() throws IOException {
    List<Object> records = new ArrayList<>();
    records.add(Arrays.asList("A", "B", "C"));
    records.add(Arrays.asList("D", "E", "F"));
    
    csvPrinter.printRecords(records);
    String expected = "A,B,C\nD,E,F\n";
    String actual = csvPrinter.getOut().toString();
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testPrintRecords_VarargsValues_AllRecordsPrinted() throws IOException {
    csvPrinter.printRecords(
        Arrays.asList("A", "B", "C"),
        Arrays.asList("D", "E", "F")
    );
    
    String expected = "A,B,C\nD,E,F\n";
    String actual = csvPrinter.getOut().toString();
    Assert.assertEquals(expected, actual);
  }
  
  @Test
  public void testPrintRecords_ResultSet_AllRecordsPrinted() throws IOException {
    // Create a mock ResultSet object
    ResultSet mockResultSet = new ResultSet() {
      String[][] records = {{"A", "B"}, {"C", "D"}};
      int currentRow = -1;
      
      public boolean next() throws SQLException {
        currentRow++;
        return currentRow < records.length;
      }
      
      public String getString(int columnIndex) throws SQLException {
        return records[currentRow][columnIndex - 1];
      }
      
      // Other ResultSet methods are not needed for this test
    };
    
    csvPrinter.printRecords(mockResultSet);
    
    String expected = "A,B\nC,D\n";
    String actual = csvPrinter.getOut().toString();
    Assert.assertEquals(expected, actual);
  }
}