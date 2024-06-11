import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CSVPrinterTest {

  private CSVPrinter csvPrinter;

  @Before
  public void setup() {
    csvPrinter = new CSVPrinter(new StringBuilder(), CSVFormat.DEFAULT);
  }

  @Test
  public void testPrint() throws IOException {
    csvPrinter.print("Hello");
    assertEquals("Hello", csvPrinter.getOut().toString());
  }

  @Test
  public void testPrintNullValue() throws IOException {
    csvPrinter.print(null);
    assertEquals("", csvPrinter.getOut().toString());
  }

  @Test
  public void testPrintAndEscape() throws IOException {
    csvPrinter.print("Hello, World!");
    assertEquals("\"Hello, World!\"", csvPrinter.getOut().toString());
  }

  @Test
  public void testPrintComment() throws IOException {
    csvPrinter.printComment("This is a comment");
    assertEquals("# This is a comment\n", csvPrinter.getOut().toString());
  }

  @Test
  public void testPrintRecord() throws IOException {
    csvPrinter.printRecord("John", "Doe");
    assertEquals("John,Doe\n", csvPrinter.getOut().toString());
  }

  @Test
  public void testPrintRecords() throws IOException {
    csvPrinter.printRecords(new String[] {"John", "Doe"});
    assertEquals("John,Doe\n", csvPrinter.getOut().toString());
  }

  @Test
  public void testPrintRecordsWithIterable() throws IOException {
    csvPrinter.printRecords(Arrays.asList("John", "Doe"));
    assertEquals("John,Doe\n", csvPrinter.getOut().toString());
  }

  @Test
  public void testPrintRecordsWithResultSet() throws SQLException, IOException {
    ResultSet resultSet = createMockResultSet();
    csvPrinter.printRecords(resultSet);
    assertEquals("John,Doe\n", csvPrinter.getOut().toString());
  }

  private ResultSet createMockResultSet() {
    return new ResultSet() {
      private boolean hasNext = true;

      @Override
      public boolean next() throws SQLException {
        boolean hasNext = this.hasNext;
        this.hasNext = false;
        return hasNext;
      }

      @Override
      public String getString(int columnIndex) throws SQLException {
        return "John";
      }

      // Other methods are not implemented for this test
    };
  }

}