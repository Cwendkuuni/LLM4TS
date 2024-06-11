import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

  private CSVRecord csvRecord;
  
  @Before
  public void setUp() {
    String[] values = {"value1", "value2", "value3"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("column1", 0);
    mapping.put("column2", 1);
    mapping.put("column3", 2);
    String comment = "This is a comment";
    long recordNumber = 1;

    csvRecord = new CSVRecord(values, mapping, comment, recordNumber);
  }
  
  @Test
  public void testGetEnum() {
    assertEquals("value1", csvRecord.get(TestEnum.COLUMN1));
  }
  
  @Test
  public void testGetInt() {
    assertEquals("value2", csvRecord.get(1));
  }
  
  @Test
  public void testGetString() {
    assertEquals("value3", csvRecord.get("column3"));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetNoMapping() {
    CSVRecord record = new CSVRecord(new String[0], null, null, 1);
    record.get("column1");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetMappingNotFound() {
    csvRecord.get("column4");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testIndexOutOfBounds() {
    csvRecord.get("column1");
  }
  
  @Test
  public void testGetComment() {
    assertEquals("This is a comment", csvRecord.getComment());
  }
  
  @Test
  public void testGetRecordNumber() {
    assertEquals(1, csvRecord.getRecordNumber());
  }
  
  @Test
  public void testIsConsistent() {
    assertTrue(csvRecord.isConsistent());
  }
  
  @Test
  public void testIsMapped() {
    assertTrue(csvRecord.isMapped("column1"));
    assertFalse(csvRecord.isMapped("column4"));
  }
  
  @Test
  public void testIsSet() {
    assertTrue(csvRecord.isSet("column1"));
    assertFalse(csvRecord.isSet("column4"));
  }
  
  @Test
  public void testIterator() {
    Iterator<String> iterator = csvRecord.iterator();
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
    Map<String, String> map = new HashMap<>();
    csvRecord.putIn(map);
    assertEquals("value1", map.get("column1"));
    assertEquals("value2", map.get("column2"));
    assertEquals("value3", map.get("column3"));
  }
  
  @Test
  public void testSize() {
    assertEquals(3, csvRecord.size());
  }
  
  @Test
  public void testToList() {
    List<String> list = csvRecord.toList();
    assertEquals("value1", list.get(0));
    assertEquals("value2", list.get(1));
    assertEquals("value3", list.get(2));
  }
  
  @Test
  public void testToMap() {
    Map<String, String> map = csvRecord.toMap();
    assertEquals("value1", map.get("column1"));
    assertEquals("value2", map.get("column2"));
    assertEquals("value3", map.get("column3"));
  }
  
  @Test
  public void testValues() {
    String[] values = csvRecord.values();
    assertEquals("value1", values[0]);
    assertEquals("value2", values[1]);
    assertEquals("value3", values[2]);
  }
  
}

enum TestEnum {
  COLUMN1,
  COLUMN2,
  COLUMN3
}