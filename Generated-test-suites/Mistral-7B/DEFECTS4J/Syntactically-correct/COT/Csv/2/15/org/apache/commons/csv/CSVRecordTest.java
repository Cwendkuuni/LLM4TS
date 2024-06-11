import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private String[] testValues = new String[]{"Name", "Age", "City"};
 private Map<String, Integer> testMapping = new java.util.HashMap<>();
 private CSVRecord testRecord;

 @Test
 public void testCSVRecordConstruction() {
 testRecord = new CSVRecord(testValues, testMapping, null, 1);
 assertArrayEquals(testValues, testRecord.values());
 assertNull(testRecord.getComment());
 assertEquals(1, testRecord.getRecordNumber());
 }

 @Test
 public void testGetMethod() {
 testMapping.put("Name", 0);
 testMapping.put("Age", 1);
 testMapping.put("City", 2);
 testRecord = new CSVRecord(testValues, testMapping, null, 1);

 // Test getting value by index
 assertEquals("Name", testRecord.get(0));
 assertEquals("Age", testRecord.get(1));
 assertEquals("City", testRecord.get(2));

 // Test getting value by name
 assertEquals("Name", testRecord.get("Name"));
 assertEquals("Age", testRecord.get("Age"));
 assertEquals("City", testRecord.get("City"));
 assertEquals(null, testRecord.get("NonExistentColumn"));
 }

 @Test
 public void testIsConsistentMethod() {
 testMapping = new java.util.HashMap<>();
 testRecord = new CSVRecord(new String[0], testMapping, null, 1);
 assertTrue(testRecord.isConsistent());

 testValues = new String[]{"Name", "Age", "City"};
 testMapping = new java.util.HashMap<>();
 testMapping.put("Name", 0);
 testMapping.put("Age", 1);
 testMapping.put("City", 2);
 testRecord = new CSVRecord(testValues, testMapping, null, 1);
 assertTrue(testRecord.isConsistent());

 testMapping = new java.util.HashMap<>();
 testMapping.put("Name", 0);
 testMapping.put("Age", 1);
 testMapping.put("City", 3); // incorrect mapping size
 testRecord = new CSVRecord(testValues, testMapping, null, 1);
 assertFalse(testRecord.isConsistent());
 }

 @Test
 public void testIsMappedMethod() {
 testMapping.put("Name", 0);
 testMapping.put("Age", 1);
 testMapping.put("City", 2);
 testRecord = new CSVRecord(new String[]{"John", "30", "New York"}, testMapping, null, 1);

 assertTrue(testRecord.isMapped("Name"));
 assertTrue(testRecord.isMapped("Age"));
 assertTrue(testRecord.isMapped("City"));
 assertFalse(testRecord.isMapped("NonExistentColumn"));
 }

 @Test
 public void testIsSetMethod() {
 testMapping.put("Name", 0);
 testMapping.put("Age", 1);
 testMapping.put("City", 2);
 testRecord = new CSVRecord(new String[]{"John", "30", "New York"}, testMapping, null, 1);

 assertTrue(testRecord.isSet("Name"));
 assertTrue(testRecord.isSet("Age"));
 assertTrue(testRecord.isSet("City"));
 assertFalse(testRecord.isSet("NonExistentColumn"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetWithNoMapping() {
 testRecord = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 assertNull(testRecord.get("Name"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithInconsistentRecord() {
 testMapping = new java.util.HashMap<>();
 testMapping.put("Name", 0);
 testMapping.put("Age", 1);
 testMapping.put("City", 2);
 testRecord = new CSVRecord(new String[]{"Name", "Age", "City", "AnotherColumn"}, testMapping, null, 1);
 assertNull(testRecord.get("Name"));
 }

 @Test
 public void testIteratorMethod() {
 testMapping.put("Name", 0);
 testMapping.put("Age", 1);
 testMapping.put("City", 2);
 testRecord = new CSVRecord(new String[]{"John", "30", "New York"}, testMapping, null, 1);

 Iterator<String> iterator = testRecord.iterator();
 assertEquals("John", iterator.next());
 assertEquals("30", iterator.next());
 assertEquals("New York", iterator.next());
 assertFalse(iterator.hasNext());
 }

 @Test
 public void testSizeMethod() {
 testRecord = new CSVRecord(new String[0], null, null, 1);
 assertEquals(0, testRecord.size());
 testValues = new String[]{"Name", "Age", "City"};
 testRecord = new CSVRecord(testValues, null, null, 1);
 assertEquals(3, testRecord.size());
 }

 @Test
 public void testToStringMethod() {
 testMapping.put("Name", 0);
 testMapping.put("Age", 1);
 testMapping.put("City", 2);
 testRecord = new CSVRecord(new String[]{"John", "30", "New York"}, testMapping, null, 1);
 assertEquals("[Name=John, Age=30, City=New York]", testRecord.toString());
 }
}