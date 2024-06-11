import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private CSVRecord record;

 @Test
 public void testCSVRecordConstructor() {
 String[] values = new String[]{"John", "Doe", "30", "john.doe@example.com"};
 Map<String, Integer> mapping = Map.of("Name", 0, "Age", 1, "Email", 2);
 String comment = "This is a comment";
 long recordNumber = 1;
 record = new CSVRecord(values, mapping, comment, recordNumber);

 Assert.assertArrayEquals(values, record.values());
 Assert.assertEquals(mapping, record.mapping);
 Assert.assertEquals(comment, record.getComment());
 Assert.assertEquals(recordNumber, record.getRecordNumber());
 }

 @Test
 public void testGetMethod() {
 record = new CSVRecord(new String[]{"John", "Doe", "30", "john.doe@example.com"}, null, null, 1);

 // Test getting value by index
 Assert.assertEquals("John", record.get(0));
 Assert.assertEquals("Doe", record.get(1));
 Assert.assertEquals("30", record.get(2));
 Assert.assertEquals("john.doe@example.com", record.get(3));

 // Test getting value by name
 Assert.assertEquals("John", record.get("Name"));
 Assert.assertEquals("Doe", record.get("Name")); // Should be the same as above
 Assert.assertEquals("30", record.get("Age"));
 Assert.assertEquals("john.doe@example.com", record.get("Email"));

 // Test getting non-existent value by name
 Assert.assertNull(record.get("NonExistentColumn"));
 }

 @Test
 public void testIsConsistent() {
 record = new CSVRecord(new String[]{"John", "Doe", "30", "john.doe@example.com"}, Map.of("Name", 0, "Age", 1, "Email", 2), null, 1);
 Assert.assertTrue(record.isConsistent());

 record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 Assert.assertTrue(record.isConsistent());

 record = new CSVRecord(new String[]{"John", "Doe", "30", "john.doe@example.com", "AnotherValue"}, Map.of("Name", 0, "Age", 1, "Email", 2), null, 1);
 Assert.assertFalse(record.isConsistent());
 }

 @Test
 public void testIsMapped() {
 record = new CSVRecord(new String[]{"John", "Doe", "30", "john.doe@example.com"}, Map.of("Name", 0, "Age", 1, "Email", 2), null, 1);

 // Test mapping existence
 Assert.assertTrue(record.isMapped("Name"));
 Assert.assertTrue(record.isMapped("Age"));
 Assert.assertTrue(record.isMapped("Email"));

 // Test mapping non-existence
 Assert.assertFalse(record.isMapped("NonExistentColumn"));
 }

 @Test
 public void testIsSet() {
 record = new CSVRecord(new String[]{"John", "Doe", "30", "john.doe@example.com"}, Map.of("Name", 0, "Age", 1, "Email", 2), null, 1);

 // Test set existence and value
 Assert.assertTrue(record.isSet("Name"));
 Assert.assertEquals("John", record.get("Name"));

 // Test set existence but no value
 record.mapping.put("Address", 3);
 Assert.assertFalse(record.isSet("Address"));
 Assert.assertNull(record.get("Address"));

 // Test set non-existence
 Assert.assertFalse(record.isSet("NonExistentColumn"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetWithNoMapping() {
 record = new CSVRecord(new String[]{"John", "Doe", "30"}, null, null, 1);
 record.get("Name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWithInconsistentRecord() {
 record = new CSVRecord(new String[]{"John", "Doe", "30"}, Map.of("Name", 0, "Age", 1, "Email", 3), null, 1);
 record.get("Email");
 }

}