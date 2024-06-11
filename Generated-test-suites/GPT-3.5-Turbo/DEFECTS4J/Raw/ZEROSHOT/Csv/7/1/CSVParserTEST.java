import org.junit.Before;
import org.junit.Test;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CSVParserTest {

  private CSVParser csvParser;

  @Before
  public void setUp() {
    String csvData = "name,age,gender\nJohn,30,Male\nJane,25,Female";
    try {
      csvParser = CSVParser.parse(csvData, CSVFormat.DEFAULT);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetRecords() {
    List<CSVRecord> records = null;
    try {
      records = csvParser.getRecords();
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertEquals(2, records.size());
  }

  @Test
  public void testGetHeaderMap() {
    assertNotNull(csvParser.getHeaderMap());
  }

  @Test
  public void testGetCurrentLineNumber() {
    assertEquals(0, csvParser.getCurrentLineNumber());
  }

  @Test
  public void testGetRecordNumber() {
    assertEquals(0, csvParser.getRecordNumber());
  }

  @Test
  public void testIsClosed() {
    assertFalse(csvParser.isClosed());
  }
}