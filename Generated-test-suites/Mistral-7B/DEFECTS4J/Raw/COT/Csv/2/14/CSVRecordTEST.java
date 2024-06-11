import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;

public class CSVRecordTest {

 private String[] testValues = new String[]{"John", "Doe", "john.doe@example.com", "123 Main St"};
 private Map<String, Integer> testMapping = Map.of("Name", 0, "Email", 1, "Address", 2);

 @Test
 public void testCSVRecordConstructionWithValuesAndMapping() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertArrayEquals(testValues, record.values());
 Assert.assertEquals(testMapping, record.mapping);
 Assert.assertNull(record.getComment());
 Assert.assertEquals(1, record.getRecordNumber());
 }

 @Test
 public void testGetMethodWithValidIndex() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertEquals("John", record.get(0));
 }

 @Test
 public void testGetMethodWithInvalidIndex() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertNull(record.get(-1));
 }

 @Test
 public void testGetMethodWithValidName() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertEquals("john.doe@example.com", record.get("Email"));
 }

 @Test
 public void testGetMethodWithInvalidName() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertNull(record.get("InvalidName"));
 }

 @Test
 public void testIsConsistentMethodWhenMappingIsNull() {
 CSVRecord record = new CSVRecord(testValues, null, null, 1);
 Assert.assertTrue(record.isConsistent());
 }

 @Test
 public void testIsConsistentMethodWhenMappingIsNotNullButSizeMismatch() {
 CSVRecord record = new CSVRecord(testValues, Map.of("Name", 0), null, 1);
 Assert.assertFalse(record.isConsistent());
 }

 @Test(expected = IllegalStateException.class)
 public void testGetMethodWithInvalidNameWhenNoMappingProvided() {
 CSVRecord record = new CSVRecord(testValues, null, null, 1);
 record.get("InvalidName");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetMethodWithInvalidIndexWhenRecordIsInconsistent() {
 CSVRecord record = new CSVRecord(testValues, Map.of("Name", 0), null, 1);
 record.values = new String[1];
 Assert.assertFalse(record.isConsistent());
 record.get(1);
 }

 @Test
 public void testIsMappedMethodWithValidName() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertTrue(record.isMapped("Email"));
 }

 @Test
 public void testIsMappedMethodWithInvalidName() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertFalse(record.isMapped("InvalidName"));
 }

 @Test
 public void testIsSetMethodWithValidName() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertTrue(record.isSet("Email"));
 }

 @Test
 public void testIsSetMethodWithInvalidName() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertFalse(record.isSet("InvalidName"));
 }

 @Test
 public void testIterableMethod() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Iterator<String> iterator = record.iterator();
 String expectedValue = testValues[0];
 Assert.assertTrue(iterator.hasNext());
 Assert.assertEquals(expectedValue, iterator.next());
 }

 @Test
 public void testSizeMethod() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 Assert.assertEquals(testValues.length, record.size());
 }

 @Test
 public void testToStringMethod() {
 CSVRecord record = new CSVRecord(testValues, testMapping, null, 1);
 String expectedToString = "[Name=John, Email=john.doe@example.com, Address=123 Main St]";
 Assert.assertEquals(expectedToString, record.toString());
 }
}