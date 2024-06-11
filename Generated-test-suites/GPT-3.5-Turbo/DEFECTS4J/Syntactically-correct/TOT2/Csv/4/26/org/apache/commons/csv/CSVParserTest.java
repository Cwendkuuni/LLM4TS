import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class CSVParserTest {
  
  @Test
  public void testParseFile() throws IOException {
    File file = new File("test.csv");
    CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
    assertNotNull(parser);
  }
  
  @Test
  public void testParseString() throws IOException {
    String csvString = "name,age\nJohn,25\nAnna,30";
    CSVParser parser = CSVParser.parse(csvString, CSVFormat.DEFAULT);
    assertNotNull(parser);
  }
  
  @Test
  public void testParseURL() throws IOException {
    URL url = new URL("http://example.com/test.csv");
    CSVParser parser = CSVParser.parse(url, Charset.defaultCharset(), CSVFormat.DEFAULT);
    assertNotNull(parser);
  }
  
  @Test
  public void testGetCurrentLineNumber() throws IOException {
    File file = new File("test.csv");
    CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
    assertEquals(0, parser.getCurrentLineNumber());
  }
  
  @Test
  public void testGetHeaderMap() throws IOException {
    File file = new File("test.csv");
    CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
    assertNull(parser.getHeaderMap());
  }
  
  @Test
  public void testGetRecordNumber() throws IOException {
    File file = new File("test.csv");
    CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
    assertEquals(0, parser.getRecordNumber());
  }
  
  @Test
  public void testIterator() throws IOException {
    File file = new File("test.csv");
    CSVParser parser = CSVParser.parse(file, CSVFormat.DEFAULT);
    assertNotNull(parser.iterator());
  }
}