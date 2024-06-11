import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

  // Test case for get(int i) method
  @Test
  public void testGetByIndex() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);

    CSVRecord record = new CSVRecord(values, mapping, null, 1);

    Assert.assertEquals("value1", record.get(0));
    Assert.assertEquals("value2", record.get(1));
    Assert.assertEquals("value3", record.get(2));
  }

  // Test case for get(String name) method when mapping is null
  @Test(expected = IllegalStateException.class)
  public void testGetByNameWithNoMapping() {
    String[] values = {"value1", "value2", "value3"};

    CSVRecord record = new CSVRecord(values, null, null, 1);

    record.get("column1");
  }

  // Test case for get(String name) method when column name is not found
  @Test
  public void testGetByNameWithNoColumnFound() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);

    CSVRecord record = new CSVRecord(values, mapping, null, 1);

    Assert.assertNull(record.get("column4"));
  }

  // Test case for get(String name) method when column name is found
  @Test
  public void testGetByNameWithColumnFound() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);

    CSVRecord record = new CSVRecord(values, mapping, null, 1);

    Assert.assertEquals("value1", record.get("column1"));
    Assert.assertEquals("value2", record.get("column2"));
    Assert.assertEquals("value3", record.get("column3"));
  }

  // Test case for isConsistent() method when mapping is null
  @Test
  public void testIsConsistentWithNoMapping() {
    String[] values = {"value1", "value2", "value3"};

    CSVRecord record = new CSVRecord(values, null, null, 1);

    Assert.assertTrue(record.isConsistent());
  }

  // Test case for isConsistent() method when mapping size equals values length
  @Test
  public void testIsConsistentWithMatchingSizes() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);

    CSVRecord record = new CSVRecord(values, mapping, null, 1);

    Assert.assertTrue(record.isConsistent());
  }

  // Test case for isConsistent() method when mapping size is different from values length
  @Test
  public void testIsConsistentWithDifferentSizes() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);

    CSVRecord record = new CSVRecord(values, mapping, null, 1);

    Assert.assertFalse(record.isConsistent());
  }

  // Test case for isMapped(String name) method when mapping is null
  @Test
  public void testIsMappedWithNoMapping() {
    String[] values = {"value1", "value2", "value3"};

    CSVRecord record = new CSVRecord(values, null, null, 1);

    Assert.assertFalse(record.isMapped("column1"));
  }

  // Test case for isMapped(String name) method when column name is not found
  @Test
  public void testIsMappedWithNoColumnFound() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);

    CSVRecord record = new CSVRecord(values, mapping, null, 1);

    Assert.assertFalse(record.isMapped("column3"));
  }

  // Test case for isMapped(String name) method when column name is found
  @Test
  public void testIsMappedWithColumnFound() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);

    CSVRecord record = new CSVRecord(values, mapping, null, 1);

    Assert.assertTrue(record.isMapped("column1"));
    Assert.assertTrue(record.isMapped("column2"));
    Assert.assertTrue(record.isMapped("column3"));
  }

  // Test case for isSet(String name) method when mapping is null and column name is found
  @Test
  public void testIsSetWithNoMappingAndColumnFound() {
    String[] values = {"value1", "value2", "value3"};

    CSVRecord record = new CSVRecord(values, null, null, 1);

    Assert.assertFalse(record.isSet("column1"));
  }

  // Test case for isSet(String name) method when column name is not found
  @Test
  public void testIsSetWithNoColumnFound() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);

    CSVRecord record = new CSVRecord(values, mapping, null, 1);

    Assert.assertFalse(record.isSet("column3"));
  }

  // Test case for isSet(String name) method when column name is found but value index is out of bounds
  @Test
  public void testIsSetWithColumnFoundAndIndexOutOfBounds() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 3);

    CSVRecord record = new CSVRecord(values, mapping, null, 1);

    Assert.assertFalse(record.isSet("column3"));
  }

  // Test case for isSet(String name) method when column name is found and value index is within bounds
  @Test
  public void testIsSetWithColumnFoundAndIndexWithinBounds() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);

    CSVRecord record = new CSVRecord(values, mapping, null, 1);

    Assert.assertTrue(record.isSet("column3"));
  }

  // Test case for iterator() method
  @Test
  public void testIterator() {
    String[] values = {"value1", "value2", "value3"};

    CSVRecord record = new CSVRecord(values, null, null, 1);

    Iterator<String> iterator = record.iterator();

    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals("value1", iterator.next());

    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals("value2", iterator.next());

    Assert.assertTrue(iterator.hasNext());
    Assert.assertEquals("value3", iterator.next());
  }

  // Test case for values() method
  @Test
  public void testValues() {
    String[] values = {"value1", "value2", "value3"};

    CSVRecord record = new CSVRecord(values, null, null, 1);

    Assert.assertArrayEquals(new String[]{"value1", "value2", "value3"}, record.values());
  }

  // Test case for getComment() method
  @Test
  public void testGetComment() {
    String[] values = {"value1", "value2", "value3"};

    CSVRecord record = new CSVRecord(values, null, "comment", 1);

    Assert.assertEquals("comment", record.getComment());
  }
 
  // Test case for getRecordNumber() method
  @Test
  public void testGetRecordNumber() {
    String[] values = {"value1", "value2", "value3"};

    CSVRecord record = new CSVRecord(values, null, "comment", 1);

    Assert.assertEquals(1, record.getRecordNumber());
  }

  // Test case for size() method
  @Test
  public void testSize() {
    String[] values = {"value1", "value2", "value3"};

    CSVRecord record = new CSVRecord(values, null, null, 1);

    Assert.assertEquals(3, record.size());
  }

  // Test case for toString() method
  @Test
  public void testToString() {
    String[] values = {"value1", "value2", "value3"};

    CSVRecord record = new CSVRecord(values, null, null, 1);

    Assert.assertEquals("[value1, value2, value3]", record.toString());
  }
}