import org.junit.Test;
import org.junit.Before;
import org.apache.commons.csv.*;
import static org.junit.Assert.*;

public class CSVParserTest {
  
  private CSVParser parser;
  
  @Before
  public void setUp() throws Exception {
    String csvData = "name,age,city\nJohn,25,New York\nAlice,30,San Francisco";
    this.parser = CSVParser.parse(csvData, CSVFormat.DEFAULT);
  }
  
  @Test
  public void testGetCurrentLineNumber() throws Exception {
    assertEquals(1, this.parser.getCurrentLineNumber());
    this.parser.nextRecord();
    assertEquals(2, this.parser.getCurrentLineNumber());
  }
  
  @Test
  public void testGetHeaderMap() throws Exception {
    assertNull(this.parser.getHeaderMap());
  }
  
  @Test
  public void testGetRecordNumber() throws Exception {
    assertEquals(0, this.parser.getRecordNumber());
    this.parser.nextRecord();
    assertEquals(1, this.parser.getRecordNumber());
  }
  
  @Test
  public void testGetRecords() throws Exception {
    assertEquals(2, this.parser.getRecords().size());
  }
  
  @Test
  public void testIsClosed() throws Exception {
    assertFalse(this.parser.isClosed());
    this.parser.close();
    assertTrue(this.parser.isClosed());
  }
  
  @Test(expected=NoSuchElementException.class)
  public void testIterator() throws Exception {
    assertFalse(this.parser.isClosed());
    assertEquals(2, this.parser.iterator().forEachRemaining(CSVRecord::new));
    assertFalse(this.parser.isClosed());
    
    // Test NoSuchElementException is thrown when the parser is closed
    this.parser.close();
    Iterator<CSVRecord> iterator = this.parser.iterator();
    assertFalse(iterator.hasNext());
    iterator.next();
  }
  
}