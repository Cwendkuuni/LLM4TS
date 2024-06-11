import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {
  
  @Test
  public void testGetValueByEnum() {
    CSVRecord csvRecord = new CSVRecord(null, null, null, 1);
    String value = csvRecord.get(TestEnum.VALUE_1);
    assertNull(value);
  }
  
  @Test
  public void testGetValueByIndex() {
    String[] values = {"value1", "value2", "value3"};
    CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
    String value = csvRecord.get(1);
    assertEquals("value2", value);
  }
  
  @Test
  public void testGetValueByName() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    mapping.put("name2", 1);
    mapping.put("name3", 2);
    CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
    String value = csvRecord.get("name2");
    assertEquals("value2", value);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetValueByName_noMapping() {
    String[] values = {"value1", "value2", "value3"};
    CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
    csvRecord.get("name2");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetValueByName_noMappingFound() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
    csvRecord.get("name2");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetValueByName_indexOutOfBounds() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    mapping.put("name2", 1);
    mapping.put("name3", 2);
    CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
    csvRecord.get("name4");
  }
  
  @Test
  public void testGetComment() {
    CSVRecord csvRecord = new CSVRecord(null, null, "This is a comment.", 1);
    String comment = csvRecord.getComment();
    assertEquals("This is a comment.", comment);
  }
  
  @Test
  public void testGetRecordNumber() {
    CSVRecord csvRecord = new CSVRecord(null, null, null, 1);
    long recordNumber = csvRecord.getRecordNumber();
    assertEquals(1, recordNumber);
  }
  
  @Test
  public void testIsConsistent_noMapping() {
    String[] values = {"value1", "value2", "value3"};
    CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
    boolean isConsistent = csvRecord.isConsistent();
    assertTrue(isConsistent);
  }
  
  @Test
  public void testIsConsistent_mappingSizeMismatch() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
    boolean isConsistent = csvRecord.isConsistent();
    assertFalse(isConsistent);
  }
  
  @Test
  public void testIsConsistent_mappingSizeMatch() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    mapping.put("name2", 1);
    mapping.put("name3", 2);
    CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
    boolean isConsistent = csvRecord.isConsistent();
    assertTrue(isConsistent);
  }
  
  @Test
  public void testIsMapped() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    mapping.put("name2", 1);
    mapping.put("name3", 2);
    CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
    boolean isMapped = csvRecord.isMapped("name2");
    assertTrue(isMapped);
  }
  
  @Test
  public void testIsMapped_notMapped() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    mapping.put("name2", 1);
    mapping.put("name3", 2);
    CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
    boolean isMapped = csvRecord.isMapped("name4");
    assertFalse(isMapped);
  }
  
  @Test
  public void testIsSet() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    mapping.put("name2", 1);
    mapping.put("name3", 2);
    CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
    boolean isSet = csvRecord.isSet("name2");
    assertTrue(isSet);
  }
  
  @Test
  public void testIsSet_notSet() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
    boolean isSet = csvRecord.isSet("name2");
    assertFalse(isSet);
  }
  
  @Test
  public void testIterator() {
    String[] values = {"value1", "value2", "value3"};
    CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
    Iterator<String> iterator = csvRecord.iterator();
    assertNotNull(iterator);
    assertTrue(iterator.hasNext());
    assertEquals("value1", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("value2", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("value3", iterator.next());
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testPutIn() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    mapping.put("name2", 1);
    mapping.put("name3", 2);
    CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
    Map<String, String> map = new HashMap<>();
    csvRecord.putIn(map);
    assertEquals(3, map.size());
    assertEquals("value1", map.get("name1"));
    assertEquals("value2", map.get("name2"));
    assertEquals("value3", map.get("name3"));
  }
  
  @Test
  public void testSize() {
    String[] values = {"value1", "value2", "value3"};
    CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
    int size = csvRecord.size();
    assertEquals(3, size);
  }
  
  @Test
  public void testToList() {
    String[] values = {"value1", "value2", "value3"};
    CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
    List<String> list = csvRecord.toList();
    assertNotNull(list);
    assertEquals(3, list.size());
    assertEquals("value1", list.get(0));
    assertEquals("value2", list.get(1));
    assertEquals("value3", list.get(2));
  }
  
  @Test
  public void testToMap() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    mapping.put("name2", 1);
    mapping.put("name3", 2);
    CSVRecord csvRecord = new CSVRecord(values, mapping, null, 1);
    Map<String, String> map = csvRecord.toMap();
    assertEquals(3, map.size());
    assertEquals("value1", map.get("name1"));
    assertEquals("value2", map.get("name2"));
    assertEquals("value3", map.get("name3"));
  }
  
  @Test
  public void testToString() {
    String[] values = {"value1", "value2", "value3"};
    CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
    String recordString = csvRecord.toString();
    assertEquals("[value1, value2, value3]", recordString);
  }
  
  @Test
  public void testValues() {
    String[] values = {"value1", "value2", "value3"};
    CSVRecord csvRecord = new CSVRecord(values, null, null, 1);
    String[] recordValues = csvRecord.values();
    assertNotNull(recordValues);
    assertEquals(3, recordValues.length);
    assertEquals("value1", recordValues[0]);
    assertEquals("value2", recordValues[1]);
    assertEquals("value3", recordValues[2]);
  }
  
  private enum TestEnum {
    VALUE_1,
    VALUE_2
  }
}