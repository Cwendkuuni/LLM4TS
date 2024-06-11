import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

  private CSVPrinter csvPrinter;

  @Before
  public void setup() throws IOException {
    this.csvPrinter = new CSVPrinter(null, null);
  }

  @Test
  public void testFlush() throws IOException {
    //TODO: Write test case for flush method
  }

  @Test
  public void testClose() throws IOException {
    //TODO: Write test case for close method
  }

  @Test
  public void testPrint() throws IOException {
    //TODO: Write test case for print method
  }

  @Test
  public void testPrintComment() throws IOException {
    //TODO: Write test case for printComment method
  }

  @Test
  public void testPrintln() throws IOException {
    //TODO: Write test case for println method
  }

  @Test
  public void testPrintRecordIterable() throws IOException {
    //TODO: Write test case for printRecord method with Iterable parameter
  }

  @Test
  public void testPrintRecordObjectArray() throws IOException {
    //TODO: Write test case for printRecord method with Object[] parameter
  }

  @Test
  public void testPrintRecordsIterable() throws IOException {
    //TODO: Write test case for printRecords method with Iterable parameter
  }

  @Test
  public void testPrintRecordsObjectArray() throws IOException {
    //TODO: Write test case for printRecords method with Object[] parameter
  }

  @Test
  public void testPrintRecordsResultSet() throws IOException, SQLException {
    //TODO: Write test case for printRecords method with ResultSet parameter
  }

  @Test
  public void testGetOut() {
    //TODO: Write test case for getOut method
  }

}