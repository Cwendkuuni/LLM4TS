import org.junit.Assert;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {
  
  //Test for get(int i) method
  @Test
  public void testGetByIndex() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, new HashMap<>(), "", 1);
    Assert.assertEquals("value1", record.get(0));
    Assert.assertEquals("value2", record.get(1));
    Assert.assertEquals("value3", record.get(2));
  }

  //Test for get(String name) method with mapping provided
  @Test
  public void testGetWithMapping() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    mapping.put("name2", 1);
    mapping.put("name3", 2);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, "", 1);
    Assert.assertEquals("value1", record.get("name1"));
    Assert.assertEquals("value2", record.get("name2"));
    Assert.assertEquals("value3", record.get("name3"));
    Assert.assertNull(record.get("name4"));
  }

  //Test for get(String name) method without mapping provided
  @Test(expected = IllegalStateException.class)
  public void testGetWithoutMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
    record.get("name");
  }

  //Test for isConsistent() method with mapping provided
  @Test
  public void testIsConsistentWithMapping() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    mapping.put("name2", 1);
    mapping.put("name3", 2);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, "", 1);
    Assert.assertTrue(record.isConsistent());
  }

  //Test for isConsistent() method without mapping provided
  @Test
  public void testIsConsistentWithoutMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
    Assert.assertTrue(record.isConsistent());
  }

  //Test for isMapped(String name) method with mapping provided
  @Test
  public void testIsMappedWithMapping() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    mapping.put("name2", 1);
    mapping.put("name3", 2);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, "", 1);
    Assert.assertTrue(record.isMapped("name1"));
    Assert.assertTrue(record.isMapped("name2"));
    Assert.assertTrue(record.isMapped("name3"));
    Assert.assertFalse(record.isMapped("name4"));
  }

  //Test for isMapped(String name) method without mapping provided
  @Test
  public void testIsMappedWithoutMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
    Assert.assertFalse(record.isMapped("name"));
  }

  //Test for isSet(String name) method with mapping and valid name provided
  @Test
  public void testIsSetWithMappingAndValidName() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    mapping.put("name2", 1);
    mapping.put("name3", 2);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, "", 1);
    Assert.assertTrue(record.isSet("name1"));
    Assert.assertTrue(record.isSet("name2"));
    Assert.assertTrue(record.isSet("name3"));
    Assert.assertFalse(record.isSet("name4"));
  }

  //Test for isSet(String name) method with mapping and invalid name provided
  @Test
  public void testIsSetWithMappingAndInvalidName() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    mapping.put("name2", 1);
    mapping.put("name3", 2);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, mapping, "", 1);
    Assert.assertFalse(record.isSet("name4"));
  }

  //Test for isSet(String name) method without mapping provided
  @Test
  public void testIsSetWithoutMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
    Assert.assertFalse(record.isSet("name"));
  }

  //Test for iterator() method
  @Test
  public void testIterator() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
    Iterator<String> iterator = record.iterator();
    Assert.assertEquals("value1", iterator.next());
    Assert.assertEquals("value2", iterator.next());
    Assert.assertEquals("value3", iterator.next());
    Assert.assertFalse(iterator.hasNext());
  }

  //Test for values() method
  @Test
  public void testValues() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
    String[] values = record.values();
    Assert.assertArrayEquals(new String[]{"value1", "value2", "value3"}, values);
  }

  //Test for getComment() method
  @Test
  public void testGetComment() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "This is a comment", 1);
    Assert.assertEquals("This is a comment", record.getComment());
  }

  //Test for getRecordNumber() method
  @Test
  public void testGetRecordNumber() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 10);
    Assert.assertEquals(10, record.getRecordNumber());
  }

  //Test for size() method
  @Test
  public void testSize() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
    Assert.assertEquals(3, record.size());
  }

  //Test for toString() method
  @Test
  public void testToString() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, "", 1);
    Assert.assertEquals("[value1, value2, value3]", record.toString());
  }
}