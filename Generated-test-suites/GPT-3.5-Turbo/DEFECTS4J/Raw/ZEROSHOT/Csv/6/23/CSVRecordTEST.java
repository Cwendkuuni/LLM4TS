import static org.junit.Assert.*;

import org.apache.commons.csv.CSVRecord;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {
  
  private CSVRecord record;
  private String[] testValues = {"value1", "value2", "value3"};
  private String testComment = "Test comment";
  private long testRecordNumber = 1L;
  
  @Before
  public void setUp() {
    record = new CSVRecord(testValues, null, testComment, testRecordNumber);
  }
  
  @Test
  public void testGetByEnum() {
    assertEquals("value1", record.get(TestEnum.VALUE1));
    assertEquals("value2", record.get(TestEnum.VALUE2));
    assertEquals("value3", record.get(TestEnum.VALUE3));
  }
  
  @Test
  public void testGetByIndex() {
    assertEquals("value1", record.get(0));
    assertEquals("value2", record.get(1));
    assertEquals("value3", record.get(2));
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetByNameWithoutMapping() {
    record.get("name");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetByNameWithInvalidName() {
    record.get("invalid");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetByNameWithInvalidIndex() {
    record.get("index");
  }
  
  @Test
  public void testGetByName() {
    record = new CSVRecord(testValues, createMapping(), testComment, testRecordNumber);
    assertEquals("value1", record.get("name1"));
    assertEquals("value2", record.get("name2"));
    assertEquals("value3", record.get("name3"));
  }
  
  @Test
  public void testGetComment() {
    assertEquals(testComment, record.getComment());
  }
  
  @Test
  public void testGetRecordNumber() {
    assertEquals(testRecordNumber, record.getRecordNumber());
  }
  
  @Test
  public void testIsConsistentWithoutMapping() {
    assertTrue(record.isConsistent());
  }
  
  @Test
  public void testIsConsistentWithMapping() {
    record = new CSVRecord(testValues, createMapping(), testComment, testRecordNumber);
    assertTrue(record.isConsistent());
  }
  
  @Test
  public void testIsConsistentWithInvalidMapping() {
    record = new CSVRecord(testValues, createInvalidMapping(), testComment, testRecordNumber);
    assertFalse(record.isConsistent());
  }
  
  @Test
  public void testIsMappedWithoutMapping() {
    assertFalse(record.isMapped("name"));
  }
  
  @Test
  public void testIsMappedWithMapping() {
    record = new CSVRecord(testValues, createMapping(), testComment, testRecordNumber);
    assertTrue(record.isMapped("name1"));
    assertTrue(record.isMapped("name2"));
    assertTrue(record.isMapped("name3"));
  }
  
  @Test
  public void testIsSetWithoutMapping() {
    assertFalse(record.isSet("name"));
  }
  
  @Test
  public void testIsSetWithMappingAndExistingIndex() {
    record = new CSVRecord(testValues, createMapping(), testComment, testRecordNumber);
    assertTrue(record.isSet("name1"));
    assertTrue(record.isSet("name2"));
    assertTrue(record.isSet("name3"));
  }
  
  @Test
  public void testIsSetWithMappingAndInvalidIndex() {
    record = new CSVRecord(testValues, createInvalidMapping(), testComment, testRecordNumber);
    assertFalse(record.isSet("name1"));
    assertFalse(record.isSet("name2"));
    assertFalse(record.isSet("name3"));
  }
  
  @Test
  public void testIterator() {
    assertArrayEquals(testValues, record.toList().toArray());
  }
  
  @Test
  public void testPutIn() {
    record = new CSVRecord(testValues, createMapping(), testComment, testRecordNumber);
    Map<String, String> map = record.putIn(new HashMap<String, String>());
    assertEquals("value1", map.get("name1"));
    assertEquals("value2", map.get("name2"));
    assertEquals("value3", map.get("name3"));
  }
  
  @Test
  public void testSize() {
    assertEquals(testValues.length, record.size());
  }
  
  @Test
  public void testToString() {
    assertEquals(Arrays.toString(testValues), record.toString());
  }
  
  private Map<String, Integer> createMapping() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    mapping.put("name2", 1);
    mapping.put("name3", 2);
    return mapping;
  }
  
  private Map<String, Integer> createInvalidMapping() {
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("name1", 0);
    mapping.put("name2", 1);
    return mapping;
  }
  
  private enum TestEnum {
    VALUE1, VALUE2, VALUE3;
  }

}