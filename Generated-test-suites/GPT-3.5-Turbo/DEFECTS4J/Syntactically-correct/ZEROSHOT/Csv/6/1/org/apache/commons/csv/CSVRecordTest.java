import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

  @Test
  public void testGet() {
    CSVRecord record = new CSVRecord(null, null, null, 1);
    assertEquals(null, record.get(0));
  }

  @Test
  public void testGetWithEnum() {
    CSVRecord record = new CSVRecord(null, null, null, 1);
    assertEquals(null, record.get(EnumValue.VALUE_1));
  }

  @Test(expected = IllegalStateException.class)
  public void testGetWithNameNoMapping() {
    CSVRecord record = new CSVRecord(null, null, null, 1);
    record.get("column1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetWithNameMappingNotFound() {
    CSVRecord record = new CSVRecord(null, createMapping(), null, 1);
    record.get("column3");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetWithNameArrayIndexOutOfBounds() {
    CSVRecord record = new CSVRecord(new String[]{"value1"}, createMapping(), null, 1);
    record.get("column1");
  }

  @Test
  public void testIsConsistentWithMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1);
    assertTrue(record.isConsistent());
  }

  @Test
  public void testIsConsistentWithoutMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertTrue(record.isConsistent());
  }

  @Test
  public void testIsConsistentWithMappingNotConsistent() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createMappingWithError(), null, 1);
    assertFalse(record.isConsistent());
  }

  @Test
  public void testIsMappedWithMapping() {
    CSVRecord record = new CSVRecord(null, createMapping(), null, 1);
    assertTrue(record.isMapped("column1"));
    assertFalse(record.isMapped("column3"));
  }

  @Test
  public void testIsMappedWithoutMapping() {
    CSVRecord record = new CSVRecord(null, null, null, 1);
    assertFalse(record.isMapped("column1"));
  }

  @Test
  public void testIsSetWithMappedColumn() {
    CSVRecord record = new CSVRecord(new String[]{"value1"}, createMapping(), null, 1);
    assertTrue(record.isSet("column1"));
    assertFalse(record.isSet("column2"));
  }

  @Test
  public void testIsSetWithoutMappedColumn() {
    CSVRecord record = new CSVRecord(null, null, null, 1);
    assertFalse(record.isSet("column1"));
  }

  @Test
  public void testGetComment() {
    CSVRecord record = new CSVRecord(null, null, "Comment", 1);
    assertEquals("Comment", record.getComment());
  }

  @Test
  public void testGetRecordNumber() {
    CSVRecord record = new CSVRecord(null, null, null, 1);
    assertEquals(1, record.getRecordNumber());
  }

  @Test
  public void testSize() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals(2, record.size());
  }

  @Test
  public void testToList() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals(java.util.Arrays.asList(new String[]{"value1", "value2"}), record.toList());
  }

  @Test
  public void testToMap() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, createMapping(), null, 1);
    assertEquals(createExpectedMap(), record.toMap());
  }

  @Test
  public void testToString() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals("[value1, value2]", record.toString());
  }

  private Map<String, Integer> createMapping() {
    Map<String, Integer> mapping = new java.util.HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    return mapping;
  }

  private Map<String, Integer> createMappingWithError() {
    Map<String, Integer> mapping = new java.util.HashMap<>();
    mapping.put("column1", 0);
    return mapping;
  }

  private Map<String, String> createExpectedMap() {
    Map<String, String> map = new java.util.HashMap<>();
    map.put("column1", "value1");
    map.put("column2", "value2");
    return map;
  }

  private enum EnumValue {
    VALUE_1
  }
}