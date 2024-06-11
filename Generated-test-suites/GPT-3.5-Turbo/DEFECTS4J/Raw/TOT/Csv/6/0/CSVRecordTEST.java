import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CSVRecordTest {

  CSVRecord record;

  /*
   * Setting up a sample CSVRecord object for testing purposes
   */
  @Before
  public void setup() {
    String[] values = {"John", "Doe", "john.doe@example.com"};
    Map<String, Integer> mapping = new HashMap<>();
    mapping.put("firstName", 0);
    mapping.put("lastName", 1);
    mapping.put("email", 2);
    String comment = "This is a sample record";
    long recordNumber = 1;
    record = new CSVRecord(values, mapping, comment, recordNumber);
  }

  /*
   * Testing get(Enum<?>) method
   * Use Case 1: Enum value exists in mapping
   */
  @Test
  public void testGetEnumValid() {
    assertEquals("John", record.get(FirstName.class));
  }

  /*
   * Testing get(Enum<?>) method
   * Use Case 2: Enum value does not exist in mapping
   * Error Case: IllegalArgumentException is thrown
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetEnumInvalid() {
    record.get(InvalidEnum.class);
  }

  /*
   * Testing get(int) method
   * Use Case 1: Valid index within the range
   */
  @Test
  public void testGetValidIndex() {
    assertEquals("Doe", record.get(1));
  }

  /*
   * Testing get(int) method
   * Use Case 2: Invalid index out of range
   * Error Case: ArrayIndexOutOfBoundsException is thrown
   */
  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testGetInvalidIndex() {
    record.get(3);
  }

  /*
   * Testing get(String) method
   * Use Case 1: Valid name exists in mapping
   */
  @Test
  public void testGetValidName() {
    assertEquals("john.doe@example.com", record.get("email"));
  }

  /*
   * Testing get(String) method
   * Use Case 2: Name does not exist in mapping
   * Error Case: IllegalArgumentException is thrown
   */
  @Test(expected = IllegalArgumentException.class)
  public void testGetInvalidName() {
    record.get("invalid");
  }

  /*
   * Testing get(String) method
   * Use Case 3: Mapping is null
   * Error Case: IllegalStateException is thrown
   */
  @Test(expected = IllegalStateException.class)
  public void testGetNullMapping() {
    CSVRecord nullMappingRecord = new CSVRecord(record.values, null, record.comment, record.recordNumber);
    nullMappingRecord.get("firstName");
  }

  /*
   * Testing getComment() method
   * Use Case 1: Comment exists
   */
  @Test
  public void testGetCommentValid() {
    assertEquals("This is a sample record", record.getComment());
  }

  /*
   * Testing getComment() method
   * Use Case 2: Comment does not exist
   */
  @Test
  public void testGetCommentInvalid() {
    CSVRecord emptyCommentRecord = new CSVRecord(record.values, record.mapping, null, record.recordNumber);
    assertNull(emptyCommentRecord.getComment());
  }

  /*
   * Testing getRecordNumber() method
   * Use Case 1: Record number exists
   */
  @Test
  public void testGetRecordNumber() {
    assertEquals(1, record.getRecordNumber());
  }

  /*
   * Testing isConsistent() method
   * Use Case 1: Mapping size is equal to the number of values
   */
  @Test
  public void testIsConsistentValid() {
    assertTrue(record.isConsistent());
  }

  /*
   * Testing isConsistent() method
   * Use Case 2: Mapping size is not equal to the number of values
   */
  @Test
  public void testIsConsistentInvalid() {
    CSVRecord inconsistentRecord = new CSVRecord(record.values, record.mapping, record.comment, record.recordNumber);
    inconsistentRecord.mapping.remove("firstName");
    assertFalse(inconsistentRecord.isConsistent());
  }

  /*
   * Testing isMapped(String) method
   * Use Case 1: Name exists in mapping
   */
  @Test
  public void testIsMappedValid() {
    assertTrue(record.isMapped("firstName"));
  }

  /*
   * Testing isMapped(String) method
   * Use Case 2: Name does not exist in mapping
   */
  @Test
  public void testIsMappedInvalid() {
    assertFalse(record.isMapped("invalid"));
  }

  /*
   * Testing isSet(String) method
   * Use Case 1: Name exists in mapping and has a value
   */
  @Test
  public void testIsSetValid() {
    assertTrue(record.isSet("email"));
  }

  /*
   * Testing isSet(String) method
   * Use Case 2: Name exists in mapping but does not have a value
   */
  @Test
  public void testIsSetInvalid() {
    assertFalse(record.isSet("lastName"));
  }

  /*
   * Testing iterator() method
   * Use Case 1: Iterator returns all values
   */
  @Test
  public void testIterator() {
    Iterator<String> iterator = record.iterator();
    assertEquals("John", iterator.next());
    assertEquals("Doe", iterator.next());
    assertEquals("john.doe@example.com", iterator.next());
    assertFalse(iterator.hasNext());
  }

  /*
   * Testing putIn(M) method
   * Use Case 1: Verify values are correctly put in the map
   */
  @Test
  public void testPutIn() {
    Map<String, String> map = new HashMap<>();
    map.put("firstName", "John");
    map.put("lastName", "Doe");
    map.put("email", "john.doe@example.com");
    assertEquals(map, record.putIn(new HashMap<String, String>()));
  }

  /*
   * Testing size() method
   */
  @Test
  public void testSize() {
    assertEquals(3, record.size());
  }

  /*
   * Testing toList() method
   */
  @Test
  public void testToList() {
    List<String> list = Arrays.asList("John", "Doe", "john.doe@example.com");
    assertEquals(list, record.toList());
  }

  /*
   * Testing toMap() method
   */
  @Test
  public void testToMap() {
    Map<String, String> map = new HashMap<>();
    map.put("firstName", "John");
    map.put("lastName", "Doe");
    map.put("email", "john.doe@example.com");
    assertEquals(map, record.toMap());
  }

  /*
   * Testing toString() method
   */
  @Test
  public void testToString() {
    assertEquals("[John, Doe, john.doe@example.com]", record.toString());
  }

  // Custom Enums for testing purpose
  enum FirstName {
    JOHN;
  }

  enum InvalidEnum {
    INVALID;
  }

}