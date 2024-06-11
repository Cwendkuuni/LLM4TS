import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

  @Test
  public void testGet() {
    String[] values = {"a", "b", "c"};
    Map<String, Integer> mapping = null;
    String comment = "";
    long recordNumber = 1;
    CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
    
    assertEquals("a", record.get(0));
    assertEquals("b", record.get(1));
    assertEquals("c", record.get(2));
  }
  
  @Test
  public void testGetWithName() {
    String[] values = {"a", "b", "c"};
    Map<String, Integer> mapping = null;
    String comment = "";
    long recordNumber = 1;
    CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
    
    assertNull(record.get("name"));
  }
  
  @Test
  public void testIsConsistent() {
    String[] values = {"a", "b", "c"};
    Map<String, Integer> mapping = null;
    String comment = "";
    long recordNumber = 1;
    CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
    
    assertTrue(record.isConsistent());
  }
  
  @Test
  public void testIsMapped() {
    String[] values = {"a", "b", "c"};
    Map<String, Integer> mapping = null;
    String comment = "";
    long recordNumber = 1;
    CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
    
    assertFalse(record.isMapped("name"));
  }
  
  @Test
  public void testIsSet() {
    String[] values = {"a", "b", "c"};
    Map<String, Integer> mapping = null;
    String comment = "";
    long recordNumber = 1;
    CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
    
    assertFalse(record.isSet("name"));
  }
  
  @Test
  public void testIterator() {
    String[] values = {"a", "b", "c"};
    Map<String, Integer> mapping = null;
    String comment = "";
    long recordNumber = 1;
    CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
    
    int count = 0;
    for (String value : record) {
      count++;
    }
    
    assertEquals(3, count);
  }
  
  @Test
  public void testValues() {
    String[] values = {"a", "b", "c"};
    Map<String, Integer> mapping = null;
    String comment = "";
    long recordNumber = 1;
    CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
    
    assertArrayEquals(values, record.values());
  }
  
  @Test
  public void testGetComment() {
    String[] values = {"a", "b", "c"};
    Map<String, Integer> mapping = null;
    String comment = "this is a comment";
    long recordNumber = 1;
    CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
    
    assertEquals("this is a comment", record.getComment());
  }
  
  @Test
  public void testGetRecordNumber() {
    String[] values = {"a", "b", "c"};
    Map<String, Integer> mapping = null;
    String comment = "";
    long recordNumber = 5;
    CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
    
    assertEquals(5, record.getRecordNumber());
  }
  
  @Test
  public void testSize() {
    String[] values = {"a", "b", "c"};
    Map<String, Integer> mapping = null;
    String comment = "";
    long recordNumber = 1;
    CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
    
    assertEquals(3, record.size());
  }
  
  @Test
  public void testToString() {
    String[] values = {"a", "b", "c"};
    Map<String, Integer> mapping = null;
    String comment = "";
    long recordNumber = 1;
    CSVRecord record = new CSVRecord(values, mapping, comment, recordNumber);
    
    assertEquals("[a, b, c]", record.toString());
  }
}