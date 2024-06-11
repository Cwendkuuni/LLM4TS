
import org.junit.Test; 

public class CSVRecordTest {
  
  // Test case for get(int i) method
  @Test
  public void testGetByIndex() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    String expected = "value1";
    String actual = record.get(0);
    assertEquals(expected, actual);
  }
  
  // Test case for get(String name) method
  @Test
  public void testGetByName() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, 
      new HashMap<String, Integer>(){{ put("column1", 0); put("column2", 1); }}, null, 1);
    String expected = "value1";
    String actual = record.get("column1");
    assertEquals(expected, actual);
  }
  
  // Test case for isConsistent() method
  @Test
  public void testIsConsistent() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, 
      new HashMap<String, Integer>(){{ put("column1", 0); put("column2", 1); }}, null, 1);
    boolean expected = true;
    boolean actual = record.isConsistent();
    assertEquals(expected, actual);
  }
  
  // Test case for isMapped(String name) method
  @Test
  public void testIsMapped() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, 
      new HashMap<String, Integer>(){{ put("column1", 0); put("column2", 1); }}, null, 1);
    boolean expected = true;
    boolean actual = record.isMapped("column1");
    assertEquals(expected, actual);
  }
  
  // Test case for isSet(String name) method
  @Test
  public void testIsSet() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, 
      new HashMap<String, Integer>(){{ put("column1", 0); put("column2", 1); }}, null, 1);
    boolean expected = true;
    boolean actual = record.isSet("column1");
    assertEquals(expected, actual);
  }
  
  // Test case for iterator() method
  @Test
  public void testIterator() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    Iterator<String> iterator = record.iterator();
    String[] expected = {"value1", "value2"};
    int i = 0;
    while (iterator.hasNext()) {
      String actual = iterator.next();
      assertEquals(expected[i], actual);
      i++;
    }
  }
  
  // Test case for values() method
  @Test
  public void testValues() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    String[] expected = {"value1", "value2"};
    String[] actual = record.values();
    assertArrayEquals(expected, actual);
  }
  
  // Test case for getComment() method
  @Test
  public void testGetComment() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, "This is a comment", 1);
    String expected = "This is a comment";
    String actual = record.getComment();
    assertEquals(expected, actual);
  }
  
  // Test case for getRecordNumber() method
  @Test
  public void testGetRecordNumber() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 5);
    long expected = 5;
    long actual = record.getRecordNumber();
    assertEquals(expected, actual);
  }
  
  // Test case for size() method
  @Test
  public void testSize() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2", "value3"}, null, null, 1);
    int expected = 3;
    int actual = record.size();
    assertEquals(expected, actual);
  }
  
  // Test case for toString() method
  @Test
  public void testToString() {
    CSVRecord record = new CSVRecord(new String[]{"value1", "value2"}, null, null, 1);
    String expected = "[value1, value2]";
    String actual = record.toString();
    assertEquals(expected, actual);
  }
  
}
