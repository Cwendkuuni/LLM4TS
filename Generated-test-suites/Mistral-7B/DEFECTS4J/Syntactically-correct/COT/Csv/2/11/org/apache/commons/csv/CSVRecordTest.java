import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private String[] testValues = new String[]{"John", "Doe", "john.doe@example.com", "123 Main St"};
 private Map<String, Integer> testMapping = Map.of("First Name", 0, "Last Name", 1, "Email", 2, "Address", 3);

 @Test
 public void testCSVRecordConstruction() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0);
 Assert.assertArrayEquals(testValues, record.values());
 Assert.assertEquals(testMapping, record.mapping);
 Assert.assertNull(record.comment);
 Assert.assertEquals(0, record.recordNumber);
 }

 @Test
 public void testGetMethod() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0);

 // Test getting value by index
 Assert.assertEquals("John", record.get(0));
 Assert.assertEquals("Doe", record.get(1));
 Assert.assertEquals("john.doe@example.com", record.get(2));
 Assert.assertEquals("123 Main St", record.get(3));

 // Test getting value by name
 Assert.assertEquals("John", record.get("First Name"));
 Assert.assertEquals("Doe", record.get("Last Name"));
 Assert.assertEquals("john.doe@example.com", record.get("Email"));
 Assert.assertEquals("123 Main St", record.get("Address"));

 Assert.assertNull(record.get("UnknownColumn"));
 }

 @Test
 public void testIsConsistentMethod() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0);
 Assert.assertTrue(record.isConsistent());

 // Test inconsistent record
 String[] inconsistentValues = testValues.clone();
 inconsistentValues[1] = null;
 CSVRecord inconsistentRecord = new CSVRecord(inconsistentValues, testMapping, null, 0);
 Assert.assertFalse(inconsistentRecord.isConsistent());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMethodWithoutMapping() {
 CSVRecord record = new CSVRecord(testValues, null, null, 0);
 record.get("First Name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMethodWithInvalidIndex() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0);
 record.get(-1);
 }

 @Test
 public void testIsMappedMethod() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0);
 Assert.assertTrue(record.isMapped("First Name"));
 Assert.assertTrue(record.isMapped("Last Name"));
 Assert.assertTrue(record.isMapped("Email"));
 Assert.assertTrue(record.isMapped("Address"));
 Assert.assertFalse(record.isMapped("UnknownColumn"));
 }

 @Test
 public void testIsSetMethod() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0);
 Assert.assertTrue(record.isSet("First Name"));
 Assert.assertTrue(record.isSet("Last Name"));
 Assert.assertTrue(record.isSet("Email"));
 Assert.assertTrue(record.isSet("Address"));
 Assert.assertFalse(record.isSet("UnknownColumn"));
 }

 @Test
 public void testIteratorMethod() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0);
 Iterator<String> iterator = record.iterator();
 Assert.assertEquals(testValues[0], iterator.next());
 Assert.assertEquals(testValues[1], iterator.next());
 Assert.assertEquals(testValues[2], iterator.next());
 Assert.assertEquals(testValues[3], iterator.next());
 Assert.assertFalse(iterator.hasNext());
 }

 @Test
 public void testSizeMethod() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0);
 Assert.assertEquals(testValues.length, record.size());
 }

 @Test
 public void testToStringMethod() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 0);
 Assert.assertEquals("[John, Doe, john.doe@example.com, 123 Main St]", record.toString());
 }

}