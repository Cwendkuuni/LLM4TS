package org.apache.commons.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CSVRecordTest {

  @Test
  public void testGet() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    CSVRecord record = new CSVRecord(values, null, null, 1);
    
    assertEquals("Value1", record.get(0));
    assertEquals("Value2", record.get(1));
    assertEquals("Value3", record.get(2));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetWithNoMapping() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    CSVRecord record = new CSVRecord(values, null, null, 1);
    
    record.get("Column1");
  }
  
  @Test
  public void testGetWithValidMapping() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    CSVRecord record = new CSVRecord(values, null, null, 1);
    record.get("Column1");
    //Test passes if no exception is thrown
  }
  
  @Test
  public void testGetWithInvalidMapping() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    CSVRecord record = new CSVRecord(values, null, null, 1);
    
    assertNull(record.get("Column4"));
  }
  
  @Test
  public void testIsConsistentWithNoMapping() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    CSVRecord record = new CSVRecord(values, null, null, 1);
    
    assertTrue(record.isConsistent());
  }
  
  @Test
  public void testIsConsistentWithValidMapping() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("Column1", 0);
    mapping.put("Column2", 1);
    mapping.put("Column3", 2);
    CSVRecord record = new CSVRecord(values, mapping, null, 1);
    
    assertTrue(record.isConsistent());
  }
  
  @Test
  public void testIsConsistentWithInvalidMapping() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("Column1", 0);
    mapping.put("Column2", 1);
    mapping.put("Column3", 2);
    CSVRecord record = new CSVRecord(values, mapping, null, 1);
    
    assertFalse(record.isConsistent());
  }
  
  @Test
  public void testIsMappedWithValidMapping() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("Column1", 0);
    mapping.put("Column2", 1);
    mapping.put("Column3", 2);
    CSVRecord record = new CSVRecord(values, mapping, null, 1);
    
    assertTrue(record.isMapped("Column1"));
    assertTrue(record.isMapped("Column2"));
    assertTrue(record.isMapped("Column3"));
  }
  
  @Test
  public void testIsMappedWithInvalidMapping() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("Column1", 0);
    mapping.put("Column2", 1);
    mapping.put("Column3", 2);
    CSVRecord record = new CSVRecord(values, mapping, null, 1);
    
    assertFalse(record.isMapped("Column4"));
  }
  
  @Test
  public void testIsSetWithMappedColumn() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("Column1", 0);
    mapping.put("Column2", 1);
    mapping.put("Column3", 2);
    CSVRecord record = new CSVRecord(values, mapping, null, 1);
    
    assertTrue(record.isSet("Column1"));
    assertTrue(record.isSet("Column2"));
    assertTrue(record.isSet("Column3"));
  }
  
  @Test
  public void testIsSetWithUnmappedColumn() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    CSVRecord record = new CSVRecord(values, null, null, 1);
    
    assertFalse(record.isSet("Column1"));
  }
  
  @Test
  public void testIterator() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    CSVRecord record = new CSVRecord(values, null, null, 1);
    
    Iterator<String> iterator = record.iterator();
    
    assertNotNull(iterator);
    assertTrue(iterator.hasNext());
    assertEquals("Value1", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("Value2", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("Value3", iterator.next());
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testValues() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    CSVRecord record = new CSVRecord(values, null, null, 1);
    
    String[] result = record.values();
    
    assertNotNull(result);
    assertEquals(3, result.length);
    assertEquals("Value1", result[0]);
    assertEquals("Value2", result[1]);
    assertEquals("Value3", result[2]);
  }
  
  @Test
  public void testGetCommentWithComment() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    CSVRecord record = new CSVRecord(values, null, "Comment", 1);
    
    String comment = record.getComment();
    
    assertNotNull(comment);
    assertEquals("Comment", comment);
  }
  
  @Test
  public void testGetCommentWithoutComment() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    CSVRecord record = new CSVRecord(values, null, null, 1);
    
    String comment = record.getComment();
    
    assertNull(comment);
  }
  
  @Test
  public void testGetRecordNumber() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    CSVRecord record = new CSVRecord(values, null, null, 5);
    
    long recordNumber = record.getRecordNumber();
    
    assertEquals(5, recordNumber);
  }
  
  @Test
  public void testSize() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    CSVRecord record = new CSVRecord(values, null, null, 1);
    
    int size = record.size();
    
    assertEquals(3, size);
  }
  
  @Test
  public void testToString() {
    String[] values = new String[]{"Value1", "Value2", "Value3"};
    CSVRecord record = new CSVRecord(values, null, null, 1);
    
    String result = record.toString();
    
    assertEquals("[Value1, Value2, Value3]", result);
  }

}