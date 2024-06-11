import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

  @Test
  public void testGetEnum() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals("value1", record.get(SomeEnum.VALUE1));
  }

  @Test
  public void testGetIndex() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals("value2", record.get(1));
  }

  @Test
  public void testGetName() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals("value1", record.get("name1"));
  }

  @Test(expected = IllegalStateException.class)
  public void testGetNameNoMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    record.get("name1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNameInvalidName() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    record.get("invalidName");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNameOutOfBounds() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    record.get("name3");
  }

  @Test
  public void testGetComment() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "This is a comment", 1);
    assertEquals("This is a comment", record.getComment());
  }

  @Test
  public void testGetRecordNumber() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals(1, record.getRecordNumber());
  }

  @Test
  public void testIsConsistent() {
    CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertTrue(record1.isConsistent());
    CSVRecord record2 = new CSVRecord(new String[]{"value1"}, null, null, 1);
    assertFalse(record2.isConsistent());
  }

  @Test
  public void testIsMapped() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertTrue(record.isMapped("value1"));
    assertFalse(record.isMapped("value3"));
  }

  @Test
  public void testIsSet() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertTrue(record.isSet("value1"));
    assertFalse(record.isSet("value3"));
  }

  @Test
  public void testIterator() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertArrayEquals(new String[]{"value1", "value2"}, record.iterator().toArray());
  }

  @Test
  public void testPutIn() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    Map<String, String> map = new HashMap<>();
    map.put("key1", "value1");
    map.put("key2", "value2");

    assertEquals(map, record.putIn(new HashMap<String, String>()));
  }

  @Test
  public void testSize() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals(2, record.size());
  }

  @Test
  public void testToList() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals(Arrays.asList("value1", "value2"), record.toList());
  }

  @Test
  public void testToMap() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    Map<String, String> map = new HashMap<>();
    map.put("value1", "value1");
    map.put("value2", "value2");

    assertEquals(map, record.toMap());
  }

  @Test
  public void testToString() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals("[value1, value2]", record.toString());
  }

  @Test
  public void testValues() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertArrayEquals(new String[]{"value1", "value2"}, record.values());
  }
}