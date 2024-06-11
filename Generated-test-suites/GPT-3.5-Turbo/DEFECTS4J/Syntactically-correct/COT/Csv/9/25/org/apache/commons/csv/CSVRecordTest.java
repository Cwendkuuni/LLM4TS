import org.junit.Test;

public class CSVRecordTest {

  @Test
  public void testGetEnum() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    String result = record.get(TestEnum.ENUM1);
    assertEquals("value1", result);
  }

  @Test
  public void testGetInt() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    String result = record.get(1);
    assertEquals("value2", result);
  }

  @Test
  public void testGetString() {
    Map<String, Integer> mapping = new HashMap<String, Integer>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
    String result = record.get("column1");
    assertEquals("value1", result);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetStringNoMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    String result = record.get("column1");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetStringMappingNotFound() {
    Map<String, Integer> mapping = new HashMap<String, Integer>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
    String result = record.get("column3");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetStringIndexOutOfBounds() {
    Map<String, Integer> mapping = new HashMap<String, Integer>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
    String result = record.get("column2");
  }

  @Test
  public void testGetComment() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
    String result = record.getComment();
    assertEquals("comment", result);
  }

  @Test
  public void testGetRecordNumber() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    long result = record.getRecordNumber();
    assertEquals(1, result);
  }

  @Test
  public void testIsConsistent() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    boolean result = record.isConsistent();
    assertTrue(result);
  }

  @Test
  public void testIsConsistentWithMapping() {
    Map<String, Integer> mapping = new HashMap<String, Integer>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
    boolean result = record.isConsistent();
    assertTrue(result);
  }

  @Test
  public void testIsConsistentWithMappingIncorrectSize() {
    Map<String, Integer> mapping = new HashMap<String, Integer>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    CSVRecord record = new CSVRecord(new String[]{"value1"}, mapping, null, 1);
    boolean result = record.isConsistent();
    assertFalse(result);
  }

  @Test
  public void testIsMapped() {
    Map<String, Integer> mapping = new HashMap<String, Integer>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
    boolean result = record.isMapped("column1");
    assertTrue(result);
  }

  @Test
  public void testIsSet() {
    Map<String, Integer> mapping = new HashMap<String, Integer>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
    boolean result = record.isSet("column1");
    assertTrue(result);
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
    Map<String, String> map = new HashMap<String, String>();
    Map<String, Integer> mapping = new HashMap<String, Integer>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, mapping, null, 1);
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
  public void testToMap() {
    Map<String, Integer> mapping = new HashMap<String, Integer>();
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
    assertEquals("[value1, value2]", result);
  }

  @Test
  public void testValues() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    String[] result = record.values();
    assertEquals(2, result.length);
    assertEquals("value1", result[0]);
    assertEquals("value2", result[1]);
  }

}