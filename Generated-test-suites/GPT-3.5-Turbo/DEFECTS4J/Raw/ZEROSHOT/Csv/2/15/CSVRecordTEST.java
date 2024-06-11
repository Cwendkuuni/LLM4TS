import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

public class CSVRecordTest {

  @Test
  public void testGetByIndex() {
    String[] values = {"A", "B", "C"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("A", 0);
    mapping.put("B", 1);
    mapping.put("C", 2);
    CSVRecord record = new CSVRecord(values, mapping, null, 1);
    assertEquals("A", record.get(0));
    assertEquals("B", record.get(1));
    assertEquals("C", record.get(2));
  }

  @Test
  public void testGetByName() {
    String[] values = {"A", "B", "C"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("A", 0);
    mapping.put("B", 1);
    mapping.put("C", 2);
    CSVRecord record = new CSVRecord(values, mapping, null, 1);
    assertEquals("A", record.get("A"));
    assertEquals("B", record.get("B"));
    assertEquals("C", record.get("C"));
    assertNull(record.get("D"));
  }

  @Test(expected = IllegalStateException.class)
  public void testGetByNameWithoutMapping() {
    String[] values = {"A", "B", "C"};
    CSVRecord record = new CSVRecord(values, null, null, 1);
    record.get("A");
  }
  
  @Test
  public void testIsConsistent() {
    String[] values = {"A", "B", "C"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("A", 0);
    mapping.put("B", 1);
    mapping.put("C", 2);
    CSVRecord record = new CSVRecord(values, mapping, null, 1);
    assertTrue(record.isConsistent());
  }
  
  @Test
  public void testIsNotConsistent() {
    String[] values = {"A", "B", "C"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("A", 0);
    CSVRecord record = new CSVRecord(values, mapping, null, 1);
    assertFalse(record.isConsistent());
  }
  
  @Test
  public void testIsMapped() {
    String[] values = {"A", "B", "C"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("A", 0);
    mapping.put("B", 1);
    mapping.put("C", 2);
    CSVRecord record = new CSVRecord(values, mapping, null, 1);
    assertTrue(record.isMapped("A"));
    assertTrue(record.isMapped("B"));
    assertTrue(record.isMapped("C"));
    assertFalse(record.isMapped("D"));
  }
  
  @Test
  public void testIsSet() {
    String[] values = {"A", "B", "C"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("A", 0);
    mapping.put("B", 1);
    mapping.put("C", 2);
    CSVRecord record = new CSVRecord(values, mapping, null, 1);
    assertTrue(record.isSet("A"));
    assertTrue(record.isSet("B"));
    assertTrue(record.isSet("C"));
    assertFalse(record.isSet("D"));
  }
  
  @Test
  public void testIterator() {
    String[] values = {"A", "B", "C"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("A", 0);
    mapping.put("B", 1);
    mapping.put("C", 2);
    CSVRecord record = new CSVRecord(values, mapping, null, 1);
    StringBuilder sb = new StringBuilder();
    for (String value : record) {
      sb.append(value);
    }
    assertEquals("ABC", sb.toString());
  }
  
  @Test
  public void testValues() {
    String[] values = {"A", "B", "C"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("A", 0);
    mapping.put("B", 1);
    mapping.put("C", 2);
    CSVRecord record = new CSVRecord(values, mapping, null, 1);
    assertArrayEquals(values, record.values());
  }
  
  @Test
  public void testGetComment() {
    String[] values = {"A", "B", "C"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("A", 0);
    mapping.put("B", 1);
    mapping.put("C", 2);
    CSVRecord record = new CSVRecord(values, mapping, "comment", 1);
    assertEquals("comment", record.getComment());
  }
  
  @Test
  public void testGetRecordNumber() {
    String[] values = {"A", "B", "C"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("A", 0);
    mapping.put("B", 1);
    mapping.put("C", 2);
    CSVRecord record = new CSVRecord(values, mapping, null, 1);
    assertEquals(1, record.getRecordNumber());
  }
  
  @Test
  public void testSize() {
    String[] values = {"A", "B", "C"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("A", 0);
    mapping.put("B", 1);
    mapping.put("C", 2);
    CSVRecord record = new CSVRecord(values, mapping, null, 1);
    assertEquals(3, record.size());
  }
  
  @Test
  public void testToString() {
    String[] values = {"A", "B", "C"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("A", 0);
    mapping.put("B", 1);
    mapping.put("C", 2);
    CSVRecord record = new CSVRecord(values, mapping, null, 1);
    assertEquals("[A, B, C]", record.toString());
  }

}