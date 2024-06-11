import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Before;
import org.junit.Test;

public class CSVPrinterTest {

  private Writer writerMock;
  private CSVFormat csvFormatMock;
  private CSVPrinter csvPrinter;

  @Before
  public void setUp() {
    writerMock = mock(Writer.class);
    csvFormatMock = mock(CSVFormat.class);
    when(csvFormatMock.getDelimiter()).thenReturn(',');
    when(csvFormatMock.getQuoteChar()).thenReturn('\"');
    when(csvFormatMock.getCommentStart()).thenReturn('#');
    when(csvFormatMock.getRecordSeparator()).thenReturn("\n");
    
    csvPrinter = new CSVPrinter(writerMock, csvFormatMock);
  }

  @Test
  public void testClose() throws IOException {
    csvPrinter.close();
    verify(writerMock).close();
  }

  @Test
  public void testFlush() throws IOException {
    csvPrinter.flush();
    verify(writerMock).flush();
  }

  @Test
  public void testPrint() throws IOException {
    csvPrinter.print("value");
    verify(writerMock).append("value");
  }

  @Test
  public void testPrintWithNullValue() throws IOException {
    csvPrinter.print(null);
    verify(writerMock).append("");
  }

  @Test
  public void testPrintComment() throws IOException {
    csvPrinter.printComment("comment");
    verify(writerMock).append("# comment\n");
  }

  @Test
  public void testPrintln() throws IOException {
    csvPrinter.println();
    verify(writerMock).append("\n");
  }

  @Test
  public void testPrintRecordWithIterable() throws IOException {
    csvPrinter.printRecord(new String[]{"value1", "value2"});
    verify(writerMock).append("value1,value2\n");
  }

  @Test
  public void testPrintRecordWithObjectArray() throws IOException {
    csvPrinter.printRecord(new String[]{"value1", "value2"});
    verify(writerMock).append("value1,value2\n");
  }

  @Test
  public void testPrintRecordsWithIterable() throws IOException {
    Iterable<String[]> records = new Iterable<String[]>() {
      @Override
      public Iterator<String[]> iterator() {
        return Arrays.asList(new String[]{"value1", "value2"}, new String[]{"value3", "value4"}).iterator();
      }
    };

    csvPrinter.printRecords(records);
    verify(writerMock).append("value1,value2\nvalue3,value4\n");
  }

  @Test
  public void testPrintRecordsWithObjectArray() throws IOException {
    Object[] records = new Object[]{new String[]{"value1", "value2"}, new String[]{"value3", "value4"}};
    csvPrinter.printRecords(records);
    verify(writerMock).append("value1,value2\nvalue3,value4\n");
  }

  @Test
  public void testPrintRecordsWithResultSet() throws SQLException, IOException {
    ResultSet resultSetMock = mock(ResultSet.class);
    ResultSetMetaData resultSetMetaDataMock = mock(ResultSetMetaData.class);

    when(resultSetMock.next()).thenReturn(true, true, false);
    when(resultSetMock.getMetaData()).thenReturn(resultSetMetaDataMock);
    when(resultSetMetaDataMock.getColumnCount()).thenReturn(2);
    when(resultSetMock.getString(1)).thenReturn("value1");
    when(resultSetMock.getString(2)).thenReturn("value2");

    csvPrinter.printRecords(resultSetMock);

    verify(resultSetMetaDataMock).getColumnCount();
    verify(resultSetMock, times(2)).getString(anyInt());
    verify(writerMock).append("value1,value2\n");
  }

  @Test
  public void testGetOut() {
    assertEquals(writerMock, csvPrinter.getOut());
  }
}