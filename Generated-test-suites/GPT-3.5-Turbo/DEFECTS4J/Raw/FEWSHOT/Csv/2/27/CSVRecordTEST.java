import static org.junit.Assert.*;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public class CSVRecordTest {
  @Test
  public void testGet() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);
  
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
    
    assertEquals("value1", record.get(0));
    assertEquals("value2", record.get(1));
    assertEquals("value3", record.get(2));
  }
  
  @Test
  public void testGetByName() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);
  
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
    
    assertEquals("value1", record.get("column1"));
    assertEquals("value2", record.get("column2"));
    assertEquals("value3", record.get("column3"));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetByNameNoMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
    
    record.get("column1");
  }
  
  @Test
  public void testIsConsistent() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);
  
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
    
    assertTrue(record.isConsistent());
  }
  
  @Test
  public void testIsConsistentNoMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
    
    assertTrue(record.isConsistent());
  }
  
  @Test
  public void testIsMapped() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);
  
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
    
    assertTrue(record.isMapped("column1"));
    assertTrue(record.isMapped("column2"));
    assertTrue(record.isMapped("column3"));
    assertFalse(record.isMapped("column4"));
  }
  
  @Test
  public void testIsSet() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);
  
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
    
    assertTrue(record.isSet("column1"));
    assertTrue(record.isSet("column2"));
    assertTrue(record.isSet("column3"));
    assertFalse(record.isSet("column4"));
  }
  
  @Test
  public void testIterator() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);
  
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
    
    Iterator<String> iterator = record.iterator();
    
    assertTrue(iterator.hasNext());
    assertEquals("value1", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("value2", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("value3", iterator.next());
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testValues() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);
  
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
    
    assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
  }
  
  @Test
  public void testGetComment() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);
  
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, "This is a comment", 1);
    
    assertEquals("This is a comment", record.getComment());
  }
  
  @Test
  public void testGetRecordNumber() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);
  
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 5);
    
    assertEquals(5, record.getRecordNumber());
  }
  
  @Test
  public void testSize() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);
  
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
    
    assertEquals(3, record.size());
  }
  
  @Test
  public void testToString() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);
  
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, null, 1);
    
    assertEquals("[value1, value2, value3]", record.toString());
  }
}