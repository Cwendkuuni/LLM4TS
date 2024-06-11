import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class CSVPrinterTest {

  private StringWriter stringWriter;
  private CSVPrinter csvPrinter;

  @Before
  public void setup() throws IOException {
    stringWriter = new StringWriter();
    csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT);
  }

  @After
  public void tearDown() throws IOException {
    csvPrinter.close();
    stringWriter.close();
  }

  @Test
  public void testPrint_StringValue_PrintsCorrectOutput() throws IOException {
    csvPrinter.print("Hello");
    csvPrinter.flush();
    assertEquals("Hello", stringWriter.toString());
  }

  @Test
  public void testPrint_NullValue_PrintsNullString() throws IOException {
    csvPrinter.print(null);
    csvPrinter.flush();
    assertEquals("", stringWriter.toString());
  }

  @Test
  public void testPrint_IntValue_PrintsIntegerAsString() throws IOException {
    csvPrinter.print(10);
    csvPrinter.flush();
    assertEquals("10", stringWriter.toString());
  }

  @Test
  public void testPrint_ObjectValue_PrintsToString() throws IOException {
    Object obj = new Object() {
      public String toString() {
        return "Custom Object";
      }
    };
    csvPrinter.print(obj);
    csvPrinter.flush();
    assertEquals("Custom Object", stringWriter.toString());
  }

  @Test
  public void testPrintComment_CommentEnabled_PrintsCommentLine() throws IOException {
    csvPrinter.printComment("This is a comment");
    csvPrinter.flush();
    assertEquals("# This is a comment\n", stringWriter.toString());
  }

  @Test
  public void testPrintComment_CommentDisabled_NoOutput() throws IOException {
    csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withCommentEnabled(false));
    csvPrinter.printComment("This is a comment");
    csvPrinter.flush();
    assertEquals("", stringWriter.toString());
  }

  @Test
  public void testPrintRecord_ObjectArrayValues_PrintsCorrectOutput() throws IOException {
    Object[] values = new Object[] { "John", "Doe", 30 };
    csvPrinter.printRecord(values);
    csvPrinter.flush();
    assertEquals("John,Doe,30\n", stringWriter.toString());
  }

  @Test
  public void testPrintRecord_IterableValues_PrintsCorrectOutput() throws IOException {
    Iterable<Integer> values = new ArrayList<>(Arrays.asList(1, 2, 3));
    csvPrinter.printRecord(values);
    csvPrinter.flush();
    assertEquals("1,2,3\n", stringWriter.toString());
  }

  @Test
  public void testPrintRecords_ObjectArrayValues_PrintsCorrectOutput() throws IOException {
    Object[] values1 = new Object[] { "John", "Doe", 30 };
    Object[] values2 = new Object[] { "Jane", "Smith", 25 };
    csvPrinter.printRecords(new Object[] { values1, values2 });
    csvPrinter.flush();
    assertEquals("John,Doe,30\nJane,Smith,25\n", stringWriter.toString());
  }

  @Test
  public void testPrintRecords_IterableValues_PrintsCorrectOutput() throws IOException {
    Iterable<Iterable<?>> values = new ArrayList<>();
    values.add(Arrays.asList("John", "Doe", 30));
    values.add(Arrays.asList("Jane", "Smith", 25));
    csvPrinter.printRecords(values);
    csvPrinter.flush();
    assertEquals("John,Doe,30\nJane,Smith,25\n", stringWriter.toString());
  }

  @Test
  public void testPrintRecords_ResultSetValues_PrintsCorrectOutput() throws IOException, SQLException {
    ResultSet resultSet = mock(ResultSet.class);
    when(resultSet.getMetaData()).thenReturn(mock(ResultSetMetaData.class));
    when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
    when(resultSet.getString(1)).thenReturn("John");
    when(resultSet.getString(2)).thenReturn("Doe");
    when(resultSet.getInt(3)).thenReturn(30);
    csvPrinter.printRecords(resultSet);
    csvPrinter.flush();
    assertEquals("John,Doe,30\nJohn,Doe,30\n", stringWriter.toString());
  }

}