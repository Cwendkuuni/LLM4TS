import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import java.io.StringWriter;
import java.io.IOException;

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
    csvPrinter.print(null);

    csvPrinter.flush();

    String expectedOutput = "Hello,123,3.14,";

    assertEquals(expectedOutput, stringWriter.toString());
  }

  @Test
  public void testPrintComment() throws IOException {
    csvPrinter.printComment("This is a comment");

    csvPrinter.flush();

    String expectedOutput = "# This is a comment" + System.lineSeparator();

    assertEquals(expectedOutput, stringWriter.toString());
  }

  @Test
  public void testPrintRecordIterable() throws IOException {
    csvPrinter.printRecord(new Object[]{"A", "B", "C"});
    csvPrinter.printRecord(new Object[]{1, 2, 3});

    csvPrinter.flush();

    String expectedOutput = "A,B,C" + System.lineSeparator() + "1,2,3" + System.lineSeparator();

    assertEquals(expectedOutput, stringWriter.toString());
  }

  @Test
  public void testPrintRecordArray() throws IOException {
    csvPrinter.printRecord("A", "B", "C");
    csvPrinter.printRecord(1, 2, 3);

    csvPrinter.flush();

    String expectedOutput = "A,B,C" + System.lineSeparator() + "1,2,3" + System.lineSeparator();

    assertEquals(expectedOutput, stringWriter.toString());
  }

  @Test
  public void testPrintRecordsIterable() throws IOException {
    csvPrinter.printRecords(new Object[]{"A", "B", "C"});
    csvPrinter.printRecords(new Object[]{1, 2, 3});

    csvPrinter.flush();

    String expectedOutput = "A,B,C" + System.lineSeparator() + "1,2,3" + System.lineSeparator();

    assertEquals(expectedOutput, stringWriter.toString());
  }

  @Test
  public void testPrintRecordsArray() throws IOException {
    csvPrinter.printRecords(new Object[]{"A", "B", "C"});
    csvPrinter.printRecords(new Object[]{1, 2, 3});

    csvPrinter.flush();

    String expectedOutput = "A,B,C" + System.lineSeparator() + "1,2,3" + System.lineSeparator();

    assertEquals(expectedOutput, stringWriter.toString());
  }

  @Test
  public void testPrintRecordsResultSet() throws IOException {
    // Mock a ResultSet
    ResultSet resultSet = new MockResultSet();

    csvPrinter.printRecords(resultSet);

    csvPrinter.flush();

    String expectedOutput = "A,B,C" + System.lineSeparator() + "1,2,3" + System.lineSeparator();

    assertEquals(expectedOutput, stringWriter.toString());
  }

  // Mock ResultSet class for testing
  class MockResultSet implements ResultSet {

    private int currentRow;

    public MockResultSet() {
      this.currentRow = 0;
    }

    @Override
    public boolean next() throws SQLException {
      if (currentRow < 2) {
        currentRow++;
        return true;
      } else {
        return false;
      }
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
      if (currentRow == 1) {
        return "A";
      } else if (currentRow == 2) {
        return "1";
      } else {
        return null;
      }
    }

    // Other ResultSet methods are not needed for testing
  }
}