import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private String[] testValues = new String[]{"Name", "Age", "City"};
 private Map<String, Integer> testMapping = new java.util.HashMap<>();

 @Test
 public void testCSVRecordConstructor() {
 CSVRecord csvRecord = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertEquals(testValues, csvRecord.values());
 Assert.assertEquals(testMapping, csvRecord.mapping);
 Assert.assertEquals(null, csvRecord.comment);
 Assert.assertEquals(1, csvRecord.getRecordNumber());
 }

 @Test
 public void testGetMethod() {
 CSVRecord csvRecord = new CSVRecord(new String[]{"Name", "Age", "City"}, testMapping, null, 1);

 // Test getting value by index
 Assert.assertEquals("Name", csvRecord.get(0));
 Assert.assertEquals("Age", csvRecord.get(1));
 Assert.assertEquals("City", csvRecord.get(2));

 // Test getting value by name
 Assert.assertEquals("Name", csvRecord.get("Name"));
 Assert.assertEquals("Age", csvRecord.get("Age"));
 Assert.assertEquals("City", csvRecord.get("City"));

 // Test getting non-existent value by name
 Assert.assertEquals(null, csvRecord.get("NonExistentColumn"));
 }

 @Test
 public void testIsConsistentMethod() {
 CSVRecord csvRecord = new CSVRecord(new String[]{"Name", "Age", "City"}, testMapping, null, 1);
 Assert.assertTrue(csvRecord.isConsistent());

 CSVRecord inconsistentRecord = new CSVRecord(new String[]{"Name", "Age"}, testMapping, null, 1);
 Assert.assertFalse(inconsistentRecord.isConsistent());
 }

 @Test
 public void testIsMappedMethod() {
 CSVRecord csvRecord = new CSVRecord(new String[]{"Name", "Age", "City"}, testMapping, null, 1);

 Assert.assertTrue(csvRecord.isMapped("Name"));
 Assert.assertTrue(csvRecord.isMapped("Age"));
 Assert.assertTrue(csvRecord.isMapped("City"));

 Assert.assertFalse(csvRecord.isMapped("NonExistentColumn"));
 }

 @Test
 public void testIsSetMethod() {
 CSVRecord csvRecord = new CSVRecord(new String[]{"Name", "Age", "City"}, testMapping, null, 1);

 Assert.assertTrue(csvRecord.isSet("Name"));
 Assert.assertTrue(csvRecord.isSet("Age"));
 Assert.assertTrue(csvRecord.isSet("City"));

 CSVRecord notSetRecord = new CSVRecord(new String[]{"Name", "Age"}, testMapping, null, 1);

 Assert.assertFalse(notSetRecord.isSet("City"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMethodWithNoMapping() {
 CSVRecord csvRecord = new CSVRecord(new String[]{"Name", "Age", "City"}, null, null, 1);
 csvRecord.get("Name");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMethodWithInconsistentRecord() {
 CSVRecord csvRecord = new CSVRecord(new String[]{"Name", "Age", "City"}, testMapping, null, 1);
 testMapping.put("City", 3);
 csvRecord.get("City");
 }

}