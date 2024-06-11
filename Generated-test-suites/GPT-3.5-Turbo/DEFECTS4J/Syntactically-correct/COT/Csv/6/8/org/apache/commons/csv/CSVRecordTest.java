import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {
  
  @Test
  public void testGetByEnum() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    String result = record.get(TestEnum.VALUE1);
    assertEquals("value1", result);
  }
  
  @Test
  public void testGetByIndex() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    String result = record.get(0);
    assertEquals("value1", result);
  }
  
  @Test
  public void testGetByName() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    String result = record.get("column1");
    assertEquals("value1", result);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetByNameNoMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    record.get("column1");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetByNameNoMappingIndexOutOfBounds() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    record.get("column3");
  }
  
  @Test
  public void testGetComment() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "This is a comment", 1);
    String result = record.getComment();
    assertEquals("This is a comment", result);
  }
  
  @Test
  public void testGetRecordNumber() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 5);
    long result = record.getRecordNumber();
    assertEquals(5, result);
  }
  
  @Test
  public void testIsConsistent() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    boolean result = record.isConsistent();
    assertTrue(result);
  }
  
  @Test
  public void testIsConsistentWithMapping() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
    boolean result = record.isConsistent();
    assertTrue(result);
  }
  
  @Test
  public void testIsConsistentWithMappingInvalidSize() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
    boolean result = record.isConsistent();
    assertFalse(result);
  }
  
  @Test
  public void testIsMapped() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
    boolean result = record.isMapped("column1");
    assertTrue(result);
  }
  
  @Test
  public void testIsMappedNotMapped() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    boolean result = record.isMapped("column1");
    assertFalse(result);
  }
  
  @Test
  public void testIsSet() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
    boolean result = record.isSet("column1");
    assertTrue(result);
  }
  
  @Test
  public void testIsSetNotMapped() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    boolean result = record.isSet("column1");
    assertFalse(result);
  }
  
  @Test
  public void testIsSetIndexOutOfBounds() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 2);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
    boolean result = record.isSet("column1");
    assertFalse(result);
  }
  
  @Test
  public void testIterator() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    Iterator<String> iterator = record.iterator();
    assertEquals("value1", iterator.next());
    assertEquals("value2", iterator.next());
    assertFalse(iterator.hasNext());
  }
  
  @Test
  public void testPutIn() {
    Map<String, String> map = new HashMap<>();
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    Map<String, String> result = record.putIn(map);
    assertEquals(2, result.size());
    assertEquals("value1", result.get("column1"));
    assertEquals("value2", result.get("column2"));
  }
  
  @Test
  public void testSize() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    int result = record.size();
    assertEquals(2, result);
  }
  
  @Test
  public void testToList() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    List<String> result = record.toList();
    assertEquals(2, result.size());
    assertEquals("value1", result.get(0));
    assertEquals("value2", result.get(1));
  }
  
  @Test
  public void testToMap() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
    Map<String, String> result = record.toMap();
    assertEquals(2, result.size());
    assertEquals("value1", result.get("column1"));
    assertEquals("value2", result.get("column2"));
  }
  
  @Test
  public void testToString() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    String result = record.toString();
    assertEquals("value1, value2", result);
  }
  
  @Test
  public void testValues() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    String[] values = record.values();
    assertArrayEquals(new String[]{"value1", "value2"}, values);
  }
  
  private enum TestEnum {
    VALUE1,
    VALUE2
  }
}