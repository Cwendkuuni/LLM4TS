import static org.junit.Assert.*;
import org.junit.Test;

public class CSVRecordTest {

  @Test
  public void testGetEnum() {
      CSVRecord record = new CSVRecord(null, null, null, 1L);
      assertNull(record.get(TestEnum.VALUE1));
  }

  @Test
  public void testGetIndex() {
      String[] values = {"value1", "value2", "value3"};
      CSVRecord record = new CSVRecord(values, null, null, 1L);
      assertEquals("value1", record.get(0));
  }

  @Test(expected=IllegalStateException.class)
  public void testGetNameWithoutMapping() {
      CSVRecord record = new CSVRecord(null, null, null, 1L);
      record.get("name");
  }

  @Test(expected=IllegalArgumentException.class)
  public void testGetNameWithInvalidMapping() {
      String[] values = {"value1", "value2", "value3"};
      CSVRecord record = new CSVRecord(values, null, null, 1L);
      record.get("name");
  }

  @Test(expected=IllegalArgumentException.class)
  public void testGetNameWithInvalidIndex() {
      String[] values = {"value1", "value2", "value3"};
      Map<String, Integer> mapping = new HashMap<>();
      mapping.put("name1", 0);
      CSVRecord record = new CSVRecord(values, mapping, null, 1L);
      record.get("name2");
  }

  @Test
  public void testGetComment() {
      CSVRecord record = new CSVRecord(null, null, "comment", 1L);
      assertEquals("comment", record.getComment());
  }

  @Test
  public void testGetRecordNumber() {
      CSVRecord record = new CSVRecord(null, null, null, 10L);
      assertEquals(10L, record.getRecordNumber());
  }

  @Test
  public void testIsConsistentWithoutMapping() {
      CSVRecord record = new CSVRecord(null, null, null, 1L);
      assertTrue(record.isConsistent());
  }

  @Test
  public void testIsConsistentWithMappingAndMatchingSize() {
      String[] values = {"value1", "value2", "value3"};
      Map<String, Integer> mapping = new HashMap<>();
      mapping.put("name1", 0);
      mapping.put("name2", 1);
      mapping.put("name3", 2);
      CSVRecord record = new CSVRecord(values, mapping, null, 1L);
      assertTrue(record.isConsistent());
  }

  @Test
  public void testIsConsistentWithMappingAndMismatchingSize() {
      String[] values = {"value1", "value2"};
      Map<String, Integer> mapping = new HashMap<>();
      mapping.put("name1", 0);
      mapping.put("name2", 1);
      mapping.put("name3", 2);
      CSVRecord record = new CSVRecord(values, mapping, null, 1L);
      assertFalse(record.isConsistent());
  }

  @Test
  public void testIsMappedWithoutMapping() {
      CSVRecord record = new CSVRecord(null, null, null, 1L);
      assertFalse(record.isMapped("name"));
  }

  @Test
  public void testIsMappedWithValidMapping() {
      String[] values = {"value1", "value2", "value3"};
      Map<String, Integer> mapping = new HashMap<>();
      mapping.put("name1", 0);
      mapping.put("name2", 1);
      mapping.put("name3", 2);
      CSVRecord record = new CSVRecord(values, mapping, null, 1L);
      assertTrue(record.isMapped("name1"));
  }

  @Test
  public void testIsMappedWithInvalidMapping() {
      String[] values = {"value1", "value2", "value3"};
      Map<String, Integer> mapping = new HashMap<>();
      mapping.put("name1", 0);
      mapping.put("name2", 1);
      mapping.put("name3", 2);
      CSVRecord record = new CSVRecord(values, mapping, null, 1L);
      assertFalse(record.isMapped("name4"));
  }

  @Test
  public void testIsSetWithoutMapping() {
      CSVRecord record = new CSVRecord(null, null, null, 1L);
      assertFalse(record.isSet("name"));
  }

  @Test
  public void testIsSetWithInvalidMapping() {
      String[] values = {"value1", "value2", "value3"};
      Map<String, Integer> mapping = new HashMap<>();
      mapping.put("name1", 0);
      mapping.put("name2", 1);
      mapping.put("name3", 2);
      CSVRecord record = new CSVRecord(values, mapping, null, 1L);
      assertFalse(record.isSet("name4"));
  }

  @Test
  public void testIsSetWithValidMappingAndInvalidIndex() {
      String[] values = {"value1", "value2"};
      Map<String, Integer> mapping = new HashMap<>();
      mapping.put("name1", 0);
      mapping.put("name2", 1);
      mapping.put("name3", 2);
      CSVRecord record = new CSVRecord(values, mapping, null, 1L);
      assertFalse(record.isSet("name3"));
  }

  @Test
  public void testIterator() {
      String[] values = {"value1", "value2", "value3"};
      CSVRecord record = new CSVRecord(values, null, null, 1L);
      assertTrue(record.iterator().hasNext());
  }

  @Test
  public void testPutIn() {
      String[] values = {"value1", "value2", "value3"};
      Map<String, Integer> mapping = new HashMap<>();
      mapping.put("name1", 0);
      mapping.put("name2", 1);
      mapping.put("name3", 2);
      CSVRecord record = new CSVRecord(values, mapping, null, 1L);
      Map<String, String> map = new HashMap<>();
      map.put("name4", "value4");
      record.putIn(map);
      assertEquals(4, map.size());
  }

  @Test
  public void testSize() {
      String[] values = {"value1", "value2", "value3"};
      CSVRecord record = new CSVRecord(values, null, null, 1L);
      assertEquals(3, record.size());
  }

  @Test
  public void testToList() {
      String[] values = {"value1", "value2", "value3"};
      CSVRecord record = new CSVRecord(values, null, null, 1L);
      assertEquals(3, record.toList().size());
  }

  @Test
  public void testToMap() {
      String[] values = {"value1", "value2", "value3"};
      Map<String, Integer> mapping = new HashMap<>();
      mapping.put("name1", 0);
      mapping.put("name2", 1);
      mapping.put("name3", 2);
      CSVRecord record = new CSVRecord(values, mapping, null, 1L);
      Map<String, String> map = record.toMap();
      assertEquals(3, map.size());
  }

  @Test
  public void testToString() {
      String[] values = {"value1", "value2", "value3"};
      CSVRecord record = new CSVRecord(values, null, null, 1L);
      assertEquals("[value1, value2, value3]", record.toString());
  }

  @Test
  public void testValues() {
      String[] values = {"value1", "value2", "value3"};
      CSVRecord record = new CSVRecord(values, null, null, 1L);
      assertArrayEquals(values, record.values());
  }

  private enum TestEnum {
      VALUE1, VALUE2
  }

}