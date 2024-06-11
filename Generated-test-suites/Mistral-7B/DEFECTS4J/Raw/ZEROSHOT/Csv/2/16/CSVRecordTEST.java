import org.apache.commons.csv.CSVRecord;
import org.junit.Test;
import static org.junit.Assert.*;

public class CSVRecordTest {

 private String[] testValues = new String[]{"Alice", "Smith", "alice.smith@example.com"};
 private Map<String, Integer> testMapping = Map.of("Name", 0, "Surname", 1, "Email", 2);

 @Test
 public void testCSVRecordConstruction() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 assertEquals(1, record.getRecordNumber());
 assertEquals(testValues, record.values());
 assertEquals(testMapping, record.mapping());
 assertNull(record.getComment());
 }

 @Test
 public void testGetMethod() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 assertEquals("Alice", record.get(0));
 assertEquals("Smith", record.get(1));
 assertEquals("alice.smith@example.com", record.get(2));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMethodNoMapping() {
 CSVRecord record = new CSVRecord(testValues, null, null, 1);
 record.get("NonexistentColumn");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMethodInconsistentRecord() {
 CSVRecord record = new CSVRecord(new String[]{"Alice", "Smith", "alice.smith@example.com", "NonexistentValue"}, null, null, 1);
 record.get("Email");
 record.get("NonexistentColumn");
 }

 @Test
 public void testIsConsistent() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 assertTrue(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 assertTrue(record.isMapped("Name"));
 assertTrue(record.isMapped("Surname"));
 assertTrue(record.isMapped("Email"));
 }

 @Test
 public void testIsSet() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 assertTrue(record.isSet("Name"));
 assertTrue(record.isSet("Surname"));
 assertTrue(record.isSet("Email"));
 }

 @Test
 public void testIterator() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Iterator<String> iterator = record.iterator();
 assertEquals("Alice", iterator.next());
 assertEquals("Smith", iterator.next());
 assertEquals("alice.smith@example.com", iterator.next());
 }

 @Test
 public void testSize() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 assertEquals(3, record.size());
 }

 @Test
 public void testToString() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 assertEquals("[Alice, Smith, alice.smith@example.com]", record.toString());
 }

 @Test
 public void testGetComment() {
 CSVRecord record = new CSVRecord(testValues, testMapping, "A comment for this record", 1);
 assertEquals("A comment for this record", record.getComment());
 }
}