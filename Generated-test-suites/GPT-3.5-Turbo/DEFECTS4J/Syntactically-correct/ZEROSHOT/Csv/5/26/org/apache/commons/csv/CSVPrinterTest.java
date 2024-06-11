import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CSVPrinterTest {

  @Mock
  private Appendable out;

  private CSVFormat format;
  private CSVPrinter csvPrinter;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    format = CSVFormat.DEFAULT;
    csvPrinter = new CSVPrinter(out, format);
  }

  @Test
  public void testPrint() throws Exception {
    csvPrinter.print("Hello");
    verify(out, times(1)).append("Hello");
  }

  @Test
  public void testPrintNull() throws Exception {
    csvPrinter.print(null);
    verify(out, times(1)).append("");
  }

  @Test
  public void testPrintAndQuote() throws Exception {
    csvPrinter.printAndQuote("Hello");
    verify(out, times(1)).append("\"Hello\"");
  }

  @Test
  public void testPrintAndEscape() throws Exception {
    csvPrinter.printAndEscape("Hello");
    verify(out, times(1)).append("Hello");
  }

  @Test
  public void testPrintComment() throws Exception {
    csvPrinter.printComment("This is a comment");
    verify(out, times(1)).append("# This is a comment\n");
  }

  @Test
  public void testPrintln() throws Exception {
    csvPrinter.println();
    verify(out, times(1)).append("\n");
  }

  @Test
  public void testPrintRecord() throws Exception {
    Object[] values = {"Value1", "Value2", "Value3"};
    csvPrinter.printRecord(values);
    verify(out, times(1)).append("Value1,Value2,Value3\n");
  }

  @Test
  public void testPrintRecords() throws Exception {
    Iterable<Object> values = new ArrayList<>();
    values.add("Value1");
    values.add("Value2");
    values.add("Value3");
    csvPrinter.printRecords(values);
    verify(out, times(1)).append("Value1,Value2,Value3\n");
  }

  @Test
  public void testPrintRecordsArray() throws Exception {
    Object[] values = {{"Value1", "Value2", "Value3"}, {"Value4", "Value5", "Value6"}};
    csvPrinter.printRecords(values);
    verify(out, times(1)).append("Value1,Value2,Value3\n");
    verify(out, times(1)).append("Value4,Value5,Value6\n");
  }

  @Test
  public void testPrintRecordsResultSet() throws Exception {
    ResultSet resultSet = mock(ResultSet.class);
    when(resultSet.getMetaData().getColumnCount()).thenReturn(3);
    when(resultSet.next()).thenReturn(true, false);
    when(resultSet.getString(1)).thenReturn("Value1");
    when(resultSet.getString(2)).thenReturn("Value2");
    when(resultSet.getString(3)).thenReturn("Value3");

    csvPrinter.printRecords(resultSet);
    verify(resultSet, times(2)).next();
    verify(resultSet, times(2)).getString(anyInt());
    verify(out, times(1)).append("Value1,Value2,Value3\n");
  }

  @Test
  public void testGetOut() throws Exception {
    assertEquals(out, csvPrinter.getOut());
  }

  @Test
  public void testClose() throws Exception {
    Closeable closeable = mock(Closeable.class);
    csvPrinter.close();
    verify(closeable, times(1)).close();
  }

  @Test
  public void testFlush() throws Exception {
    Flushable flushable = mock(Flushable.class);
    csvPrinter.flush();
    verify(flushable, times(1)).flush();
  }

}