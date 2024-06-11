import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.csv.CSVFormat;
import org.junit.Test;

public class CSVRecordTest {

  @Test
  public void testGetWithEnum() {
    CSVRecord record = new CSVRecord(null, null, null, 1);
    assertEquals(null, record.get(TestEnum.TEST));
  }

  @Test
  public void testGetWithIndex() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals("value1", record.get(0));
    assertEquals("value2", record.get(1));
  }

  @Test(expected = IllegalStateException.class)
  public void testGetWithNameWithoutMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    record.get("name");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetWithNameWithInvalidMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    record.get("name");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetWithNameWithInvalidIndex() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    record.get("name");
  }

  @Test
  public void testGetWithNameWithValidMapping() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    record.put("name", 0);
    assertEquals("value1", record.get("name"));
  }

  @Test
  public void testGetComment() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "comment", 1);
    assertEquals("comment", record.getComment());
  }

  @Test
  public void testGetRecordNumber() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals(1, record.getRecordNumber());
  }

  @Test
  public void testIsConsistent() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertTrue(record.isConsistent());

    record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    record.put("name", 0);
    assertFalse(record.isConsistent());

    record = new CSVRecord(new String[]{"value1"}, null, null, 1);
    assertTrue(record.isConsistent());
  }

  @Test
  public void testIsMapped() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertFalse(record.isMapped("name"));

    record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    record.put("name", 0);
    assertTrue(record.isMapped("name"));
  }

  @Test
  public void testIsSet() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertFalse(record.isSet("name"));

    record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    record.put("name", 0);
    assertTrue(record.isSet("name"));
  }

  @Test
  public void testIterator() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    List<String> values = record.toList();
    int index = 0;
    for (String value : record) {
      assertEquals(values.get(index), value);
      index++;
    }
  }

  @Test
  public void testPutIn() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    record.put("name1", 0);
    record.put("name2", 1);
    Map<String, String> map = new HashMap<>();
    map.put("name1", "value1");
    map.put("name2", "value2");
    assertEquals(map, record.putIn(new HashMap<String, String>()));
  }

  @Test
  public void testSize() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals(2, record.size());
  }

  @Test
  public void testToString() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    assertEquals("[value1, value2]", record.toString());
  }

  private enum TestEnum {
    TEST
  }
}