import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

  @Test
  public void testGetByIndex() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
    
    assertEquals("value1", record.get(0));
    assertEquals("value2", record.get(1));
    assertEquals("value3", record.get(2));
  }
  
  @Test
  public void testGetByName() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
    
    assertNull(record.get("name1"));
    assertNull(record.get("name2"));
    
    record = new CSVRecord(new String[]{"value1", "value2", "value3"}, 
                           Map.of("name1", 0, "name2", 1), null, 0);
    
    assertEquals("value1", record.get("name1"));
    assertEquals("value2", record.get("name2"));
    
    record = new CSVRecord(new String[]{"value1", "value2", "value3"}, 
                           Map.of("name1", 0, "name2", 1, "name3", 2), null, 0);
    
    assertEquals("value1", record.get("name1"));
    assertEquals("value2", record.get("name2"));
    assertNull(record.get("name3"));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetByNameWithoutMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
    record.get("name1");
  }
  
  @Test
  public void testIsConsistent() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
    assertTrue(record.isConsistent());
    
    record = new CSVRecord(new String[]{"value1", "value2", "value3"}, 
                          Map.of("name1", 0, "name2", 1), null, 0);
    assertTrue(record.isConsistent());
    
    record = new CSVRecord(new String[]{"value1", "value2"}, 
                          Map.of("name1", 0, "name2", 1, "name3", 2), null, 0);
    assertFalse(record.isConsistent());
  }
  
  @Test
  public void testIsMapped() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
    
    assertFalse(record.isMapped("name1"));
    assertFalse(record.isMapped("name2"));
    
    record = new CSVRecord(new String[]{"value1", "value2", "value3"}, 
                           Map.of("name1", 0, "name2", 1), null, 0);
    
    assertTrue(record.isMapped("name1"));
    assertTrue(record.isMapped("name2"));
    assertFalse(record.isMapped("name3"));
  }
  
  @Test
  public void testIsSet() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
    
    assertFalse(record.isSet("name1"));
    assertFalse(record.isSet("name2"));
    
    record = new CSVRecord(new String[]{"value1", "value2", "value3"}, 
                           Map.of("name1", 0, "name2", 1), null, 0);
    
    assertTrue(record.isSet("name1"));
    assertTrue(record.isSet("name2"));
    assertFalse(record.isSet("name3"));
  }
  
  @Test
  public void testIterator() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
    
    int count = 0;
    for (String value : record) {
      count++;
    }
    
    assertEquals(record.size(), count);
  }
  
  @Test
  public void testValues() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
    
    assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
  }
  
  @Test
  public void testGetComment() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "comment", 0);
    
    assertEquals("comment", record.getComment());
  }
  
  @Test
  public void testGetRecordNumber() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 123);
    
    assertEquals(123, record.getRecordNumber());
  }
  
  @Test
  public void testSize() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
    
    assertEquals(3, record.size());
  }
  
  @Test
  public void testToString() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 0);
    
    assertEquals("[value1, value2, value3]", record.toString());
  }
}