import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

  @Test
  public void testGetEnum() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals("value1", record.get(Enum.VALUE1));
    assertEquals("value2", record.get(Enum.VALUE2));
  }

  @Test
  public void testGetIndex() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals("value1", record.get(0));
    assertEquals("value2", record.get(1));
  }

  @Test(expected = IllegalStateException.class)
  public void testGetNameWithNullMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    record.get("name");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNameWithInvalidMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, 
    ImmutableMap.of("column1", 0), null, 1);
    record.get("name");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetNameWithInvalidIndex() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, 
    ImmutableMap.of("column1", 0), null, 1);
    record.get("column1");
  }

  @Test
  public void testGetComment() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "this is a comment", 1);
    assertEquals("this is a comment", record.getComment());
  }

  @Test
  public void testGetRecordNumber() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 3);
    assertEquals(3, record.getRecordNumber());
  }

  @Test
  public void testIsConsistent() {
    CSVRecord record1 = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    CSVRecord record2 = new CSVRecord(new String[]{"value1", "value2"}, 
    ImmutableMap.of("column1", 0, "column2", 1), null, 1);

    assertTrue(record1.isConsistent());
    assertTrue(record2.isConsistent());
  }

  @Test
  public void testIsNotConsistent() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, 
    ImmutableMap.of("column1", 0), null, 1);

    assertFalse(record.isConsistent());
  }

  @Test
  public void testIsMapped() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, 
    ImmutableMap.of("column1", 0, "column2", 1), null, 1);

    assertTrue(record.isMapped("column1"));
    assertTrue(record.isMapped("column2"));
  }

  @Test
  public void testIsNotMapped() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertFalse(record.isMapped("column1"));
  }

  @Test
  public void testIsSet() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, 
    ImmutableMap.of("column1", 0, "column2", 1), null, 1);

    assertTrue(record.isSet("column1"));
    assertTrue(record.isSet("column2"));
  }

  @Test
  public void testIsNotSet() {
    CSVRecord record = new CSVRecord(new String[]{"value1"}, 
    ImmutableMap.of("column1", 0), null, 1);

    assertFalse(record.isSet("column2"));
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
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, 
    ImmutableMap.of("column1", 0, "column2", 1), null, 1);

    Map<String, String> map = record.putIn(new HashMap<>());
    assertEquals("value1", map.get("column1"));
    assertEquals("value2", map.get("column2"));
  }

  @Test
  public void testSize() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals(2, record.size());
  }

  @Test
  public void testToList() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    List<String> list = record.toList();
    assertEquals("value1", list.get(0));
    assertEquals("value2", list.get(1));
  }

  @Test
  public void testToMap() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, 
    ImmutableMap.of("column1", 0, "column2", 1), null, 1);

    Map<String, String> map = record.toMap();
    assertEquals("value1", map.get("column1"));
    assertEquals("value2", map.get("column2"));
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